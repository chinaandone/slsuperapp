package com.cleverm.smartpen.ui.loading.indicator;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;

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
public class LineScaleIndicator extends BaseIndicatorController {

    public static final float SCALE=1.0f;

    float[] scaleYFloats=new float[]{SCALE,
            SCALE,
            SCALE,
            SCALE,
            SCALE,};

    @Override
    public void draw(Canvas canvas, Paint paint) {
        float translateX=getWidth()/11;
        float translateY=getHeight()/2;
        for (int i = 0; i < 5; i++) {
            canvas.save();
            canvas.translate((2 + i * 2) * translateX - translateX / 2, translateY);
            canvas.scale(SCALE, scaleYFloats[i]);
            RectF rectF=new RectF(-translateX/2,-getHeight()/2.5f,translateX/2,getHeight()/2.5f);
            canvas.drawRoundRect(rectF, 5, 5, paint);
            canvas.restore();
        }
    }

    @Override
    public List<Animator> createAnimation() {
        List<Animator> animators=new ArrayList<Animator>();
        long[] delays=new long[]{100,200,300,400,500};
        for (int i = 0; i < 5; i++) {
            final int index=i;
            ValueAnimator scaleAnim=ValueAnimator.ofFloat(1, 0.4f, 1);
            scaleAnim.setDuration(1000);
            scaleAnim.setRepeatCount(-1);
            scaleAnim.setStartDelay(delays[i]);
            scaleAnim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    scaleYFloats[index] = (Float) animation.getAnimatedValue();
                    postInvalidate();
                }
            });
            scaleAnim.start();
            animators.add(scaleAnim);
        }
        return animators;
    }

}
