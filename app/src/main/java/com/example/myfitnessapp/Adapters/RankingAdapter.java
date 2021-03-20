package com.example.myfitnessapp.Adapters;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.nfc.Tag;
import android.service.notification.NotificationListenerService;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myfitnessapp.Classes.RankModel;
import com.example.myfitnessapp.Classes.RewardsModel;
import com.example.myfitnessapp.Classes.videoModel;
import com.example.myfitnessapp.R;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.firebase.ui.firestore.paging.FirestorePagingAdapter;
import com.firebase.ui.firestore.paging.FirestorePagingOptions;
import com.firebase.ui.firestore.paging.LoadingState;
import com.google.firebase.firestore.DocumentSnapshot;
import com.squareup.picasso.Picasso;

import java.util.List;


public class RankingAdapter extends RecyclerView.Adapter<RankingAdapter.RankViewHolder> {

    private List<RankModel> rankItem;
    Context context;

    public RankingAdapter(List<RankModel> rankItem, Context context) {
        this.rankItem = rankItem;
        this.context = context;
    }


    @NonNull
    @Override
    public RankViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new RankingAdapter.RankViewHolder(
                LayoutInflater.from(parent.getContext()).inflate(
                        R.layout.single_rank_item,
                        parent,
                        false
                )
        );
    }

    @Override
    public void onBindViewHolder(@NonNull RankViewHolder holder, int position) {
        holder.setData(rankItem.get(position));
    }

    @Override
    public int getItemCount() {
        return rankItem.size();
    }

    public class RankViewHolder extends RecyclerView.ViewHolder {

        TextView textViewChallengeName,textViewUsername,textViewPoints,textViewRankItemPlace;
        ImageView imageViewRankItemUserImage;

        public RankViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewChallengeName = itemView.findViewById(R.id.rankItemChallengeName);
            textViewUsername = itemView.findViewById(R.id.rankItemName);
            textViewPoints = itemView.findViewById(R.id.rankItemPoints);
            imageViewRankItemUserImage =itemView.findViewById(R.id.rankItemUserImage);
            textViewRankItemPlace= itemView.findViewById(R.id.rankItemPlace);
        }
        void setData(final RankModel rankModel) {
            textViewUsername.setText(rankModel.getUsername());
            textViewChallengeName.setText(rankModel.getChallenge_name());
            textViewPoints.setText(String.valueOf(rankModel.getPoints()));
            Picasso.get().load(rankModel.getProfileimg()).into(imageViewRankItemUserImage);
            // this gives the current rank of the person
            textViewRankItemPlace.setText(getAdapterPosition()+1 +".");

            if (rankModel.getUsername().equals("kihtrak") ){
                //TODO make shared preference to save the points
            }
        }
    }
}