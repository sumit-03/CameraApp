package com.example.quantum.cameraapp;

import android.content.res.Configuration;
import android.hardware.Camera;
import android.nfc.Tag;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {
    Camera camera=null;
    FrameLayout frameLayout;
    ShowCamera showCamera=null;
    Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        camera = getCameraInstance();
        //camera= Camera.open(); this line can also do samething as getCameraInstance()

        frameLayout = (FrameLayout) findViewById(R.id.frame);
        showCamera = new ShowCamera(this, camera);
        frameLayout.addView(showCamera);

        myButtonListenerMethod();

    }

    private void myButtonListenerMethod() {
         button=(Button) findViewById(R.id.button);

         button.setOnClickListener(new View.OnClickListener(){

             @Override
             public void onClick(View v) {
                 if (camera!=null){
                     camera.takePicture(null,null,mPictureCallback);
                 }
             }
         });
    }

    Camera.PictureCallback mPictureCallback=new Camera.PictureCallback(){

        @Override
        public void onPictureTaken(byte[] bytes, Camera camera) {
            File picture_file=getOutputMediaFile();
            if(picture_file==null){
                return ;
            }else{
                try{
                    FileOutputStream fos=new FileOutputStream(picture_file);
                    fos.write(bytes);
                    fos.close();
                    camera.startPreview();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    };

    private File getOutputMediaFile() {

        String state = Environment.getExternalStorageState();
        if(!state.equals(Environment.MEDIA_MOUNTED)){
            return null;
        }else{
            File folder_gui=new File(Environment.getExternalStorageDirectory()+File.separator+"GUI");
            if (!folder_gui.exists()){
                folder_gui.mkdirs();
            }
            File outputFile=new File(folder_gui,"temp.jpg");
            return outputFile;
        }
    }


    public Camera getCameraInstance() {
        Camera c=null ;
        try {
            c = Camera.open();

        } catch (Exception e) {
        }

        return c;
    }
}
