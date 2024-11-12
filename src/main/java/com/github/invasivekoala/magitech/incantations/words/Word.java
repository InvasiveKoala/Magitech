package com.github.invasivekoala.magitech.incantations.words;

import com.github.invasivekoala.magitech.packets.ClientboundPushPacket;
import com.github.invasivekoala.magitech.packets.PacketRegistry;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;

import java.util.List;


public abstract class Word {

    public enum Types{
        ENTITY,
        BLOCK,
        DIRECTION,
        ANY
    }


    public final String registryName;
    public final Types classification;
    public Word(String registryName){
        this(registryName, Types.ENTITY);
    }
    public Word(String registryName, Types words){
        this.registryName = registryName;
        classification = words;
    }

    protected TranslatableComponent getTranslation(){
        return new TranslatableComponent("word.magitech." + registryName +"_translation");
    }

    protected TranslatableComponent getLatin(){
        return new TranslatableComponent("word.magitech." + registryName);
    }
    public String latin(){
        return this.getLatin().getContents();
    }
    public String translated(){
        return getTranslation().getContents();
    }


    public static void push(Entity e, Vec3 direction){
        if (!(e instanceof ServerPlayer se))
            e.push(0, 1.0f, 0);

        else { // Since we're serverside we have to send this annoying packet instead
            PacketRegistry.sendTo(new ClientboundPushPacket(new Vec3(0, 1.0f, 0)), se);
        }
    }

    public static boolean compatible(List<Types> verbTypes, NounWord<?> nounType){
        if (nounType.classification == Types.ANY) return true;
        return verbTypes.contains(nounType.classification);
    }

}
