// ignore_for_file: non_constant_identifier_names

import 'package:flutter/foundation.dart';
import 'package:flutter/services.dart';

import 'info_platform_interface.dart';

/// An implementation of [InfoPlatform] that uses method channels.
class MethodChannelInfo extends InfoPlatform {
  /// The method channel used to interact with the native platform.
  @visibleForTesting
  final methodChannel = const MethodChannel('info-mehran');
  @visibleForTesting
  EventChannel eventChannel = const EventChannel('info-event-mehran');
  @visibleForTesting
  EventChannel connectivityEventChannel =
      const EventChannel('info-event-mehran-connectivity');
  @override
  Future<Object?> Device() async {
    final version = await methodChannel.invokeMethod<Object>('DEVICE');
    return version;
  }

  @override
  Stream Battery() async* {
    /// Fires whenever the battery state changes.

    yield* eventChannel.receiveBroadcastStream();
  }

  @override
  Stream OnChangeConnectivity() async* {
    /// Fires whenever the Connection state changes.
   

    yield* connectivityEventChannel.receiveBroadcastStream();
  }

  // Stream<Object> get onConnectivityChanged {
  //
  //   yield* connectivityEventChannel
  //       .receiveBroadcastStream();
  // }

  @override
  Future<Object?> Connectivity() async {
    return await methodChannel.invokeMethod<Object>('CONNECTIVITY');
  }

  @override
  Future<Object?> System() async {
    final version = await methodChannel.invokeMethod<Object>('SYSTEM');
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
