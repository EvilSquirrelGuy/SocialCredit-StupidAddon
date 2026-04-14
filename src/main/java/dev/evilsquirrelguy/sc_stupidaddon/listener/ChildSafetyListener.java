package dev.evilsquirrelguy.sc_stupidaddon.listener;

import com.example.socialcredit.api.event.SocialCreditPreChangeEvent;
import dev.evilsquirrelguy.jhaac.ConfigGroup;
import dev.evilsquirrelguy.sc_stupidaddon.SocialCreditStupidAddon;
import dev.evilsquirrelguy.sc_stupidaddon.util.MessageFormatter;
import org.bukkit.entity.Ageable;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.List;

public class ChildSafetyListener implements Listener {
  private final SocialCreditStupidAddon plugin;

  private final double searchRadius;
  private final int penalty;
  private final String message;

  public ChildSafetyListener(SocialCreditStupidAddon plugin) {
    this.plugin = plugin;
    // load config
    ConfigGroup childSafetyConfig = plugin.config.getGroup("child-safety");
    searchRadius = childSafetyConfig.getEntry("search").getDouble();
    penalty = childSafetyConfig.getEntry("penalty").getInt();
    message = childSafetyConfig.getEntry("message").getString();
  }

  @EventHandler
  public void onSocialCreditChange(SocialCreditPreChangeEvent event) {
    // if they gained credit, leave
    if (event.getDelta() >= 0) return;

    plugin.getSLF4JLogger().info("delta: " + String.valueOf(event.getDelta()));
    plugin.getSLF4JLogger().info("modifier: " + String.valueOf(penalty));

    Player player = plugin.getServer().getPlayer(event.getPlayerId());

    if (player == null) return; // ok, this should never run, but if it does then something went horribly wrong?

    List<Entity> nearbyEntities = player.getNearbyEntities(searchRadius, searchRadius, searchRadius);

    // no nearby entities
    if (nearbyEntities.isEmpty()) return;

    for (Entity nearbyEntity : nearbyEntities) {
      // check for nearby children
      if (!(nearbyEntity instanceof Ageable ageable)) continue;
      if (ageable.isAdult()) continue;
      // there are children
      else {
        int newDelta = event.getDelta() - penalty ;
        event.setDelta(newDelta);
        player.sendMessage(
            MessageFormatter.ministryNotification(message, false)
        );
        return;
      }
    }
  }

}
