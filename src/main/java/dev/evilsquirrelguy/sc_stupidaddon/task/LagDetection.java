package dev.evilsquirrelguy.sc_stupidaddon.task;

import dev.evilsquirrelguy.sc_stupidaddon.SocialCreditStupidAddon;

import com.example.socialcredit.api.SocialCreditAPI;
import com.example.socialcredit.api.SocialCreditProvider;
import dev.evilsquirrelguy.sc_stupidaddon.util.MessageFormatter;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.NamedTextColor;

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
    // read config for threshold and which avg tickrate to read
    String monitorPeriodS = this.plugin.config.getGroup("lag-detect").getEntry("monitor-period").getString();
    // default value
    int monitorPeriodIndex = 0;

    switch (monitorPeriodS) {
      case "1m":
        monitorPeriodIndex = 0;
        break;
      case "5m":
        monitorPeriodIndex = 1;
        break;
      case "15m":
        monitorPeriodIndex = 2;
        break;
    }

    double threshold = this.plugin.config.getGroup("lag-detect").getEntry("threshold").getDouble();
    double tickRate = this.plugin.getServer().getTPS()[monitorPeriodIndex];

    // no lag? skip the fun part
    if (tickRate > threshold) return;
    // >:)

    // grab the api
    SocialCreditAPI scApi = SocialCreditProvider.get();
    // read config values
    String reason = this.plugin.config.getGroup("lag-detect").getEntry("reason").getString();
    int penalty = -1 * this.plugin.config.getGroup("lag-detect").getEntry("penalty").getInt();

    // loop through all the online players
    this.plugin.getServer().getOnlinePlayers().forEach(player -> {
      // cache player info
      UUID uuid = player.getUniqueId();
      String name = player.getName();
      // get rid of their social credit score
      scApi.addScore(uuid, name, penalty, reason);
    });

    String message = this.plugin.config.getGroup("lag-detect").getEntry("message").getString();
    // build fancy message
    final TextComponent broadcastMsg = MessageFormatter.stateBroadcast(message);
    // send
    this.plugin.getServer().broadcast(broadcastMsg);

  }

}
