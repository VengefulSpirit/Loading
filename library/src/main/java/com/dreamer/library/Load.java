package com.dreamer.library;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.RadialGradient;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.view.View;

/**
 * 作者： zhangzixu
 * 时间： 2016/1/22
 * 详情：
 */
public class Load extends View {
    public static final int PULL = 1;
    public static final int PRE_LOAD = 2;
    public static final int LOADING = 3;
    private int mStatus = PULL;

    private static final int LEFT_SIDE = 1;
    private static final int RIGHT_SIDE = 2;

    public static final float MAX_DISTANCE = 60;

    private PointF mPointF = new PointF(200, 200);

    private float smallRadius = 10;
    private float bigRadius = 15;
    private float leftMoveDistance, rightMoveDistance;

    private Paint mSmallLeftPaint, mSmallRightPaint;
    private Paint mBigPaint;
    private Paint mLeftBezierPaint;
    private Paint mRightBezierPaint;
    private Path path;

    public Load(Context context) {
        this(context, null);
    }

    public Load(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public Load(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mSmallLeftPaint = new Paint();
        mSmallLeftPaint.setColor(getResources().getColor(R.color.colorPrimary));
        mSmallLeftPaint.setAntiAlias(true);
        mSmallLeftPaint.setStyle(Paint.Style.FILL);

        mSmallRightPaint = new Paint();
        mSmallRightPaint.setColor(getResources().getColor(R.color.colorPrimary));
        mSmallRightPaint.setAntiAlias(true);
        mSmallRightPaint.setStyle(Paint.Style.FILL);

        mBigPaint = new Paint();
        mBigPaint.setColor(getResources().getColor(R.color.colorAccent));
        mBigPaint.setAntiAlias(true);
        mBigPaint.setStyle(Paint.Style.FILL);

        mLeftBezierPaint = new Paint();
        mLeftBezierPaint.setAntiAlias(true);

        mRightBezierPaint = new Paint();
        mRightBezierPaint.setAntiAlias(true);

        path = new Path();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (mStatus == PULL) {
            if (leftMoveDistance > 0 && leftMoveDistance <= bigRadius + smallRadius + 20) {
                calcBezier(LEFT_SIDE);
                /*mLeftBezierPaint.setShader(new LinearGradient(mPointF.x - leftMoveDistance, mPointF.y,
                        mPointF.x, mPointF.y,
                        getResources().getColor(R.color.colorPrimary),
                        getResources().getColor(R.color.colorAccent), Shader.TileMode.REPEAT));*/
                mLeftBezierPaint.setShader(new RadialGradient(mPointF.x - leftMoveDistance, mPointF.y, leftMoveDistance, getResources().getColor(R.color.colorPrimary),
                        getResources().getColor(R.color.colorAccent), Shader.TileMode.REPEAT));
                canvas.drawPath(path, mLeftBezierPaint); //左贝塞尔
            } else {
                //处理断开动画
            }

            if (rightMoveDistance > 0 && rightMoveDistance <= bigRadius + smallRadius + 20) {
                calcBezier(RIGHT_SIDE);
                /*mLeftBezierPaint.setShader(new LinearGradient(mPointF.x - leftMoveDistance, mPointF.y,
                        mPointF.x, mPointF.y,
                        getResources().getColor(R.color.colorPrimary),
                        getResources().getColor(R.color.colorAccent), Shader.TileMode.REPEAT));*/
                mRightBezierPaint.setShader(new RadialGradient(mPointF.x + rightMoveDistance, mPointF.y, rightMoveDistance, getResources().getColor(R.color.colorPrimary),
                        getResources().getColor(R.color.colorAccent), Shader.TileMode.REPEAT));
                canvas.drawPath(path, mLeftBezierPaint); //左贝塞尔
            } else {

            }

            canvas.drawCircle(mPointF.x - leftMoveDistance, mPointF.y, smallRadius, mSmallLeftPaint);   //左
            canvas.drawCircle(mPointF.x + rightMoveDistance, mPointF.y, smallRadius, mSmallRightPaint);  //右
            canvas.drawCircle(mPointF.x, mPointF.y, bigRadius, mBigPaint);                          //中
        } else if (mStatus == PRE_LOAD) {
            canvas.drawCircle(mPointF.x - MAX_DISTANCE - leftMoveDistance, mPointF.y, smallRadius, mSmallLeftPaint);   //左
            canvas.drawCircle(mPointF.x + MAX_DISTANCE - rightMoveDistance, mPointF.y, smallRadius, mSmallRightPaint);  //右
            canvas.drawCircle(mPointF.x, mPointF.y, bigRadius, mBigPaint);                          //中
        } else if (mStatus == LOADING) {
            canvas.drawCircle(mPointF.x - 2 * MAX_DISTANCE + leftMoveDistance, mPointF.y, smallRadius, mSmallLeftPaint);   //左
            canvas.drawCircle(mPointF.x - MAX_DISTANCE + rightMoveDistance, mPointF.y, smallRadius, mSmallRightPaint);  //右
            canvas.drawCircle(mPointF.x, mPointF.y, bigRadius, mBigPaint);                          //中
        } else {
            canvas.drawCircle(mPointF.x, mPointF.y, smallRadius, mSmallLeftPaint);   //左
            canvas.drawCircle(mPointF.x, mPointF.y, smallRadius, mSmallRightPaint);  //右
            canvas.drawCircle(mPointF.x, mPointF.y, bigRadius, mBigPaint);                          //中
        }
    }

    private void calcBezier(int side) {
        float smallX1 = 0;  //左上
        float smallY1 = 0;  //左上
        float smallX2 = 0;  //左下
        float smallY2 = 0;  //左下
        float bigX1 = 0;    //中上
        float bigY1 = 0;    //中上
        float bigX2 = 0;    //中下
        float bigY2 = 0;    //中下
        float anchorX1 = 0; //左控制点
        float anchorY1 = 0; //左控制点
        float anchorX2 = 0; //
        float anchorY2 = 0; //

        path.reset();
        if (side == LEFT_SIDE) {
            smallX1 = mPointF.x - leftMoveDistance;
            smallY1 = mPointF.y - smallRadius;

            smallX2 = mPointF.x - leftMoveDistance;
            smallY2 = mPointF.y + smallRadius;

            bigX1 = mPointF.x;
            bigY1 = mPointF.y - bigRadius;

            bigX2 = mPointF.x;
            bigY2 = mPointF.y + bigRadius;

            anchorX1 = mPointF.x - (leftMoveDistance / 3) * 2;
            anchorY1 = mPointF.y;

            path.moveTo(smallX1, smallY1);
            path.quadTo(anchorX1, anchorY1 + leftMoveDistance / 5, bigX1, bigY1);
            path.lineTo(bigX2, bigY2);
            path.quadTo(anchorX1, anchorY1 - leftMoveDistance / 5, smallX2, smallY2);
            path.lineTo(smallX1, smallY1);
        } else if (side == RIGHT_SIDE) {
            smallX1 = mPointF.x + rightMoveDistance;
            smallY1 = mPointF.y - smallRadius;

            smallX2 = mPointF.x + rightMoveDistance;
            smallY2 = mPointF.y + smallRadius;

            bigX1 = mPointF.x;
            bigY1 = mPointF.y - bigRadius;

            bigX2 = mPointF.x;
            bigY2 = mPointF.y + bigRadius;

            anchorX2 = mPointF.x + (rightMoveDistance / 3) * 2;
            anchorY2 = mPointF.y;

            path.moveTo(smallX1, smallY1);
            path.quadTo(anchorX2, anchorY2 + leftMoveDistance / 5, bigX1, bigY1);
            path.lineTo(bigX2, bigY2);
            path.quadTo(anchorX2, anchorY2 - leftMoveDistance / 5, smallX2, smallY2);
            path.lineTo(smallX1, smallY1);
        }
    }

    public float getBigRadius() {
        return this.bigRadius;
    }

    public void setBigRadius(float mBigRadius) {
        this.bigRadius = mBigRadius;
        invalidate();
    }

    public float getSmallRadius() {
        return smallRadius;
    }

    public void setSmallRadius(float mSmallRadius) {
        this.smallRadius = mSmallRadius;
    }

    public int getStatus() {
        return mStatus;
    }

    public void setStatus(int mStatus) {
        this.mStatus = mStatus;
        leftMoveDistance = rightMoveDistance = 0;
    }

    public float getLeftMoveDistance() {
        return leftMoveDistance;
    }

    public void setLeftMoveDistance(float leftMoveDistance) {
        this.leftMoveDistance = leftMoveDistance;
        invalidate();
    }

    public float getRightMoveDistance() {
        return rightMoveDistance;
    }

    public void setRightMoveDistance(float rightMoveDistance) {
        this.rightMoveDistance = rightMoveDistance;
        invalidate();
    }
}
