package me.partlysunny.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.kotcrab.vis.ui.VisUI;
import de.eskalon.commons.screen.ManagedScreen;
import me.partlysunny.game.util.constants.GameInfo;
import me.partlysunny.game.MainGame;

public abstract class BasicGuiScreen extends ManagedScreen {

    protected final Camera camera = new OrthographicCamera(GameInfo.SCREEN_WIDTH, GameInfo.SCREEN_HEIGHT);
    protected final Viewport viewport = new ExtendViewport(camera.viewportWidth, camera.viewportHeight, camera);
    protected final MainGame game;
    protected final Stage stage;

    public BasicGuiScreen(MainGame game) {
        this.game = game;
        this.stage = new Stage(viewport, game.batch());
        preGui();
        createGui();
    }

    public MainGame game() {
        return game;
    }

    public Stage stage() {
        return stage;
    }

    @Override
    public void show() {
        super.show();
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    protected void create() {
    }

    @Override
    public void hide() {
        //Nothing happens here
    }

    @Override
    public void render(float delta) {
        viewport.update(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        stage.act(delta);
        additionalActs(delta);
        stage.draw();
    }

    @Override
    public void dispose() {
        stage.dispose();
        VisUI.dispose();
    }

    @Override
    public Color getClearColor() {
        return GameInfo.BACKGROUND_COLOR;
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height);
    }

    protected void additionalActs(float delta) {
        //Any additional stuff to do in the acting phase (every frame) can go here
    }

    protected void preGui() {
        //Override in children if you want something to happen before UI initializes
    }

    protected abstract void createGui();
}
