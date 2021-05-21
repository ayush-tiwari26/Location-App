package com.example.hikerswatch;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
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
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    LocationManager mlocationManager;
    LocationListener mlocationListner;
    Geocoder mgeocoder;

    //Printable details
    String mlongitude;
    String mlatitude;
    String maltitude;
    String maddress;
    String maccuracy;
    String mlocality;


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            mlocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 10000, 5, mlocationListner);
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        LinearLayout mlists=(LinearLayout)findViewById(R.id.linearLayout);

        //Initializing the service
        mlocationManager=(LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        mlocationListner=new LocationListener() {
            @Override
            public void onLocationChanged(@NonNull Location location) {

                // decoding the location
                mgeocoder=new Geocoder(getApplicationContext(), Locale.US);

                setLocationDetails(location);

                try {
                    List<Address> mlist=mgeocoder.getFromLocation(location.getLatitude(),location.getLongitude(),1);
                    mlocality=mlist.get(0).getSubLocality()+", "+mlist.get(0).getLocality();
                    Log.i("Location",mlocality);

                    showLocationDetails();

                }catch (Exception e){
                    Log.i("Location","ERROR:"+e);
                }

            }
        };

        //Getting permissions
        //if permission not granted
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            //Asking permissions
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},1);
        }
        else{
            mlocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,10000,5,mlocationListner);
        }

    }

    private void setLocationDetails(Location location){

        // Setting Details
        mlongitude= String.valueOf((int)location.getLongitude())+"°";
        mlatitude= String.valueOf((int)location.getLatitude())+"°";
        maltitude=String.valueOf((int)location.getAltitude())+" Mts";
        maccuracy=String.valueOf((int)location.getAccuracy())+"%";

    }

    private void showLocationDetails(){

        TextView list1=(TextView)findViewById(R.id.list1);
        TextView list2=(TextView)findViewById(R.id.list2);
        TextView list3=(TextView)findViewById(R.id.list3);
        TextView list4=(TextView)findViewById(R.id.list4);


        CardView mlists=(CardView) findViewById(R.id.card);
        mlists.animate().alpha(1f).setDuration(2000);

        //Hiding progress bar
        ProgressBar mspinner=(ProgressBar) findViewById(R.id.progressBar);
        mspinner.setVisibility(View.INVISIBLE);

        list1.setText("Lat: "+mlatitude+"  Lon: "+mlongitude);
        list1.animate().alpha(1).setDuration(1000);

        list2.setText("Altitude: "+maltitude);
        list2.animate().alpha(1).setDuration(1000);

        list3.setText("Location: "+mlocality);
        list3.animate().alpha(1).setDuration(1000);

        list4.setText("Accuracy: "+maccuracy);
        list4.animate().alpha(1).setDuration(1000);

    }

}