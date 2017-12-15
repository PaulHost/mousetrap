package ph.hostev.paul;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class MouseTrapStarter extends Game {

    SpriteBatch batch;
    BitmapFont bitmapFont;

    @Override
    public void create() {
        batch = new SpriteBatch();
        bitmapFont = new BitmapFont();
        this.setScreen(new MainMenuScreen(this));
    }

    @Override
    public void resume() {
        super.resume();
    }

    @Override
    public void dispose() {
        super.dispose();
        batch.dispose();
        bitmapFont.dispose();
    }
}
