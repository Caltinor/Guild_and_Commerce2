package dicemc.gnc.guild.network;

import java.util.function.Supplier;

import dicemc.gnclib.guilds.entries.Guild;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

public class PacketOpenGui_Guild {
	private final int coreCount, outpostCount;
	private final double worth, taxableWorth, balG;
	private final Guild guild;
	
	public PacketOpenGui_Guild(PacketBuffer buf) {
		coreCount = buf.readInt();
		outpostCount = buf.readInt();
		worth = buf.readDouble();
		taxableWorth = buf.readDouble();
		balG = buf.readDouble();
		guild = Guild.getDefault();
		guild.readBytes(buf);
	}
	
	public PacketOpenGui_Guild(Guild guild, double worth, double taxableWorth, double balG, int coreCount, int outpostCount) {
		this.guild = guild;
		this.worth = worth;
		this.taxableWorth = taxableWorth;
		this.balG = balG;
		this.coreCount = coreCount;
		this.outpostCount = outpostCount;
	}
	
	public void toBytes(PacketBuffer buf) {
		buf.writeInt(coreCount);
		buf.writeInt(outpostCount);
		buf.writeDouble(worth);
		buf.writeDouble(taxableWorth);
		buf.writeDouble(balG);
		buf.writeBytes(guild.writeBytes(buf));
	}
 	
	public boolean handle(Supplier<NetworkEvent.Context> ctx) {
		ctx.get().enqueueWork(() -> {
			//GuiGuildManager.open(guild, worth, taxableWorth, balG, coreCount, outpostCount);
		});
		return true;
	}
}
