package dicemc.gnc.land.network;

import java.util.function.Supplier;

import dicemc.gnc.land.client.GuiLandWidget;
import dicemc.gnclib.guilds.entries.Guild;
import dicemc.gnclib.realestate.ChunkData;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

public class PacketOpenGui_Land {
	private final double balP;
	private final double balG;
	private final Guild guild;
	private final ChunkData ckData;

	public PacketOpenGui_Land(PacketBuffer buf) {
		this.balG = buf.readDouble();
		this.balP = buf.readDouble();
		this.guild = Guild.getDefault();
		guild.readBytes(buf);
		ckData = ChunkData.getPlaceholder();
		ckData.readBytes(buf);
	}
	
	public PacketOpenGui_Land(double balG, double balP, Guild guild, ChunkData data) {
		this.balG = balG;
		this.balP = balP;
		this.guild = guild;
		this.ckData = data;
	}
	
	public void toBytes(PacketBuffer buf) {
		buf.writeDouble(balG);
		buf.writeDouble(balP);
		guild.writeBytes(buf);
		ckData.writeBytes(buf);
	}

	public boolean handle(Supplier<NetworkEvent.Context> ctx) {
		ctx.get().enqueueWork(() -> {
			GuiLandWidget.sync(balG, balP, guild, ckData);
		});
		return true;
	}
}
