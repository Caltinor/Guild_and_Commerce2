package dicemc.gnc.land.network;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

import dicemc.gnc.guild.Guild;
import dicemc.gnc.land.client.GuiLandManager;
import dicemc.gnc.land.client.GuiLandManager.ChunkSummary;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.math.ChunkPos;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.fml.network.NetworkEvent;

public class PacketOpenGui_Land {
	private final double balP;
	private final double balG;
	private final ChunkPos center;
	private final Guild guild;
	private final Map<ChunkPos, ChunkSummary> chunkData;
	//String response, double balG, double balP, Guild guild, Map<ChunkPos, ChunkSummary> chunkData, ChunkPos center
	public PacketOpenGui_Land(PacketBuffer buf) {
		chunkData = new HashMap<ChunkPos, ChunkSummary>();
		this.balG = buf.readDouble();
		this.balP = buf.readDouble();
		this.guild = new Guild(buf.readCompoundTag());
		ListNBT list = buf.readCompoundTag().getList("data", Constants.NBT.TAG_COMPOUND);
		for (int i = 0; i < list.size(); i++) {
			chunkData.put(new ChunkPos(list.getCompound(i).getLong("pos")), new ChunkSummary(list.getCompound(i).getCompound("sum")));
		}
		this.center = new ChunkPos(buf.readLong());
	}
	
	public PacketOpenGui_Land(double balG, double balP, Guild guild, Map<ChunkPos, ChunkSummary> chunkData, ChunkPos center) {
		this.balG = balG;
		this.balP = balP;
		this.guild = guild;
		this.chunkData = chunkData;
		this.center = center;
	}
	
	public void toBytes(PacketBuffer buf) {
		buf.writeDouble(balG);
		buf.writeDouble(balP);
		buf.writeCompoundTag(guild.toNBT());
		CompoundNBT nbt = new CompoundNBT();
		ListNBT list = new ListNBT();
		if (chunkData.size() > 0) { for (Map.Entry<ChunkPos, ChunkSummary> entry : chunkData.entrySet()) {
			CompoundNBT snbt = new CompoundNBT();
			snbt.putLong("pos", entry.getKey().asLong());
			snbt.put("sum", entry.getValue().toNBT());
			list.add(snbt);
		}}
		nbt.put("data", list);
		buf.writeCompoundTag(nbt);
		buf.writeLong(center.asLong());
	}

	public boolean handle(Supplier<NetworkEvent.Context> ctx) {
		ctx.get().enqueueWork(() -> {
			GuiLandManager.open(balG, balP, guild, chunkData, center);
		});
		return true;
	}
}
