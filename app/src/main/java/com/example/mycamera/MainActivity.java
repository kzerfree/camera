package com.example.mycamera;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity {
    static public final int CODE_HELLO = 1;
    private EditText edtU = null, edtP = null;
    private Button btnLogin = null;

    private static final int REQUEST_IMAGE_CAPTURE = 2;
    private Button btCamera, btCamera2;
    private ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        doLogin();

        imageView = (ImageView) findViewById(R.id.imageView);

        btCamera = (Button) findViewById(R.id.btCamera);
        btCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TakePicture();
            }
        });

        btCamera2 = (Button) findViewById(R.id.btCamera2);
        btCamera2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openCameraActivityForResult();
            }
        });

    }

    private void TakePicture() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        Intent chooser = Intent.createChooser(takePictureIntent, "Chọn App để gọi");
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(chooser, REQUEST_IMAGE_CAPTURE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CODE_HELLO) {
            if (resultCode == RESULT_OK) {
                String s = data.getStringExtra("txt");
                Toast.makeText(this, s, Toast.LENGTH_SHORT).show();
            }
        }

        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            imageView.setImageBitmap(imageBitmap);
        }
    }

    ActivityResultLauncher<Intent> cameraActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        // There are no request codes
                        Intent data = result.getData();
                        Bundle extras = data.getExtras();
                        Bitmap imageBitmap = (Bitmap) extras.get("data");
                        imageView.setImageBitmap(imageBitmap);
                    }
                }
            });

    public void openCameraActivityForResult() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        cameraActivityResultLauncher.launch(intent);
    }

    private void doLogin() {
        edtU = (EditText)findViewById(R.id.edtUsername);
        edtP = (EditText)findViewById(R.id.edtPassword);
        btnLogin = (Button)findViewById(R.id.btLogin);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkPassword(edtU.getText().toString(), edtP.getText().toString())) {
                    Intent intHello = new Intent(MainActivity.this, HelloActivity.class);
                    startActivityForResult(intHello, CODE_HELLO);
                }
                else {
                    Uri page = Uri.parse("http://vnexpress.net");
                    Intent intWeb = new Intent(Intent.ACTION_VIEW, page);
                    //startActivity(intWeb);
                    Intent chooser = Intent.createChooser(intWeb, "Chọn App để gọi");
                    startActivity(chooser);
                }

            }
        });
    }

    private boolean checkPassword(String u, String p) {
        return (u.equals("hello") && p.equals("123"));
    }
}