package dicemc.gnc.land.client;

import com.mojang.blaze3d.systems.RenderSystem;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldVertexBufferUploader;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.text.StringTextComponent;

public class GuiRealEstateManager extends Screen{

	protected GuiRealEstateManager() {
		super(new StringTextComponent("REMgr"));
		// TODO Auto-generated constructor stub
	}
	
	private static void rect(double x1, double y1, double x2, double y2, int color) {
		if (x1 < x2) { double i = x1; x1 = x2; x2 = i; }
		if (y1 < y2) { double j = y1; y1 = y2; y2 = j; }
		
		float f3 = (float)(color >> 24 & 255) / 255.0F;
		float f = (float)(color >> 16 & 255) / 255.0F;
		float f1 = (float)(color >> 8 & 255) / 255.0F;
		float f2 = (float)(color & 255) / 255.0F;
		BufferBuilder bufferbuilder = Tessellator.getInstance().getBuilder();
		RenderSystem.enableBlend();
		RenderSystem.disableTexture();
		RenderSystem.defaultBlendFunc();
		bufferbuilder.begin(7, DefaultVertexFormats.POSITION_COLOR);
		bufferbuilder.vertex(x1, y2, 0.0F).color(f, f1, f2, f3).endVertex();
		bufferbuilder.vertex(x2, y2, 0.0F).color(f, f1, f2, f3).endVertex();
		bufferbuilder.vertex(x2, y1, 0.0F).color(f, f1, f2, f3).endVertex();
		bufferbuilder.vertex(x1, y1, 0.0F).color(f, f1, f2, f3).endVertex();
		bufferbuilder.endVertex();
		WorldVertexBufferUploader.end(bufferbuilder);
		RenderSystem.enableTexture();
		RenderSystem.disableBlend();
	}

}
