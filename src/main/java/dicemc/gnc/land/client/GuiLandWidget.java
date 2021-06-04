package dicemc.gnc.land.client;

import java.util.ArrayList;
import java.util.List;

import com.mojang.blaze3d.matrix.MatrixStack;
import dicemc.gnc.GnC;
import dicemc.gnc.common.client.DropDownBox;
import dicemc.gnclib.guilds.entries.Guild;
import dicemc.gnclib.realestate.ChunkData;
import dicemc.gnclib.util.ComVars;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.AbstractGui;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.Widget;
import net.minecraft.client.gui.widget.button.ImageButton;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;

public class GuiLandWidget extends Widget{
	private enum MenuType {INFO, ACCESS, RENT}
	public static final ResourceLocation MAP_BORDER = new ResourceLocation(GnC.MOD_ID+":guis/chunkgui.png");
	public static final ResourceLocation INVENTORY_ADDITIONS = new ResourceLocation(GnC.MOD_ID+":guis/inventoryadditions.png");
	private FontRenderer font;
	private boolean isExpanded = false;
	private boolean showREopenButton;
	//Global Objects
	private DropDownBox menuToggle;
	private ImageButton openREButton;
	private String header = "";	
	//Given Variables
	public Screen parent;
	public double balP, balG;
	public ChunkData ckData;
	public Guild myGuild = Guild.getDefault();
	public int respX, respY;
	
	@SuppressWarnings("resource")
	public static void sync(double balG, double balP, Guild guild, ChunkData ckData) {
		Screen screen = Minecraft.getInstance().screen;
		for (int i = 0; i < screen.children().size(); i++) {
			if (screen.children().get(i) instanceof GuiLandWidget) {
				GuiLandWidget widget = (GuiLandWidget) screen.children().get(i);
				widget.updateGui(balG, balP, guild, ckData);
				break;
			}
		}
	}
	
	private void updateGui(double balG, double balP, Guild guild, ChunkData ckData) {
		this.balG = balG;
		this.balP = balP;
		this.myGuild = guild;
		this.ckData = ckData;
		updateVisibility();
	}
	
	public GuiLandWidget(FontRenderer font, int x, int y, int width, int height, ITextComponent title, double balG, double balP, Guild guild, ChunkData summary, boolean showREopenButton, int respX, int respY) {
		super(x, y, width, height, title);
		this.showREopenButton = showREopenButton;
		this.font = font;
		this.balP = balP;
		this.balG = balG;
		this.myGuild = guild != null ? guild : myGuild;
		this.ckData = summary;
		this.respX = respX;
		this.respY = respY;
		init();
	}
	
	protected void init() {
		openREButton = new ImageButton(this.x + this.width - 32, this.y, 32, 32, 157, 0, 32, INVENTORY_ADDITIONS, button -> actionOpenREMenu());
		header = ownerName(ckData);
		List<String> options = new ArrayList<>();
		options.add(new TranslationTextComponent("gnc.gui.land.widget.dropdown.info").getContents());
		options.add(new TranslationTextComponent("gnc.gui.land.widget.dropdown.access").getContents());
		options.add(new TranslationTextComponent("gnc.gui.land.widget.dropdown.rent").getContents());
		menuToggle = new DropDownBox(Minecraft.getInstance(), 0, 0, 0, 0, options) ;
		updateVisibility();
	}
	
	private void updateVisibility() {
		header = isExpanded ? new TranslationTextComponent("gnc.gui.land.widget.close").getContents() : ownerName(ckData);
	}
	
	@Override
	public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
		super.keyPressed(keyCode, scanCode, modifiers);
		return true;
	}
	
	public boolean charTyped(char ch, int a) {
		super.charTyped(ch, a);
		return true;
	}
	
	public boolean mouseScrolled(double mouseX, double mouseY, double amountScrolled) {
		super.mouseScrolled(mouseX, mouseY, amountScrolled);
		return true;
	}
	
	
	public boolean mouseClicked(double mouseX, double mouseY, int mouseButton) {		
		if (mouseX>this.x && mouseX < this.x+this.width-32 && mouseY > this.y && mouseY < this.y+this.height) {
			Minecraft.getInstance().setScreen(new GuiLandDetail(this));
			return true;
		}
		return super.mouseClicked(mouseX, mouseY, mouseButton);
	}
	
	public boolean isMouseOver(double mouseX, double mouseY) {
		if (mouseX>this.x && mouseX < this.x+this.width && mouseY > this.y && mouseY < this.y+this.height) {
			return true;
		}
	    return super.isMouseOver(mouseX, mouseY);
	}
	
	@Override
	public void render(MatrixStack mStack, int mouseX, int mouseY, float partialTicks) {
		mStack.translate(0, 0, 1000);
		Minecraft.getInstance().getTextureManager().bind(MAP_BORDER);
		blit(mStack, this.x, this.y, this.width, this.height, 0, 0, 256, 256, 256, 256);
		drawScaledString(mStack, 0.75f, this.font, isExpanded ? "" : ownerPretext(ckData), this.x+3, this.y+18, 16777215);
		drawScaledString(mStack, 0.75f, this.font, header, this.x+3, this.y+25, 16777215);
		if (showREopenButton) openREButton.renderButton(mStack, mouseX, mouseY, partialTicks);
		if (!isExpanded) {
			
		}
		if (isExpanded) {
			menuToggle.render(mStack, mouseX, mouseY, partialTicks);
		}	
		mStack.translate(0,  0, -1000);
	}
	
	private static void drawScaledString(MatrixStack mStack, float scale, FontRenderer font, String text, int x, int y, int color) {
		mStack.pushPose();
		mStack.scale(scale, scale, 1f);
		AbstractGui.drawString(mStack, font, text, (int)((float)x/scale), (int)((float)y/scale), color);
		mStack.popPose();
	}
	
	public MenuType getMenuSelection() {
		if (menuToggle.selection.equalsIgnoreCase(new TranslationTextComponent("gnc.gui.land.widget.dropdown.info").getContents()))
			return MenuType.INFO;
		if (menuToggle.selection.equalsIgnoreCase(new TranslationTextComponent("gnc.gui.land.widget.dropdown.access").getContents()))
			return MenuType.ACCESS;
		if (menuToggle.selection.equalsIgnoreCase(new TranslationTextComponent("gnc.gui.land.widget.dropdown.rent").getContents()))
			return MenuType.RENT;
		return null;
	}
	
	private void actionOpenREMenu() {Minecraft.getInstance().setScreen(new GuiRealEstateManager());}	
	
	private String ownerName(ChunkData data) {
		if (!data.owner.refID.equals(ComVars.NIL)) return data.owner.name;
		if (!data.renter.refID.equals(ComVars.NIL)) return data.renter.name;
		return new TranslationTextComponent("gnc.gui.land.ownername.unowned").getString();
	}
	
	private String ownerPretext(ChunkData data) {
		if (data.owner.refID.equals(ComVars.NIL) && !data.renter.refID.equals(ComVars.NIL)) return new TranslationTextComponent("gnc.gui.land.ownername.renter").getString();
		return new TranslationTextComponent("gnc.gui.land.ownername.owner").getString();
	}
}
