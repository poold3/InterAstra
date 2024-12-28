package io.github.interastra.services;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import io.github.interastra.models.CameraEnabledEntity;

import java.util.logging.Level;

/**
 * Hi! I'm a camera operator! Just tell me where to film by setting my targetPosition and targetZoom properties.
 * Every time my move() function is called, I slowly move towards the targetPosition and targetZoom settings.
 */
public class CameraOperatorService extends OrthographicCamera {
    public static final float MOVE_SPEED = 0.1f;
    public static final float ZOOM_SPEED = 0.05f;

    private ExtendViewport viewport;
    public Vector3 targetPosition;
    public float targetZoom;

    public CameraOperatorService() {
        super();
        this.targetPosition = new Vector3();
        this.targetZoom = 1f;
    }

    public void setViewport(final ExtendViewport viewport) {
        this.viewport = viewport;
    }

    public void move() {
        try {
            // Adjust zoom
            this.zoom = floatLerp(this.zoom, this.targetZoom, ZOOM_SPEED);

            // Adjust position
            this.position.x = floatLerp(this.position.x, this.targetPosition.x, MOVE_SPEED);

            this.position.y = floatLerp(this.position.y, this.targetPosition.y, MOVE_SPEED);

            this.update();
        } catch (Exception e) {
            InterAstraLog.logger.log(Level.SEVERE, e.getMessage(), e);
        }
    }

    public void reset() {
        this.targetZoom = 1f;
        this.center();
    }

    public void center() {
        this.targetPosition.set(this.viewport.getWorldWidth() / 2f, this.viewport.getWorldHeight() / 2f, 0f);
    }

    public void followCameraEnabledEntity(final CameraEnabledEntity entity) {
        this.targetPosition.set(entity.getX(), entity.getY(), 0f);
    }

    public void forceCameraPosition() {
        this.position.x = this.targetPosition.x;
        this.position.y = this.targetPosition.y;
        this.position.z = this.targetPosition.z;
        this.update();
    }

    /**
     * Gets the zoom value for this camera to ensure that the provided dimension value is visible in both the x and y
     * directions.
     * @param dimension A value in world units
     * @return The camera zoom value needed to accommodate the dimension
     */
    public float getZoomForSize(float dimension) {
        float widthZoomValue = (1f / this.viewport.getWorldWidth()) * dimension;
        float heightZoomValue = (1f / this.viewport.getWorldHeight()) * dimension;
        return Math.max(widthZoomValue, heightZoomValue);
    }

    public static float floatLerp(float current, float target, float epsilon) {
        return (1f - epsilon) * current + epsilon * target;
    }

    public static float floatLerp(float current, float target) {
        return floatLerp(current, target, 0.5f);
    }
}
