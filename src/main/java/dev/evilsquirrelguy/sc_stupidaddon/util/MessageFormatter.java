package dev.evilsquirrelguy.sc_stupidaddon.util;

import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;

public class MessageFormatter {

  /**
   * Generate a state broadcast message.
   *
   * @param message The message to append to the state broadcast tag.
   * @return The colourised and formatted component form of the message to send.
   */
  public static TextComponent stateBroadcast(String message) {
    return (Component
        .text("[STATE BROADCAST]")
        .color(NamedTextColor.GOLD)
        .append(Component.text(message, NamedTextColor.GOLD))
    );

  }
}
