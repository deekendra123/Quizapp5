package com.aptitude.education.e2buddy.Menu;

import android.app.DialogFragment;
import android.os.Bundle;
import androidx.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.aptitude.education.e2buddy.R;

public class Merchandise_Dialog  extends DialogFragment {

    private Callback callback;
    TextView tv1,tv2,tv3,tv4,tv5,tv6,tv7,tv8,tv9,tv10;
    public static Merchandise_Dialog newInstance() {
        return new Merchandise_Dialog();
    }

    public void setCallback(Callback callback) {
        this.callback = callback;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(android.app.DialogFragment.STYLE_NORMAL, R.style.FullscreenDialogTheme);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.merchandise_bottem_sheet_dialog, container, false);
        ImageButton closeDialog = view.findViewById(R.id.fullscreen_dialog_close);

        tv1 = view.findViewById(R.id.tv1);
        tv2 = view.findViewById(R.id.tv2);
        tv3 = view.findViewById(R.id.tv3);
        tv4 = view.findViewById(R.id.tv4);
        tv5 = view.findViewById(R.id.tv5);
        tv6 = view.findViewById(R.id.tv6);
        tv7 = view.findViewById(R.id.tv7);
        tv8 = view.findViewById(R.id.tv8);
        tv9 = view.findViewById(R.id.tv9);
        tv10 = view.findViewById(R.id.tv10);



        closeDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });


        return view;
    }

    public interface Callback {

        void onActionClick(String name);

    }



}
