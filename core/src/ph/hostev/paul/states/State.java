package ph.hostev.paul.states;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;

public abstract class State {
    protected OrthographicCamera camera;
    protected Vector3 vector;
    protected GameStateManager gsm;

    public State(GameStateManager gameStateManager) {
        this.gsm = gameStateManager;
        this.camera = new OrthographicCamera();
        this.vector = new Vector3();
    }

    protected abstract void handleInput();

    protected abstract void update(float dt);

    protected abstract void render(SpriteBatch sb);

    protected abstract void dispose();
}
