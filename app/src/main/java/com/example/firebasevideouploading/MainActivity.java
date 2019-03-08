package com.example.firebasevideouploading;

import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.MediaController;
import android.widget.Toast;
import android.widget.VideoView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    private StorageReference mStorageRef;
    DatabaseReference database;

    EditText text;
    Uri mainUri,dbstoredPath;
    VideoView videoView;
    private static final int SELECT_VIDEO = 1;
    private String selectedVideoPath;
    MediaController videoMediaController;

    ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        videoView = findViewById(R.id.videoView);
        text = findViewById(R.id.editText);
        videoMediaController = new MediaController(this);

    }

    public void showProgress(){
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Uploading..");
        progressDialog.setMessage("please wait uploading in progress...");
        progressDialog.show();
    }
    public void upload(View view) {
        if (mainUri!=null){
            showProgress();
            text.getText().toString();
            mStorageRef = FirebaseStorage.getInstance().getReference().child("Videos").child(mainUri.getLastPathSegment());
            database = FirebaseDatabase.getInstance().getReference("Videos");

            mStorageRef.putFile(mainUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            mStorageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    dbstoredPath = uri;

                                    Toast.makeText(MainActivity.this, "Uploaded!!!", Toast.LENGTH_SHORT).show();
                                    Log.i("Upload:",""+uri.toString());

                                    Map<String,Object> map = new HashMap<>();
                                    map.put("about",text.getText().toString());
                                    map.put("video_url",uri.toString());
                                    database.child(text.getText().toString()).setValue(map);
                                    Toast.makeText(MainActivity.this, "Saved", Toast.LENGTH_SHORT).show();

                                    progressDialog.dismiss();
                                    finish();
                                }
                            });

                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.i("Upload:","Failed :"+e.getMessage());
                        }
                    });

        }else {
            Toast.makeText(this, "Please Select video!!!", Toast.LENGTH_SHORT).show();
        }

    }

    public void choose(View view) {
        Intent i = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Video.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(i, SELECT_VIDEO);
    }
    @ Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == SELECT_VIDEO) {
                selectedVideoPath = getPath(data.getData());
                if(selectedVideoPath == null) {
                    Log.e("selected video path ","= null!");
                    finish();
                } else {

                    Log.i("URI",selectedVideoPath);

                    videoView.setVideoURI(data.getData());
                    mainUri = data.getData();
                    videoMediaController.setMediaPlayer(videoView);
                    videoView.setMediaController(videoMediaController);
                    videoView.requestFocus();
                    /**
                     * try to do something there
                     * selectedVideoPath is path to the selected video
                     */
                }
            }
        }
       //
    }

    private String getPath(Uri data) {
        String[] projection = { MediaStore.Images.Media.DATA };
        Cursor cursor = managedQuery(data, projection, null, null, null);
        if(cursor!=null) {
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        }
        else return null;
    }
}
