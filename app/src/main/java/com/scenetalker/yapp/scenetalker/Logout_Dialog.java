package com.scenetalker.yapp.scenetalker;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

import com.android.yapp.scenetalker.R;

public class Logout_Dialog {
    private Context context;
    public Logout_Dialog(Context context) {
        this.context = context;
    }

    public void callFunction(){
        final Dialog dlg = new Dialog(context);
        dlg.setContentView(R.layout.logout_layout);
        dlg.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dlg.setCanceledOnTouchOutside(false);
        WindowManager.LayoutParams params = dlg.getWindow().getAttributes();


        dlg.show();



        final Button notify_exit_button_yes= dlg.findViewById(R.id.logout_dilaog_button_yes);
        notify_exit_button_yes.setOnClickListener(new View.OnClickListener() {

            Intent intent;

            @Override
            public void onClick(View view) {
                dlg.dismiss();
                ((Activity) context).finish();
                Intent intent=new Intent(view.getContext(),LoginActivity.class);
                view.getContext().startActivity(intent);

            }
        });

        final Button notify_exit_button_no = dlg.findViewById(R.id.logout_dilaog_button_no);
        notify_exit_button_no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dlg.dismiss();

            }
        });
    }


}
