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
	private final List<Polyhedron> field;
	private final float asteroidsSpeed;
	private final float cameraSpeed;
	private float distanceElapsed;

	private int celestialProgram;
	private float cameraPosX;
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
		this.modelView = new float[16];
		this.mvp = new float[16];
		this.crossPosition = new float[3];
		this.modelHud = new float[16];
		this.hudCamera = new float[16];
		this.headView = new float[16];
		initializeAsteroidField(config);
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

	//	cameraPosX += asteroidsDistance;
		if (distanceElapsed >0f) {
	//		cameraPosZ += cameraDistance;
			distanceElapsed -= cameraDistance;
		}

		Matrix.setLookAtM(this.camera, 0,
				cameraPosX, cameraPosY, cameraPosZ,
				cameraPosX + cameraDirX, cameraPosY + cameraDirY, cameraPosZ + cameraDirZ,
				0, 1, 0);
		Matrix.setLookAtM(this.hudCamera, 0,
				getProperties().getCameraPositionX(), cameraPosY, getProperties().getCameraPositionZ(),
				cameraDirX, cameraDirY, cameraDirZ,
				0, 1, 0);

		headTransform.getHeadView(this.headView, 0);
	}

	@Override
	public void onDrawEye(final Eye eye) {
		super.onDrawEye(eye);

		Matrix.multiplyMM(this.view, 0, eye.getEyeView(), 0, this.camera, 0);
		Matrix.multiplyMM(this.mvp, 0, eye.getPerspective(0.1f, 100f), 0, this.view, 0);
		OpenGLUtils.use(this.celestialProgram);
		OpenGLUtils.bindMVPToProgram(this.celestialProgram, this.mvp, "vMatrix");

		for (final Polyhedron asteroid : this.field) {
			final int verticesHandle = OpenGLUtils.bindVerticesToProgram(this.celestialProgram, asteroid.bufferize(), "vVertices");
			final int colorHandle = OpenGLUtils.bindColorToProgram(this.celestialProgram, asteroid.colors(), "vColors");
			OpenGLUtils.drawTriangles(asteroid.size(), verticesHandle, colorHandle);
		}

		OpenGLUtils.use(this.hudProgram);
		Matrix.multiplyMM(this.view, 0, eye.getEyeView(), 0, this.hudCamera, 0);
		final float[] staticModel = new float[16];
		// FIXME invert the rotation on the Y axis.
		Matrix.multiplyMM(staticModel, 0, this.headView, 0, this.modelHud, 0);
		Matrix.multiplyMM(this.modelView, 0, this.view, 0, staticModel, 0);
		Matrix.multiplyMM(this.mvp, 0, eye.getPerspective(0.1f, 100f), 0, this.modelView, 0);

		OpenGLUtils.bindMVPToProgram(this.hudProgram, this.mvp, "vMatrix");
		final int verticesHandle = OpenGLUtils.bindVerticesToProgram(this.hudProgram, this.crossVertices, "vVertices");
		final int colorHandle = OpenGLUtils.bindColorToProgram(this.hudProgram, this.crossColor, "vColors");
		OpenGLUtils.drawLines(4, 14, verticesHandle, colorHandle);
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
}
