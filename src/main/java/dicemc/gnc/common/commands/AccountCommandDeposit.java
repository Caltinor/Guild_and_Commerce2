package dicemc.gnc.common.commands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;

import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;

public class AccountCommandDeposit implements Command<CommandSource>{
	private static final AccountCommandDeposit CMD = new AccountCommandDeposit();
	
	public static ArgumentBuilder<CommandSource, ?> register(CommandDispatcher<CommandSource> dispatcher) {
		return Commands.literal("deposit").executes(CMD);
	}

	@Override
	public int run(CommandContext<CommandSource> context) throws CommandSyntaxException {
		// TODO Auto-generated method stub
		return 0;
	}	

}
