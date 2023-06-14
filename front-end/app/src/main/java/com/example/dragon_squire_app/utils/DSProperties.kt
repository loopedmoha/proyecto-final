package com.example.dragon_squire_app.utils

import android.content.Context
import android.content.res.AssetManager
import java.io.InputStream
import java.util.*


object DSProperties {
    private val properties = Properties()
    var activeUrl: String = ""

    fun getAdress(context: Context): String {
        val properties = Properties()
        val assetManager: AssetManager = context.assets
        val inputStream: InputStream = assetManager.open("configuration.properties")
        properties.load(inputStream)
        if (properties.getProperty("dev").equals("true")) {
            activeUrl = properties.getProperty("local.url") + properties.getProperty("local.port")
            return activeUrl
        }
        activeUrl = properties.getProperty("aws.url") + properties.getProperty("aws.port")
        return activeUrl
    }

    fun getProperty(key: String?, context: Context): String? {
        val properties = Properties()
        val assetManager: AssetManager = context.assets
        val inputStream: InputStream = assetManager.open("configuration.properties")
        properties.load(inputStream)
        if (properties.getProperty("dev").equals("true")) {
            activeUrl = properties.getProperty("local.url") + properties.getProperty("local.port")
            return properties.getProperty(key)
        }
        activeUrl = properties.getProperty("aws.url") + properties.getProperty("aws.port")
        return properties.getProperty(key)
    }
}