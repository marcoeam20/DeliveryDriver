package com.example.hendryshanedeguia.driverretrieval;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.github.gcacace.signaturepad.views.SignaturePad;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

//import static com.example.hendryshanedeguia.driverretrieval.R.id.imageView;

public class Signature extends AppCompatActivity {

    private TextView orderID;

    private SignaturePad mSignaturePad;
    private Button mClearButton;
    private Button mSaveButton;

    private StorageReference mStoreSignature;
    DatabaseReference mComplete;
    DatabaseReference mPendings;
    DatabaseReference mDriver;

    private Uri resultUri;

    private static final int SIGNATURE_INTENT = 2;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signature);


        mComplete = FirebaseDatabase.getInstance().getReference("Orders").child("Completed Orders");
        mPendings = FirebaseDatabase.getInstance().getReference("Orders").child("Pending Orders");
        mDriver = FirebaseDatabase.getInstance().getReference("Drivers");

        mSignaturePad = (SignaturePad) findViewById(R.id.signature_pad);
        mSignaturePad.setOnSignedListener(new SignaturePad.OnSignedListener() {
            @Override
            public void onStartSigning() {
                Toast.makeText(Signature.this, "OnStartSigning", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onSigned() {

                mSaveButton.setEnabled(true);
                mClearButton.setEnabled(true);

            }

            @Override
            public void onClear() {

                mSaveButton.setEnabled(false);
                mClearButton.setEnabled(false);

            }
        });

        mClearButton = (Button) findViewById(R.id.clear_button);
        mSaveButton = (Button) findViewById(R.id.save_button);

        mClearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mSignaturePad.clear();
            }
        });

        final Intent x = getIntent();

            Intent SigIntent = this.getIntent();
            final String pendingID = SigIntent.getStringExtra("pendingID");
            final String theID = SigIntent.getStringExtra("orderID");
            final String user = SigIntent.getStringExtra("driverID");
            //orderID.setText(theID);


        mStoreSignature = FirebaseStorage.getInstance().getReference("Orders").child("All Orders");


        mSaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Bitmap signatureBitmap = mSignaturePad.getSignatureBitmap();
                if (addJpgSignatureToGallery(signatureBitmap)) {

                    Toast.makeText(Signature.this, "Signature saved", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(Signature.this, ConfirmOrder.class);
                    intent.setType("image/*");
                    intent.putExtra("orderID",theID);
                    intent.putExtra("pendingID", pendingID);
                    intent.putExtra("driverID", user);


                    startActivity(intent);



                } else {
                    Toast.makeText(Signature.this, "Unable to store the signature", Toast.LENGTH_SHORT).show();
                }





            }
        });



    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == SIGNATURE_INTENT  && resultCode == RESULT_OK){

            Uri uri = data.getData();

            StorageReference filepath = mStoreSignature.child("Signatures").child(uri.getLastPathSegment());

            filepath.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Toast.makeText(getApplicationContext(), "Upload Done...", Toast.LENGTH_LONG).show();


                }
            });

            Intent SigIntent = this.getIntent();
            String pendingId = SigIntent.getStringExtra("pendingID");
            final String theID = SigIntent.getStringExtra("orderID");
            final String user = SigIntent.getStringExtra("driverID");

            Intent confirm = new Intent(Signature.this, ConfirmOrder.class);
            confirm.putExtra("pendingID", pendingId);
            confirm.putExtra("orderID",theID);
            confirm.putExtra("driverID", user);
            startActivity(confirm);

        }


    }

    private boolean addJpgSignatureToGallery(Bitmap signature) {
        boolean result = false;
        try {
            File photo = new File(getAlbumStorageDir("SignaturePad"), String.format("Signature_%d.jpg", System.currentTimeMillis()));
            saveBitmapToJPG(signature, photo);
            scanMediaFile(photo);
            result = true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    private void scanMediaFile(File photo) {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        Uri contentUri = Uri.fromFile(photo);
        mediaScanIntent.setData(contentUri);
        Signature.this.sendBroadcast(mediaScanIntent);
    }

    private void saveBitmapToJPG(Bitmap bitmap, File photo) throws FileNotFoundException {

        Bitmap newBitmap = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(newBitmap);
        canvas.drawColor(Color.WHITE);
        canvas.drawBitmap(bitmap, 0, 0, null);
        OutputStream stream = new FileOutputStream(photo);
        newBitmap.compress(Bitmap.CompressFormat.JPEG, 80, stream);
        try {
            stream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private File getAlbumStorageDir(String albumName) {

        // Get the directory for the user's public pictures directory.
        File file = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), albumName);
        if (!file.mkdirs()) {
            Log.e("SignaturePad", "Directory not created");
        }
        return file;
    }

//    @Override
//    public boolean onKeyDown(int keyCode, KeyEvent event) {
//
//        if (keyCode == android.view.KeyEvent.KEYCODE_BACK) {
//
//            Intent x = getIntent();
//            String user = x.getStringExtra("driverID");
//            Intent back = new Intent(getApplicationContext(), HomeActivity.class);
//            back.putExtra("driverID", user);
//            startActivity(back);
//        }
//        return super.onKeyDown(keyCode, event);
//    }


}
