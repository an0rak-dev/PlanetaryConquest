package com.github.an0rakdev.planetaryconquest.renderers;

import android.content.Context;
import android.opengl.Matrix;
import android.os.SystemClock;

import com.github.an0rakdev.planetaryconquest.MathUtils;
import com.github.an0rakdev.planetaryconquest.OpenGLUtils;
import com.github.an0rakdev.planetaryconquest.R;
import com.github.an0rakdev.planetaryconquest.graphics.models.AsteroidField;
import com.github.an0rakdev.planetaryconquest.graphics.models.Coordinates;
import com.github.an0rakdev.planetaryconquest.graphics.models.Laser;
import com.github.an0rakdev.planetaryconquest.graphics.models.polyhedrons.Sphere;
import com.google.vr.sdk.audio.GvrAudioEngine;
import com.google.vr.sdk.base.Eye;
import com.google.vr.sdk.base.HeadTransform;

import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.List;

import javax.microedition.khronos.egl.EGLConfig;

/**
 * This renderer manages the second demo of the application which
 * make the user across an asteroid fields and shoot them.
 *
 * @author Sylvain Nieuwlandt
 * @version 1.0
 */
public class AsteroidsRenderer extends SpaceRenderer {
	// MUST BE a single channel track, or the gvrAudioEngine.createSoundObject will return -1.
	private static final String LASER_SOUNDFILE = "laser.wav";
	private static final String EXPLOSION_SOUNDFILE = "explosion.wav";
    private final AsteroidField field;
	private final float cameraSpeed;

	private int celestialProgram;
	private float cameraPosZ;
	private final float[] camera;
	private final float[] view;
	private final float[] modelView;
	private final float[] mvp;

	private int hudProgram;
	private final float[] crossPosition;
	private final float[] modelHud;
	private final float[] hudCamera;
	private FloatBuffer crossColor;
	private FloatBuffer crossVertices;
	private final float[] headView;

	private final float[] currentAsteroidModel;
	private List<Laser> lasers;
	private final float laserSpeed;
	private int lasersProgram;
	private long currentCooldown;
	private final float[] lasersCamera;
	private final float[] lasersView;
	private final float[] lasersMvp;

	private final GvrAudioEngine audioEngine;
	private final float[] headQuaternion;

    /**
     * Create a new Asteroids Renderer with the given Android Context.
     *
     * @param context the current Android context.
     */
	public AsteroidsRenderer(final Context context) {
		super(context, new AsteroidsProperties(context));
		final AsteroidsProperties config = (AsteroidsProperties) this.getProperties();
		this.cameraSpeed = config.getCameraSpeed() / 1000;
		this.cameraPosZ = getProperties().getCameraPositionZ();
		this.camera = new float[16];
		this.view = new float[16];
		this.modelView = new float[16];
		this.mvp = new float[16];
		this.crossPosition = new float[3];
		this.modelHud = new float[16];
		this.hudCamera = new float[16];
		this.headView = new float[16];
		this.currentAsteroidModel = new float[16];
		this.lasers = new ArrayList<>();
		this.laserSpeed = config.getLaserSpeed() / 1000;
		this.currentCooldown = 0L;
		this.lasersCamera = new float[16];
		this.lasersView = new float[16];
		this.lasersMvp = new float[16];
		this.audioEngine = new GvrAudioEngine(context, GvrAudioEngine.RenderingMode.BINAURAL_HIGH_QUALITY);
		this.headQuaternion = new float[4];


		this.field = new AsteroidField(
				config.getAsteroidsCount(), config.getMinAsteroidSize(), config.getMaxAsteroidSize(),
				config.getAsteroidMinX(), config.getAsteroidMinY(), config.getAsteroidMinZ(),
				config.getAsteroidMaxX(), config.getAsteroidMaxY(), config.getAsteroidMaxZ(),
				config.getAsteroidsColorR(), config.getAsteroidsColorG(), config.getAsteroidsColorB()
		);

		initializeHud(config);
	}

	@Override
	public void onSurfaceCreated(final EGLConfig config) {
		super.onSurfaceCreated(config);

		this.celestialProgram = OpenGLUtils.newProgram();
		final String vertexSources = readContentOf(R.raw.mvp_vertex);
		final String fragmentSources = readContentOf(R.raw.multicolor_fragment);
		final int vertexShader = OpenGLUtils.addVertexShaderToProgram(vertexSources, this.celestialProgram);
		final int fragmentShader = OpenGLUtils.addFragmentShaderToProgram(fragmentSources, this.celestialProgram);
		OpenGLUtils.linkProgram(this.celestialProgram, vertexShader, fragmentShader);

		this.hudProgram = OpenGLUtils.newProgram();
		final int hudVertexShader = OpenGLUtils.addVertexShaderToProgram(vertexSources, this.hudProgram);
		final int hudFragmentShader = OpenGLUtils.addFragmentShaderToProgram(fragmentSources, this.hudProgram);
		OpenGLUtils.linkProgram(this.hudProgram, hudVertexShader, hudFragmentShader);

		this.lasersProgram = OpenGLUtils.newProgram();
		final int lasersVertexShader = OpenGLUtils.addVertexShaderToProgram(vertexSources, this.lasersProgram);
		final int lasersFragmentShader = OpenGLUtils.addFragmentShaderToProgram(fragmentSources, this.lasersProgram);
		OpenGLUtils.linkProgram(this.lasersProgram, lasersVertexShader, lasersFragmentShader);
		Matrix.setLookAtM(lasersCamera, 0,
				getProperties().getCameraPositionX(), getProperties().getCameraPositionY(), getProperties().getCameraPositionZ(),
				getProperties().getCameraDirectionX(), getProperties().getCameraDirectionY(), getProperties().getCameraDirectionZ(),
				0, 1, 0);

        new Thread(new Runnable() {
                    @Override
                    public void run() {
                        audioEngine.preloadSoundFile(LASER_SOUNDFILE);
                        audioEngine.preloadSoundFile(EXPLOSION_SOUNDFILE);
                    }
                }).start();
	}

	@Override
	public void onNewFrame(final HeadTransform headTransform) {
		super.onNewFrame(headTransform);
		long time = SystemClock.uptimeMillis() % this.getTimeBetweenFrames();
		final float cameraDistance = this.cameraSpeed * time;
		final float laserDistance = this.laserSpeed * time;

		final float cameraPosX = getProperties().getCameraPositionX();
		final float cameraPosY = getProperties().getCameraPositionY();
		final float cameraDirX = getProperties().getCameraDirectionX();
		final float cameraDirY = getProperties().getCameraDirectionY();
		final float cameraDirZ = getProperties().getCameraDirectionZ();

		if (this.cameraPosZ < ((AsteroidsProperties) getProperties()).getDistanceToTravel()) {
			cameraPosZ += cameraDistance;
		}

		for (final Sphere asteroid : this.field.asteroids()) {
			if (isLookingAt(asteroid) && 0L >= this.currentCooldown) {
				fireAt(asteroid);
			}
		}

		for (final Laser laser : this.lasers) {
			laser.move(0,0,laserDistance);
		}

		this.currentCooldown -= time;

		checkCollisions();

		Matrix.setLookAtM(this.camera, 0,
				cameraPosX, cameraPosY, cameraPosZ,
				cameraPosX + cameraDirX, cameraPosY + cameraDirY, cameraPosZ + cameraDirZ,
				0, 1, 0);
		Matrix.setLookAtM(this.hudCamera, 0,
				cameraPosX, cameraPosY, getProperties().getCameraPositionZ(),
				cameraDirX, cameraDirY, cameraDirZ,
				0, 1, 0);

		headTransform.getHeadView(this.headView, 0);

		headTransform.getQuaternion(this.headQuaternion, 0);
		audioEngine.setHeadRotation(this.headQuaternion[0], this.headQuaternion[1],
				this.headQuaternion[2], this.headQuaternion[3]);
		audioEngine.update();
	}

	@Override
	public void onDrawEye(final Eye eye) {
		super.onDrawEye(eye);

		Matrix.multiplyMM(this.view, 0, eye.getEyeView(), 0, this.camera, 0);
		Matrix.multiplyMM(this.mvp, 0, eye.getPerspective(0.1f, 100f), 0, this.view, 0);
		OpenGLUtils.use(this.celestialProgram);
		OpenGLUtils.bindMVPToProgram(this.celestialProgram, this.mvp, "vMatrix");

		for (final Sphere asteroid : this.field.asteroids()) {
			final int verticesHandle = OpenGLUtils.bindVerticesToProgram(this.celestialProgram, this.field.vertices(asteroid), "vVertices");
			final int colorHandle = OpenGLUtils.bindColorToProgram(this.celestialProgram, this.field.colors(asteroid), "vColors");
			OpenGLUtils.drawTriangles(this.field.size(asteroid), verticesHandle, colorHandle);
		}
		displayLasers(eye);
		displayHud(eye);
	}


	public void pause() {
	    this.audioEngine.pause();
    }

    public void resume() {
	    this.audioEngine.resume();
    }

	private void initializeHud(final AsteroidsProperties config) {
		this.crossPosition[0] = 0f;
		this.crossPosition[1] = 0f;
		this.crossPosition[2] = 6f;
		final float crossSize = config.getCrossSize();
		this.crossVertices = createFloatBuffer(4 * DIMENSION * FLOAT_BYTES);
		this.crossVertices.put(this.crossPosition[0] - crossSize);
		this.crossVertices.put(this.crossPosition[1]);
		this.crossVertices.put(this.crossPosition[2]);
		this.crossVertices.put(this.crossPosition[0] + crossSize);
		this.crossVertices.put(this.crossPosition[1]);
		this.crossVertices.put(this.crossPosition[2]);
		this.crossVertices.put(this.crossPosition[0]);
		this.crossVertices.put(this.crossPosition[1] - crossSize);
		this.crossVertices.put(this.crossPosition[2]);
		this.crossVertices.put(this.crossPosition[0]);
		this.crossVertices.put(this.crossPosition[1] + crossSize);
		this.crossVertices.put(this.crossPosition[2]);
		this.crossVertices.position(0);
		this.crossColor = createFloatBuffer(4 * 4 * FLOAT_BYTES);
		final float[] color = OpenGLUtils.toOpenGLColor(
				config.getCrossColorR(),
				config.getCrossColorG(),
				config.getCrossColorB());
		for (int i=0; i < 4; i++) {
			this.crossColor.put(color);
		}
		this.crossColor.position(0);
		Matrix.setIdentityM(this.modelHud, 0);
		Matrix.translateM(this.modelHud, 0,
				this.crossPosition[0], this.crossPosition[1], this.crossPosition[2]);
	}

	private void displayHud(final Eye eye) {
		OpenGLUtils.use(this.hudProgram);
		final float[] symetry = new float[16];
		Matrix.setIdentityM(symetry, 0);
		symetry[0] *= -1;
		Matrix.multiplyMM(this.view, 0, eye.getEyeView(), 0, this.hudCamera, 0);
		final float[] staticModel = new float[16];
		Matrix.multiplyMM(staticModel, 0, this.headView, 0, this.modelHud, 0);
		Matrix.multiplyMM(staticModel, 0, symetry, 0, staticModel, 0);

		Matrix.multiplyMM(this.modelView, 0, this.view, 0, staticModel, 0);
		Matrix.multiplyMM(this.mvp, 0, eye.getPerspective(0.1f, 100f), 0, this.modelView, 0);

		OpenGLUtils.bindMVPToProgram(this.hudProgram, this.mvp, "vMatrix");
		final int verticesHandle = OpenGLUtils.bindVerticesToProgram(this.hudProgram, this.crossVertices, "vVertices");
		final int colorHandle = OpenGLUtils.bindColorToProgram(this.hudProgram, this.crossColor, "vColors");
		OpenGLUtils.drawLines(4, 14, verticesHandle, colorHandle);
	}

	private void displayLasers(final Eye eye) {
		float[] laserModel;
		final float[] laserModelView = new float[16];

		Matrix.multiplyMM(this.lasersView, 0, eye.getEyeView(), 0, this.lasersCamera, 0);
		OpenGLUtils.use(this.lasersProgram);

		final List<Laser> oldLasers = new ArrayList<>();
		for (final Laser laser : this.lasers) {
			laserModel = laser.model();
			if (laserModel[14] > 30) {
				oldLasers.add(laser);
				this.audioEngine.stopSound(laser.audio());
			} else {
				Matrix.multiplyMM(laserModelView, 0, this.lasersView, 0, laserModel, 0);
				Matrix.multiplyMM(this.lasersMvp, 0, eye.getPerspective(0.1f, 100f), 0, laserModelView, 0);
				OpenGLUtils.bindMVPToProgram(this.lasersProgram, this.lasersMvp, "vMatrix");
				final int verticesHandle = OpenGLUtils.bindVerticesToProgram(this.lasersProgram, laser.bufferize(), "vVertices");
				final int colorHandle = OpenGLUtils.bindColorToProgram(this.lasersProgram, laser.colors(), "vColors");
				OpenGLUtils.drawLines(laser.size(), 200, verticesHandle, colorHandle);
				this.audioEngine.setSoundObjectPosition(laser.audio(),
                        laserModel[12], laserModel[13], laserModel[14]);
			}
		}
		this.lasers.removeAll(oldLasers);
	}

	private boolean isLookingAt(final Sphere shape) {
		Matrix.setIdentityM(this.currentAsteroidModel, 0);

		final float[] t = new float[16];
		Matrix.setIdentityM(t, 0);

		final float dZ = shape.getPosition().z - this.cameraPosZ;
		Matrix.translateM(this.currentAsteroidModel, 0, shape.getPosition().x, shape.getPosition().y, dZ);

		Matrix.translateM(t, 0, shape.getRadius(), shape.getRadius(), dZ);

		final float[] modelView = new float[16];
		Matrix.multiplyMM(modelView, 0, this.headView, 0, this.currentAsteroidModel, 0);

		// shape Area
		final float shapePitchAreaRad = (float) Math.atan2(t[12], t[14]);
		final float shapeYawAreaRad = (float) Math.atan2(t[13], t[14]);
		// pitch of sight
		final float sightPitchRad = (float) Math.atan2(modelView[12], modelView[14]);
		// yaw of sight
		final float sightYawRad = (float) Math.atan2(modelView[13], modelView[14]);
		return Math.abs(sightPitchRad) < Math.abs(shapePitchAreaRad) && Math.abs(sightYawRad) < Math.abs(shapeYawAreaRad);
	}

	private void fireAt(final Sphere target) {
		final Coordinates start = new Coordinates(0, -1, this.cameraPosZ - 1);
		final Coordinates end = new Coordinates(0, -1, start.z + 0.3f);
		final Laser laser = new Laser(start, end);
		laser.color(253,106,2);

		// pitch ( polar angle)
		final float realXPos = target.getPosition().x;
		final float realZPos = target.getPosition().z - this.cameraPosZ;
		final float angleInRadian = (float) Math.atan2(realXPos,realZPos);
		laser.pitch((float)Math.toDegrees(angleInRadian));

		// yaw
		final float dY = target.getPosition().y - end.y;
		final float dZ = realZPos - end.z;
		final float yawInRadian = (float) Math.atan2(dY,dZ);
		laser.yaw((float) Math.toDegrees(yawInRadian) + 180);

		final int laserAudioId = this.audioEngine.createSoundObject(LASER_SOUNDFILE);
        laser.audio(laserAudioId);
        this.audioEngine.setSoundObjectPosition(laserAudioId, end.x, end.y, end.z);
        this.audioEngine.playSound(laserAudioId, true);

		lasers.add(laser);
		this.currentCooldown = ((AsteroidsProperties) getProperties()).getLaserCoolDown();
	}

	private void checkCollisions() {
		final List<Laser> lasersToRemove = new ArrayList<>();
		for (final Laser laser : this.lasers) {
			final List<Sphere> asteroidsToRemove = new ArrayList<>();
			for (final Sphere asteroid : this.field.asteroids()) {
				if (collides(laser, asteroid)) {
					lasersToRemove.add(laser);
					this.audioEngine.stopSound(laser.audio());
					asteroidsToRemove.add(asteroid);
					final int explosionSound = this.audioEngine.createSoundObject(EXPLOSION_SOUNDFILE);
					this.audioEngine.setSoundObjectPosition(explosionSound,
							asteroid.getPosition().x, asteroid.getPosition().y, asteroid.getPosition().z);
					this.audioEngine.playSound(explosionSound, false);
					break;
				}
			}
			this.field.removeAll(asteroidsToRemove);
		}
		this.lasers.removeAll(lasersToRemove);
	}

	private boolean collides(final Laser laser, final Sphere shape) {
		final float shapeX = shape.getPosition().x;
		final float shapeY = shape.getPosition().y;
		final float shapeZ = shape.getPosition().z - this.cameraPosZ;
		final float rad = shape.getRadius();
		final float[] laserModel = laser.model();
		boolean result = shapeX - rad < laserModel[12] && laserModel[12] <= shapeX + rad;
		result = result && shapeY - rad < laserModel[13] && laserModel[13] <= shapeY + rad;
		result = result && shapeZ - rad <= laserModel[14]; // Lasers are removed after hit, so we don't need to check if the laser is not in the asteroids back.
		//return result;
		return false;
	}
}
