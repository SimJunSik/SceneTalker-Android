package com.scenetalker.yapp.scenetalker;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

import com.android.yapp.scenetalker.R;

public class Chattingroom_Notify_Dialog {
    private Context context;
    Dialog dlg;
    public Chattingroom_Notify_Dialog(Context context) {
        this.context = context;
    }

    public void callFunction(){
        dlg = new Dialog(context);
        dlg.setContentView(R.layout.chattingroom_notify_dialog);
        dlg.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dlg.setCanceledOnTouchOutside(false);
        WindowManager.LayoutParams wm = new WindowManager.LayoutParams();
        wm.copyFrom(dlg.getWindow().getAttributes());
        wm.width=302;
        wm.height=336;


        dlg.show();

        final Button notify_exit_button = dlg.findViewById(R.id.notify_exit_button);
        notify_exit_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dlg.dismiss();
            }
        });


    }
    public void delayTime(long time) {

        new Handler().postDelayed(new Runnable() {

            public void run() {

                dlg.dismiss();

            }

        }, time);
    }
}
