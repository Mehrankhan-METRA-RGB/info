// ignore_for_file: non_constant_identifier_names

import 'package:flutter/foundation.dart';
import 'package:flutter/services.dart';

import 'info_platform_interface.dart';

/// An implementation of [InfoPlatform] that uses method channels.
class MethodChannelInfo extends InfoPlatform {
  /// The method channel used to interact with the native platform.
  @visibleForTesting
  final methodChannel = const MethodChannel('info-mehran');

  @override
  Future<String?> System() async {
    final version = await methodChannel.invokeMethod<String>('SYSTEM');
    return version;
  }

  @override
  Future<String?> Battery() async {
    final version = await methodChannel.invokeMethod<String>('BATTERY');
    return version;
  }

  @override
  Future<String?> Device() async {
    final version = await methodChannel.invokeMethod<String>('DEVICE');
    return version;
  }

  @override
  Future<String?> Thermal() async {
    final version = await methodChannel.invokeMethod<String>('THERMAL');
    return version;
  }

  @override
  Future<String?> SOC() async {
    final version = await methodChannel.invokeMethod<String>('SOC');
    return version;
  }

  @override
  Future<String?> Sensors() async {
    final version = await methodChannel.invokeMethod<String>('SENSORS');
    return version;
  }
}
