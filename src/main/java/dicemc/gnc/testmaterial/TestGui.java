package dicemc.gnc.testmaterial;

import com.mojang.blaze3d.matrix.MatrixStack;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.util.text.StringTextComponent;

public class TestGui extends Screen{
	private Screen parentScreen;

	public static void open() {
		Screen parent = Minecraft.getInstance().currentScreen;
		Minecraft.getInstance().displayGuiScreen(new TestGui(parent));
	}
	
	protected TestGui(Screen parentScreen) { 
		super(new StringTextComponent("Test")); 
		this.parentScreen = parentScreen;
	}
	
	@Override
	protected void init() {		
		addButton(new Button(5, 5, 75, 20, new StringTextComponent("Exit"), button -> action()));
	}
	
	@Override
	public boolean isPauseScreen() {return false;}
	
	@Override
	public boolean keyPressed(int p_231046_1_, int p_231046_2_, int p_231046_3_) {
		return super.keyPressed(p_231046_1_, p_231046_2_, p_231046_3_);
	}
	
	private void action() {minecraft.displayGuiScreen(null);}

	@Override
	public void render(MatrixStack mStack, int mouseX, int mouseY, float partialTicks) {
		this.renderBackground(mStack);
		super.render(mStack, mouseX, mouseY, partialTicks);
	}
}
