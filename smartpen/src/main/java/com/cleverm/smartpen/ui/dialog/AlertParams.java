package com.cleverm.smartpen.ui.dialog;

import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.DialogInterface.OnDismissListener;
import android.text.TextUtils;

/**
 * Created by Jimmy on 2016/5/25.
 */
public class AlertParams {

    public Context mContext;
    public CharSequence mTitle;
    public CharSequence mMessage;
    public CharSequence mPositiveButtonText;
    public OnClickListener mPositiveButtonListener;
    public CharSequence mNeutralButtonText;
    public OnClickListener mNeutralButtonListener;
    public CharSequence mNegativeButtonText;
    public OnClickListener mNegativeButtonListener;
    public OnDismissListener mOnDismissListener;
    public boolean mCancelable;

    public AlertParams(Context context) {
        mContext = context;
    }

    public void apply(BaseAlertDialog dialog) {
        if (!TextUtils.isEmpty(mTitle)) {
            dialog.setTitle(mTitle);
        }

        if (!TextUtils.isEmpty(mMessage)) {
            dialog.setMessage(mMessage);
        }

        if (!TextUtils.isEmpty(mPositiveButtonText)) {
            dialog.setButton(DialogInterface.BUTTON_POSITIVE, mPositiveButtonText, mPositiveButtonListener, null);
        }

        if (!TextUtils.isEmpty(mNegativeButtonText)) {
            dialog.setButton(DialogInterface.BUTTON_NEGATIVE, mNegativeButtonText, mNegativeButtonListener, null);
        }

        if (!TextUtils.isEmpty(mNeutralButtonText)) {
            dialog.setButton(DialogInterface.BUTTON_NEUTRAL, mNeutralButtonText, mNeutralButtonListener, null);
        }
    }
}
