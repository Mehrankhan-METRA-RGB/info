package com.mehran.plugin.flutter.info.device

//import java.io.BufferedReader
//import java.io.InputStream
//import java.io.InputStreamReader


class System() {
//    private var checkedPermission = PackageManager.PERMISSION_DENIED
private val mapData: MutableMap<String, Any> = HashMap()

    public fun info():MutableMap<String, Any>{

mapData["api"]=android.os.Build.VERSION.SDK_INT
mapData["android_version"]=android.os.Build.VERSION.BASE_OS
mapData["security_patch"]=android.os.Build.VERSION.SECURITY_PATCH
mapData["code_name"]=android.os.Build.VERSION.CODENAME
mapData["incremental"]=android.os.Build.VERSION.INCREMENTAL
mapData["preview_sdk"]=android.os.Build.VERSION.PREVIEW_SDK_INT
mapData["release"]=android.os.Build.VERSION.RELEASE
mapData["boot_loader"]=android.os.Build.BOOTLOADER
mapData["build_id"]=android.os.Build.ID
mapData["up_time"]=android.os.Build.TIME

        mapData["java_vm"]="UNKNOWN"
        mapData["root_access"]="UNKNOWN"
        mapData["open_gl_es"]="UNKNOWN"
        mapData["kernal_architecture"]="UNKNOWN"
        mapData["kernal_version"]="UNKNOWN"
        mapData["google_play_services"]="UNKNOWN"

        return mapData
    }

}