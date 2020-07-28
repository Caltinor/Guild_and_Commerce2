package dicemc.gnc;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

import java.util.UUID;

import dicemc.gnc.datastorage.database.DatabaseManager;
import dicemc.gnc.datastorage.wsd.MarketWSD;
import dicemc.gnc.datastorage.wsd.WorldWSD;
import dicemc.gnc.land.ChunkManager;
import dicemc.gnc.setup.ClientSetup;
import dicemc.gnc.setup.CommonSetup;
import dicemc.gnc.setup.Config;

@Mod(GnC.MOD_ID)
public class GnC {
	public static final String MOD_ID = "gnc";
	public static DatabaseManager DBM_MAIN, DBM_ALT;
	public static MarketWSD marketSaver;
	public static WorldWSD worldSaver;
	public static ChunkManager ckMgr;
	public static UUID NIL = UUID.fromString("00000000-0000-0000-0000-000000000000");
	
	public GnC() {
		ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, Config.CLIENT_CONFIG);
		ModLoadingContext.get().registerConfig(ModConfig.Type.SERVER, Config.SERVER_CONFIG);
		
		FMLJavaModLoadingContext.get().getModEventBus().addListener(CommonSetup::init);
		FMLJavaModLoadingContext.get().getModEventBus().addListener(ClientSetup::init);
		
		MinecraftForge.EVENT_BUS.register(this);
	}	
 }
