package com.mehran.plugin.flutter.info.device

import android.content.*
import android.net.ConnectivityManager
import android.net.ConnectivityManager.NetworkCallback
import android.net.Network
import android.net.NetworkCapabilities
import android.os.*
import android.view.WindowManager
import io.flutter.plugin.common.EventChannel
import java.util.*
import io.flutter.embedding.engine.plugins.FlutterPlugin
import androidx.annotation.NonNull


 class Connectivity: BroadcastReceiver(), EventChannel.StreamHandler {
    public var applicationContext: Context? = null
//    public var broadcastReceiver: BroadcastReceiver? = null
    val CONNECTIVITY_NONE = "none"
    val CONNECTIVITY_WIFI = "wifi"
    val CONNECTIVITY_MOBILE = "mobile"
    val CONNECTIVITY_ETHERNET = "ethernet"
    val CONNECTIVITY_BLUETOOTH = "bluetooth"
    val CONNECTIVITY_VPN = "vpn"
     lateinit var connectivityManager: ConnectivityManager
//    private var connectivity: Connectivity? = null
    lateinit var events: EventChannel.EventSink
    private val mainHandler = Handler(Looper.getMainLooper())
    private var networkCallback:  ConnectivityManager.NetworkCallback? = null
    val CONNECTIVITY_ACTION = "android.net.conn.CONNECTIVITY_CHANGE"
     private lateinit var connectivityEventChannel : EventChannel


        ///TODO:call in InfoPlugin method onListen
    override    public  fun onListen(arguments: Any?, events: EventChannel.EventSink) {
            this.events = events;


            when {
                Build.VERSION.SDK_INT >= Build.VERSION_CODES.N -> {
                    networkCallback = object : NetworkCallback() {
                        override fun onAvailable(network: Network) {
                            sendEvent(events)
                        }

                        override fun onLost(network: Network) {
                            sendEvent(CONNECTIVITY_NONE,events)
                        }
                    }

                    connectivityManager.registerDefaultNetworkCallback(networkCallback!!)
                }
                else -> {
//                    broadcastReceiver=  object : BroadcastReceiver() {
//                        override fun onReceive(context: Context, intent: Intent) {
//                            val change = intent.getIntExtra(CONNECTIVITY_ACTION, -1)
//                            events.success(connectivity!!.getNetworkType())
//                        }
//                    }

                    applicationContext!!.registerReceiver(this, IntentFilter(CONNECTIVITY_ACTION))
                }
            }
        }


        ///TODO:call in InfoPlugin method onCancel
      override  public fun onCancel(arguments: Any?) {
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


   public fun getNetworkType(): String? {
        when {
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.M -> {
                val network = connectivityManager.activeNetwork
                val capabilities = connectivityManager.getNetworkCapabilities(network)
                    ?: return CONNECTIVITY_NONE
                when {
                    capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> {
                        return CONNECTIVITY_WIFI
                    }
                    capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> {
                        return CONNECTIVITY_ETHERNET
                    }
                    capabilities.hasTransport(NetworkCapabilities.TRANSPORT_VPN) -> {
                        return CONNECTIVITY_VPN
                    }
                    capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> {
                        return CONNECTIVITY_MOBILE
                    }
                    capabilities.hasTransport(NetworkCapabilities.TRANSPORT_BLUETOOTH) -> {
                        return CONNECTIVITY_BLUETOOTH
                    }
                }
            }
        }
        return getNetworkTypeLegacy()
    }


////BROADCAST RECIEVER
//    public fun changeReceiver(events: EventChannel.EventSink): BroadcastReceiver {
//        return object : BroadcastReceiver() {
//            override fun onReceive(context: Context, intent: Intent) {
//                events.success(getNetworkType());
//
//            }
//        }
//    }




    private fun getNetworkTypeLegacy(): String? {
        // handle type for Android versions less than Android 6
        val info = connectivityManager.activeNetworkInfo
        if (info == null || !info.isConnected) {
            return CONNECTIVITY_NONE
        }
        val type = info.type
        return when (type) {
            ConnectivityManager.TYPE_BLUETOOTH -> CONNECTIVITY_BLUETOOTH
            ConnectivityManager.TYPE_ETHERNET -> CONNECTIVITY_ETHERNET
            ConnectivityManager.TYPE_WIFI, ConnectivityManager.TYPE_WIMAX -> CONNECTIVITY_WIFI
            ConnectivityManager.TYPE_VPN -> CONNECTIVITY_VPN
            ConnectivityManager.TYPE_MOBILE, ConnectivityManager.TYPE_MOBILE_DUN, ConnectivityManager.TYPE_MOBILE_HIPRI -> CONNECTIVITY_MOBILE
            else -> CONNECTIVITY_NONE
        }
    }
    private fun sendEvent(events:EventChannel.EventSink) {
        val runnable = Runnable { events.success(getNetworkType()) }
        mainHandler.post(runnable)
    }

    private fun sendEvent(networkType: String,events:EventChannel.EventSink) {
        val runnable = Runnable { events.success(networkType) }
        mainHandler.post(runnable)
    }

     override fun onReceive(p0: Context?, p1: Intent?) {
         if (events != null) {
             events.success(getNetworkType());
         }
         TODO("Not yet implemented")
     }


     public fun onAttachedToEngineConnectivity(@NonNull flutterPluginBinding: FlutterPlugin.FlutterPluginBinding) {
         var context:Context=flutterPluginBinding.getApplicationContext()
         connectivityManager=
             context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
       applicationContext = flutterPluginBinding.applicationContext
         connectivityEventChannel = EventChannel(flutterPluginBinding.binaryMessenger, "info-event-mehran-connectivity")
         connectivityEventChannel.setStreamHandler(this)



     }
 }