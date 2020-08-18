package dicemc.gnc.land.network;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

import dicemc.gnc.guild.Guild;
import dicemc.gnc.land.client.GuiLandManager;
import dicemc.gnc.land.client.GuiLandManager.ChunkSummary;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

public class PacketUpdateChunkManagerGui {
	private final double balG;
	private final double balP;
	private final Guild guild;
	private final String resp;
	private List<ChunkSummary> summary = new ArrayList<ChunkSummary>();
	
	public PacketUpdateChunkManagerGui(PacketBuffer buf) {
		balG = buf.readDouble();
		balP = buf.readDouble();
		guild = new Guild(buf.readCompoundTag());
		resp = buf.readString();
		int size = buf.readInt();
		for (int i = 0; i < size; i++) {
			summary.add(new ChunkSummary(buf.readCompoundTag()));
		}
	}
	
	public PacketUpdateChunkManagerGui(double balG, double balP, Guild guild, List<ChunkSummary> summary, String resp) {
		this.balG = balG;
		this.balP = balP;
		this.guild = guild;
		this.resp = resp;
		this.summary = summary;
	}
	
	public void toBytes(PacketBuffer buf) {
		buf.writeDouble(balG);
		buf.writeDouble(balP);
		buf.writeCompoundTag(guild.toNBT());
		buf.writeString(resp);
		buf.writeInt(summary.size());
		for (int i = 0; i < summary.size(); i++) {
			buf.writeCompoundTag(summary.get(i).toNBT());
		}
	}
 	
	public boolean handle(Supplier<NetworkEvent.Context> ctx) {
		ctx.get().enqueueWork(() -> {
			GuiLandManager.sync(balG, balP, guild, summary, resp);
		});
		return true;
	}
}
