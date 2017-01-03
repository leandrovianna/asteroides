package com.allg.asteroides.game.levels;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.view.MotionEvent;

import com.allg.asteroides.engine.Collision;
import com.allg.asteroides.engine.ControllerInterface;
import com.allg.asteroides.engine.GameManager;
import com.allg.asteroides.game.objects.Asteroid;
import com.allg.asteroides.game.objects.AsteroidManager;
import com.allg.asteroides.game.objects.Score;
import com.allg.asteroides.game.objects.Shot;
import com.allg.asteroides.game.objects.SpaceShip;
import com.allg.asteroides.game.objects.TextCenter;
import com.allg.asteroides.game.objects.abstracts.Background;
import com.allg.asteroides.game.objects.abstracts.Music;
import com.allg.asteroides.util.SpaceShipControl;

public class AsteroidesLevelController implements ControllerInterface {

    private SpaceShip ship;
    private SpaceShipControl shipControl;
    private Score score;

    private Background background;

    private AsteroidManager asteroidManager;

    private Music music;

    private LevelManager levelManager;

    private boolean playerWin = false;

    private TextCenter textCenter;

    public AsteroidesLevelController(Context context, SpaceShip ship, Background background,
                                     Music music, int asteroidesNumber, int velocity,
                                     LevelManager levelManager) {

        super();
        this.ship = ship;
        this.shipControl = new SpaceShipControl(context, ship);

        this.background = background;

        this.score = new Score(context, ship);

        this.asteroidManager = new AsteroidManager(context, asteroidesNumber, velocity);

        this.music = music;

        this.levelManager = levelManager;

        this.textCenter = new TextCenter(context, "Você Venceu!", 60, Color.YELLOW);
    }

    @Override
    public void initObjects(Canvas canvas) {
        ship.initObject(canvas);
        shipControl.startControl();
        background.initObject(canvas);
        score.initObject(canvas);
        asteroidManager.initObject(canvas);
        music.initObject(canvas);
        textCenter.initObject(canvas);
    }

    @Override
    public void stepObjects(Canvas canvas) {
        ship.step(canvas);
        score.step(canvas);
        background.step(canvas);
        asteroidManager.step(canvas);

        for (Asteroid a : asteroidManager.getAsteroids()) {
            if (Collision.isCollided(a, ship) && !a.isExploded()) {
                ship.explodir();
                levelManager.setGameState(GameManager.State.END);
                playerWin = false;
            }

            try {
                for (Shot shot : ship.getShots()) {
                    if (Collision.isCollided(shot, a)) {
                        ship.delShots(shot);
                        a.explodir();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        if (asteroidManager.isAllAsteroidesCreated() && !ship.isExploded()) {
            playerWin = true;
            levelManager.setGameState(GameManager.State.END);
        }

    }

    @Override
    public void drawObjects(Canvas canvas) {
        background.draw(canvas);
        ship.draw(canvas);
        score.draw(canvas);
        asteroidManager.draw(canvas);

    }

    @Override
    public void stepObjectsFinal(Canvas canvas) {
        ship.step(canvas);
    }

    @Override
    public void drawObjectsFinal(Canvas canvas) {
        background.draw(canvas) ;
        score.draw(canvas);
        ship.draw(canvas);

        if (playerWin) {
            textCenter.draw(canvas);
            score.saveCacheScore();
        } else {
            score.clearCacheScore();
        }
    }

    @Override
    public void unloadObjects() {
        music.stopMusic();
    }

    @Override
    public void touchEvent(MotionEvent event) {
        if (levelManager.getGameState() == GameManager.State.END && playerWin) {
            levelManager.levelFinish();
        } else {
            ship.disparar();
        }
    }
}