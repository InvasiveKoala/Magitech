package com.github.invasivekoala.magitech.incantations;

import com.github.invasivekoala.magitech.incantations.words.VerbWord;
import com.github.invasivekoala.magitech.incantations.words.Word;
import com.github.invasivekoala.magitech.incantations.words.adjectives.RaytraceAdjective;
import com.github.invasivekoala.magitech.incantations.words.adjectives.NearbyAdjective;
import com.github.invasivekoala.magitech.incantations.words.adjectives.NearestAdjective;
import com.github.invasivekoala.magitech.incantations.words.nouns.BlockNoun;
import com.github.invasivekoala.magitech.incantations.words.nouns.DirectionNoun;
import com.github.invasivekoala.magitech.incantations.words.nouns.GenericEntityNoun;
import com.github.invasivekoala.magitech.incantations.words.nouns.StackNoun;
import com.github.invasivekoala.magitech.incantations.words.verbs.*;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.phys.Vec3;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class WordRegistry {

    // Word classification (for nouns and adjectives)

    public static final HashMap<String, Word> WORDS = new HashMap<>();
    public static final HashMap<String, VerbWord> VERBS = new HashMap<>();

    private static void register(String wordId, Word word){
        WORDS.put(wordId, word);
    }
    private static void registerVerb(String wordId, VerbWord word){
        register(wordId, word);
        VERBS.put(wordId, word);
    }

    static{
        // Verbs
        registerVerb("mori", new WiltVerb("wilt"));
        registerVerb("adficio", new DrainVerb("drain"));
        registerVerb("dis", new PushVerb("push"));
        registerVerb("oriri", new RiseVerb("rise"));
        registerVerb("florere", new BloomVerb("bloom"));
        registerVerb("displodo", new ExplodeVerb("explode"));
        registerVerb("memento", new AddToStackVerb("remember"));
        registerVerb("oblivisci", new ClearStackVerb("forget"));
        registerVerb("duplici", new DoubleStackVerb("double"));
        registerVerb("lapsus", new DropItemVerb("drop"));
        registerVerb("lineus", new VectorBetweenStackVerb("vector"));
        registerVerb("conjuro", new ConjureVerb("conjure"));
        registerVerb("impetum", new TargetVerb("target"));
        registerVerb("usus", new UseVerb("use"));
        // Entity Nouns
        register("me", new GenericEntityNoun<>("player", EntityType.PLAYER));
        register("bovis", new GenericEntityNoun<>("cow", EntityType.COW));
        register("porcus", new GenericEntityNoun<>("pig", EntityType.PIG));
        register("pullum", new GenericEntityNoun<>("chicken", EntityType.CHICKEN));
        register("ignis", new GenericEntityNoun<>("fireball", EntityType.FIREBALL, true));
        register("sagitta", new GenericEntityNoun<>("arrow", EntityType.ARROW, true));
        register("tnt", new GenericEntityNoun<>("tnt", EntityType.TNT, true));
        register("res", new GenericEntityNoun<>("thing", Entity.class));
        register("bestia", new GenericEntityNoun<>("animal", Animal.class));
        register("monstrum", new GenericEntityNoun<>("monster", Monster.class));

        // Adjectives
        register("prope", new NearbyAdjective("nearby"));
        register("proximus", new NearestAdjective("nearest"));
        //register("quod", new EntityRaytraceAdjective("that_entity"));
        register("hoc", new RaytraceAdjective("that_block"));

        // Direction Nouns
        register("ceps", new DirectionNoun("forward", (cxt, object) -> object.getForward()));
        register("retro", new DirectionNoun("backward", (cxt, object) -> object.getForward().reverse()));
        register("sinis", new DirectionNoun("left", (cxt, object) -> object.getForward().yRot(-90)));
        register("dex", new DirectionNoun("right", (cxt, object) -> object.getForward().yRot(90)));
        register("supra", new DirectionNoun("up", (cxt, object) -> new Vec3(0,1,0)));
        register("infra", new DirectionNoun("down", (cxt, object) -> new Vec3(0,-1,0)));
        register("meceps", new DirectionNoun("my_forward", (cxt, object) -> cxt.playerCaster.getForward()));
        register("meretro", new DirectionNoun("my_backward", (cxt, object) -> cxt.playerCaster.getForward().reverse()));
        register("mesinis", new DirectionNoun("my_left", (cxt, object) -> cxt.playerCaster.getForward().yRot(-90)));
        register("medex", new DirectionNoun("my_right", (cxt, object) -> cxt.playerCaster.getForward().yRot(90)));

        // Block Nouns
        register("cubus", new BlockNoun<>("block", BlockPos.class));

        // Stack Nouns
        register("novus", new StackNoun<>("firstmemory", (stack) -> List.of(stack.removeFirst())));
        register("memoria", new StackNoun<>("lastmemory", (stack) -> List.of(stack.removeLast())));
        register("memorium", new StackNoun<>("memories", (stack) -> {
            List<Object> list = new ArrayList<>(stack);
            stack.clear();
            return list;
        }));

    }


}
