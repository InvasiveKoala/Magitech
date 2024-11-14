package com.github.invasivekoala.magitech.packets;

import com.github.invasivekoala.magitech.events.ClientEvents;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.InteractionHand;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class ClientboundDropItemPacket {

    public ClientboundDropItemPacket()
    {
    }


    public static void encode(ClientboundDropItemPacket pkt, FriendlyByteBuf buf)
    {
    }

    public static ClientboundDropItemPacket decode(FriendlyByteBuf buf)
    {
        return new ClientboundDropItemPacket();
    }

    public static void handle(ClientboundDropItemPacket message, Supplier<NetworkEvent.Context> context)
    {
        context.get().enqueueWork(() -> {
            LocalPlayer p = ClientEvents.getClient().player;
            if (p == null) return;
            p.drop(false);
            p.swing(InteractionHand.MAIN_HAND);
        });
        context.get().setPacketHandled(true);
    }

}
