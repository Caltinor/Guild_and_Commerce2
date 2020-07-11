package dicemc.gnc.command.impl;

import java.util.List;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;

import dicemc.gnc.common.TestEventHandler;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.util.text.StringTextComponent;

public class SetHostCommand implements Command<CommandSource>{
	
	private static final SetHostCommand CMD = new SetHostCommand();
	
	public static ArgumentBuilder<CommandSource, ?> register(CommandDispatcher<CommandSource> dispatcher) {
		return Commands.literal("getresults").executes(CMD);
	}

	@Override
	public int run(CommandContext<CommandSource> context) throws CommandSyntaxException {
		List<String> sqlResults = TestEventHandler.dbConn.getResults();
		for (int i = 0; i < sqlResults.size(); i++) {
			context.getSource().sendFeedback(new StringTextComponent(sqlResults.get(i)), false);
		}
		return 0;
	}

}
