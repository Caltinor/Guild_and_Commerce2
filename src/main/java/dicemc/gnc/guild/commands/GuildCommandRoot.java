package dicemc.gnc.guild.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.tree.LiteralCommandNode;

import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;

public class GuildCommandRoot {

	public static void register(CommandDispatcher<CommandSource> dispatcher) {
		LiteralCommandNode<CommandSource> cmdSRC = dispatcher.register(Commands.literal("acccount")
				.then(GuildCommandInfo.register(dispatcher))
				.then(GuildCommandLeave.register(dispatcher)));
	}
}
