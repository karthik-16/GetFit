package com.example.myfitnessapp.Adapters;

import android.content.Context;
import android.content.Intent;
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

import com.example.myfitnessapp.Activity.PurchasingReward;
import com.example.myfitnessapp.Activity.ViewOthersProfile;
import com.example.myfitnessapp.Classes.SearchPeopleModel;
import com.example.myfitnessapp.R;
import com.firebase.ui.firestore.paging.FirestorePagingAdapter;
import com.firebase.ui.firestore.paging.FirestorePagingOptions;
import com.firebase.ui.firestore.paging.LoadingState;
import com.squareup.picasso.Picasso;

public class SearchPeopleAdapter extends FirestorePagingAdapter<SearchPeopleModel,SearchPeopleAdapter.searchPeopleViewHolder> {


    public SearchPeopleAdapter(@NonNull FirestorePagingOptions<SearchPeopleModel> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull searchPeopleViewHolder holder, int position, @NonNull final SearchPeopleModel model) {
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
    public searchPeopleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view =  LayoutInflater.from(parent.getContext()).inflate(
                R.layout.single_search_page_item,
                parent,
                false
        );
        return new SearchPeopleAdapter.searchPeopleViewHolder(view);
    }

    @Override
    protected void onLoadingStateChanged(@NonNull LoadingState state) {
        super.onLoadingStateChanged(state);

        switch (state){
            case ERROR: Log.d("PAGING_LOG", "onLoadingStateChanged: Error" + getItemCount() );
                break;
            case LOADED:Log.d("PAGING_LOG", "onLoadingStateChanged: Total Items Loaded" + getItemCount() );
                break;
            case FINISHED:Log.d("PAGING_LOG", "onLoadingStateChanged: All items loaded" + getItemCount() );
                break;
            case LOADING_INITIAL:Log.d("PAGING_LOG", "onLoadingStateChanged: Loaded Initial" + getItemCount() );
                break;
            case LOADING_MORE: //Here we could make the progress bar invisible
                Log.d("PAGING_LOG", "onLoadingStateChanged: Loading Next Page" + getItemCount() );
                break;

        }
    }

    public class searchPeopleViewHolder extends RecyclerView.ViewHolder{

        TextView textViewUsernameSearchPeople,textViewNameSearchPeople;
        ImageView imageViewProfileImageSearchPeople;

        public searchPeopleViewHolder(@NonNull View itemView) {
            super(itemView);

            textViewNameSearchPeople = itemView.findViewById(R.id.tvNameSearchPeople);
            textViewUsernameSearchPeople = itemView.findViewById(R.id.tvUserNameSearchPeople);
            imageViewProfileImageSearchPeople = itemView.findViewById(R.id.ivProfileImageSearchPeople);
            //here we can add onClickListeners for even the views in the single items
        }
    }
}
