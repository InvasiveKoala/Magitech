package com.github.invasivekoala.magitech.packets;

import com.github.invasivekoala.magitech.Magitech;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;

/*public final class PacketRegistry {

    private static final String PROTOCOL_VERSION = "1";

    public static final SimpleChannel CHANNEL = NetworkRegistry.newSimpleChannel(new ResourceLocation(Magitech.MOD_ID, "main"),
            () -> PROTOCOL_VERSION, PROTOCOL_VERSION::equals, PROTOCOL_VERSION::equals);

    public static void init(){
        int i = 0;
        CHANNEL.messageBuilder(ServerboundCraftBrainPacket.class, i++, NetworkDirection.PLAY_TO_SERVER).add();
    }
}
*/