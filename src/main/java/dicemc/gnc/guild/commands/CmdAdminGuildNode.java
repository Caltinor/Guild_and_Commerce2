package dicemc.gnc.guild.commands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;

import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.util.text.StringTextComponent;

public class CmdAdminGuildNode implements Command<CommandSource>{
	private static final CmdAdminGuildNode CMD = new CmdAdminGuildNode();
	
	public static ArgumentBuilder<CommandSource, ?> register(CommandDispatcher<CommandSource> dispatcher) {
		return Commands.literal("guild").executes(CMD);
	}

	@Override
	public int run(CommandContext<CommandSource> context) throws CommandSyntaxException {
		context.getSource().sendSuccess(new StringTextComponent("[Command WIP] Action: accesses guild functions"), false);
		return 0;
	}
}
