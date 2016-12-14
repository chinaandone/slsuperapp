package com.cleverm.smartpen.ui.dialog;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Handler;
import android.os.Message;

import java.lang.ref.WeakReference;

/**
 * Created by Jimmy on 2015/8/14.
 */
public class BaseDialog extends DialogFragment {

    @SuppressWarnings("unused")
    private static final String TAG = BaseDialog.class.getSimpleName();

    private static final int DISMISS = 0x43;

    private final Handler mListenersHandler = new ListenersHandler(getDialog());
    private Message mDismissMessage;

    private static final class ListenersHandler extends Handler {
        private WeakReference<DialogInterface> mDialog;

        public ListenersHandler(Dialog dialog) {
            mDialog = new WeakReference<DialogInterface>(dialog);
        }

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case DISMISS: {
                    ((DialogInterface.OnDismissListener) msg.obj).onDismiss(mDialog.get());
                    break;
                }
                default:
                    break;
            }
        }
    }

    public void setOnDismissListener(final DialogInterface.OnDismissListener listener) {
        if (listener != null) {
            mDismissMessage = mListenersHandler.obtainMessage(DISMISS, listener);
        } else {
            mDismissMessage = null;
        }
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        if (mDismissMessage != null) {
            // Obtain a new message so this dialog can be re-used
            Message.obtain(mDismissMessage).sendToTarget();
        }
    }
}
