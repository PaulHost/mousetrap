package ph.hostev.paul.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;

import ph.hostev.paul.admob.AdManager;

public class MenuState extends State {

    Stage stage;
    private Texture bg, bf, playBtn;
    private int w, h, playWidth;
    private AdManager adMob;

    public MenuState(GameStateManager gameStateManager, Stage stage, AdManager adMob) {
        super(gameStateManager);
        this.stage = stage;
        bg = new Texture("floor.png");
        bf=new Texture("blood-frame.png");
        playBtn = new Texture("play.png");
        this.adMob = adMob;

        w = Gdx.graphics.getWidth();
        h = Gdx.graphics.getHeight();
        playWidth = h / 6;
    }

    @Override
    protected void handleInput() {
        if (Gdx.input.justTouched()) {
            gsm.set(new GameState(gsm, stage, adMob));
            if (adMob != null) adMob.hide();
        }

    }

    @Override
    protected void update(float dt) {
        handleInput();
    }

    @Override
    protected void render(SpriteBatch sb) {
        sb.begin();
        sb.draw(bg, 0, 0, w, h);
        sb.draw(bf, 0, 0, w, h);
        sb.draw(playBtn, w / 2 - playWidth / 2, h / 2 - playWidth / 2, playWidth, playWidth);
        sb.end();
    }

    @Override
    protected void dispose() {
        bg.dispose();
        playBtn.dispose();
    }
}
