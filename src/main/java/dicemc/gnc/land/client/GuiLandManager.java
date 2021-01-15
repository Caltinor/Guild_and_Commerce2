package dicemc.gnc.land.client;

import java.awt.Color;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;

import dicemc.gnc.GnC;
import dicemc.gnc.guild.Guild;
import dicemc.gnc.guild.Guild.permKey;
import dicemc.gnc.land.ChunkData;
import dicemc.gnc.land.WhitelistItem;
import dicemc.gnc.land.network.PacketChunkDataToServer;
import dicemc.gnc.setup.Config;
import dicemc.gnc.setup.Networking;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldVertexBufferUploader;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.ITextProperties;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.client.gui.ScrollPanel;
import net.minecraftforge.common.ForgeHooks;

public class GuiLandManager extends Screen{
	public static final ResourceLocation MAP_BORDER = new ResourceLocation(GnC.MOD_ID+":guis/chunkgui.png");
	private Screen parentScreen;
	private DecimalFormat df = new DecimalFormat("###,###,###,##0.00");
	private boolean chunkView = true;
	private boolean overlayView = false;
	private ChunkPos selectedCK;
	private Map<ChunkPos, Integer> overlayColors;
	private double d, mapX, mapY;
	private String ownerText;
	//Objects
	private Button backButton, chunkButton, subletButton, overlayButton;
	private Button tempClaimButton, guildClaimButton, abandonButton, extendButton, sellButton;
	private Button updateSubletButton, publicToggleButton, minRankIncreaseButton, minRankDecreaseButton, disableSubletButton, 
		breakToggleButton, interactToggleButton, clearWhitelistButton, memberAddButton, memberRemoveButton; 
	private TextFieldWidget sellField, subletCostField, subletDurationField, playerAddField;
	private PlayerListPanel playerList;
	private WhiteListPanel whiteList;
	//Given Variables
	private String response;
	private double balP, balG;
	private Map<ChunkPos, ChunkSummary> ckData = new HashMap<ChunkPos, ChunkSummary>();
	private Guild myGuild = new Guild("N/A", GnC.NIL, false);
	private ChunkPos center;
	private int[][] mapColors = new int[176][176];
	
	public static void open(double balG, double balP, Guild guild, int[][] mapColors, ChunkPos center, List<ChunkSummary> summary) {
		Screen parent = Minecraft.getInstance().currentScreen;
		Minecraft.getInstance().displayGuiScreen(new GuiLandManager(parent, balG, balP, guild, mapColors, center, summary));
	}
	
	public static void sync(double balG, double balP, Guild guild, List<ChunkSummary> summary, String resp) {
		GuiLandManager screen = (GuiLandManager) Minecraft.getInstance().currentScreen;
		screen.updateGui(balG, balP, guild, summary, resp);
	}
	
	protected void updateGui(double balG, double balP, Guild guild, List<ChunkSummary> summary, String resp) {
		this.balP = balP;
		this.balG = balG;
		this.myGuild = guild != null ? guild : myGuild;
		for (int i = 0; i < summary.size(); i++) {this.ckData.put(summary.get(i).data.pos, summary.get(i));}
		updateVisibility();
		this.response = resp;
	}
	
	protected GuiLandManager(Screen parentScreen, double balG, double balP, Guild guild, int[][] mapColors, ChunkPos center, List<ChunkSummary> summary) {
		super(new StringTextComponent("Land Manager"));
		this.parentScreen = parentScreen;
		this.balP = balP;
		this.balG = balG;
		this.myGuild = guild != null ? guild : myGuild;
		for (int i = 0; i < summary.size(); i++) {this.ckData.put(summary.get(i).data.pos, summary.get(i));}
		this.center = center;
		this.selectedCK = center;
		this.mapColors = mapColors;
	}
	
	@Override
	protected void init() {
		d = (this.width/2) > (this.height-55) ? this.height-55 : this.width/2;
		mapX = ((this.width/2)-d)/2;
		mapY = 25;
		overlayColors = generateMapColors();
		response = "Account: $"+df.format(balP) +" [Guild $"+df.format(balG)+"]";
		selectedCK = center;
		int xq1 = this.width/4;
		int xq2 = this.width/2;
		int xq3 = (this.width/4)*3;
		int yq1 = this.height/4;
		//int yq2 = this.height/2;
		int yq3 = (this.height/4)*3;
		backButton = new Button(xq2-75, this.height-30, 75, 20, new StringTextComponent("Back"), button -> actionBack());
		chunkButton = new Button(xq2, 3, xq1, 20, new StringTextComponent("Chunk View"), button -> actionChunkToggle());
		subletButton = new Button(xq3, 3, xq1, 20, new StringTextComponent("Sublet View"), button -> actionSubletToggle());
		overlayButton = new Button(5, this.height-30, xq1, 20, new StringTextComponent("Overlay: Off"), button -> actionOverlayToggle());
		playerList = new PlayerListPanel(minecraft, xq1, yq1-9, yq3, xq2+3);
		//Claim Toggle Objects 
		tempClaimButton = new Button(xq2+3, 28, xq1-6, 20, new StringTextComponent("Temp Claim"), button -> actionTempClaim());
		guildClaimButton = new Button(xq2+3, tempClaimButton.y + tempClaimButton.getHeightRealms()+ 10, xq1-6, 20, new StringTextComponent("Guild Claim"), button -> actionGuildClaim());
		abandonButton = new Button(xq2+3, guildClaimButton.y + guildClaimButton.getHeightRealms() + 3, xq1-6, 20, new StringTextComponent("Abandon Claim"), button -> actionAbandon());
		extendButton = new Button(xq3, tempClaimButton.y, xq1-6, 20, new StringTextComponent("Extend Time"), button -> actionExtend());
		sellButton = new Button(xq3, guildClaimButton.y, xq1-6, 20, new StringTextComponent("Sell Claim"), button -> actionSell());
		sellField = new TextFieldWidget(font, xq3, abandonButton.y, xq1-6, 20, new StringTextComponent(""));
		//Sublet Toggle Objects
		subletCostField = new TextFieldWidget(font, xq2+3, 41, ((xq2-6)/3), 20, new StringTextComponent(""));
		subletDurationField = new TextFieldWidget(font, subletCostField.x + subletCostField.getWidth(), subletCostField.y, subletCostField.getWidth(), 20, new StringTextComponent(""));
		updateSubletButton = new Button(subletDurationField.x + subletDurationField.getWidth()+ 3, subletCostField.y, subletCostField.getWidth()-5, 20, new StringTextComponent("Update Rent"), button -> actionUpdateSublet());
		publicToggleButton = new Button(xq2+3, subletCostField.y + subletCostField.getHeightRealms() + 3, 60, 20, new StringTextComponent("Public: No"), button -> actionPublicToggle());
		whiteList = new WhiteListPanel(minecraft, updateSubletButton.x-(xq2+3), (playerList.y-13)-(publicToggleButton.y+publicToggleButton.getHeightRealms()+13), (publicToggleButton.y+publicToggleButton.getHeightRealms()+13), playerList.x);
		disableSubletButton = new Button(updateSubletButton.x, publicToggleButton.y, updateSubletButton.getWidth(), 20, new StringTextComponent("Disable Rent"), button -> actionDisableSublet());
		minRankIncreaseButton = new Button(disableSubletButton.x - 18, disableSubletButton.y, 15, 20, new StringTextComponent("+"), button -> actionMinRankIncrease());
		minRankDecreaseButton = new Button(minRankIncreaseButton.x-15, minRankIncreaseButton.y, 15, 20, new StringTextComponent("-"), button -> actionMinRankDecrease());
		breakToggleButton = new Button(disableSubletButton.x, whiteList.y, updateSubletButton.getWidth(), 20, new StringTextComponent("Break"), button -> actionBreakToggle());
		interactToggleButton = new Button(breakToggleButton.x, breakToggleButton.y + breakToggleButton.getHeightRealms() + 3, updateSubletButton.getWidth(), 20, new StringTextComponent("Interact"), button -> actionInteractToggle());
		clearWhitelistButton = new Button(interactToggleButton.x, whiteList.y+whiteList.height-20, updateSubletButton.getWidth(), 20, new StringTextComponent("Clear List"), button -> actionClearWhitelist());
		playerAddField = new TextFieldWidget(font, playerList.x+playerList.width+3, playerList.y, this.width-playerList.x-playerList.width-12, 20, new StringTextComponent(""));
		memberRemoveButton = new Button(playerAddField.x, playerAddField.y+playerAddField.getHeightRealms()+3, playerAddField.getWidth()/2, 20, new StringTextComponent("Remove"), button -> actionMemberRemove());
		memberAddButton = new Button(memberRemoveButton.x+memberRemoveButton.getWidth(), memberRemoveButton.y, memberRemoveButton.getWidth(), 20, new StringTextComponent("Add"), button -> actionMemberAdd());
		//final touches
		addButton(backButton);
		addButton(chunkButton);
		addButton(subletButton);
		addButton(overlayButton);
		addButton(tempClaimButton);
		addButton(guildClaimButton);
		addButton(abandonButton);
		addButton(extendButton);
		addButton(sellButton);
		addButton(updateSubletButton);
		addButton(publicToggleButton);
		addButton(disableSubletButton);
		addButton(minRankIncreaseButton);
		addButton(minRankDecreaseButton);
		addButton(breakToggleButton);
		addButton(interactToggleButton);
		addButton(clearWhitelistButton);
		addButton(memberRemoveButton);
		addButton(memberAddButton);
		updateVisibility();
	}
	
	private void updateVisibility() {
		overlayColors = generateMapColors();
		ownerText = ckData.get(selectedCK).data.owner.equals(GnC.NIL) ? (ckData.get(selectedCK).data.renter.equals(GnC.NIL) ? "Unclaimed Land" : "Temporarily claimed by: "+ckData.get(selectedCK).guildName) : "Owned by: "+ckData.get(selectedCK).guildName;
		response = "Account: $"+df.format(balP) +" [Guild $"+df.format(balG)+"]";
		chunkButton.active = !chunkView;
		subletButton.active = chunkView;
		overlayButton.setMessage(overlayView ? new StringTextComponent("Overlay: On") : new StringTextComponent("Overlay: Off"));
		
		//chunk toggle items
		tempClaimButton.visible = chunkView;
		tempClaimButton.active = ckData.get(selectedCK).data.owner.equals(GnC.NIL) && ckData.get(selectedCK).data.renter.equals(GnC.NIL) && balP >= (ckData.get(selectedCK).data.price * Config.TEMPCLAIM_RATE.get());
		
		boolean gon = guildOwnedNeighbor();
		guildClaimButton.visible = chunkView;
		guildClaimButton.active = 	(isPermitted(permKey.CORE_CLAIM) && gon && !guildOwnedNeighborIsOutpost() && balG >= ckData.get(selectedCK).data.price)|| 
									(isPermitted(permKey.OUTPOST_CLAIM) && gon && guildOwnedNeighborIsOutpost() && balG >= ckData.get(selectedCK).data.price)||
									(isPermitted(permKey.OUTPOST_CREATE) && !gon && (balG >= (ckData.get(selectedCK).data.price + Config.OUTPOST_CREATE_COST.get())));
		guildClaimButton.setMessage(new StringTextComponent(gon ? "Guild Claim" : "New Outpost"));
		
		abandonButton.visible = chunkView;
		abandonButton.active = (!ckData.get(selectedCK).data.isForSale && isPermitted(permKey.CLAIM_ABANDON)
				&& ckData.get(selectedCK).data.owner.equals(myGuild.guildID));
		
		extendButton.visible = chunkView;
		extendButton.active = (ckData.get(selectedCK).data.owner.equals(myGuild.guildID) 
				|| !ckData.get(selectedCK).data.permittedPlayers.getOrDefault(minecraft.player.getUniqueID(), "N/A").equalsIgnoreCase("N/A")) &&
				balP >= (ckData.get(selectedCK).data.price * Config.TEMPCLAIM_RATE.get() * ckData.get(selectedCK).data.permittedPlayers.size());
		
		sellButton.visible = chunkView;
		sellButton.active = ckData.get(selectedCK).data.owner.equals(myGuild.guildID) && isPermitted(permKey.CLAIM_SELL);
		
		sellField.visible = chunkView;
		sellField.setEnabled(sellButton.active);
		sellField.setText(String.valueOf(ckData.get(selectedCK).data.price));
		//sublet toggle items
		updateSubletButton.visible = !chunkView;
		updateSubletButton.active = ckData.get(selectedCK).data.owner.equals(myGuild.guildID) && isPermitted(permKey.SUBLET_MANAGE);		
		publicToggleButton.visible = !chunkView;
		publicToggleButton.active = (ckData.get(selectedCK).data.owner.equals(myGuild.guildID) && isPermitted(permKey.SUBLET_MANAGE)  ||
										ckData.get(selectedCK).data.renter.equals(minecraft.getInstance().player.getUniqueID()));
		publicToggleButton.setMessage(new StringTextComponent(ckData.get(selectedCK).data.isPublic ? "Public: Yes" : "Public: No"));
		disableSubletButton.visible = !chunkView;
		disableSubletButton.active = ckData.get(selectedCK).data.owner.equals(myGuild.guildID) && isPermitted(permKey.SUBLET_MANAGE);
		minRankIncreaseButton.visible = !chunkView;
		minRankIncreaseButton.active = ckData.get(selectedCK).data.owner.equals(myGuild.guildID) && isPermitted(permKey.SUBLET_MANAGE);
		minRankDecreaseButton.visible = !chunkView;
		minRankDecreaseButton.active = ckData.get(selectedCK).data.owner.equals(myGuild.guildID) && isPermitted(permKey.SUBLET_MANAGE);
		breakToggleButton.visible = !chunkView;
		breakToggleButton.active = ckData.get(selectedCK).data.owner.equals(myGuild.guildID) && isPermitted(permKey.SUBLET_MANAGE);
		interactToggleButton.visible = !chunkView;
		interactToggleButton.active = ckData.get(selectedCK).data.owner.equals(myGuild.guildID) && isPermitted(permKey.SUBLET_MANAGE);
		clearWhitelistButton.visible = !chunkView;
		clearWhitelistButton.active = ckData.get(selectedCK).data.owner.equals(myGuild.guildID) && isPermitted(permKey.SUBLET_MANAGE);
		memberRemoveButton.visible = !chunkView;
		memberRemoveButton.active = (ckData.get(selectedCK).data.owner.equals(myGuild.guildID) && isPermitted(permKey.SUBLET_MANAGE) ||
									ckData.get(selectedCK).data.renter.equals(minecraft.getInstance().player.getUniqueID()));
		memberAddButton.visible = !chunkView;
		memberAddButton.active = (ckData.get(selectedCK).data.owner.equals(myGuild.guildID) && isPermitted(permKey.SUBLET_MANAGE) ||
									ckData.get(selectedCK).data.renter.equals(minecraft.getInstance().player.getUniqueID()));
		subletCostField.visible = !chunkView;
		subletCostField.setEnabled(updateSubletButton.active);
		subletCostField.setText(String.valueOf(ckData.get(selectedCK).data.leasePrice));
		subletDurationField.visible = !chunkView;
		subletDurationField.setEnabled(updateSubletButton.active);
		subletDurationField.setText(String.valueOf(ckData.get(selectedCK).data.leaseDuration));
		playerAddField.visible = !chunkView;
		playerAddField.setEnabled(memberAddButton.active);
		whiteList.clearInfo();
		whiteList.setInfo(ckData.get(selectedCK).data.whitelist);
		playerList.clearInfo();
		playerList.setInfo(ckData.get(selectedCK).data.permittedPlayers, ckData.get(selectedCK).data.renter);
	}
	
	@Override
	public boolean isPauseScreen() {return false;}
	
	@Override
	public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
		super.keyPressed(keyCode, scanCode, modifiers);
		sellField.keyPressed(keyCode, scanCode, modifiers);
		subletCostField.keyPressed(keyCode, scanCode, modifiers);
		subletDurationField.keyPressed(keyCode, scanCode, modifiers);
		playerAddField.keyPressed(keyCode, scanCode, modifiers);
		return true;
	}
	
	public boolean charTyped(char ch, int a) {
		super.charTyped(ch, a);
		if (sellField.isFocused()) sellField.charTyped(ch, a);
		if (subletCostField.isFocused()) subletCostField.charTyped(ch, a);
		if (subletDurationField.isFocused()) subletDurationField.charTyped(ch, a);
		if (playerAddField.isFocused()) playerAddField.charTyped(ch, a);
		return true;
	}
	
	public boolean mouseScrolled(double mouseX, double mouseY, double amountScrolled) {
		super.mouseScrolled(mouseX, mouseY, amountScrolled);
		if (mouseX > playerList.x && mouseX < playerList.x+playerList.width && mouseY > playerList.y && mouseY < playerList.y+playerList.height)
			playerList.mouseScrolled(mouseX, mouseY, amountScrolled);
		if (mouseX > whiteList.x && mouseX < whiteList.x+whiteList.width && mouseY > whiteList.y && mouseY < whiteList.y+whiteList.height)
			whiteList.mouseScrolled(mouseX, mouseY, amountScrolled);
		return true;
	}
	
	
	public boolean mouseClicked(double mouseX, double mouseY, int mouseButton) {
		super.mouseClicked(mouseX, mouseY, mouseButton);
		if (mouseX > (double)mapX+4d && mouseX <= (double)mapX+(double)d-4d && mouseY > (double)mapY+4d && mouseY < (double)mapY+(double)d-4d) {
			int ckX = (center.x-5)+(int)Math.floor(((mouseX - (double)mapX-4d)/((d-8)/11)));
			int ckZ = (center.z-5)+(int)Math.floor(((mouseY - (double)mapY-4d)/((d-8)/11)));
			selectedCK = new ChunkPos(ckX, ckZ);
			updateVisibility();
			return true;
		}
		sellField.mouseClicked(mouseX, mouseY, mouseButton);
		subletCostField.mouseClicked(mouseX, mouseY, mouseButton);
		subletDurationField.mouseClicked(mouseX, mouseY, mouseButton);
		playerAddField.mouseClicked(mouseX, mouseY, mouseButton);
		playerList.mouseClicked(mouseX, mouseY, mouseButton);
		whiteList.mouseClicked(mouseX, mouseY, mouseButton);
		return false;
	}
	
	@Override
	public void render(MatrixStack mStack, int mouseX, int mouseY, float partialTicks) {
		this.renderBackground(mStack);
		minecraft.getTextureManager().bindTexture(MAP_BORDER);
		blit(mStack, (int)mapX, (int)mapY, (int)d, (int)d, 0, 0, 256, 256, 256, 256);
		blit(mStack, this.width/2, 25, (this.width/2)-3, this.height-28, 0, 0, 256, 256, 256, 256);
		renderMap(mapX+4, mapY+4, d-8);
		renderOverlay(mapX+4, mapY+4, d-8);
		this.drawString(mStack, this.font, response, 5, 5, 16777215);	
		this.font.func_243246_a(mStack, new StringTextComponent(TextFormatting.BLACK+"Permitted Players:"), playerList.x, playerList.y-10, 1677215);
		sellField.render(mStack, mouseX, mouseY, partialTicks);
		playerList.render(mStack, mouseX, mouseY, partialTicks);
		subletCostField.render(mStack, mouseX, mouseY, partialTicks);
		subletDurationField.render(mStack, mouseX, mouseY, partialTicks);
		playerAddField.render(mStack, mouseX, mouseY, partialTicks);		
		if (chunkView) {
			this.font.func_243246_a(mStack, new StringTextComponent(TextFormatting.BLACK+ownerText), abandonButton.x, abandonButton.y + abandonButton.getHeightRealms() + 10, 16777215);
			this.font.func_243246_a(mStack, new StringTextComponent(TextFormatting.BLACK+"Outpost: "+(ckData.get(selectedCK).data.isOutpost ? "Yes" : "No")), abandonButton.x, abandonButton.y + abandonButton.getHeightRealms() + 25, 16777215);
			this.font.func_243246_a(mStack, new StringTextComponent(TextFormatting.BLACK+"Whitelist Type: "+ whitelistType()), abandonButton.x, abandonButton.y + abandonButton.getHeightRealms() + 40, 16777215);
			if (!ckData.get(selectedCK).data.renter.equals(GnC.NIL))
				this.font.func_243246_a(mStack, new StringTextComponent(TextFormatting.BLUE+"Rented Until ["+String.valueOf(new Timestamp(ckData.get(selectedCK).data.rentEnd)+"]")), abandonButton.x, abandonButton.y + abandonButton.getHeightRealms() + 55, 16777215);
		}
		if (!chunkView) {
			this.font.func_243246_a(mStack, new StringTextComponent(TextFormatting.BLACK+"Min Rank:"), publicToggleButton.x + publicToggleButton.getWidth() + 3, publicToggleButton.y, 16777215);
			this.font.func_243246_a(mStack, new StringTextComponent(TextFormatting.BLACK+String.valueOf(ckData.get(selectedCK).data.permMin)), publicToggleButton.x + publicToggleButton.getWidth() + 3, publicToggleButton.y+11, 16777215);	
			this.font.func_243246_a(mStack, new StringTextComponent(TextFormatting.BLACK+"Whitelist: "+TextFormatting.RED+"Break"+TextFormatting.BLUE+" Interact"), whiteList.x, whiteList.y-10, 16777215);
			this.font.func_243246_a(mStack, new StringTextComponent(TextFormatting.BLACK+"Sublet Rate:"), subletCostField.x, subletCostField.y-10, 16777215);
			this.font.func_243246_a(mStack, new StringTextComponent(TextFormatting.BLACK+"Rent Duration (in Hours)"), subletDurationField.x, subletDurationField.y-10, 16777215);
			whiteList.render(mStack, mouseX, mouseY, partialTicks);
		}
		super.render(mStack, mouseX, mouseY, partialTicks);
	}
	
	private void renderMap(double left, double top, double d) {
		double ivl = (double)d/176.0;
		for (int x = 0; x < 176; x++) {
			for (int z = 0; z < 176; z++) {
				Color color = new Color(mapColors[x][z]);
				double l = (double)left+((double)x*ivl);
				double t = (double)top+((double)z*ivl);
				rect(l, t, l+ivl, t+ivl, color.getRGB());
			}
		}
		for (int v = 1; v < 11; v++) {
			rect((double)left+(((double)d/11.0)*(double)v), (double)top, (double)left+(((double)d/11.0)*(double)v)+1, (double)top+(double)d, Color.BLACK.getRGB());
        }
        for (int h = 1; h < 11; h++) {
        	rect((double)left, (double)top+(((double)d/11.0)*(double)h), (double)left+(double)d, (double)top+(((double)d/11.0)*(double)h)+1, Color.BLACK.getRGB());
        }
	}
	
	private void renderOverlay(double left, double top, double d) {
		double ivl = (d)/11;
		double x = (double)left + (ivl*(double)(5+(selectedCK.x-center.x)));
		double y = (double)top + (ivl*(double)(5+(selectedCK.z-center.z)));
		//draw selection box
		rect(x+1, y+1, x+ivl, y+ivl, 0x800000FF);
		//draw overlay if toggled on
		if (overlayView) {
			for (int x1 = 0; x1 < 11; x1++) {
				for (int z1 = 0; z1 < 11; z1++) {
					ChunkPos pos = new ChunkPos(center.x+(-5+x1), center.z+(-5+z1));
					double x2 = (double)left+(ivl*(double)x1);
					double y2 = (double)top+ (ivl*(double)z1);
					rect(x2+1, y2+1, x2+ivl, y2+ivl, overlayColors.getOrDefault(pos, 0xFF000000));
				}
			}
		}

	}
	
	private static void rect(double x1, double y1, double x2, double y2, int color) {
	      if (x1 < x2) { double i = x1; x1 = x2; x2 = i; }
	      if (y1 < y2) { double j = y1; y1 = y2; y2 = j; }

	      float f3 = (float)(color >> 24 & 255) / 255.0F;
	      float f = (float)(color >> 16 & 255) / 255.0F;
	      float f1 = (float)(color >> 8 & 255) / 255.0F;
	      float f2 = (float)(color & 255) / 255.0F;
	      BufferBuilder bufferbuilder = Tessellator.getInstance().getBuffer();
	      RenderSystem.enableBlend();
	      RenderSystem.disableTexture();
	      RenderSystem.defaultBlendFunc();
	      bufferbuilder.begin(7, DefaultVertexFormats.POSITION_COLOR);
	      bufferbuilder.pos(x1, y2, 0.0F).color(f, f1, f2, f3).endVertex();
	      bufferbuilder.pos(x2, y2, 0.0F).color(f, f1, f2, f3).endVertex();
	      bufferbuilder.pos(x2, y1, 0.0F).color(f, f1, f2, f3).endVertex();
	      bufferbuilder.pos(x1, y1, 0.0F).color(f, f1, f2, f3).endVertex();
	      bufferbuilder.finishDrawing();
	      WorldVertexBufferUploader.draw(bufferbuilder);
	      RenderSystem.enableTexture();
	      RenderSystem.disableBlend();
	   }
	
	private void actionBack() {minecraft.displayGuiScreen(parentScreen);}
	private void actionChunkToggle() {chunkView = true; updateVisibility();}
	private void actionSubletToggle() {chunkView = false; updateVisibility();}
	private void actionOverlayToggle() {overlayView = overlayView ? false : true; updateVisibility();}
	private void actionTempClaim() {Networking.sendToServer(new PacketChunkDataToServer(PacketChunkDataToServer.PkType.TEMPCLAIM, selectedCK, Minecraft.getInstance().player.getUniqueID()));}
	private void actionGuildClaim() {Networking.sendToServer(new PacketChunkDataToServer(PacketChunkDataToServer.PkType.GUILDCLAIM, selectedCK, Minecraft.getInstance().player.getUniqueID()));}
	private void actionAbandon() {}
	private void actionExtend() {Networking.sendToServer(new PacketChunkDataToServer(PacketChunkDataToServer.PkType.EXTEND, selectedCK, Minecraft.getInstance().player.getUniqueID()));}
	private void actionSell() {}
	private void actionUpdateSublet() {}
	private void actionPublicToggle() {Networking.sendToServer(new PacketChunkDataToServer(PacketChunkDataToServer.PkType.PUBLIC, selectedCK, !ckData.get(selectedCK).data.isPublic));} 
	private void actionMinRankIncrease() {}
	private void actionMinRankDecrease() {}
	private void actionDisableSublet() {} 
	private void actionBreakToggle() { // UNTESTED
		WhitelistItem wlItem = ckData.get(selectedCK).data.whitelist.get(whiteList.selectedItem);
		wlItem.setCanBreak(!wlItem.getCanBreak());
		Networking.sendToServer(new PacketChunkDataToServer(PacketChunkDataToServer.PkType.INTERACT, selectedCK, wlItem.toNBT().toString()));
	}
	private void actionInteractToggle() { // UNTESTED
		WhitelistItem wlItem = ckData.get(selectedCK).data.whitelist.get(whiteList.selectedItem);
		wlItem.setCanBreak(!wlItem.getCanBreak());
		Networking.sendToServer(new PacketChunkDataToServer(PacketChunkDataToServer.PkType.INTERACT, selectedCK, wlItem.toNBT().toString()));
	}
	private void actionClearWhitelist() {Networking.sendToServer(new PacketChunkDataToServer(PacketChunkDataToServer.PkType.CLEARWL, selectedCK));}
	private void actionMemberAdd() {
		Networking.sendToServer(new PacketChunkDataToServer(PacketChunkDataToServer.PkType.MEMBER, selectedCK, playerAddField.getText()));
		playerAddField.setText("");
	}
	private void actionMemberRemove() {
		Networking.sendToServer(new PacketChunkDataToServer(PacketChunkDataToServer.PkType.MEMBER, selectedCK, playerList.getSelected()));
		playerList.selectedItem = 0;
	} 
	
	private boolean guildOwnedNeighbor() {
		if (ckData.get(new ChunkPos(selectedCK.x-1, selectedCK.z)).data.owner.equals(myGuild.guildID)) return true;
		if (ckData.get(new ChunkPos(selectedCK.x+1, selectedCK.z)).data.owner.equals(myGuild.guildID)) return true;
		if (ckData.get(new ChunkPos(selectedCK.x, selectedCK.z-1)).data.owner.equals(myGuild.guildID)) return true;
		if (ckData.get(new ChunkPos(selectedCK.x, selectedCK.z+1)).data.owner.equals(myGuild.guildID)) return true;
		return false;
	}
	
	private boolean guildOwnedNeighborIsOutpost() {
		if (ckData.get(new ChunkPos(selectedCK.x-1, selectedCK.z)).data.isOutpost) return true;
		if (ckData.get(new ChunkPos(selectedCK.x+1, selectedCK.z)).data.isOutpost) return true;
		if (ckData.get(new ChunkPos(selectedCK.x, selectedCK.z-1)).data.isOutpost) return true;
		if (ckData.get(new ChunkPos(selectedCK.x, selectedCK.z+1)).data.isOutpost) return true;
		return false;
	}
	
	private boolean isPermitted(permKey key) {
		int rank = myGuild.members.getOrDefault(minecraft.player.getUniqueID(), -3);
		if (rank < 0) return false;
		if (rank <= myGuild.permissions.get(key)) return true;
		return false;
	}
	
	private String whitelistType() {
		if (ckData.get(selectedCK).data.isPublic) return TextFormatting.GREEN+"PUBLIC";
		if (ckData.get(selectedCK).data.whitelist.size() == 0) {
			return ckData.get(selectedCK).data.permittedPlayers.size() == 0 ? TextFormatting.BLACK+"MEMBERS ONLY" : TextFormatting.RED+"RENTED (U)";
		}
		else { return ckData.get(selectedCK).data.permittedPlayers.size() == 0 ? TextFormatting.YELLOW+"SPECIAL ACCESS" : TextFormatting.RED+"RENTED (R)";}
	}
	
	private Map<ChunkPos, Integer> generateMapColors() {
		Map<ChunkPos, Integer> map = new HashMap<ChunkPos, Integer>();
		for (Map.Entry<ChunkPos, ChunkSummary> entry : ckData.entrySet()) {
			int color = 0x00000000;
			if (entry.getValue().data.owner.equals(GnC.NIL) && entry.getValue().data.renter.equals(GnC.NIL)) { map.put(entry.getKey(), color); }
			else if (!entry.getValue().data.owner.equals(GnC.NIL) || !entry.getValue().data.renter.equals(GnC.NIL)) {
				boolean existing = false;
				for (Map.Entry<ChunkPos, Integer> ent : map.entrySet()) {
					if (ckData.get(ent.getKey()).data.owner.equals(entry.getValue().data.owner) && ckData.get(ent.getKey()).data.renter.equals(entry.getValue().data.renter)) {
						color = ent.getValue();
						map.put(entry.getKey(), color);
						existing = true;
						break;
					}
				}
				if (!existing) {
					Random rnd = new Random();
					color = 180 << 24 | rnd.nextInt(255) << 16 | rnd.nextInt(255) << 8 | rnd.nextInt(255);
					map.put(entry.getKey(), color);
				}
			}
		}
		return map;
	}
	
	class PlayerListPanel extends ScrollPanel {
        private List<ITextProperties> lines = Collections.emptyList();
        public int x, y, width, height, selectedItem;

        public PlayerListPanel(Minecraft mcIn, int width, int height, int y, int x)
        {
            super(mcIn, width, height, y, x);
            this.x = x;
            this.y = y;
            this.width = width;
            this.height = height;
            selectedItem = -1;
        }

        public String getSelected() {return lines.get(selectedItem).getString();}
        
        void setInfo(Map<UUID, String> lines, UUID renter) { 
        	List<String> list = new ArrayList<String>();
        	list.add(lines.getOrDefault(renter, "NO RENTER"));
        	for (Map.Entry<UUID, String> entries : lines.entrySet()) {
        		if (!entries.getKey().equals(renter)) list.add(entries.getValue());
        	}
        	this.lines = resizeContent(list); 
        }

        void clearInfo() { this.lines = Collections.emptyList(); }

        private List<ITextProperties> resizeContent(List<String> lines) {
            List<ITextProperties> ret = new ArrayList<>();
            for (String line : lines) {
                if (line == null) {
                    ret.add(null);
                    continue;
                }
                ITextComponent chat = ForgeHooks.newChatWithLinks(line, false);
                int maxTextLength = this.width - 12;
                if (maxTextLength >= 0) {
                    ret.addAll(font.getCharacterManager().func_238362_b_(chat, maxTextLength, Style.EMPTY));
                }
            }
            return ret;
        }

        @Override
        public int getContentHeight() {
            int height = 50;
            height += (lines.size() * font.FONT_HEIGHT);
            if (height < this.bottom - this.top - 8)
                height = this.bottom - this.top - 8;
            return height;
        }

        @Override
        protected int getScrollAmount() { return font.FONT_HEIGHT * 3; }

        @Override
        protected void drawPanel(MatrixStack mStack, int entryRight, int relativeY, Tessellator tess, int mouseX, int mouseY)
        {
            for (int i = 0; i < lines.size(); i++){
                if (lines.get(i) != null)
                {
                	if (i == selectedItem) {
                    	hLine(mStack, left, left+width, relativeY-1, Color.YELLOW.getRGB());
                    	hLine(mStack, left, left+width, relativeY-1+font.FONT_HEIGHT, Color.YELLOW.getRGB());
                    	vLine(mStack, left, relativeY-1, relativeY-1+font.FONT_HEIGHT, Color.YELLOW.getRGB());
                    	vLine(mStack, left+width-1, relativeY-1, relativeY-1+font.FONT_HEIGHT, Color.YELLOW.getRGB());
                    }
                    RenderSystem.enableBlend();
                    //GuiLandManager.this.font.func_243248_b(mStack, lines.get(i), left+1, relativeY, 0xFFFFFF);
                    RenderSystem.disableAlphaTest();
                    RenderSystem.disableBlend();
                }
                relativeY += font.FONT_HEIGHT;
            }
        }

        private Style findTextLine(final int mouseX, final int mouseY) {
            double offset = (mouseY - top) + border + scrollDistance + 1;

            int lineIdx = (int) (offset / font.FONT_HEIGHT);
            if (lineIdx > lines.size() || lineIdx < 1)
                return null;

            ITextProperties line = lines.get(lineIdx-1);
            selectedItem = lineIdx-1;
            if (line != null)
            {
                return font.getCharacterManager().func_238357_a_(line, mouseX);
            }
            return null;
        }

        @Override
        public boolean mouseClicked(final double mouseX, final double mouseY, final int button) {
            final Style component = findTextLine((int) mouseX, (int) mouseY);
            if (component != null) {
                GuiLandManager.this.handleComponentClicked(component);
                return true;
            }
            return super.mouseClicked(mouseX, mouseY, button);
        }

        @Override
        protected void drawBackground() {}
	}	

	class WhiteListPanel extends ScrollPanel {
		 private List<ITextProperties> lines = Collections.emptyList();
	        public int x, y, width, height, selectedItem;

	        public WhiteListPanel(Minecraft mcIn, int width, int height, int y, int x)
	        {
	            super(mcIn, width, height, y, x);
	            this.x = x;
	            this.y = y;
	            this.width = width;
	            this.height = height;
	            selectedItem = -1;
	        }
	        
	        void setInfo(List<WhitelistItem> wl) { 
	        	List<String> list = new ArrayList<String>();
	        	for (WhitelistItem entries : wl) {
	        		String line = entries.getBlock()+entries.getEntity();
	        		line += TextFormatting.RED+(entries.getCanBreak() ? " Yes " : " No ");
	        		line += TextFormatting.BLUE+(entries.getCanInteract() ? "Yes" : "No");
	        		list.add(line);
	        	}
	        	this.lines = resizeContent(list); 
	        }

	        void clearInfo() { this.lines = Collections.emptyList(); }

	        private List<ITextProperties> resizeContent(List<String> lines) {
	            List<ITextProperties> ret = new ArrayList<>();
	            for (String line : lines) {
	                if (line == null) {
	                    ret.add(null);
	                    continue;
	                }
	                ITextComponent chat = ForgeHooks.newChatWithLinks(line, false);
	                int maxTextLength = this.width - 12;
	                if (maxTextLength >= 0) {
	                    ret.addAll(font.getCharacterManager().func_238362_b_(chat, maxTextLength, Style.EMPTY));
	                }
	            }
	            return ret;
	        }

	        @Override
	        public int getContentHeight() {
	            int height = 50;
	            height += (lines.size() * font.FONT_HEIGHT);
	            if (height < this.bottom - this.top - 8)
	                height = this.bottom - this.top - 8;
	            return height;
	        }

	        @Override
	        protected int getScrollAmount() { return font.FONT_HEIGHT * 3; }

	        @Override
	        protected void drawPanel(MatrixStack mStack, int entryRight, int relativeY, Tessellator tess, int mouseX, int mouseY)
	        {
	            for (int i = 0; i < lines.size(); i++){
	                if (lines.get(i) != null)
	                {
	                	if (i == selectedItem) {
	                    	hLine(mStack, left, left+width, relativeY-1, Color.YELLOW.getRGB());
	                    	hLine(mStack, left, left+width, relativeY-1+font.FONT_HEIGHT, Color.YELLOW.getRGB());
	                    	vLine(mStack, left, relativeY-1, relativeY-1+font.FONT_HEIGHT, Color.YELLOW.getRGB());
	                    	vLine(mStack, left+width-1, relativeY-1, relativeY-1+font.FONT_HEIGHT, Color.YELLOW.getRGB());
	                    }
	                    RenderSystem.enableBlend();
	                    //GuiLandManager.this.font.func_238407_a_(mStack, lines.get(i), left+1, relativeY, 0xFFFFFF);
	                    RenderSystem.disableAlphaTest();
	                    RenderSystem.disableBlend();
	                }
	                relativeY += font.FONT_HEIGHT;
	            }
	        }

	        private Style findTextLine(final int mouseX, final int mouseY) {
	            double offset = (mouseY - top) + border + scrollDistance + 1;

	            int lineIdx = (int) (offset / font.FONT_HEIGHT);
	            if (lineIdx > lines.size() || lineIdx < 1)
	                return null;

	            ITextProperties line = lines.get(lineIdx-1);
	            selectedItem = lineIdx-1;
	            if (line != null)
	            {
	                return font.getCharacterManager().func_238357_a_(line, mouseX);
	            }
	            return null;
	        }

	        @Override
	        public boolean mouseClicked(final double mouseX, final double mouseY, final int button) {
	            final Style component = findTextLine((int) mouseX, (int) mouseY);
	            if (component != null) {
	                GuiLandManager.this.handleComponentClicked(component);
	                return true;
	            }
	            return super.mouseClicked(mouseX, mouseY, button);
	        }

	        @Override
	        protected void drawBackground() {}
	}

	public static class ChunkSummary {
		public ChunkData data;
		public String guildName;
		
		public ChunkSummary(ChunkData data, String guildName) {
			this.data = data; 
			this.guildName = guildName;
		}
		
		public ChunkSummary(CompoundNBT nbt) {
			data = new ChunkData(nbt.getCompound("data"));
			guildName = nbt.getString("name");
		}
		
		public CompoundNBT toNBT() {
			CompoundNBT nbt = new CompoundNBT();
			nbt.put("data", data.toNBT());
			nbt.putString("name", guildName);
			return nbt;
		}
	}
}
