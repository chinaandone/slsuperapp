package com.cleverm.smartpen.ui.loading.indicator;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
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
public class SquareSpinIndicator extends BaseIndicatorController {

    @Override
    public void draw(Canvas canvas, Paint paint) {
        canvas.drawRect(new RectF(getWidth()/5,getHeight()/5,getWidth()*4/5,getHeight()*4/5),paint);
    }

    @Override
    public List<Animator> createAnimation() {
        List<Animator> animators=new ArrayList<Animator>();
        PropertyValuesHolder rotation5=PropertyValuesHolder.ofFloat("rotationX",0,180,180,0,0);
        PropertyValuesHolder rotation6=PropertyValuesHolder.ofFloat("rotationY",0,0,180,180,0);
        ObjectAnimator animator=ObjectAnimator.ofPropertyValuesHolder(getTarget(), rotation6,rotation5);
        animator.setInterpolator(new LinearInterpolator());
        animator.setRepeatCount(-1);
        animator.setDuration(2500);
        animator.start();
        animators.add(animator);
        return animators;
    }
}
