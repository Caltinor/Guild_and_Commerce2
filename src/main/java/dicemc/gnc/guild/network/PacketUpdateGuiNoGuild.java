package dicemc.gnc.guild.network;

import java.util.List;
import java.util.function.Supplier;

import dicemc.gnc.guild.client.GuiNoGuild;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

public class PacketUpdateGuiNoGuild {
	private final double balP;
	private List<String> invites, openGuilds;
	
	public PacketUpdateGuiNoGuild(PacketBuffer buf) {
		balP = buf.readDouble();
		int size = buf.readInt();
		for (int i = 0; i < size; i++) {invites.add(buf.readUtf(32767));}
		size = buf.readInt();
		for (int i = 0; i < size; i++) {openGuilds.add(buf.readUtf(32767));}
	}
	
	public PacketUpdateGuiNoGuild(double balP, List<String> invites, List<String> openGuilds) {
		this.balP = balP;
		this.invites = invites;
		this.openGuilds = openGuilds;
	}
	
	public void toBytes(PacketBuffer buf) {
		buf.writeDouble(balP);
		buf.writeInt(invites.size());
		for (int i = 0; i < invites.size(); i++) {buf.writeUtf(invites.get(i));}
		buf.writeInt(openGuilds.size());
		for (int i = 0; i < openGuilds.size(); i++) {buf.writeUtf(openGuilds.get(i));}
	}
 	
	public boolean handle(Supplier<NetworkEvent.Context> ctx) {
		ctx.get().enqueueWork(() -> {
			GuiNoGuild.sync(balP, invites, openGuilds);
		});
		return true;
	}
}
