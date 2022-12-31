package com.example.kasirroti.AuthScreen;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.camera.core.Camera;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.FocusMeteringAction;
import androidx.camera.core.ImageCapture;
import androidx.camera.core.MeteringPoint;
import androidx.camera.core.MeteringPointFactory;
import androidx.camera.core.Preview;
import androidx.camera.core.SurfaceOrientedMeteringPointFactory;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.camera.view.PreviewView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.example.kasirroti.Helper.VolleyMultipartRequest;
import com.example.kasirroti.HomeActivity;
import com.example.kasirroti.LaporanScreen.ExportActivity;
import com.example.kasirroti.R;
import com.example.kasirroti.Server.Server;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.common.util.concurrent.ListenableFuture;
import com.shashank.sony.fancytoastlib.FancyToast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

public class FotoTutupActivity extends AppCompatActivity {
    private ImageView img_hasil, img_take, img_simpan, img_batal, img_change, img_akhir, img_map;
    private TextView txt_lokasi;
    private ConstraintLayout layout_lokasi;
    private LinearLayout linear_aksi;
    private ConstraintLayout layout_take;

    PreviewView previewView;
    ProcessCameraProvider cameraProvider;
    Preview preview;
    ImageCapture imageCapture;
    CameraSelector cameraSelector   = CameraSelector.DEFAULT_BACK_CAMERA;
    String isi = "", currentPhotoPath;
    private Camera camera;
    OutputStream fos = null;

    FusedLocationProviderClient fusedLocationProviderClient;
    MapView mapFragment;
    LatLng latLng;
    GoogleMap gMap;

    Bitmap bitmap2;
    double latitude, longitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutup_toko);

        txt_lokasi          = findViewById(R.id.txt_lokasi);
        img_hasil           = findViewById(R.id.img_hasil);
        img_take            = findViewById(R.id.img_take);
        img_simpan          = findViewById(R.id.img_simpan);
        img_batal           = findViewById(R.id.img_batal);
        img_batal           = findViewById(R.id.img_batal);
        img_change          = findViewById(R.id.img_change);
        img_map             = findViewById(R.id.img_map);
        img_akhir           = findViewById(R.id.img_akhir);
        layout_lokasi       = findViewById(R.id.layout_lokasi);
        linear_aksi         = findViewById(R.id.linear_aksi);
        layout_take         = findViewById(R.id.layout_take);
        previewView         = findViewById(R.id.frame_camera_capture);
        mapFragment         = findViewById(R.id.map);
        mapFragment.onCreate(savedInstanceState);

        startCamera();

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            return;
        }
        fusedLocationProviderClient.getLastLocation()
                .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(@NonNull Location location) {
                        if (location != null) {
                            latitude = location.getLatitude();
                            longitude = location.getLongitude();

                            mapFragment.getMapAsync(new OnMapReadyCallback() {
                                @Override
                                public void onMapReady(@NonNull GoogleMap googleMap) {
                                    latLng = new LatLng(location.getLatitude(), location.getLongitude());
                                    MarkerOptions markerOptions = new MarkerOptions();
                                    markerOptions.position(latLng);
                                    googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));
                                    googleMap.addMarker(markerOptions);
                                    googleMap.getUiSettings().setMapToolbarEnabled(false);

                                    gMap = googleMap;

                                    Geocoder geocoder = new Geocoder(FotoTutupActivity.this, Locale.getDefault());
                                    try {
                                        List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                                        txt_lokasi.setText(addresses.get(0).getAddressLine(0));
                                        Log.d("Lokasi", addresses.toString());
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }

                                }
                            });
                        }
                        else {
                            Toast.makeText(FotoTutupActivity.this, "Lokasi tidak ditemukan!", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

        img_take.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                takepoto();
            }
        });

        img_simpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                kirimFoto();
            }
        });

        img_batal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startCamera();
                img_akhir.setVisibility(View.GONE);
                img_hasil.setVisibility(View.GONE);
                layout_lokasi.setVisibility(View.VISIBLE);
                layout_take.setVisibility(View.VISIBLE);
                linear_aksi.setVisibility(View.GONE);
            }
        });

        img_change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(cameraSelector == CameraSelector.DEFAULT_BACK_CAMERA){
                    cameraSelector = CameraSelector.DEFAULT_FRONT_CAMERA;
                }else{
                    cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA;
                }
                try{
                    cameraProvider.unbindAll();
                    startCamera();
                }catch (Exception e){

                }
            }
        });
    }

    private void startCamera() {
        ListenableFuture<ProcessCameraProvider> cameraProviderFuture = ProcessCameraProvider.getInstance(this);
        cameraProviderFuture.addListener(()->{
            try{
                cameraProvider  = cameraProviderFuture.get();
                preview         = new Preview.Builder().build();
                imageCapture    = new ImageCapture.Builder().setCaptureMode(ImageCapture.CAPTURE_MODE_MINIMIZE_LATENCY).setFlashMode(ImageCapture.FLASH_MODE_OFF).build();
                Preview.SurfaceProvider surfaceProvider = previewView.getSurfaceProvider();
                preview.setSurfaceProvider(surfaceProvider);

                try {
                    cameraProvider.unbindAll();
                    camera = cameraProvider.bindToLifecycle(this, cameraSelector, preview, imageCapture);
                    //AutoFocus Every X Seconds
                    MeteringPointFactory AFfactory = new SurfaceOrientedMeteringPointFactory((float) previewView.getWidth(),(float) previewView.getHeight());
                    float centerWidth = (float) previewView.getWidth();
                    float centerHeight = (float) previewView.getHeight();
                    MeteringPoint AFautoFocusPoint = AFfactory.createPoint(centerWidth, centerHeight);
                    try {
                        FocusMeteringAction action = new FocusMeteringAction.Builder(AFautoFocusPoint,FocusMeteringAction.FLAG_AF).setAutoCancelDuration(1, TimeUnit.SECONDS).build();
                    }catch (Exception e){

                    }

                }catch (Exception e){
                    Toast.makeText(this,"Failed",Toast.LENGTH_SHORT).show();
                }
            }catch (ExecutionException | InterruptedException e){

            }
        }, ContextCompat.getMainExecutor(this));
    }

    private void takepoto()  {
        Bitmap bmp = previewView.getBitmap();
        img_hasil.setVisibility(View.VISIBLE);
//        Log.d("outputreslut",""+imageUri);
//        resolver.delete(imageUri,null,null);
        img_hasil.setImageBitmap(bmp);
        layout_lokasi.setDrawingCacheEnabled(true);
        bitmap2      = Bitmap.createBitmap(layout_lokasi.getWidth(), layout_lokasi.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas can          = new Canvas(bitmap2);
        Drawable bgDraw     = layout_lokasi.getBackground();
        if (bgDraw != null){
            bgDraw.draw(can);
        } else {
            can.drawColor(Color.TRANSPARENT);
        }
        layout_lokasi.draw(can);
        img_akhir.setImageBitmap(bitmap2);
        img_akhir.setVisibility(View.VISIBLE);
        img_hasil.setVisibility(View.GONE);
        layout_lokasi.setVisibility(View.GONE);
        layout_take.setVisibility(View.GONE);
        linear_aksi.setVisibility(View.VISIBLE);

    }

    public void kirimFoto(){
        Dialog dialog       = new Dialog(FotoTutupActivity.this);
        dialog.setContentView(R.layout.loading);
        dialog.show();
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.setCancelable(false);

        TextView txt_title  = dialog.findViewById(R.id.txt_title);
        txt_title.setText("Mengirim foto");

        VolleyMultipartRequest volleyMultipartRequest = new VolleyMultipartRequest(Request.Method.POST, Server.URL + "foto_last_order",
                new Response.Listener<NetworkResponse>() {
                    @Override
                    public void onResponse(NetworkResponse response) {
                        String resultResponse = new String(response.data);
                        try {
                            JSONObject jsonObject = new JSONObject(resultResponse);
                            String respon = jsonObject.getString("response");
                            String message = jsonObject.getString("message");
                            if (respon.equals("200")){
                                FancyToast.makeText(
                                        FotoTutupActivity.this,
                                        "Foto berhasil dikirim",
                                        FancyToast.LENGTH_SHORT,
                                        FancyToast.SUCCESS,
                                        false
                                ).show();
                                startActivity(new Intent(FotoTutupActivity.this, ExportActivity.class));
                                finish();
                            }else {
                                FancyToast.makeText(
                                        FotoTutupActivity.this,
                                        message,
                                        FancyToast.LENGTH_SHORT,
                                        FancyToast.ERROR,
                                        false
                                ).show();
                            }
                            dialog.dismiss();
                        } catch (JSONException e){
                            e.printStackTrace();
                            FancyToast.makeText(
                                    FotoTutupActivity.this,
                                    e.getMessage(),
                                    FancyToast.LENGTH_SHORT,
                                    FancyToast.ERROR,
                                    false
                            ).show();
                            dialog.dismiss();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                FancyToast.makeText(
                        FotoTutupActivity.this,
                        error.getMessage(),
                        FancyToast.LENGTH_SHORT,
                        FancyToast.ERROR,
                        false
                ).show();
                dialog.dismiss();

            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("Accept", "application/json");
                headers.put("Authorization", "Bearer " + HomeActivity.token);
                return headers;
            }

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("id_outlet",HomeActivity.id_outlet);
                return params;
            }

            @Override
            protected Map<String, DataPart> getByteData() {
                Map<String, DataPart> params = new HashMap<>();
                params.put("foto", new DataPart(HomeActivity.nama + ".jpg", getFileDataFromDrawable(bitmap2)));
                return params;
            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(volleyMultipartRequest);
        volleyMultipartRequest.setRetryPolicy(new DefaultRetryPolicy(
                0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
    }



    public byte[] getFileDataFromDrawable(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 90, byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();
    }
}