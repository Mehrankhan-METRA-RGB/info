import 'info_platform_interface.dart';

class Info {
  Future<String?> system() {
    return InfoPlatform.instance.System();
  }

  Future<String?> device() {
    return InfoPlatform.instance.Device();
  }

  Future<String?> battery() {
    return InfoPlatform.instance.Battery();
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
