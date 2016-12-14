package com.cleverm.smartpen.ui.windows.engineer;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.graphics.Point;
import android.os.Looper;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.FrameLayout;
import android.widget.ListView;

import com.cleverm.smartpen.application.SmartPenApplication;
import com.cleverm.smartpen.util.QuickUtils;
import com.cleverm.smartpen.util.WeakHandler;
import com.nhaarman.listviewanimations.swinginadapters.prepared.ScaleInAnimationAdapter;
import com.nhaarman.listviewanimations.swinginadapters.prepared.SwingRightInAnimationAdapter;

/**
 * Created by xiong,An android project Engineer,on 19/8/2016.
 * Data:19/8/2016  上午 11:13
 * Base on clever-m.com(JAVA Service)
 * Describe:
 * Version:1.0
 * Open source
 */
public class EngineerModeView extends FrameLayout {

    WindowManager windowManager;
    private WindowManager.LayoutParams wmParams;
    private Context context;
    EngineerAdapter adapter;
    ListView listView;


    public EngineerModeView() {
        super(SmartPenApplication.getApplication());
        context=SmartPenApplication.getApplication();
        WindowManager windowManager = getWindowManager();
        Point windowDimen = new Point();
        windowManager.getDefaultDisplay().getSize(windowDimen);

        //Logging console
        listView= new ListView(context);
        listView.setBackgroundColor(Color.parseColor("#64000000"));
        listView.setTranscriptMode(AbsListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
        listView.setStackFromBottom(true);
        listView.setDividerHeight(0);

        adapter = new EngineerAdapter(context);
        ScaleInAnimationAdapter animationAdapter = new ScaleInAnimationAdapter(adapter);
        animationAdapter.setAbsListView(listView);
        listView.setAdapter(animationAdapter);

        FrameLayout.LayoutParams listViewLayoutParams =
                new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT);
        listView.setLayoutParams(listViewLayoutParams);

        //add views
        addView(listView);


        //set  View parameters
        int layoutHeight =  dpToPx(context,800);
        WindowManager.LayoutParams windowParams =
                new WindowManager.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT,
                        WindowManager.LayoutParams.TYPE_TOAST, WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE|WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
                        | WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                        PixelFormat.TRANSLUCENT);

        windowParams.gravity = Gravity.TOP | Gravity.LEFT;
        windowParams.x = 0;
        windowParams.y = windowDimen.y;


        //attach
        windowManager.addView(this,windowParams);

    }


    public void add() {
        //wm = getWindowManager();
        wmParams = new WindowManager.LayoutParams();
        wmParams.type = WindowManager.LayoutParams.TYPE_TOAST;
        wmParams.format = PixelFormat.RGBA_8888;
        wmParams.gravity= Gravity.BOTTOM|Gravity.RIGHT;
        wmParams.flags =
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
                        |WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
                        | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
        //wmParams.width = viewWidth;
        //wmParams.height = viewHeight;
        //wm.addView(this, wmParams);
    }

    public void log(final String message){
          updateAdapter(message);
    }

    private void updateAdapter(String message){
        if(adapter.getItems().size()==10000){
            adapter.getItems().clear();
        }
        adapter.getItems().add(message);
        adapter.notifyDataSetChanged();
    }



    private  WindowManager getWindowManager() {
        if (windowManager == null) {
            windowManager = (WindowManager) SmartPenApplication.getApplication().getSystemService(Context.WINDOW_SERVICE);
        }
        return windowManager;
    }

    public void remove(){
        if(windowManager!=null){
            windowManager.removeView(this);
        }
    }

    private int dpToPx(Context context, int dp) {
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        return dp * (metrics.densityDpi / 160);
    }

}
