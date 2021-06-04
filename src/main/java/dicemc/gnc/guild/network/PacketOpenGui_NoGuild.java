package dicemc.gnc.guild.network;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

import dicemc.gnc.guild.client.GuiNoGuild;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

public class PacketOpenGui_NoGuild {
	private final double balP;
	private List<String> invites = new ArrayList<String>();
	private List<String> openGuilds = new ArrayList<String>();
	
	public PacketOpenGui_NoGuild(PacketBuffer buf) {
		balP = buf.readDouble();
		int size = buf.readInt();
		for (int i = 0; i < size; i++) {invites.add(buf.readUtf(32767));}
		size = buf.readInt();
		for (int i = 0; i < size; i++) {openGuilds.add(buf.readUtf(32767));}
	}
	
	public PacketOpenGui_NoGuild(double balP, List<String> invites, List<String> openGuilds) {
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
			GuiNoGuild.open(balP, invites, openGuilds);
		});
		return true;
	}
}
 