package com.android.yapp.scenetalker;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class PasswordCheckDialog {
    TextView pwemail_ok;
    private Context context;

    public PasswordCheckDialog(Context context) {
        this.context = context;
    }

    public void callFunction(){
        final Dialog dlg = new Dialog(context);

        //dlg.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dlg.setContentView(R.layout.password_check_dialog);
        dlg.show();

        pwemail_ok= dlg.findViewById(R.id.password_dialog_email_ok);

        pwemail_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dlg.dismiss();

                Intent intent = new Intent(context,PasswordChangeEmailActivity.class);
                context.startActivity(intent);
            }
        });
    }
}
