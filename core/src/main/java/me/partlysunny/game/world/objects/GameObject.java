package me.partlysunny.game.world.objects;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.PooledEngine;

public interface GameObject {

    Entity build(float initialX, float initialY);

    default void insert(PooledEngine engine, float initialX, float initialY) {
        engine.addEntity(build(initialX, initialY));
    }

}
