package dev.evilsquirrelguy.sc_stupidaddon.task;

public interface ScheduledTask extends Runnable {
  long getDelay();
  long getInterval();
}
