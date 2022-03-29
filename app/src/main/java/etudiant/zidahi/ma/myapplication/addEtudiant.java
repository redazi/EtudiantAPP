package etudiant.zidahi.ma.myapplication;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;

import androidx.annotation.Nullable;

import androidx.appcompat.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.request.RequestOptions;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.bumptech.glide.Glide;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


public class addEtudiant extends AppCompatActivity implements View.OnClickListener {

    private EditText nom;
    private EditText prenom;
    private Spinner ville;
    private RadioButton m;
    private RadioButton f;
    private Button add ,show;
    private ImageView photo;
    private  Button addPic , deletePic ;
    private Bitmap bitmap = null;
    RequestQueue requestQueue;
    String insertUrl = "http://192.168.56.1/PhpProject3/ws/createEtudiant.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_etudiant);
        nom = (EditText) findViewById(R.id.nom);
        prenom = (EditText) findViewById(R.id.prenom);
        ville = (Spinner) findViewById(R.id.ville);
        add = (Button) findViewById(R.id.add);

        m = (RadioButton) findViewById(R.id.m);
        f = (RadioButton) findViewById(R.id.f);
        photo = findViewById(R.id.photo);
        addPic = findViewById(R.id.addPic);
        deletePic = findViewById(R.id.removePic);
        add.setOnClickListener(this);

        addPic.setOnClickListener(this);
        deletePic.setOnClickListener(this);
    }
    @Override
    public void onClick(View v) {
        Log.d("ok","ok");
        if(v == addPic) {
            ImagePicker.with(this)
                    .crop()
                    .compress(1024)
                    .maxResultSize(1080, 1080)
                    .start();
        }
        if(v == deletePic) {

            bitmap = null;
            Glide
                    .with(getApplicationContext())
                    .load(R.drawable.images)
                    .apply(RequestOptions.fitCenterTransform())
                    .into(photo);
        }
        if (v == add) {
            requestQueue = Volley.newRequestQueue(getApplicationContext());
            StringRequest request = new StringRequest(Request.Method.POST,
                    insertUrl, response -> {
                Log.d("TAG", response);
                Toast.makeText(addEtudiant.this, "Ajout avec succès", Toast.LENGTH_SHORT).show();
            }, error -> Toast.makeText(addEtudiant.this, error.getMessage(), Toast.LENGTH_SHORT).show()) {
                @Override
                protected Map<String, String> getParams()  {
                    String sexe;
                    if(m.isChecked())
                        sexe = "homme";
                    else
                        sexe = "femme";
                    HashMap<String, String> params = new HashMap<String, String>();
                    params.put("nom", nom.getText().toString());
                    params.put("prenom", prenom.getText().toString());
                    params.put("ville", ville.getSelectedItem().toString());
                    params.put("sexe", sexe);
                    String stringImg = null;
                    if(bitmap != null) {
                        stringImg = getStringImage(bitmap);
                        params.put("photo", stringImg);
                    }else {
                        params.put("photo", "no");
                    }
                    return params;
                }
            };
            requestQueue.add(request);
            Intent intent = new Intent(addEtudiant.this, ListActivity.class);
            startActivity(intent);
            addEtudiant.this.finish();
        }

    }
    public String getStringImage(Bitmap bmp) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == Activity.RESULT_OK) {
            Uri uri = data.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);
            } catch (IOException e) {
                e.printStackTrace();
            }
            Glide
                    .with(getApplicationContext())
                    .load(uri)
                    .apply(RequestOptions.fitCenterTransform())
                    .into(photo);
        }
    }
}

