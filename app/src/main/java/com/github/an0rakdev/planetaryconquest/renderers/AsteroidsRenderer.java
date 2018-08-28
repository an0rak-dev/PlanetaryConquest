package com.github.an0rakdev.planetaryconquest.renderers;

import android.content.Context;
import android.opengl.Matrix;
import android.os.SystemClock;

import com.github.an0rakdev.planetaryconquest.R;
import com.github.an0rakdev.planetaryconquest.MathUtils;
import com.github.an0rakdev.planetaryconquest.OpenGLUtils;
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
	private final List<Sphere> field;
	private final float asteroidsSpeed;
	private float asteroidMvt;
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
		this.asteroidMvt = 0f;
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

		this.asteroidMvt += asteroidsDistance;
		this.cameraPosX += asteroidsDistance;
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

		final List<Sphere> destroyed = new ArrayList<>();
		for (final Sphere asteroid : this.field) {
			if (isLookingAt(asteroid)) {
	//			destroyed.add(asteroid);
			} else {
				final int verticesHandle = OpenGLUtils.bindVerticesToProgram(this.celestialProgram, asteroid.bufferize(), "vVertices");
				final int colorHandle = OpenGLUtils.bindColorToProgram(this.celestialProgram, asteroid.colors(), "vColors");
				OpenGLUtils.drawTriangles(asteroid.size(), verticesHandle, colorHandle);
			}
		}
		this.field.removeAll(destroyed);

		displayHud(eye);
	}

	private void initializeAsteroidField(final AsteroidsProperties config) {
		final int asteroidsCount = config.getAsteroidsCount();
		final float[] asteroidColor = OpenGLUtils.toOpenGLColor(
				config.getAsteroidsColorR(), config.getAsteroidsColorG(), config.getAsteroidsColorB()
		);
		//*
		final float minSize = config.getMinAsteroidSize();
		final float maxSize = config.getMaxAsteroidSize();
		final float minX = config.getAsteroidMinX();
		final float minY = config.getAsteroidMinY();
		final float minZ = config.getAsteroidMinZ();
		final float maxX = config.getAsteroidMaxX();
		final float maxY = config.getAsteroidMaxY();
		final float maxZ = config.getAsteroidMaxZ();

		while (this.field.size() < asteroidsCount) {
			final float radius = MathUtils.randRange(minSize, maxSize);
			final Coordinates center = new Coordinates(
				MathUtils.randRange(minX, maxX),
				MathUtils.randRange(minY, maxY),
				MathUtils.randRange(minZ, maxZ));
			final Sphere asteroid = new Sphere(center, radius);
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

	private boolean isLookingAt(final Sphere shape) {
		final float[] asteroidModel = new float[16];
		Matrix.setIdentityM(asteroidModel, 0);
		//FIXME(SNI) : Substract because a bug cause the X-axis to be revert.
		final float realXPos = shape.getPosition().x - this.asteroidMvt;
		Matrix.translateM(asteroidModel, 0, realXPos, shape.getPosition().y, shape.getPosition().z);
		final float[] modelView = new float[16];
		Matrix.multiplyMM(modelView, 0, this.headView, 0, asteroidModel, 0);

		final float[] position = new float[4];
		Matrix.multiplyMV(position,0, modelView, 0, new float[] {0f,0f,0f,1f}, 0);

		final float[] forwardVec = new float[] {0f,0f,1f,1f};
		final float angle = MathUtils.angleBetween(position, forwardVec);
		final float targetPerimeter = MathUtils.angleBetween(
				new float[] {realXPos, shape.getPosition().y, shape.getPosition().z, 1},
				new float[] {realXPos + shape.getRadius(), shape.getPosition().y, shape.getPosition().z, 1}
		);
		return angle <= targetPerimeter;
	}
}
