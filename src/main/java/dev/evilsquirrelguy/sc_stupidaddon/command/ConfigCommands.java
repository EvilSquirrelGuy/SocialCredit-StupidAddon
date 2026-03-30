/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package dev.evilsquirrelguy.sc_stupidaddon.command;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.tree.LiteralCommandNode;
import dev.evilsquirrelguy.sc_stupidaddon.SocialCreditStupidAddon;
import dev.evilsquirrelguy.sc_stupidaddon.module.Module;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import org.bukkit.command.CommandSender;

import java.util.List;
import java.util.Objects;

public class ConfigCommands {
  private final SocialCreditStupidAddon plugin;

  public ConfigCommands(SocialCreditStupidAddon plugin) {
    this.plugin = plugin;
  }

  @SuppressWarnings("UnstableApiUsage") // shush.
  public LiteralCommandNode<CommandSourceStack> createCommand(final String commandName) {

    return Commands.literal(commandName)
        // piggyback off socialcredit admin permission node
        .requires(source -> source.getSender().hasPermission("socialcredit.admin"))
        // reload subcommand
        .then(Commands.literal("reload")
            .executes( ctx -> {
              // literally just reload config
              plugin.reloadConfig();
              return Command.SINGLE_SUCCESS;
          })
        )
        // module management
        .then(Commands.literal("module")
            .executes(ctx -> {
              List<String> moduleIds = plugin.getModuleManager().getModuleIdentifiers();
              // use colours to show if modules are enabled
              List<String> colouredModules = moduleIds.stream().map(module -> {
                if (plugin.getModuleManager().getModule(module).isEnabled()) {
                  return "<green>" + module + "</green>";
                } else {
                  return "<red>" + module + "</red>";
                }
              }).sorted().toList();

              CommandSender src = Objects.requireNonNullElse(ctx.getSource().getExecutor(), ctx.getSource().getSender());

              src.sendRichMessage("<yellow>The follwing modules are available:</yellow>");
              src.sendRichMessage(String.join(", ", colouredModules));

              return Command.SINGLE_SUCCESS;
            })
            .then(Commands.argument("module", StringArgumentType.word())
                // suggest modules
                .suggests((ctx, builder) -> {
                  for (String module : plugin.getModuleManager().getModuleIdentifiers()) {
                    builder.suggest(module);
                  }
                  return builder.buildFuture();
                })
                // provide enable/disable subcommand
                .then(Commands.literal("enable")
                    .executes(ctx -> {
                      String moduleName = StringArgumentType.getString(ctx, "module");
                      Module mod = plugin.getModuleManager().getModule(moduleName);

                      CommandSender src = Objects.requireNonNullElse(ctx.getSource().getExecutor(), ctx.getSource().getSender());

                      if (mod == null) {
                        src.sendRichMessage("<red>Error: No such module <gold>%s</gold></red>".formatted(moduleName));
                      } else if (mod.isEnabled()) {
                        src.sendRichMessage("<yellow>Module <gold>%s</gold> already enabled.</yellow>".formatted(moduleName));
                      } else {
                        mod.enable();
                        src.sendRichMessage("<green>Enabled</green><yellow> module <gold>%s</gold></yellow>".formatted(moduleName));
                      }
                      return Command.SINGLE_SUCCESS;
                    })
                )
                .then(Commands.literal("disable")
                    .executes(ctx -> {
                      String moduleName = StringArgumentType.getString(ctx, "module");
                      Module mod = plugin.getModuleManager().getModule(moduleName);

                      CommandSender src = Objects.requireNonNullElse(ctx.getSource().getExecutor(), ctx.getSource().getSender());

                      if (mod == null) {
                        src.sendRichMessage("<red>Error: No such module <gold>%s</gold></red>".formatted(moduleName));
                      } else if (!mod.isEnabled()) {
                        src.sendRichMessage("<yellow>Module <gold>%s</gold> already disabled.</yellow>".formatted(moduleName));
                      } else {
                        mod.disable();
                        src.sendRichMessage("<red>Disabled</red><yellow> module <gold>%s</gold></yellow>".formatted(moduleName));
                      }
                      return Command.SINGLE_SUCCESS;
                    })
                )
            )
        )
        .build();
  }
}
