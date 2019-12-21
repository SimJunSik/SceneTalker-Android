package com.android.yapp.scenetalker;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.view.WindowManager;
import android.widget.TextView;

public class SweetPotato_Pass_Dialog {
    private Context context;
    public static TextView sweetpotato_pass_textview;
    public SweetPotato_Pass_Dialog(Context context) {
        this.context = context;
    }
    Dialog dlg;

    public void callFunction(){
        dlg = new Dialog(context);
        dlg.setContentView(R.layout.sweetpotato_pass_dialog);
        dlg.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dlg.setCanceledOnTouchOutside(false);
        sweetpotato_pass_textview = dlg.findViewById(R.id.sweet_potato_textView);
        WindowManager.LayoutParams wm = new WindowManager.LayoutParams();
        wm.copyFrom(dlg.getWindow().getAttributes());
        wm.width=302;
        wm.height=336;

        dlg.show();

    }

    public TextView getSweetpotato_pass_textview() {
        return sweetpotato_pass_textview;
    }
    public void delayTime(long time){

        new Handler().postDelayed(new Runnable() {

            public void run() {

                dlg.dismiss();

            }

        }, time);

    }
}
