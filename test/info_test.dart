import 'package:flutter_test/flutter_test.dart';
import 'package:info/info.dart';
import 'package:info/info_method_channel.dart';
import 'package:info/info_platform_interface.dart';
import 'package:plugin_platform_interface/plugin_platform_interface.dart';

class MockInfoPlatform with MockPlatformInterfaceMixin implements InfoPlatform {
  @override
  Future<String?> system() => Future.value('42');

  @override
  Stream Battery() {
    // TODO: implement Battery
    throw UnimplementedError();
  }

  @override
  Future<Object?> Device() {
    // TODO: implement Device
    throw UnimplementedError();
  }

  @override
  Future<String?> SOC() {
    // TODO: implement SOC
    throw UnimplementedError();
  }

  @override
  Future<String?> Sensors() {
    // TODO: implement Sensors
    throw UnimplementedError();
  }

  @override
  Future<String?> System() {
    // TODO: implement System
    throw UnimplementedError();
  }

  @override
  Future<String?> Thermal() {
    // TODO: implement Thermal
    throw UnimplementedError();
  }
}

void main() {
  final InfoPlatform initialPlatform = InfoPlatform.instance;

  test('$MethodChannelInfo is the default instance', () {
    expect(initialPlatform, isInstanceOf<MethodChannelInfo>());
  });

  test('getPlatformVersion', () async {
    Info infoPlugin = Info();
    MockInfoPlatform fakePlatform = MockInfoPlatform();
    InfoPlatform.instance = fakePlatform;

    expect(await infoPlugin.system(), '42');
  });
}
