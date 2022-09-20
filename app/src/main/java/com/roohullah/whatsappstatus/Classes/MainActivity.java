package com.roohullah.whatsappstatus.Classes;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.Manifest;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.widget.Toast;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.roohullah.whatsappstatus.R;
import com.roohullah.whatsappstatus.Adapter.StatusAdapter;
import com.roohullah.whatsappstatus.model.StatusModel;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    SwipeRefreshLayout swipeRefreshLayout;

    StatusAdapter statusAdapter;
    File[] files;
    ArrayList<Object> filelists = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initviews();

        Dexter.withContext(this)
                .withPermissions(Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport multiplePermissionsReport) {
                        setUpRefresh();
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> list, PermissionToken permissionToken) {
                        permissionToken.continuePermissionRequest();
                    }
                }).check();
//        isPermissionGranted();
//
//        checkPermissions();
    }
    protected void initviews(){
        recyclerView = findViewById(R.id.recylerview);
        swipeRefreshLayout = findViewById(R.id.swiperecycler);


        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefreshLayout.setRefreshing(true);
                setUpRefresh();
                (
                        new Handler()).postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        swipeRefreshLayout.setRefreshing(false);
                        Toast.makeText(MainActivity.this, "Refresh..", Toast.LENGTH_SHORT).show();
                    }
                },2000);
            }
        });
    }
//    private boolean isPermissionGranted() {
//        int readExternalStorage = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE);
//        return readExternalStorage == PackageManager.PERMISSION_GRANTED;
//    }
//
//    private boolean checkPermissions() {
//        String[] permissions = new String[]{
//                Manifest.permission.CAMERA,
//                Manifest.permission.READ_EXTERNAL_STORAGE,
//                Manifest.permission.WRITE_EXTERNAL_STORAGE
//        };
//        int result;
//        List<String> listPermissionsNeeded = new ArrayList<>();
//        for (String p : permissions) {
//            result = ContextCompat.checkSelfPermission(this, p);
//            if (result != PackageManager.PERMISSION_GRANTED) {
//                listPermissionsNeeded.add(p);
//            }
//        }
//        if (!listPermissionsNeeded.isEmpty()) {
//            ActivityCompat.requestPermissions(this, listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), 100);
//            return false;
//        }
//        return true;
//    }
    private void setUpRefresh(){
        filelists.clear();
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        statusAdapter = new StatusAdapter(MainActivity.this,getData());
        recyclerView.setAdapter(statusAdapter);
        statusAdapter.notifyDataSetChanged();
    }
    private ArrayList<Object> getData(){

        StatusModel model;
        String tp = Environment.getExternalStorageDirectory().getAbsolutePath() + Constant.FOLDERNAME+"Media/.statuses";
        File td = new File(tp);
        files = td.listFiles();
        for (int i=0; i<files.length;i++){
            File file = files[i];
            model = new StatusModel();
            model.setUri(Uri.fromFile(file));
            model.setPath(files[i].getAbsolutePath());
            model.setFilename(file.getName());

            if (!model.getUri().toString().endsWith(".nomedia")){
                filelists.add(model);
            }
        }
        return filelists;
    }
}