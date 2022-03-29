package etudiant.zidahi.ma.myapplication.Adapter;


import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView ;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import etudiant.zidahi.ma.myapplication.R;
import etudiant.zidahi.ma.myapplication.beans.Etudiant;


public class EtudiantAdapter extends RecyclerView.Adapter<EtudiantAdapter.EtudiantViewHolder> {

    private List<Etudiant> etudiants;
    private LayoutInflater inflater;
    private Context context;
    RequestQueue requestQueue;
    String deleteUrl = "http://192.168.56.1/PhpProject3/ws/deleteEtudiant.php";

    public EtudiantAdapter(Context context, List<Etudiant> etudiants) {
        this.etudiants = etudiants;
        this.context = context;
        this.inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public EtudiantViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = inflater.inflate(R.layout.item, parent, false);
        return new EtudiantViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull EtudiantViewHolder holder, int i) {

        holder.idf.setText(etudiants.get(i).getId()+"");
        holder.nom.setText(etudiants.get(i).getNom());
        holder.prenom.setText(etudiants.get(i).getPrenom());
        holder.ville.setText(etudiants.get(i).getVille());
        holder.sexe.setText(etudiants.get(i).getSexe());
        if(etudiants.get(i).getPhoto() == null) {
            String link = "drawable-v24/maleuser.png";
            Glide
                    .with(context)
                    .load(Uri.parse(link))
                    .apply(RequestOptions.fitCenterTransform())
                    .into(holder.image);
        }else {
            byte[] decodedString = Base64.decode(etudiants.get(i).getPhoto(), Base64.DEFAULT);
            Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
            Glide
                    .with(context)
                    .load(decodedByte)
                    .apply(RequestOptions.fitCenterTransform())
                    .into(holder.image);

        }



    }


    @Override
    public int getItemCount() {
        return etudiants.size();
    }

    public class EtudiantViewHolder extends RecyclerView.ViewHolder{
        ImageView image;
        TextView nom, prenom, ville, sexe, idf;
        public EtudiantViewHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.image);
            nom = itemView.findViewById(R.id.nom);
            prenom = itemView.findViewById(R.id.prenom);
            ville = itemView.findViewById(R.id.ville);
            sexe = itemView.findViewById(R.id.sexe);
            idf = itemView.findViewById(R.id.idf);
        }
    }
}
