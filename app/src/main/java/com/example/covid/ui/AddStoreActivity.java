package com.example.covid.ui;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.BitmapCompat;

import com.example.covid.R;
import com.example.covid.data.Store;
import com.example.covid.data.Supply;
import com.example.covid.data.Supply.suppliesType;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.lang.reflect.Array;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.Map;

public class AddStoreActivity extends AppCompatActivity {

    //public enum suppliesType {SURGICAL_MASK, HAND_SANITIZER, BLEACH, RUBBING_ALCOHOL, RESPIRATOR, ISOLATION_CLOTHING}
    static final int REQUEST_IMAGE_CAPTURE = 1;
    private static final String TAG = "AddStoreActivity";

    private ProgressBar progress_add;
    private Button btn_add;
    private ImageButton btn_camera, btn_close;
    private TextInputLayout input_storeName, input_address, input_timeFrom, input_timeTo;
    private Spinner spinner_district;
    private ImageView img_tick;
    private Bitmap storeMap = null;

    private FirebaseFirestore db;
    FirebaseStorage storage;
    StorageReference storageRef;
    StorageReference storeRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_store);

        db = FirebaseFirestore.getInstance();
        storage = FirebaseStorage.getInstance();

        progress_add = findViewById(R.id.progress_add);
        btn_add = findViewById(R.id.btn_add);
        btn_camera = findViewById(R.id.btn_camera);
        btn_close = findViewById(R.id.btn_close);

        input_address = findViewById(R.id.input_address);
        input_storeName = findViewById(R.id.input_storeName);
        input_timeFrom = findViewById(R.id.input_timeFrom);
        input_timeTo = findViewById(R.id.input_timeTo);
        spinner_district = (Spinner) findViewById(R.id.spinner_district);
        img_tick = findViewById(R.id.img_tick);

        btn_add.setOnClickListener(v -> {
            if(isValidInfo()){
                progress_add.setVisibility(View.VISIBLE);
                uploadStore();
            }
        });

        btn_camera.setOnClickListener(v -> {
            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            }
        });

        btn_close.setOnClickListener(v -> {
            finish();
        });

        getSupportActionBar().hide();

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.district, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_district.setAdapter(adapter);

    }

    private boolean isValidInfo(){
        boolean valid = true;

        String storeName = input_storeName.getEditText().getText().toString();
        if (TextUtils.isEmpty(storeName)) {
            input_storeName.setError("Required.");
            valid = false;
        } else {
            input_storeName.setError(null);
        }

        String address = input_address.getEditText().getText().toString();
        if (TextUtils.isEmpty(storeName)) {
            input_address.setError("Required.");
            valid = false;
        } else {
            input_address.setError(null);
        }

        String from = input_timeFrom.getEditText().getText().toString();
        String to = input_timeTo.getEditText().getText().toString();

        if (TextUtils.isEmpty(from) && TextUtils.isEmpty(to)) {
            input_timeFrom.setError(null);
            input_timeTo.setError(null);
        } else if (TextUtils.isEmpty(from)){
            input_timeFrom.setError("Required.");
            valid = false;
        }else if (TextUtils.isEmpty(to)){
            input_timeTo.setError("Required.");
            valid = false;
        }else{
            try {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HHmm");
                LocalTime.parse(from, formatter);
                LocalTime.parse(to, formatter);
                input_timeFrom.setError(null);
                input_timeTo.setError(null);
            } catch (DateTimeParseException e) {
                input_timeFrom.setError("Wrong Format.");
                input_timeTo.setError("Wrong Format.");
                valid = false;
            }
        }
        return valid;
    }

    private void uploadStore(){
        // Add a new document with a generated id.
        Map<String, Object> data = new HashMap<>();
        data.put("name", input_storeName.getEditText().getText().toString());
        data.put("district", spinner_district.getSelectedItem().toString());
        data.put("address", input_address.getEditText().getText().toString());
        data.put("timeFrom", input_timeFrom.getEditText().getText().toString());
        data.put("timeTo", input_timeTo.getEditText().getText().toString());
        data.put("image", null);
        data.put("approved", false);

        Map<String, ArrayList> supplyType = new HashMap<>();
        ArrayList<Supply> supplies = new ArrayList<>();
        Supply supply = new Supply(suppliesType.SURGICAL_MASK, "", 0);

        supplies.add(supply);
        supplyType.put(suppliesType.SURGICAL_MASK.toString(), supplies);

        ArrayList masks = supplyType.get(suppliesType.SURGICAL_MASK.toString());

        data.put("supplies",supplyType);

        db.collection("stores")
                .add(data)
                .addOnSuccessListener(documentReference ->
                {
                    Log.d(TAG, "DocumentSnapshot written with ID: " + documentReference.getId());

                    //Upload Image and get download url
                    if (storeMap != null) {
                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        storeMap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                        byte[] byteArray = baos.toByteArray();
                        storeMap.recycle();
                        storeMap = null;

                        storageRef = storage.getReference();
                        storeRef = storageRef.child("store_" + documentReference.getId() + ".jpg");

                        UploadTask uploadTask = storeRef.putBytes(byteArray);
                        uploadTask.addOnFailureListener(exception -> {
                            // Handle unsuccessful uploads
                        }).addOnSuccessListener(taskSnapshot -> {
                            // taskSnapshot.getMetadata() contains file metadata such as size, content-type, etc.
                            // ...
                            Task<Uri> result = taskSnapshot.getStorage().getDownloadUrl();
                            result.addOnSuccessListener(uri -> documentReference.update("image", uri.toString())
                                    .addOnSuccessListener(aVoid -> {
                                        Log.d(TAG, "DocumentSnapshot successfully updated!");
                                        progress_add.setVisibility(View.INVISIBLE);
                                        finish();
                                    })
                                    .addOnFailureListener(e -> Log.w(TAG, "Error updating document", e)));
                        });
                    }else {
                        progress_add.setVisibility(View.INVISIBLE);
                        finish();
                    }
                })
                .addOnFailureListener(e -> Log.w(TAG, "Error adding document", e));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            storeMap = (Bitmap) extras.get("data");

            Matrix matrix = new Matrix();
            matrix.postRotate(90);
            Bitmap scaledBitmap = Bitmap.createScaledBitmap(storeMap, storeMap.getWidth() * 3, storeMap.getHeight() * 3, true);
            storeMap = Bitmap.createBitmap(scaledBitmap, 0, 0, scaledBitmap.getWidth(), scaledBitmap.getHeight(), matrix, true);
            img_tick.setVisibility(View.VISIBLE);
            scaledBitmap.recycle();
        }
    }
}
