package dev.evilsquirrelguy.sc_stupidaddon.module;

import dev.evilsquirrelguy.sc_stupidaddon.SocialCreditStupidAddon;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;


public class ListenerModule implements Module {
  private final SocialCreditStupidAddon plugin;
  private final Class<? extends Listener> listenerClass;
  private Listener listenerInstance = null;

  private boolean enabled = false;

  public ListenerModule(SocialCreditStupidAddon plugin, Class<? extends Listener> listenerClass) {
    this.plugin = plugin;
    this.listenerClass = listenerClass;
  }

  @Override
  public void enable() {
    if (enabled) return;

    try {
      // get constructor, and use it with our plugin
      listenerInstance = listenerClass.getConstructor(SocialCreditStupidAddon.class).newInstance(plugin);
    } catch (Exception e) {
      // failed to load
      plugin.getSLF4JLogger().error("Failed to create listener instance: {}", e.getMessage());
      return;
    }

    plugin.getServer().getPluginManager().registerEvents(listenerInstance, plugin);
    enabled = true;
    plugin.getSLF4JLogger().info("{} module enabled", listenerClass.getSimpleName());
  }

  @Override
  public void disable() {
    if (!enabled) return;

    HandlerList.unregisterAll(listenerInstance);
    enabled = false;
    plugin.getSLF4JLogger().info("{} module disabled", listenerClass.getSimpleName());
  }

  @Override
  public boolean isEnabled() {
    return enabled;
  }
}
