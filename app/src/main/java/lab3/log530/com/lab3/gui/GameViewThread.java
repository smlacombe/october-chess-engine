package lab3.log530.com.lab3.gui;

import android.graphics.Canvas;
import android.util.Log;
import android.view.SurfaceHolder;

public class GameViewThread extends Thread {

    private SurfaceHolder threadSurfaceHolder;
    private BoardView threadGameView;
    private boolean isRunning = false;
    private boolean isPaused = false;

    public GameViewThread(SurfaceHolder surfaceHolder, BoardView surfaceView) {
        threadSurfaceHolder = surfaceHolder;
        threadGameView = surfaceView;
    }

    public void setRunning(boolean isRunning) {
        this.isRunning = isRunning;
    }

    public void setPaused(boolean isPaused) {
        this.isPaused = isPaused;
    }

    @Override
    public void run() {
        while (isRunning) {
            Canvas c = null;
            try {
                if(isRunning) {
                    c = threadSurfaceHolder.lockCanvas(null);
                    synchronized (threadSurfaceHolder) {
                        threadGameView.onDraw(c);
                    }
                }

            }
            catch(Exception e) {
                this.interrupt();
            }
            finally {
                // do this in a finally so that if an exception is thrown
                // during the above, we don't leave the Surface in an
                // inconsistent state
                if (c != null) {
                    threadSurfaceHolder.unlockCanvasAndPost(c);
                }
            }
            while(isPaused) {
                try {
                    synchronized (threadSurfaceHolder) {
                        //sleep(10);
                    }
                }
                catch(Exception e) {
                    Log.e("GameViewThread","Error in the waiting of the thread GameView", e);
                }
            }
        }
    }
}
