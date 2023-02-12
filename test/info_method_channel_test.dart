import 'package:flutter/services.dart';
import 'package:flutter_test/flutter_test.dart';
import 'package:info/info_method_channel.dart';

void main() {
  MethodChannelInfo platform = MethodChannelInfo();
  const MethodChannel channel = MethodChannel('info');

  TestWidgetsFlutterBinding.ensureInitialized();

  setUp(() {
    channel.setMockMethodCallHandler((MethodCall methodCall) async {
      return '42';
    });
  });

  tearDown(() {
    channel.setMockMethodCallHandler(null);
  });

  test('getPlatformVersion', () async {
    expect(await platform.System(), '42');
  });
}
