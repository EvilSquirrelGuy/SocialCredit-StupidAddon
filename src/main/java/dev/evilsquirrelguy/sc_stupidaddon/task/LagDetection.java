/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package dev.evilsquirrelguy.sc_stupidaddon.task;

import dev.evilsquirrelguy.sc_stupidaddon.SocialCreditStupidAddon;

import com.example.socialcredit.api.SocialCreditAPI;
import com.example.socialcredit.api.SocialCreditProvider;
import dev.evilsquirrelguy.sc_stupidaddon.util.MessageFormatter;
import net.kyori.adventure.text.TextComponent;

import java.util.UUID;
import java.util.concurrent.TimeUnit;


public class LagDetection implements ScheduledTask {
  /**
   * Drop all players' social credit score when the server is lagging :)
   */

  @Override
  public long getDelay() { return TimeUnit.MINUTES.toSeconds(1) * 20; }

  @Override
  public long getInterval() { return getDelay(); }

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
    int monitorPeriodIndex = switch (monitorPeriodS) {
      case "1m" -> 0;
      case "5m" -> 1;
      case "15m" -> 2;
      default -> 0;
    };

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
