package com.cleverm.smartpen.ui.dialog;

import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;


import com.cleverm.smartpen.R;

import java.lang.ref.WeakReference;

/**
 * Created by Jimmy on 2015/8/14.
 */
public abstract class BaseAlertDialog extends BaseDialog {

    @SuppressWarnings("unused")
    private static final String TAG = BaseAlertDialog.class.getSimpleName();

    protected View mDialogView;

    protected CharSequence mTitle;
    protected CharSequence mMessage;
    protected Button mButtonPositive;
    protected CharSequence mButtonPositiveText;
    protected Message mButtonPositiveMessage;
    protected Button mButtonNegative;
    protected CharSequence mButtonNegativeText;
    protected Message mButtonNegativeMessage;
    protected Button mButtonNeutral;
    protected CharSequence mButtonNeutralText;
    protected Message mButtonNeutralMessage;
    private final Handler mHandler = new ButtonHandler(getDialog());

    private final View.OnClickListener mButtonHandler = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            final Message m;
            if (v == mButtonPositive && mButtonPositiveMessage != null) {
                m = Message.obtain(mButtonPositiveMessage);
            } else if (v == mButtonNegative && mButtonNegativeMessage != null) {
                m = Message.obtain(mButtonNegativeMessage);
            } else if (v == mButtonNeutral && mButtonNeutralMessage != null) {
                m = Message.obtain(mButtonNeutralMessage);
            } else {
                m = null;
            }
            if (m != null) {
                m.sendToTarget();
            }
            // Post a message so we dismiss after the above handlers are executed
            mHandler.obtainMessage(ButtonHandler.MSG_DISMISS_DIALOG, getDialog()).sendToTarget();
        }
    };

    private static final class ButtonHandler extends Handler {
        // Button clicks have Message.what as the BUTTON{1,2,3} constant
        private static final int MSG_DISMISS_DIALOG = 1;

        private WeakReference<DialogInterface> mDialog;

        public ButtonHandler(DialogInterface dialog) {
            mDialog = new WeakReference<DialogInterface>(dialog);
        }

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {

                case DialogInterface.BUTTON_POSITIVE:
                case DialogInterface.BUTTON_NEGATIVE:
                case DialogInterface.BUTTON_NEUTRAL:
                    ((DialogInterface.OnClickListener) msg.obj).onClick(mDialog.get(), msg.what);
                    break;

                case MSG_DISMISS_DIALOG:
                    ((DialogInterface) msg.obj).dismiss();
            }
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NO_TITLE, R.style.DefaultDialog);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mDialogView = inflater.inflate(R.layout.dialog_base_alert, container, false);
        setupView();
        return mDialogView;
    }

    private void setupView() {
        setupTitle();
        setupBody();
        setupButtons();
    }

    private void setupTitle() {
        TextView titleTextView = (TextView) mDialogView.findViewById(R.id.tv_title);
        if (TextUtils.isEmpty(mTitle)) {
            titleTextView.setVisibility(View.GONE);
        } else {
            titleTextView.setVisibility(View.VISIBLE);
            titleTextView.setText(mTitle);
        }
    }

    protected abstract void setupBody();

    private void setupButtons() {
        final int BIT_BUTTON_POSITIVE = 1;
        final int BIT_BUTTON_NEGATIVE = 2;
        final int BIT_BUTTON_NEUTRAL = 4;
        int whichButtons = 0;

        mButtonPositive = (Button) mDialogView.findViewById(R.id.btn_positive);
        mButtonPositive.setOnClickListener(mButtonHandler);
        if (TextUtils.isEmpty(mButtonPositiveText)) {
            mButtonPositive.setVisibility(View.GONE);
        } else {
            mButtonPositive.setText(mButtonPositiveText);
            mButtonPositive.setVisibility(View.VISIBLE);
            whichButtons = whichButtons | BIT_BUTTON_POSITIVE;
        }

        mButtonNegative = (Button) mDialogView.findViewById(R.id.btn_negative);
        mButtonNegative.setOnClickListener(mButtonHandler);
        if (TextUtils.isEmpty(mButtonNegativeText)) {
            mButtonNegative.setVisibility(View.GONE);
        } else {
            mButtonNegative.setText(mButtonNegativeText);
            mButtonNegative.setVisibility(View.VISIBLE);
            whichButtons = whichButtons | BIT_BUTTON_NEGATIVE;
        }

        mButtonNeutral = (Button) mDialogView.findViewById(R.id.btn_neutral);
        mButtonNeutral.setOnClickListener(mButtonHandler);
        if (TextUtils.isEmpty(mButtonNeutralText)) {
            mButtonNeutral.setVisibility(View.GONE);
        } else {
            mButtonNeutral.setText(mButtonNeutralText);
            mButtonNeutral.setVisibility(View.VISIBLE);
            whichButtons = whichButtons | BIT_BUTTON_NEUTRAL;
        }

        switch (whichButtons) {
            case BIT_BUTTON_POSITIVE: {
                mButtonPositive.setBackgroundResource(R.drawable.dialog_btn_single);
                break;
            }
            case BIT_BUTTON_NEGATIVE: {
                mButtonNegative.setBackgroundResource(R.drawable.dialog_btn_single);
                break;
            }
            case BIT_BUTTON_NEUTRAL: {
                mButtonNeutral.setBackgroundResource(R.drawable.dialog_btn_single);
                break;
            }
            case BIT_BUTTON_POSITIVE | BIT_BUTTON_NEGATIVE: {
                break;
            }
            case BIT_BUTTON_POSITIVE | BIT_BUTTON_NEUTRAL: {
                mButtonNeutral.setBackgroundResource(R.drawable.dialog_btn_left);
                break;
            }
            case BIT_BUTTON_NEGATIVE | BIT_BUTTON_NEUTRAL: {
                mButtonNeutral.setBackgroundResource(R.drawable.dialog_btn_right);
                break;
            }
            case BIT_BUTTON_POSITIVE | BIT_BUTTON_NEGATIVE | BIT_BUTTON_NEUTRAL: {
                break;
            }
            default:
                break;
        }
    }


    public void setTitle(CharSequence title) {
        mTitle = title;
    }

    public void setMessage(CharSequence message) {
        mMessage = message;
    }

    public void setButton(int whichButton, CharSequence text, DialogInterface.OnClickListener listener, Message msg) {
        if (msg == null && listener != null) {
            msg = mHandler.obtainMessage(whichButton, listener);
        }

        switch (whichButton) {

            case DialogInterface.BUTTON_POSITIVE: {
                mButtonPositiveText = text;
                mButtonPositiveMessage = msg;
                break;
            }
            case DialogInterface.BUTTON_NEGATIVE: {
                mButtonNegativeText = text;
                mButtonNegativeMessage = msg;
                break;
            }
            case DialogInterface.BUTTON_NEUTRAL: {
                mButtonNeutralText = text;
                mButtonNeutralMessage = msg;
                break;
            }
            default:
                throw new IllegalArgumentException("Button does not exist");
        }
    }


    /**
     * An inner class for creating a BaseAlertDialog
     */
    public abstract static class Builder {

        protected AlertParams P;

        public Builder(Context context) {
            P = new AlertParams(context);
        }

        public Builder setTitle(CharSequence title) {
            P.mTitle = title;
            return this;
        }

        public Builder setTitle(int titleId) {
            P.mTitle = P.mContext.getText(titleId);
            return this;
        }

        public Builder setMessage(CharSequence message) {
            P.mMessage = message;
            return this;
        }

        public Builder setMessage(int messageId) {
            P.mMessage = P.mContext.getText(messageId);
            return this;
        }

        public Builder setPositiveButton(CharSequence text, final DialogInterface.OnClickListener listener) {
            P.mPositiveButtonText = text;
            P.mPositiveButtonListener = listener;
            return this;
        }

        public Builder setPositiveButton(int textId, final DialogInterface.OnClickListener listener) {
            P.mPositiveButtonText = P.mContext.getText(textId);
            P.mPositiveButtonListener = listener;
            return this;
        }

        public Builder setNegativeButton(CharSequence text, final DialogInterface.OnClickListener listener) {
            P.mNegativeButtonText = text;
            P.mNegativeButtonListener = listener;
            return this;
        }

        public Builder setNegativeButton(int textId, final DialogInterface.OnClickListener listener) {
            P.mNegativeButtonText = P.mContext.getText(textId);
            P.mNegativeButtonListener = listener;
            return this;
        }

        public Builder setNeutralButton(CharSequence text, final DialogInterface.OnClickListener listener) {
            P.mNeutralButtonText = text;
            P.mNeutralButtonListener = listener;
            return this;
        }

        public Builder setNeutralButton(int textId, final DialogInterface.OnClickListener listener) {
            P.mNeutralButtonText = P.mContext.getText(textId);
            P.mNeutralButtonListener = listener;
            return this;
        }

        public Builder setOnDismissListener(DialogInterface.OnDismissListener onDismissListener) {
            P.mOnDismissListener = onDismissListener;
            return this;
        }

        public Builder setCancelable(boolean cancelable) {
            P.mCancelable = cancelable;
            return this;
        }

        public abstract BaseAlertDialog create();
    }
}
