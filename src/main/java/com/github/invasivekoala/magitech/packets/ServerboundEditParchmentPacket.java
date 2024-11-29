package com.github.invasivekoala.magitech.packets;

import com.github.invasivekoala.magitech.items.ItemRegistry;
import com.github.invasivekoala.magitech.items.parchment.ParchmentItem;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class ServerboundEditParchmentPacket {
    String text;
    int index;

    public ServerboundEditParchmentPacket()
    {
    }

    public ServerboundEditParchmentPacket(String text, int index)
    {
        this.text=  text;
        this.index = index;
    }

    public static void encode(ServerboundEditParchmentPacket pkt, FriendlyByteBuf buf)
    {
        buf.writeUtf(pkt.text); // TODO length
        buf.writeInt(pkt.index);
    }

    public static ServerboundEditParchmentPacket decode(FriendlyByteBuf buf)
    {
        return new ServerboundEditParchmentPacket(buf.readUtf(), buf.readInt());
    }

    public static void handle(ServerboundEditParchmentPacket message, Supplier<NetworkEvent.Context> context)
    {
        ServerPlayer player = context.get().getSender();
        ItemStack parchment = player.getSlot(message.index).get();
        if (parchment.is(ItemRegistry.SPELL_PARCHMENT.get())) {
            ParchmentItem.savePageTextToNbt(player, parchment, message.text);
        }
        context.get().setPacketHandled(true);
    }

}
