package th.thacks;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.pm.PackageManager;
import android.graphics.drawable.BitmapDrawable;
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
import java.net.UnknownHostException;
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
import java.nio.charset.StandardCharsets;

import java.lang.System;
import java.net.Socket;
import java.net.InetAddress;
import java.net.UnknownHostException;

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

    private void requestImage(final int[] colors) throws IOException
    {
        InetAddress SERVER_ADDR = null;
        try {
            SERVER_ADDR = InetAddress.getByAddress(new byte[]{(byte) 192, (byte) 168, (byte) 0, (byte) 0});
        } catch(UnknownHostException ignored) {}
        final int SERVER_PORT = 9999;

        if(colors.length != 6)
            throw new IllegalArgumentException();
        Socket socket = new Socket(SERVER_ADDR,SERVER_PORT);
        StringBuilder sb = new StringBuilder();
        sb.append("GET ");
        for(int i : colors)
        {
            sb.append(Integer.toHexString(i & 0xfffffff));
            sb.append(' ');
        }
        sb.append("\r\n");
        socket.getOutputStream().write(sb.toString().getBytes());
        Bitmap bm = BitmapFactory.decodeStream(socket.getInputStream());
        this.getWindow().getDecorView().setBackground(new BitmapDrawable(bm));
    }
}

