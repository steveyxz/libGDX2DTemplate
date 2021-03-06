package me.partlysunny.game.world.systems.render;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.utils.Array;
import me.partlysunny.game.util.constants.Mappers;
import me.partlysunny.game.world.components.render.ActorComponent;

public class ActorUpdatingSystem extends IteratingSystem {

    private final Batch batch;
    private final Array<Entity> renderQueue; // an array used to allow sorting of images allowing us to draw images on top of each other

    public ActorUpdatingSystem(Batch batch) {
        super(Family.all(ActorComponent.class).get());
        this.batch = batch;
        this.renderQueue = new Array<>();
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        renderQueue.add(entity);
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);

        for (Entity e : renderQueue) {
            ActorComponent actor = Mappers.actorMapper.get(e);
            actor.update(deltaTime, batch);
        }

        renderQueue.clear();
    }
}
