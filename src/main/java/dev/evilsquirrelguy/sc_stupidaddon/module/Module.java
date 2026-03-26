package dev.evilsquirrelguy.sc_stupidaddon.module;

import dev.evilsquirrelguy.sc_stupidaddon.SocialCreditStupidAddon;

public interface Module {
  void enable();
  void disable();
  boolean isEnabled();
}
