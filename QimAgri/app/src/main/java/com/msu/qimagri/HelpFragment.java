package com.msu.qimagri;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

public class HelpFragment extends Fragment {

    public HelpFragment() {
        // Required empty public constructor
    }

    public static HelpFragment newInstance() {
        return new HelpFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_help, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Help");
        Button buttonDeveloperInfo = view.findViewById(R.id.button_developer_info);
        Button buttonGuide = view.findViewById(R.id.button_guide);
        Button buttonKeyFeatures = view.findViewById(R.id.button_key_features);
        Button buttonSupportOrContactInfo = view.findViewById(R.id.button_support_or_contact_info);

        View popupMessageBox = getLayoutInflater().inflate(R.layout.popup_message_box, null);
        PopupWindow popupWindow = new PopupWindow(popupMessageBox, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);

        ViewGroup root = (ViewGroup) requireActivity().getWindow().getDecorView().getRootView();
        popupWindow.setOnDismissListener(() -> clearDim(root));

        TextView popupTitle = popupMessageBox.findViewById(R.id.title);
        TextView popupDescription = popupMessageBox.findViewById(R.id.description);
        Button buttonClose = popupMessageBox.findViewById(R.id.button_close);

        buttonDeveloperInfo.setOnClickListener(v -> {
            popupTitle.setText("Developer Info");
            popupDescription.setText("Students of Group One from Management and Science University\n1. Muhammad Harish Haqim Bin Adnan\n2. Afnan Farish Bin Abdul Rahim\n3. Kuvalesan A/L Ravichandran\nLecturer: Dr. Jamal Abdullahi Nuh");
            applyDim(root, 0.5f);
            popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);
        });

        buttonGuide.setOnClickListener(v -> {
            popupTitle.setText("User Guide");
            popupDescription.setText("1. Use the search bar to find pests or diseases by name.\n2. Tap on a list item to view details and treatment\n3. Each pest and disease will have a button to natural treatment\n4. Same goes for natural treatment will have button to pest or disease");
            applyDim(root, 0.5f);
            popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);
        });

        buttonKeyFeatures.setOnClickListener(v -> {
            popupTitle.setText("Key Features");
            popupDescription.setText("1. Search bar to find pests or diseases by name\n2. List of pest and diseases\n3. List of natural treatments\n4. Button to pest or disease\n5. Button to natural treatment\n6. Help menu\n7. Static data for pest and disease and natural treatments\n8.Image rich descriptions");
            applyDim(root, 0.5f);
            popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);
        });

        buttonSupportOrContactInfo.setOnClickListener(v -> {
            popupTitle.setText("Support & Contact Info");
            popupDescription.setText("Email address: qimagri@outlook.com\nPhone number: +601121117352");
            applyDim(root, 0.5f);
            popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);
        });

        if (buttonClose != null) {
            buttonClose.setOnClickListener(v -> popupWindow.dismiss());
        }

        AppCompatActivity activity = (AppCompatActivity) getActivity();
        if (activity != null && activity.getSupportActionBar() != null) {
            activity.getSupportActionBar().setTitle("Help");
        }
    }

    private void applyDim(@NonNull ViewGroup parent, float dimAmount) {
        Drawable dim = new ColorDrawable(Color.BLACK);
        dim.setBounds(0, 0, parent.getWidth(), parent.getHeight());
        dim.setAlpha((int) (255 * dimAmount));
        parent.getOverlay().add(dim);
    }

    private void clearDim(@NonNull ViewGroup parent) {
        parent.getOverlay().clear();
    }
}
