//
//  Generated file. Do not edit.
//

// clang-format off

#include "generated_plugin_registrant.h"

#include <info/info_plugin.h>

void fl_register_plugins(FlPluginRegistry* registry) {
  g_autoptr(FlPluginRegistrar) info_registrar =
      fl_plugin_registry_get_registrar_for_plugin(registry, "InfoPlugin");
  info_plugin_register_with_registrar(info_registrar);
}
