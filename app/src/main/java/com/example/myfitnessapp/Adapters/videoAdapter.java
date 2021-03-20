package com.example.myfitnessapp.Adapters;

import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.nfc.Tag;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myfitnessapp.Classes.videoModel;
import com.example.myfitnessapp.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;



import java.util.List;

public class videoAdapter extends RecyclerView.Adapter<videoAdapter.videoViewHolder> {
    private static final String Tag = "TAG";

    private List<videoModel> videoItems;
        Context context;

public videoAdapter(List<videoModel> videoItems, Context context) {
        this.videoItems = videoItems;
        this.context = context;
        }

@NonNull
@Override
public videoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new videoViewHolder(
        LayoutInflater.from(parent.getContext()).inflate(
        R.layout.videoitem,
        parent,
        false
        )
        );
        }

@Override
public void onBindViewHolder(@NonNull videoViewHolder holder, int position) {

        holder.setData(videoItems.get(position));
        }

@Override
public int getItemCount() {
        return videoItems.size();
        }


static class videoViewHolder extends RecyclerView.ViewHolder {

    VideoView videoView;
    TextView tvName, tvChallenge,tvLikes;
    ImageView profileImage;
    ProgressBar progressBar;


    public videoViewHolder(@NonNull View itemView) {
        super(itemView);

        videoView = itemView.findViewById(R.id.videoView);
        progressBar = itemView.findViewById(R.id.videoProgressBar);
        tvLikes = itemView.findViewById(R.id.like_count);
        tvChallenge = itemView.findViewById(R.id.textViewChallenge);
        profileImage = itemView.findViewById(R.id.profileImg);
        tvName = itemView.findViewById(R.id.textViewName);
    }

    void setData(final videoModel videoItem) {
        tvName.setText(videoItem.getUsername());
        tvChallenge.setText(videoItem.getChallenge_name());
        tvLikes.setText(videoItem.getLikes() + "");
        videoView.setVideoPath(videoItem.getVideourl());
        Picasso.get().load(videoItem.getProfileimg()).into(profileImage);



        ///////////////////////////
        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                progressBar.setVisibility(View.GONE);
                mp.start();

                float videoRatio = mp.getVideoWidth() / (float) mp.getVideoHeight();
                float screenRatio = videoView.getWidth() / (float) videoView.getHeight();

                float scale = videoRatio / screenRatio;
                if (scale >= 1f) {
                    videoView.setScaleX(scale);
                } else {
                    videoView.setScaleY(1f / scale);
                }
            }
        });
        videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                mp.start();
            }
        });

    }
}

}

