package com.cleverm.smartpen.ui.banner;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.cleverm.smartpen.R;
import com.cleverm.smartpen.ui.banner.transformer.AccordionPageTransformer;
import com.cleverm.smartpen.ui.banner.transformer.AlphaPageTransformer;
import com.cleverm.smartpen.ui.banner.transformer.CubePageTransformer;
import com.cleverm.smartpen.ui.banner.transformer.DefaultPageTransformer;
import com.cleverm.smartpen.ui.banner.transformer.DepthPageTransformer;
import com.cleverm.smartpen.ui.banner.transformer.FadePageTransformer;
import com.cleverm.smartpen.ui.banner.transformer.FlipPageTransformer;
import com.cleverm.smartpen.ui.banner.transformer.RotatePageTransformer;
import com.cleverm.smartpen.ui.banner.transformer.StackPageTransformer;
import com.cleverm.smartpen.ui.banner.transformer.ZoomCenterPageTransformer;
import com.cleverm.smartpen.ui.banner.transformer.ZoomFadePageTransformer;
import com.cleverm.smartpen.ui.banner.transformer.ZoomPageTransformer;
import com.cleverm.smartpen.ui.banner.transformer.ZoomStackPageTransformer;
import com.nineoldandroids.view.ViewHelper;

import java.util.List;


public class BGABanner extends RelativeLayout {

    private static final String TAG = BGABanner.class.getSimpleName();
    private static final int RMP = LayoutParams.MATCH_PARENT;
    private static final int RWC = LayoutParams.WRAP_CONTENT;
    private static final int LWC = LinearLayout.LayoutParams.WRAP_CONTENT;
    private static final int WHAT_AUTO_PLAY = 1000;
    private BGAViewPager mViewPager;
    private List<View> mViews;
    private List<String> mTips;
    private LinearLayout mPointRealContainerLl;
    private TextView mTipTv;
    private int mCrossNum=30000;//Use Integer.MAX_VALUE will be ANR!
    private boolean mAutoPlayAble = true;
    private boolean mIsAutoPlaying = false;
    private int mAutoPlayInterval = 3000;
    private int mPageChangeDuration = 800;
    private int mPointGravity = Gravity.CENTER_HORIZONTAL | Gravity.BOTTOM;
    private int mPointLeftRightMargin;
    private int mPointTopBottomMargin;
    private int mPointContainerLeftRightPadding;
    private int mTipTextSize;
    private int mTipTextColor = Color.WHITE;
    private int mPointDrawableResId = R.drawable.selector_bgabanner_point;
    private Drawable mPointContainerBackgroundDrawable;


    private BAGInterface mBAGInterface;
    public interface BAGInterface{
        void setNum();
    }
    public void setBAGInterfaceListener(BAGInterface mInterface){
        this.mBAGInterface=mInterface;
    }

    public BGAViewPager getViewPager() {
        return mViewPager;
    }

    private Handler mAutoPlayHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            mViewPager.setCurrentItem(mViewPager.getCurrentItem() + 1,true);
            if(mBAGInterface!=null){
                mBAGInterface.setNum();
            }
            mAutoPlayHandler.sendEmptyMessageDelayed(WHAT_AUTO_PLAY, mAutoPlayInterval);
        }
    };

    public BGABanner(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BGABanner(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initDefaultAttrs(context);
        initCustomAttrs(context, attrs);
        initView(context);
    }

    private void initDefaultAttrs(Context context) {
        mViewPager = new BGAViewPager(context);
        mPointLeftRightMargin = dp2px(context, 3);
        mPointTopBottomMargin = dp2px(context, 6);
        mPointContainerLeftRightPadding = dp2px(context, 10);
        mTipTextSize = sp2px(context, 8);
        mPointContainerBackgroundDrawable = new ColorDrawable(Color.parseColor("#44aaaaaa"));
    }

    private void initCustomAttrs(Context context, AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.BGABanner);
        final int N = typedArray.getIndexCount();
        for (int i = 0; i < N; i++) {
            initCustomAttr(typedArray.getIndex(i), typedArray);
        }
        typedArray.recycle();
    }

    private void initCustomAttr(int attr, TypedArray typedArray) {
        if (attr == R.styleable.BGABanner_banner_pointDrawable) {
            mPointDrawableResId = typedArray.getResourceId(attr, R.drawable.selector_bgabanner_point);
        } else if (attr == R.styleable.BGABanner_banner_pointContainerBackground) {
            mPointContainerBackgroundDrawable = typedArray.getDrawable(attr);
        } else if (attr == R.styleable.BGABanner_banner_pointLeftRightMargin) {
            mPointLeftRightMargin = typedArray.getDimensionPixelSize(attr, mPointLeftRightMargin);
        } else if (attr == R.styleable.BGABanner_banner_pointContainerLeftRightPadding) {
            mPointContainerLeftRightPadding = typedArray.getDimensionPixelSize(attr, mPointContainerLeftRightPadding);
        } else if (attr == R.styleable.BGABanner_banner_pointTopBottomMargin) {
            mPointTopBottomMargin = typedArray.getDimensionPixelSize(attr, mPointTopBottomMargin);
        } else if (attr == R.styleable.BGABanner_banner_pointGravity) {
            mPointGravity = typedArray.getInt(attr, mPointGravity);
        } else if (attr == R.styleable.BGABanner_banner_pointAutoPlayAble) {
            mAutoPlayAble = typedArray.getBoolean(attr, mAutoPlayAble);
        } else if (attr == R.styleable.BGABanner_banner_pointAutoPlayInterval) {
            mAutoPlayInterval = typedArray.getInteger(attr, mAutoPlayInterval);
        } else if (attr == R.styleable.BGABanner_banner_pageChangeDuration) {
            mPageChangeDuration = typedArray.getInteger(attr, mPageChangeDuration);
        } else if (attr == R.styleable.BGABanner_banner_transitionEffect) {
            int ordinal = typedArray.getInt(attr, TransitionEffect.Accordion.ordinal());
            setTransitionEffect(TransitionEffect.values()[ordinal]);
        } else if (attr == R.styleable.BGABanner_banner_tipTextColor) {
            mTipTextColor = typedArray.getColor(attr, mTipTextColor);
        } else if (attr == R.styleable.BGABanner_banner_tipTextSize) {
            mTipTextSize = typedArray.getDimensionPixelSize(attr, mTipTextSize);
        }
    }

    private void initView(Context context) {
        addView(mViewPager, new LayoutParams(RMP, RMP));
        setPageChangeDuration(mPageChangeDuration);

        RelativeLayout pointContainerRl = new RelativeLayout(context);
        if (Build.VERSION.SDK_INT >= 16) {
            pointContainerRl.setBackground(mPointContainerBackgroundDrawable);
        } else {
            pointContainerRl.setBackgroundDrawable(mPointContainerBackgroundDrawable);
        }
        pointContainerRl.setPadding(mPointContainerLeftRightPadding, 0, mPointContainerLeftRightPadding, 0);

        //点
        LayoutParams pointContainerLp = new LayoutParams(RMP, RWC);


        // 处理圆点在顶部还是底部
        if ((mPointGravity & Gravity.VERTICAL_GRAVITY_MASK) == Gravity.TOP) {
            pointContainerLp.addRule(RelativeLayout.ALIGN_PARENT_TOP);
        } else {
            pointContainerLp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        }
        addView(pointContainerRl, pointContainerLp);

        mPointRealContainerLl = new LinearLayout(context);
        mPointRealContainerLl.setId(R.id.banner_pointContainerId);
        mPointRealContainerLl.setOrientation(LinearLayout.HORIZONTAL);
        LayoutParams pointRealContainerLp = new LayoutParams(RWC, RWC);
        pointContainerRl.addView(mPointRealContainerLl, pointRealContainerLp);

        LayoutParams tipLp = new LayoutParams(RMP, getResources().getDrawable(mPointDrawableResId).getIntrinsicHeight() + 2 * mPointTopBottomMargin);
        mTipTv = new TextView(context);
        mTipTv.setGravity(Gravity.CENTER_VERTICAL);
        mTipTv.setSingleLine(true);
        mTipTv.setEllipsize(TextUtils.TruncateAt.END);
        mTipTv.setTextColor(mTipTextColor);
        mTipTv.setTextSize(TypedValue.COMPLEX_UNIT_PX, mTipTextSize);
        pointContainerRl.addView(mTipTv, tipLp);

        int horizontalGravity = mPointGravity & Gravity.HORIZONTAL_GRAVITY_MASK;
        // 处理圆点在左边、右边还是水平居中
        if (horizontalGravity == Gravity.LEFT) {
            pointRealContainerLp.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
            tipLp.addRule(RelativeLayout.RIGHT_OF, R.id.banner_pointContainerId);
            mTipTv.setGravity(Gravity.CENTER_VERTICAL | Gravity.RIGHT);
        } else if (horizontalGravity == Gravity.RIGHT) {
            pointRealContainerLp.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            tipLp.addRule(RelativeLayout.LEFT_OF, R.id.banner_pointContainerId);
        } else {
            pointRealContainerLp.addRule(RelativeLayout.CENTER_HORIZONTAL);
            tipLp.addRule(RelativeLayout.LEFT_OF, R.id.banner_pointContainerId);
        }
    }

    /**
     * 设置页码切换过程的时间长度
     *
     * @param duration 页码切换过程的时间长度
     */
    public void setPageChangeDuration(int duration) {
        if (duration >= 0 && duration <= 2000) {
            mViewPager.setPageChangeDuration(duration);
        }
    }

    /**
     * 设置每一页的控件和文案
     *
     * @param views 每一页的控件集合
     * @param tips  每一页的提示文案集合
     */
    public void setViewsAndTips(List<View> views, List<String> tips) {

        //xiong add this line code on 2016-03-17 fix one image bug still srcoll bug~~
        if(views.size()<=1){
            mAutoPlayAble=false;
        }


        if (mAutoPlayAble && views.size() < 1) {
            //轮播时一个图片都没有
            throw new IllegalArgumentException("开启指定轮播时至少有一个页面");
        }
        if (tips != null && tips.size() < views.size()) {
            throw new IllegalArgumentException("提示文案数必须等于页面数量");
        }
        mViews = views;
        mTips = tips;



        mViewPager.setAdapter(new PageAdapter());
        mViewPager.setOnPageChangeListener(new ChangePointListener());

        initPoints();
        processAutoPlay();
    }

    public void addOnPageChangeListener(ViewPager.OnPageChangeListener listener) {
        mViewPager.setOnPageChangeListener(listener);
    }

    /**
     * 设置每一页的控件
     *
     * @param views 每一页的控件集合
     */
    public void setViews(List<View> views) {
        setViewsAndTips(views, null);
    }

    /**
     * 设置每一页的提示文案
     *
     * @param tips 提示文案集合
     */
    public void setTips(List<String> tips) {
        if (tips != null && mViews != null && tips.size() < mViews.size()) {
            throw new IllegalArgumentException("提示文案数必须等于页面数量");
        }
        mTips = tips;
    }

    private void initPoints() {
        mPointRealContainerLl.removeAllViews();
        mViewPager.removeAllViews();

        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LWC, LWC);
        lp.setMargins(mPointLeftRightMargin, mPointTopBottomMargin, mPointLeftRightMargin, mPointTopBottomMargin);
        ImageView imageView;
        for (int i = 0; i < mViews.size(); i++) {
            imageView = new ImageView(getContext());
            imageView.setLayoutParams(lp);
            imageView.setImageDrawable(getResources().getDrawable(mPointDrawableResId));
            //imageView.setBackgroundResource(mPointDrawableResId);
            //imageView.setImageResource(mPointDrawableResId);
            mPointRealContainerLl.addView(imageView);
        }
    }

    private void processAutoPlay() {
        if (mAutoPlayAble) {
            int zeroItem =mCrossNum / 2 - (mCrossNum / 2) % mViews.size();
            mViewPager.setCurrentItem(zeroItem,true);

            startAutoPlay();
        } else {
            switchToPoint(0);
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (mAutoPlayAble) {
            switch (ev.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    stopAutoPlay();
                    break;
                case MotionEvent.ACTION_UP:
                case MotionEvent.ACTION_CANCEL:
                    startAutoPlay();
                    break;
            }
        }
        return super.dispatchTouchEvent(ev);
    }

    /**
     * Set the currently selected page.
     *
     * @param item Item index to select
     */
    public void setCurrentItem(int item) {
        if (mAutoPlayAble) {
            int realCurrentItem = mViewPager.getCurrentItem();
            int currentItem = realCurrentItem % mViews.size();
            int offset = item - currentItem;

            // 这里要使用循环递增或递减设置，否则会ANR
            if (offset < 0) {
                for (int i = -1; i >= offset; i--) {
                    mViewPager.setCurrentItem(realCurrentItem + i, true);
                }
            } else if (offset > 0) {
                for (int i = 1; i <= offset; i++) {
                    mViewPager.setCurrentItem(realCurrentItem + i, true);
                }
            }

            stopAutoPlay();
            startAutoPlay();
        } else {
            mViewPager.setCurrentItem(item, true);
        }
    }

    /**
     * 向前移动一个方位
     * @param vpImage
     */
    public void setDec(BGABanner vpImage){
        int realCurrentItem = mViewPager.getCurrentItem();
        int currentItem = realCurrentItem % mViews.size();
        vpImage.setCurrentItem(currentItem - 1);
        if(mBAGInterface!=null){
            mBAGInterface.setNum();
        }
    }

    /**
     * 向后挪动一个方位
     * @param vpImage
     */
    public void setAdd(BGABanner vpImage){
        int realCurrentItem = mViewPager.getCurrentItem();
        int currentItem = realCurrentItem % mViews.size();
        vpImage.setCurrentItem(currentItem + 1);
        if(mBAGInterface!=null){
            mBAGInterface.setNum();
        }
    }

    /**
     * 设置数字
     * @param tv
     */
    public void setNum(TextView tv){
        int realCurrentItem = mViewPager.getCurrentItem();
        int currentItem = realCurrentItem % mViews.size();
        tv.setText((currentItem+1) + "/" +mViews.size());
    }


    @Override
    protected void onVisibilityChanged(View changedView, int visibility) {
        super.onVisibilityChanged(changedView, visibility);
        if (visibility == VISIBLE) {
            startAutoPlay();
        } else if (visibility == INVISIBLE) {
            stopAutoPlay();
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        stopAutoPlay();
    }

    public void startAutoPlay() {
        if (mAutoPlayAble && !mIsAutoPlaying) {
            mIsAutoPlaying = true;
            mAutoPlayHandler.sendEmptyMessageDelayed(WHAT_AUTO_PLAY, mAutoPlayInterval);
        }
    }

    public void stopAutoPlay() {
        if (mAutoPlayAble && mIsAutoPlaying) {
            mIsAutoPlaying = false;
            mAutoPlayHandler.removeMessages(WHAT_AUTO_PLAY);
        }
    }

    private void switchToPoint(int newCurrentPoint) {
        for (int i = 0; i < mPointRealContainerLl.getChildCount(); i++) {
            mPointRealContainerLl.getChildAt(i).setEnabled(false);
        }
        mPointRealContainerLl.getChildAt(newCurrentPoint).setEnabled(true);

        if (mTipTv != null && mTips != null) {
            mTipTv.setText(mTips.get(newCurrentPoint % mTips.size()));
        }
    }

    /**
     * 设置页面也换动画
     *
     * @param effect
     */
    public void setTransitionEffect(TransitionEffect effect) {
        switch (effect) {
            case Default:
                mViewPager.setPageTransformer(true, new DefaultPageTransformer());
                break;
            case Alpha:
                mViewPager.setPageTransformer(true, new AlphaPageTransformer());
                break;
            case Rotate:
                mViewPager.setPageTransformer(true, new RotatePageTransformer());
                break;
            case Cube:
                mViewPager.setPageTransformer(true, new CubePageTransformer());
                break;
            case Flip:
                mViewPager.setPageTransformer(true, new FlipPageTransformer());
                break;
            case Accordion:
                mViewPager.setPageTransformer(true, new AccordionPageTransformer());
                break;
            case ZoomFade:
                mViewPager.setPageTransformer(true, new ZoomFadePageTransformer());
                break;
            case Fade:
                mViewPager.setPageTransformer(true, new FadePageTransformer());
                break;
            case ZoomCenter:
                mViewPager.setPageTransformer(true, new ZoomCenterPageTransformer());
                break;
            case ZoomStack:
                mViewPager.setPageTransformer(true, new ZoomStackPageTransformer());
                break;
            case Stack:
                mViewPager.setPageTransformer(true, new StackPageTransformer());
                break;
            case Depth:
                mViewPager.setPageTransformer(true, new DepthPageTransformer());
                break;
            case Zoom:
                mViewPager.setPageTransformer(true, new ZoomPageTransformer());
                break;
            default:
                break;
        }
    }

    /**
     * 设置自定义页面切换动画
     *
     * @param transformer
     */
    public void setPageTransformer(ViewPager.PageTransformer transformer) {
        if (transformer != null) {
            mViewPager.setPageTransformer(true, transformer);
        }
    }

    private final class PageAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            //无限滑动BUG
            return mAutoPlayAble ? mCrossNum : mViews.size();
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            View view = mViews.get(position % mViews.size());

            // 在destroyItem方法中销毁的话，当只有3页时会有问题
            if (container.equals(view.getParent())) {
                container.removeView(view);
            }
            container.addView(view);
            return view;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }
    }

    private final class ChangePointListener extends BGAViewPager.SimpleOnPageChangeListener {

        @Override
        public void onPageSelected(int position) {



            switchToPoint(position % mViews.size());
        }

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            if (mTipTv != null && mTips != null) {
                if (positionOffset > 0.5) {
                    mTipTv.setText(mTips.get((position + 1) % mTips.size()));
                    ViewHelper.setAlpha(mTipTv, positionOffset);
                } else {
                    ViewHelper.setAlpha(mTipTv, 1 - positionOffset);
                    mTipTv.setText(mTips.get(position % mTips.size()));
                }
            }
            if(mBAGInterface!=null){
                mBAGInterface.setNum();
            }
        }
    }

    public enum TransitionEffect {
        Default,
        Alpha,
        Rotate,
        Cube,
        Flip,
        Accordion,
        ZoomFade,
        Fade,
        ZoomCenter,
        ZoomStack,
        Stack,
        Depth,
        Zoom
    }

    public static int dp2px(Context context, float dpValue) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dpValue, context.getResources().getDisplayMetrics());
    }

    public static int sp2px(Context context, float spValue) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, spValue, context.getResources().getDisplayMetrics());
    }

    private static void debug(String msg) {
        Log.i(TAG, msg);
    }
}