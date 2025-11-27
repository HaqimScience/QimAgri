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
public class PestAndDiseaseDetailFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARGUMENT_ID = "id";
    private static final String ARGUMENT_NAME = "name";
    private static final String ARGUMENT_TYPE = "type";
    private static final String ARGUMENT_DESCRIPTION = "description";
    private static final String ARGUMENT_IMAGE_NAME = "image_name";
    private static final String ARGUMENT_NATURAL_TREATMENT_ID = "natural_treatment_id";
    public static PestAndDiseaseDetailFragment newInstance(int id, String name, String type, String description, String imageName, int naturalTreatmentId) {
        // Required empty public constructor
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
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_pest_and_disease_detail, container, false);
    }
    @Override
    public void onViewCreated(@NonNull View view,
                             @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        TextView textName = view.findViewById(R.id.detail_name);
        TextView textType = view.findViewById(R.id.detail_type);
        TextView textDescription = view.findViewById(R.id.detail_description);
        ImageView viewImage = view.findViewById(R.id.detail_image);
        Button buttonToNaturalTreatment = view.findViewById(R.id.button_to_natural_treatment);
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        if(getArguments() != null && activity != null){
            int id = getArguments().getInt(ARGUMENT_ID);
            String name = getArguments().getString(ARGUMENT_NAME);
            String type = getArguments().getString(ARGUMENT_TYPE);
            String description = getArguments().getString(ARGUMENT_DESCRIPTION);
            String imageName = getArguments().getString(ARGUMENT_IMAGE_NAME);
            int naturalTreatmentId = getArguments().getInt(ARGUMENT_NATURAL_TREATMENT_ID);

            activity.getSupportActionBar().setTitle(name);
            activity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            textName.setText(name);
            textType.setText("Type: " + type);
            textDescription.setText(description);
            int resId = getContext().getResources().getIdentifier(imageName, "drawable", getContext().getPackageName());
            viewImage.setImageResource(resId != 0 ? resId : R.drawable.damaged_leaf);
            try {
                InputStream is = getResources().openRawResource(R.raw.data);
                Scanner scanner = new Scanner(is).useDelimiter("\\A");
                String json = scanner.hasNext() ? scanner.next() : "";
                scanner.close();

                JSONObject root = new JSONObject(json);
                JSONArray naturalTreatmentArray = root.getJSONArray("natural_treatments");

                for (int i = 0; i < naturalTreatmentArray.length(); i++) {
                    JSONObject obj = naturalTreatmentArray.getJSONObject(i);
                    if (obj.getInt("id") == naturalTreatmentId) {
                        // SET THE BUTTON TEXT HERE
                        buttonToNaturalTreatment.setText(obj.getString("name"));
                        break; // Exit the loop since we found our match
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                buttonToNaturalTreatment.setText("View Treatment"); // Fallback text
            }
            buttonToNaturalTreatment.setOnClickListener(v -> {
                try {
                    InputStream is = getResources().openRawResource(R.raw.data);
                    Scanner scanner = new Scanner(is).useDelimiter("\\A");
                    String json = scanner.hasNext() ? scanner.next() : "";
                    scanner.close();

                    JSONObject root = new JSONObject(json);
                    JSONArray naturalTreatmentArray = root.getJSONArray("natural_treatments");

                    for (int i = 0; i < naturalTreatmentArray.length(); i++) {
                        JSONObject obj = naturalTreatmentArray.getJSONObject(i);
                        if (obj.getInt("id") == naturalTreatmentId) {
                            NaturalTreatmentDetailFragment detailFragment = NaturalTreatmentDetailFragment.newInstance(
                                    obj.getInt("id"),
                                    obj.getString("name"),
                                    obj.getString("description"),
                                    obj.getString("image_name"),
                                    obj.getInt("pest_and_disease_id")
                            );
                            getParentFragmentManager().beginTransaction()
                                    .replace(R.id.fragment_frame_layout, detailFragment)
                                    .addToBackStack(null)
                                    .commit();
                            return;
                        }
                    }
                } catch (Exception e) {
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
    // Add this method to your PestAndDiseaseDetailFragment class

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