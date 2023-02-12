package com.mehran.plugin.flutter.info.device

import android.content.pm.PackageManager
import android.provider.Settings

class System {
//    private var checkedPermission = PackageManager.PERMISSION_DENIED

    public fun info():String{
        return """
            "api":"${android.os.Build.VERSION.SDK_INT}",
            "android_version":"${android.os.Build.VERSION.BASE_OS}",
            "security_patch":"${android.os.Build.VERSION.SECURITY_PATCH}",
            "code_name":"${android.os.Build.VERSION.CODENAME}",
            "incremental":"${android.os.Build.VERSION.INCREMENTAL}",
            "preview_sdk":"${android.os.Build.VERSION.PREVIEW_SDK_INT}",
            "release":"${android.os.Build.VERSION.RELEASE}",
            "boot_loader":"${android.os.Build.BOOTLOADER}",
            "build_id":"${android.os.Build.ID}",
            "up_time":"${android.os.Build.TIME}",
            "java_vm":"UNKNOWN",
            "root_access":"UNKNOWN",
            "open_gl_es":"UNKNOWN",
            "kernal_architecture":"UNKNOWN",
            "kernal_version":"UNKNOWN",
            "google_play_services":"UNKNOWN",
        """.trimIndent()
    }
}