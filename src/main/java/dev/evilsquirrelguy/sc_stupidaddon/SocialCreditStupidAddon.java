package dev.evilsquirrelguy.sc_stupidaddon;

import dev.evilsquirrelguy.jhaac.Config;
import dev.evilsquirrelguy.jhaac.ConfigFile;
import dev.evilsquirrelguy.jhaac.ConfigGroup;
import dev.evilsquirrelguy.sc_stupidaddon.listener.ChatCensorshipListener;
import dev.evilsquirrelguy.sc_stupidaddon.module.ListenerModule;
import dev.evilsquirrelguy.sc_stupidaddon.module.Module;
import dev.evilsquirrelguy.sc_stupidaddon.module.ModuleManager;
import dev.evilsquirrelguy.sc_stupidaddon.module.TaskModule;
import dev.evilsquirrelguy.sc_stupidaddon.task.EyeContactDetection;
import dev.evilsquirrelguy.sc_stupidaddon.task.LagDetection;
import org.bukkit.plugin.java.JavaPlugin;
import com.example.socialcredit.api.SocialCreditAPI;
import com.example.socialcredit.api.SocialCreditProvider;
import org.bukkit.scheduler.BukkitScheduler;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class SocialCreditStupidAddon extends JavaPlugin {

  public SocialCreditAPI scApi;
  public Config config;

  private ModuleManager moduleManager;

  public ModuleManager getModuleManager() {
    return moduleManager;
  }

  public void loadConfig() {
    ConfigFile cfgFile;
    this.getSLF4JLogger().info("Attempting to load config file...");
    try {
      cfgFile = new ConfigFile(new File(getDataFolder(), "config.html"));
      this.getSLF4JLogger().info("Loaded config file successfully");
    } catch (IOException e) {
      this.getSLF4JLogger().error("Failed to load config file", e);
      return;
    }

    this.config = cfgFile.getConfig();
  }

  public void reloadConfig() {
    this.loadConfig();
    this.loadModules();
  }

  public void loadModules() {
    // load modules conditionally
    ConfigGroup modulesToEnable = config.getGroup("modules");

    for (String key : moduleManager.getModuleIdentifiers()) {
      Module module = moduleManager.getModule(key);

      // check if enabled
      if (modulesToEnable.getEntry(key).getBoolean()) {
        module.enable();
      } else {
        module.disable();
      }
    }
  }

  @Override
  public void onEnable() {
    // Plugin startup logic
    // generate config (or, well, copy it)
    // yeah... i was serious, it is HTML
    saveResource("config.html", false);

    // load the social credit api (safely)
    if (!SocialCreditProvider.isAvailable()) {
      this.getSLF4JLogger().error("Failed to load plugin: SocialCredit API unavailable");
      this.getServer().getPluginManager().disablePlugin(this);
      return; // well now we can't load all our misery :(
    }

    this.scApi = SocialCreditProvider.get();

    loadConfig();

    // initialise modules
    moduleManager.registerModule(
        "lag-detect", new TaskModule(this, LagDetection.class)
    );
    moduleManager.registerModule(
        "eye-contact", new TaskModule(this, EyeContactDetection.class)
    );
    moduleManager.registerModule(
        "chat-censor", new ListenerModule(this, ChatCensorshipListener.class)
    );

    loadModules();

  }

  @Override
  public void onDisable() {
    // Plugin shutdown logic

    // disable all modules
    for (Module module : moduleManager.getModules()) {
      module.disable();
    }
  }
}
