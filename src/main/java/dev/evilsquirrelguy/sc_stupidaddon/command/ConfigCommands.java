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

                      if (mod == null) {
                        ctx.getSource().getExecutor().sendRichMessage("<red>Error: No such module <gold>%s</gold></red>".formatted(moduleName));
                      } else if (mod.isEnabled()) {
                        ctx.getSource().getExecutor().sendRichMessage("<yellow>Module <gold>%s</gold> already enabled.</yellow>".formatted(moduleName));
                      } else {
                        mod.enable();
                        ctx.getSource().getExecutor().sendRichMessage("<green>Enabled</green><yellow> module <gold>%s</gold></yellow>".formatted(moduleName));
                      }
                      return Command.SINGLE_SUCCESS;
                    })
                )
                .then(Commands.literal("disable")
                    .executes(ctx -> {
                      String moduleName = StringArgumentType.getString(ctx, "module");
                      Module mod = plugin.getModuleManager().getModule(moduleName);

                      if (mod == null) {
                        ctx.getSource().getExecutor().sendRichMessage("<red>Error: No such module <gold>%s</gold></red>".formatted(moduleName));
                      } else if (!mod.isEnabled()) {
                        ctx.getSource().getExecutor().sendRichMessage("<yellow>Module <gold>%s</gold> already disabled.</yellow>".formatted(moduleName));
                      } else {
                        mod.disable();
                        ctx.getSource().getExecutor().sendRichMessage("<red>Disabled</red><yellow> module <gold>%s</gold></yellow>".formatted(moduleName));
                      }
                      return Command.SINGLE_SUCCESS;
                    })
                )
            )
        )
        .build();
  }
}
