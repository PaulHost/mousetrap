package ph.hostev.paul;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.CharArray;
import com.badlogic.gdx.utils.TimeUtils;

public class GameScreen implements Screen {

    private final MouseTrapStarter game;

    private OrthographicCamera camera;
    private Texture mouseImg, trapImg, floorImg, heartImg, bloodImg, gameOverImg, pouseImg;
    private Music music;
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
    private float bloodY = 0, bloodX = 0;
    private int mouseCatchCount = 0, life = 5;


    public GameScreen(final MouseTrapStarter mouseTrapStarter) {
        game = mouseTrapStarter;

        mouseImg = new Texture("mouse.png");
        trapImg = new Texture("mausetrap.png");
        floorImg = new Texture("floor.png");
        heartImg = new Texture("heart.png");
        bloodImg = new Texture("blood.png");
        gameOverImg = new Texture("game_over.png");
        pouseImg = new Texture("pause.png");
        nombers = initNombers();

        music = Gdx.audio.newMusic(Gdx.files.internal("mouses.mp3"));
        dead = Gdx.audio.newSound(Gdx.files.internal("dead.mp3"));
        music.setLooping(true);

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

        showMouse();
    }

    @Override
    public void show() {
        music.play();
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0.2f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

//        mouseShowSpeed -= 50000; // TODO stop when 0.01382067___1759(imposible speed)___1089(count)___0

        camera.update();

        game.batch.begin();
        game.batch.draw(floorImg, 0, 0, sceneWidth, sceneHeight);
        game.batch.setProjectionMatrix(camera.combined);
        game.batch.draw(trapImg, trap.x, trap.y, trapWidth, trapHeight);
        if (bloodY > 0) {
            game.batch.draw(bloodImg, bloodX, bloodY, bloodHeight, bloodWidth);
        }
        if (mouseCatchCount > 0) {
            setScore(game.batch, mouseCatchCount);
        }

        for (Rectangle mouse : mouses) {
            game.batch.draw(mouseImg, mouse.x, mouse.y, mouseWidth, mouseHeight);
        }
        if (life > 0) {
            setLives(game.batch, life);
        } else {
            game.setScreen(new GameOverScreen(game, floorImg, gameOverImg, sceneHeight, sceneWidth));
        }

        game.batch.draw(pouseImg, sceneWidth - heartWidth, sceneHeight - heartHeight, heartWidth, heartHeight);
        game.batch.end();

        if (Gdx.input.isTouched()) {
            touchPosition.set(Gdx.input.getX(), Gdx.input.getY(), 0);
            camera.unproject(touchPosition);
            trap.x = (int) (touchPosition.x - trapWidth / 2);
        }

        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) trap.x -= speed * Gdx.graphics.getDeltaTime();
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) trap.x += speed * Gdx.graphics.getDeltaTime();

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
    public void dispose() {
        floorImg.dispose();
        mouseImg.dispose();
        trapImg.dispose();
        music.dispose();
        dead.dispose();
    }
}
