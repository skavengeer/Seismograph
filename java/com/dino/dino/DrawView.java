package com.dino.dino;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.view.View;
import android.widget.Toast;

import java.util.Date;

public class DrawView extends View {

    float AMPLIFY_FACTOR;
    int DRAW_INTERVAL;
    final int DRAW_MAX = 100;
    final boolean EMULATOR_MODE = false;
    final float FILTERING_FACTOR = 0.1F;
    final int MAJOR_V_LINE_NUM = 10;
    final int MAX_HISTORY = 1000;
    final int MINOR_V_LINE_NUM = 50;
    int SCR_HEIGHT;
    int SCR_WIDTH;
    int X_OFFSET_TOP;
    int Y_OFFSET_TOP;
    int Z_OFFSET_TOP;
    float mAccX;
    float mAccY;
    float mAccZ;
    public int mCurrentIndex;
    public float[] mHistoryX = new float[1000];
    public float[] mHistoryY = new float[1000];
    public float[] mHistoryZ = new float[1000];
    public boolean mIsHighPassFilterOn = true;
    public boolean mIsPlaying;
    private SensorEventListener mListener;
    SensorManager mSensorManager;
    float mX;
    float mY;
    float mZ;
    Paint paintLineHorizontal = new Paint();
    Paint paintLineHorizontalMinor = new Paint();
    Paint paintLineVerticalMajor = new Paint();
    Paint paintLineVerticalMinor = new Paint();
    Paint paintNeedle = new Paint();
    Paint paintText = new Paint();
    Paint paintX = new Paint();
    Paint paintY = new Paint();
    Paint paintZ = new Paint();


    public DrawView(Context paramContext) {
        super(paramContext);
        this.paintX.setAntiAlias(true);
        this.paintX.setColor(-65536);
        this.paintY.setAntiAlias(true);
        this.paintY.setColor(-16711936);
        this.paintZ.setAntiAlias(true);
        this.paintZ.setColor(-16776961);
        this.paintLineVerticalMinor.setColor(-3355444);
        this.paintLineVerticalMajor.setColor(Color.argb(100, 170, 170, 220));
        this.paintLineVerticalMajor.setStrokeWidth(2.0F);
        this.paintLineHorizontal.setColor(-7829368);
        this.paintLineHorizontal.setStrokeWidth(3.0F);
        this.paintLineHorizontalMinor.setColor(-7829368);
        this.paintLineHorizontalMinor.setStrokeWidth(1.0F);
        this.paintText.setColor(-7829368);
        this.paintText.setAlpha(150);
        this.paintText.setTextSize(60.0F);
        this.paintNeedle.setColor(-16777216);
        this.paintNeedle.setStrokeWidth(6.0F);
        this.paintNeedle.setAntiAlias(true);
        this.mIsPlaying = true;

        new DrawView$DrawTimer(this, 1000000L, 100L).start();

        this.mSensorManager = ((SensorManager)paramContext.getSystemService(Context.SENSOR_SERVICE));

        this.mListener = new DrawView$1(this);
        this.mSensorManager.registerListener(this.mListener, this.mSensorManager.getDefaultSensor(1), 0);

        this.mSensorManager.registerListener(this.mListener, this.mSensorManager.getDefaultSensor(1), 0);
    }

    public void Update()    {        postInvalidate();    }

    public void UpdateAcc(float paramFloat1, float paramFloat2, float paramFloat3)
    {
        if (this.mIsHighPassFilterOn)
        {
            this.mX = (paramFloat1 / 10.0F);
            this.mY = (paramFloat2 / 10.0F);
            this.mZ = (paramFloat3 / 10.0F);
            this.mAccX = ((float)(this.mX * 0.1F + this.mAccX * 0.8999999985098839D));
            this.mAccY = ((float)(this.mY * 0.1F + this.mAccY * 0.8999999985098839D));
            this.mAccZ = ((float)(this.mZ * 0.1F + this.mAccZ * 0.8999999985098839D));
            return;
        }
        this.mX = (paramFloat1 / 200.0F);
        this.mY = (paramFloat2 / 200.0F);
        this.mZ = (paramFloat3 / 200.0F);
        this.mAccX = 0.0F;
        this.mAccY = 0.0F;
        this.mAccZ = 0.0F;
    }

    public void setFilter(boolean paramBoolean)
    {
        this.mIsHighPassFilterOn = paramBoolean;
        if (paramBoolean)
        {
            Toast.makeText(getContext(), "High Pass Filter is ON. It shows the device acceleration.", 4).show();
            return;
        }
        Toast.makeText(getContext(), "High Pass Filter is OFF. It shows the device orientation.", 4).show();
    }

    public void togglePlay()
    {
        if (this.mIsPlaying)
        {
            this.mIsPlaying = false;
            return;
        }
        this.mIsPlaying = true;
    }
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)    {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        this.X_OFFSET_TOP = (getMeasuredHeight() / 4);
        this.Y_OFFSET_TOP = (getMeasuredHeight() * 2 / 4);
        this.Z_OFFSET_TOP = (getMeasuredHeight() * 3 / 4);
        this.SCR_WIDTH = getMeasuredWidth();
        this.SCR_HEIGHT = getMeasuredHeight();
        this.DRAW_INTERVAL = (this.SCR_WIDTH / 100);
        this.AMPLIFY_FACTOR = (this.SCR_HEIGHT / 10.0F);
    }
    public float Amplify(float paramFloat)
    {
        return ((float)Math.pow(10.0D, 1.0F + paramFloat) - 10.0F) * this.AMPLIFY_FACTOR;
    }

    public void onDraw(Canvas canvas) {
        float f;
        int n = -100 + this.mCurrentIndex;
        if (n < 0) {
            n = -1 + (n + 1000);
        }
        canvas.drawText("X", 20.0f, (float)(20 + this.X_OFFSET_TOP), this.paintText);
        canvas.drawText("Y", 20.0f, (float)(20 + this.Y_OFFSET_TOP), this.paintText);
        canvas.drawText("Z", 20.0f, (float)(20 + this.Z_OFFSET_TOP), this.paintText);
        canvas.drawLine(0.0f, (float)(this.X_OFFSET_TOP / 2), (float)this.SCR_WIDTH, (float)(this.X_OFFSET_TOP / 2), this.paintLineHorizontalMinor);
        canvas.drawLine(0.0f, (float)this.X_OFFSET_TOP, (float)this.SCR_WIDTH, (float)this.X_OFFSET_TOP, this.paintLineHorizontal);
        canvas.drawLine(0.0f, (float)((this.Y_OFFSET_TOP + this.X_OFFSET_TOP) / 2), (float)this.SCR_WIDTH, (float)((this.Y_OFFSET_TOP + this.X_OFFSET_TOP) / 2), this.paintLineHorizontalMinor);
        canvas.drawLine(0.0f, (float)this.Y_OFFSET_TOP, (float)this.SCR_WIDTH, (float)this.Y_OFFSET_TOP, this.paintLineHorizontal);
        canvas.drawLine(0.0f, (float)((this.Z_OFFSET_TOP + this.Y_OFFSET_TOP) / 2), (float)this.SCR_WIDTH, (float)((this.Z_OFFSET_TOP + this.Y_OFFSET_TOP) / 2), this.paintLineHorizontalMinor);
        canvas.drawLine(0.0f, (float)this.Z_OFFSET_TOP, (float)this.SCR_WIDTH, (float)this.Z_OFFSET_TOP, this.paintLineHorizontal);
        canvas.drawLine(0.0f, (float)((this.SCR_HEIGHT + this.Z_OFFSET_TOP) / 2), (float)this.SCR_WIDTH, (float)((this.SCR_HEIGHT + this.Z_OFFSET_TOP) / 2), this.paintLineHorizontalMinor);
        int n2 = 0;
        while (true){
            f = (float)(n2 * this.DRAW_INTERVAL - n * 7);
            if ((float)this.SCR_WIDTH < f){
                break;
            }
            if (n2 % 25 == 0) {
                canvas.drawLine(f, 0.0f, f, (float)this.SCR_HEIGHT, this.paintLineVerticalMajor);
            } else if (n2 % 5 == 0) {
                canvas.drawLine(f, 0.0f, f, (float)this.SCR_HEIGHT, this.paintLineVerticalMinor);
            }
            ++n2;
        }
        int n3 = 0;
        while (n3 < 100) {
            canvas.drawLine((float)(-30 + n3 * this.DRAW_INTERVAL), (float)this.X_OFFSET_TOP + this.Amplify(this.mHistoryX[n]), (float)(-30 + this.DRAW_INTERVAL * (n3 + 1)), (float)this.X_OFFSET_TOP + this.Amplify(this.mHistoryX[n + 1]), this.paintX);
            canvas.drawLine((float)(-30 + n3 * this.DRAW_INTERVAL), (float)this.Y_OFFSET_TOP + this.Amplify(this.mHistoryY[n]), (float)(-30 + this.DRAW_INTERVAL * (n3 + 1)), (float)this.Y_OFFSET_TOP + this.Amplify(this.mHistoryY[n + 1]), this.paintY);
            canvas.drawLine((float)(-30 + n3 * this.DRAW_INTERVAL), (float)this.Z_OFFSET_TOP + this.Amplify(this.mHistoryZ[n]), (float)(-30 + this.DRAW_INTERVAL * (n3 + 1)), (float)this.Z_OFFSET_TOP + this.Amplify(this.mHistoryZ[n + 1]), this.paintZ);
            if (n3 == 99) {
                float f2 = (float)this.X_OFFSET_TOP + this.Amplify(this.mHistoryX[n + 1]);
                float f3 = (float)this.Y_OFFSET_TOP + this.Amplify(this.mHistoryY[n + 1]);
                float f4 = (float)this.Z_OFFSET_TOP + this.Amplify(this.mHistoryZ[n + 1]);
                canvas.drawLine((float)(-30 + this.DRAW_INTERVAL * (n3 + 1)), f2, (float)(15 + this.DRAW_INTERVAL * (n3 + 2)), f2 - 3.0f, this.paintNeedle);
                canvas.drawLine((float)(-30 + this.DRAW_INTERVAL * (n3 + 1)), f2, (float)(15 + this.DRAW_INTERVAL * (n3 + 2)), f2 + 3.0f, this.paintNeedle);
                canvas.drawLine((float)(-30 + this.DRAW_INTERVAL * (n3 + 1)), f3, (float)(15 + this.DRAW_INTERVAL * (n3 + 2)), f3 - 3.0f, this.paintNeedle);
                canvas.drawLine((float)(-30 + this.DRAW_INTERVAL * (n3 + 1)), f3, (float)(15 + this.DRAW_INTERVAL * (n3 + 2)), f3 + 3.0f, this.paintNeedle);
                canvas.drawLine((float)(-30 + this.DRAW_INTERVAL * (n3 + 1)), f4, (float)(15 + this.DRAW_INTERVAL * (n3 + 2)), f4 - 3.0f, this.paintNeedle);
                canvas.drawLine((float)(-30 + this.DRAW_INTERVAL * (n3 + 1)), f4, (float)(15 + this.DRAW_INTERVAL * (n3 + 2)), f4 + 3.0f, this.paintNeedle);
            }
            if (++n > 998) {
                n = 0;
            }
            ++n3;
        }
    }

}
