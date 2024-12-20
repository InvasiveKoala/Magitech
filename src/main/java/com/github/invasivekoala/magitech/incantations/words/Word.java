package com.github.invasivekoala.magitech.incantations.words;

import com.github.invasivekoala.magitech.packets.ClientboundPushPacket;
import com.github.invasivekoala.magitech.packets.PacketRegistry;
import net.minecraft.core.BlockPos;
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
        TYPE,
        ANY
    }


    public String registryName;
    public final Types classification;
    public Word(){
        this(Types.ENTITY);
    }
    public Word(Types words){
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
            e.push(direction.x, direction.y, direction.z);

        else { // Since we're serverside we have to send this annoying packet instead
            PacketRegistry.sendTo(new ClientboundPushPacket(new Vec3(direction.x, direction.y, direction.z)), se);
        }
    }

    public static boolean compatible(List<Types> verbTypes, NounWord<?> nounType){
        if (nounType.classification == Types.ANY) return true;
        return verbTypes.contains(nounType.classification);
    }

    protected static Vec3 getPosition(Object obj, boolean eye){
        if (obj instanceof BlockPos b){
            return new Vec3(b.getX(), b.getY(), b.getZ());
        }
        else if (obj instanceof Entity e){
            if (eye) return e.getEyePosition();
            return e.position();
        }
        return null;
    }

    protected static Vec3 getPosition(Object obj){
        return getPosition(obj, true);
    }

}
