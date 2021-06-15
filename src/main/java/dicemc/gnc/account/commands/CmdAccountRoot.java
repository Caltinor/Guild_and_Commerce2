package dicemc.gnc.account.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.tree.LiteralCommandNode;

import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;

public class CmdAccountRoot {

	public static void register(CommandDispatcher<CommandSource> dispatcher) {
		LiteralCommandNode<CommandSource> cmdSRC = dispatcher.register(Commands.literal("acccount")
				.then(CmdAccountDeposit.register(dispatcher))
				.then(CmdAccountWithdraw.register(dispatcher)));
		
		dispatcher.register(Commands.literal("acct").redirect(cmdSRC));
	}
}
