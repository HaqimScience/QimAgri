// Package name
package com.msu.qimagri;
// Libraries
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.MenuHost;
import androidx.core.view.MenuProvider;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.Lifecycle;
import org.json.JSONArray;
import org.json.JSONObject;
import java.io.InputStream;
import java.util.Scanner;
// The class
public class NaturalTreatmentDetailFragment extends Fragment {

    // Attributes
    private static final String ARGUMENT_ID = "id";
    private static final String ARGUMENT_NAME = "name";
    private static final String ARGUMENT_DESCRIPTION = "description";
    private static final String ARGUMENT_IMAGE_NAME = "image_name";
    private static final String ARGUMENT_PEST_AND_DISEASE_ID ="pest_and_disease_id";
    public static NaturalTreatmentDetailFragment newInstance(int id, String name, String description, String imageName, int pestAndDiseaseId) {
        // Required empty public constructor
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
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_natural_treatment_detail, container, false);
    }
    @Override
    public void onViewCreated(@NonNull View view,
                              @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        TextView textName = view.findViewById(R.id.detail_name);
        TextView textDescription = view.findViewById(R.id.detail_description);
        ImageView viewImage = view.findViewById(R.id.detail_image);
        Button buttonToPestAndDisease = view.findViewById(R.id.button_to_pest_and_disease);
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        if(getArguments() != null && activity != null){
            int id = getArguments().getInt(ARGUMENT_ID);
            String name = getArguments().getString(ARGUMENT_NAME);
            String description = getArguments().getString(ARGUMENT_DESCRIPTION);
            String imageName = getArguments().getString(ARGUMENT_IMAGE_NAME);
            int pestAndDiseaseId = getArguments().getInt(ARGUMENT_PEST_AND_DISEASE_ID);
            activity.getSupportActionBar().setTitle(name);
            activity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            textName.setText(name);
            textDescription.setText(description);
            int resId = getContext().getResources().getIdentifier(imageName, "drawable", getContext().getPackageName());
            viewImage.setImageResource(resId != 0 ? resId : R.drawable.damaged_leaf);
            try {
                InputStream is = getResources().openRawResource(R.raw.data);
                Scanner scanner = new Scanner(is).useDelimiter("\\A");
                String json = scanner.hasNext() ? scanner.next() : "";
                scanner.close();

                JSONObject root = new JSONObject(json);
                JSONArray pestsAndDiseaseArray = root.getJSONArray("pests_and_diseases");

                for (int i = 0; i < pestsAndDiseaseArray.length(); i++) {
                    JSONObject obj = pestsAndDiseaseArray.getJSONObject(i);
                    if (obj.getInt("id") == pestAndDiseaseId) {
                        // SET THE BUTTON TEXT HERE
                        buttonToPestAndDisease.setText(obj.getString("name"));
                        break; // Exit the loop since we found our match
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                buttonToPestAndDisease.setText("View Treatment"); // Fallback text
            }
            buttonToPestAndDisease.setOnClickListener(v -> {
                try {
                    InputStream is = getResources().openRawResource(R.raw.data);
                    Scanner scanner = new Scanner(is).useDelimiter("\\A");
                    String json = scanner.hasNext() ? scanner.next() : "";
                    scanner.close();

                    JSONObject root = new JSONObject(json);
                    JSONArray pestsAndDiseaseArray = root.getJSONArray("pests_and_diseases");

                    for (int i = 0; i < pestsAndDiseaseArray.length(); i++) {
                        JSONObject obj = pestsAndDiseaseArray.getJSONObject(i);
                        if (obj.getInt("id") == pestAndDiseaseId) {
                            PestAndDiseaseDetailFragment detailFragment = PestAndDiseaseDetailFragment.newInstance(
                                    obj.getInt("id"),
                                    obj.getString("name"),
                                    obj.getString("type"),
                                    obj.getString("description"),
                                    obj.getString("image_name"),
                                    obj.getInt("natural_treatment_id")
                            );
                            getParentFragmentManager().beginTransaction()
                                    .replace(R.id.fragment_frame_layout, detailFragment)
                                    .addToBackStack(null)
                                    .commit();
                            return;
                        }
                    }
                } catch (Exception e) {
                    System.out.println("It aint working");
                    e.printStackTrace();
                    Toast.makeText(getContext(), "Could not find treatment details.", Toast.LENGTH_SHORT).show();
                }
            });
        }
        MenuHost menuHost = requireActivity();
        menuHost.addMenuProvider(new MenuProvider() {
            @Override
            public void onCreateMenu(@NonNull Menu menu, @NonNull MenuInflater menuInflater) {
                // Add menu items here if needed.
            }
            @Override
            public boolean onMenuItemSelected(@NonNull MenuItem menuItem) {
                if (menuItem.getItemId() == android.R.id.home) {
                    requireActivity().getSupportFragmentManager().popBackStack();
                    return true;
                }
                return false;
            }
        }, getViewLifecycleOwner(), Lifecycle.State.RESUMED);
    }
    // Add this method to your NaturalTreatmentDetailFragment class

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        if (activity != null && activity.getSupportActionBar() != null) {
            // Reset the ActionBar to its original state
            activity.getSupportActionBar().setDisplayHomeAsUpEnabled(false);
//            // Optionally, reset the title if your home fragment doesn't set its own title
//            activity.getSupportActionBar().setTitle(R.string.); // Or whatever your default title is
        }
    }
}