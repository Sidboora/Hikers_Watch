package com.example.hikerswatch;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    LocationManager locationManager;
    LocationListener locationlistner;
    TextView longView;
    TextView latView;
   TextView addressView;
    TextView accuracyView;
    TextView altitudeView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        latView = findViewById(R.id.latView);
        longView = findViewById(R.id.longView);
        addressView = findViewById(R.id.addressView);
        accuracyView = findViewById(R.id.accuracyView);
        altitudeView = findViewById(R.id.altitudeview);


        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        locationlistner = new LocationListener() {
            @Override
            public void onLocationChanged(@NonNull Location location) {
                //latView.setText((int) location.getLatitude());
                //longView.setText((int) location.getLongitude());
                //Log.i("the location is",location.toString());
                updatelocation(location);

            }
        };
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        } else {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationlistner);
            Location lastLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

            if (lastLocation != null) {
                updatelocation(lastLocation);
            }
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            startListning();
        }
    }
    private  void startListning()
    {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationlistner);
        }
    }
    private void updatelocation(Location lastLocation) {
        latView.setText("Lattitude: "+Double.toString(lastLocation.getLatitude()));
        longView.setText("Longitude: "+Double.toString(lastLocation.getLongitude()));
        altitudeView.setText("Altitude: "+Double.toString(lastLocation.getAltitude()));
        accuracyView.setText("Accuracy: "+Double.toString(lastLocation.getAccuracy()));

        Geocoder geocoder =new Geocoder(getApplicationContext(), Locale.getDefault());
        try {
            List<Address> listAddress = geocoder.getFromLocation(lastLocation.getLatitude(),lastLocation.getLongitude(),1);
            if(listAddress!= null && listAddress.size()>0)
            {
                addressView.setText("Address:\n"+listAddress.get(0).getSubLocality()+ ", "+listAddress.get(0).getLocality() );
            }
            else
            {
                addressView.setText("Address not found :(");
            }

        }
        catch(Exception e)
        {
            e.printStackTrace();
        }

        //Log.i("Location is", (lastLocation).toString());
    }
}