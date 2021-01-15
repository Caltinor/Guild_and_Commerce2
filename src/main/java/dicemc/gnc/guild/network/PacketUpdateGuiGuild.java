package dicemc.gnc.guild.network;

import java.util.function.Supplier;

import dicemc.gnc.guild.Guild;
import dicemc.gnc.guild.client.GuiGuildManager;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

public class PacketUpdateGuiGuild {
	private final int coreCount, outpostCount;
	private final double worth, taxableWorth, balG;
	private final Guild guild;
	
	public PacketUpdateGuiGuild(PacketBuffer buf) {
		coreCount = buf.readInt();
		outpostCount = buf.readInt();
		worth = buf.readDouble();
		taxableWorth = buf.readDouble();
		balG = buf.readDouble();
		guild = new Guild(buf.readCompoundTag());
	}
	
	public PacketUpdateGuiGuild(Guild guild, double worth, double taxableWorth, double balG, int coreCount, int outpostCount) {
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
		buf.writeCompoundTag(guild.toNBT());
	}
 	
	public boolean handle(Supplier<NetworkEvent.Context> ctx) {
		ctx.get().enqueueWork(() -> {
			GuiGuildManager.sync(guild, worth, taxableWorth, balG, coreCount, outpostCount);
		});
		return true;
	}
}
