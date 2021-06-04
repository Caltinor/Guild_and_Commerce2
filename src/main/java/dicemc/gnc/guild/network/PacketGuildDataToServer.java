package dicemc.gnc.guild.network;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

import dicemc.gnc.GnC;
import dicemc.gnc.guild.client.GuiGuildManager;
import dicemc.gnc.setup.Networking;
import dicemc.gnclib.guilds.LogicGuilds;
import dicemc.gnclib.guilds.entries.Guild;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

public class PacketGuildDataToServer {
	private final Guild guild;
	
	public PacketGuildDataToServer(PacketBuffer buf) {
		guild = Guild.getDefault();
		guild.readBytes(buf);
	}
	
	public PacketGuildDataToServer(Guild guild) {
		this.guild = guild;
	}
	
	public void toBytes(PacketBuffer buf) {
		buf.writeBytes(guild.writeBytes(buf));
	}
 	
	public boolean handle(Supplier<NetworkEvent.Context> ctx) {
		ctx.get().enqueueWork(() -> {
			/*GnC.gMgr.setGuild(guild, changes);
			double worth = GnC.gMgr.guildWorth(guild.guildID);
			double taxable = GnC.gMgr.taxableWorth(guild.guildID);
			double balG = GnC.aMgr.getBalance(guild.guildID);
			int core = GnC.gMgr.landCoreCount(guild.guildID);
			int outpost = GnC.gMgr.landOutpostCount(guild.guildID);
			Networking.sendToClient(new PacketUpdateGuiGuild(LogicGuilds.getGuildByID(guild.guildID), worth, taxable, balG, core, outpost), ctx.get().getSender());*/
		});
		return true;
	}
}
