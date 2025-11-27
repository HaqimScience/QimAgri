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
public class PestAndDiseaseAdapter extends RecyclerView.Adapter<PestAndDiseaseAdapter.ViewHolder>{
    // Attributes
    private Context context;
    private List<PestAndDiseaseItem> pestAndDiseaseItemList;
    private List<PestAndDiseaseItem> pestAndDiseaseItemListBackup;
    // Constructor declaring context and list
    public PestAndDiseaseAdapter(Context context, List<PestAndDiseaseItem> pestAndDiseaseItemList){
        this.context = context;
        this.pestAndDiseaseItemList = pestAndDiseaseItemList;
        this.pestAndDiseaseItemListBackup = new ArrayList<>(pestAndDiseaseItemList);
    }
    // View Holder
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType){
        View view = LayoutInflater.from(context).inflate(R.layout.row_pest_and_disease, parent, false);
        return new ViewHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        PestAndDiseaseItem pestAndDiseaseItem = pestAndDiseaseItemList.get(position);
        // Load Id
        int resId = context.getResources().getIdentifier(pestAndDiseaseItem.getImageName(), "drawable", context.getPackageName());
        // Load name
        holder.name.setText(pestAndDiseaseItem.getName());
        // Load image using
        holder.imageName.setImageResource(resId != 0 ? resId : R.drawable.damaged_leaf);
        // Detect click on a row
        holder.itemView.setOnClickListener(v -> {
            PestAndDiseaseDetailFragment detailFragment = PestAndDiseaseDetailFragment.newInstance(
                    pestAndDiseaseItem.getId(),
                    pestAndDiseaseItem.getName(),
                    pestAndDiseaseItem.getType(),
                    pestAndDiseaseItem.getDescription(),
                    pestAndDiseaseItem.getImageName(),
                    pestAndDiseaseItem.getNaturalTreatmentId()
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
        return pestAndDiseaseItemList.size();
    }
    // View Holder
    public static class ViewHolder extends RecyclerView.ViewHolder {
        // Attributes from JSON data file
        TextView name;
        ImageView imageName;

        // Constructor
        public ViewHolder(@NonNull View pestAndDiseaseView) {
            super(pestAndDiseaseView);
            name = pestAndDiseaseView.findViewById(R.id.name_pest_and_disease);
            imageName = pestAndDiseaseView.findViewById(R.id.image_name_pest_and_disease);
        }
        // Set attributes names
    }
    // Search filter
    public void filter(String text) {
        pestAndDiseaseItemList.clear();
        if (text == null || text.trim().isEmpty()) {
            pestAndDiseaseItemList.addAll(pestAndDiseaseItemListBackup);
        } else {
            String query = text.toLowerCase();
            for (PestAndDiseaseItem item : pestAndDiseaseItemListBackup) {
                if (item.getName().toLowerCase().contains(query)) {
                    pestAndDiseaseItemList.add(item);
                }
            }
        }
        notifyDataSetChanged();
    }
    public void updateList(List<PestAndDiseaseItem> newList){
        pestAndDiseaseItemList.clear();
        pestAndDiseaseItemList.addAll(newList);
        notifyDataSetChanged();
    }
}
