package dicemc.gnc.common.commands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;

import dicemc.gnc.account.commands.CmdAdminAccountNode;
import dicemc.gnc.guild.commands.CmdAdminGuildNode;
import dicemc.gnc.land.commands.CmdAdminLandNode;
import dicemc.gnc.market.commands.CmdAdminMarketNode;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.util.text.StringTextComponent;

public class CmdAdminRoot implements Command<CommandSource>{
	private static final CmdAdminRoot CMD = new CmdAdminRoot();

	public static void register(CommandDispatcher<CommandSource> dispatcher) {
		dispatcher.register(Commands.literal("gncadmin")
				.then(CmdAdminLandNode.register(dispatcher))
				.then(CmdAdminAccountNode.register(dispatcher))
				.then(CmdAdminGuildNode.register(dispatcher))
				.then(CmdAdminMarketNode.register(dispatcher))
				.then(Commands.literal("gui")
						.executes(CMD))
				.executes(CMD));
	}
	
	@Override
	public int run(CommandContext<CommandSource> context) throws CommandSyntaxException {
		if (context.getSource().getPlayerOrException() != null) {
			context.getSource().sendSuccess(new StringTextComponent("[Command WIP] Action: Opens Admin GUI"), false);
			//TODO open admin gui
		}
		return 0;
	}
}
