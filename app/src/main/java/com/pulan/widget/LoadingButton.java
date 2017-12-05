package com.pulan.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.pulan.eatwhat.R;
import com.pulan.util.PuLanUtil;

/**
 * Created by pulan on 17/11/30.
 */

public class LoadingButton extends RelativeLayout {

    Context mContext;

    OverWatchLoadingView loadingView;
    ImageView imageView;
    TextView textView;
    String text;

    final static int MSG_START_ANIM = 0x1001;//点击后开始动画
    Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case MSG_START_ANIM:
                    //隐藏iamgeView
                    imageView.setVisibility(GONE);
                    textView.setVisibility(GONE);
                    loadingView.start();
                    break;
                default:
                    break;
            }
        }
    };

    public LoadingButton(Context context) {
        this(context, null);
    }

    public LoadingButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.LoadingButton);
        text = typedArray.getString(R.styleable.LoadingButton_text);
        initView(context);
    }

    private void initView(Context context) {
        setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                PuLanUtil.dip2px(context, 80)));

        //按钮背景
        imageView = new ImageView(context);
        LayoutParams imageParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                PuLanUtil.dip2px(context, 48));
        imageParams.setMargins(PuLanUtil.dip2px(context, 16),
                PuLanUtil.dip2px(context, 16),
                PuLanUtil.dip2px(context, 16),
                PuLanUtil.dip2px(context, 16));
        imageParams.addRule(CENTER_IN_PARENT, TRUE);
        imageView.setLayoutParams(imageParams);
        //设置背景
        imageView.setBackgroundResource(R.drawable.bkg_btn_normal);
        addView(imageView);

        //加载动画
        loadingView = new OverWatchLoadingView(context, null);
        LayoutParams loadingViewParams = new LayoutParams(PuLanUtil.dip2px(context, 56),
                PuLanUtil.dip2px(context, 56));
        loadingViewParams.addRule(CENTER_IN_PARENT, TRUE);
        loadingView.setLayoutParams(loadingViewParams);
        addView(loadingView);

        //按钮文字
        textView = new TextView(context);
        LayoutParams tvParams = new LayoutParams(LayoutParams.WRAP_CONTENT,
                LayoutParams.WRAP_CONTENT);
        tvParams.addRule(CENTER_IN_PARENT, TRUE);
        textView.setLayoutParams(tvParams);
        //18sp
        textView.setTextSize(18);
        if (text != null && !text.equals("")) {
            textView.setText(text);
        }
        textView.setTextColor(getResources().getColor(R.color.font_black));
        addView(textView);

        //点击事件
        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null) {
                    mListener.onClick(v);
                    handler.sendEmptyMessage(MSG_START_ANIM);
                }
            }
        });
    }

    @Override
    public void setOnClickListener(@Nullable OnClickListener l) {
        super.setOnClickListener(l);
    }

    ClickListener mListener;

    public interface ClickListener {
        void onClick(View view);
    }

    public void setClickListener(ClickListener listener) {
        this.mListener = listener;
    }

    /**
     * 停止加载
     */
    public void stopAnimation() {
        if (loadingView != null) {
            loadingView.end();
        }
    }
}
