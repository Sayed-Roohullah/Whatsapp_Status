package com.roohullah.whatsappstatus.Adapter;

import android.content.Context;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.roohullah.whatsappstatus.Classes.Constant;
import com.roohullah.whatsappstatus.R;
import com.roohullah.whatsappstatus.model.StatusModel;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class StatusAdapter extends RecyclerView.Adapter<StatusAdapter.StatusviewHolder>{
    private Context context;
    private ArrayList<Object> filelist;

    public StatusAdapter(Context context, ArrayList<Object> filelist) {
        this.context = context;
        this.filelist = filelist;
    }

    @NonNull
    @Override
    public StatusviewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
       View view = LayoutInflater.from(context).inflate(R.layout.show_card,null,false);
        return new StatusviewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull StatusviewHolder holder, int position) {
        final StatusModel files = (StatusModel) filelist.get(position);

        if (files.getUri().toString().endsWith(".mp4")){
            holder.playbtn.setVisibility(View.VISIBLE);
        }else{
            holder.playbtn.setVisibility(View.INVISIBLE);
        }
        Glide.with(context).load(files.getUri()).into(holder.mainimg);
        holder.downloadbtn.setOnClickListener(v -> {
            checkFolder();

            final String path = ((StatusModel) filelist.get(position)).getPath();
            final File file = new File(path);

            String destination = Environment.getExternalStorageDirectory().getAbsolutePath() + Constant.SAVEFOLDERNAME;
            File destiFile = new File(destination);

            try {
                FileUtils.copyFileToDirectory(file,destiFile);
            } catch (IOException e) {
                e.printStackTrace();
            }
            MediaScannerConnection.scanFile(context,
                    new String[]{destination + files.getFilename()},
                    new String[]{"*/*"},
                    new MediaScannerConnection.MediaScannerConnectionClient() {
                        @Override
                        public void onMediaScannerConnected() {

                        }

                        @Override
                        public void onScanCompleted(String path, Uri uri) {

                        }
                    });
            Toast.makeText(context, "saved to :" +destination +files.getFilename(), Toast.LENGTH_SHORT).show();
        });
    }
    private void checkFolder(){
        String path = Environment.getExternalStorageDirectory().getAbsolutePath() + Constant.SAVEFOLDERNAME;
        File dir = new File(path);
        boolean isDirectoryCreate = dir.exists();
        if (!isDirectoryCreate){
            isDirectoryCreate = dir.mkdir();
        }
        if (isDirectoryCreate){
            Log.d("Folder","Already Created");
        }
    }

    @Override
    public int getItemCount() {
        return filelist.size();
    }

    public class StatusviewHolder extends RecyclerView.ViewHolder{

        ImageView playbtn,downloadbtn,mainimg;
        public StatusviewHolder(@NonNull View itemView) {
            super(itemView);
            playbtn = itemView.findViewById(R.id.playImg);
            downloadbtn = itemView.findViewById(R.id.downloadbtn);
            mainimg = itemView.findViewById(R.id.mainImageView);


        }
    }
}
