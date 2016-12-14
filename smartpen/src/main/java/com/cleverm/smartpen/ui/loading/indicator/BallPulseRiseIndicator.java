package com.cleverm.smartpen.ui.loading.indicator;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.animation.LinearInterpolator;

import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.ObjectAnimator;
import com.nineoldandroids.animation.PropertyValuesHolder;

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
public class BallPulseRiseIndicator extends BaseIndicatorController{

    @Override
    public void draw(Canvas canvas, Paint paint) {
        float radius=getWidth()/10;
        canvas.drawCircle(getWidth()/4,radius*2,radius,paint);
        canvas.drawCircle(getWidth()*3/4,radius*2,radius,paint);

        canvas.drawCircle(radius,getHeight()-2*radius,radius,paint);
        canvas.drawCircle(getWidth()/2,getHeight()-2*radius,radius,paint);
        canvas.drawCircle(getWidth()-radius,getHeight()-2*radius,radius,paint);
    }

    @Override
    public List<Animator> createAnimation() {
        PropertyValuesHolder rotation6=PropertyValuesHolder.ofFloat("rotationX",0,360);
        ObjectAnimator animator=ObjectAnimator.ofPropertyValuesHolder(getTarget(), rotation6);
        animator.setInterpolator(new LinearInterpolator());
        animator.setRepeatCount(-1);
        animator.setDuration(1500);
        animator.start();
        List<Animator> animators=new ArrayList<Animator>();
        animators.add(animator);
        return animators;
    }
}
