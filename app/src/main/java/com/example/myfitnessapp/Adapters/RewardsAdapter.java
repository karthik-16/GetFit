package com.example.myfitnessapp.Adapters;

import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myfitnessapp.Activity.PurchasingReward;
import com.example.myfitnessapp.Classes.RewardsModel;
import com.example.myfitnessapp.R;
import com.firebase.ui.firestore.paging.FirestorePagingAdapter;
import com.firebase.ui.firestore.paging.FirestorePagingOptions;
import com.squareup.picasso.Picasso;

public class RewardsAdapter extends FirestorePagingAdapter<RewardsModel,RewardsAdapter.rewardsViewHolder> {

    public RewardsAdapter(@NonNull FirestorePagingOptions<RewardsModel> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull rewardsViewHolder holder, int position, @NonNull final RewardsModel model)  {
        holder.textViewRewardTitle.setText(model.getTitle());
        holder.textViewRewardsDesc.setText(model.getDesc());
        Picasso.get().load(model.getImage()).into(holder.imageViewRewardImage);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i= new Intent(v.getContext(), PurchasingReward.class);
                i.putExtra("id",model.getId());
               // Toast.makeText(v.getContext(), "id"+model.getId()+"", Toast.LENGTH_SHORT).show();
                v.getContext().startActivity(i);
            }
        });

    }

    @NonNull
    @Override
    public rewardsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view =  LayoutInflater.from(parent.getContext()).inflate(
                R.layout.single_rewards_item,
                parent,
                false
        );
        return new RewardsAdapter.rewardsViewHolder(view);
    }

    public class rewardsViewHolder extends RecyclerView.ViewHolder {

        TextView textViewRewardTitle, textViewRewardsDesc;
        ImageView imageViewRewardImage;

        public rewardsViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewRewardTitle = itemView.findViewById(R.id.tvRewardTitle);
            textViewRewardsDesc = itemView.findViewById(R.id.tvRewardDesc);
            imageViewRewardImage = itemView.findViewById(R.id.ivRewardImage);
            //itemView.setOnClickListener(this);

        }

    }

}

