package com.cleverm.smartpen.ui.loading.indicator;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.View;

import com.nineoldandroids.animation.Animator;

import java.lang.ref.WeakReference;
import java.util.List;

/**
 * Created by xiong,An android project Engineer,on 5/5/2016.
 * Data:5/5/2016  下午 02:56
 * Base on clever-m.com(JAVA Service)
 * Describe:
 * Version:1.0
 * Open source
 */
public abstract class BaseIndicatorController {


    private WeakReference<View> mTarget;

    private List<Animator> mAnimators;


    public void setTarget(View target){
        this.mTarget=new WeakReference<View>(target);
    }

    public View getTarget(){
        return mTarget!=null?mTarget.get():null;
    }


    public int getWidth(){
        return getTarget()!=null?getTarget().getWidth():0;
    }

    public int getHeight(){
        return getTarget()!=null?getTarget().getHeight():0;
    }

    public void postInvalidate(){
        if (getTarget()!=null){
            getTarget().postInvalidate();
        }
    }

    /**
     * draw indicator
     * @param canvas
     * @param paint
     */
    public abstract void draw(Canvas canvas,Paint paint);

    /**
     * create animation or animations
     */
    public abstract List<Animator> createAnimation();

    public void initAnimation(){
        mAnimators=createAnimation();
    }

    /**
     * make animation to start or end when target
     * view was be Visible or Gone or Invisible.
     * make animation to cancel when target view
     * be onDetachedFromWindow.
     * @param animStatus
     */
    public void setAnimationStatus(AnimStatus animStatus){
        if (mAnimators==null){
            return;
        }
        int count=mAnimators.size();
        for (int i = 0; i < count; i++) {
            Animator animator=mAnimators.get(i);
            boolean isRunning=animator.isRunning();
            switch (animStatus){
                case START:
                    if (!isRunning){
                        animator.start();
                    }
                    break;
                case END:
                    if (isRunning){
                        animator.end();
                    }
                    break;
                case CANCEL:
                    if (isRunning){
                        animator.cancel();
                    }
                    break;
            }
        }
    }


    public enum AnimStatus{
        START,END,CANCEL
    }



}
