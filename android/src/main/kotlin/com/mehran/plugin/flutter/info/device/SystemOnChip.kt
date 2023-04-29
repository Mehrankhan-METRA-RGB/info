package com.mehran.plugin.flutter.info.device

import android.app.ActivityManager
import android.content.Context
import android.content.Context.ACTIVITY_SERVICE
import io.flutter.embedding.engine.plugins.FlutterPlugin
import androidx.annotation.NonNull
class SystemOnChip {
    lateinit var context: Context
    lateinit  var activityManager:ActivityManager
    var memoryInfo: ActivityManager.MemoryInfo = ActivityManager.MemoryInfo()

public fun getRam(){
    activityManager.getMemoryInfo(memoryInfo);
// var    availableMegs: Long = memoryInfo.availMem / 0x100000L;
activityManager.deviceConfigurationInfo.reqGlEsVersion
//Percentage can be calculated for API 16+
 var    pevarrcentAvail:Long = memoryInfo.availMem / memoryInfo.totalMem * 100.0.toLong()

}




    public fun onAttachedEngine(@NonNull flutterPluginBinding: FlutterPlugin.FlutterPluginBinding) {
         context = flutterPluginBinding.getApplicationContext()
      activityManager  = context.getSystemService(ACTIVITY_SERVICE) as ActivityManager;


    }

    public fun onDettachEngine(@NonNull flutterPluginBinding: FlutterPlugin.FlutterPluginBinding) {



    }
}