package com.pulan.widget;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.StyleRes;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.pulan.eatwhat.R;

/**
 * Created by pulan on 2017/9/4.
 */

public class CommonDialog extends Dialog {

    public CommonDialog(@NonNull Context context) {
        super(context);
    }

    public CommonDialog(@NonNull Context context, @StyleRes int themeResId) {
        super(context, themeResId);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public static class Builder {
        Context mContext;
        String okText;
        String contentText;
        OnClickListener positiveClickListener;

        public Builder(Context context) {
            this.mContext = context;
        }

        public Builder setContext(Context context) {
            this.mContext = context;
            return this;
        }

        public Builder setContent(String str) {
            contentText = str;
            return this;
        }

        public Builder setOk(String str, OnClickListener listener) {
            okText = str;
            positiveClickListener = listener;
            return this;
        }

        public CommonDialog create() {
            //内容文字
            TextView tv_content;
            //取消按钮
            ImageView iv_cancel;
            //确定按钮
            Button btn_ok;

            LayoutInflater inflater = (LayoutInflater) mContext
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            // instantiate the dialog with the custom Theme
            final CommonDialog dialog = new CommonDialog(mContext, R.style.CommonDialog);
            View layout = inflater.inflate(R.layout.layout_dialog_common, null);
            dialog.addContentView(layout, new ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

            //findView
            tv_content = (TextView) layout.findViewById(R.id.tv_content);
            iv_cancel = (ImageView) layout.findViewById(R.id.iv_cancel);
            btn_ok = (Button) layout.findViewById(R.id.btn_ok);

            //取消对话框
            iv_cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //取消对话框
                    dialog.dismiss();
                }
            });

            if (contentText != null) {
                tv_content.setText(contentText);
            }

            if (okText != null) {
                btn_ok.setText(okText);
            }

            if (positiveClickListener != null) {
                btn_ok.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        positiveClickListener.onClick(dialog, Dialog.BUTTON_POSITIVE);
                    }
                });
            }

            dialog.setContentView(layout);
            return dialog;
        }

    }
}
