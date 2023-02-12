import 'package:flutter_test/flutter_test.dart';
import 'package:info/info.dart';
import 'package:info/info_method_channel.dart';
import 'package:info/info_platform_interface.dart';
import 'package:plugin_platform_interface/plugin_platform_interface.dart';

class MockInfoPlatform with MockPlatformInterfaceMixin implements InfoPlatform {
  @override
  Future<String?> system() => Future.value('42');
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
