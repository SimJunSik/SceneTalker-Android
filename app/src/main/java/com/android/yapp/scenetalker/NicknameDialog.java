package com.android.yapp.scenetalker;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class NicknameDialog {
    private Context context;

    public NicknameDialog(Context context) {
        this.context = context;
    }
    public void callFunction(final TextView changed_nickname){
        final Dialog dlg = new Dialog(context);
        //dlg.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dlg.setContentView(R.layout.nickname_change_dialog);
        dlg.show();

        final EditText nickname_edit = (EditText)dlg.findViewById(R.id.nickname_edit);
        final Button edit_ok_btn = dlg.findViewById(R.id.nickname_dialog_ok_btn);
        final Button edit_cancel_btn = dlg.findViewById(R.id.nickname_dialog_cancel_btn);

        edit_ok_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changed_nickname.setText(nickname_edit.getText().toString());
                dlg.dismiss();
            }
        });
        edit_cancel_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dlg.dismiss();
            }
        });

    }
}
