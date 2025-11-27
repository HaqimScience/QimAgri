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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.widget.SearchView;
import java.util.List;
import java.util.ArrayList;
import android.util.Log;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Scanner;
// Class name
public class PestAndDiseaseFragment extends Fragment {
    // Attributes
    private RecyclerView recyclerView;
    private PestAndDiseaseAdapter adapter;
    private List<PestAndDiseaseItem> pestAndDiseaseItemList = new ArrayList<>();
    private SearchView searchView;

    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_pest_and_disease, container, false);
    }
    @Override
    public void onViewCreated(@NonNull View view,
                              @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        if(activity != null && activity.getSupportActionBar() != null){
            activity.getSupportActionBar().setTitle("Pests and Diseases");
        }
        recyclerView = view.findViewById(R.id.list_pest_and_disease);
        searchView = view.findViewById(R.id.search_bar_pest_and_disease);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        loadTheDataFromJSON();
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
    // Load the data from JSON file
    private void loadTheDataFromJSON() {
        try {
            pestAndDiseaseItemList.clear();
            InputStream is = getResources().openRawResource(R.raw.data);
            Scanner scanner = new Scanner(is).useDelimiter("\\A");
            String json = scanner.hasNext() ? scanner.next() : "";
            scanner.close();

            JSONObject root = new JSONObject(json);
            JSONArray pestsArray = root.getJSONArray("pests_and_diseases");

            for (int i = 0; i < pestsArray.length(); i++) {
                JSONObject obj = pestsArray.getJSONObject(i);
                pestAndDiseaseItemList.add(new PestAndDiseaseItem(
                        obj.getInt("id"),
                        obj.getString("name"),
                        obj.getString("type"),
                        obj.getString("description"),
                        obj.getString("image_name"),
                        obj.getInt("natural_treatment_id")
                ));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    // Search filter
    private void filterList(String text) {
        List<PestAndDiseaseItem> filteredList = new ArrayList<>();
        for (PestAndDiseaseItem item : pestAndDiseaseItemList) {
            if (item.getName().toLowerCase().contains(text.toLowerCase()) ||
                    item.getDescription().toLowerCase().contains(text.toLowerCase())) {
                filteredList.add(item);
            }
        }
        adapter.updateList(filteredList);
    }

}