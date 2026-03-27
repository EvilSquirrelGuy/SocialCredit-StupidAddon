/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package dev.evilsquirrelguy.sc_stupidaddon.module;

import dev.evilsquirrelguy.sc_stupidaddon.SocialCreditStupidAddon;
import dev.evilsquirrelguy.sc_stupidaddon.task.ScheduledTask;
import org.bukkit.scheduler.BukkitTask;


public class TaskModule implements Module {
  private final SocialCreditStupidAddon plugin;
  private final Class<? extends ScheduledTask> taskClass;
  private ScheduledTask taskInstance = null;

  private BukkitTask task;
  private boolean enabled = false;

  public TaskModule(SocialCreditStupidAddon plugin, Class<? extends ScheduledTask> taskClass) {
    this.plugin = plugin;
    this.taskClass = taskClass;
  }

  @Override
  public void enable() {
    if (enabled) return;

    try {
      // get constructor, and use it with our plugin
      taskInstance = taskClass.getConstructor(SocialCreditStupidAddon.class).newInstance(plugin);
    } catch (Exception e) {
      // failed to load
      plugin.getSLF4JLogger().error("Failed to create task instance: {}", e.getMessage());
      return;
    }

    // start timer
    task = plugin.getServer().getScheduler().runTaskTimer(
        plugin,
        taskInstance,
        taskInstance.getDelay(), // run after
        taskInstance.getInterval() // every
        // if we want dynamic task-chaining, consider subclassing this class and rewriting the logic
    );

    enabled = true;
    plugin.getSLF4JLogger().info("{} module enabled", taskClass.getSimpleName());
  }

  @Override
  public void disable() {
    if (!enabled) return;
    if (task != null) task.cancel();
    enabled = false;
    plugin.getSLF4JLogger().info("{} module disabled", taskClass.getSimpleName());
  }

  @Override
  public boolean isEnabled() {
    return enabled;
  }
}
