package com.aoedb.widgets;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Rect;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.VelocityTracker;
import android.view.View;

public class Zoom2DScrollView extends TwoDScrollView implements ScaleGestureDetector.OnScaleGestureListener {

    private enum Mode {
        NONE,
        DRAG,
        ZOOM
    }

    private static final float MIN_ZOOM = 1.0f;
    private static final float MAX_ZOOM = 4.0f;

    private Mode mode = Mode.NONE;
    private float scale = 1.0f;
    private float lastScaleFactor = 0f;

    private float startX = 0f;
    private float startY = 0f;

    private float dx = 0f;
    private float dy = 0f;
    private float prevDx = 0f;
    private float prevDy = 0f;

    private float velocityX = 0f;
    private float velocityY = 0f;

    private float zoomPosX = 0f;
    private float zoomPosY = 0f;


    private Handler handler;

    private VelocityTracker velocityTracker;


    public Zoom2DScrollView(Context context) {
        super(context);
        init(context);
    }

    public Zoom2DScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public Zoom2DScrollView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    private boolean canFling(){
        return Math.abs(velocityX) > 500 || Math.abs(velocityY) > 500;
    }

    Runnable statusChecker = new Runnable() {
        @Override
        public void run() {
            int handlerInterval = 15;
            try{
                dx += velocityX / 1000 * handlerInterval;
                dy += velocityY / 1000 * handlerInterval;
                velocityX *= 0.95;
                velocityY *= 0.95;
                applyScaleAndTranslation();
                prevDx = dx;
                prevDy = dy;
            }
            finally {
                if (!canFling()) {
                    stopFling();
                }
                else handler.postDelayed(statusChecker, handlerInterval);
            }
        }
    };


    void startFling() {
        statusChecker.run();
    }

    void stopFling() {
        handler.removeCallbacks(statusChecker);
    }

    @SuppressLint("ClickableViewAccessibility")
    public void init(Context context) {
        velocityTracker = VelocityTracker.obtain();
        handler = new Handler();

        final ScaleGestureDetector scaleDetector = new ScaleGestureDetector(context, this);
        this.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {

                switch (motionEvent.getAction() & MotionEvent.ACTION_MASK) {
                    case MotionEvent.ACTION_DOWN:
                        mode = Mode.DRAG;

                        startX = motionEvent.getRawX() - prevDx;
                        startY = motionEvent.getRawY() - prevDy;
                        velocityX = 0;
                        velocityY = 0;
                        stopFling();
                        if (velocityTracker == null)  velocityTracker = VelocityTracker.obtain();
                        else velocityTracker.clear();
                        velocityTracker.addMovement(motionEvent);
                        break;
                    case MotionEvent.ACTION_MOVE:
                        if (mode == Mode.DRAG) {
                            dx = motionEvent.getRawX() - startX;
                            dy = motionEvent.getRawY() - startY;
                            velocityTracker.addMovement(motionEvent);
                        }
                        break;
                    case MotionEvent.ACTION_POINTER_DOWN:
                        mode = Mode.ZOOM;
                        break;
                    case MotionEvent.ACTION_POINTER_UP:
                        mode = Mode.DRAG;
                        break;
                    case MotionEvent.ACTION_UP:
                        mode = Mode.NONE;
                        prevDx = dx;
                        prevDy = dy;
                        velocityTracker.addMovement(motionEvent);
                        velocityTracker.computeCurrentVelocity(1000);
                        velocityX = velocityTracker.getXVelocity();
                        velocityY = velocityTracker.getYVelocity();
                        if (canFling()) startFling();
                        break;
                    case MotionEvent.ACTION_CANCEL:
                        velocityTracker.recycle();
                        velocityTracker = null;
                }
                scaleDetector.onTouchEvent(motionEvent);

                applyScaleAndTranslation();

                return true;
            }
        });
    }

    @Override
    public boolean onScaleBegin(ScaleGestureDetector scaleDetector) {
        Rect window = new Rect();
        child().getLocalVisibleRect(window);
        float newWindowX = window.left + scaleDetector.getFocusX() - window.width()/2.0f, newWindowY = window.top + scaleDetector.getFocusY()- window.height()/2.0f;
        zoomPosX = newWindowX / scale;
        zoomPosY =  newWindowY / scale;
        return true;
    }


    @Override
    public boolean onScale(ScaleGestureDetector scaleDetector) {
        float scaleFactor = scaleDetector.getScaleFactor();
        if (lastScaleFactor == 0 || (Math.signum(scaleFactor) == Math.signum(lastScaleFactor))) {
            scale *= scaleFactor;
            scale = Math.max(MIN_ZOOM, Math.min(scale, MAX_ZOOM));
            lastScaleFactor = scaleFactor;
            Rect bounds = getBounds();
            dx = bounds.right - zoomPosX * scale;
            dy = bounds.bottom - zoomPosY * scale;
        } else {
            lastScaleFactor = 0;
        }
        return true;
    }



    @Override
    public void onScaleEnd(ScaleGestureDetector scaleDetector) {
        prevDx = dx;
        prevDy = dy;
    }

    private Rect getBounds(){

        float maxX = ((child().getWidth() /2.0f)* scale) - (child().getWidth() /2.0f);
        float maxY = ((child().getHeight() /2.0f)* scale) - (child().getHeight() /2.0f) - getTop();
        float minX = maxX - (child().getWidth() * scale - getResources().getDisplayMetrics().widthPixels);
        float minY = maxY - (child().getHeight() * scale - getResources().getDisplayMetrics().heightPixels);
        return new Rect((int)minX, (int)minY,(int) maxX,(int) maxY);

    }

    private void applyScaleAndTranslation() {

        Rect rect = getBounds();


        dx = Math.min(Math.max(dx, rect.left), rect.right);
        dy = Math.min(Math.max(dy, rect.top), rect.bottom);


        if (dx == rect.right || dx == rect.left) velocityX = 0;
        if (dy == rect.top || dy == rect.bottom) velocityY = 0;
        if (velocityX == 0 && velocityY == 0) stopFling();

        child().setScaleX(scale);
        child().setScaleY(scale);
        child().setTranslationX(dx);
        child().setTranslationY(dy);
        invalidate();
    }

    private View child() {
        return getChildAt(0);
    }

}