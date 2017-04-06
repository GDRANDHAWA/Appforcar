package com.example.dell.appforcar;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {
    EditText et1, et2, et3, et4, et5;
    String st1, st2, st3, st4, st5;
    Button bt2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        et1 = (EditText) findViewById(R.id.s2);
        et2 = (EditText) findViewById(R.id.s4);
        et3 = (EditText) findViewById(R.id.s6);
        et4 = (EditText) findViewById(R.id.s8);
        et5 = (EditText) findViewById(R.id.s10);


    }

    public void getstart(View view) {

        Button b = (Button) view;
        b.setText("stop");

        st1 = et1.getText().toString();
        st2 = et2.getText().toString();
        st3 = et3.getText().toString();
        st4 = et4.getText().toString();
        st5 = et5.getText().toString();

        if (st2.equals("admin") && st3.equals("123467")) {


            LocationManager mLocationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            mLocationManager.requestLocationUpdates(
                    LocationManager.NETWORK_PROVIDER, 0, 0, new LocationListener() {
                        @Override
                        public void onStatusChanged(String provider, int status, Bundle extras) {
                        }

                        @Override
                        public void onProviderEnabled(String provider) {
                        }

                        @Override
                        public void onProviderDisabled(String provider) {
                        }

                        @Override
                        public void onLocationChanged(final Location location) {

                            double lat = location.getLatitude();

                            double lng = location.getLongitude();

                            JSONObject job = new JSONObject();
                            Calendar c = Calendar.getInstance();

                            int hours = c.get(Calendar.HOUR_OF_DAY);
                            int minute = c.get(Calendar.MINUTE);
                            int seconds = c.get(Calendar.SECOND);

                            int day = c.get(Calendar.DAY_OF_MONTH);
                            int month=c.get(Calendar.MONTH)+1;
                            int year= c.get(Calendar.YEAR);


                            String time = String.valueOf(hours) + "-" + String.valueOf(minute) + "-" + String.valueOf(seconds);
                                  String date = String.valueOf(day) + "/" + String.valueOf(month) + "/" + String.valueOf(year);

                            System.out.println(time +" "+ String.valueOf(lat) + "/" + String.valueOf(lng));

                            try {
                                job.put("lat_key", String.valueOf(lat));
                                job.put("lng_key", String.valueOf(lng));
                                job.put("vehicle_no", st4);
                                job.put("time_key", time);
                                job.put("dte_key", date);

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                            JsonObjectRequest jobjreq = new JsonObjectRequest("http://192.168.0.56/appforcar/appcar.php", job, new Response.Listener<JSONObject>() {
                                @Override
                                public void onResponse(JSONObject response) {


                                }
                            }, new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    System.out.println(error);
                                }
                            });

                            jobjreq.setRetryPolicy(new DefaultRetryPolicy(20000 , 2,2));

                            AppController app = new AppController(MainActivity.this);

                            app.addToRequestQueue(jobjreq);



                        }


                    });
        }
    }
}




