package com.example.myfitnessapp.Adapters;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myfitnessapp.Activity.ViewOthersProfile;
import com.example.myfitnessapp.Classes.SearchPeopleModel;
import com.example.myfitnessapp.R;
import com.firebase.ui.firestore.paging.FirestorePagingAdapter;
import com.firebase.ui.firestore.paging.FirestorePagingOptions;
import com.squareup.picasso.Picasso;

public class FollowersAndFollowingAdapter extends FirestorePagingAdapter<SearchPeopleModel, FollowersAndFollowingAdapter.FollowersAndFollowingViewHolder> {

    public FollowersAndFollowingAdapter(@NonNull FirestorePagingOptions<SearchPeopleModel> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull FollowersAndFollowingViewHolder holder, int position, @NonNull final SearchPeopleModel model) {

        holder.textViewNameSearchPeople.setText(model.getName());
        holder.textViewUsernameSearchPeople.setText(model.getUsername());
        Picasso.get().load(model.getProfileimg()).into(holder.imageViewProfileImageSearchPeople);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i= new Intent(v.getContext(), ViewOthersProfile.class);
                i.putExtra("user_AuthId",model.getUser_AuthId());
                v.getContext().startActivity(i);
            }
        });
    }

    @NonNull
    @Override
    public FollowersAndFollowingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view =  LayoutInflater.from(parent.getContext()).inflate(
                R.layout.single_search_page_item,
                parent,
                false
        );
        return new FollowersAndFollowingAdapter.FollowersAndFollowingViewHolder(view);
    }

    public class FollowersAndFollowingViewHolder extends RecyclerView.ViewHolder{

        TextView textViewUsernameSearchPeople,textViewNameSearchPeople;
        ImageView imageViewProfileImageSearchPeople;

        public FollowersAndFollowingViewHolder(@NonNull View itemView) {
            super(itemView);

            textViewNameSearchPeople = itemView.findViewById(R.id.tvNameSearchPeople);
            textViewUsernameSearchPeople = itemView.findViewById(R.id.tvUserNameSearchPeople);
            imageViewProfileImageSearchPeople = itemView.findViewById(R.id.ivProfileImageSearchPeople);

        }
    }
}
