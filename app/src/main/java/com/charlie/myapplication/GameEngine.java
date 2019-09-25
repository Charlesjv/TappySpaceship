package com.charlie.myapplication;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;

import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.Random;

public class GameEngine extends SurfaceView implements Runnable {

    // Android debug variables
    final static String TAG="TAPPY-SPACESHIP";

    // screen size
    int screenHeight;
    int screenWidth;

    // game state
    boolean gameIsRunning;

    // threading
    Thread gameThread;


    // drawing variables
    SurfaceHolder holder;
    Canvas canvas;
    Paint paintbrush;



    // -----------------------------------
    // GAME SPECIFIC VARIABLES
    // -----------------------------------

    // ----------------------------
    // ## SPRITES
    // ----------------------------
    Player player;
    Enemy enemy;

    // ----------------------------
    // ## GAME STATS
    // ----------------------------
    int score = 0;
    int lives = 5;

    public GameEngine(Context context, int w, int h) {
        super(context);


        this.holder = this.getHolder();
        this.paintbrush = new Paint();

        this.screenWidth = w;
        this.screenHeight = h;


        this.printScreenInfo();

        // @TODO: Add your sprites
        this.spawnPlayer();
        this.spawnEnemyShips();
        // @TODO: Any other game setup

    }


    private void printScreenInfo() {

        Log.d(TAG, "Screen (w, h) = " + this.screenWidth + "," + this.screenHeight);
    }

    private void spawnPlayer() {
        // put player in middle of screen --> you may have to adjust the Y position
        // depending on your device / emulator
        player = new Player(this.getContext(), 100, (this.screenHeight-300) / 2);

    }
    private void spawnEnemyShips() {
        Random random = new Random();

        //@TODO: Place the enemies in a random location
        enemy = new Enemy(this.getContext(), 1400, 100);
    }

    // ------------------------------
    // GAME STATE FUNCTIONS (run, stop, start)
    // ------------------------------
    @Override
    public void run() {
        while (gameIsRunning == true) {
            this.updatePositions();
            this.redrawSprites();
            this.setFPS();
        }
    }


    public void pauseGame() {
        gameIsRunning = false;
        try {
            gameThread.join();
        } catch (InterruptedException e) {
            // Error
        }
    }

    public void startGame() {
        gameIsRunning = true;
        gameThread = new Thread(this);
        gameThread.start();
    }


    // ------------------------------
    // GAME ENGINE FUNCTIONS
    // - update, draw, setFPS
    // ------------------------------

    public void updatePositions() {
        // @TODO: Update position of player
        player.updatePlayerPosition();
        // @TODO: Update position of enemy ships
        enemy.updateEnemyPosition();

        // @TODO: Collision detection between enemy and wall
        if (enemy.getXPosition() <= 0) {

            //reset the enemy's starting position to right side of screen
            // you may need to adjust this number according to your device/emulator
            enemy.setXPosition(this.screenWidth);
        }

        // @TODO: Collision detection between player and enemy
        if (player.getHitbox().intersect(enemy.getHitbox())) {
            // reduce lives
            lives--;
            // reset player to original position
            player.setXPosition(100);
            player.setYPosition((this.screenHeight-300) / 2);
        }
    }

    public void redrawSprites() {
        if (this.holder.getSurface().isValid()) {
            this.canvas = this.holder.lockCanvas();

            //----------------

            // configure the drawing tools
            this.canvas.drawColor(Color.argb(255,255,255,255));
            paintbrush.setColor(Color.WHITE);


            //@TODO: Draw the player

            Bitmap playerImage = BitmapFactory.decodeResource(this.getContext().getResources(),
                    R.drawable.player_ship);

            canvas.drawBitmap(playerImage, 100, 120, paintbrush);

            //@TODO: Draw the enemy

            Bitmap enemyImage = BitmapFactory.decodeResource(this.getContext().getResources(),
                    R.drawable.alien_ship2);

            canvas.drawBitmap(enemyImage, 500, 120, paintbrush);


            // Show the hitboxes on player and enemy


            //----------------
            this.holder.unlockCanvasAndPost(canvas);
        }
    }

    public void setFPS() {
        try {
            gameThread.sleep(50);
        }
        catch (Exception e) {

        }
    }

    // ------------------------------
    // USER INPUT FUNCTIONS
    // ------------------------------

    String fingerAction = "";
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int userAction = event.getActionMasked();
        //@TODO: What should happen when person touches the screen?
        if (userAction == MotionEvent.ACTION_DOWN) {
            // move player up
            fingerAction = "mouse down";

            Log.d(TAG,"Player tapped the screen");
        }
        else if (userAction == MotionEvent.ACTION_UP) {
            // move player down
            fingerAction = "Player lifted the finger";
            Log.d(TAG,"Person lifted finger");
        }

        return true;
    }
}
