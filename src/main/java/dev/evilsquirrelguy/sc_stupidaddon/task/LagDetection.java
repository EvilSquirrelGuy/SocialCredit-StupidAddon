package dev.evilsquirrelguy.sc_stupidaddon.task;

import dev.evilsquirrelguy.sc_stupidaddon.SocialCreditStupidAddon;

import com.example.socialcredit.api.SocialCreditAPI;
import com.example.socialcredit.api.SocialCreditProvider;

import java.util.UUID;


public class LagDetection implements Runnable {
  /**
   * Drop all players' social credit score when the server is lagging :)
   */

  private final SocialCreditStupidAddon plugin;

  public LagDetection(SocialCreditStupidAddon plugin) {
    this.plugin = plugin;
  }

  @Override
  public void run() {
    // now then, has the server been lagging? hmmmmm
    // only check the past minute, punishing for any longer period of time would be a bit over the top
    double tickRate = this.plugin.getServer().getTPS()[0];
    double threshold = 18.5;  // TODO: make configurable

    // no lag? skip the fun part
    if (tickRate > threshold) return;
    // >:)

    // grab the api
    SocialCreditAPI scApi = SocialCreditProvider.get();

    // loop through all the online players
    this.plugin.getServer().getOnlinePlayers().forEach(player -> {
      // IF LAG-FRIENDLY IS ON
      UUID uuid = player.getUniqueId();
      String name = player.getName();

      this.plugin.getServer().getScheduler().runTaskAsynchronously(this.plugin, () -> {
        // get rid of their social credit score
        // TODO: make this number (and message) configurable in my beautiful HTML-as-a-config file
        // or maybe the message should go in a language-file... idk
        scApi.addScore(uuid, name, -2, "Malicious performance-based state sabotage");
      });
      // ELSE
      // scApi.addScore(uuid, name, -2, "Malicious performance-based state sabotage");
    });
  }

}
