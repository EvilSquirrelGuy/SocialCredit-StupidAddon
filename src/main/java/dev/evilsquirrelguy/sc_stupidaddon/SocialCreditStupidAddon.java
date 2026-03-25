package dev.evilsquirrelguy.sc_stupidaddon;

import dev.evilsquirrelguy.jhaac.Config;
import dev.evilsquirrelguy.jhaac.ConfigFile;
import dev.evilsquirrelguy.jhaac.ConfigGroup;
import dev.evilsquirrelguy.sc_stupidaddon.task.EyeContactDetection;
import dev.evilsquirrelguy.sc_stupidaddon.task.LagDetection;
import org.bukkit.plugin.java.JavaPlugin;
import com.example.socialcredit.api.SocialCreditAPI;
import com.example.socialcredit.api.SocialCreditProvider;
import org.bukkit.scheduler.BukkitScheduler;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

public final class SocialCreditStupidAddon extends JavaPlugin {

  public SocialCreditAPI scApi;
  public Config config;

  public void loadConfig() {
    ConfigFile cfgFile;
    try {
      cfgFile = new ConfigFile(new File(getDataFolder(), "config.html"));
    } catch (IOException e) {
      this.getSLF4JLogger().error("Failed to load config file", e);
      return;
    }

    this.config = cfgFile.getConfig();
  }

  @Override
  public void onEnable() {
    // Plugin startup logic
    // generate config (or, well, copy it)
    // yeah... i was serious
    saveResource("config.html", false);

    // load the social credit api (safely)
    if (!SocialCreditProvider.isAvailable()) {
      return; // well now we can't load all our misery :(
    }
    this.scApi = SocialCreditProvider.get();

    loadConfig();


    // first, we do scheduled tasks, because they are very cool
    BukkitScheduler scheduler = this.getServer().getScheduler();
    // register all my... interesting tasks

    // load modules conditionally
    ConfigGroup modules = config.getGroup("modules");

    if (modules.getEntry("lag-detect").getBoolean()) { // lag detector module
      scheduler.runTaskTimer(
          this,
          new LagDetection(this),
          TimeUnit.MINUTES.toSeconds(1) * 20, // run after 1 minute
          TimeUnit.MINUTES.toSeconds(1) * 20 // every 1 minute
          // ok this solution is not perfect, cuz it ignores lag, but i don't see any better ones
      );
    }

    if (modules.getEntry("eye-contact").getBoolean()) {
      // run eye contact checker every tick... this is probably fine, right?
      scheduler.runTaskTimer(
          this,
          new EyeContactDetection(this),
          0L, 1L
      );
    }

    if (modules.getEntry("chat-censor").getBoolean()) {
      // enable chat ~~censorship~~ scanning
      // this.getServer().getPluginManager().registerEvents(new ChatCensorshipListener(this), this);
    }


    //this.getServer().getPluginManager().registerEvents(this, this);
  }

  @Override
  public void onDisable() {
    // Plugin shutdown logic
  }
}
