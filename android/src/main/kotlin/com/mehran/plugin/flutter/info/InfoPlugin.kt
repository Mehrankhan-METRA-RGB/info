package com.mehran.plugin.flutter.info

import androidx.annotation.NonNull
import android.content.*
import android.content.pm.PackageManager
import android.view.WindowManager
import com.mehran.plugin.flutter.info.device.Battery
import com.mehran.plugin.flutter.info.device.Connectivity
import com.mehran.plugin.flutter.info.device.Device
import com.mehran.plugin.flutter.info.device.System
import io.flutter.embedding.engine.plugins.FlutterPlugin
import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel.Result
import io.flutter.plugin.common.MethodChannel
import io.flutter.plugin.common.MethodChannel.MethodCallHandler

//import kotlin.coroutines.jvm.internal.CompletedContinuation.context

/** InfoPlugin */
open class InfoPlugin : FlutterPlugin, MethodCallHandler {
    /// The MethodChannel that will the communication between Flutter and native Android
    ///
    /// This local reference serves to register the plugin with the Flutter Engine and unregister it
    /// when the Flutter Engine is detached from the Activity
    private var battery = Battery()
    private var connectivity = Connectivity()
    private var device = Device()
    private var system = System()
    private lateinit var methodChannel: MethodChannel


    ///DEVICE
//    private var packageManager: PackageManager? = null
//    private var windowManager: WindowManager? = null


    override fun onAttachedToEngine(@NonNull flutterPluginBinding: FlutterPlugin.FlutterPluginBinding) {
        battery.applicationContext = flutterPluginBinding.applicationContext
        connectivity.applicationContext = flutterPluginBinding.applicationContext

//            //   DEVICE
//            packageManager  = flutterPluginBinding.applicationContext.packageManager
//            windowManager = flutterPluginBinding.applicationContext.getSystemService(Context.WINDOW_SERVICE) as WindowManager

        methodChannel = MethodChannel(flutterPluginBinding.binaryMessenger, "info-mehran")
        methodChannel.setMethodCallHandler(this)
        device.onAttachedEngine(flutterPluginBinding)
        connectivity.onAttachedEngine(flutterPluginBinding)
        battery.onAttachEngine(flutterPluginBinding)
        system.onAttachedEngine(flutterPluginBinding)


    }

    override fun onMethodCall(
        @NonNull call: MethodCall,
        @NonNull result: Result,
    ) {
        when (call.method) {
            "SYSTEM" -> {
                result.success(System().info())
            }
            "BATTERY" -> {

                result.success(Battery())
            }
            "CONNECTIVITY" -> {

                result.success(connectivity.networkType())
            }
            "DEVICE" -> {
                result.success(device.info())
            }
            "THERMAL" -> {
                result.success("THERMAL COMING_SOON...")
            }
            "SOC" -> {
                result.success("SOC COMING_SOON...")
            }
            "SENSORS" -> {
                result.success("SENSORS COMING_SOON...")
            }
            else -> {
                result.notImplemented()
            }
        }
    }

    override fun onDetachedFromEngine(@NonNull binding: FlutterPlugin.FlutterPluginBinding) {

        methodChannel.setMethodCallHandler(null)
        connectivity.onDettachEngine(binding)
        battery.onDettachEngine(binding)
        device.onDettachEngine(binding)
        system.onDettachEngine(binding)
    }


}
