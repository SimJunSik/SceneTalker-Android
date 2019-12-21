package com.android.yapp.scenetalker;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import org.w3c.dom.Text;

public class Cider_Pass_Dialog {
    private Context context;
    public static TextView cider_pass_textview;
    public Cider_Pass_Dialog(Context context) {
        this.context = context;
    }
    Dialog dlg;
    public void callFunction(){
        dlg = new Dialog(context);
        dlg.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dlg.setCanceledOnTouchOutside(false);

        dlg.setContentView(R.layout.cider_pass_dialog);
        cider_pass_textview = dlg.findViewById(R.id.cider_pass_textView);
        WindowManager.LayoutParams wm = new WindowManager.LayoutParams();
        wm.copyFrom(dlg.getWindow().getAttributes());
        wm.width=302;
        wm.height=336;

        dlg.show();

    }
    public void delayTime(long time){

        new Handler().postDelayed(new Runnable() {

            public void run() {

                dlg.dismiss();

            }

        }, time);

    }




    public TextView getCider_pass_textview() {
        return cider_pass_textview;
    }
}
