package com.github.invasivekoala.magitech.incantations;

import com.github.invasivekoala.magitech.incantations.words.VerbWord;
import com.github.invasivekoala.magitech.incantations.words.Word;
import com.github.invasivekoala.magitech.incantations.words.adjectives.NearbyAdjective;
import com.github.invasivekoala.magitech.incantations.words.adjectives.NearestAdjective;
import com.github.invasivekoala.magitech.incantations.words.adjectives.RaytraceAdjective;
import com.github.invasivekoala.magitech.incantations.words.nouns.BlockNoun;
import com.github.invasivekoala.magitech.incantations.words.nouns.DirectionNoun;
import com.github.invasivekoala.magitech.incantations.words.nouns.GenericEntityNoun;
import com.github.invasivekoala.magitech.incantations.words.nouns.StackNoun;
import com.github.invasivekoala.magitech.incantations.words.verbs.*;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
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
        word.registryName = wordId;
        WORDS.put(wordId, word);
    }
    private static void registerVerb(String wordId, VerbWord word){
        register(wordId, word);
        VERBS.put(wordId, word);
    }

    static{
        // Verbs
        registerVerb("mori", new WiltVerb());
        registerVerb("adficio", new DrainVerb());
        registerVerb("dis", new PushVerb());
        registerVerb("oriri", new RiseVerb());
        registerVerb("florere", new BloomVerb());
        registerVerb("displodo", new ExplodeVerb());
        registerVerb("memento", new AddToStackVerb());
        registerVerb("oblivisci", new ClearStackVerb());
        registerVerb("duplico", new DoubleStackVerb());
        registerVerb("lapsus", new DropItemVerb());
        registerVerb("lineus", new VectorBetweenStackVerb());
        registerVerb("conjuro", new ConjureVerb());
        registerVerb("impetum", new TargetVerb());
        registerVerb("usus", new UseVerb());
        registerVerb("uro", new BurnVerb());
        registerVerb("curare", new CureVerb());
        registerVerb("volare", new EffectVerb(new MobEffectInstance(MobEffects.LEVITATION, 200)));
        registerVerb("resistere", new EffectVerb(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 200)));
        registerVerb("volito", new EffectVerb(new MobEffectInstance(MobEffects.SLOW_FALLING, 200)));
        registerVerb("moror", new DelayVerb(1));
        registerVerb("quinmoror", new DelayVerb(5));
        registerVerb("decimoror", new DelayVerb(10));
        registerVerb("totumoror", new DelayVerb(20));
        registerVerb("verto", new ChangeOriginVerb());
        // Entity Nouns
        register("me", new GenericEntityNoun<>(EntityType.PLAYER));
        register("bovis", new GenericEntityNoun<>(EntityType.COW));
        register("porcus", new GenericEntityNoun<>(EntityType.PIG));
        register("pullum", new GenericEntityNoun<>(EntityType.CHICKEN));
        register("ignis", new GenericEntityNoun<>(EntityType.FIREBALL, true));
        register("sagitta", new GenericEntityNoun<>(EntityType.ARROW, true));
        register("tnt", new GenericEntityNoun<>(EntityType.TNT, true));
        register("res", new GenericEntityNoun<>(Entity.class));
        register("bestia", new GenericEntityNoun<>(Animal.class));
        register("monstrum", new GenericEntityNoun<>(Monster.class));

        // Adjectives
        register("prope", new NearbyAdjective());
        register("proxi", new NearestAdjective());
        //register("quod", new EntityRaytraceAdjective("that_entity"));
        register("hoc", new RaytraceAdjective());

        // Direction Nouns
        register("ceps", new DirectionNoun((cxt, object) -> object.getForward()));
        register("retro", new DirectionNoun((cxt, object) -> object.getForward().reverse()));
        register("sinis", new DirectionNoun((cxt, object) -> object.getForward().yRot(-90)));
        register("dex", new DirectionNoun((cxt, object) -> object.getForward().yRot(90)));
        register("supra", new DirectionNoun((cxt, object) -> new Vec3(0,1,0)));
        register("infra", new DirectionNoun((cxt, object) -> new Vec3(0,-1,0)));
        register("meceps", new DirectionNoun((cxt, object) -> cxt.playerCaster.getForward()));
        register("meretro", new DirectionNoun((cxt, object) -> cxt.playerCaster.getForward().reverse()));
        register("mesinis", new DirectionNoun((cxt, object) -> cxt.playerCaster.getForward().yRot(-90)));
        register("medex", new DirectionNoun((cxt, object) -> cxt.playerCaster.getForward().yRot(90)));

        // Block Nouns
        register("cubus", new BlockNoun<>());
        register("origo", new BlockNoun<>());

        // Stack Nouns
        register("novus", new StackNoun<>((stack) -> List.of(stack.removeFirst())));
        register("memoria", new StackNoun<>((stack) -> List.of(stack.removeLast())));
        register("memorium", new StackNoun<>((stack) -> {
            List<Object> list = new ArrayList<>(stack);
            stack.clear();
            return list;
        }));

    }


}
