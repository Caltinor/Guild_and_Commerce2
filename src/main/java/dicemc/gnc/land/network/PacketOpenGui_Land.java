package dicemc.gnc.land.network;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
	private final List<ChunkSummary> chunkData;
	private final int[][] mapColors;

	public PacketOpenGui_Land(PacketBuffer buf) {
		this.balG = buf.readDouble();
		this.balP = buf.readDouble();
		this.center = new ChunkPos(buf.readLong());
		this.guild = new Guild(buf.readCompoundTag());
		int size = buf.readInt();
		this.chunkData = new ArrayList<ChunkSummary>();
		for (int i = 0; i < size; i++) {this.chunkData.add(new ChunkSummary(buf.readCompoundTag()));}		
		mapColors = new int[176][176];
		for (int x = 0; x < 176; x++) {
			for (int z = 0; z < 176; z++) {
				mapColors[x][z] = buf.readInt();
			}
		}
	}
	
	public PacketOpenGui_Land(double balG, double balP, Guild guild, int[][] mapColors, ChunkPos center, List<ChunkSummary> summary) {
		this.balG = balG;
		this.balP = balP;
		this.guild = guild;
		this.chunkData = summary;
		this.center = center;
		this.mapColors = mapColors;
	}
	
	public void toBytes(PacketBuffer buf) {
		buf.writeDouble(balG);
		buf.writeDouble(balP);
		buf.writeLong(center.asLong());
		buf.writeCompoundTag(guild.toNBT());
		buf.writeInt(chunkData.size());
		for (int i = 0; i < chunkData.size(); i++) {buf.writeCompoundTag(chunkData.get(i).toNBT());}
		for (int x = 0; x < 176; x++) {
			for (int z = 0; z < 176; z++) {
				buf.writeInt(mapColors[x][z]);
			}
		}
	}

	public boolean handle(Supplier<NetworkEvent.Context> ctx) {
		ctx.get().enqueueWork(() -> {
			GuiLandManager.open(balG, balP, guild, mapColors, center, chunkData);
		});
		return true;
	}
}
