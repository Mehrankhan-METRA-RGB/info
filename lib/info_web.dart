// In order to *not* need this ignore, consider extracting the "web" version
// of your plugin as a separate package, instead of inlining it in the same
// package as the core of your plugin.
// ignore: avoid_web_libraries_in_flutter
// ignore_for_file: non_constant_identifier_names

import 'dart:html' as html show window;

import 'package:flutter_web_plugins/flutter_web_plugins.dart';

import 'info_platform_interface.dart';

/// A web implementation of the InfoPlatform of the Info plugin.
class InfoWeb extends InfoPlatform {
  /// Constructs a InfoWeb
  InfoWeb();

  static void registerWith(Registrar registrar) {
    InfoPlatform.instance = InfoWeb();
  }

  /// Returns a [String] containing the version of the platform.
  @override
  Future<String?> System() async {
    final version = html.window.navigator.userAgent;
    return version;
  }
}
