package com.aptitude.education.e2buddy.Placement_Papers;

import android.app.Dialog;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;

import com.aptitude.education.e2buddy.R;
import com.aptitude.education.e2buddy.databinding.PlacementanswerdialogBinding;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class PlacementPaperAnswerDialog extends BottomSheetDialogFragment {

    BottomSheetBehavior bottomSheetBehavior;
    PlacementanswerdialogBinding bi;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {


        BottomSheetDialog bottomSheet = (BottomSheetDialog) super.onCreateDialog(savedInstanceState);

    //inflating layout
    View view = View.inflate(getContext(), R.layout.placementanswerdialog, null);

    //binding views to data binding.
    bi = DataBindingUtil.bind(view);



        //setting layout with bottom sheet
        bottomSheet.setContentView(view);

    bottomSheetBehavior = BottomSheetBehavior.from((View) (view.getParent()));

        Bundle bundle = getArguments();
        String questionid = bundle.getString("questionid","");

        bi.nameToolbar.setText("Question "+questionid);
        bi.tvQuestionNumber.setText("Question "+questionid);



        //setting Peek at the 16:9 ratio keyline of its parent.
        bottomSheetBehavior.setPeekHeight(BottomSheetBehavior.PEEK_HEIGHT_AUTO);




    //setting max height of bottom sheet
        bi.extraSpace.setMinimumHeight((Resources.getSystem().getDisplayMetrics().heightPixels) / 2);


        bottomSheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
        @Override
        public void onStateChanged(@NonNull View view, int i) {
            if (BottomSheetBehavior.STATE_EXPANDED == i) {
                showView(bi.appBarLayout, getActionBarSize());
                hideAppBar(bi.profileLayout);

            }
            if (BottomSheetBehavior.STATE_COLLAPSED == i) {
                hideAppBar(bi.appBarLayout);
                showView(bi.profileLayout, getActionBarSize());
            }

            if (BottomSheetBehavior.STATE_HIDDEN == i) {
                dismiss();
            }

        }

        @Override
        public void onSlide(@NonNull View view, float v) {

        }
    });

    //aap bar cancel button clicked
        bi.cancelBtn.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            dismiss();
        }
    });



    //hiding app bar at the start
    hideAppBar(bi.appBarLayout);


        return bottomSheet;
}

    @Override
    public void onStart() {
        super.onStart();

        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
    }

    private void hideAppBar(View view) {
        ViewGroup.LayoutParams params = view.getLayoutParams();
        params.height = 0;
        view.setLayoutParams(params);

    }

    private void showView(View view, int size) {
        ViewGroup.LayoutParams params = view.getLayoutParams();
        params.height = size;
        view.setLayoutParams(params);
    }

    private int getActionBarSize() {
        final TypedArray array = getContext().getTheme().obtainStyledAttributes(new int[]{android.R.attr.actionBarSize});
        int size = (int) array.getDimension(0, 0);
        return size;
    }

}
