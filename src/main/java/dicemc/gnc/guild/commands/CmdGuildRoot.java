package dicemc.gnc.guild.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.tree.LiteralCommandNode;

import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;

public class CmdGuildRoot {

	public static void register(CommandDispatcher<CommandSource> dispatcher) {
		LiteralCommandNode<CommandSource> cmdSRC = dispatcher.register(Commands.literal("acccount")
				.then(CmdGuildInfo.register(dispatcher))
				.then(CmdGuildLeave.register(dispatcher)));
	}
}
