#ifndef FLUTTER_PLUGIN_INFO_PLUGIN_H_
#define FLUTTER_PLUGIN_INFO_PLUGIN_H_

#include <flutter/method_channel.h>
#include <flutter/plugin_registrar_windows.h>

#include <memory>

namespace info {

class InfoPlugin : public flutter::Plugin {
 public:
  static void RegisterWithRegistrar(flutter::PluginRegistrarWindows *registrar);

  InfoPlugin();

  virtual ~InfoPlugin();

  // Disallow copy and assign.
  InfoPlugin(const InfoPlugin&) = delete;
  InfoPlugin& operator=(const InfoPlugin&) = delete;

 private:
  // Called when a method is called on this plugin's channel from Dart.
  void HandleMethodCall(
      const flutter::MethodCall<flutter::EncodableValue> &method_call,
      std::unique_ptr<flutter::MethodResult<flutter::EncodableValue>> result);
};

}  // namespace info

#endif  // FLUTTER_PLUGIN_INFO_PLUGIN_H_
