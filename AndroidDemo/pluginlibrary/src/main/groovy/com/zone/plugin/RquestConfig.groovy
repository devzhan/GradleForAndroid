package com.zone.plugin

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction
import org.gradle.internal.impldep.com.google.api.client.http.HttpResponse
import org.gradle.internal.impldep.org.apache.http.client.methods.HttpGet
import org.gradle.internal.impldep.org.apache.http.impl.client.CloseableHttpClient
import org.gradle.internal.impldep.org.apache.http.impl.client.DefaultHttpClient
import org.w3c.dom.Document


class RequestConfig extends DefaultTask {
    @TaskAction
    def request() {
        println 'request config from server'
        getConfig()
    }


    def getConfig(){
        String domain = 'https://juejin.im/post/5a523dd56fb9a01cbf382ce9'
        HttpURLConnection connection = new URL(domain).openConnection()
        connection.setDoInput(true)
        connection.setConnectTimeout(15000)
        connection.setReadTimeout(15000)
        connection.setRequestMethod("GET")
        connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8")
        def resultText =    connection.inputStream.text
        println(resultText)
    }
}