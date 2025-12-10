package net.reminitous.civilizationsmod.monument;

import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;

public class MonumentScreen extends AbstractContainerScreen<MonumentMenu> {

    public MonumentScreen(MonumentMenu menu, Inventory inv, Component title) {
        super(menu, inv, title);
    }

    @Override
    protected void renderBg(net.minecraft.client.gui.GuiGraphics gfx, float partialTicks, int mouseX, int mouseY) {
        // Render custom background and buttons
    }

    @Override
    public void render(net.minecraft.client.gui.GuiGraphics gfx, int mouseX, int mouseY, float partialTicks) {
        super.render(gfx, mouseX, mouseY, partialTicks);
    }
}
