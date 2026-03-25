package dev.evilsquirrelguy.sc_stupidaddon.listener;

import dev.evilsquirrelguy.sc_stupidaddon.SocialCreditStupidAddon;
import io.papermc.paper.event.player.AsyncChatEvent;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.ArrayList;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

public class ChatCensorshipListener implements Listener {

  ArrayList<Pattern> badWords;
  SocialCreditStupidAddon plugin;

  public ChatCensorshipListener(SocialCreditStupidAddon plugin) {
    this.plugin = plugin;
    // load the bad words from the config
    this.badWords = new ArrayList<>();
    plugin.config.getEntry("chat-censor.messages").getList().forEach(el -> {
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

  }


  @EventHandler
  public void onIncomingChat(AsyncChatEvent event) {
    // TODO: store raw text form of chat message from async here
    Player player = event.getPlayer();
    // String rawMessage = event.originalMessage() //whatever, serialise it here

    // TODO: add sync code here
    this.plugin.getServer().getScheduler().runTask(this.plugin, () -> {
      // analyse message contents
      // each hit increases penalty... probably

      // reduce sc
      // this.plugin.scApi.addScore(player.getUniqueId(), player.getName(), -1, "dummy text");
      // notify
      // ...
    });
  }
}
