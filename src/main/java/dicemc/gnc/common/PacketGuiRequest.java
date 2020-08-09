package dicemc.gnc.common;

import java.awt.Color;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.function.Supplier;

import dicemc.gnc.GnC;
import dicemc.gnc.account.AccountUtils;
import dicemc.gnc.guild.Guild;
import dicemc.gnc.land.network.PacketOpenGui_Land;
import dicemc.gnc.land.client.GuiLandManager.ChunkSummary;
import dicemc.gnc.setup.Networking;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.fml.network.NetworkEvent;

public class PacketGuiRequest {

	private final int gui;
	
	public PacketGuiRequest(PacketBuffer buf) {gui = buf.readInt();}
	
	public PacketGuiRequest(int guiType) {this.gui = guiType;}
	
	public void toBytes(PacketBuffer buf) {buf.writeInt(gui);}
 	
	public boolean handle(Supplier<NetworkEvent.Context> ctx) {
		ctx.get().enqueueWork(() -> {
			switch(gui) {
			case 0: {
				UUID gid = GnC.NIL;
				Guild guild = new Guild("N/A", GnC.NIL);
				if (GnC.gMgr.getGuildByMember(ctx.get().getSender().getUniqueID()) != null) {
					guild = GnC.gMgr.getGuildByMember(ctx.get().getSender().getUniqueID());
				}
				ServerWorld server = ctx.get().getSender().getServerWorld();
				ChunkPos center = new ChunkPos(ctx.get().getSender().chunkCoordX, ctx.get().getSender().chunkCoordZ);
				Map<ChunkPos, ChunkSummary> chunkData = new HashMap<ChunkPos, ChunkSummary>();
				for (int xck = (center.x-6); xck <= (center.x+6); xck++) {
					for (int zck = (center.z-6); zck <= (center.z+6); zck++) {
						ChunkPos pos = new ChunkPos(xck, zck);
						Map<BlockPos, Color> mapGrid = new HashMap<BlockPos, Color>();
						for (int x = pos.getXStart(); x <= pos.getXEnd(); x++) {
							for (int z = pos.getZStart(); z <= pos.getZEnd(); z++) {
								Color color = new Color(16777215);
								BlockPos bp = new BlockPos(x, 255, z);
								for (int i = 255; i >= 0; i--) {
									bp = new BlockPos(x, i, z);
									if (!server.getBlockState(bp).isAir()) {break;}
								}
								color = new Color(server.getBlockState(bp).getMaterialColor(server.getWorld(), bp).colorValue);
								mapGrid.put(new BlockPos(x, 0, z), color);
							}
						}
						String gName = "Unowned";
						if (GnC.ckMgr.getChunk(pos).owner.equals(GnC.NIL)) {}
						else if (GnC.gMgr.getGuildByID(GnC.ckMgr.getChunk(pos).owner) != null) {gName = GnC.gMgr.getGuildByID(GnC.ckMgr.getChunk(pos).owner).name;}
						else if (server.getPlayerByUuid(GnC.ckMgr.getChunk(pos).owner) != null) {gName = server.getPlayerByUuid(GnC.ckMgr.getChunk(pos).owner).getDisplayName().toString();}
						ChunkSummary sum = new ChunkSummary(GnC.ckMgr.getChunk(pos), gName, mapGrid);
						chunkData.put(pos, sum);
					}					
				}
				double balG = gid.equals(GnC.NIL) ? 0.0 : AccountUtils.getBalance(gid);
				double balP = AccountUtils.getBalance(ctx.get().getSender().getUniqueID());
				Networking.sendToClient(new PacketOpenGui_Land(balG, balP, guild, chunkData, center), ctx.get().getSender()); 
				break;
			}
			case 1: {break;}
			case 2: {break;}
			default:
			}
			
		});
		return true;
	}
}
