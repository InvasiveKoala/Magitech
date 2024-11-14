package com.github.invasivekoala.magitech.packets;

import com.github.invasivekoala.magitech.Magitech;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;

public final class PacketRegistry {

    private static final String PROTOCOL_VERSION = "1";

    public static final SimpleChannel CHANNEL = NetworkRegistry.newSimpleChannel(new ResourceLocation(Magitech.MOD_ID, "main"),
            () -> PROTOCOL_VERSION, PROTOCOL_VERSION::equals, PROTOCOL_VERSION::equals);

    public static void init(){
        int i = 0;
        CHANNEL.messageBuilder(ClientboundPushPacket.class, i++, NetworkDirection.PLAY_TO_CLIENT)
                .encoder(ClientboundPushPacket::encode).decoder(ClientboundPushPacket::decode)
                .consumer(ClientboundPushPacket::handle).add();
        CHANNEL.messageBuilder(ClientboundDropItemPacket.class, i++, NetworkDirection.PLAY_TO_CLIENT)
                .encoder(ClientboundDropItemPacket::encode).decoder(ClientboundDropItemPacket::decode)
                .consumer(ClientboundDropItemPacket::handle).add();
    }


    public static <MSG> void sendTo(MSG message, ServerPlayer player)
    {
        CHANNEL.sendTo(message, player.connection.getConnection(), NetworkDirection.PLAY_TO_CLIENT);
    }
}
