package com.example.review;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.core.Tag;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

public class UploadActivity extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1;

    private Button mButtonChooseImage;
    private Button mButtonUpload;
    private ImageButton mTextViewShowUpLoads;
    private EditText mEditTextFileName;
    private ImageView mImageview;
    private ProgressBar mProgressbar;
    private EditText mQuestions;

    private StorageReference mStorageRef;
    private DatabaseReference mDatabaseRef;

    private Uri mImageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload);


        mButtonChooseImage= findViewById(R.id.button_choose_image);
        mButtonUpload= findViewById(R.id.button_upload);
        mTextViewShowUpLoads= findViewById(R.id.uploadnextbtn);
        mEditTextFileName= findViewById(R.id.ET_file_name);
        mImageview= findViewById(R.id.image_view);
        mProgressbar=findViewById(R.id.progress_bar);
        mQuestions= findViewById(R.id.input_questions);

        mStorageRef= FirebaseStorage.getInstance().getReference("uploads");
        mDatabaseRef= FirebaseDatabase.getInstance().getReference("uploads");

        mButtonChooseImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFileChooser();
            }
        });



        mButtonUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadFile();

            }
        });


        mTextViewShowUpLoads.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                openImagesActivity();

            }
        });

    }

    private void openFileChooser(){

        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() !=null){
            mImageUri = data.getData();

            Picasso.get().load(mImageUri).into(mImageview);
        }
    }

    //to get the extension from our file(image)
    private String getFileExtension(Uri uri){
        ContentResolver cR= getContentResolver();
        MimeTypeMap mime= MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }

    private void uploadFile(){
        if(mImageUri !=null){
            StorageReference fileReference = mStorageRef.child(System.currentTimeMillis()+"."+getFileExtension(mImageUri));

            StorageTask<UploadTask.TaskSnapshot> mUploadTask = fileReference.putFile(mImageUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    mProgressbar.setProgress(0);
                                }
                            }, 5000);

                            Toast.makeText(UploadActivity.this, "Upload Succesful!", Toast.LENGTH_LONG).show();
                            /*Upload upload = new Upload(mEditTextFileName.getText().toString().trim(), taskSnapshot.getStorage().getDownloadUrl().toString(), mQuestions.getText().toString().trim());
                            String uploadId = mDatabaseRef.push().getKey();
                            mDatabaseRef.child(uploadId).setValue(upload);*/

                         Task<Uri> urlTask= taskSnapshot.getStorage().getDownloadUrl();
                         while(! urlTask.isSuccessful());
                         Uri downloadUrl = urlTask.getResult();

                         //Log.d(TAG,"onSuccess:firebase download url:"+downloadUrl.toString();)

                            Upload upload = new Upload(mEditTextFileName.getText().toString().trim(), downloadUrl.toString(), mQuestions.getText().toString().trim());
                         String uploadId = mDatabaseRef.push().getKey();
                         mDatabaseRef.child(uploadId).setValue(upload);

                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(UploadActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                            //update progress bar
                            double progress = (100.0 * snapshot.getBytesTransferred() / snapshot.getTotalByteCount());
                            mProgressbar.setProgress((int) progress);
                        }
                    });
        }else{
            Toast.makeText(this, "No file selected", Toast.LENGTH_SHORT).show();
        }

    }

    private void openImagesActivity(){
        Intent intent= new Intent(this, ImagesActivity.class);
        startActivity(intent);
    }

}