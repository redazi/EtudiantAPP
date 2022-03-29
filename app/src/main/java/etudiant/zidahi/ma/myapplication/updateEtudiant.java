package etudiant.zidahi.ma.myapplication;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import etudiant.zidahi.ma.myapplication.beans.DemoClass;
import etudiant.zidahi.ma.myapplication.beans.Etudiant;


public class updateEtudiant extends AppCompatActivity implements View.OnClickListener{

        private ImageView image;
        private Button remove;
        private EditText nom;
        private EditText prenom;
        private Spinner ville;
        private RadioButton m;
        private RadioButton f;
        private  Button addPic , deletePic ;
        private Button update;
        private Bitmap bitmap = null;
        private String link = "drawable-v24/maleuser.png";
        private int id = 0;
        RequestQueue requestQueue;
        String loadUrl = "http://192.168.56.1/PhpProject3/ws/loadEtudiant.php";
        String updateUrl="http://192.168.56.1/PhpProject3/ws/updateEtudiant.php";

        @Override
        protected void onCreate(Bundle savedInstanceState) {
                super.onCreate(savedInstanceState);
                setContentView(R.layout.activity_update_etudiant);
                addPic = findViewById(R.id.addPicE);
                deletePic = findViewById(R.id.removePicE);
                image = findViewById(R.id.photoE);

                nom = findViewById(R.id.nomE);
                prenom = findViewById(R.id.prenomE);
                ville = findViewById(R.id.villeE);
                update = findViewById(R.id.update);
                m = findViewById(R.id.mE);
                f = findViewById(R.id.fE);

                id = DemoClass.message;

                requestQueue = Volley.newRequestQueue(getApplicationContext());
                StringRequest request = new StringRequest(Request.Method.POST,
                        loadUrl, response -> {
                        Log.d("TAG", response);
                        Type type = new TypeToken<Collection<Etudiant>>(){}.getType();
                        Collection<Etudiant> etudiants = new Gson().fromJson(response, type);
                        for(Etudiant e : etudiants) {
                                if(e.getId() == id) {
                                        nom.setText(e.getNom());
                                        prenom.setText(e.getPrenom());
                                        if (e.getVille().equals("casablanca")) {
                                                ville.setSelection(0);
                                        }else if(e.getVille().equals("marrakech")) {
                                                ville.setSelection(1);
                                        }else if(e.getVille().equals("safi")) {
                                                ville.setSelection(2);
                                        }else if(e.getVille().equals("dakar")) {
                                                ville.setSelection(3);
                                        }

                                        if(e.getSexe().equals("homme")) {
                                                m.setChecked(true);
                                        }else {
                                                f.setChecked(true);
                                        }

                                        if(e.getPhoto() == null) {
                                                String link = "drawable-v24/maleuser.png";
                                                Glide
                                                        .with(getApplicationContext())
                                                        .load(Uri.parse(link))
                                                        .apply(RequestOptions.fitCenterTransform())
                                                        .into(image);
                                        }else {
                                                byte[] decodedString = Base64.decode(e.getPhoto(), Base64.DEFAULT);
                                                Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                                                bitmap = decodedByte;
                                                Glide
                                                        .with(getApplicationContext())
                                                        .load(decodedByte)
                                                        .apply(RequestOptions.fitCenterTransform())
                                                        .into(image);
                                        }
                                        break;
                                }
                        }
                }, error -> Toast.makeText(updateEtudiant.this, error.getMessage(), Toast.LENGTH_SHORT).show()) {
                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {
                                HashMap<String, String> params = new HashMap<String, String>();
                                params.put("id", String.valueOf(id));
                                return params;
                        }
                };
                requestQueue.add(request);

                addPic.setOnClickListener(this);
                deletePic.setOnClickListener(this);
                update.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
                Log.d("ok","ok");
                if(view == addPic) {
                        ImagePicker.with(this)
                                .crop()
                                .compress(1024)
                                .maxResultSize(1080, 1080)
                                .start();
                }
                if(view == deletePic) {
                        link = "drawable-v24/maleuser.png";
                        bitmap = null;
                        Glide
                                .with(getApplicationContext())
                                .load(Uri.parse(link))
                                .apply(RequestOptions.fitCenterTransform())
                                .into(image);
                }
                if (view == update) {
                        requestQueue = Volley.newRequestQueue(getApplicationContext());
                        StringRequest request = new StringRequest(Request.Method.POST,
                                updateUrl, response -> {
                                Log.d("TAG", response);
                                Toast.makeText(updateEtudiant.this, "Modification avec succès", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(updateEtudiant.this, ListActivity.class));
                        }, error -> Toast.makeText(updateEtudiant.this, error.getMessage(), Toast.LENGTH_SHORT).show()) {
                                @Override
                                protected Map<String, String> getParams() throws AuthFailureError {
                                        String sexe = "";
                                        if(m.isChecked())
                                                sexe = "homme";
                                        else
                                                sexe = "femme";
                                        HashMap<String, String> params = new HashMap<String, String>();
                                        params.put("id", String.valueOf(id));
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
                }
        }
        public String getStringImage(Bitmap bmp) {
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                byte[] imageBytes = baos.toByteArray();
                String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
                return encodedImage;
        }

        @Override
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
                                .into(image);
                }
        }
}
