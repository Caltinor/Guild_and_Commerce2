package dicemc.gnc.land.client;

import java.awt.Color;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import com.mojang.blaze3d.matrix.MatrixStack;

import dicemc.gnc.GnC;
import dicemc.gnc.guild.Guild;
import dicemc.gnc.guild.Guild.permKey;
import dicemc.gnc.land.ChunkData;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.client.gui.ScrollPanel;

public class GuiLandManager extends Screen{
	public static final ResourceLocation MAP_BORDER = new ResourceLocation(GnC.MOD_ID+":guis/chunkgui.png");
	private Screen parentScreen;
	private DecimalFormat df = new DecimalFormat("###,###,###,##0.00");
	private boolean chunkView = true;
	private boolean overlayView = false;
	private ChunkPos selectedCK;
	private Map<ChunkPos, Color> overlayColors;
	private int d, mapX, mapY;
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
	private Guild myGuild = new Guild("N/A", GnC.NIL);
	private ChunkPos center;
	private int[][] mapColors = new int[176][176];
	
	public static void open(double balG, double balP, Guild guild, int[][] mapColors, ChunkPos center, List<ChunkSummary> summary) {
		Screen parent = Minecraft.getInstance().currentScreen;
		Minecraft.getInstance().displayGuiScreen(new GuiLandManager(parent, balG, balP, guild, mapColors, center, summary));
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
		System.out.println(ckData.toString());
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
		guildClaimButton = new Button(xq2+3, tempClaimButton.y + tempClaimButton.getHeight()+ 10, xq1-6, 20, new StringTextComponent("Guild Claim"), button -> actionGuildClaim());
		abandonButton = new Button(xq2+3, guildClaimButton.y + guildClaimButton.getHeight() + 3, xq1-6, 20, new StringTextComponent("Abandon Claim"), button -> actionAbandon());
		extendButton = new Button(xq3, tempClaimButton.y, xq1-6, 20, new StringTextComponent("Extend Time"), button -> actionExtend());
		sellButton = new Button(xq3, guildClaimButton.y, xq1-6, 20, new StringTextComponent("Sell Claim"), button -> actionSell());
		sellField = new TextFieldWidget(font, xq3, abandonButton.y, xq1-6, 20, new StringTextComponent("")); //TODO replace text with land current value
		//Sublet Toggle Objects
		subletCostField = new TextFieldWidget(font, xq2+3, 41, ((xq2-6)/3), 20, new StringTextComponent(""));
		subletDurationField = new TextFieldWidget(font, subletCostField.x + subletCostField.getWidth(), subletCostField.y, subletCostField.getWidth(), 20, new StringTextComponent(""));
		updateSubletButton = new Button(subletDurationField.x + subletDurationField.getWidth()+ 3, subletCostField.y, subletCostField.getWidth()-5, 20, new StringTextComponent("Update Rent"), button -> actionUpdateSublet());
		publicToggleButton = new Button(xq2+3, subletCostField.y + subletCostField.getHeight() + 3, 60, 20, new StringTextComponent("Public: No"), button -> actionPublicToggle());
		whiteList = new WhiteListPanel(minecraft, updateSubletButton.x-(xq2+3), (playerList.y-13)-(publicToggleButton.y+publicToggleButton.getHeight()+13), (publicToggleButton.y+publicToggleButton.getHeight()+13), playerList.x);
		disableSubletButton = new Button(updateSubletButton.x, publicToggleButton.y, updateSubletButton.getWidth(), 20, new StringTextComponent("Disable Rent"), button -> actionDisableSublet());
		minRankIncreaseButton = new Button(disableSubletButton.x - 18, disableSubletButton.y, 15, 20, new StringTextComponent("+"), button -> actionMinRankIncrease());
		minRankDecreaseButton = new Button(minRankIncreaseButton.x-15, minRankIncreaseButton.y, 15, 20, new StringTextComponent("-"), button -> actionMinRankDecrease());
		breakToggleButton = new Button(disableSubletButton.x, whiteList.y, updateSubletButton.getWidth(), 20, new StringTextComponent("Break"), button -> actionBreakToggle());
		interactToggleButton = new Button(breakToggleButton.x, breakToggleButton.y + breakToggleButton.getHeight() + 3, updateSubletButton.getWidth(), 20, new StringTextComponent("Interact"), button -> actionInteractToggle());
		clearWhitelistButton = new Button(interactToggleButton.x, whiteList.bottom-20, updateSubletButton.getWidth(), 20, new StringTextComponent("Clear List"), button -> actionClearWhitelist());
		playerAddField = new TextFieldWidget(font, playerList.right+3, playerList.y, this.width-playerList.right-12, 20, new StringTextComponent(""));
		memberRemoveButton = new Button(playerAddField.x, playerAddField.y+playerAddField.getHeight()+3, playerAddField.getWidth()/2, 20, new StringTextComponent("Remove"), button -> actionMemberRemove());
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
		chunkButton.active = !chunkView;
		subletButton.active = chunkView;
		overlayButton.setMessage(overlayView ? new StringTextComponent("Overlay: On") : new StringTextComponent("Overlay: Off"));
		
		//chunk toggle items
		tempClaimButton.visible = chunkView;
		tempClaimButton.active = ckData.get(selectedCK).data.owner.equals(GnC.NIL);
		
		boolean gon = guildOwnedNeighbor();
		guildClaimButton.visible = chunkView;
		guildClaimButton.active = 	(isPermitted(permKey.CORE_CLAIM) && gon)|| 
									(isPermitted(permKey.OUTPOST_CLAIM) && gon && guildOwnedNeighborIsOutpost())||
									(isPermitted(permKey.OUTPOST_CREATE) && !gon);
		guildClaimButton.setMessage(new StringTextComponent(gon ? "Guild Claim" : "New Outpost"));
		
		abandonButton.visible = chunkView;
		abandonButton.active = (!ckData.get(selectedCK).data.isForSale && isPermitted(permKey.CLAIM_ABANDON)
				&& ckData.get(selectedCK).data.owner.equals(myGuild.guildID));
		
		extendButton.visible = chunkView;
		extendButton.active = ckData.get(selectedCK).data.owner.equals(myGuild.guildID) 
				|| !ckData.get(selectedCK).data.permittedPlayers.getOrDefault(minecraft.player.getUniqueID(), "N/A").equalsIgnoreCase("N/A");
		
		sellButton.visible = chunkView;
		sellButton.active = ckData.get(selectedCK).data.owner.equals(myGuild.guildID) && isPermitted(permKey.CLAIM_SELL);
		
		sellField.visible = chunkView;
		sellField.active = sellButton.active;
		//sublet toggle items
		updateSubletButton.visible = !chunkView;
		updateSubletButton.active = ckData.get(selectedCK).data.owner.equals(myGuild.guildID) && isPermitted(permKey.SUBLET_MANAGE);		
		publicToggleButton.visible = !chunkView;
		publicToggleButton.active = ckData.get(selectedCK).data.owner.equals(myGuild.guildID) && isPermitted(permKey.SUBLET_MANAGE);
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
		memberRemoveButton.active = ckData.get(selectedCK).data.owner.equals(myGuild.guildID) && isPermitted(permKey.SUBLET_MANAGE);
		memberAddButton.visible = !chunkView;
		memberAddButton.active = ckData.get(selectedCK).data.owner.equals(myGuild.guildID) && isPermitted(permKey.SUBLET_MANAGE);
		subletCostField.visible = !chunkView;
		subletCostField.active = updateSubletButton.active;
		subletDurationField.visible = !chunkView;
		subletDurationField.active = updateSubletButton.active;
		playerAddField.visible = !chunkView;
		playerAddField.active = memberAddButton.active;
	}
	
	@Override
	public boolean isPauseScreen() {return false;}
	
	@Override
	public boolean keyPressed(int p_231046_1_, int p_231046_2_, int p_231046_3_) {
		//input if statement for the keycode being used.  guess the mappings
		return super.keyPressed(p_231046_1_, p_231046_2_, p_231046_3_);
	}
	
	public boolean mouseClicked(double mouseX, double mouseY, int mouseButton) {
		super.mouseClicked(mouseX, mouseY, mouseButton);
		if (mouseX > mapX+4 && mouseX <= mapX+4+d && mouseY > mapY+4 && mouseY < mapY+4+d) {
			int ckX = (center.x-5)+(((int)mouseX - mapX-4)/16);
			int ckZ = (center.z-5)+(((int)mouseY - mapY-4)/16);
			selectedCK = new ChunkPos(ckX, ckZ);
			updateVisibility();
			return true;
		}
		return false;
	}
	
	@Override
	public void render(MatrixStack mStack, int mouseX, int mouseY, float partialTicks) {
		this.renderBackground(mStack);
		minecraft.getTextureManager().bindTexture(MAP_BORDER);
		blit(mStack, mapX, mapY, d, d, 0, 0, 256, 256, 256, 256);
		blit(mStack, this.width/2, 25, (this.width/2)-3, this.height-28, 0, 0, 256, 256, 256, 256);
		renderMap(mStack, mapX+4, mapY+4, d-6);
		renderOverlay(mStack, mapX+4, mapY+4, d-6);
		this.drawString(mStack, this.font, response, 5, 5, 16777215);	
		this.font.func_238422_b_(mStack, new StringTextComponent(TextFormatting.BLUE+"Permitted Players:"), playerList.x, playerList.y-10, 1677215);
		sellField.render(mStack, mouseX, mouseY, partialTicks);
		playerList.render(mStack, mouseX, mouseY, partialTicks);
		subletCostField.render(mStack, mouseX, mouseY, partialTicks);
		subletDurationField.render(mStack, mouseX, mouseY, partialTicks);
		playerAddField.render(mStack, mouseX, mouseY, partialTicks);		
		if (chunkView) {
			this.font.func_238422_b_(mStack, new StringTextComponent(TextFormatting.DARK_RED+"Outpost = No"), abandonButton.x, abandonButton.y + abandonButton.getHeight() + 10, 16777215);
			this.font.func_238422_b_(mStack, new StringTextComponent(TextFormatting.DARK_PURPLE+"Whitelist Type: Default"), abandonButton.x, abandonButton.y + abandonButton.getHeight() + 25, 16777215);
		}
		if (!chunkView) {
			this.font.func_238422_b_(mStack, new StringTextComponent("Min Rank:"), publicToggleButton.x + publicToggleButton.getWidth() + 3, publicToggleButton.y, 16777215);
			this.font.func_238422_b_(mStack, new StringTextComponent("Leader"), publicToggleButton.x + publicToggleButton.getWidth() + 3, publicToggleButton.y+11, 16777215); //TODO replace text with rank variable	
			this.font.func_238422_b_(mStack, new StringTextComponent(TextFormatting.BLACK+"Whitelist: "+TextFormatting.RED+"Break"+TextFormatting.BLUE+" Interact"), whiteList.x, whiteList.y-10, 16777215);
			this.font.func_238422_b_(mStack, new StringTextComponent(TextFormatting.BLACK+"Sublet Rate:"), subletCostField.x, subletCostField.y-10, 16777215);
			this.font.func_238422_b_(mStack, new StringTextComponent(TextFormatting.BLACK+"Rent Duration (in Hours)"), subletDurationField.x, subletDurationField.y-10, 16777215);
			whiteList.render(mStack, mouseX, mouseY, partialTicks);
		}
		super.render(mStack, mouseX, mouseY, partialTicks);
	}
	
	private void renderMap(MatrixStack mStack, int left, int top, int d) {
		int ivl = (d)/176;
		for (int x = 0; x < 176; x++) {
			for (int z = 0; z < 176; z++) {
				Color color = new Color(mapColors[x][z]);
				int l = left+(x*ivl);
				int t = top+(z*ivl);
				fill(mStack, l+ivl, t, l, t+ivl, color.getRGB());
			}
		}
		for (int v = 1; v < 11; v++) {
			fill(mStack, left+(ivl*v*16), top, left+(ivl*v*16)+1, top+d-3, Color.BLACK.getRGB());
        }
        for (int h = 1; h < 11; h++) {
        	fill(mStack, left, top+(ivl*h*16), left+d-3, top+(ivl*h*16)+1, Color.BLACK.getRGB());
        }
	}
	
	private void renderOverlay(MatrixStack mStack, int left, int top, int d) {
		int ivl = (d)/11;
		int x = left + (ivl*(5+(selectedCK.x-center.x)));
		int y = top + (ivl*(5+(selectedCK.z-center.z)));
		//draw selection box
		fill(mStack, x, y, x+ivl, y+ivl, 0x800000FF);
		//draw overlay if toggled on
		if (overlayView) {
			for (int x1 = 0; x1 < 11; x1++) {
				for (int z1 = 0; z1 < 11; z1++) {
					ChunkPos pos = new ChunkPos(center.x+(-5+x1), center.z+(-5+z1));
					int x2 = left+(ivl*x1*16);
					int y2 = top+ (ivl*z1*16);
					if (!ckData.get(pos).data.owner.equals(GnC.NIL)) fill(mStack, x2, y2, x2+ivl, y2+ivl, overlayColors.getOrDefault(pos, new Color(0x00000000)).getRGB());
				}
			}
		}

	}
	
	private void actionBack() {minecraft.displayGuiScreen(parentScreen);}
	private void actionChunkToggle() {chunkView = true; updateVisibility();}
	private void actionSubletToggle() {chunkView = false; updateVisibility();}
	private void actionOverlayToggle() {overlayView = overlayView ? false : true; updateVisibility();}
	private void actionTempClaim() {}
	private void actionGuildClaim() {}
	private void actionAbandon() {}
	private void actionExtend() {}
	private void actionSell() {}
	private void actionUpdateSublet() {}
	private void actionPublicToggle() {} 
	private void actionMinRankIncrease() {}
	private void actionMinRankDecrease() {}
	private void actionDisableSublet() {} 
	private void actionBreakToggle() {}
	private void actionInteractToggle() {}
	private void actionClearWhitelist() {}
	private void actionMemberAdd() {}
	private void actionMemberRemove() {} 
	
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
	
	private Map<ChunkPos, Color> generateMapColors() {
		Map<ChunkPos, Color> map = new HashMap<ChunkPos, Color>();
		for (Map.Entry<ChunkPos, ChunkSummary> entry : ckData.entrySet()) {
			if (map.get(entry.getKey()) == null) {
				Random rnd = new Random();
				Color color = new Color(rnd.nextInt(255), rnd.nextInt(255), rnd.nextInt(255), 180);
				map.put(entry.getKey(), color);
			}
		}
		return map;
	}
	
	class PlayerListPanel extends ScrollPanel {
		public int x, y, right, bottom;

		public PlayerListPanel(Minecraft client, int width, int height, int top, int left) {
			super(client, width, height, top, left);
			x = left;
			y = top;
			right = x + width;
			bottom = y + height;
			// TODO Add to params populating data
		}
		
		public void refresh() {
			//TODO add parameter to update new data
		}

		@Override
		protected int getContentHeight() {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		protected void drawPanel(MatrixStack mStack, int entryRight, int relativeY, Tessellator tess, int mouseX, int mouseY) {
			// TODO Auto-generated method stub			
		}
		
	}
	
	class WhiteListPanel extends ScrollPanel {
		public int x, y, right, bottom;

		public WhiteListPanel(Minecraft client, int width, int height, int top, int left) {
			super(client, width, height, top, left);
			x = left;
			y = top;
			right = x + width;
			bottom = y + height;
			// TODO Add to params populating data
		}
		
		public void refresh() {
			//TODO add parameter to update new data
		}

		@Override
		protected int getContentHeight() {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		protected void drawPanel(MatrixStack mStack, int entryRight, int relativeY, Tessellator tess, int mouseX, int mouseY) {
			// TODO Auto-generated method stub			
		}
		
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
