package dicemc.gnc.land.network;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

import dicemc.gnclib.guilds.entries.Guild;
import dicemc.gnclib.realestate.ChunkData;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

public class PacketUpdateChunkManagerGui {
	private final double balG;
	private final double balP;
	private final Guild guild;
	private final String resp;
	private List<ChunkData> summary = new ArrayList<ChunkData>();
	
	public PacketUpdateChunkManagerGui(PacketBuffer buf) {
		balG = buf.readDouble();
		balP = buf.readDouble();
		guild = Guild.getDefault();
		guild.readBytes(buf);
		resp = buf.readUtf(32767);
		int size = buf.readInt();
		for (int i = 0; i < size; i++) {
			ChunkData data = ChunkData.getPlaceholder();
			data.readBytes(buf);
			summary.add(data);
		}
	}
	
	public PacketUpdateChunkManagerGui(double balG, double balP, Guild guild, List<ChunkData> summary, String resp) {
		this.balG = balG;
		this.balP = balP;
		this.guild = guild;
		this.resp = resp;
		this.summary = summary;
	}
	
	public void toBytes(PacketBuffer buf) {
		buf.writeDouble(balG);
		buf.writeDouble(balP);
		buf.writeBytes(guild.writeBytes(buf));
		buf.writeUtf(resp);
		buf.writeInt(summary.size());
		for (int i = 0; i < summary.size(); i++) {
			buf.writeBytes(summary.get(i).writeBytes(buf));
		}
	}
 	
	public boolean handle(Supplier<NetworkEvent.Context> ctx) {
		ctx.get().enqueueWork(() -> {
			//GuiLandWidget.sync(balG, balP, guild, summary, resp);
		});
		return true;
	}
}
