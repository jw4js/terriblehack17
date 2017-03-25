package th.thacks;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.pm.PackageManager;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.app.LoaderManager.LoaderCallbacks;

import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;

import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import android.content.Intent;
import java.io.File;
import java.io.IOException;
import android.support.v7.graphics.Palette;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.roger.catloadinglibrary.CatLoadingView;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

import static android.Manifest.permission.READ_CONTACTS;
import java.lang.System;

/**
 * A login screen that offers login via email/password.
 */
public class Main extends AppCompatActivity {
    CatLoadingView mView = new CatLoadingView();
    static final int REQUEST_IMAGE_CAPTURE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Set up the login form.

        Button button = (Button)findViewById(R.id.test_button);
        button.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                mView.show(getSupportFragmentManager(), "");
            }
        });
    }

    /**
     * Callback received when a permissions request has been completed.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
    }

    File image_file = null;

    public void takePicture(View view)
    {
        try {
            image_file = File.createTempFile("img", null);
        } catch (IOException ignored) {}
//        Intent picture_intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//        picture_intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY,1);
//        Uri image_uri = Uri.fromFile(image_file);
//        picture_intent.putExtra(MediaStore.EXTRA_OUTPUT,image_uri);

        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode,int resultCode,Intent data)
    {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            //mImageView.setImageBitmap(imageBitmap);
            Palette palette = Palette.from(imageBitmap).generate();
            Log.d("Hello", palette.toString());
            this.getWindow().getDecorView().setBackgroundColor(palette.getVibrantColor(0));
        }
    }
}

