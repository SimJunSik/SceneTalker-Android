package com.scenetalker.yapp.scenetalker;

import android.app.Dialog;
import android.content.Context;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.yapp.scenetalker.R;

import org.w3c.dom.Text;

public class ProfileDialog {
    private Context context;

    public ProfileDialog(Context context) {
        this.context = context;
    }

    public void callFunction(){
        final Dialog dlg = new Dialog(context);

        //dlg.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dlg.setContentView(R.layout.profile_change_dialog);
        dlg.show();

        TextView profile_img_change = dlg.findViewById(R.id.dialog_profile_img_change);
        TextView profile_img_delete = dlg.findViewById(R.id.dialog_profile_img_delete);

        profile_img_change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //이미지 변경
            }
        });

        profile_img_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //이미지 삭제
            }
        });
    }
}
