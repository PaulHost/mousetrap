package ph.hostev.paul;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
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
import com.badlogic.gdx.utils.TimeUtils;

public class MousetrapStarter extends ApplicationAdapter {
    private OrthographicCamera camera;
    private SpriteBatch batch;
    private Texture mouseImg, trapImg;
    private Music music;
    private Sound dead;
    private Rectangle trap;
    private Array<Rectangle> mouses;
    private Vector3 touchPosition;
    private long lastMousTime;
    private int speed = 200, mouseShowSpeed = 1000000000;
    private int sceneWidth, sceneHeight;
    private int mouseWidth, mouseHeight;
    private int trapWidth, trapHeight, size = 2;


    @Override
    public void create() {
        batch = new SpriteBatch();

        mouseImg = new Texture("mouse.png");
        trapImg = new Texture("mausetrap.png");

        music = Gdx.audio.newMusic(Gdx.files.internal("mouses.mp3"));
        dead = Gdx.audio.newSound(Gdx.files.internal("dead.mp3"));
        music.setLooping(true);
        music.play();

        sceneHeight = Gdx.graphics.getHeight();
        sceneWidth = Gdx.graphics.getWidth();
        mouseWidth = mouseImg.getWidth() / size;
        mouseHeight = mouseImg.getHeight() / size;
        trapWidth = trapImg.getWidth() / size;
        trapHeight = trapImg.getHeight() / size;

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
    public void render() {
        Gdx.gl.glClearColor(0, 0, 0.2f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        mouseShowSpeed -= 50000;

        camera.update();

        batch.begin();
        batch.setProjectionMatrix(camera.combined);
        batch.draw(trapImg, trap.x, trap.y, trapWidth, trapHeight);
        for (Rectangle mouse : mouses) {
            batch.draw(mouseImg, mouse.x, mouse.y, mouseWidth, mouseHeight);
        }
        batch.end();

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
            speed++;
        }

        for (int i = 0; i < mouses.size; i++) {
            mouses.get(i).y -= speed * Gdx.graphics.getDeltaTime();
            if (mouses.get(i).overlaps(trap)) {
                dead.play();
                mouses.removeIndex(i);
            }
            if (mouses.size < 0 && mouses.get(i).y + mouseHeight < 0) mouses.removeIndex(i);
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
        batch.dispose();
        mouseImg.dispose();
        trapImg.dispose();
        music.dispose();
        dead.dispose();
    }
}
