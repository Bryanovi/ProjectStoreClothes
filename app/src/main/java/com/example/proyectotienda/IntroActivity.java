package com.example.proyectotienda;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;


import com.example.proyectotienda.LoginActivity;
import com.example.proyectotienda.RegisterActivity;

import java.util.ArrayList;
import java.util.List;

public class IntroActivity extends AppCompatActivity {

    private List<Integer> videos = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);

        videos.add(R.raw.intro);
        videos.add(R.raw.intro2);
        videos.add(R.raw.intro);

        ViewPager2 viewPager = findViewById(R.id.viewPager);
        VideoAdapter videoAdapter = new VideoAdapter(videos);
        viewPager.setAdapter(videoAdapter);
        Button loginBtn = findViewById(R.id.btnLoginI);
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               startActivity(new Intent(IntroActivity.this, LoginActivity.class));

            }
        });
        Button registerBtn = findViewById(R.id.btnRegisterI);
        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(IntroActivity.this, RegisterActivity.class));
            }
        });
    }


    private static class VideoAdapter extends RecyclerView.Adapter<VideoAdapter.VideoViewHolder> {

        private List<Integer> videos;

        public VideoAdapter(List<Integer> videos) {
            this.videos = videos;
        }

        @NonNull
        @Override
        public VideoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_video, parent, false);
            return new VideoViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull VideoViewHolder holder, int position) {
            Uri videoUri = Uri.parse("android.resource://" + holder.videoView.getContext().getPackageName() + "/" + videos.get(position));
            holder.videoView.setVideoURI(videoUri);
            holder.videoView.start();
        }

        @Override
        public int getItemCount() {
            return videos.size();
        }

        public static class VideoViewHolder extends RecyclerView.ViewHolder {

            public VideoView videoView;

            public VideoViewHolder(@NonNull View itemView) {
                super(itemView);
                videoView = itemView.findViewById(R.id.videoView);
            }
        }
    }
}
