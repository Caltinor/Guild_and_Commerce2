package dicemc.gnc.testmaterial;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.tree.LiteralCommandNode;

import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;

public class DBCommands {
	
	public static void register(CommandDispatcher<CommandSource> dispatcher) {
		LiteralCommandNode<CommandSource> cmdSRC = dispatcher.register(Commands.literal("db")
				.then(SetHostCommand.register(dispatcher))
				.then(SortMarketsCommand.register(dispatcher)));
		
		dispatcher.register(Commands.literal("database").redirect(cmdSRC));
	}
}

//.then(CommandClass.register(dispatcher)
