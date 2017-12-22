package ph.hostev.paul;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;

import ph.hostev.paul.states.GameStateManager;
import ph.hostev.paul.states.MenuState;

public class GameScreen extends ApplicationAdapter {

    private GameStateManager gsm;
    private SpriteBatch batch;
    private Stage stage;

    public GameScreen() {
    }

    @Override
    public void create() {
        stage = new Stage();
        gsm = new GameStateManager();
        batch = new SpriteBatch();

        Gdx.input.setInputProcessor(stage);
        Gdx.gl.glClearColor(1, 0, 0, 1);

        gsm.push(new MenuState(gsm, stage));
    }

    @Override
    public void render() {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        gsm.update(Gdx.graphics.getDeltaTime());
        gsm.render(batch);
    }
}
