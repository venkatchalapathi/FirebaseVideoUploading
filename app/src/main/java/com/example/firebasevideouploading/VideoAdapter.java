package com.example.firebasevideouploading;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.VideoView;

import java.util.ArrayList;

class VideoAdapter extends RecyclerView.Adapter<VideoAdapter.ViewHolder>{
    VideosActivity videosActivity;
    ArrayList<Model> list;

    public VideoAdapter(VideosActivity videosActivity, ArrayList<Model> list) {
        this.videosActivity = videosActivity;
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new ViewHolder(LayoutInflater.from(videosActivity).inflate(R.layout.video_row,viewGroup,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        viewHolder.name.setText(list.get(i).getAbout());
    }

    @Override
    public int getItemCount() {
        if (list!=null) {
            return list.size();
        }else {
            return 0;
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView name;
        ImageView video;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.videoname);
            video = itemView.findViewById(R.id.video);
            video.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {

            Uri mainUri = Uri.parse(list.get(getAdapterPosition()).getVideo_url());
            AlertDialog.Builder builder = new AlertDialog.Builder(videosActivity);
            builder.setTitle(list.get(getAdapterPosition()).getAbout());
            View view = LayoutInflater.from(videosActivity).inflate(R.layout.video_layout,null);

            VideoView videoView = view.findViewById(R.id.videoView2);
            MediaController videoMediaController= new MediaController(videosActivity);
            videoView.setVideoURI(mainUri);
            videoMediaController.setMediaPlayer(videoView);
            videoView.setMediaController(videoMediaController);
            videoView.requestFocus();

            builder.setView(view);
            builder.setPositiveButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });

            builder.show();

        }
    }
}
