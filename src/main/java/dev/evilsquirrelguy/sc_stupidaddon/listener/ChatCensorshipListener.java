package dev.evilsquirrelguy.sc_stupidaddon.listener;

import dev.evilsquirrelguy.jhaac.ConfigGroup;
import dev.evilsquirrelguy.sc_stupidaddon.SocialCreditStupidAddon;
import dev.evilsquirrelguy.sc_stupidaddon.util.MessageFormatter;
import io.papermc.paper.event.player.AsyncChatEvent;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.ArrayList;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

public class ChatCensorshipListener implements Listener {

  private final SocialCreditStupidAddon plugin;
  // config stuff
  private final ArrayList<Pattern> badWords;
  private final String reason;
  private final String message;
  private final int penalty;

  public ChatCensorshipListener(SocialCreditStupidAddon plugin) {
    this.plugin = plugin;
    // load the bad words from the config
    this.badWords = new ArrayList<>();

    ConfigGroup cfg = plugin.config.getGroup("chat-censor");

    cfg.getEntry("messages").getList().forEach(el -> {
      String[] tokenised = el.getString().split("/", -1);
      // parse into regex
      Pattern pattern = Pattern.compile(
          tokenised[1],
          // apply case insensitive flag
          tokenised[2].contains("i") ? Pattern.CASE_INSENSITIVE : 0x00
      );
      // add
      badWords.add(pattern);
    });

    // load the other things from the config
    this.reason = cfg.getEntry("reason").getString();
    this.message = cfg.getEntry("message").getString();
    this.penalty = cfg.getEntry("penalty").getInt();
  }


  @EventHandler
  public void onIncomingChat(AsyncChatEvent event) {
    Player player = event.getPlayer();
    String rawMessage = PlainTextComponentSerializer.plainText().serialize(event.message());
    // String rawMessage = event.originalMessage() //whatever, serialise it here

    this.plugin.getServer().getScheduler().runTask(this.plugin, () -> {
      int detections = 0;

      // check all patterns
      for (Pattern pattern : badWords) {
        // scan message for matches
        Matcher matcher = pattern.matcher(rawMessage);
        if (matcher.find()) detections++;
      }

      if (detections == 0) return;

      // reduce sc
      this.plugin.scApi.addScore(
          player.getUniqueId(),
          player.getName(),
          -1 * detections * this.penalty,
          this.reason
      );
      // notify
      player.sendMessage(
          MessageFormatter.stateBroadcast(this.message)
      );

    });
  }
}
