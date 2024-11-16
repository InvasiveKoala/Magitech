package com.github.invasivekoala.magitech.client.screen.hexicon;

import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.TextComponent;

public class HexiconEntry extends Screen {

    final Screen parentScreen;
    protected HexiconEntry(Screen screen) {
        super(TextComponent.EMPTY);
        this.parentScreen=screen;
    }
}
