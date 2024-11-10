package com.github.invasivekoala.magitech.incantations.words.nouns;

import com.github.invasivekoala.magitech.incantations.WordRegistry;
import com.github.invasivekoala.magitech.incantations.words.NounWord;
import net.minecraft.world.entity.player.Player;

public class PlayerNoun extends NounWord<Player> {
    public PlayerNoun(String id, WordRegistry.Types... t) {
        super(id, t);
    }

    @Override
    public Class<Player> nounClass() {
        return Player.class;
    }
}
