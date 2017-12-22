package ph.hostev.paul.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.CharArray;
import com.badlogic.gdx.utils.TimeUtils;

public class GameState extends State {

    private OrthographicCamera camera;
    private Texture mouseImg, trapImg, floorImg, heartImg, bloodImg, playImg, pouseImg;
    private Sound dead;
    private Rectangle trap;
    private Array<Rectangle> mouses;
    private Array<Texture> nombers;
    private Vector3 touchPosition;
    private long lastMousTime;
    private int speed = 200, step, mouseShowSpeed = 1000000000;
    private int sceneWidth, sceneHeight;
    private int mouseWidth, mouseHeight;
    private int trapWidth, trapHeight;
    private int heartWidth, heartHeight;
    private int bloodWidth, bloodHeight;
    private int playWidth;
    private float bloodY = 0, bloodX = 0;
    private int mouseCatchCount = 0, life = 5;

    private Stage mStage;
    private ImageButton pauseBtn, playBtn;
    private boolean play = true;


    public GameState(GameStateManager gameStateManager, final Stage stage) {
        super(gameStateManager);

        mouseImg = new Texture("mouse.png");
        trapImg = new Texture("mausetrap.png");
        floorImg = new Texture("floor.png");
        heartImg = new Texture("heart.png");
        bloodImg = new Texture("blood.png");
        playImg = new Texture("play.png");
        pouseImg = new Texture("pause.png");
        nombers = initNombers();

        dead = Gdx.audio.newSound(Gdx.files.internal("dead.mp3"));

        sceneHeight = Gdx.graphics.getHeight();
        sceneWidth = Gdx.graphics.getWidth();
        mouseWidth = sceneHeight / 7;
        mouseHeight = mouseWidth + mouseWidth / 2;
        trapWidth = mouseWidth;
        trapHeight = trapWidth;
        heartWidth = sceneHeight / 10;
        heartHeight = heartWidth;
        bloodHeight = mouseHeight;
        bloodWidth = mouseWidth;
        playWidth = sceneHeight / 4;

        speed = sceneHeight * 24 / 100;
        step = speed / 100;

        camera = new OrthographicCamera();
        camera.setToOrtho(false, sceneWidth, sceneHeight);

        touchPosition = new Vector3();

        mouses = new Array<Rectangle>();

        trap = new Rectangle();
        trap.x = sceneWidth / 2 - trapWidth / 2;
        trap.y = 2;
        trap.width = trapWidth;
        trap.height = trapHeight;

        pauseBtn = new ImageButton(new TextureRegionDrawable(new TextureRegion(pouseImg)));
        pauseBtn.setPosition(sceneWidth - heartWidth, sceneHeight - heartHeight);
        pauseBtn.setSize(heartWidth, heartHeight);
        pauseBtn.addListener(new ClickListener() {

            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                play = false;
                playBtn.setVisible(true);
                pauseBtn.setVisible(false);
            }
        });

        playBtn = new ImageButton(new TextureRegionDrawable(new TextureRegion(playImg)));
        playBtn.setPosition(sceneWidth / 2 - playWidth / 2, sceneHeight / 2 - playWidth / 2);
        playBtn.setVisible(false);
        playBtn.setSize(playWidth, playWidth);
        playBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                play = true;
                pauseBtn.setVisible(true);
                playBtn.setVisible(false);
            }
        });

        mStage = stage;
        mStage.addActor(pauseBtn);
        mStage.addActor(playBtn);

        showMouse();
    }

    @Override
    protected void handleInput() {
        if (Gdx.input.isTouched() && play) {
            touchPosition.set(Gdx.input.getX(), Gdx.input.getY(), 0);
            camera.unproject(touchPosition);
            trap.x = (int) (touchPosition.x - trapWidth / 2);
        }

        if (Gdx.input.isKeyPressed(Input.Keys.LEFT) && play)
            trap.x -= speed * Gdx.graphics.getDeltaTime();
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT) && play)
            trap.x += speed * Gdx.graphics.getDeltaTime();
    }

    @Override
    protected void update(float dt) {
        handleInput();
        if (play) {

            if (trap.x < 0) trap.x = 0;
            if (trap.x > sceneWidth - trapWidth) trap.x = sceneWidth - trapWidth;

            if (TimeUtils.nanoTime() - lastMousTime > mouseShowSpeed) {
                showMouse();
                speed += step;
            }

            for (int i = 0; i < mouses.size; i++) {
                mouses.get(i).y -= speed * Gdx.graphics.getDeltaTime();
                if (mouses.get(i).overlaps(trap)) {
                    bloodY = mouses.get(i).y;
                    bloodX = mouses.get(i).x;
                    mouseCatchCount++;
                    dead.play();
                    mouses.removeIndex(i);
                } else if (mouses.get(i).y + mouseHeight < 0) {
                    life--;
                    mouses.removeIndex(i);
                }
            }

        }
    }

    @Override
    protected void render(SpriteBatch sb) {

        camera.update();

        sb.begin();
        sb.draw(floorImg, 0, 0, sceneWidth, sceneHeight);
        sb.setProjectionMatrix(camera.combined);
        sb.draw(trapImg, trap.x, trap.y, trapWidth, trapHeight);
        if (bloodY > 0) {
            sb.draw(bloodImg, bloodX, bloodY, bloodHeight, bloodWidth);
        }
        if (mouseCatchCount > 0) {
            setScore(sb, mouseCatchCount);
        }

        for (Rectangle mouse : mouses) {
            sb.draw(mouseImg, mouse.x, mouse.y, mouseWidth, mouseHeight);
        }
        if (life > 0) {
            setLives(sb, life);
        } else {
            gsm.set(new MenuState(gsm, mStage));
        }

        sb.end();

        mStage.act(Gdx.graphics.getDeltaTime()); //Perform ui logic
        mStage.draw();

    }

    private Array<Texture> initNombers() {
        Array<Texture> nom = new Array<Texture>();
        for (int i = 0; i < 10; i++) {
            nom.add(new Texture(i + ".png"));
        }
        return nom;
    }

    private void setLives(final SpriteBatch batch, int count) {
        int pos = sceneWidth - heartWidth * 2;
        for (int i = 0; i < count; i++) {
            batch.draw(heartImg, pos, sceneHeight - heartHeight, heartHeight, heartWidth);
            pos -= heartWidth;
        }
    }

    private void setScore(final SpriteBatch batch, int count) {
        String s = Integer.toString(count);
        CharArray chars = CharArray.with(s.toCharArray());
        Texture texture;
        int pos = 0;
        for (int i = 0; i < chars.size; i++) {
            texture = nombers.get(Character.getNumericValue(chars.get(i)));
            batch.draw(texture, pos, sceneHeight - mouseHeight, mouseWidth, mouseHeight);
            pos += mouseWidth;
        }
    }

    private void showMouse() {
        Rectangle newMouse = new Rectangle();
        newMouse.width = mouseWidth;
        newMouse.height = mouseHeight;
        newMouse.x = MathUtils.random(0, sceneWidth - mouseWidth);
        newMouse.y = sceneHeight;
        mouses.add(newMouse);
        lastMousTime = TimeUtils.nanoTime();
    }

    @Override
    protected void dispose() {
        floorImg.dispose();
        mouseImg.dispose();
        trapImg.dispose();
        heartImg.dispose();
        pouseImg.dispose();
        bloodImg.dispose();
        dead.dispose();
    }
}
