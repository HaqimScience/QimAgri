// Package name
package com.msu.qimagri;
// Libraries
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;
import java.util.ArrayList;
// Class name
public class NaturalTreatmentAdapter extends RecyclerView.Adapter<NaturalTreatmentAdapter.ViewHolder>{
    // Attributes
    private Context context;
    private List<NaturalTreatmentItem> naturalTreatmentItemList;
    private List<NaturalTreatmentItem> naturalTreatmentItemListBackup;
    // Constructor declaring context and list
    public NaturalTreatmentAdapter(Context context, List<NaturalTreatmentItem> naturalTreatmentItemList){
        this.context = context;
        this.naturalTreatmentItemList = naturalTreatmentItemList;
        this.naturalTreatmentItemListBackup = new ArrayList<>(naturalTreatmentItemList);
    }
    // View Holder
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType){
        View view = LayoutInflater.from(context).inflate(R.layout.row_natural_treatment, parent, false);
        return new ViewHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        NaturalTreatmentItem naturalTreatmentItem = naturalTreatmentItemList.get(position);
        // Load Id
        int resId = context.getResources().getIdentifier(naturalTreatmentItem.getImageName(), "drawable", context.getPackageName());
        // Load name
        holder.name.setText(naturalTreatmentItem.getName());
        // Load image using
        holder.imageName.setImageResource(resId != 0 ? resId : R.drawable.damaged_leaf);
        // Detect click on a row
        holder.itemView.setOnClickListener(v -> {
            NaturalTreatmentDetailFragment detailFragment = NaturalTreatmentDetailFragment.newInstance(
                    naturalTreatmentItem.getId(),
                    naturalTreatmentItem.getName(),
                    naturalTreatmentItem.getDescription(),
                    naturalTreatmentItem.getImageName(),
                    naturalTreatmentItem.getPestAndDiseaseId()
            );
            ((AppCompatActivity) context).getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_frame_layout, detailFragment)
                    .addToBackStack(null)
                    .commit();

        });
    }
    // Get total items inside the list
    @Override
    public int getItemCount() {
        return naturalTreatmentItemList.size();
    }
    // View Holder
    public static class ViewHolder extends RecyclerView.ViewHolder {
        // Attributes from JSON data file
        TextView name;
        ImageView imageName;

        // Constructor
        public ViewHolder(@NonNull View naturalTreatmentView) {
            super(naturalTreatmentView);
            name = naturalTreatmentView.findViewById(R.id.name_natural_treatment);
            imageName = naturalTreatmentView.findViewById(R.id.image_name_natural_treatment);
        }
        // Set attributes names
    }
    // Search filter
    public void filter(String text) {
        naturalTreatmentItemList.clear();
        if (text == null || text.trim().isEmpty()) {
            naturalTreatmentItemList.addAll(naturalTreatmentItemListBackup);
        } else {
            String query = text.toLowerCase();
            for (NaturalTreatmentItem item : naturalTreatmentItemListBackup) {
                if (item.getName().toLowerCase().contains(query)) {
                    naturalTreatmentItemList.add(item);
                }
            }
        }
        notifyDataSetChanged();
    }
    public void updateList(List<NaturalTreatmentItem> newList){
        naturalTreatmentItemList.clear();
        naturalTreatmentItemList.addAll(newList);
        notifyDataSetChanged();
    }
}
