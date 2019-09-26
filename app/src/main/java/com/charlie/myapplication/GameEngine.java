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

    Rect playerHitbox;
    Rect enemyHitbox;
    Rect enemyHitbox2;


    Bitmap playerImage;


    Bitmap enemyImage2;

    int enemyXPosition2;
    int enemyYPosition2;


    // -----------------------------------
    // GAME SPECIFIC VARIABLES
    // -----------------------------------

    // ----------------------------
    // ## SPRITES
    // ----------------------------
    int playerX;
    int playerY;

    Bitmap enemyImage;
    int enemyXPosition;
    int enemyYPosition;

    String fingerAction = "";

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

       this.playerImage =  BitmapFactory.decodeResource(this.getContext().getResources(),
                R.drawable.player_ship);
        this.printScreenInfo();

        // @TODO: Add your sprites


        this.playerX = 100;
        this.playerY = 120;

        this.enemyXPosition = 500;
        this.enemyYPosition =  120;


        this.enemyXPosition2 = 500;
        this.enemyYPosition2 =  220;
        // @TODO: Any other game setup

        this.enemyImage  = BitmapFactory.decodeResource(this.getContext().getResources(),
                R.drawable.alien_ship2);

        this.enemyImage2  = BitmapFactory.decodeResource(this.getContext().getResources(),
                R.drawable.alien_ship3);


        this.enemyHitbox = new Rect(500,120, 500 +enemyImage.getWidth(), 120+enemyImage.getHeight() );

        this.playerHitbox = new Rect(100,120, 100+ playerImage.getWidth(),120 +  playerImage.getHeight());




        this.enemyHitbox2 = new Rect(500,120, 500 +enemyImage2.getWidth(), 120+enemyImage2.getHeight() );



    }


    private void printScreenInfo() {

        Log.d(TAG, "Screen (w, h) = " + this.screenWidth + "," + this.screenHeight);
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


        if(this.fingerAction == "mousedown"){

            this.playerY = this.playerY - 60;

            this.playerHitbox.left = this.playerX;
            this.playerHitbox.top = this.playerY;
            this.playerHitbox.right = this.playerX + this.playerImage.getWidth();
            this.playerHitbox.bottom = this.playerY +this.playerImage.getHeight();


        }
        if(this.fingerAction == "mouseup"){
            this.playerY = this.playerY + 10;


            this.playerHitbox.left = this.playerX;
            this.playerHitbox.top = this.playerY;
            this.playerHitbox.right = this.playerX + this.playerImage.getWidth();
            this.playerHitbox.bottom = this.playerY + this.playerImage.getHeight();

        }


        // MAKE THE ENEMY MOVE

        this.enemyXPosition = this.enemyXPosition - 5;

        // Move the hitbox
        this.enemyHitbox.left = this.enemyXPosition;
        this.enemyHitbox.top = this.enemyYPosition;
        this.enemyHitbox.right = this.enemyXPosition + this.enemyImage.getWidth();
        this.enemyHitbox.bottom = this.enemyYPosition + this.enemyImage.getHeight();



        this.enemyXPosition2 = this.enemyXPosition2 - 5;

        // Move the hitbox
        this.enemyHitbox2.left = this.enemyXPosition2;
        this.enemyHitbox2.top = this.enemyYPosition2;
        this.enemyHitbox2.right = this.enemyXPosition2 + this.enemyImage2.getWidth();
        this.enemyHitbox2.bottom = this.enemyYPosition2 + this.enemyImage2.getHeight();

        if(this.enemyXPosition <= 0){
            this.enemyXPosition = 800;
            this.enemyYPosition = 100;




            this.enemyHitbox.left = this.enemyXPosition;
            this.enemyHitbox.top = this.enemyYPosition;
            this.enemyHitbox.right = this.enemyXPosition + this.enemyImage.getWidth();
            this.enemyHitbox.bottom = this.enemyYPosition + this.enemyImage.getHeight();





        }

        if(this.enemyXPosition2 <= 0){
            this.enemyXPosition2 = 800;
            this.enemyYPosition2 = 220;




            this.enemyHitbox2.left = this.enemyXPosition2;
            this.enemyHitbox2.top = this.enemyYPosition2;
            this.enemyHitbox2.right = this.enemyXPosition2 + this.enemyImage2.getWidth();
            this.enemyHitbox2.bottom = this.enemyYPosition2 + this.enemyImage2.getHeight();





        }


        // Check enemy colision between enemy and player

        if (this.playerHitbox.intersect(this.enemyHitbox)){
            Log.d(TAG,"Enemy Colliding with the player");


            // RESTART THE PLAYER
            this.playerX = 100;
            this.playerY = 120;
            this.playerHitbox = new Rect(100,120, 100+ playerImage.getWidth(),120 +  playerImage.getHeight());


            lives = lives-1;


        }


    }

    public void redrawSprites() {
        if (this.holder.getSurface().isValid()) {
            this.canvas = this.holder.lockCanvas();

            //----------------

            // configure the drawing tools
            this.canvas.drawColor(Color.argb(255,255,255,255));
            paintbrush.setColor(Color.BLUE);
            paintbrush.setStyle(Paint.Style.STROKE);
            paintbrush.setStrokeWidth(5);

            //@TODO: Draw the player



            playerImage.getWidth();
            playerImage.getHeight();

            canvas.drawBitmap(playerImage, playerX, playerY, paintbrush);

            //drw the player's hitbox


            canvas.drawRect(this.playerHitbox, paintbrush);

            //@TODO: Draw the enemy



            canvas.drawBitmap(enemyImage, enemyXPosition, enemyYPosition, paintbrush);

            canvas.drawRect(this.enemyHitbox, paintbrush);
            // Show the hitboxes on player and enemy

            canvas.drawBitmap(enemyImage2, enemyXPosition2, enemyYPosition2, paintbrush);

            canvas.drawRect(this.enemyHitbox2, paintbrush);


            // lives

            paintbrush.setColor(Color.BLACK);
            paintbrush.setTextSize(40);
            paintbrush.setStrokeWidth(2);
            canvas.drawText("Lives remaining: " +lives,220,90,paintbrush);

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


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int userAction = event.getActionMasked();
        //@TODO: What should happen when person touches the screen?
        if (userAction == MotionEvent.ACTION_DOWN) {
            // move player up
            fingerAction = "mousedown";

            Log.d(TAG,"Player tapped the screen");
        }
        else if (userAction == MotionEvent.ACTION_UP) {
            // move player down
            fingerAction = "mouseup";
            Log.d(TAG,"Person lifted finger");
        }

        return true;
    }
}
