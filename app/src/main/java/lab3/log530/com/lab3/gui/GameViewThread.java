package lab3.log530.com.lab3.gui;

import android.graphics.Canvas;
import android.view.SurfaceHolder;

public class GameViewThread extends Thread {

    private SurfaceHolder threadSurfaceHolder;
    private BoardView threadGameView;
    private boolean myThreadRun = false;

    public GameViewThread(SurfaceHolder surfaceHolder, BoardView surfaceView) {
        threadSurfaceHolder = surfaceHolder;
        threadGameView = surfaceView;
    }

    public void setRunning(boolean b) {
        myThreadRun = b;
    }

    @Override
    public void run() {
        while (myThreadRun) {
            Canvas c = null;
            try {
                c = threadSurfaceHolder.lockCanvas(null);
                synchronized (threadSurfaceHolder) {
                    threadGameView.onDraw(c);
                }
            } finally {
                // do this in a finally so that if an exception is thrown
                // during the above, we don't leave the Surface in an
                // inconsistent state
                if (c != null) {
                    threadSurfaceHolder.unlockCanvasAndPost(c);
                }
            }
        }
    }
}
