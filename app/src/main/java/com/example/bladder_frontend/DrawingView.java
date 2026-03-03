package com.example.bladder_frontend;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.util.AttributeSet;
import android.view.MotionEvent;
import androidx.appcompat.widget.AppCompatImageView;

import java.util.ArrayList;
import java.util.List;

public class DrawingView extends AppCompatImageView {

    public enum Mode { NONE, DRAW, MEASURE }
    private Mode currentMode = Mode.NONE;

    private Path drawPath;
    private Paint drawPaint, canvasPaint;
    private Canvas drawCanvas;
    private Bitmap canvasBitmap;

    private float startX, startY;
    
    private List<DrawAction> drawActions = new ArrayList<>();
    
    private static class DrawAction {
        Path path;
        Paint paint;
        Mode mode;
        float distance = 0;

        DrawAction(Path path, Paint paint, Mode mode) {
            this.path = new Path(path);
            this.paint = new Paint(paint);
            this.mode = mode;
        }
    }

    public DrawingView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setupDrawing();
    }

    private void setupDrawing() {
        drawPath = new Path();
        drawPaint = new Paint();
        drawPaint.setColor(Color.RED);
        drawPaint.setAntiAlias(true);
        drawPaint.setStrokeWidth(8);
        drawPaint.setStyle(Paint.Style.STROKE);
        drawPaint.setStrokeJoin(Paint.Join.ROUND);
        drawPaint.setStrokeCap(Paint.Cap.ROUND);
        canvasPaint = new Paint(Paint.DITHER_FLAG);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        if (w > 0 && h > 0) {
            canvasBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
            drawCanvas = new Canvas(canvasBitmap);
            redrawAll();
        }
    }

    private void redrawAll() {
        if (drawCanvas == null) return;
        drawCanvas.drawColor(0, PorterDuff.Mode.CLEAR);
        for (DrawAction action : drawActions) {
            drawCanvas.drawPath(action.path, action.paint);
        }
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawBitmap(canvasBitmap, 0, 0, canvasPaint);
        if (currentMode != Mode.NONE) {
            canvas.drawPath(drawPath, drawPaint);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (currentMode == Mode.NONE) return false;

        float touchX = event.getX();
        float touchY = event.getY();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                startX = touchX;
                startY = touchY;
                drawPath.moveTo(touchX, touchY);
                break;
            case MotionEvent.ACTION_MOVE:
                if (currentMode == Mode.DRAW) {
                    drawPath.lineTo(touchX, touchY);
                } else if (currentMode == Mode.MEASURE) {
                    drawPath.reset();
                    drawPath.moveTo(startX, startY);
                    drawPath.lineTo(touchX, touchY);
                }
                break;
            case MotionEvent.ACTION_UP:
                if (currentMode == Mode.MEASURE) {
                    drawPath.reset();
                    drawPath.moveTo(startX, startY);
                    drawPath.lineTo(touchX, touchY);
                    
                    double dist = Math.sqrt(Math.pow(touchX - startX, 2) + Math.pow(touchY - startY, 2));
                    // Simple scale: 100px = 1cm (for demo)
                    float cm = (float) (dist / 100.0);
                    if (onMeasurementListener != null) {
                        onMeasurementListener.onMeasured(cm);
                    }
                }
                
                DrawAction action = new DrawAction(drawPath, drawPaint, currentMode);
                drawActions.add(action);
                drawCanvas.drawPath(drawPath, drawPaint);
                drawPath.reset();
                break;
            default:
                return false;
        }

        invalidate();
        return true;
    }

    public void setMode(Mode mode) {
        this.currentMode = mode;
        if (mode == Mode.DRAW) {
            drawPaint.setColor(Color.RED);
            drawPaint.setStrokeWidth(8);
        } else if (mode == Mode.MEASURE) {
            drawPaint.setColor(Color.YELLOW);
            drawPaint.setStrokeWidth(5);
        }
    }

    public void undo() {
        if (!drawActions.isEmpty()) {
            drawActions.remove(drawActions.size() - 1);
            redrawAll();
        }
    }

    public void clear() {
        drawActions.clear();
        redrawAll();
    }

    public interface OnMeasurementListener {
        void onMeasured(float distanceCm);
    }

    private OnMeasurementListener onMeasurementListener;

    public void setOnMeasurementListener(OnMeasurementListener listener) {
        this.onMeasurementListener = listener;
    }
}
