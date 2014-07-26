package lab3.log530.com.lab3.gui;

import android.content.Context;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.Picture;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.applantation.android.svg.SVG;
import com.applantation.android.svg.SVGParser;

import lab3.log530.com.lab3.App;
import lab3.log530.com.lab3.Board;
import lab3.log530.com.lab3.GameEvent;
import lab3.log530.com.lab3.GameListener;
import lab3.log530.com.lab3.Player;
import lab3.log530.com.lab3.Position;
import lab3.log530.com.lab3.MoveList;
import lab3.log530.com.lab3.Piece;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.concurrent.CountDownLatch;
import lab3.log530.com.lab3.Move;
import android.graphics.drawable.shapes.RoundRectShape;
import android.view.View;
import lab3.log530.com.lab3.R;

public class BoardView extends SurfaceView implements View.OnTouchListener, SurfaceHolder.Callback, Player, GameListener {

    private int nbCasesHeight = 8;
    private int nbCasesWidth = 8;

    private int tileSize = 0;

    private static final String LOG_TAG = "BoardView";
    /** Size of a tile in working coordinates. */
    private static final double TILE_SIZE = 200.0;

   // private static final Shape

    /** The board being displayed. */
    private Board board;

    /** Indicate flipped status. */
    private boolean flipped = true;

    /** The currently selected tile. */
    private Position selected = null;

    /** The list of moves for the selected tile. */
    private MoveList moves = null;

    /** The color for the dark tiles on the board. */
    static final int DARK = Color.rgb(209, 139, 71);

    /** The color for the light tiles on the board. */
    static final int LIGHT = Color.rgb(255, 206, 158);

    /** Border color for a selected tile. */
    static final int SELECTED = Color.rgb(0, 255, 255);

    /** Border color for a highlighted movement tile. */
    static final int MOVEMENT = Color.rgb(127, 0, 0);

    /** Last move highlight color. */
    static final int LAST = Color.rgb(0, 127, 0);

    /** Minimum size of a tile, in pixels. */
    static final int MIN_SIZE = 25;

    /** Preferred size of a tile, in pixels. */
    static final int PREF_SIZE = 75;

    /** The current interaction mode. */
    private Mode mode = Mode.WAIT;

    /** Current player making a move, when interactive. */
    private Piece.Side side;

    /** Latch to hold down the Game thread while the user makes a selection. */
    private CountDownLatch latch;

    /** The move selected by the player. */
    private Move selectedMove;

    @Override
    public final void gameEvent(final GameEvent e) {
        board = e.getGame().getBoard();
        if (e.getType() != GameEvent.STATUS) {
            invalidate();
        }
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        int touchType = motionEvent.getAction();
        if (touchType == MotionEvent.ACTION_UP)
        {
            doTouchAction(motionEvent);
            invalidate();
            return true;
        }
        else
            return false;
    }

    /**
     * Handle the event when a simple touch is done
     *
     * @param motionEvent the motion event
     */
    private void doTouchAction(final MotionEvent motionEvent)
    {
        if (mode == Mode.WAIT) {
            return;
        }

        Position pos = new Position((int) motionEvent.getX(), (int) motionEvent.getY());
    }

    /**
     * Determine which tile a pixel point belongs to.
     *
     * @param p the point
     * @return  the position on the board
     */
    private Position getPixelPosition(final PointF p) {
        PointF pout = null;

        /*
        try {
            pout = getTransform().inverseTransform(p, null);
        } catch (java.awt.geom.NoninvertibleTransformException t) {
            return null;
        }
        */

        int x = (int) (pout.x / TILE_SIZE);
        int y = (int) (pout.y / TILE_SIZE);
        if (flipped) {
            y = board.getHeight() - 1 - y;
        }
        return new Position(x, y);
    }

    /** The interaction modes. */
    private enum Mode {
        /** Don't interact with the player. */
        WAIT,
        /** Interact with the player. */
        PLAYER;
    }


    public BoardView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        getHolder().addCallback(this);
    }

    private void updateSize() {
        setMinimumWidth(MIN_SIZE * board.getWidth());
        setMinimumHeight(MIN_SIZE * board.getHeight());
    }

    /**
     * Return the desired aspect ratio of the board.
     *
     * @return desired aspect ratio
     */
    public final double getRatio() {
        return board.getWidth() / (1.0 * board.getHeight());
    }

    /**
     * Change the board to be displayed.
     *
     * @param b the new board
     */
    public final void setBoard(final Board b) {
        board = b;
        updateSize();
        invalidate();
    }

    /**
     * Set whether or not the board should be displayed flipped.
     * @param value  the new flipped state
     */
    public final void setFlipped(final boolean value) {
        flipped = value;
    }

    @Override
    public final Move takeTurn(final Board turnBoard,
                               final Piece.Side currentSide) {
        latch = new CountDownLatch(1);
        board = turnBoard;
        side = currentSide;
        invalidate();
        mode = Mode.PLAYER;
        try {
            latch.await();
        } catch (InterruptedException e) {
            Log.w(LOG_TAG, "BoardPanel interrupted during turn.");
        }
        return selectedMove;
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        Canvas canvas = holder.lockCanvas();

        onDraw(canvas);

        holder.unlockCanvasAndPost(canvas);
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.drawColor(Color.WHITE);
        tileSize = canvas.getWidth()/8;

        Paint paint = new Paint();
        paint.setAntiAlias(true);

        for(int y = 0 ; y < nbCasesHeight ; y++) {
            for(int x = 0 ; x < nbCasesWidth ; x++) {
                if(((x+1)%2 == 0 && (y+1)%2 != 0) || ((x+1)%2 != 0 && (y+1)%2 == 0)) {
                    paint.setColor(DARK);
                    canvas.drawRect(x*tileSize, y*tileSize, (x*tileSize)+tileSize, (y*tileSize)+tileSize, paint);
                }
                else {
                    paint.setColor(LIGHT);
                    canvas.drawRect(x*tileSize, y*tileSize, (x*tileSize)+tileSize, (y*tileSize)+tileSize, paint);
                }
            }
        }

        for(int y = 0 ; y < nbCasesHeight ; y++) {
            for(int x = 0 ; x < nbCasesWidth ; x++) {
                //Piece piece = board.getPiece(new Position(x, y));
                /*if (piece != null) {
                    Picture picture = piece.getImage();
                    Rect rect = new Rect(x*pixelSize, y*pixelSize, (x*pixelSize)+pixelSize, (y*pixelSize)+pixelSize);
                    canvas.drawPicture(picture, rect);
                    /*Image tile = piece.getImage();
                    int yy = y;
                    if (flipped) {
                        yy = board.getHeight() - 1 - y;
                    }
                    at.setToTranslation(x * TILE_SIZE, yy * TILE_SIZE);
                    g.drawImage(tile, at, null);*/
                //}
            }
        }


    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getAction();

        if(action == MotionEvent.ACTION_DOWN) {
            invalidate();
        }

        return true;
    }

}
