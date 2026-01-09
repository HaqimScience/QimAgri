
package com.msu.qimagri;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;
import com.msu.qimagri.util.DatabaseHelper;
import java.util.ArrayList;
import java.util.List;

public class PestAndDiseaseFragment extends Fragment {
    private RecyclerView recyclerView;
    private PestAndDiseaseAdapter adapter;
    private List<PestAndDiseaseItem> pestAndDiseaseItemList = new ArrayList<>();
    private SearchView searchView;
    private DatabaseHelper dbHelper;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dbHelper = new DatabaseHelper(getContext());
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_pest_and_disease, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView = view.findViewById(R.id.list_pest_and_disease);
        searchView = view.findViewById(R.id.search_bar_pest_and_disease);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        loadTheDataFromDatabase();
        adapter = new PestAndDiseaseAdapter(getContext(), pestAndDiseaseItemList);
        recyclerView.setAdapter(adapter);
        // Search filter
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                adapter.filter(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.filter(newText);
                return true;
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        if (getActivity() instanceof AppCompatActivity) {
            ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Pests and Diseases");
        }
    }

    private void loadTheDataFromDatabase() {
        pestAndDiseaseItemList.clear();
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor cursor = db.query(DatabaseHelper.TABLE_PEST_DISEASE, null, null, null, null, null, null);
        while (cursor.moveToNext()) {
            int id = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_PD_ID));
            String name = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_PD_NAME));
            String type = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_PD_TYPE));
            String image = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_PD_IMAGE));
            String description = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_PD_DESCRIPTION));
            int naturalTreatmentId = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_PD_NATURAL_TREATMENT_ID));
            pestAndDiseaseItemList.add(new PestAndDiseaseItem(id, name, type, description, image, naturalTreatmentId));
        }
        cursor.close();
    }
}
