/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package dev.evilsquirrelguy.sc_stupidaddon.module;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ModuleManager {
  private final HashMap<String, Module> modules;

  public ModuleManager() {
    modules = new HashMap<>();
  }

  public void registerModule(String identifier, Module module) {
    if (this.modules.containsKey(identifier)) return;
    // add the module, should be enabled later
    this.modules.put(identifier, module);
  }

  public void unregisterModule(String identifier) {
    if (!this.modules.containsKey(identifier)) return;
    // disable module so we're not left with dangling objects
    this.modules.get(identifier).disable();

    this.modules.remove(identifier);
  }

  public List<String> getModuleIdentifiers() {
    return new ArrayList<>(modules.keySet());
  }

  public List<Module> getModules() {
    return new ArrayList<>(modules.values());
  }

  public Module getModule(String identifier) {
    return this.modules.get(identifier);
  }

}
