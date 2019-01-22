package com.dino.dino;

import android.os.CountDownTimer;

public class DrawView$DrawTimer extends CountDownTimer {

    final com.dino.dino.DrawView this$0;

    public DrawView$DrawTimer( DrawView this$0,long millisInFuture, long countDownInterval) {
        super(millisInFuture, countDownInterval);
        this.this$0 = this$0;
    }


    @Override
    public void onTick(long millisUntilFinished) {
        if (!this.this$0.mIsPlaying) {
            return;
        }
        DrawView localDrawView = this.this$0;
        localDrawView.mCurrentIndex += 1;
        if (this.this$0.mCurrentIndex > 999) {
            this.this$0.mCurrentIndex = 0;
        }
        this.this$0.mHistoryX[this.this$0.mCurrentIndex] = (this.this$0.mX - this.this$0.mAccX);
        this.this$0.mHistoryY[this.this$0.mCurrentIndex] = (this.this$0.mY - this.this$0.mAccY);
        this.this$0.mHistoryZ[this.this$0.mCurrentIndex] = (this.this$0.mZ - this.this$0.mAccZ);
        this.this$0.Update();
    }


    @Override
    public void onFinish() {start(); }


}
