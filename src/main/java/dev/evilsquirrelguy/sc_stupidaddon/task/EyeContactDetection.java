package dev.evilsquirrelguy.sc_stupidaddon.task;

import dev.evilsquirrelguy.jhaac.ConfigGroup;
import dev.evilsquirrelguy.sc_stupidaddon.SocialCreditStupidAddon;
import dev.evilsquirrelguy.sc_stupidaddon.util.MessageFormatter;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.UUID;

public class EyeContactDetection implements Runnable {

  private final SocialCreditStupidAddon plugin;
  private final long maxLookDuration;
  private final int maxDistance;
  private final int penalty;
  private final String message;
  private final String reason;

  // store who's looking at whom (and for how long)
  private final HashMap<UUID, UUID> eyeContactPairs;
  private final HashMap<UUID, Long> eyeContactTimes;

  public EyeContactDetection(SocialCreditStupidAddon plugin) {
    this.plugin = plugin;

    // read config
    ConfigGroup cfg = plugin.config.getGroup("eye-contact");
    this.maxLookDuration = cfg.getEntry("max-duration").getLong();
    this.maxDistance = cfg.getEntry("max-distance").getInt();
    this.penalty = cfg.getEntry("penalty").getInt();
    this.message = cfg.getEntry("message").getString();
    this.reason = cfg.getEntry("reason").getString();

    // initialise the funny data objects
    this.eyeContactPairs = new HashMap<UUID, UUID>();
    this.eyeContactTimes = new HashMap<UUID, Long>();
  }

  private void reset(Player player) {
    UUID uuid = player.getUniqueId();
    eyeContactPairs.remove(uuid);
    eyeContactTimes.remove(uuid);
  }

  @Override
  public void run() {
    // check all players
    for (Player player : this.plugin.getServer().getOnlinePlayers()) {
      // get targeted entity
      Entity lookingAt = player.getTargetEntity(this.maxDistance);

      // do the looking at checks, if they fail, clear the player's entry

      // are we looking at a player
      if (!(lookingAt instanceof Player target)) {
        reset(player);
        return;
      }
      // is the player that's looking at us looking at a player
      if (!(target.getTargetEntity(this.maxDistance) instanceof Player otherTarget)) {
        reset(player);
        return;
      }
      // is that player looking at us
      if (!(otherTarget.getUniqueId().equals(player.getUniqueId()))) {
        reset(player);
        return;
      }

      // stash uuid
      UUID uuid = player.getUniqueId();

      // who were we looking at previously?
      UUID prevUuid = eyeContactPairs.get(uuid);

      // are we looking at the target/anyone atm?
      // side-note, since prevUuid is null if the entry doesn't exist, it's basically the same thing as if
      // we had been looking at someone else before

      if (!prevUuid.equals(target.getUniqueId())) {
        eyeContactPairs.put(uuid, target.getUniqueId());
      }

      // reset timers if they're not there
      if (!eyeContactTimes.containsKey(uuid)) {
        eyeContactTimes.put(uuid, 0L);
      // if we've been looking too long
      } else if (eyeContactTimes.get(uuid) >= maxLookDuration) {
        // clear countdown
        eyeContactTimes.remove(uuid);
        // punish
        this.plugin.scApi.addScore(uuid, player.getName(), -1 * this.penalty, this.reason);
        // notify them
        // TODO: come up with a more official sounding template for notifying the player
        player.sendMessage(
            // fill in other player's name
            MessageFormatter.stateBroadcast(message.replace("$1", target.getName()))
        );
      } else {
        // ok they're just continuing looking, add a tick
        eyeContactTimes.put(uuid, eyeContactTimes.get(uuid) + 1);
      }
    }
  }
}
