package idv.tomazwang.app.drawpokemon.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.os.Build;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

public class DrawingView extends View {


    private Path mDrawingPath;
    private Paint mDrawingPaint;
    private Paint mCanvasPaint;

    // color ARGB
    private int mPaintAlpha = 255;
    private int mPaintR = 0;
    private int mPaintG = 0;
    private int mPaintB = 0;

    private Canvas mCanvas;
    private Bitmap mCanvasBitmap;
    private float mStrokeWidth = 20f;


    public DrawingView(Context context) {
        super(context);
    }

    public DrawingView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setupDrawing();
    }

    public DrawingView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public DrawingView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }


    private void setupDrawing() {

        // initialize paint (color)
        mDrawingPaint = new Paint();

        int colorInt = Color.argb(mPaintAlpha, mPaintR, mPaintB, mPaintG);
        mDrawingPaint.setColor(colorInt);

        mDrawingPaint.setAntiAlias(true);
        mDrawingPaint.setStrokeWidth(mStrokeWidth);

        mDrawingPaint.setStyle(Paint.Style.STROKE);
        mDrawingPaint.setStrokeCap(Paint.Cap.ROUND);
        mDrawingPaint.setStrokeJoin(Paint.Join.ROUND);

        // initialize path
        mDrawingPath = new Path();

        // initialize canvas color
        mCanvasPaint = new Paint(Paint.DITHER_FLAG);

    }


    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        mCanvasBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        mCanvas = new Canvas(mCanvasBitmap);

    }


    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawBitmap(mCanvasBitmap, 0, 0, mCanvasPaint);
        canvas.drawPath(mDrawingPath, mDrawingPaint);
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float touchX = event.getX();
        float touchY = event.getY();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mDrawingPath.moveTo(touchX, touchY);
                break;
            case MotionEvent.ACTION_MOVE:
                mDrawingPath.lineTo(touchX, touchY);
                break;
            case MotionEvent.ACTION_UP:
                mCanvas.drawPath(mDrawingPath, mDrawingPaint);
                mDrawingPath.reset();
                break;
            default:
                return false;
        }

        invalidate();
        return true;
    }

    public void setPaintColor(int R, int G, int B){
        setPaintColor(255, R, G, B);
    }

    public void setPaintColor(int A, int R, int G, int B){
        setPaintColor(Color.argb(A,R,G,B));
    }

    public void setPaiintColor(String colorStr){
        setPaintColor(Color.parseColor(colorStr));
    }

    public void setPaintColor(int colorInt){

        invalidate();

        mPaintAlpha = Color.alpha(colorInt);
        mPaintR = Color.red(colorInt);
        mPaintB = Color.blue(colorInt);
        mPaintG = Color.green(colorInt);

        mDrawingPaint.setColor(colorInt);
    }
}
