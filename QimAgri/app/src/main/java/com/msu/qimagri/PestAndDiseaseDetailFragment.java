
package com.msu.qimagri;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Lifecycle;
import androidx.core.view.MenuHost;
import androidx.core.view.MenuProvider;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import com.msu.qimagri.util.DatabaseHelper;

public class PestAndDiseaseDetailFragment extends Fragment {

    private static final String ARGUMENT_ID = "id";
    private static final String ARGUMENT_NAME = "name";
    private static final String ARGUMENT_TYPE = "type";
    private static final String ARGUMENT_DESCRIPTION = "description";
    private static final String ARGUMENT_IMAGE_NAME = "image_name";
    private static final String ARGUMENT_NATURAL_TREATMENT_ID = "natural_treatment_id";

    private DatabaseHelper dbHelper;

    public static PestAndDiseaseDetailFragment newInstance(int id, String name, String type, String description, String imageName, int naturalTreatmentId) {
        PestAndDiseaseDetailFragment fragment = new PestAndDiseaseDetailFragment();
        Bundle arguments = new Bundle();
        arguments.putInt(ARGUMENT_ID, id);
        arguments.putString(ARGUMENT_NAME, name);
        arguments.putString(ARGUMENT_TYPE, type);
        arguments.putString(ARGUMENT_DESCRIPTION, description);
        arguments.putString(ARGUMENT_IMAGE_NAME, imageName);
        arguments.putInt(ARGUMENT_NATURAL_TREATMENT_ID, naturalTreatmentId);
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
        return inflater.inflate(R.layout.fragment_pest_and_disease_detail, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        TextView textName = view.findViewById(R.id.detail_name);
        TextView textType = view.findViewById(R.id.detail_type);
        TextView textDescription = view.findViewById(R.id.detail_description);
        ImageView viewImage = view.findViewById(R.id.detail_image);
        Button buttonToNaturalTreatment = view.findViewById(R.id.button_to_natural_treatment);
        AppCompatActivity activity = (AppCompatActivity) getActivity();

        if (getArguments() != null && activity != null) {
            int id = getArguments().getInt(ARGUMENT_ID);
            String name = getArguments().getString(ARGUMENT_NAME);
            String type = getArguments().getString(ARGUMENT_TYPE);
            String description = getArguments().getString(ARGUMENT_DESCRIPTION);
            String imageName = getArguments().getString(ARGUMENT_IMAGE_NAME);
            int naturalTreatmentId = getArguments().getInt(ARGUMENT_NATURAL_TREATMENT_ID);

            activity.getSupportActionBar().setTitle(name);
            textName.setText(name);
            textType.setText("Type: " + type);
            textDescription.setText(description);
            int resId = getContext().getResources().getIdentifier(imageName, "drawable", getContext().getPackageName());
            viewImage.setImageResource(resId != 0 ? resId : R.drawable.damaged_leaf);

            SQLiteDatabase db = dbHelper.getReadableDatabase();
            Cursor cursor = db.query(DatabaseHelper.TABLE_NATURAL_TREATMENT, new String[]{DatabaseHelper.COLUMN_TREATMENT_NAME}, DatabaseHelper.COLUMN_TREATMENT_ID + "=?", new String[]{String.valueOf(naturalTreatmentId)}, null, null, null);
            if (cursor.moveToFirst()) {
                buttonToNaturalTreatment.setText(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_TREATMENT_NAME)));
            }
            cursor.close();

            buttonToNaturalTreatment.setOnClickListener(v -> {
                Cursor treatmentCursor = db.query(DatabaseHelper.TABLE_NATURAL_TREATMENT, null, DatabaseHelper.COLUMN_TREATMENT_ID + "=?", new String[]{String.valueOf(naturalTreatmentId)}, null, null, null);
                if (treatmentCursor.moveToFirst()) {
                    NaturalTreatmentDetailFragment detailFragment = NaturalTreatmentDetailFragment.newInstance(
                            treatmentCursor.getInt(treatmentCursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_TREATMENT_ID)),
                            treatmentCursor.getString(treatmentCursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_TREATMENT_NAME)),
                            treatmentCursor.getString(treatmentCursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_TREATMENT_DESCRIPTION)),
                            treatmentCursor.getString(treatmentCursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_TREATMENT_IMAGE)),
                            treatmentCursor.getInt(treatmentCursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_TREATMENT_PEST_DISEASE_ID))
                    );
                    getParentFragmentManager().beginTransaction()
                            .replace(R.id.fragment_frame_layout, detailFragment)
                            .addToBackStack(null)
                            .commit();
                }
                treatmentCursor.close();
            });
        }
    }
}
