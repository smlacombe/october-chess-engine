package lab3.log530.com.lab3.gui;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.Picture;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import lab3.log530.com.lab3.Board;
import lab3.log530.com.lab3.GameEvent;
import lab3.log530.com.lab3.GameListener;
import lab3.log530.com.lab3.Player;
import lab3.log530.com.lab3.Position;
import lab3.log530.com.lab3.MoveList;
import lab3.log530.com.lab3.Piece;
import java.util.concurrent.CountDownLatch;
import lab3.log530.com.lab3.Move;


public class BoardView extends SurfaceView implements SurfaceHolder.Callback, Player, GameListener {

    private int tileSize = 0;

    private GameViewThread thread;

    private static final String LOG_TAG = "BoardView";

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


    public BoardView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        getHolder().addCallback(this);
        thread = new GameViewThread(getHolder(), this);
    }

    @Override
    public final void gameEvent(final GameEvent e) {
        board = e.getGame().getBoard();
        if (e.getType() != GameEvent.STATUS) {
            thread.setPaused(false);
        }
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {

        int action = event.getAction();
        Log.i("Motion", "onTouchEvent: " + action);
        if (action == MotionEvent.ACTION_UP)
        {
            System.out.println("touch up!");
            doTouchAction(event);
            return true;
        }

        return true;
    }

    /**
     * Handle the event when a simple touch is done
     *
     * @param motionEvent the motion event
     */
    private void doTouchAction(final MotionEvent motionEvent)
    {
        Log.i("Motion", "dotouch");
        if (mode == Mode.WAIT) {
            System.out.println("We wait");
            return;
        }
        Position pos = getPixelPosition(new PointF(motionEvent.getX(), motionEvent.getY()));
        System.out.println("Position detected x:" + pos.getX() + " y: " + pos.getY());
        if (!board.inRange(pos)) {
            /* Click was outside the board, somehow. */
            return;
        }
        if (pos != null) {
            if (pos.equals(selected)) {
                /* Deselect */
                selected = null;
                moves = null;
            } else if (moves != null && moves.containsDest(pos)) {
                /* Move selected piece */
                mode = Mode.WAIT;
                Move move = moves.getMoveByDest(pos);
                selected = null;
                moves = null;
                selectedMove = move;
                latch.countDown();
            } else {
                /* Select this position */
                Piece p = board.getPiece(pos);
                if (p != null && p.getSide() == side) {
                    selected = pos;
                    moves = p.getMoves(true);
                }
            }
        }
    }

    /**
     * Determine which tile a pixel point belongs to.
     *
     * @param p the point
     * @return  the position on the board
     */
    private Position getPixelPosition(final PointF p) {
        Matrix scaledMatrix = getScaledMatrix();
        Matrix inverseMatrix = new Matrix();
        scaledMatrix.invert(inverseMatrix);
        float[] touchPoint = new float[] {p.x,p.y};
        inverseMatrix.mapPoints(touchPoint);

        int x = (int) (touchPoint[0] / tileSize);
        int y = (int) (touchPoint[1]  / tileSize);
        if (flipped) {
            y = board.getHeight() - 1 - y;
        }
        return new Position(x, y);
    }

    /**
     * Return the transform between working space and drawing space.
     *
     * @return display transform
     */
    public final Matrix getScaledMatrix() {
        Matrix m = new Matrix();
        m.setScale((float)(getWidth() / (tileSize * board.getWidth())), (float) (getHeight() / (tileSize * board.getHeight())));
        return m;
    }

    /** The interaction modes. */
    private enum Mode {
        /** Don't interact with the player. */
        WAIT,
        /** Interact with the player. */
        PLAYER;
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
        //invalidate();
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
        thread.setRunning(true);
        thread.start();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        boolean retry = true;
        thread.setRunning(false);
        while (retry) {
            try {
                thread.join();
                retry = false;
            } catch (InterruptedException e) {
            }
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.drawColor(Color.WHITE);

        if(board != null) {
            tileSize = canvas.getWidth() / board.getWidth();

            Paint paint = new Paint();
            paint.setAntiAlias(true);

            for (int y = 0; y < board.getHeight(); y++) {
                for (int x = 0; x < board.getWidth(); x++) {
                    if (((x + 1) % 2 == 0 && (y + 1) % 2 != 0) || ((x + 1) % 2 != 0 && (y + 1) % 2 == 0)) {
                        paint.setColor(DARK);
                        canvas.drawRect(x * tileSize, y * tileSize, (x * tileSize) + tileSize, (y * tileSize) + tileSize, paint);
                    } else {
                        paint.setColor(LIGHT);
                        canvas.drawRect(x * tileSize, y * tileSize, (x * tileSize) + tileSize, (y * tileSize) + tileSize, paint);
                    }
                }
            }



            for(int y = 0 ; y < board.getHeight() ; y++) {
                for (int x = 0; x < board.getWidth(); x++) {
                    Piece piece = board.getPiece(new Position(x, y));
                    if (piece != null) {
                        Picture picture = piece.getImage();
                        int yy = y;
                        if (flipped) {
                            yy = board.getHeight() - 1 - y;
                        }

                        Rect rect = new Rect(x * tileSize, yy * tileSize, (x * tileSize) + tileSize, (yy * tileSize) + tileSize);
                        canvas.drawPicture(picture, rect);
                    }
                }
            }


            // Draw last move
            Move last = board.last();
            if (last != null) {
                paint.setColor(LAST);
            //highlight(g, last.getOrigin());
            //highlight(g, last.getDest());
            }

            // Draw selected square
            if (selected != null) {
                paint.setColor(SELECTED);
                //highlight(g, selected);

                // Draw piece moves
                if (moves != null) {
                    paint.setColor(MOVEMENT);
                    for (Move move : moves) {
                        //highlight(g, move.getDest());
                    }
                }
            }
            thread.setPaused(true);
        }
    }
}
