package dicemc.gnc.setup;

import dicemc.gnc.GnC;
import dicemc.gnc.land.items.WhitelistStick;
import dicemc.gnc.land.items.Whitelister;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class Registration {
	public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, GnC.MOD_ID);
	
	public static void init() {
		ITEMS.register(FMLJavaModLoadingContext.get().getModEventBus());
	}
	
	public static final RegistryObject<Item> WHITELISTER = ITEMS.register("whitelister", () -> new Whitelister(new Item.Properties().tab(ItemGroup.TAB_MISC)));
	public static final RegistryObject<Item> WHITELIST_STICK_GREEN = ITEMS.register("whitelist_stick_green", () -> new WhitelistStick(new Item.Properties().tab(ItemGroup.TAB_MISC)));
	public static final RegistryObject<Item> WHITELIST_STICK_RED = ITEMS.register("whitelist_stick_red", () -> new WhitelistStick(new Item.Properties().tab(ItemGroup.TAB_MISC)));

}
