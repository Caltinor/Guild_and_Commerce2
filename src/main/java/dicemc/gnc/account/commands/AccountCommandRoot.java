package dicemc.gnc.account.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.tree.LiteralCommandNode;

import dicemc.gnc.common.commands.AccountCommandDeposit;
import dicemc.gnc.common.commands.AccountCommandWithdraw;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;

public class AccountCommandRoot {

	public static void register(CommandDispatcher<CommandSource> dispatcher) {
		LiteralCommandNode<CommandSource> cmdSRC = dispatcher.register(Commands.literal("acccount")
				.then(AccountCommandDeposit.register(dispatcher))
				.then(AccountCommandWithdraw.register(dispatcher)));
		
		dispatcher.register(Commands.literal("acct").redirect(cmdSRC));
	}
}
