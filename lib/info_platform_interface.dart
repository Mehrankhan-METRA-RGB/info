// ignore_for_file: non_constant_identifier_names

import 'package:plugin_platform_interface/plugin_platform_interface.dart';

import 'info_method_channel.dart';

abstract class InfoPlatform extends PlatformInterface {
  /// Constructs a InfoPlatform.
  InfoPlatform() : super(token: _token);

  static final Object _token = Object();

  static InfoPlatform _instance = MethodChannelInfo();

  /// The default instance of [InfoPlatform] to use.
  ///
  /// Defaults to [MethodChannelInfo].
  static InfoPlatform get instance => _instance;

  /// Platform-specific implementations should set this with their own
  /// platform-specific class that extends [InfoPlatform] when
  /// they register themselves.
  static set instance(InfoPlatform instance) {
    PlatformInterface.verifyToken(instance, _token);
    _instance = instance;
  }

  Future<Object?> System() {
    throw UnimplementedError('System() has not been implemented.');
  }

  Stream OnChangeConnectivity() {
    throw UnimplementedError(
        'onChangeConnectivity() has not been implemented.');
  }

  Future<Object?> Connectivity() {
    throw UnimplementedError('Connectivty() has not been implemented.');
  }

  Stream Battery() {
    throw UnimplementedError('Battery() has not been implemented.');
  }

  Future<Object?> Device() {
    throw UnimplementedError('Device() has not been implemented.');
  }

  Future<String?> SOC() {
    throw UnimplementedError('SOC() has not been implemented.');
  }

  Future<String?> Thermal() {
    throw UnimplementedError('Thermal() has not been implemented.');
  }

  Future<String?> Sensors() {
    throw UnimplementedError('Sensors() has not been implemented.');
  }
}
