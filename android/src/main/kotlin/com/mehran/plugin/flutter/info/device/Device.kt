package com.mehran.plugin.flutter.info.device

import android.content.pm.FeatureInfo
import android.content.pm.PackageManager
import android.os.Build
import android.util.DisplayMetrics
import android.view.Display
import android.view.WindowManager

class Device(  val packageManager: PackageManager,
                  val windowManager: WindowManager) {

    val mapData: MutableMap<String, Any> = HashMap()
    val display: Display = windowManager.defaultDisplay
    val metrics = DisplayMetrics()

    public fun info(): MutableMap<String, Any> {
        mapData["id"] = Build.ID
        mapData["manufacturer"] = Build.MANUFACTURER
        mapData["model"] = Build.MODEL
        mapData["product"] = Build.PRODUCT
        mapData["board"] = Build.BOARD
        mapData["bootloader"] = Build.BOOTLOADER
        mapData["brand"] = Build.BRAND
        mapData["device"] = Build.DEVICE
        mapData["display"] = Build.DISPLAY
        mapData["fingerprint"] = Build.FINGERPRINT
        mapData["hardware"] = Build.HARDWARE
        mapData["host"] = Build.HOST


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mapData["supported32BitAbis"] = listOf(*Build.SUPPORTED_32_BIT_ABIS)
            mapData["supported64BitAbis"] = listOf(*Build.SUPPORTED_64_BIT_ABIS)
            mapData["supportedAbis"] = listOf<String>(*Build.SUPPORTED_ABIS)
        } else {
            mapData["supported32BitAbis"] = emptyList<String>()
            mapData["supported64BitAbis"] = emptyList<String>()
            mapData["supportedAbis"] = emptyList<String>()
        }

        mapData["tags"] = Build.TAGS
        mapData["type"] = Build.TYPE
        mapData["isPhysicalDevice"] = !isEmulator
        mapData["systemFeatures"] = getSystemFeatures()

        val version: MutableMap<String, Any> = HashMap()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            version["baseOS"] = Build.VERSION.BASE_OS
            version["previewSdkInt"] = Build.VERSION.PREVIEW_SDK_INT
            version["securityPatch"] = Build.VERSION.SECURITY_PATCH
        }
        version["codename"] = Build.VERSION.CODENAME
        version["incremental"] = Build.VERSION.INCREMENTAL
        version["release"] = Build.VERSION.RELEASE
        version["sdkInt"] = Build.VERSION.SDK_INT
        mapData["version"] = version


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            display.getRealMetrics(metrics)
        } else {
            display.getMetrics(metrics)
        }

        val displayResult: MutableMap<String, Any> = HashMap()
        displayResult["widthPx"] = metrics.widthPixels.toDouble()
        displayResult["heightPx"] = metrics.heightPixels.toDouble()
        displayResult["xDpi"] = metrics.xdpi
        displayResult["yDpi"] = metrics.ydpi
        mapData["displayMetrics"] = displayResult

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            mapData["serialNumber"] = try {
                Build.getSerial()
            } catch (ex: SecurityException) {
                Build.UNKNOWN
            }
        } else {
            mapData["serialNumber"] = Build.SERIAL
        }







        return mapData

    }
private fun getSystemFeatures(): List<String> {
    val featureInfos: Array<FeatureInfo> = packageManager.systemAvailableFeatures
    return featureInfos
        .filterNot { featureInfo -> featureInfo.name == null }
        .map { featureInfo -> featureInfo.name }
}


/**
  A simple emulator-detection based on the flutter tools detection logic and a couple of legacy
  detection systems
 */
private val isEmulator: Boolean
    get() = (Build.BRAND.startsWith("generic") && Build.DEVICE.startsWith("generic")
            || Build.FINGERPRINT.startsWith("generic")
            || Build.FINGERPRINT.startsWith("unknown")
            || Build.HARDWARE.contains("goldfish")
            || Build.HARDWARE.contains("ranchu")
            || Build.MODEL.contains("google_sdk")
            || Build.MODEL.contains("Emulator")
            || Build.MODEL.contains("Android SDK built for x86")
            || Build.MANUFACTURER.contains("Genymotion")
            || Build.PRODUCT.contains("sdk_google")
            || Build.PRODUCT.contains("google_sdk")
            || Build.PRODUCT.contains("sdk")
            || Build.PRODUCT.contains("sdk_x86")
            || Build.PRODUCT.contains("vbox86p")
            || Build.PRODUCT.contains("emulator")
            || Build.PRODUCT.contains("simulator"))



}