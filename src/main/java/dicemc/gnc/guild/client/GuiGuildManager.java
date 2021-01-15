package dicemc.gnc.guild.client;

import java.awt.Color;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;

import dicemc.gnc.GnC;
import dicemc.gnc.common.PacketGuiRequest;
import dicemc.gnc.guild.Guild;
import dicemc.gnc.guild.Guild.permKey;
import dicemc.gnc.guild.GuildManager.guildUpdates;
import dicemc.gnc.guild.network.PacketGuildDataToServer;
import dicemc.gnc.setup.Config;
import dicemc.gnc.setup.Networking;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.client.gui.widget.button.ImageButton;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.ITextProperties;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.Style;
import net.minecraftforge.client.gui.ScrollPanel;
import net.minecraftforge.common.ForgeHooks;

public class GuiGuildManager extends Screen{
	private Screen parentScreen;
	public static final ResourceLocation INVENTORY_ADDITIONS = new ResourceLocation(GnC.MOD_ID+":guis/inventoryadditions.png");
	private DecimalFormat df = new DecimalFormat("###,###,###,##0.00");
	private DecimalFormat pf = new DecimalFormat("#0.00");
	private int detailX, detailY;
	private String line1, line2, line3, line4, line5, line6, line7;
	private String line1a, line2a, line3a, line4a, line5a, line6a, line7a;
	private List<guildUpdates> changes = new ArrayList<guildUpdates>();
	//objects
	private Button nameChange, setTax, openToggle, rankBuy, rankRename;
	private ImageButton openMembers, openPerms, openREmgr, openAccount;
	private TextFieldWidget nameField, taxField, rankNameField;
	private RankListPanel rankList;
	//Given Variables
	private int coreCount, outpostCount;
	private double worth, taxableWorth, taxCharge, balG;
	private Guild guild;

	public static void open(Guild guild, double worth, double taxableWorth, double balG, int coreCount, int outpostCount) {
		Screen parent = Minecraft.getInstance().currentScreen;
		Minecraft.getInstance().displayGuiScreen(new GuiGuildManager(parent, guild, worth, taxableWorth, balG, coreCount, outpostCount));
	}
	
	public static void sync(Guild guild, double worth, double taxableWorth, double balG, int coreCount, int outpostCount) {
		GuiGuildManager screen = (GuiGuildManager) Minecraft.getInstance().currentScreen;
		screen.updateGui(guild, worth, taxableWorth, balG, coreCount, outpostCount);
	}
	
	protected void updateGui(Guild guild, double worth, double taxableWorth, double balG, int coreCount, int outpostCount) {
		this.guild = guild;
		this.worth = worth;
		this.taxableWorth = taxableWorth;
		this.taxCharge = taxableWorth * Config.GLOBAL_TAX_RATE.get();
		this.balG = balG;
		this.coreCount = coreCount;
		this.outpostCount = outpostCount;
		updateVisibility();
	}
	
	protected GuiGuildManager(Screen parentScreen, Guild guild, double worth, double taxableWorth, double balG, int coreCount, int outpostCount) { 
		super(new StringTextComponent("Test")); 
		this.parentScreen = parentScreen;
		this.guild = guild;
		this.worth = worth;
		this.taxableWorth = taxableWorth;
		this.taxCharge = taxableWorth * Config.GLOBAL_TAX_RATE.get();
		this.balG = balG;
		this.coreCount = coreCount;
		this.outpostCount = outpostCount;
	}
	
	@Override
	protected void init() {
		line1 = "Guild Balance:";
		line2 = "Chunk Count:";
		line3 = "       Core:";
		line4 = "    Outpost:";
		line5 = "Guild Worth:";
		line6 = "Taxable Worth:";
		line7 = "    Taxes Due:";
		detailX = this.width/2;
		detailY = 36;
		addButton(new Button(this.width/2-38, this.height-30, 75, 20, new StringTextComponent("Back"), button -> actionBack()));
		nameField = new TextFieldWidget(font, 5, 16, 75, 20, new StringTextComponent(""));
		taxField = new TextFieldWidget(font, nameField.x, nameField.y+nameField.getHeightRealms()+14, nameField.getWidth(), 20, new StringTextComponent(""));
		nameChange = new Button(nameField.x+ nameField.getWidth()+1, nameField.y, 75, 20, new StringTextComponent("Change Name"), button -> actionNameChange());
		setTax = new Button(taxField.x+taxField.getWidth()+1, taxField.y, 75, 20, new StringTextComponent("Set Tax"), button -> actionSetTax());
		openToggle = new Button(nameField.x, taxField.y+taxField.getHeightRealms()+12, 75, 20, new StringTextComponent(""), button -> actionOpenToggle());
		rankList = new RankListPanel(minecraft, this.width/4, this.height-(openToggle.y+openToggle.getHeightRealms()+25), openToggle.y+openToggle.getHeightRealms()+12, nameField.x);
		rankBuy = new Button(rankList.x+rankList.width+3, rankList.y, 75, 20, new StringTextComponent("Buy New Rank"), button -> actionRankBuy());
		rankNameField = new TextFieldWidget(font, rankBuy.x, rankBuy.y+rankBuy.getHeightRealms()+5, 75, 20, new StringTextComponent(""));
		rankRename = new Button(rankBuy.x, rankNameField.y+rankNameField.getHeightRealms()+1, 75, 20, new StringTextComponent("Rename Rank"), button -> actionRankRename());
		openREmgr = new ImageButton(this.width-25, 5, 19, 19, 0, 0, 19, INVENTORY_ADDITIONS, button -> actionOpenREmgr());
		openMembers = new ImageButton(openREmgr.x-23, 5, 19, 19, 42, 0, 19, INVENTORY_ADDITIONS, button -> actionOpenMembers());
		openPerms = new ImageButton(openMembers.x-23, 5, 19, 19, 84, 0, 19, INVENTORY_ADDITIONS, button -> actionOpenPerms());		
		openAccount = new ImageButton(detailX-20, detailY, 19, 19, 63, 0, 19, INVENTORY_ADDITIONS, button -> actionOpenAccount());
		addButton(nameChange);
		addButton(setTax);
		addButton(openToggle);
		addButton(rankBuy);
		addButton(rankRename);
		addButton(openMembers);
		addButton(openPerms);
		addButton(openREmgr);
		addButton(openAccount);
		updateVisibility();
	}
	
	private void updateVisibility() {
		List<String> ranks = new ArrayList<String>();
		for (int i = 0; i < guild.permLevels.size(); i++) ranks.add("["+String.valueOf(i)+"]"+guild.permLevels.getOrDefault(i, "Error"));
		rankList.setInfo(ranks);
		line1a = "$"+df.format(balG);
		line2a = "";
		line3a = String.valueOf(coreCount) +" (# Over Limit: "+ String.valueOf(coreCount - (guild.members.size()*Config.CHUNKS_PER_MEMBER.get()))+")";
		line4a = String.valueOf(outpostCount);
		line5a = "$"+df.format(worth);
		line6a = "$"+df.format(taxableWorth);
		line7a = "$"+df.format(taxCharge);
		nameField.active = isPermitted(permKey.CHANGE_NAME);
		nameField.setText(guild.name);
		nameChange.active = isPermitted(permKey.CHANGE_NAME);
		taxField.setText(pf.format(guild.tax));
		taxField.active = isPermitted(permKey.SET_TAX);
		setTax.active = isPermitted(permKey.SET_TAX);
		openToggle.setMessage(new StringTextComponent("Public :"+ (guild.open ? "Yes" : "No")));
		openToggle.active = isPermitted(permKey.SET_OPEN_TO_JOIN);
		rankBuy.active = isPermitted(permKey.BUY_NEW_RANK);
		rankNameField.active = isPermitted(permKey.RANK_TITLE_CHANGE);
		rankRename.active = isPermitted(permKey.RANK_TITLE_CHANGE);
	}
	
	private boolean isPermitted(permKey key) {
		int rank = guild.members.getOrDefault(minecraft.player.getUniqueID(), -3);
		if (rank < 0) return false;
		if (rank <= guild.permissions.get(key)) return true;
		return false;
	}
	
	@Override
	public boolean isPauseScreen() {return false;}
	
	public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
		super.keyPressed(keyCode, scanCode, modifiers);
		nameField.keyPressed(keyCode, scanCode, modifiers);
		taxField.keyPressed(keyCode, scanCode, modifiers);
		rankNameField.keyPressed(keyCode, scanCode, modifiers);
		return true;
	}
	
	public boolean charTyped(char ch, int a) {
		super.charTyped(ch, a);
		if (nameField.isFocused()) return nameField.charTyped(ch, a);
		if (taxField.isFocused()) return taxField.charTyped(ch, a);
		if (rankNameField.isFocused()) return rankNameField.charTyped(ch, a);
		return true;
	}
	
	public boolean mouseScrolled(double mouseX, double mouseY, double amountScrolled) {
		super.mouseScrolled(mouseX, mouseY, amountScrolled);
		if (mouseX > rankList.x && mouseX < rankList.x+rankList.width && mouseY > rankList.y && mouseY < rankList.y+rankList.height)
			return rankList.mouseScrolled(mouseX, mouseY, amountScrolled);
		return true;
	}
	
	public boolean mouseClicked(double mouseX, double mouseY, int mouseButton) {
		super.mouseClicked(mouseX, mouseY, mouseButton);
		nameField.mouseClicked(mouseX, mouseY, mouseButton);
		taxField.mouseClicked(mouseX, mouseY, mouseButton);
		rankNameField.mouseClicked(mouseX, mouseY, mouseButton);
		if (mouseX > rankList.x && mouseX < rankList.x+rankList.width && mouseY > rankList.y && mouseY < rankList.y+rankList.height)
			rankList.mouseClicked(mouseX, mouseY, mouseButton);
		return true;
	}

	@Override
	public void render(MatrixStack mStack, int mouseX, int mouseY, float partialTicks) {
		this.renderBackground(mStack);
		this.drawString(mStack, font, "Guild Name (Change Cost = $"+df.format(Config.GUILD_NAME_CHANGE_COST.get())+")", 5, 5, 16777215);
		this.drawString(mStack, font, "Member Tax Rate:", taxField.x, taxField.y-11, 16777215);
		this.drawString(mStack, font, "Guild Ranks (Cost to Add = $"+df.format(Config.GUILD_RANK_ADD_COST.get())+")", rankList.x, rankList.y-11, 16777215);
		this.drawString(mStack, font, line1, detailX, detailY    , 16777215);
		this.drawString(mStack, font, line2, detailX, detailY+ 11, 16777215);
		this.drawString(mStack, font, line3, detailX, detailY+ 23, 16777215);
		this.drawString(mStack, font, line4, detailX, detailY+ 35, 16777215);
		this.drawString(mStack, font, line5, detailX, detailY+ 47, 16777215);
		this.drawString(mStack, font, line6, detailX, detailY+ 59, 16777215);
		this.drawString(mStack, font, line7, detailX, detailY+ 71, 16777215);
		this.drawString(mStack, font, line1a, detailX+ 100, detailY    , 16777215);
		this.drawString(mStack, font, line2a, detailX+ 100, detailY+ 11, 16777215);
		this.drawString(mStack, font, line3a, detailX+ 100, detailY+ 23, 16777215);
		this.drawString(mStack, font, line4a, detailX+ 100, detailY+ 35, 16777215);
		this.drawString(mStack, font, line5a, detailX+ 100, detailY+ 47, 16777215);
		this.drawString(mStack, font, line6a, detailX+ 100, detailY+ 59, 16777215);
		this.drawString(mStack, font, line7a, detailX+ 100, detailY+ 71, 16777215);
		nameField.render(mStack, mouseX, mouseY, partialTicks);
		taxField.render(mStack, mouseX, mouseY, partialTicks);
		rankNameField.render(mStack, mouseX, mouseY, partialTicks);
		rankList.render(mStack, mouseX, mouseY, partialTicks);		
		super.render(mStack, mouseX, mouseY, partialTicks);
	}
	
	private void actionBack() {minecraft.displayGuiScreen(parentScreen);}
	private void actionNameChange() {
		if (nameField.getText().length() > 0) guild.name = nameField.getText();
		else return;
		changes.add(guildUpdates.NAME);
		Networking.sendToServer(new PacketGuildDataToServer(guild, changes));
	}
	private void actionSetTax() {
		if (taxField.getText().length() > 0) guild.tax = Double.valueOf(taxField.getText());
		else return;
		changes.add(guildUpdates.TAX);
		Networking.sendToServer(new PacketGuildDataToServer(guild, changes));
	}
	private void actionOpenToggle() {
		guild.open = !guild.open;
		changes.add(guildUpdates.OPEN);
		Networking.sendToServer(new PacketGuildDataToServer(guild, changes));
	}
	private void actionRankBuy() {
		changes.add(guildUpdates.PERMLVL_ADD);
		Networking.sendToServer(new PacketGuildDataToServer(guild, changes));
	}
	private void actionRankRename() {
		if (rankNameField.getText().length() > 0 && rankList.selectedItem >= 0) guild.permLevels.put(rankList.selectedItem, rankNameField.getText());
		else return;
		changes.add(guildUpdates.PERMLVLS);
		Networking.sendToServer(new PacketGuildDataToServer(guild, changes));
	}
	private void actionOpenMembers() {Networking.sendToServer(new PacketGuiRequest(PacketGuiRequest.gui.MEMBERS));}
	private void actionOpenPerms() {Networking.sendToServer(new PacketGuiRequest(PacketGuiRequest.gui.PERMS));}
	private void actionOpenREmgr() {Networking.sendToServer(new PacketGuiRequest(PacketGuiRequest.gui.REAL_ESTATE));}
	private void actionOpenAccount() {Networking.sendToServer(new PacketGuiRequest(PacketGuiRequest.gui.ACCOUNT));}
	
	class RankListPanel extends ScrollPanel {
		private List<ITextProperties> lines = Collections.emptyList();
        public int x, y, width, height, selectedItem;

        public RankListPanel(Minecraft mcIn, int width, int height, int y, int x)
        {
            super(mcIn, width, height, y, x);
            this.x = x;
            this.y = y;
            this.width = width;
            this.height = height;
            selectedItem = -1;
        }

        public String getSelected() {return lines.get(selectedItem).getString();}
        
        void setInfo(List<String> lines) { this.lines = resizeContent(lines); }

        void clearInfo() { this.lines = Collections.emptyList(); }

        private List<ITextProperties> resizeContent(List<String> lines) {
            List<ITextProperties> ret = new ArrayList<>();
            if (lines.size() == 0) {ret.add(new StringTextComponent("")); return ret;}
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
                    //GuiGuildManager.this.font.func_238407_a_(mStack, lines.get(i), left+1, relativeY, 0xFFFFFF);
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
                GuiGuildManager.this.handleComponentClicked(component);
                return true;
            }
            return super.mouseClicked(mouseX, mouseY, button);
        }

        @Override
        protected void drawBackground() {}
	}
}
