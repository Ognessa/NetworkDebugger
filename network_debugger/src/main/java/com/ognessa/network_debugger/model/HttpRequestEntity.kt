package com.ognessa.network_debugger.model

import okhttp3.Headers
import java.util.UUID

internal class HttpRequestEntity {

    enum class Status {
        Requested,
        Complete,
        Failed
    }

    private val id: String = UUID.randomUUID().toString()

    //request
    private var requestDate: String = "" //yyyy-MM-dd HH:mm:ss.SSS
    private var requestMethod: String = "" //GET, POST etc
    private var requestUrl: String = ""
    private var requestHeaders: Headers? = null
    private var requestContentType: String = ""
    private var requestContentLength: Long = 0L
    private var requestBodyIsPlainText: Boolean = false
    private var requestBody: String = ""

    //response
    private var error: Exception? = null
    private var responseDate: String = "" //yyyy-MM-dd HH:mm:ss.SSS
    private var responseTookMs: Long = 0L
    private var responseProtocol: String = ""
    private var responseCode: Int = 0
    private var responseMessage: String = ""
    private var responseContentLength: Long = 0L
    private var responseContentType: String = ""
    private var responseHeaders: Headers? = null
    private var responseBodyIsPlainText: Boolean = false
    private var responseBody: String = ""

    //request
    fun setRequestDate(date: String) {
        requestDate = date
    }

    fun getRequestDate(): String = requestDate

    fun setRequestMethod(method: String) {
        requestMethod = method
    }

    fun getRequestMethod(): String = requestMethod

    fun setRequestUrl(url: String) {
        requestUrl = url
    }

    fun getRequestUrl(): String = requestUrl

    fun setRequestHeaders(headers: Headers) {
        requestHeaders = headers
    }

    fun getRequestHeaders(): Headers? = requestHeaders

    fun setRequestContentType(type: String) {
        requestContentType = type
    }

    fun getRequestContentType(): String = requestContentType

    fun setRequestContentLength(length: Long) {
        requestContentLength = length
    }

    fun setRequestContentLength(): Long = requestContentLength

    fun setRequestBodyIsPlainText(isPlain: Boolean) {
        requestBodyIsPlainText = isPlain
    }

    fun getRequestBodyIsPlainText(): Boolean = requestBodyIsPlainText

    fun setRequestBody(body: String) {
        requestBody = body
    }

    fun setRequestBody(): String = requestBody

    //response
    fun setError(error: Exception) {
        this.error = error
    }

    fun getError(): Exception? = error

    fun setResponseDate(date: String) {
        responseDate = date
    }

    fun getResponseDate(): String = responseDate

    fun setTookMs(millis: Long) {
        responseTookMs = millis
    }

    fun getTookMs(): Long = responseTookMs

    fun setProtocol(protocol: String) {
        responseProtocol = protocol
    }

    fun getProtocol(): String = responseProtocol

    fun setResponseCode(code: Int) {
        responseCode = code
    }

    fun getResponseCode(): Int = responseCode

    fun setResponseMessage(message: String) {
        responseMessage = message
    }

    fun getResponseMessage(): String = responseMessage

    fun setResponseContentLength(length: Long?) {
        responseContentLength = length ?: 0L
    }

    fun getResponseContentLength(): Long = responseContentLength

    fun setResponseContentType(type: String) {
        responseContentType = type
    }

    fun getResponseContentType(): String = responseContentType

    fun setResponseHeaders(headers: Headers) {
        responseHeaders = headers
    }

    fun getResponseHeaders(): Headers? = responseHeaders

    fun setResponseBodyIsPlainText(boolean: Boolean) {
        responseBodyIsPlainText = boolean
    }

    fun setResponseBodyIsPlainText(): Boolean = responseBodyIsPlainText

    fun setResponseBody(body: String) {
        responseBody = body
    }

    fun getResponseBody(): String = responseBody

    //other
    fun getStatus(): Status {
        return when {
            error != null -> Status.Failed
            responseCode == 0 -> Status.Requested
            else -> Status.Complete
        }
    }
}
