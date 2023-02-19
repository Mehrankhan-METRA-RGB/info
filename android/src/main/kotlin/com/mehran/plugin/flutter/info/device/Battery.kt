package com.mehran.plugin.flutter.info.device

import android.content.*
import android.net.ConnectivityManager
import android.os.BatteryManager
import android.os.*
import android.os.PowerManager
import android.provider.Settings
import androidx.annotation.RequiresApi
import io.flutter.plugin.common.EventChannel
import java.util.*
import io.flutter.embedding.engine.plugins.FlutterPlugin
import androidx.annotation.NonNull
//import kotlin.coroutines.CoroutineContext

//import android.os.BatteryManager

class Battery:BroadcastReceiver(), EventChannel.StreamHandler{
//    public  var  receiver:BroadcastReceiver?=null
////    public var applicationContext: Context? = null
//public var chargingStateChangeReceiver: BroadcastReceiver? = null
public var applicationContext: Context? = null
    public var chargingStateChangeReceiver: BroadcastReceiver? = null

 ///TODO:call in InfoPlugin method onListen
  override public  fun onListen(arguments: Any?, events: EventChannel.EventSink) {
     print(" B A T T E R Y    P R I N T ");
        chargingStateChangeReceiver = changeReceiver(events)
        applicationContext?.registerReceiver(chargingStateChangeReceiver, IntentFilter(Intent.ACTION_BATTERY_CHANGED))
//        val status = getBatteryStatus()
//        val level = getBatteryLevel()
//        val power = isInPowerSaveMode()
//
//        updateDetails(events,"""{
//            "status":$status,
//            "level":$level,
//            "power":$power,
//            }""".trimMargin())
    }
    ///TODO:call in InfoPlugin method onCancel
  override  public fun onCancel(arguments: Any?) {
        print(" B A T T E R Y    CAncel");
        applicationContext!!.unregisterReceiver(chargingStateChangeReceiver)
        chargingStateChangeReceiver = null
    }

    override fun onReceive(p0: Context?, p1: Intent?) {
        TODO("Not yet implemented")
    }

    public fun getBatteryStatus(): String? {
        val status: Int = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            getBatteryProperty(BatteryManager.BATTERY_PROPERTY_STATUS)
        } else {
            val intent = ContextWrapper(applicationContext).registerReceiver(null, IntentFilter(Intent.ACTION_BATTERY_CHANGED))
            intent?.getIntExtra(BatteryManager.EXTRA_STATUS, -1) ?: -1
        }
        return convertBatteryStatus(status)
    }

    public fun getBatteryLevel(): Int {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getBatteryProperty(BatteryManager.BATTERY_PROPERTY_CAPACITY)
        } else {
            val intent = ContextWrapper(applicationContext).registerReceiver(null, IntentFilter(Intent.ACTION_BATTERY_CHANGED))
            val level = intent!!.getIntExtra(BatteryManager.EXTRA_LEVEL, -1)
            val scale = intent.getIntExtra(BatteryManager.EXTRA_SCALE, -1)
          return  (level * 100 / scale)
        }
    }

    public fun isInPowerSaveMode(): Boolean? {
        val deviceManufacturer = Build.MANUFACTURER.lowercase(Locale.getDefault())

        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            when (deviceManufacturer) {
                "xiaomi" -> isXiaomiPowerSaveModeActive()
                "huawei" -> isHuaweiPowerSaveModeActive()
                "samsung" -> isSamsungPowerSaveModeActive()
                else -> {
                    val powerManager = applicationContext!!.getSystemService(Context.POWER_SERVICE) as PowerManager
                    powerManager.isPowerSaveMode
                }
            }
        } else {
            null
        }
    }

    public fun isSamsungPowerSaveModeActive(): Boolean {
        val mode = Settings.System.getString(applicationContext!!.contentResolver, "psm_switch")
        return if (mode == null && Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val powerManager = applicationContext!!.getSystemService(Context.POWER_SERVICE) as PowerManager
            powerManager.isPowerSaveMode
        } else {
            POWER_SAVE_MODE_SAMSUNG == mode
        }
    }

    public fun isHuaweiPowerSaveModeActive(): Boolean? {
        val mode = Settings.System.getInt(applicationContext!!.contentResolver, "SmartModeStatus", -1)
        return if (mode != -1) {
            mode == POWER_SAVE_MODE_HUAWEI
        } else {
            null
        }
    }

    public fun isXiaomiPowerSaveModeActive(): Boolean? {
        val mode = Settings.System.getInt(applicationContext!!.contentResolver, "POWER_SAVE_MODE_OPEN", -1)
        return if (mode != -1) {
            mode == POWER_SAVE_MODE_XIAOMI
        } else {
            null
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public fun getBatteryProperty(property: Int): Int {
        val batteryManager = applicationContext!!.getSystemService(Context.BATTERY_SERVICE) as BatteryManager
        return batteryManager.getIntProperty(property)
    }

    public fun changeReceiver(events: EventChannel.EventSink): BroadcastReceiver {
        return object : BroadcastReceiver() {
            override fun onReceive(context: Context, intent: Intent) {
                val status = intent.getIntExtra(BatteryManager.EXTRA_STATUS, -1)
//                val power = intent.getIntExtra(BatteryManager.EX, -1)
                val level = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1)
                val tech = intent.getIntExtra(BatteryManager.EXTRA_TECHNOLOGY, -1)
                val health = intent.getIntExtra(BatteryManager.EXTRA_HEALTH, -1)
                val temp = intent.getIntExtra(BatteryManager.EXTRA_TEMPERATURE, -1)
                val voltage = intent.getIntExtra(BatteryManager.EXTRA_VOLTAGE, -1)
                val plugged = intent.getIntExtra(BatteryManager.EXTRA_PLUGGED, -1)
                val scale = intent.getIntExtra(BatteryManager.EXTRA_SCALE, -1)

var power=isInPowerSaveMode()

                updateDetails(events,"""{
            "status":${convertBatteryStatus(status)},
            "level":$level,
            "technology":$tech,
            "health":${convertBatteryHealth(health)},
            "temprature":$temp,
            "plugged":$plugged,
            "voltage":$voltage,
            "scale":$scale,
            "power":$power,
            }""".trimMargin())
            }
        }
    }

    public fun convertBatteryHealth(health:Int):String?{
        return when (health){
            BatteryManager.BATTERY_HEALTH_COLD->"Cold"
            BatteryManager.BATTERY_HEALTH_DEAD->"DEAD"
            BatteryManager.BATTERY_HEALTH_OVERHEAT->"OVER-HEATED"
            BatteryManager.BATTERY_HEALTH_GOOD->"GOOD"
            BatteryManager.BATTERY_HEALTH_UNKNOWN->"UNKNOWN"
            BatteryManager.BATTERY_HEALTH_OVER_VOLTAGE->"OVER-VOLTAGE"
            BatteryManager.BATTERY_HEALTH_UNSPECIFIED_FAILURE->"UNDPECIFIED"
            else->null
        }
    }



    public fun convertBatteryStatus(status: Int): String? {
        return when (status) {
            BatteryManager.BATTERY_STATUS_CHARGING -> "charging"
            BatteryManager.BATTERY_STATUS_FULL -> "full"
            BatteryManager.BATTERY_STATUS_DISCHARGING, BatteryManager.BATTERY_STATUS_NOT_CHARGING -> "discharging"
            BatteryManager.BATTERY_STATUS_UNKNOWN -> "unknown"
            else -> null
        }
    }

    public fun updateDetails(events: EventChannel.EventSink,data:String?,) {
        if (data != null) {
            events.success(data)
        } else {
            events.error("UNAVAILABLE", "Battery unavailable", null)
        }
    }

    companion object {
        public const val POWER_SAVE_MODE_SAMSUNG = "1"
        public const val POWER_SAVE_MODE_XIAOMI = 1
        public const val POWER_SAVE_MODE_HUAWEI = 4
    }







//override  fun onListen(arguments:Any?,events:EventSink?){
//    if(events==null) return
//    receiver=initReceiver(events)
//    context.registerReceiver(receiver, IntentFilter(Intent.ACTION_BATTERY_CHANGED))
//}
//    override  fun onCancel(arguments:Any?){
//        context.unregisterReceiver(receiver)
//        receiver=null
//    }
//    public  fun initReceiver(events:EventSink):BroadcastReceiver{
//        return  object:BroadcastReceiver(){
//            override fun onReceive(context: Context?, intent: Intent?) {
//                val status= intent?.getIntExtra(BatteryManager.EXTRA_STATUS,-1)
//                when(status){
//                    BatteryManager.BATTERY_STATUS_CHARGING->events.success("BATTERY CHARGING")
//                    BatteryManager.BATTERY_STATUS_FULL->events.success("BATTERY FULL")
//                    BatteryManager.BATTERY_STATUS_DISCHARGING->events.success("BATTERY DISCHARGING")
//                }
//                TODO("Not yet implemented")
//            }
//        }
//    }
//    public final var broadCastReciver= BroadcastReciver(){
//        @Override
//        listOfNotNull()
//    }
//    val battery= BatteryManager()
//    public fun info():String{
//       return """
//           {"isCharging":"${battery.isCharging}",
//            ""}
//       """.trimIndent()
//    }

    public fun onAttachedToEngineConnectivity(@NonNull flutterPluginBinding: FlutterPlugin.FlutterPluginBinding) {
        var context:Context=flutterPluginBinding.getApplicationContext()
        connectivityManager=
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        applicationContext = flutterPluginBinding.applicationContext
        connectivityEventChannel = EventChannel(flutterPluginBinding.binaryMessenger, "info-event-mehran-connectivity")
        connectivityEventChannel.setStreamHandler(this)



    }
}