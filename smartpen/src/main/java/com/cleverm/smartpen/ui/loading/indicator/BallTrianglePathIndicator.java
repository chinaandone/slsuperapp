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
public class BallTrianglePathIndicator extends BaseIndicatorController {

    float[] translateX=new float[3],translateY=new float[3];

    @Override
    public void draw(Canvas canvas, Paint paint) {
        paint.setStrokeWidth(3);
        paint.setStyle(Paint.Style.STROKE);
        for (int i = 0; i < 3; i++) {
            canvas.save();
            canvas.translate(translateX[i], translateY[i]);
            canvas.drawCircle(0, 0, getWidth() / 10, paint);
            canvas.restore();
        }
    }

    @Override
    public List<Animator> createAnimation() {
        List<Animator> animators=new ArrayList<Animator>();
        float startX=getWidth()/5;
        float startY=getWidth()/5;
        for (int i = 0; i < 3; i++) {
            final int index=i;
            ValueAnimator translateXAnim=ValueAnimator.ofFloat(getWidth()/2,getWidth()-startX,startX,getWidth()/2);
            if (i==1){
                translateXAnim=ValueAnimator.ofFloat(getWidth()-startX,startX,getWidth()/2,getWidth()-startX);
            }else if (i==2){
                translateXAnim=ValueAnimator.ofFloat(startX,getWidth()/2,getWidth()-startX,startX);
            }
            ValueAnimator translateYAnim=ValueAnimator.ofFloat(startY,getHeight()-startY,getHeight()-startY,startY);
            if (i==1){
                translateYAnim=ValueAnimator.ofFloat(getHeight()-startY,getHeight()-startY,startY,getHeight()-startY);
            }else if (i==2){
                translateYAnim=ValueAnimator.ofFloat(getHeight()-startY,startY,getHeight()-startY,getHeight()-startY);
            }

            translateXAnim.setDuration(2000);
            translateXAnim.setInterpolator(new LinearInterpolator());
                translateXAnim.setRepeatCount(-1);
            translateXAnim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    translateX [index]= (Float) animation.getAnimatedValue();
                    postInvalidate();
                }
            });
            translateXAnim.start();

            translateYAnim.setDuration(2000);
            translateYAnim.setInterpolator(new LinearInterpolator());
            translateYAnim.setRepeatCount(-1);
            translateYAnim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    translateY [index]= (Float) animation.getAnimatedValue();
                    postInvalidate();
                }
            });
            translateYAnim.start();

            animators.add(translateXAnim);
            animators.add(translateYAnim);
        }
        return animators;
    }


}
