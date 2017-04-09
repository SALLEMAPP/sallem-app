package com.seniorproject.sallemapp.Activities;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.seniorproject.sallemapp.R;
import com.seniorproject.sallemapp.entities.DomainPost;
import com.seniorproject.sallemapp.entities.DomainUser;
import com.seniorproject.sallemapp.entities.Post;
import com.seniorproject.sallemapp.entities.PostImage;
import com.seniorproject.sallemapp.helpers.CommonMethods;
import com.seniorproject.sallemapp.helpers.EntityAsyncResult;
import com.seniorproject.sallemapp.helpers.MyHelper;
import com.seniorproject.sallemapp.helpers.SavePostAsync;
import com.squareup.okhttp.OkHttpClient;

import org.joda.time.LocalDateTime;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.security.InvalidKeyException;
import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

public class AddPostActivity extends AppCompatActivity {

    Bitmap bm;
    private static final int REQUEST_CODE = 121;
    ProgressBar progressBar;
    private String mPostId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_post);
        progressBar = (ProgressBar) findViewById(R.id.progressBar2);
        progressBar.setVisibility(View.GONE);
        attchPostButton();
        attachOpenGalaryButton();


    }

    private void attachOpenGalaryButton() {
        ImageButton b = (ImageButton) findViewById(R.id.addPost_imgbtnChoosePhoto);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openImageFromGalary();
            }
        });

    }

    private void openImageFromGalary() {
        Intent gallaryIntent = new Intent();
        gallaryIntent.setType("image/*");
        gallaryIntent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(gallaryIntent, REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK && requestCode == REQUEST_CODE) {
            ImageView imageView = (ImageView) findViewById(R.id.addPost_imgPostImage);

            if (data != null) {
                try {
                    Bitmap photo = MediaStore.Images.Media.getBitmap(
                            getApplicationContext().getContentResolver(), data.getData()
                    );
                    Bitmap scaledPhoto = Bitmap.createScaledBitmap(photo, 740, 412, false);
                    imageView.setImageBitmap(scaledPhoto);
                    bm = scaledPhoto;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else if (resultCode == Activity.RESULT_CANCELED) {
                Toast.makeText(getApplicationContext(), "Cancelled", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public Bitmap getPicture(Uri selectedImage) {
        String[] filePathColumn = {MediaStore.Images.Media.DATA};
        Cursor cursor = this.getContentResolver().query(selectedImage, filePathColumn, null, null, null);
        cursor.moveToFirst();
        int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
        String picturePath = cursor.getString(columnIndex);
        cursor.close();
        return BitmapFactory.decodeFile(picturePath);
    }

    private void attchPostButton() {
        Button b = (Button) findViewById(R.id.addPost_btnPost);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addPost();
            }
        });
    }

    private void addPost() {
        String subject = ((EditText) findViewById(R.id.addPost_txtPostSubject)).getText().toString();
        ImageView image = (ImageView) findViewById(R.id.addPost_imgPostImage);
        String postedAt = new LocalDateTime().toString();
        String userId = DomainUser.CURRENT_USER.getId();
        Post post = new Post();

        post.setId(UUID.randomUUID().toString());
        post.setPostedAt(postedAt);
        post.setUserId(userId);
        post.setSubject(subject);

        if (bm != null) {
            //post.set_imagePath(UUID.randomUUID().toString() + ".jpg");
            String imageAsString = MyHelper.ImageAsString(bm);
            post.setPostImage(imageAsString);
        }
        //
        SavePostAsync savePostAsync = new SavePostAsync(post, bm, this);
        savePostAsync.execute();
        //Don not remove, while this is not used any more, it useful reference for a trick on
        // how to pass images between different activities to overcome Android limitation for passing
        //objects as parcel that are more than 1mb. Android will throw run time exception if parcel is too big, e.g. > 1mb.
        // if(bm != null) {
           // MyHelper.saveImageToDisk(getApplicationContext(), post.get_imagePath(), bm);
        //}
        DomainPost domainPost = createDomainPost(post);
        Intent i = new Intent();
        i.putExtra("newPost", domainPost);
        i.setAction(CommonMethods.ACTION_NOTIFY_ADD_POST);
        sendBroadcast(i);
        finish();
    }
    private DomainPost createDomainPost(Post post){
        DomainPost domainPost = new DomainPost();
        domainPost.set_id(post.getId());
        domainPost.set_userId(DomainUser.CURRENT_USER.getId());
        domainPost.set_user(DomainUser.CURRENT_USER);
        domainPost.set_subject(post.getSubject());
        domainPost.setImagePath(post.getPostImage());
        domainPost.set_activityId(post.getActivityId());
        domainPost.set_postedAt(post.getPostedAt());

        return domainPost;
    }







}
