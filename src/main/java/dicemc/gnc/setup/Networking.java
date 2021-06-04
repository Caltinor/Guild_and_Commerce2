package dicemc.gnc.setup;

import dicemc.gnc.GnC;
import dicemc.gnc.common.PacketGuiRequest;
import dicemc.gnc.guild.network.PacketGuildDataToServer;
import dicemc.gnc.guild.network.PacketNoGuildDataToServer;
import dicemc.gnc.guild.network.PacketOpenGui_Guild;
import dicemc.gnc.guild.network.PacketOpenGui_NoGuild;
import dicemc.gnc.guild.network.PacketUpdateGuiGuild;
import dicemc.gnc.guild.network.PacketUpdateGuiNoGuild;
import dicemc.gnc.land.network.PacketChunkDataToServer;
import dicemc.gnc.land.network.PacketOpenGui_Land;
import dicemc.gnc.land.network.PacketUpdateChunkManagerGui;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.network.NetworkDirection;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.simple.SimpleChannel;

public class Networking {
	
	private static SimpleChannel INSTANCE;
	private static int ID = 0;
	
	private static int nextID() {return ID++;}
	
	public static void registerMessages() {
		INSTANCE = NetworkRegistry.newSimpleChannel(new ResourceLocation(GnC.MOD_ID, "net"),
				() -> "1.0", 
				s -> true, 
				s -> true);
		//Common Packets
		INSTANCE.messageBuilder(PacketGuiRequest.class, nextID())
			.encoder(PacketGuiRequest::toBytes)
			.decoder(PacketGuiRequest::new)
			.consumer(PacketGuiRequest::handle)
			.add();
		//Land Packets
		INSTANCE.messageBuilder(PacketOpenGui_Land.class, nextID())
			.encoder(PacketOpenGui_Land::toBytes)
			.decoder(PacketOpenGui_Land::new)
			.consumer(PacketOpenGui_Land::handle)
			.add();		
		INSTANCE.messageBuilder(PacketChunkDataToServer.class, nextID())
			.encoder(PacketChunkDataToServer::toBytes)
			.decoder(PacketChunkDataToServer::new)
			.consumer(PacketChunkDataToServer::handle)
			.add();
		INSTANCE.messageBuilder(PacketUpdateChunkManagerGui.class, nextID())
			.encoder(PacketUpdateChunkManagerGui::toBytes)
			.decoder(PacketUpdateChunkManagerGui::new)
			.consumer(PacketUpdateChunkManagerGui::handle)
			.add();
		//Guild Packets
		INSTANCE.messageBuilder(PacketOpenGui_NoGuild.class, nextID())
			.encoder(PacketOpenGui_NoGuild::toBytes)
			.decoder(PacketOpenGui_NoGuild::new)
			.consumer(PacketOpenGui_NoGuild::handle)
			.add();
		INSTANCE.messageBuilder(PacketNoGuildDataToServer.class, nextID())
			.encoder(PacketNoGuildDataToServer::toBytes)
			.decoder(PacketNoGuildDataToServer::new)
			.consumer(PacketNoGuildDataToServer::handle)
			.add();
		INSTANCE.messageBuilder(PacketUpdateGuiNoGuild.class, nextID())
			.encoder(PacketUpdateGuiNoGuild::toBytes)
			.decoder(PacketUpdateGuiNoGuild::new)
			.consumer(PacketUpdateGuiNoGuild::handle)
			.add();
		INSTANCE.messageBuilder(PacketOpenGui_Guild.class, nextID())
			.encoder(PacketOpenGui_Guild::toBytes)
			.decoder(PacketOpenGui_Guild::new)
			.consumer(PacketOpenGui_Guild::handle)
			.add();
		INSTANCE.messageBuilder(PacketGuildDataToServer.class, nextID())
			.encoder(PacketGuildDataToServer::toBytes)
			.decoder(PacketGuildDataToServer::new)
			.consumer(PacketGuildDataToServer::handle)
			.add();
		INSTANCE.messageBuilder(PacketUpdateGuiGuild.class, nextID())
			.encoder(PacketUpdateGuiGuild::toBytes)
			.decoder(PacketUpdateGuiGuild::new)
			.consumer(PacketUpdateGuiGuild::handle)
			.add();
	}
	
	public static void sendToClient(Object packet, ServerPlayerEntity player) {
		INSTANCE.sendTo(packet, player.connection.connection, NetworkDirection.PLAY_TO_CLIENT);
	}
	public static void sendToServer(Object packet) {
		INSTANCE.sendToServer(packet);
	}
}
