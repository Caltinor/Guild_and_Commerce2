package dicemc.gnc.common;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.function.Consumer;
import java.util.function.Supplier;

import dicemc.gnc.GnC;
import dicemc.gnc.guild.Guild;
import dicemc.gnc.guild.network.PacketOpenGui_NoGuild;
import dicemc.gnc.land.network.PacketOpenGui_Land;
import dicemc.gnc.land.client.GuiLandManager.ChunkSummary;
import dicemc.gnc.setup.Networking;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.fml.network.NetworkEvent;

public class PacketGuiRequest {
	private final gui action;
	
	public enum gui {
		CHUNK(ctx -> {
			UUID gid = GnC.NIL;
			Guild guild = new Guild("N/A", GnC.NIL, false);
			if (GnC.gMgr.getGuildByMember(ctx.get().getSender().getUniqueID()) != null) {
				guild = GnC.gMgr.getGuildByMember(ctx.get().getSender().getUniqueID());
			}
			ServerWorld server = ctx.get().getSender().getServerWorld();
			ChunkPos center = new ChunkPos(ctx.get().getSender().chunkCoordX, ctx.get().getSender().chunkCoordZ);
			List<ChunkSummary> chunkData = new ArrayList<ChunkSummary>();
			for (int x = center.x-6; x <= center.x+6; x++) {
				for (int z = center.z-6; z <= center.z+6; z++) {
					ChunkPos pos = new ChunkPos(x, z);
					chunkData.add(new ChunkSummary(GnC.ckMgr.get(ctx.get().getSender().getEntityWorld().func_234923_W_()).getChunk(pos), ownerName(pos, server, ctx)));
				}
			}
			//Start mapColors gathering
			int baseX = center.getXStart()-(16*5);
			int baseZ = center.getZStart()-(16*5);
			int[][] mapColors = new int[176][176];
			int start = (ctx.get().getSender().getEntityWorld().func_234923_W_().equals(World.field_234919_h_) ? (int) ctx.get().getSender().getPosY() : 255);
			start = (start > 255) ? 255 : start;
			for (int x = baseX; x < baseX+176; x++) {
				for (int z = baseZ; z < baseZ+176; z++) {
					for (int y = start; y >= 0; y--) {
						if (!server.getBlockState(new BlockPos(x, y, z)).isAir()) {
							mapColors[x-baseX][z-baseZ] = server.getBlockState(new BlockPos(x, y, z)).getMaterialColor(server.getWorld(), new BlockPos(x, y, z)).colorValue;
							break;
						}
					}
				}
			}
			//end mapColors gathering
			double balG = gid.equals(GnC.NIL) ? 0.0 : GnC.aMgr.getBalance(gid);
			double balP = GnC.aMgr.getBalance(ctx.get().getSender().getUniqueID());
			Networking.sendToClient(new PacketOpenGui_Land(balG, balP, guild, mapColors, center, chunkData), ctx.get().getSender()); 
		}),
		MARKET(ctx -> {
			
		}),
		GUILD(ctx -> {
			Guild guild = GnC.gMgr.getGuildByMember(ctx.get().getSender().getUniqueID());
			double balP = GnC.aMgr.getBalance(ctx.get().getSender().getUniqueID());
			if (guild == null) {
				List<String> invites = GnC.gMgr.getInvitedGuilds(ctx.get().getSender().getUniqueID());
				List<String> openGuilds = GnC.gMgr.getOpenGuilds(ctx.get().getSender().getUniqueID());
				Networking.sendToClient(new PacketOpenGui_NoGuild(balP, invites, openGuilds), ctx.get().getSender());
			}
			else {
				System.out.println("Uhm.  how TF do you have a guild?");
			}
		});
		
		public final Consumer<Supplier<NetworkEvent.Context>> packetHandler;
		
		gui(Consumer<Supplier<NetworkEvent.Context>> packetHandler) {this.packetHandler = packetHandler;}
	
		static String ownerName(ChunkPos pos, ServerWorld server, Supplier<NetworkEvent.Context> ctx) {
			if (GnC.ckMgr.get(ctx.get().getSender().getEntityWorld().func_234923_W_()).getChunk(pos).owner.equals(GnC.NIL) &&
				GnC.ckMgr.get(ctx.get().getSender().getEntityWorld().func_234923_W_()).getChunk(pos).renter.equals(GnC.NIL)) {
				return "Unowned";
			}
			if (GnC.gMgr.getGuildByID(GnC.ckMgr.get(ctx.get().getSender().getEntityWorld().func_234923_W_()).getChunk(pos).owner) != null) {
				return GnC.gMgr.getGuildByID(GnC.ckMgr.get(ctx.get().getSender().getEntityWorld().func_234923_W_()).getChunk(pos).owner).name;
			}
			if (GnC.ckMgr.get(ctx.get().getSender().getEntityWorld().func_234923_W_()).getChunk(pos).owner.equals(GnC.NIL) &&
				!GnC.ckMgr.get(ctx.get().getSender().getEntityWorld().func_234923_W_()).getChunk(pos).renter.equals(GnC.NIL)) {
				return server.getPlayerByUuid(GnC.ckMgr.get(ctx.get().getSender().getEntityWorld().func_234923_W_()).getChunk(pos).renter).getScoreboardName();
			}
			return "Logical Error";
		}
	}
	
	public PacketGuiRequest(PacketBuffer buf) {action = gui.values()[buf.readInt()];}
	
	public PacketGuiRequest(gui guiType) {this.action = guiType;}
	
	public void toBytes(PacketBuffer buf) {buf.writeInt(action.ordinal());}
 	
	public boolean handle(Supplier<NetworkEvent.Context> ctx) {
		ctx.get().enqueueWork(() -> {this.action.packetHandler.accept(ctx);});
		return true;
	}
	
	
}
