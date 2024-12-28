package io.github.interastra.stages;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.Viewport;
import io.github.interastra.screens.GameScreen;
import io.github.interastra.services.InterAstraLog;

import java.util.logging.Level;

public class GameStage extends Stage {
    public GameScreen screen;
    public Vector3 touchDownMousePosition;

    public GameStage(Viewport viewport, GameScreen screen) {
        super(viewport);
        this.screen = screen;
        this.touchDownMousePosition = new Vector3();
    }

    @Override
    public boolean touchDown (int screenX, int screenY, int pointer, int button) {
        boolean result = super.touchDown(screenX, screenY, pointer, button);
        try {
            if (!result) {
                this.touchDownMousePosition.x = screenX;
                this.touchDownMousePosition.y = screenY;
                this.touchDownMousePosition.z = 0;
                this.screen.gameViewport.unproject(this.touchDownMousePosition);
            }
        } catch (Exception e) {
            InterAstraLog.logger.log(Level.SEVERE, e.getMessage(), e);
        }
        return result;
    }

    @Override
    public boolean touchDragged (int screenX, int screenY, int pointer) {
        boolean result = super.touchDragged(screenX, screenY, pointer);
        try {
            if (!result) {
                if (this.screen.entityBeingFollowed != null) {
                    if (this.screen.planetDashboardButtonTable.isVisible) {
                        this.screen.removePlanetDashboardButton();
                    }
                    this.screen.entityBeingFollowed = null;
                }

                Vector3 mousePosition = new Vector3(screenX, screenY, 0);
                this.screen.gameViewport.unproject(mousePosition);
                Vector3 diff = mousePosition.sub(this.touchDownMousePosition);
                this.screen.camera.targetPosition.sub(diff);
                this.screen.camera.targetPosition.x = MathUtils.clamp(
                    this.screen.camera.targetPosition.x,
                    0f,
                    this.screen.gameViewport.getWorldWidth()
                );
                this.screen.camera.targetPosition.y = MathUtils.clamp(
                    this.screen.camera.targetPosition.y,
                    0f,
                    this.screen.gameViewport.getWorldHeight()
                );
                this.screen.camera.forceCameraPosition();
            }
        } catch (Exception e) {
            InterAstraLog.logger.log(Level.SEVERE, e.getMessage(), e);
        }
        return result;
    }

    @Override
    public boolean scrolled (float amountX, float amountY) {
        boolean result = super.scrolled(amountX, amountY);
        try {
            if (!result) {
                this.screen.camera.targetZoom += amountY * 0.1f * this.screen.camera.zoom;
                this.screen.camera.targetZoom = MathUtils.clamp(this.screen.camera.targetZoom, 0.001f, 1f);
            }
        } catch (Exception e) {
            InterAstraLog.logger.log(Level.SEVERE, e.getMessage(), e);
        }
        return result;
    }
}
