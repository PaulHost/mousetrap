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
    private Texture mouseImg;
    private Texture trapImg;
    private Music music;
    private Sound dead;
    private Rectangle trap;
    private Array<Rectangle> mouses;
    private Vector3 touchPosition;
    private long lastMousTime;
    private int speed = 200;
    private int sceneWidth = 800;
    private int sceneHeight = 480;

    @Override
    public void create() {
        batch = new SpriteBatch();

        camera = new OrthographicCamera();
        camera.setToOrtho(false, sceneWidth, sceneHeight);

        touchPosition = new Vector3();

        mouses = new Array<Rectangle>();

        mouseImg = new Texture("mouse.png");
        trapImg = new Texture("mausetrap.png");

        music = Gdx.audio.newMusic(Gdx.files.internal("mouses.mp3"));
        dead = Gdx.audio.newSound(Gdx.files.internal("dead.mp3"));

        music.setLooping(true);
        music.play();

        trap = new Rectangle();
        trap.x = (sceneWidth - trapImg.getWidth()) / 2;
        trap.y = 2;
        trap.width = trapImg.getWidth();
        trap.height = trapImg.getHeight();

        showMouse();
    }

    @Override
    public void render() {
        Gdx.gl.glClearColor(0, 0, 0.2f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        camera.update();

        batch.begin();
        batch.setProjectionMatrix(camera.combined);
        batch.draw(trapImg, trap.x, trap.y);
        for (Rectangle mouse : mouses) {
            batch.draw(mouseImg, mouse.x, mouse.y);
        }
        batch.end();

        if (Gdx.input.isTouched()) {
            touchPosition.set(Gdx.input.getX(), Gdx.input.getY(), 0);
            camera.unproject(touchPosition);
            trap.x = (int) (touchPosition.x - trapImg.getWidth() / 2);
        }

        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) trap.x += speed * Gdx.graphics.getDeltaTime();
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) trap.x -= speed * Gdx.graphics.getDeltaTime();

        if (trap.x < 0) trap.x = 0;
        if (trap.x > sceneWidth - trapImg.getWidth()) trap.x = sceneWidth - trapImg.getWidth();

        if (TimeUtils.nanoTime() - lastMousTime > 1000000000) showMouse();


        for (int i = 0; i < mouses.size; i++) {
            mouses.get(i).y -= speed * Gdx.graphics.getDeltaTime();
            if (mouses.get(i).overlaps(trap)) {
                dead.play();
                mouses.removeIndex(i);
            }
            if (mouses.get(i).y + mouseImg.getHeight() < 0) mouses.removeIndex(i);
            System.out.println(mouses.size);
        }
    }

    private void showMouse() {
        Rectangle newMouse = new Rectangle();
        newMouse.x = MathUtils.random(0, sceneWidth - mouseImg.getWidth());
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
