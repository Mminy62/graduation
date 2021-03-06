package com.example.proj_graduation;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.HandlerThread;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.PixelCopy;
import android.view.View;
import android.view.ViewManager;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.FragmentManager;

import com.google.android.material.snackbar.Snackbar;
import com.google.ar.core.Anchor;
import com.google.ar.core.Pose;
import com.google.ar.core.TrackingState;
import com.google.ar.sceneform.AnchorNode;
import com.google.ar.sceneform.ArSceneView;
import com.google.ar.sceneform.FrameTime;
import com.google.ar.sceneform.Node;
import com.google.ar.sceneform.math.Vector3;
import com.google.ar.sceneform.rendering.ModelRenderable;
import com.google.ar.sceneform.ux.ArFragment;
import com.naver.maps.geometry.LatLng;
import com.naver.maps.map.CameraUpdate;
import com.naver.maps.map.LocationTrackingMode;
import com.naver.maps.map.MapFragment;
import com.naver.maps.map.NaverMap;
import com.naver.maps.map.OnMapReadyCallback;
import com.naver.maps.map.UiSettings;
import com.naver.maps.map.overlay.LocationOverlay;
import com.naver.maps.map.overlay.Marker;
import com.naver.maps.map.overlay.OverlayImage;
import com.naver.maps.map.util.FusedLocationSource;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static android.hardware.SensorManager.AXIS_X;
import static android.hardware.SensorManager.AXIS_Z;

public class MainActivity extends AppCompatActivity
        implements OnMapReadyCallback,
        ActivityCompat.OnRequestPermissionsResultCallback,
        SensorEventListener {

    private static final String TAG = "MainActivity";

    private FrameLayout popupLayout;

    // ????????? ?????? ??????
    private static final int PERMISSION_REQUEST_CODE = 100;
    private static final String[] PERMISSIONS = {
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
    };

    private FusedLocationSource mLocationSource;
    private NaverMap mNaverMap;

    // ?????? ??????
    public Location mCurrentLocation;

    // ?????? ??????
    private Location destination;
    private final Location[] markers = new Location[3];
    private final Location[] handLocation = new Location[2];

    // ar ??????
    private View mLayout;  // Snackbar ???????????? ???????????? View??? ???????????????.
    // (????????? Toast????????? Context??? ??????????????????.)

    // ????????? ArCamera??? ?????? ?????? ??????
    private ArFragment arFragment;
    private ArSceneView arSceneView;
    private AnchorNode destAnchor;
    private AnchorNode videoAnchor;
    private final AnchorNode[] mAnchorNode = new AnchorNode[3];

    private ModelRenderable destRenderable;
    private ModelRenderable handRenderable; //hand model

    private final ModelRenderable[] nodeRenderable = new ModelRenderable[3]; // CourseNode Renderable Object

    // Device Orientation ??????
    private SensorManager mSensorManager;
    private Sensor mAccelerometer;
    private Sensor mMagnetometer;
    private final float[] mLastAccelerometer = new float[3];
    private final float[] mLastMagnetometer = new float[3];
    private boolean mLastAccelerometerSet = false;
    private boolean mLastMagnetometerSet = false;
    private float mCurrentAzim = 0f; // ?????????
    private float mCurrentPitch = 0f; // ??????
    private float mCurrentRoll = 0f; // ???
    Context context;

    private ImageView filter01;
    private ImageView filter02;
    private ImageView filter03;

    //   private PointHand pointHand;
    public static com.example.proj_graduation.MainActivity ma;

    int filterID = 0;
    boolean isCapturing = false;

    private final boolean[] call = {true, true, true};

    @SuppressLint("InflateParams")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON,
                WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        ma = this;

        // Device Orientation ??????
        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mMagnetometer = mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);

        // ???????????? ????????????
        mLayout = findViewById(R.id.layout_main);

        // ????????? ??????
        markers[0] = new Location("point A"); //
        markers[0].setLatitude(37.29388);
        markers[0].setLongitude(126.975678);
        // ????????? ??????
        markers[1] = new Location("point B");
        markers[1].setLatitude(37.294073);
        markers[1].setLongitude(126.975709);

        // ????????? ??????
        markers[2] = new Location("point C");
        markers[2].setLatitude(37.294287);
        markers[2].setLongitude(126.975741);

        // ?????? ??????
        destination = new Location("Jng-ang highschool");
        destination.setLatitude(37.29451);
        destination.setLongitude(126.975773);

        // ar ??????
        arFragment = (ArFragment) getSupportFragmentManager().findFragmentById(R.id.arCamera);
        arSceneView = arFragment.getArSceneView();
        setUpModel();

        arFragment.getArSceneView().getScene().setOnUpdateListener(this::onSceneUpdate);

        Intent intent = getIntent();
        isCapturing = intent.getBooleanExtra("capturing", false);

        if (isCapturing) {
            //filter layout
            LayoutInflater inflater02 = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            LinearLayout ll2 = (LinearLayout) inflater02.inflate(R.layout.character_filter, null);
            ll2.setBackgroundColor(Color.parseColor("#00000000"));
            LinearLayout.LayoutParams paramll2 = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT
            );
            addContentView(ll2, paramll2);

            //filter ??????
            filter01 = findViewById(R.id.filter_img01);
            filter02 = findViewById(R.id.filter_img02);
            filter03 = findViewById(R.id.filter_img03);

            filter01.setVisibility(View.INVISIBLE);
            filter02.setVisibility(View.INVISIBLE);
            filter03.setVisibility(View.INVISIBLE);

            Button filterBtn = findViewById(R.id.select_btn);
            Button cameraBtn = findViewById(R.id.camera_btn);

            filterBtn.setOnClickListener(v -> {
                filterID = (filterID + 1) % 3;
                CharacterFilter(filterID);
            });

            cameraBtn.setOnClickListener(v -> {
                cameraBtn.setVisibility(View.INVISIBLE);
                filterBtn.setVisibility(View.INVISIBLE);

                View content = getWindow().getDecorView().getRootView();
                content.setDrawingCacheEnabled(true);
                getBitmapFromView(arSceneView);

                cameraBtn.setVisibility(View.VISIBLE);
                filterBtn.setVisibility(View.VISIBLE);
            });
        } else {

            // ??????????????? ?????? ????????? ????????? ??????
            LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            // ???????????? ?????? ??????
            LinearLayout ll = (LinearLayout) inflater.inflate(R.layout.navermap, null);
            // ???????????? ?????? ????????? ??????
            ll.setBackgroundColor(Color.parseColor("#00000000"));
            // ???????????? ?????? ?????????
            LinearLayout.LayoutParams paramll = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT
            );
            addContentView(ll, paramll);
            //ll.setVisibility(View.INVISIBLE);

            // '????????? ?????? ???' ????????? ????????????
            popupLayout = (FrameLayout) inflater.inflate(R.layout.gps_loading, null);
            popupLayout.setBackgroundColor(Color.parseColor("#CC000000"));
            FrameLayout.LayoutParams popParams = new FrameLayout.LayoutParams(
                    FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT
            );
            addContentView(popupLayout, popParams);


            // ?????? ?????? ??????
            FragmentManager fm = getSupportFragmentManager();
            MapFragment mapFragment = (MapFragment) fm.findFragmentById(R.id.map);
            if (mapFragment == null) {
                mapFragment = MapFragment.newInstance();
                fm.beginTransaction().add(R.id.map, mapFragment).commit();
            }

            // getMapAsync??? ???????????? ???????????? onMapReady ?????? ????????? ??????
            // onMapReady?????? NaverMap ????????? ??????
            mapFragment.getMapAsync(this);

            // ????????? ???????????? ???????????? FusedLocationSource ??????
            mLocationSource =
                    new FusedLocationSource(this, PERMISSION_REQUEST_CODE);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        arFragment.getPlaneDiscoveryController().hide();
        arFragment.getPlaneDiscoveryController().setInstructionView(null);

        mSensorManager.registerListener(this, mAccelerometer, SensorManager.SENSOR_DELAY_UI);
        mSensorManager.registerListener(this, mMagnetometer, SensorManager.SENSOR_DELAY_UI);

        Intent intent = getIntent();
        isCapturing = intent.getBooleanExtra("capturing", false);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mSensorManager.unregisterListener(this, mAccelerometer);
        mSensorManager.unregisterListener(this, mMagnetometer);

    }

    private void setUpModel() {

        ModelRenderable.builder()
                .setSource(this, R.raw.hand)
                .build().thenAccept(renderable -> handRenderable = renderable)
                .exceptionally(
                        throwable -> {
                            Toast.makeText(this, "Unable to load hand logo model", Toast.LENGTH_SHORT).show();
                            return null;
                        }
                );

        ModelRenderable.builder()
                .setSource(this, R.raw.ourbeloved_1)
                .build().thenAccept(renderable -> nodeRenderable[0] = renderable)
                .exceptionally(
                        throwable -> {
                            Toast.makeText(this, "Unable to load orange note model", Toast.LENGTH_SHORT).show();
                            return null;
                        }
                );

        ModelRenderable.builder()
                .setSource(this, R.raw.ourbeloved_2)
                .build().thenAccept(renderable -> nodeRenderable[1] = renderable)
                .exceptionally(
                        throwable -> {
                            Toast.makeText(this, "Unable to load red note model", Toast.LENGTH_SHORT).show();
                            return null;
                        }
                );

        ModelRenderable.builder()
                .setSource(this, R.raw.ourbeloved_3)
                .build().thenAccept(renderable -> nodeRenderable[2] = renderable)
                .exceptionally(
                        throwable -> {
                            Toast.makeText(this, "Unable to load albumRenderable 1 model", Toast.LENGTH_SHORT).show();
                            return null;
                        }
                );

     /*   ModelRenderable.builder()
                .setSource(this, R.raw.ourbeloved_4)
                .build().thenAccept(renderable -> albumRenderable[4] = renderable)
                .exceptionally(
                        throwable -> {
                            Toast.makeText(this, "Unable to load albumRenderable 2 model", Toast.LENGTH_SHORT).show();
                            return null;
                        }
                );
*/
        ModelRenderable.builder()
                .setSource(this, R.raw.highschool)
                .build().thenAccept(renderable -> destRenderable = renderable)
                .exceptionally(
                        throwable -> {
                            Toast.makeText(this, "Unable to load albumRenderable 3 model", Toast.LENGTH_SHORT).show();
                            return null;
                        }
                );
    }

    @SuppressLint("RtlHardcoded")
    @Override
    public void onMapReady(@NonNull NaverMap naverMap) {
        Log.d(TAG, "onMapReady");

        // ?????? ??????
        for(int i=0; i<3; i++) {
            Marker marker = new Marker();
            marker.setPosition(new LatLng(markers[i].getLatitude(), markers[i].getLongitude()));
            marker.setHeight(60);
            marker.setWidth(60);
            marker.setIcon(OverlayImage.fromResource(R.drawable.loc_logo));
            marker.setAnchor(new PointF(0.5f, 1));
            marker.setMap(naverMap);
        }

        Marker logo = new Marker();
        logo.setPosition(new LatLng(destination.getLatitude(), destination.getLongitude()));
        logo.setHeight(60);
        logo.setWidth(60);
        logo.setIcon(OverlayImage.fromResource(R.drawable.loc_logo));
        logo.setAnchor(new PointF(0.5f, 0.5f));
        logo.setMap(naverMap);

        // NaverMap ?????? ????????? NaverMap ????????? ?????? ?????? ??????
        mNaverMap = naverMap;
        mNaverMap.setLocationSource(mLocationSource);

        // UI ????????? ?????????
        UiSettings uiSettings = mNaverMap.getUiSettings();
        uiSettings.setCompassEnabled(false); // ????????? : true
        uiSettings.setScaleBarEnabled(false); // ????????? : true
        uiSettings.setZoomControlEnabled(false); // ????????? : true
        uiSettings.setLocationButtonEnabled(false); // ????????? : false
        uiSettings.setLogoGravity(Gravity.LEFT | Gravity.BOTTOM);
        uiSettings.setLogoMargin(0, 0, 0, -5);

        CameraUpdate cameraUpdate = CameraUpdate.zoomTo(15);
        mNaverMap.moveCamera(cameraUpdate);
        mNaverMap.setLocationTrackingMode(LocationTrackingMode.Follow);
        mNaverMap.setLiteModeEnabled(true);

        LocationOverlay locationOverlay = mNaverMap.getLocationOverlay();
        locationOverlay.setIconWidth(40);
        locationOverlay.setIconHeight(40);

        locationOverlay.setSubIconWidth(40);
        locationOverlay.setSubIconHeight(40);
        locationOverlay.setSubAnchor(new PointF(0.5f, 0.9f));

        mNaverMap.addOnLocationChangeListener(location ->
                mCurrentLocation = location);
        // ????????????. ????????? onRequestPermissionsResult ?????? ????????? ??????
        ActivityCompat.requestPermissions(this, PERMISSIONS, PERMISSION_REQUEST_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        // request code??? ???????????? ?????? ??????
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                mNaverMap.setLocationTrackingMode(LocationTrackingMode.Follow);
            }
        }
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor == mAccelerometer) {
            System.arraycopy(event.values, 0, mLastAccelerometer, 0, event.values.length);
            mLastAccelerometerSet = true;
        } else if (event.sensor == mMagnetometer) {
            System.arraycopy(event.values, 0, mLastMagnetometer, 0, event.values.length);
            mLastMagnetometerSet = true;
        }
        if (mLastAccelerometerSet && mLastMagnetometerSet) {
            float[] rotationMatrix = new float[9];
            SensorManager.getRotationMatrix(rotationMatrix, null, mLastAccelerometer, mLastMagnetometer);

            float[] adjustedRotationMatrix = new float[9];
            SensorManager.remapCoordinateSystem(rotationMatrix, AXIS_X, AXIS_Z, adjustedRotationMatrix);
            float[] orientation = new float[3];
            SensorManager.getOrientation(adjustedRotationMatrix, orientation);

            mCurrentAzim = orientation[0]; // ????????? (?????????)
            mCurrentPitch = orientation[1]; // ??????
            mCurrentRoll = orientation[2]; // ???
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    private void onSceneUpdate(FrameTime frameTime) {
        // ?????? ????????? ???????????? ???
        if(destAnchor != null) {
            if (destAnchor.getAnchor().getTrackingState() != TrackingState.TRACKING
                    && arSceneView.getArFrame().getCamera().getTrackingState() == TrackingState.TRACKING) {
                // Detach the old anchor
                List<Node> children = new ArrayList<>(destAnchor.getChildren());
                for (Node n : children) {
                    Log.d(TAG, "find node list");
                    if (n instanceof Destination) {
                        Log.d(TAG, "removed");
                        destAnchor.removeChild(n);
                        n.setParent(null);
                    }
                }
                arSceneView.getScene().removeChild(destAnchor);
                destAnchor.getAnchor().detach();
                destAnchor.setParent(null);
                destAnchor = null;
            }
        }

        for (int i = 0; i < 3; i++) {
            if (mAnchorNode[i] != null) {
                if(call[i]){

                    // ???????????? ??????????????? ??????????????? (????????? ????????? ???????????????)
                    if (mAnchorNode[i].getAnchor().getTrackingState() != TrackingState.TRACKING
                            && arSceneView.getArFrame().getCamera().getTrackingState() == TrackingState.TRACKING) {
                        // Detach the old anchor
                        List<Node> children = new ArrayList<>(mAnchorNode[i].getChildren());
                        for (Node n : children) {
                            Log.d(TAG, "find node list");
                            if (n instanceof CourseNode) {
                                Log.d(TAG, "removed");
                                mAnchorNode[i].removeChild(n);
                                n.setParent(null);
                            }
                        }
                        arSceneView.getScene().removeChild(mAnchorNode[i]);
                        mAnchorNode[i].getAnchor().detach();
                        mAnchorNode[i].setParent(null);
                        mAnchorNode[i] = null;
                    }
                }
            }
        }

        if( destAnchor != null && mAnchorNode[0] != null && mAnchorNode[1] != null && mAnchorNode[2] != null ){
            return;
        }

        if (mCurrentLocation == null) {
            Log.d(TAG, "Location is null");
            return;
        }
        else{
            if(popupLayout == null || popupLayout.getParent() != null)
                ((ViewManager)popupLayout.getParent()).removeView(popupLayout);
        }

        if (destRenderable == null) {
            Log.d(TAG, "onUpdate: dest logo Renderable is null");
            return;
        }

//        if (handRenderable == null) {
//            Log.d(TAG, "onUpdate: hand Renderable is null");
//            return;
//        }

//        for (ModelRenderable m : musicNotes) {
//            if (m == null) {
//                Log.d(TAG, "onUpdate: musicNotes Renderable is null");
//                return;
//            }
//        }

        for (ModelRenderable m : nodeRenderable) {
            if (m == null) {
                Log.d(TAG, "onUpdate: course Renderable is null");
                return;
            }
        }

        if (arSceneView.getArFrame() == null) {
            Log.d(TAG, "onUpdate: No frame available");
            // No frame available
            return;
        }

        if (arSceneView.getArFrame().getCamera().getTrackingState() != TrackingState.TRACKING) {
            Log.d(TAG, "onUpdate: Tracking not started yet");
            // Tracking not started yet
            return;
        }

        // ???????????? ??????!
        for (int i = 0; i < 3; i++) {
            if (mAnchorNode[i] == null) {
                //Log.d(TAG, "onUpdate: mAnchorNode["+ i +"] is null");
                // ??????????????? ??? gps??? ?????? ??????, ???????????? ?????? ????????? ???????????? ???????????? Pose??? ????????????!
                if (!createNode(i)) continue;
            }
        }

        // ????????? ?????? ???????????? ??????
        if(destAnchor == null){
            createLogo();
        }

    }

    public void createLogo(){
        float dLatitude = (float) (destination.getLatitude() - mCurrentLocation.getLatitude()) * 110900f;
        float dLongitude = (float) (destination.getLongitude() - mCurrentLocation.getLongitude()) * 88400f;

       // ????????? ??????

     /*   dLatitude = 20f;
        dLongitude = 0f; */

        float distance = (float) Math.sqrt((dLongitude * dLongitude) + (dLatitude * dLatitude));

        if (distance > 25) { // 25m?????? ?????? ???????????? ??????X
            return;
        }

        float height = 0.5f;
        Vector3 objVec = new Vector3(dLongitude, dLatitude, height);

        Vector3 xUnitVec;
        Vector3 yUnitVec;
        Vector3 zUnitVec;

        zUnitVec = new Vector3((float) (Math.cos(mCurrentPitch) * Math.sin(mCurrentAzim)), (float) (Math.cos(mCurrentPitch) * Math.cos(mCurrentAzim)), (float) (-Math.sin(mCurrentPitch)));
        zUnitVec = zUnitVec.normalized().negated();

        yUnitVec = new Vector3((float) (Math.sin(mCurrentPitch) * Math.sin(mCurrentAzim)), (float) (Math.sin(mCurrentPitch) * Math.cos(mCurrentAzim)), (float) (Math.cos(mCurrentPitch))).normalized();


        float wx = zUnitVec.x;
        float wy = zUnitVec.y;
        float wz = zUnitVec.z;

        float yx = yUnitVec.x;
        float yy = yUnitVec.y;
        float yz = yUnitVec.z;

        float t = 1 - (float) Math.cos(mCurrentRoll);
        float s = (float) Math.sin(mCurrentRoll);
        float c = (float) Math.cos(mCurrentRoll);

        float[][] rotMat = {{wx * wx * t + c, wx * wy * t + wz * s, wx * wz * t - wy * s},
                {wy * wx * t - wz * s, wy * wy * t + c, wy * wz * t + wx * s},
                {wz * wx * t + wy * s, wz * wy * t - wx * s, wz * wz * t + c}};

        yUnitVec = new Vector3(yx * rotMat[0][0] + yy * rotMat[0][1] + yz * rotMat[0][2],
                yx * rotMat[1][0] + yy * rotMat[1][1] + yz * rotMat[1][2],
                yx * rotMat[2][0] + yy * rotMat[2][1] + yz * rotMat[2][2]).normalized();


        xUnitVec = Vector3.cross(yUnitVec, zUnitVec).normalized();

        float xPos = Vector3.dot(objVec, xUnitVec);
        float yPos = Vector3.dot(objVec, yUnitVec);
        float zPos = Vector3.dot(objVec, zUnitVec);

        Vector3 xAxis = arSceneView.getScene().getCamera().getRight().normalized().scaled(xPos);
        Vector3 yAxis = arSceneView.getScene().getCamera().getUp().normalized().scaled(yPos);
        Vector3 zAxis = arSceneView.getScene().getCamera().getBack().normalized().scaled(zPos);
        Vector3 objectPos = new Vector3(xAxis.x + yAxis.x + zAxis.x, xAxis.y + yAxis.y + zAxis.y, xAxis.z + yAxis.z + zAxis.z);
        Vector3 cameraPos = arSceneView.getScene().getCamera().getWorldPosition();

        Vector3 position = Vector3.add(cameraPos, objectPos);

        // Create an ARCore Anchor at the position.
        Pose pose = Pose.makeTranslation(position.x, position.y, position.z);
        Anchor anchor = arSceneView.getSession().createAnchor(pose);

        destAnchor = new AnchorNode(anchor);
        destAnchor.setParent(arSceneView.getScene());

        // ???????????? ????????? ????????????
        Vector3 v = new Vector3(0f, 0f, 1f);
        xPos = Vector3.dot(v, xUnitVec);
        yPos = Vector3.dot(v, yUnitVec);
        zPos = Vector3.dot(v, zUnitVec);

        xAxis = arSceneView.getScene().getCamera().getRight().normalized().scaled(xPos);
        yAxis = arSceneView.getScene().getCamera().getUp().normalized().scaled(yPos);
        zAxis = arSceneView.getScene().getCamera().getBack().normalized().scaled(zPos);

        Vector3 up = new Vector3(xAxis.x + yAxis.x + zAxis.x, xAxis.y + yAxis.y + zAxis.y, xAxis.z + yAxis.z + zAxis.z).normalized();

        Destination destination = new Destination(destAnchor, destRenderable, arSceneView);


        destination.setOnTapListener((hitTestResult, motionEvent) -> {
            Intent intent = new Intent(getApplicationContext(), PopupActivity.class);
            startActivity(intent);
        });

        Snackbar.make(mLayout, "?????? ???????????? ?????? (distance: " + distance + "m)", Snackbar.LENGTH_SHORT).show();
    }

    // ?????? ???????????? ?????? ??????
    public boolean createNode(int i) {

        float dLatitude = (float) (markers[i].getLatitude() - mCurrentLocation.getLatitude()) * 110900f;
        float dLongitude = (float) (markers[i].getLongitude() - mCurrentLocation.getLongitude()) * 88400f;

        // ????????? ??????

      /*  if( i == 0 ) {
            dLatitude = 3f;
            dLongitude = 0f;
            return false;
        }
        else if ( i == 1 ){
            dLatitude = -3f;
            dLongitude = 0f;
        }
        else{
            dLatitude = 0f;
            dLongitude = 3f;
        }
        */

        float distance = (float) Math.sqrt((dLongitude * dLongitude) + (dLatitude * dLatitude));

        if (distance > 15) { // 15m?????? ?????? ???????????? ??????X
            return false;
        }
        float height = -0.5f;
        Vector3 objVec = new Vector3(dLongitude, dLatitude, height);

        Vector3 xUnitVec;
        Vector3 yUnitVec;
        Vector3 zUnitVec;

        zUnitVec = new Vector3((float) (Math.cos(mCurrentPitch) * Math.sin(mCurrentAzim)), (float) (Math.cos(mCurrentPitch) * Math.cos(mCurrentAzim)), (float) (-Math.sin(mCurrentPitch)));
        zUnitVec = zUnitVec.normalized().negated();

        yUnitVec = new Vector3((float) (Math.sin(mCurrentPitch) * Math.sin(mCurrentAzim)), (float) (Math.sin(mCurrentPitch) * Math.cos(mCurrentAzim)), (float) (Math.cos(mCurrentPitch))).normalized();

        float wx = zUnitVec.x;
        float wy = zUnitVec.y;
        float wz = zUnitVec.z;

        float yx = yUnitVec.x;
        float yy = yUnitVec.y;
        float yz = yUnitVec.z;

        float t = 1 - (float) Math.cos(mCurrentRoll);
        float s = (float) Math.sin(mCurrentRoll);
        float c = (float) Math.cos(mCurrentRoll);

        float[][] rotMat = {{wx * wx * t + c, wx * wy * t + wz * s, wx * wz * t - wy * s},
                {wy * wx * t - wz * s, wy * wy * t + c, wy * wz * t + wx * s},
                {wz * wx * t + wy * s, wz * wy * t - wx * s, wz * wz * t + c}};

        yUnitVec = new Vector3(yx * rotMat[0][0] + yy * rotMat[0][1] + yz * rotMat[0][2],
                yx * rotMat[1][0] + yy * rotMat[1][1] + yz * rotMat[1][2],
                yx * rotMat[2][0] + yy * rotMat[2][1] + yz * rotMat[2][2]).normalized();


        xUnitVec = Vector3.cross(yUnitVec, zUnitVec).normalized();

        float xPos = Vector3.dot(objVec, xUnitVec);
        float yPos = Vector3.dot(objVec, yUnitVec);
        float zPos = Vector3.dot(objVec, zUnitVec);

        Vector3 xAxis = arSceneView.getScene().getCamera().getRight().normalized().scaled(xPos);
        Vector3 yAxis = arSceneView.getScene().getCamera().getUp().normalized().scaled(yPos);
        Vector3 zAxis = arSceneView.getScene().getCamera().getBack().normalized().scaled(zPos);
        Vector3 objectPos = new Vector3(xAxis.x + yAxis.x + zAxis.x, xAxis.y + yAxis.y + zAxis.y, xAxis.z + yAxis.z + zAxis.z);
        Vector3 cameraPos = arSceneView.getScene().getCamera().getWorldPosition();

        Vector3 position = Vector3.add(cameraPos, objectPos);

        // Create an ARCore Anchor at the position.
        Pose pose = Pose.makeTranslation(position.x, position.y, position.z);
        Anchor anchor = arSceneView.getSession().createAnchor(pose);

        mAnchorNode[i] = new AnchorNode(anchor);
        mAnchorNode[i].setParent(arSceneView.getScene());

        // ???????????? ????????? ????????????
        Vector3 v = new Vector3(0f, 0f, 1f);
        xPos = Vector3.dot(v, xUnitVec);
        yPos = Vector3.dot(v, yUnitVec);
        zPos = Vector3.dot(v, zUnitVec);

        xAxis = arSceneView.getScene().getCamera().getRight().normalized().scaled(xPos);
        yAxis = arSceneView.getScene().getCamera().getUp().normalized().scaled(yPos);
        zAxis = arSceneView.getScene().getCamera().getBack().normalized().scaled(zPos);

        Vector3 up = new Vector3(xAxis.x + yAxis.x + zAxis.x, xAxis.y + yAxis.y + zAxis.y, xAxis.z + yAxis.z + zAxis.z).normalized();

        CourseNode courseNode = new CourseNode(mAnchorNode[i], nodeRenderable[i], arSceneView);

   //   PointHand pointHand = new PointHand(mAnchorNode[i], handRenderable, arSceneView);

        Snackbar.make(mLayout, "???????????? ??????[" + i + "] (distance: " + distance + "m)", Snackbar.LENGTH_SHORT).show();

        return true;
    }

    public void CharacterFilter(int filterID){
        switch (filterID){
            case 0:
                filter01.setVisibility(View.VISIBLE);
                filter02.setVisibility(View.INVISIBLE);
                filter03.setVisibility(View.INVISIBLE);
                break;
            case 1:
                filter01.setVisibility(View.INVISIBLE);
                filter02.setVisibility(View.VISIBLE);
                filter03.setVisibility(View.INVISIBLE);
                break;
            case 2:
                filter01.setVisibility(View.INVISIBLE);
                filter02.setVisibility(View.INVISIBLE);
                filter03.setVisibility(View.VISIBLE);
                break;
        }

    }

    private String generateFilename() {
        String date =
                new SimpleDateFormat("yyyyMMddHHmmss", java.util.Locale.getDefault()).format(new Date());
        return Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_DCIM) + File.separator + "Sceneform/" + date + "_screenshot.png";
    }

    private Bitmap overlayBitmap(Bitmap baseBmp, View filter) {
        filter.setDrawingCacheEnabled(true);
        Bitmap bitmap2 = Bitmap.createBitmap(filter.getWidth(), filter.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas filterCanvas = new Canvas(bitmap2);
        filter.draw(filterCanvas);

        Bitmap resultBmp = Bitmap.createBitmap(baseBmp.getWidth(), baseBmp.getHeight(), baseBmp.getConfig());
        Canvas canvas = new Canvas(resultBmp);
        canvas.drawBitmap(baseBmp, new Matrix(), null);
        canvas.drawBitmap(bitmap2, new Matrix(), null);

        return resultBmp;
    }

    private void getBitmapFromView(ArSceneView view){
        Bitmap bitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(), Bitmap.Config.ARGB_8888);
        String filename = generateFilename();
        final HandlerThread handlerThread = new HandlerThread("PixelCopier");
        handlerThread.start();

        PixelCopy.request(view, bitmap, (copyResult) -> {
            if (copyResult == PixelCopy.SUCCESS) {
                try {
                    switch (filterID){
                        case 0:
                            saveBitmapToDisk(overlayBitmap(bitmap, filter01), filename);
                            break;
                        case 1:
                            saveBitmapToDisk(overlayBitmap(bitmap, filter02), filename);
                            break;
                        case 2:
                            saveBitmapToDisk(overlayBitmap(bitmap, filter03), filename);
                            break;

                    }
                } catch (IOException e) {
                    Toast toast = Toast.makeText(MainActivity.this, e.toString(),
                            Toast.LENGTH_LONG);
                    toast.show();
                    return;
                }
                Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content),
                        "Photo saved", Snackbar.LENGTH_LONG);
                snackbar.setAction("Open in Photos", v -> {
                    File file = new File(filename);

                    // ?????? ??????????????? ???????????? ???
                    Uri photoURI = FileProvider.getUriForFile(MainActivity.this,
                            MainActivity.this.getPackageName() + ".ar.codelab.name.provider",
                            file);
                    Intent intent = new Intent(Intent.ACTION_VIEW, photoURI);
                    intent.setDataAndType(photoURI, "image/*");
                    intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    startActivity(intent);
                });
                snackbar.show();
            } else {
                Toast toast = Toast.makeText(MainActivity.this,
                        "Failed to copyPixels: " + copyResult, Toast.LENGTH_LONG);
                toast.show();
            }
            handlerThread.quitSafely();
        }, new Handler(handlerThread.getLooper()));
    }

    private void saveBitmapToDisk(Bitmap bitmap, String filePath) throws IOException
    {
        File file = new File(filePath);
        // ???????????? ???????????? arcapture ???????????? ?????? ??? Bitmap??? JPEG ???????????? ???????????? ??????
        if (!file.getParentFile().exists()){
            file.getParentFile().mkdirs();
        }
        try (FileOutputStream outputStream = new FileOutputStream(file);
             ByteArrayOutputStream outputData = new ByteArrayOutputStream()){
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputData);
                outputData.writeTo(outputStream);
                outputStream.flush();

                Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                mediaScanIntent.setData(Uri.fromFile(file));
                getApplicationContext().sendBroadcast(mediaScanIntent);
        } catch (IOException ex) {
            throw new IOException("Failed to save bitmap to disk", ex);
        }
    }
}
