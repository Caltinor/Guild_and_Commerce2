package dicemc.gnc;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import dicemc.gnc.common.Networking;
import dicemc.gnc.util.Reference;

@Mod(Reference.MOD_ID)
public class GnC {
	private static final Logger LOGGER = LogManager.getLogger();
	
	public GnC() {
		ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, Config.CLIENT_CONFIG);
		ModLoadingContext.get().registerConfig(ModConfig.Type.SERVER, Config.SERVER_CONFIG);
		
		FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
		FMLJavaModLoadingContext.get().getModEventBus().addListener(this::doClientStuff);
		//TODO:  setup custome classes for these methods and instead call
		//FMLJavaModLoadingContext.get().getModEventBus().addListener(ModSetup::init);
		//FMLJavaModLoadingContext.get().getModEventBus().addListener(ClientSetup::init);
		
		MinecraftForge.EVENT_BUS.register(this);
	}
	
	private void setup(final FMLCommonSetupEvent event) {
		LOGGER.info("Setup");
		Networking.registerMessages();
	}
	
	private void doClientStuff(final FMLClientSetupEvent event) {}
	
 }
