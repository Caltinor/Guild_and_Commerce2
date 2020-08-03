package dicemc.gnc;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.inventory.InventoryScreen;
import net.minecraft.client.gui.widget.button.ImageButton;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.client.event.InputEvent.KeyInputEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(Dist.CLIENT)
public class GuiEventHandler {
	static ImageButton guildButton, chunkButton, marketsButton;
	public static final ResourceLocation INVENTORY_ADDITIONS = new ResourceLocation(GnC.MOD_ID+":guis/inventoryadditions.png");

	@SubscribeEvent
	public static void onInventoryLoad(GuiScreenEvent.InitGuiEvent event) {
		if (event.getGui() instanceof InventoryScreen) {
			int guiX = (event.getGui().width - 176)/2;
			int xOffset = 80;
			int guiY = (event.getGui().height - 166)/2;
			chunkButton = 	new ImageButton(guiX +xOffset + 3, guiY - 17, 19, 17, 0 , 0, 19, INVENTORY_ADDITIONS, button -> chunkGuiLoad());
			marketsButton = new ImageButton(guiX +xOffset +21, guiY - 17, 19, 17, 63, 0, 19, INVENTORY_ADDITIONS, button -> marketGuiLoad());
			guildButton = 	new ImageButton(guiX +xOffset +39, guiY - 17, 19, 17, 21, 0, 19, INVENTORY_ADDITIONS, button -> guildGuiLoad());
			event.addWidget(chunkButton);
			event.addWidget(marketsButton);
			event.addWidget(guildButton);
		}
	}
	
	@SubscribeEvent
	public static void onKeyPress(KeyInputEvent event) {
		if (Minecraft.getInstance().currentScreen instanceof InventoryScreen) {
			if (event.getKey() == 49) {System.out.println("Pressed 1");/*replace with packet to open chunk gui*/}
			if (event.getKey() == 50) {System.out.println("Pressed 2");/*replace with packet to open market gui*/}
			if (event.getKey() == 51) {System.out.println("Pressed 3");/*replace with packet to open guild gui*/}
		}
	}
	
	private static void chunkGuiLoad() {System.out.println("Clicked Chunk Button");}
	private static void marketGuiLoad() {System.out.println("Clicked Market Button");}
	private static void guildGuiLoad() {System.out.println("Clicked Guild Button");}
}
