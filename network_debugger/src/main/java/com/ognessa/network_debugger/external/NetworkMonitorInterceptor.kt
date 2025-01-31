package com.ognessa.network_debugger.external

import android.content.Context
import android.util.Log
import com.ognessa.network_debugger.R
import com.ognessa.network_debugger.model.HttpRequestEntity
import com.ognessa.network_debugger.storage.RequestsStorage
import com.ognessa.network_debugger.util.DebuggerTimeUtil
import okhttp3.Headers
import okhttp3.Interceptor
import okhttp3.Response
import okhttp3.internal.http.HttpHeaders
import okio.BufferedSource
import okio.GzipSource
import okio.Okio
import okio.Buffer
import java.io.EOFException
import java.nio.charset.Charset
import java.nio.charset.UnsupportedCharsetException
import java.util.concurrent.TimeUnit
import kotlin.math.min

class NetworkMonitorInterceptor(
    private val context: Context
) : Interceptor {

    private val LOG_TAG = "NetworkMonitorInter..."
    private val UTF8 = Charset.forName("UTF-8")

    private val maxContentLength: Long = 250000L

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()

        val requestBody = request.body()
        val hasRequestBody = requestBody != null

        val entity = HttpRequestEntity()

        entity.setRequestDate(DebuggerTimeUtil.getTime())
        entity.setRequestMethod(request.method())
        entity.setRequestUrl(request.url().toString())
        entity.setRequestHeaders(request.headers())

        if (hasRequestBody) {
            if (requestBody?.contentType() != null) {
                entity.setRequestContentType(requestBody.contentType().toString())
            }

            val contentLength = requestBody?.contentLength() ?: -1
            if (contentLength != -1L) {
                entity.setRequestContentLength(contentLength)
            }
        }

        val requestBodyIsPlainText = bodyHasUnsupportedEncoding(request.headers()).not()
        entity.setRequestBodyIsPlainText(requestBodyIsPlainText)

        if (hasRequestBody && requestBodyIsPlainText) {
            val source: BufferedSource =
                getNativeSource(Buffer(), bodyGzipped(request.headers()))
            val buffer = source.buffer
            requestBody?.writeTo(buffer)
            var charset = UTF8
            val contentType = requestBody?.contentType()
            if (contentType != null) {
                charset = contentType.charset(UTF8)
            }
            if (isPlaintext(buffer)) {
                entity.setRequestBody(readFromBuffer(buffer, charset))
            } else {
                entity.setRequestBodyIsPlainText(false)
            }
        }

        val startNs = System.nanoTime()
        var response: Response? = null
        try {
            response = chain.proceed(request)
        } catch (e: Exception) {
            entity.setError(e)
            throw e
        }
        val tookMs = TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - startNs)

        val responseBody = response.body()

        entity.setRequestHeaders(
            response.request().headers()
        ) // includes headers added later in the chain
        entity.setResponseDate(DebuggerTimeUtil.getTime())
        entity.setTookMs(tookMs)
        entity.setProtocol(response.protocol().toString())
        entity.setResponseCode(response.code())
        entity.setResponseMessage(response.message())

        val contentLength = responseBody?.contentLength()
        entity.setResponseContentLength(contentLength)
        if (contentLength != null) {
            entity.setResponseContentType(responseBody.contentType().toString())
        }
        entity.setResponseHeaders(response.headers())

        val responseBodyIsPlainText = bodyHasUnsupportedEncoding(response.headers()).not()
        entity.setResponseBodyIsPlainText(responseBodyIsPlainText)
        if (HttpHeaders.hasBody(response) && responseBodyIsPlainText) {
            val source = getNativeSource(response)
            source.request(Long.MAX_VALUE)
            val buffer = source.buffer
            var charset = UTF8
            val contentType = responseBody?.contentType()
            if (contentType != null) {
                try {
                    charset = contentType.charset(UTF8)
                } catch (e: UnsupportedCharsetException) {
                    return response
                }
            }
            if (isPlaintext(buffer)) {
                entity.setResponseBody(readFromBuffer(buffer.clone(), charset))
            } else {
                entity.setResponseBodyIsPlainText(false)
            }
            entity.setResponseContentLength(buffer.size())
        }

        RequestsStorage.addHttpRequest(context, entity)

        return response
    }

    private fun isPlaintext(buffer: Buffer): Boolean {
        try {
            val prefix = Buffer()
            val byteCount = if (buffer.size() < 64) buffer.size() else 64
            buffer.copyTo(prefix, 0, byteCount)
            for (i in 0..15) {
                if (prefix.exhausted()) {
                    break
                }
                val codePoint = prefix.readUtf8CodePoint()
                if (Character.isISOControl(codePoint) && Character.isWhitespace(codePoint).not()) {
                    return false
                }
            }
            return true
        } catch (e: EOFException) {
            return false // Truncated UTF-8 sequence.
        }
    }

    private fun bodyHasUnsupportedEncoding(headers: Headers): Boolean {
        val contentEncoding = headers.get("Content-Encoding")
        return contentEncoding != null &&
                !contentEncoding.equals("identity", true) &&
                !contentEncoding.equals("gzip", true)
    }

    private fun bodyGzipped(headers: Headers): Boolean {
        val contentEncoding = headers.get("Content-Encoding")
        return "gzip".equals(contentEncoding, true)
    }

    private fun readFromBuffer(buffer: Buffer, charset: Charset): String {
        val bufferSize = buffer.size()
        val maxBytes = min(bufferSize, maxContentLength)
        var body = ""
        try {
            body = buffer.readString(maxBytes, charset)
        } catch (e: EOFException) {
            body += context.getString(R.string.body_unexpected_eof)
        }
        if (bufferSize > maxContentLength) {
            body += context.getString(R.string.body_content_truncated)
        }
        return body
    }

    private fun getNativeSource(input: BufferedSource, isGzipped: Boolean): BufferedSource {
        if (isGzipped) {
            val source = GzipSource(input)
            return Okio.buffer(source)
        } else {
            return input
        }
    }

    private fun getNativeSource(response: Response): BufferedSource {
        if (bodyGzipped(response.headers())) {
            val source = response.peekBody(maxContentLength).source()
            if (source.buffer.size() < maxContentLength) {
                return getNativeSource(source, true)
            } else {
                Log.w(LOG_TAG, "gzip encoded response was too long")
            }
        }
        return response.body()?.source() ?: getNativeSource(
            Buffer(),
            bodyGzipped(response.headers())
        )
    }
}