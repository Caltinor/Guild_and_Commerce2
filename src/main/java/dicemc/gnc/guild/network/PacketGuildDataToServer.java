package dicemc.gnc.guild.network;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

import dicemc.gnc.GnC;
import dicemc.gnc.guild.Guild;
import dicemc.gnc.guild.GuildManager.guildUpdates;
import dicemc.gnc.guild.client.GuiGuildManager;
import dicemc.gnc.setup.Networking;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

public class PacketGuildDataToServer {
	private final Guild guild;
	private List<guildUpdates> changes = new ArrayList<guildUpdates>();
	
	public PacketGuildDataToServer(PacketBuffer buf) {
		guild = new Guild(buf.readCompoundTag());
		int size = buf.readInt();
		for (int i = 0; i < size; i++) changes.add(guildUpdates.values()[buf.readInt()]);
	}
	
	public PacketGuildDataToServer(Guild guild, List<guildUpdates> changes) {
		this.guild = guild;
		this.changes = changes;
	}
	
	public void toBytes(PacketBuffer buf) {
		buf.writeCompoundTag(guild.toNBT());
		buf.writeInt(changes.size());
		for (int i = 0; i < changes.size(); i++) buf.writeInt(changes.get(i).ordinal());
	}
 	
	public boolean handle(Supplier<NetworkEvent.Context> ctx) {
		ctx.get().enqueueWork(() -> {
			GnC.gMgr.setGuild(guild, changes);
			double worth = GnC.gMgr.guildWorth(guild.guildID);
			double taxable = GnC.gMgr.taxableWorth(guild.guildID);
			double balG = GnC.aMgr.getBalance(guild.guildID);
			int core = GnC.gMgr.landCoreCount(guild.guildID);
			int outpost = GnC.gMgr.landOutpostCount(guild.guildID);
			Networking.sendToClient(new PacketUpdateGuiGuild(GnC.gMgr.getGuildByID(guild.guildID), worth, taxable, balG, core, outpost), ctx.get().getSender());
		});
		return true;
	}
}
