package com.mehran.plugin.flutter.info.device

import android.content.*
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

    
public var applicationContext: Context? = null
    lateinit var events: EventChannel.EventSink
    private lateinit var eventChannel : EventChannel


  override public  fun onListen(arguments: Any?, events: EventChannel.EventSink) {
        this.events=events
        applicationContext?.registerReceiver(this, IntentFilter(Intent.ACTION_BATTERY_CHANGED))

    }
    
    
  override  public fun onCancel(arguments: Any?) {
        applicationContext!!.unregisterReceiver(this)
    }

    override fun onReceive(context: Context, intent: Intent) {

        val status = intent.getIntExtra(BatteryManager.EXTRA_STATUS, -1)
//                val power = intent.getIntExtra(BatteryManager.EX, -1)
        val level = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1)
        val tech = intent.getIntExtra(BatteryManager.EXTRA_TECHNOLOGY, 0)
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
            "plugged":${convertPlugged(plugged)},
            "voltage":$voltage,
            "scale":$scale,
            "power":$power,
            }""".trimMargin())
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
    public fun convertPlugged(health:Int):String?{
        return when (health){
            BatteryManager.BATTERY_PLUGGED_AC->"AC"
            BatteryManager.BATTERY_PLUGGED_USB->"USB"
            BatteryManager.BATTERY_PLUGGED_WIRELESS->"Wireless"
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










    public fun onAttachEngine(@NonNull flutterPluginBinding: FlutterPlugin.FlutterPluginBinding) {

        applicationContext = flutterPluginBinding.applicationContext
        eventChannel = EventChannel(flutterPluginBinding.binaryMessenger, "info-event-mehran")
        eventChannel.setStreamHandler(this)

    }
    public fun onDettachEngine(@NonNull flutterPluginBinding: FlutterPlugin.FlutterPluginBinding) {

        eventChannel.setStreamHandler(null)

    }



    companion object {
        public const val POWER_SAVE_MODE_SAMSUNG = "1"
        public const val POWER_SAVE_MODE_XIAOMI = 1
        public const val POWER_SAVE_MODE_HUAWEI = 4
    }
}