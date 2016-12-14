package com.cleverm.smartpen.ui.infinite.Loader;

import android.content.Context;
import android.widget.ImageView;


public interface ImageLoader {

    void initLoader(Context context);

    void load(Context context, ImageView targetView, Object res);

}
