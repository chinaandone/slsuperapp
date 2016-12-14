package com.cleverm.smartpen.ui.loading.indicator;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.animation.LinearInterpolator;

import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.ValueAnimator;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xiong,An android project Engineer,on 5/5/2016.
 * Data:5/5/2016  下午 02:56
 * Base on clever-m.com(JAVA Service)
 * Describe:
 * Version:1.0
 * Open source
 */
public class BallScaleRippleIndicator extends BallScaleIndicator {


    @Override
    public void draw(Canvas canvas, Paint paint) {
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(3);
        super.draw(canvas, paint);
    }

    @Override
    public List<Animator> createAnimation() {
        List<Animator> animators=new ArrayList<Animator>();
        ValueAnimator scaleAnim=ValueAnimator.ofFloat(0,1);
        scaleAnim.setInterpolator(new LinearInterpolator());
        scaleAnim.setDuration(1000);
        scaleAnim.setRepeatCount(-1);
        scaleAnim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                scale = (Float) animation.getAnimatedValue();
                postInvalidate();
            }
        });
        scaleAnim.start();

        ValueAnimator alphaAnim=ValueAnimator.ofInt(0, 255);
        alphaAnim.setInterpolator(new LinearInterpolator());
        alphaAnim.setDuration(1000);
        alphaAnim.setRepeatCount(-1);
        alphaAnim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                alpha = (Integer) animation.getAnimatedValue();
                postInvalidate();
            }
        });
        alphaAnim.start();

        animators.add(scaleAnim);
        animators.add(alphaAnim);
        return animators;
    }

}
