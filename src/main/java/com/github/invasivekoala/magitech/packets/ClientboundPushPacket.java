package com.github.invasivekoala.magitech.packets;

import com.github.invasivekoala.magitech.events.ClientEvents;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class ClientboundPushPacket {
    Vec3 vec;

    public ClientboundPushPacket()
    {
    }

    public ClientboundPushPacket(Vec3 pushAmt)
    {
        this.vec = pushAmt;
    }

    public static void encode(ClientboundPushPacket pkt, FriendlyByteBuf buf)
    {
        buf.writeDouble(pkt.vec.x);
        buf.writeDouble(pkt.vec.y);
        buf.writeDouble(pkt.vec.z);

    }

    public static ClientboundPushPacket decode(FriendlyByteBuf buf)
    {
        return new ClientboundPushPacket(new Vec3(buf.readDouble(), buf.readDouble(), buf.readDouble()));
    }

    public static void handle(ClientboundPushPacket message, Supplier<NetworkEvent.Context> context)
    {
        context.get().enqueueWork(() -> {
            if (ClientEvents.getClient().player == null) return;
            ClientEvents.getClient().player.push(message.vec.x, message.vec.y, message.vec.z);
        });
        context.get().setPacketHandled(true);
    }

}
