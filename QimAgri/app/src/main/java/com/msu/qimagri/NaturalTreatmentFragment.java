// Package name
package com.msu.qimagri;
// Libraries
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.widget.SearchView;
import java.util.List;
import java.util.ArrayList;
import org.json.JSONArray;
import org.json.JSONObject;
import java.io.InputStream;
import java.util.Scanner;

public class NaturalTreatmentFragment extends Fragment {
    // Attributes
    private RecyclerView recyclerView;
    private NaturalTreatmentAdapter adapter;
    private List<NaturalTreatmentItem> naturalTreatmentItemList = new ArrayList<>();
    private SearchView searchView;

    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_natural_treatment, container, false);
    }
    @Override
    public void onViewCreated(@NonNull View view,
                              @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        if(activity != null && activity.getSupportActionBar() != null){
            activity.getSupportActionBar().setTitle("Natural Treatments");
        }
        recyclerView = view.findViewById(R.id.list_natural_treatment);
        searchView = view.findViewById(R.id.search_bar_natural_treatment);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        loadTheDataFromJSON();
        adapter = new NaturalTreatmentAdapter(getContext(), naturalTreatmentItemList);
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
    // Load the data from JSON file
    private void loadTheDataFromJSON() {
        try {
            naturalTreatmentItemList.clear();
            InputStream is = getResources().openRawResource(R.raw.data);
            Scanner scanner = new Scanner(is).useDelimiter("\\A");
            String json = scanner.hasNext() ? scanner.next() : "";
            scanner.close();

            JSONObject root = new JSONObject(json);
            JSONArray naturalTreatmentsArray = root.getJSONArray("natural_treatments");

            for (int i = 0; i < naturalTreatmentsArray.length(); i++) {
                JSONObject obj = naturalTreatmentsArray.getJSONObject(i);
                naturalTreatmentItemList.add(new NaturalTreatmentItem(
                        obj.getInt("id"),
                        obj.getString("name"),
                        obj.getString("description"),
                        obj.getString("image_name"),
                        obj.getInt("pest_and_disease_id")
                ));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    // Search filter
    private void filterList(String text) {
        List<NaturalTreatmentItem> filteredList = new ArrayList<>();
        for (NaturalTreatmentItem item : naturalTreatmentItemList) {
            if (item.getName().toLowerCase().contains(text.toLowerCase()) ||
                    item.getDescription().toLowerCase().contains(text.toLowerCase())) {
                filteredList.add(item);
            }
        }
        adapter.updateList(filteredList);
    }

}