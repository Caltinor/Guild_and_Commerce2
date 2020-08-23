package dicemc.gnc;

import net.minecraft.util.RegistryKey;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import dicemc.gnc.account.AccountManager;
import dicemc.gnc.datastorage.database.DatabaseManager;
import dicemc.gnc.guild.GuildManager;
import dicemc.gnc.land.ChunkManager;
import dicemc.gnc.setup.ClientSetup;
import dicemc.gnc.setup.CommonSetup;
import dicemc.gnc.setup.Config;

@Mod(GnC.MOD_ID)
public class GnC {
	public static final String MOD_ID = "gnc";
	public static final UUID NIL = UUID.fromString("00000000-0000-0000-0000-000000000000");
	public static Map<RegistryKey<World>, ChunkManager> wldMgr = new HashMap<RegistryKey<World>, ChunkManager>();
	public static DatabaseManager DBMgr;
	public static GuildManager gMgr;
	public static AccountManager aMgr;
	
	public GnC() {
		ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, Config.CLIENT_CONFIG);
		ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, Config.COMMON_CONFIG);
		ModLoadingContext.get().registerConfig(ModConfig.Type.SERVER, Config.SERVER_CONFIG);
		
		FMLJavaModLoadingContext.get().getModEventBus().addListener(CommonSetup::init);
		FMLJavaModLoadingContext.get().getModEventBus().addListener(ClientSetup::init);
		
		MinecraftForge.EVENT_BUS.register(this);
	}	
 }
