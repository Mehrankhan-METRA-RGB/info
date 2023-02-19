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
import io.flutter.plugin.common.MethodChannel.Result
import io.flutter.plugin.common.EventChannel
import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel
import io.flutter.plugin.common.MethodChannel.MethodCallHandler

//import kotlin.coroutines.jvm.internal.CompletedContinuation.context

/** InfoPlugin */
open class InfoPlugin: FlutterPlugin, MethodCallHandler{
  /// The MethodChannel that will the communication between Flutter and native Android
  ///
  /// This local reference serves to register the plugin with the Flutter Engine and unregister it
  /// when the Flutter Engine is detached from the Activity
  private  var battery=Battery()
  private  var connectivity= Connectivity()
//  public var applicationContext: Context? = battery.applicationContext
//  public var chargingStateChangeReceiver: BroadcastReceiver? = battery.chargingStateChangeReceiver
  private lateinit var methodChannel : MethodChannel
  private lateinit var eventChannel : EventChannel
  private lateinit var connectivityEventChannel : EventChannel

///DEVICE
private var packageManager: PackageManager?=null
 private var windowManager: WindowManager?=null


        override fun onAttachedToEngine(@NonNull flutterPluginBinding: FlutterPlugin.FlutterPluginBinding) {
//             BATTERY
            battery.applicationContext = flutterPluginBinding.applicationContext
//            CONNECTIVITY
            connectivity.applicationContext = flutterPluginBinding.applicationContext
            //   DEVICE
            packageManager  = flutterPluginBinding.applicationContext.packageManager
            windowManager = flutterPluginBinding.applicationContext.getSystemService(Context.WINDOW_SERVICE) as WindowManager

            methodChannel = MethodChannel(flutterPluginBinding.binaryMessenger, "info-mehran")
            methodChannel.setMethodCallHandler(this)


//            BATTERY,CONNECTIVITY
    eventChannel = EventChannel(flutterPluginBinding.binaryMessenger, "info-event-mehran")
//            connectivityEventChannel = EventChannel(flutterPluginBinding.binaryMessenger, "info-event-mehran-connectivity")
//    eventChannel.setStreamHandler(this)
//            connectivityEventChannel.setStreamHandler(this)
connectivity.onAttachedToEngineConnectivity(flutterPluginBinding)


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

            result.success(connectivity.getNetworkType())
        }
        "DEVICE" -> {
          result.success(Device(packageManager!!,windowManager!!).info())
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
//     methodChannel=null
    eventChannel.setStreamHandler(null)
    connectivityEventChannel.setStreamHandler(null)
//   eventChannel=null

  }

//  override fun onListen(arguments: Any?, events: EventChannel.EventSink) {
//      print(" L I S T E N");
////Battery listen
////battery.onListen(arguments,events)
////Connectivity Listen
////      connectivity.onListen(arguments,events)
//
//  }
//
//  override fun onCancel(arguments: Any?) {
//      print(" C A N C E L");
////Battery Cancel
////battery.onCancel(arguments)
////      Connectivity Cancel
////connectivity.onCancel(arguments)
//
//  }


}
