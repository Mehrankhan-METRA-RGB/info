package com.mehran.plugin.flutter.info

import androidx.annotation.NonNull
import com.mehran.plugin.flutter.info.device.Device
import com.mehran.plugin.flutter.info.device.System

import io.flutter.embedding.engine.plugins.FlutterPlugin
import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel
import io.flutter.plugin.common.MethodChannel.MethodCallHandler
import io.flutter.plugin.common.MethodChannel.Result

/** InfoPlugin */
class InfoPlugin: FlutterPlugin, MethodCallHandler {
  /// The MethodChannel that will the communication between Flutter and native Android
  ///
  /// This local reference serves to register the plugin with the Flutter Engine and unregister it
  /// when the Flutter Engine is detached from the Activity
  private lateinit var channel : MethodChannel

  override fun onAttachedToEngine(@NonNull flutterPluginBinding: FlutterPlugin.FlutterPluginBinding) {
    channel = MethodChannel(flutterPluginBinding.binaryMessenger, "info-mehran")
    channel.setMethodCallHandler(this)
  }

  override fun onMethodCall(@NonNull call: MethodCall, @NonNull result: Result) {
    if (call.method == "SYSTEM") {
      result.success(System().info())
    } else if(call.method=="BATTERY"){
      result.success(" BATTERY COMING_SOON...")
    }
    else if(call.method=="DEVICE"){
      result.success(Device().info())
    }
    else if(call.method=="THERMAL"){
      result.success("THERMAL COMING_SOON...")
    }
    else if(call.method=="SOC"){
      result.success("SOC COMING_SOON...")
    }
    else if(call.method=="SENSORS"){
      result.success("SENSORS COMING_SOON...")
    }

    else{
      result.notImplemented()
    }
  }

  override fun onDetachedFromEngine(@NonNull binding: FlutterPlugin.FlutterPluginBinding) {
    channel.setMethodCallHandler(null)
  }
}
