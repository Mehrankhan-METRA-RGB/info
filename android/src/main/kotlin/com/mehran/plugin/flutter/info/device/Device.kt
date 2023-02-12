package com.mehran.plugin.flutter.info.device

class Device {

    public fun info():String{
        return """
            "model":"${android.os.Build.MODEL}",
            "brand":"${android.os.Build.BRAND}",
            "time":"${android.os.Build.TIME}",
            "id":"${android.os.Build.ID}",
            "board":"${android.os.Build.BOARD}",
            "device":"${android.os.Build.DEVICE}",
            "display":"${android.os.Build.DISPLAY}",
            "finger_print":"${android.os.Build.FINGERPRINT}",
            "hardware":"${android.os.Build.HARDWARE}",
            "host":"${android.os.Build.HOST}",
            "manufacturer":"${android.os.Build.MANUFACTURER}",
            "product":"${android.os.Build.PRODUCT}",
            "supported_32_abis":"${android.os.Build.SUPPORTED_32_BIT_ABIS}",
            "supported_64_abis":"${android.os.Build.SUPPORTED_64_BIT_ABIS}",
            "supported_abis":"${android.os.Build.SUPPORTED_ABIS}",
            "tags":"${android.os.Build.TAGS}",
            "type":"${android.os.Build.TYPE}",
            "user":"${android.os.Build.USER}",
        """.trimIndent()
    }
}