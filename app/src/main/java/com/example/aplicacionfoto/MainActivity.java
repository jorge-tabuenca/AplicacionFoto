package com.example.aplicacionfoto;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    private Button boton;
    private ImageView imageView;
    private final int REQUEST_TAKE_PHOTO = 1;
    String currentPhotoPath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        boton = findViewById(R.id.button);
        imageView = findViewById(R.id.imageView);

        boton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                takePhoto();

            }
        });
    }
    private void takePhoto() {

        Intent takePicture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

            File photoFile = null;

                try {
                    photoFile = createPhotoFile();

                } catch (IOException e) {
                    e.printStackTrace();
                }

            if(photoFile != null){
                Uri photoUri = FileProvider.getUriForFile(this, BuildConfig.APPLICATION_ID+ ".provider", photoFile);
                takePicture.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
                startActivityForResult(takePicture, REQUEST_TAKE_PHOTO);
            }else{
                Toast.makeText(this, "CAMARA NO DISPONIBLE", Toast.LENGTH_SHORT).show();
            }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_TAKE_PHOTO && resultCode == RESULT_OK) {

            File f = new File(currentPhotoPath);
            imageView.setImageURI(takeLastPhoto());

        }
    }

    private Uri takeLastPhoto() {

        Uri uri = null;
        File f = new File(currentPhotoPath);

        if(f != null){
            uri = FileProvider.getUriForFile(this, BuildConfig.APPLICATION_ID + ".provider", f);
        }else{
            uri = null;
        }
        return uri;
    }

    private File createPhotoFile() throws IOException {

        String timestamp = new SimpleDateFormat( "yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timestamp + "_";

        File storageFile = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File photoFile = File.createTempFile(
          imageFileName, ".jpg",
                storageFile
        );
        currentPhotoPath = photoFile.getAbsolutePath();

        return photoFile;
    }
}