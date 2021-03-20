package com.example.myfitnessapp.Adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myfitnessapp.Classes.AcievementsTagsModel;
import com.example.myfitnessapp.Classes.RewardsModel;
import com.example.myfitnessapp.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class AchivementsTags extends RecyclerView.Adapter<AchivementsTags.AchievementsViewHolder> {

    private List<AcievementsTagsModel> AchievementItems;
    Context context;

    public AchivementsTags(List<AcievementsTagsModel> achievementItems, Context context) {
        AchievementItems = achievementItems;
        this.context = context;
    }

    @NonNull
    @Override
    public AchievementsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new AchivementsTags.AchievementsViewHolder(
                LayoutInflater.from(parent.getContext()).inflate(
                        R.layout.single_achievements_tags,
                        parent,
                        false
                )
        );
    }

    @Override
    public void onBindViewHolder(@NonNull AchievementsViewHolder holder, int position) {
        holder.setData(AchievementItems.get(position));
    }

    @Override
    public int getItemCount() {
        return AchievementItems.size();
    }


    public class AchievementsViewHolder extends RecyclerView.ViewHolder{
        TextView textViewAchievementsTags;

        public AchievementsViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewAchievementsTags = itemView.findViewById(R.id.tvAchievementsTags);
        }
        void setData(final AcievementsTagsModel AchievementItems) {
            textViewAchievementsTags.setText(AchievementItems.getaTag());
        }
    }
}
