package lab3.log530.com.lab3.gui;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class GameView extends SurfaceView implements SurfaceHolder.Callback {

    private int nbCasesHeight = 8;
    private int nbCasesWidth = 8;
    private final static int PIXEL_SIZE = 40;


    public GameView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        getHolder().addCallback(this);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        Canvas canvas = holder.lockCanvas(null);

        canvas.drawColor(Color.BLUE);

        Paint paint = new Paint();
        paint.setColor(Color.BLACK);
        paint.setAntiAlias(true);

        for(int y = 0 ; y < nbCasesHeight ; y++) {
            for(int x = 0 ; x < nbCasesWidth ; x++) {
                if(((x+1)%2 == 0 && (y+1)%2 != 0) || ((x+1)%2 != 0 && (y+1)%2 == 0)) {
                    paint.setColor(Color.BLACK);
                    canvas.drawRect(x*PIXEL_SIZE, y*PIXEL_SIZE, (x*PIXEL_SIZE)+PIXEL_SIZE, (y*PIXEL_SIZE)+PIXEL_SIZE, paint);
                }
                else {
                    paint.setColor(Color.WHITE);
                    canvas.drawRect(x*PIXEL_SIZE, y*PIXEL_SIZE, (x*PIXEL_SIZE)+PIXEL_SIZE, (y*PIXEL_SIZE)+PIXEL_SIZE, paint);
                }
            }
        }

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

    }
}
