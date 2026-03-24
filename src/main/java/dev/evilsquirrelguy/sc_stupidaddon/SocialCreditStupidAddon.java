package dev.evilsquirrelguy.sc_stupidaddon;

import dev.evilsquirrelguy.sc_stupidaddon.task.LagDetection;
import org.bukkit.plugin.java.JavaPlugin;
import com.example.socialcredit.api.SocialCreditAPI;
import com.example.socialcredit.api.SocialCreditProvider;
import org.bukkit.scheduler.BukkitScheduler;

import java.util.concurrent.TimeUnit;

public final class SocialCreditStupidAddon extends JavaPlugin {

  public SocialCreditAPI scApi;

  @Override
  public void onEnable() {
    // Plugin startup logic
    // load the social credit api first (safely)
    if (!SocialCreditProvider.isAvailable()) {
      return; // well now we can't load all our misery :(
    }
    this.scApi = SocialCreditProvider.get();

    // TODO: add config read logic (ok ngl i have NO IDEA WHERE THIS GOES SEND HELP)

    // first, we do scheduled tasks, because they are very cool
    BukkitScheduler scheduler = this.getServer().getScheduler();
    // register all my... interesting tasks

    // lag detector
    scheduler.runTaskTimer(
        this,
        new LagDetection(this),
        TimeUnit.MINUTES.toSeconds(1) * 20, // run after 1 minute
        TimeUnit.MINUTES.toSeconds(1) * 20 // every 1 minute
        // ok this solution is not perfect, cuz it ignores lag, but i don't see any better ones
    );


    //this.getServer().getPluginManager().registerEvents(this, this);
  }

  @Override
  public void onDisable() {
    // Plugin shutdown logic
  }
}
