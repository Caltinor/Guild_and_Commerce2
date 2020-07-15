package dicemc.gnc;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import dicemc.gnc.setup.ClientSetup;
import dicemc.gnc.setup.CommonSetup;
import dicemc.gnc.testmaterial.Config;

@Mod(GnC.MOD_ID)
public class GnC {
	public static final String MOD_ID = "gnc";
	private static final Logger LOGGER = LogManager.getLogger();
	
	public GnC() {
		ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, Config.CLIENT_CONFIG);
		ModLoadingContext.get().registerConfig(ModConfig.Type.SERVER, Config.SERVER_CONFIG);
		LOGGER.info("Config Registered");
		
		FMLJavaModLoadingContext.get().getModEventBus().addListener(CommonSetup::init);
		FMLJavaModLoadingContext.get().getModEventBus().addListener(ClientSetup::init);
		
		MinecraftForge.EVENT_BUS.register(this);
	}	
 }
