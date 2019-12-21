package com.android.yapp.scenetalker;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;

public class Chattingroom_Exit_Dialog {
    private Context context;

    public Chattingroom_Exit_Dialog(Context context) {
        this.context = context;
    }

    public void callFunction(){
        final Dialog dlg = new Dialog(context);
        dlg.setContentView(R.layout.chattingroom_exit_dialog);
        dlg.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dlg.setCanceledOnTouchOutside(false);
        WindowManager.LayoutParams params = dlg.getWindow().getAttributes();


        dlg.show();



        final Button notify_exit_button = dlg.findViewById(R.id.exit_dilaog_button);
        notify_exit_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dlg.dismiss();

//                Intent intent = new Intent(context,FeedPage.class);
//                context.startActivity(intent);
                //피드바로가기
            }
        });
    }
}
