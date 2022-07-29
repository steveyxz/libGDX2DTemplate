package me.partlysunny.game.world.systems.render;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.SortedIteratingSystem;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import me.partlysunny.game.util.constants.FontPresets;
import me.partlysunny.game.util.constants.GameInfo;
import me.partlysunny.game.util.constants.Mappers;
import me.partlysunny.game.world.components.collision.TransformComponent;
import me.partlysunny.game.world.components.render.TextComponent;
import me.partlysunny.game.world.components.render.TextureComponent;
import me.partlysunny.game.world.components.render.TintComponent;

import java.util.Comparator;

public class TextureRenderingSystem extends SortedIteratingSystem {

    public static final float PPM = 8f;

    public static final float FRUSTUM_WIDTH = GameInfo.SCREEN_WIDTH / PPM;
    public static final float FRUSTUM_HEIGHT = GameInfo.SCREEN_HEIGHT / PPM;

    public static final float PIXELS_TO_METRES = 1.0f / PPM;

    private static final Vector2 meterDimensions = new Vector2();
    private static final Vector2 pixelDimensions = new Vector2();
    private final Batch batch; // a reference to our spritebatch
    private final Array<Entity> renderQueue; // an array used to allow sorting of images allowing us to draw images on top of each other
    private final Comparator<Entity> comparator = new ZComparator(); // a comparator to sort images based on the z position of the transfromComponent

    public TextureRenderingSystem(Batch batch, Camera camera) {
        // gets all entities with a TransformComponent and TextureComponent
        super(Family.all(TransformComponent.class).one(TextureComponent.class, TextComponent.class).get(), new ZComparator());

        // create the array for sorting entities
        renderQueue = new Array<>();

        this.batch = batch;  // set our batch to the one supplied in constructor

        // set up the camera to match our screen size
        camera.position.set(FRUSTUM_WIDTH / 2f, FRUSTUM_HEIGHT / 2f, 0);
    }

    public static Vector2 getScreenSizeInMeters() {
        meterDimensions.set(GameInfo.SCREEN_WIDTH * PIXELS_TO_METRES,
                GameInfo.SCREEN_HEIGHT * PIXELS_TO_METRES);
        return meterDimensions;
    }

    public static Vector2 getScreenSizeInPixels() {
        pixelDimensions.set(GameInfo.SCREEN_WIDTH, GameInfo.SCREEN_HEIGHT);
        return pixelDimensions;
    }

    public static float pixelsToMeters(float pixelValue) {
        return pixelValue * PIXELS_TO_METRES;
    }

    public static float metersToPixels(float meterValue) {
        return meterValue / PIXELS_TO_METRES;
    }

    @Override
    public void update(float delta) {
        super.update(delta);

        // sort the renderQueue based on z index
        renderQueue.sort(comparator);

        // loop through each entity in our render queue
        for (Entity entity : renderQueue) {
            TransformComponent transform = Mappers.transformMapper.get(entity);
            Vector3 tint = null;
            float opacity = 0;
            if (Mappers.tintMapper.has(entity)) {
                TintComponent tintComponent = Mappers.tintMapper.get(entity);
                tint = tintComponent.tint();
                opacity = tintComponent.alpha();
            }
            TextureComponent texture = Mappers.textureMapper.get(entity);
            TextComponent text = Mappers.textMapper.get(entity);

            if (tint != null) {
                batch.setColor(new Color(tint.x, tint.y, tint.z, opacity));
            }

            if (texture != null) {
                if (texture.texture() == null || texture.isHidden()) {
                    continue;
                }

                float width = texture.texture().getRegionWidth();
                float height = texture.texture().getRegionHeight();

                float originX = width / 2f;
                float originY = height / 2f;

                float x = transform.position.x;
                float y = transform.position.y;
                float x1 = x - originX;
                float y1 = y - originY;

                float rotation = transform.rotation;

                batch.draw(texture.texture(),
                        x1, y1,
                        originX, originY,
                        width, height,
                        transform.scale.x / texture.texture().getRegionWidth(), transform.scale.y / texture.texture().getRegionHeight(),
                        rotation * MathUtils.radiansToDegrees);
            } else if (text != null) {
                float size = text.size();
                String content = text.text();
                String font = text.font();

                BitmapFont fontWithSize = FontPresets.getFontWithSize(font, size);
                if (tint != null) {
                    fontWithSize.setColor(new Color(tint.x, tint.y, tint.z, opacity));
                }
                fontWithSize.draw(batch, content, transform.position.x, transform.position.y);
                fontWithSize.setColor(Color.BLACK);
            }

            batch.setColor(Color.WHITE);
        }

        renderQueue.clear();
    }

    @Override
    public void processEntity(Entity entity, float deltaTime) {
        renderQueue.add(entity);
    }
}
