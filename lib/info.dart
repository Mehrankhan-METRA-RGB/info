import 'info_platform_interface.dart';

class Info {
  Future<Object?> system() async {
    return await InfoPlatform.instance.System();
  }

  Future<Object?> device() async {
    // Map<String, dynamic>? data = await InfoPlatform.instance.Device();
    return await InfoPlatform.instance.Device();
  }

  Stream battery() {
    return InfoPlatform.instance.Battery();
  }

  Stream onChangeConnectivity() {
    return InfoPlatform.instance.OnChangeConnectivity();
  }

  Future<Object?> connectivity() {
    return InfoPlatform.instance.Connectivity();
  }

  Future<String?> soc() {
    return InfoPlatform.instance.SOC();
  }

  Future<String?> thermal() {
    return InfoPlatform.instance.Thermal();
  }

  Future<String?> sensors() {
    return InfoPlatform.instance.Sensors();
  }
}
