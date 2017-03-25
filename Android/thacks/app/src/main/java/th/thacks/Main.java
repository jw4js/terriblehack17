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
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import static android.Manifest.permission.READ_CONTACTS;
import java.lang.System;

/**
 * A login screen that offers login via email/password.
 */
public class Main extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Set up the login form.
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
        Intent picture_intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        picture_intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY,1);
        Uri image_uri = Uri.fromFile(image_file);
        picture_intent.putExtra(MediaStore.EXTRA_OUTPUT,image_uri);
        startActivityForResult(picture_intent,0);
    }

    @Override
    protected void onActivityResult(int requestCode,int resultCode,Intent data)
    {
        switch(requestCode)
        {
            case 0:
            {
                if(resultCode == RESULT_OK)
                {
                    BitmapFactory.Options bitmap_options = new BitmapFactory.Options();
                    bitmap_options.inPreferredConfig = Bitmap.Config.ARGB_8888;
                    Bitmap image = null;
                    try {
                        InputStream is = new FileInputStream(image_file);
                        image = BitmapFactory.decodeStream(is,null,bitmap_options);
                        System.out.println(image);
                    } catch(FileNotFoundException e) {}
                    Palette palette = Palette.from(image).generate();
                    this.getWindow().getDecorView().setBackgroundColor(palette.getVibrantColor(0));
                }
                else
                {
                    throw new NullPointerException();
                }
            }
        }
    }
}

