package com.example.sasesangeet;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.io.File;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    ListView list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        list =findViewById(R.id.listview);
        Dexter.withContext(MainActivity.this)
                .withPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
                      //  Toast.makeText(MainActivity.this, "permission granted!", Toast.LENGTH_SHORT).show();
                        ArrayList<File> mysong = fetchsong(Environment.getExternalStorageDirectory());
                        String items[] = new String[mysong.size()];
                        for (int i = 0; i < mysong.size(); i++) {
                            items[i] =mysong.get(i).getName().replace(".mp3","");
                        }
                        ArrayAdapter<String> adapter =new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_list_item_1,items);
                        list.setAdapter(adapter);
                       list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                           @Override
                           public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                               Intent in =new Intent(MainActivity.this,PlaySong.class);
                               String currentsong =list.getItemAtPosition(i).toString();
                               in.putExtra("songList",mysong);
                               in.putExtra("c",currentsong);
                               in.putExtra("position",i);
                               startActivity(in);
                           }
                       });
                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {

                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {
                            permissionToken.continuePermissionRequest();
                    }
                })
                .check();

    }
    public ArrayList<File>fetchsong(File file){
        ArrayList arraylist =new ArrayList();
        File []song =file.listFiles();
        if(song!=null){
            for(File myFile:song){
                if(!myFile.isHidden() && myFile.isDirectory()){
                    arraylist.addAll(fetchsong(myFile));
                }
                else{
                    if(myFile.getName().endsWith(".mp3")&& !myFile.getName().startsWith(".")){
                        arraylist.add(myFile);
                    }
                }
            }
        }
        return arraylist;
    }
}