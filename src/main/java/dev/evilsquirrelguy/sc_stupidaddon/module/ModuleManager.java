package dev.evilsquirrelguy.sc_stupidaddon.module;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ModuleManager {
  private HashMap<String, Module> modules;

  public ModuleManager() {
    modules = new HashMap<>();
  }

  public boolean registerModule(String identifier, Module module) {
    if (this.modules.containsKey(identifier)) return false;
    // add the module, should be enabled later
    this.modules.put(identifier, module);
    return true;
  }

  public boolean unregisterModule(String identifier) {
    if (!this.modules.containsKey(identifier)) return false;
    // disable module so we're not left with dangling objects
    this.modules.get(identifier).disable();

    this.modules.remove(identifier);
    return true;
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
