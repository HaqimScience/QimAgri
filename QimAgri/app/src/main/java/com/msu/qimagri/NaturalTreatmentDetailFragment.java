
package com.msu.qimagri;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import com.msu.qimagri.util.DatabaseHelper;

public class NaturalTreatmentDetailFragment extends Fragment {

    private static final String ARGUMENT_ID = "id";
    private static final String ARGUMENT_NAME = "name";
    private static final String ARGUMENT_DESCRIPTION = "description";
    private static final String ARGUMENT_IMAGE_NAME = "image_name";
    private static final String ARGUMENT_PEST_AND_DISEASE_ID = "pest_and_disease_id";

    private DatabaseHelper dbHelper;

    public static NaturalTreatmentDetailFragment newInstance(int id, String name, String description, String imageName, int pestAndDiseaseId) {
        NaturalTreatmentDetailFragment fragment = new NaturalTreatmentDetailFragment();
        Bundle arguments = new Bundle();
        arguments.putInt(ARGUMENT_ID, id);
        arguments.putString(ARGUMENT_NAME, name);
        arguments.putString(ARGUMENT_DESCRIPTION, description);
        arguments.putString(ARGUMENT_IMAGE_NAME, imageName);
        arguments.putInt(ARGUMENT_PEST_AND_DISEASE_ID, pestAndDiseaseId);
        fragment.setArguments(arguments);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dbHelper = new DatabaseHelper(getContext());
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_natural_treatment_detail, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        TextView textName = view.findViewById(R.id.detail_name);
        TextView textDescription = view.findViewById(R.id.detail_description);
        ImageView viewImage = view.findViewById(R.id.detail_image);
        Button buttonToPestAndDisease = view.findViewById(R.id.button_to_pest_and_disease);
        AppCompatActivity activity = (AppCompatActivity) getActivity();

        if (getArguments() != null && activity != null) {
            int id = getArguments().getInt(ARGUMENT_ID);
            String name = getArguments().getString(ARGUMENT_NAME);
            String description = getArguments().getString(ARGUMENT_DESCRIPTION);
            String imageName = getArguments().getString(ARGUMENT_IMAGE_NAME);
            int pestAndDiseaseId = getArguments().getInt(ARGUMENT_PEST_AND_DISEASE_ID);

            activity.getSupportActionBar().setTitle(name);
            textName.setText(name);
            textDescription.setText(description);
            int resId = getContext().getResources().getIdentifier(imageName, "drawable", getContext().getPackageName());
            viewImage.setImageResource(resId != 0 ? resId : R.drawable.damaged_leaf);

            SQLiteDatabase db = dbHelper.getReadableDatabase();
            Cursor cursor = db.query(DatabaseHelper.TABLE_PEST_DISEASE, new String[]{DatabaseHelper.COLUMN_PD_NAME}, DatabaseHelper.COLUMN_PD_ID + "=?", new String[]{String.valueOf(pestAndDiseaseId)}, null, null, null);
            if (cursor.moveToFirst()) {
                buttonToPestAndDisease.setText(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_PD_NAME)));
            }
            cursor.close();

            buttonToPestAndDisease.setOnClickListener(v -> {
                Cursor pdCursor = db.query(DatabaseHelper.TABLE_PEST_DISEASE, null, DatabaseHelper.COLUMN_PD_ID + "=?", new String[]{String.valueOf(pestAndDiseaseId)}, null, null, null);
                if (pdCursor.moveToFirst()) {
                    PestAndDiseaseDetailFragment detailFragment = PestAndDiseaseDetailFragment.newInstance(
                            pdCursor.getInt(pdCursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_PD_ID)),
                            pdCursor.getString(pdCursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_PD_NAME)),
                            pdCursor.getString(pdCursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_PD_TYPE)),
                            pdCursor.getString(pdCursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_PD_DESCRIPTION)),
                            pdCursor.getString(pdCursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_PD_IMAGE)),
                            pdCursor.getInt(pdCursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_PD_NATURAL_TREATMENT_ID))
                    );
                    getParentFragmentManager().beginTransaction()
                            .replace(R.id.fragment_frame_layout, detailFragment)
                            .addToBackStack(null)
                            .commit();
                }
                pdCursor.close();
            });
        }
    }
}
