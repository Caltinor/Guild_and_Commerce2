package dicemc.gnc.testmaterial;

import com.mojang.blaze3d.matrix.MatrixStack;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.util.text.StringTextComponent;

public class TestGui extends Screen{
	//private Screen parentScreen;

	public static void open() {
		//Screen parent = Minecraft.getInstance().currentScreen;
		//Minecraft.getInstance().displayGuiScreen(new TestGui(parent));
	}
	
	protected TestGui(Screen parentScreen) { 
		super(new StringTextComponent("Test")); 
		//this.parentScreen = parentScreen;
	}
	
	@Override
	protected void init() {		
		addButton(new Button(5, 5, 75, 20, new StringTextComponent("Exit"), button -> action()));
	}
	
	@Override
	public boolean isPauseScreen() {return false;}
	
	@Override
	public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
		return super.keyPressed(keyCode, scanCode, modifiers);
	}
	
	private void action() {minecraft.setScreen(null);}

	@Override
	public void render(MatrixStack mStack, int mouseX, int mouseY, float partialTicks) {
		this.renderBackground(mStack);
		super.render(mStack, mouseX, mouseY, partialTicks);
	}
}
