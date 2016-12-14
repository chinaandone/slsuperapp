package com.cleverm.smartpen.ui.loading.indicator;


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
public class BallZigZagDeflectIndicator extends BallZigZagIndicator {




    @Override
    public List<Animator> createAnimation() {
        List<Animator> animators=new ArrayList<Animator>();
        float startX=getWidth()/6;
        float startY=getWidth()/6;
        for (int i = 0; i < 2; i++) {
            final int index=i;
            ValueAnimator translateXAnim=ValueAnimator.ofFloat(startX,getWidth()-startX,startX,getWidth()-startX,startX);
            if (i==1){
                translateXAnim=ValueAnimator.ofFloat(getWidth()-startX,startX,getWidth()-startX,startX,getWidth()-startX);
            }
            ValueAnimator translateYAnim=ValueAnimator.ofFloat(startY,startY,getHeight()-startY,getHeight()-startY,startY);
            if (i==1){
                translateYAnim=ValueAnimator.ofFloat(getHeight()-startY,getHeight()-startY,startY,startY,getHeight()-startY);
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
