package me.partlysunny.game.world.components.render;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.Pool;

public class TextComponent implements Component, Pool.Poolable {

    private String text;
    private String font;
    private float size = 0;

    public String text() {
        return text;
    }

    public float size() {
        return size;
    }

    public String font() {
        return font;
    }

    public void init(String text, String font, float size) {
        this.text = text;
        this.size = size;
        this.font = font;
    }

    @Override
    public void reset() {
        text = null;
        font = null;
        size = 0;
    }
}
