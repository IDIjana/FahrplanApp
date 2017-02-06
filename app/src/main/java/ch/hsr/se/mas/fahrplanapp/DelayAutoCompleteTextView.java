package ch.hsr.se.mas.fahrplanapp;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.ProgressBar;


public class DelayAutoCompleteTextView extends AutoCompleteTextView {
    private ProgressBar loadingIndicator;

    private final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            DelayAutoCompleteTextView.super.performFiltering((CharSequence) msg.obj, msg.arg1);
        }
    };

    public DelayAutoCompleteTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setLoadingIndicator(ProgressBar progressBar) {
        loadingIndicator = progressBar;
    }

    @Override
    protected void performFiltering(CharSequence text, int keyCode) {
        if (loadingIndicator != null) {
            loadingIndicator.setVisibility(View.VISIBLE);
        }
        mHandler.removeMessages(100);
        mHandler.sendMessageDelayed(mHandler.obtainMessage(100, text), 750);
    }

    @Override
    public void onFilterComplete(int count) {
        if (loadingIndicator != null) {
            loadingIndicator.setVisibility(View.GONE);
        }
        super.onFilterComplete(count);
    }
}
