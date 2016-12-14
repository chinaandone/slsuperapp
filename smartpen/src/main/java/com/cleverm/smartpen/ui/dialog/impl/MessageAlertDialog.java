package com.cleverm.smartpen.ui.dialog.impl;

import android.content.Context;
import android.content.DialogInterface;
import android.text.TextUtils;
import android.view.ViewStub;
import android.widget.TextView;

import com.cleverm.smartpen.R;
import com.cleverm.smartpen.ui.dialog.BaseAlertDialog;


/**
 * Created by Jimmy on 2015/8/14.
 */
public class MessageAlertDialog extends BaseAlertDialog {

    @SuppressWarnings("unused")
    private static final String TAG = MessageAlertDialog.class.getSimpleName();


    @Override
    protected void setupBody() {
        if (TextUtils.isEmpty(mMessage)) {
            return;
        }

        ViewStub viewStub = (ViewStub) mDialogView.findViewById(R.id.body_stub);
        viewStub.setLayoutResource(R.layout.layout_message_alert_dialog_body);
        viewStub.inflate();

        ((TextView) mDialogView.findViewById(R.id.message)).setText(mMessage);
    }

    public static final class Builder extends BaseAlertDialog.Builder {

        public Builder(Context context) {
            super(context);
        }

        public Builder setTitle(CharSequence title) {
            super.setTitle(title);
            return this;
        }

        public Builder setTitle(int titleId) {
            super.setTitle(titleId);
            return this;
        }

        public Builder setMessage(CharSequence message) {
            super.setMessage(message);
            return this;
        }

        public Builder setMessage(int messageId) {
            super.setMessage(messageId);
            return this;
        }

        public Builder setPositiveButton(CharSequence text, final DialogInterface.OnClickListener listener) {
            super.setPositiveButton(text, listener);
            return this;
        }

        public Builder setPositiveButton(int textId, final DialogInterface.OnClickListener listener) {
            super.setPositiveButton(textId, listener);
            return this;
        }

        public Builder setNegativeButton(CharSequence text, final DialogInterface.OnClickListener listener) {
            super.setNegativeButton(text, listener);
            return this;
        }

        public Builder setNegativeButton(int textId, final DialogInterface.OnClickListener listener) {
            super.setNegativeButton(textId, listener);
            return this;
        }

        public Builder setNeutralButton(CharSequence text, final DialogInterface.OnClickListener listener) {
            super.setNeutralButton(text, listener);
            return this;
        }

        public Builder setNeutralButton(int textId, final DialogInterface.OnClickListener listener) {
            super.setNeutralButton(textId, listener);
            return this;
        }

        public Builder setOnDismissListener(DialogInterface.OnDismissListener onDismissListener) {
            super.setOnDismissListener(onDismissListener);
            return this;
        }

        public Builder setCancelable(boolean cancelable) {
            super.setCancelable(cancelable);
            return this;
        }

        @Override
        public MessageAlertDialog create() {
            MessageAlertDialog dialog = new MessageAlertDialog();
            P.apply(dialog);
            dialog.setCancelable(P.mCancelable);
            dialog.setOnDismissListener(P.mOnDismissListener);
            return dialog;
        }
    }
}
