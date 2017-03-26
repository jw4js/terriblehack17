package th.thacks;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.TransitionDrawable;
import android.media.Image;
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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.net.URL;
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
import java.io.ByteArrayOutputStream;
import java.net.UnknownHostException;

/**
 * A login screen that offers login via email/password.
 */
public class Main extends Activity {
//    CatLoadingView mView = new CatLoadingView();
    static final int REQUEST_IMAGE_CAPTURE = 1;
    ImageView iv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Set up the login form.
        iv = (ImageView) findViewById(R.id.foodImage);

        RelativeLayout linearLayout = (RelativeLayout) findViewById(R.id.linearlayout);
        AnimationDrawable trans = (AnimationDrawable) linearLayout.getBackground();
        trans.start();


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
            //Log.d("Hello", palette.toString());
            this.getWindow().getDecorView().setBackgroundColor(palette.getVibrantColor(0));
            List<Palette.Swatch> swatches = palette.getSwatches();
            int [] list = new int[swatches.size()];
            for(int i = 0; i < swatches.size(); i++){
                list[i] = swatches.get(i).getRgb();
            }
            new Thread(new ImageSetter(list,this)).start();
        }
    }

    public void requestImage(final int[] colors) throws IOException
    {

        Log.d("Hello" ,"World");
        InetAddress SERVER_ADDR = null;
        try {
            SERVER_ADDR = InetAddress.getByAddress(new byte[]{(byte) 10, (byte) 84, (byte) 66, (byte) 119});
        } catch(UnknownHostException ignored) {}
        Log.d("Hello" ,"World1");
        final int SERVER_PORT = 9999;

        Socket socket = new Socket(SERVER_ADDR,SERVER_PORT);
        Log.d("Hello" ,"World2");
        StringBuilder sb = new StringBuilder();
        sb.append("GET");
        for(int i : colors)
        {
            sb.append(' ');
            sb.append(Integer.toHexString(i & 0xfffffff));
        }
        Log.d("Hello" ,"World3");
        sb.append("\n");
        InputStream socket_is = socket.getInputStream();
        socket.getOutputStream().write(sb.toString().getBytes());
        String s = new String(read(socket_is));
        Log.d("Hello" , s);
        ImageThread it = new ImageThread(s);

        socket.close();
        it.execute(s);
    }

    public class ImageThread extends AsyncTask<String, Void, Drawable> {
        String world;
        Drawable ivv;
        public ImageThread(String hello) {
            super();
            world = hello;
        }

        @Override
        protected Drawable doInBackground(String... voids) {
            ivv = loadImageFromWebOperations(voids[0]);
            Log.d("Pokemon", world);
            return null;
        }

        @Override
        protected void onPostExecute(Drawable aVoid) {
            super.onPostExecute(aVoid);
            iv.setBackground(ivv);


        }
    }

    public static Drawable loadImageFromWebOperations(String url) {
        try {
            InputStream is = (InputStream) new URL(url).getContent();
            Drawable d = Drawable.createFromStream(is, "src name");
            return d;
        } catch (Exception e) {
            return null;

        }
    }

    public static byte[] read(final InputStream i) throws IOException
    {
        final ByteArrayOutputStream o = new ByteArrayOutputStream();
        final byte[] b = new byte[4096];
        int l;
        while((l = i.read(b,0,4096)) != -1)
            if(l != 0)
                o.write(b,0,l);
        i.close();
        return o.toByteArray();
    }
}

