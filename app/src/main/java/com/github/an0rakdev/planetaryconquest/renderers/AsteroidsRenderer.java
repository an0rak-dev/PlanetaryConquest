package com.github.an0rakdev.planetaryconquest.renderers;

import android.content.Context;
import android.opengl.Matrix;
import android.os.SystemClock;

import com.github.an0rakdev.planetaryconquest.R;
import com.github.an0rakdev.planetaryconquest.RandomUtils;
import com.github.an0rakdev.planetaryconquest.OpenGLUtils;
import com.github.an0rakdev.planetaryconquest.graphics.models.polyhedrons.Polyhedron;
import com.github.an0rakdev.planetaryconquest.graphics.models.polyhedrons.Sphere;
import com.github.an0rakdev.planetaryconquest.graphics.models.Coordinates;
import com.google.vr.sdk.base.Eye;
import com.google.vr.sdk.base.HeadTransform;

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
	private final List<Polyhedron> field;
	private final float asteroidsSpeed;
	private final float cameraSpeed;
	private float distanceElapsed;

	private int celestialProgram;
	private float cameraPosX;
	private float cameraPosZ;
	private final float[] camera;
	private final float[] view;
	private final float[] model;
	private final float[] modelView;
	private final float[] mvp;

    /**
     * Create a new Asteroids Renderer with the given Android Context.
     *
     * @param context the current Android context.
     */
	public AsteroidsRenderer(final Context context) {
		super(context, new AsteroidsProperties(context));
		final AsteroidsProperties config = (AsteroidsProperties) this.getProperties();
		this.field = new ArrayList<>();
		this.asteroidsSpeed = config.getAsteroidsSpeed() / 1000;
		this.cameraSpeed = config.getCameraSpeed() / 1000;
		this.distanceElapsed = config.getDistanceToTravel();
		this.cameraPosX = getProperties().getCameraPositionX();
		this.cameraPosZ = getProperties().getCameraPositionZ();
		this.camera = new float[16];
		this.view = new float[16];
		this.model = new float[16];
		this.modelView = new float[16];
		this.mvp = new float[16];
		initializeAsteroidField(config);
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
	}

	@Override
	public void onNewFrame(final HeadTransform headTransform) {
		super.onNewFrame(headTransform);
		long time = SystemClock.uptimeMillis() % this.getTimeBetweenFrames();
		float asteroidsDistance = this.asteroidsSpeed * time;
		final float cameraDistance = this.cameraSpeed * time;

		final float cameraPosY = getProperties().getCameraPositionY();
		final float cameraDirX = getProperties().getCameraDirectionX();
		final float cameraDirY = getProperties().getCameraDirectionY();
		final float cameraDirZ = getProperties().getCameraDirectionZ();

		cameraPosX += asteroidsDistance;
		if (distanceElapsed >0f) {
			cameraPosZ += cameraDistance;
			distanceElapsed -= cameraDistance;
		}

		Matrix.setLookAtM(this.camera, 0,
				cameraPosX, cameraPosY, cameraPosZ,
				cameraPosX + cameraDirX, cameraPosY + cameraDirY, cameraPosZ + cameraDirZ,
				0, 1, 0);
		Matrix.setIdentityM(this.model, 0);
	}

	@Override
	public void onDrawEye(final Eye eye) {
		super.onDrawEye(eye);

		Matrix.multiplyMM(this.view, 0, eye.getEyeView(), 0, this.camera, 0);
		Matrix.multiplyMM(this.modelView, 0, this.view, 0, this.model, 0);
		Matrix.multiplyMM(this.mvp, 0, eye.getPerspective(0.1f, 100f), 0, this.view, 0);
		OpenGLUtils.use(this.celestialProgram);
		OpenGLUtils.bindMVPToProgram(this.celestialProgram, this.mvp, "vMatrix");

		for (final Polyhedron asteroid : this.field) {
			final int verticesHandle = OpenGLUtils.bindVerticesToProgram(this.celestialProgram, asteroid.bufferize(), "vVertices");
			final int colorHandle = OpenGLUtils.bindColorToProgram(this.celestialProgram, asteroid.colors(), "vColors");
			OpenGLUtils.drawTriangles(asteroid.size(), verticesHandle, colorHandle);
		}
	}

	private void initializeAsteroidField(final AsteroidsProperties config) {
		final int asteroidsCount = config.getAsteroidsCount();
		final float[] asteroidColor = OpenGLUtils.toOpenGLColor(
				config.getAsteroidsColorR(), config.getAsteroidsColorG(), config.getAsteroidsColorB()
		);
		final float minSize = config.getMinAsteroidSize();
		final float maxSize = config.getMaxAsteroidSize();
		final float minX = config.getAsteroidMinX();
		final float minY = config.getAsteroidMinY();
		final float minZ = config.getAsteroidMinZ();
		final float maxX = config.getAsteroidMaxX();
		final float maxY = config.getAsteroidMaxY();
		final float maxZ = config.getAsteroidMaxZ();

		while (this.field.size() < asteroidsCount) {
			final float radius = RandomUtils.randRange(minSize, maxSize);
			final Coordinates center = new Coordinates(
				RandomUtils.randRange(minX, maxX),
				RandomUtils.randRange(minY, maxY),
				RandomUtils.randRange(minZ, maxZ));
			final Polyhedron asteroid = new Sphere(center, radius);
			asteroid.precision(1);
			asteroid.background(asteroidColor);
			this.field.add(asteroid);
		}
	}
}
