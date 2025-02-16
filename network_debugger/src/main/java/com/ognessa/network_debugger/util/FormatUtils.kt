package com.ognessa.network_debugger.util

import com.google.gson.JsonParser
import okhttp3.Headers
import org.xml.sax.InputSource
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.util.Locale
import javax.xml.transform.OutputKeys
import javax.xml.transform.Source
import javax.xml.transform.Transformer
import javax.xml.transform.sax.SAXSource
import javax.xml.transform.sax.SAXTransformerFactory
import javax.xml.transform.stream.StreamResult

object FormatUtils {

    fun formatHeaders(httpHeaders: Headers): String {
        var out = ""
        val names = httpHeaders.names()

        names.forEachIndexed { index, s ->
            val value = httpHeaders.values(s).toString()
            out += "$s: $value" + if (names.size - 1 == index) "" else "\n\n"
        }

        return out
    }

    fun formatByteCount(bytes: Long, si: Boolean): String {
        val unit = if (si) 1000 else 1024
        if (bytes < unit) return "$bytes B"
        val exp = (Math.log(bytes.toDouble()) / Math.log(unit.toDouble())).toInt()
        val pre = (if (si) "kMGTPE" else "KMGTPE")[exp - 1] + (if (si) "" else "i")
        return String.format(
            Locale.US,
            "%.1f %sB",
            bytes / Math.pow(unit.toDouble(), exp.toDouble()),
            pre
        )
    }

    fun formatJson(json: String): String {
        try {
            val jp = JsonParser()
            val je = jp.parse(json)
            return JsonConvertor.getInstance().toJson(je);
        } catch (e: Exception) {
            return json
        }
    }

    fun formatXml(xml: String): String {
        try {
            val serializer: Transformer = SAXTransformerFactory.newInstance().newTransformer()
            serializer.setOutputProperty(OutputKeys.INDENT, "yes")
            serializer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2")
            val xmlSource: Source = SAXSource(InputSource(ByteArrayInputStream(xml.toByteArray())))
            val res = StreamResult(ByteArrayOutputStream())
            serializer.transform(xmlSource, res)
            return String((res.outputStream as ByteArrayOutputStream).toByteArray())
        } catch (e: Exception) {
            return xml
        }
    }
}