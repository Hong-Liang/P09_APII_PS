package com.myapplicationdev.android.p09_apii_ps;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.content.PermissionChecker;

import android.Manifest;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

public class MainActivity extends AppCompatActivity {

    Button btnStart, btnStop, btnCheck;
    TextView tvLat, tvLong;
    FusedLocationProviderClient client;

    @Override
    protected void onResume() {
        super.onResume();

        client = LocationServices.getFusedLocationProviderClient(MainActivity.this);

        Task<Location> task = client.getLastLocation();
        if (checkPermission() == true) {
            task.addOnSuccessListener(MainActivity.this, new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {
                    //Get last known location.
                    if (location != null) {
                        tvLat.setText("Latitude: " + location.getLatitude());
                        tvLong.setText("Longitude: " + location.getLongitude());
                    } else {
                        String msg = "No last known location found";
                        Toast.makeText(MainActivity.this,msg , Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnStart = findViewById(R.id.btnStart);
        btnStop = findViewById(R.id.btnStop);
        btnCheck = findViewById(R.id.btnCheck);
        tvLat = findViewById(R.id.tvLat);
        tvLong = findViewById(R.id.tvLong);

        client = LocationServices.getFusedLocationProviderClient(MainActivity.this);

        Task<Location> task = client.getLastLocation();
        if (checkPermission() == true) {
            task.addOnSuccessListener(MainActivity.this, new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {
                    //Get last known location.
                    if (location != null) {
                        tvLat.setText("Latitude: " + location.getLatitude());
                        tvLong.setText("Longitude: " + location.getLongitude());
                    } else {
                        String msg = "No last known location found";
                        Toast.makeText(MainActivity.this,msg , Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }

        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, MyService.class);
                startService(i);
            }
        });

        btnStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this, MyService.class);
                stopService(i);
            }
        });

        final String folderLocation = Environment.getExternalStorageDirectory().getAbsolutePath() + "/MyFolder";

        btnCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this, MyService.class);
                startService(i);
                File targetFile = new File(folderLocation, "location.txt");
                if (targetFile.exists() == true) {
                    String data = "";
                    try {
                        FileReader reader = new FileReader(targetFile);
                        BufferedReader br = new BufferedReader(reader);
                        String line = br.readLine();
                        while (line != null) {
                            data += line + "\n";
                            line = br.readLine();
                        }
                        br.close();
                        reader.close();
                    } catch (Exception e) {
                        Toast.makeText(getApplicationContext(), "Failed to read", Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    private boolean checkPermission(){
        int permissionCheck_Coarse = ContextCompat.checkSelfPermission(
                MainActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION);
        int permissionCheck_Fine = ContextCompat.checkSelfPermission(
                MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION);
        if (permissionCheck_Coarse == PermissionChecker.PERMISSION_GRANTED
                || permissionCheck_Fine == PermissionChecker.PERMISSION_GRANTED) {
            return true;
        } else {
            return false;
        }
    }
}
