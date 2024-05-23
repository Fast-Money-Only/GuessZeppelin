package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.ScreenUtils;

public class GameScreen implements Screen {
    final GuessZep game;
    OrthographicCamera camera;
    Zeppelin zeppelin;
    Texture background;
    BitmapFont font;
    boolean simulationStarted;
    boolean zeppelinFalling;
    String message;
    float fallSpeed;

    // Textures for different materials
    Texture material1Texture;
    Texture material2Texture;
    Texture material3Texture;
    Texture material4Texture;

    // Buttons
    Rectangle material1Button;
    Rectangle material2Button;
    Rectangle material3Button;
    Rectangle material4Button;
    Rectangle playButton;

    public GameScreen(final GuessZep game) {
        this.game = game;

        // Load textures
        background = new Texture(Gdx.files.internal("download2.jpg"));
        material1Texture = new Texture(Gdx.files.internal("zepLeather.png"));
        material2Texture = new Texture(Gdx.files.internal("almindeligZep.png"));
        material3Texture = new Texture(Gdx.files.internal("zepFabric.png"));
        material4Texture = new Texture(Gdx.files.internal("zepLeather.png"));

        // Setup camera
        camera = new OrthographicCamera();
        camera.setToOrtho(false, 800, 600);

        // Initialize Zeppelin
        zeppelin = new Zeppelin(280, 300, 240, 100);

        // Initialize buttons
        material1Button = new Rectangle(20, 50, 150, 50);
        material2Button = new Rectangle(170, 50, 150, 50);
        material3Button = new Rectangle(380, 50, 100, 50);
        material4Button = new Rectangle(470, 50, 150, 50);
        playButton = new Rectangle(680, 50, 100, 50);

        // Font
        font = new BitmapFont();

        // Simulation state
        simulationStarted = false;
        zeppelinFalling = false;
        message = "";
        fallSpeed = 0;
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0, 0, 0.2f, 1);
        camera.update();
        game.batch.setProjectionMatrix(camera.combined);

        game.batch.begin();

        // Draw background
        game.batch.draw(background, 0, 0);

        // Draw Zeppelin
        game.batch.draw(zeppelin.getTexture(), zeppelin.getRectangle().x, zeppelin.getRectangle().y,
                zeppelin.getRectangle().width, zeppelin.getRectangle().height);

        // Draw buttons if simulation hasn't started
        if (!simulationStarted) {
            font.draw(game.batch, "Læder (200 køer)", material1Button.x, material1Button.y + 30);
            font.draw(game.batch, "Blindtarme (250.000 køer)", material2Button.x, material2Button.y + 30);
            font.draw(game.batch, "Stof", material3Button.x, material3Button.y + 30);
            font.draw(game.batch, "Læder (150.000 køer)", material4Button.x, material4Button.y + 30);
            font.draw(game.batch, "Play", playButton.x + 20, playButton.y + 30);
        }

        // Draw message
        font.draw(game.batch, message, 300, 580);

        game.batch.end();

        // Handle input
        if (!simulationStarted && Gdx.input.justTouched()) {
            int x = Gdx.input.getX();
            int y = Gdx.input.getY();
            y = 600 - y; // Convert to y-down coordinate system

            if (material1Button.contains(x, y)) {
                zeppelin.setTexture(material1Texture, false);
            } else if (material2Button.contains(x, y)) {
                zeppelin.setTexture(material2Texture, true);
            } else if (material3Button.contains(x, y)) {
                zeppelin.setTexture(material3Texture, false);
            } else if (material4Button.contains(x, y)) {
                zeppelin.setTexture(material4Texture, false);
            } else if (playButton.contains(x, y)) {
                startSimulation();
            }
        }

        // Handle simulation
        if (simulationStarted && !zeppelin.isCorrectMaterial()) {
            zeppelin.getRectangle().y -= fallSpeed * delta;
            fallSpeed += 98 * delta; // Increase fall speed over time (simulating gravity)

            if (zeppelin.getRectangle().y <= 0) {
                zeppelin.getRectangle().y = 0;
                zeppelinFalling = false;
                message = "The Zeppelin crashes! Game over.";
            }
        }
    }

    private void startSimulation() {
        simulationStarted = true;
        if (zeppelin.isCorrectMaterial()) {
            message = "The Zeppelin stays afloat! You win!";
        } else {
            zeppelinFalling = true;
            fallSpeed = 100; // Initial fall speed
            message = "Wrong material";
        }
    }

    @Override
    public void resize(int width, int height) {
    }

    @Override
    public void show() {
    }

    @Override
    public void hide() {
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }

    @Override
    public void dispose() {
        background.dispose();
        material1Texture.dispose();
        material2Texture.dispose();
        material3Texture.dispose();
        material4Texture.dispose();
        zeppelin.getTexture().dispose();
        font.dispose();
    }
}
