package dicemc.gnc.testmaterial;

import java.util.List;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;

import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.util.text.StringTextComponent;

public class SortMarketsCommand implements Command<CommandSource>{
	
	private static final SortMarketsCommand CMD = new SortMarketsCommand();
	
	public static ArgumentBuilder<CommandSource, ?> register(CommandDispatcher<CommandSource> dispatcher) {
		return Commands.literal("sortMarkets")
				.then(Commands.argument("sort", BoolArgumentType.bool())
						.executes(CMD));
	}

	@Override
	public int run(CommandContext<CommandSource> context) throws CommandSyntaxException {
		TestEventHandler.dbConn.callSortedPriceList(BoolArgumentType.getBool(context, "sort"));
		List<String> sqlResults = TestEventHandler.dbConn.getResults();
		for (int i = 0; i < sqlResults.size(); i++) {
			context.getSource().sendFeedback(new StringTextComponent(sqlResults.get(i)), false);
		}
		return 0;
	}

}
