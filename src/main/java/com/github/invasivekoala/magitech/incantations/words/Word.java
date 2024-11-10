package com.github.invasivekoala.magitech.incantations.words;

import com.github.invasivekoala.magitech.incantations.WordRegistry;
import net.minecraft.network.chat.TranslatableComponent;

import java.util.List;

public abstract class Word {
    protected final String registryName;
    public final List<WordRegistry.Types> classification;
    public Word(String registryName){
        this(registryName, WordRegistry.Types.ENTITY);
    }
    public Word(String registryName, WordRegistry.Types... words){
        this.registryName = registryName;
        classification = List.of(words);
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

    public static boolean compatible(Word w1, Word w2){
        return w1.classification.stream().anyMatch(w2.classification::contains);
    }

}
