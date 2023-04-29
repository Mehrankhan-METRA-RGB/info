@file:Suppress("DEPRECATION")

package com.mehran.plugin.flutter.info.device

import android.content.*
import android.net.ConnectivityManager
import android.net.ConnectivityManager.NetworkCallback
import android.net.Network
import android.net.NetworkCapabilities
import android.os.*
import io.flutter.plugin.common.EventChannel
import java.util.*
import io.flutter.embedding.engine.plugins.FlutterPlugin
import androidx.annotation.NonNull


class Connectivity : BroadcastReceiver(), EventChannel.StreamHandler {
    public var applicationContext: Context? = null
    val NONE = "none"
    val WIFI = "wifi"
    val MOBILE = "mobile"
    val ETHERNET = "ethernet"
    val BLUETOOTH = "bluetooth"
    val VPN = "vpn"
    lateinit var connectivityManager: ConnectivityManager
    lateinit var events: EventChannel.EventSink
    private val mainHandler = Handler(Looper.getMainLooper())
    private var networkCallback: ConnectivityManager.NetworkCallback? = null
    val CONNECTIVITY_ACTION = "android.net.conn.CONNECTIVITY_CHANGE"
    private lateinit var eventChannel: EventChannel


    override public fun onListen(arguments: Any?, events: EventChannel.EventSink) {
        this.events = events;


        when {
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.N -> {
                networkCallback = object : NetworkCallback() {
                    override fun onAvailable(network: Network) {
                        sendEvent(events)
                    }

                    override fun onLost(network: Network) {
                        sendEvent(NONE, events)
                    }
                }

                connectivityManager.registerDefaultNetworkCallback(networkCallback!!)
            }
            else -> {

                applicationContext!!.registerReceiver(this, IntentFilter(CONNECTIVITY_ACTION))
            }
        }
    }


    override public fun onCancel(arguments: Any?) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            if (networkCallback != null) {
                connectivityManager.unregisterNetworkCallback(networkCallback!!)
                networkCallback = null
            }
        } else {
            try {
                applicationContext!!.unregisterReceiver(this)

            } catch (e: Exception) {
                //listen never called, ignore the error
            }
        }
    }

    /**
     * Running for updated devices
     * updated or equal to marshmellow android OS*/
    public fun networkType(): String? {
        when {
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.M -> {
                val network = connectivityManager.activeNetwork
                val capabilities = connectivityManager.getNetworkCapabilities(network)
                    ?: return NONE
                when {
                    capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> {
                        return WIFI
                    }
                    capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> {
                        return ETHERNET
                    }
                    capabilities.hasTransport(NetworkCapabilities.TRANSPORT_VPN) -> {
                        return VPN
                    }
                    capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> {
                        return MOBILE
                    }
                    capabilities.hasTransport(NetworkCapabilities.TRANSPORT_BLUETOOTH) -> {
                        return BLUETOOTH
                    }
                }
            }
        }
        return legacyNetworks()
    }


    /**
    This method runs for old Android devices
    Lower than Marshmellow
    or
    handle type for Android versions less than Android 6
     */
    private fun legacyNetworks(): String? {
        val info = connectivityManager.activeNetworkInfo
        if (info == null || !info.isConnected) {
            return NONE
        }
        val type = info.type
        return when (type) {
            ConnectivityManager.TYPE_BLUETOOTH -> BLUETOOTH
            ConnectivityManager.TYPE_ETHERNET -> ETHERNET
            ConnectivityManager.TYPE_WIFI, ConnectivityManager.TYPE_WIMAX -> WIFI
            ConnectivityManager.TYPE_VPN -> VPN
            ConnectivityManager.TYPE_MOBILE, ConnectivityManager.TYPE_MOBILE_DUN, ConnectivityManager.TYPE_MOBILE_HIPRI -> MOBILE
            else -> NONE
        }
    }

    private fun sendEvent(events: EventChannel.EventSink) {
        val runnable = Runnable { events.success(networkType()) }
        mainHandler.post(runnable)
    }

    private fun sendEvent(networkType: String, events: EventChannel.EventSink) {
        val runnable = Runnable { events.success(networkType) }
        mainHandler.post(runnable)
    }

    override fun onReceive(p0: Context?, p1: Intent?) {
        if (events != null) {
            events.success(networkType());
        }
    }


    public fun onAttachedEngine(@NonNull flutterPluginBinding: FlutterPlugin.FlutterPluginBinding) {
        var context: Context = flutterPluginBinding.getApplicationContext()
        connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        applicationContext = flutterPluginBinding.applicationContext
        eventChannel =
            EventChannel(flutterPluginBinding.binaryMessenger, "info-event-mehran-connectivity")
        eventChannel.setStreamHandler(this)


    }

    public fun onDettachEngine(@NonNull flutterPluginBinding: FlutterPlugin.FlutterPluginBinding) {

        eventChannel.setStreamHandler(null)

    }
}