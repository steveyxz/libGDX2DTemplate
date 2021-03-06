package me.partlysunny.game.world.components.player;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.Pool;

public class PlayerControlComponent implements Component, Pool.Poolable {

    private PlayerKeyMap keyMap = null;

    public void setKeyMap(PlayerKeyMap keyMap) {
        this.keyMap = keyMap;
    }

    public PlayerKeyMap keyMap() {
        return keyMap;
    }

    @Override
    public void reset() {
        keyMap = new PlayerKeyMap();
    }
}
