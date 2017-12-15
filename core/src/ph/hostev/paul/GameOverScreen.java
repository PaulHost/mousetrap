package ph.hostev.paul;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;

/**
 * Created by paulhostev on 15.12.2017.
 */

class GameOverScreen implements Screen {

    MouseTrapStarter game;
    Texture floorImg, gameOverImg;
    int screenHeight, screenWidth;

    public GameOverScreen(MouseTrapStarter game, Texture floorImg, Texture gameOverImg, int screenHeight, int screenWidth) {
        this.game = game;
        this.gameOverImg = gameOverImg;
        this.floorImg = floorImg;
        this.screenHeight = screenHeight;
        this.screenWidth = screenWidth;
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        game.batch.begin();
        game.batch.draw(floorImg, 0, 0, screenWidth, screenHeight);
        game.batch.draw(gameOverImg, 0, 0, screenWidth, screenHeight);
        game.batch.end();
        if (Gdx.input.isTouched()) {
            game.setScreen(new GameScreen(game));
        }
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {

    }
}
