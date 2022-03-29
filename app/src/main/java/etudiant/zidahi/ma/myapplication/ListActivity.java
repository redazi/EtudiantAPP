package etudiant.zidahi.ma.myapplication;

import android.content.Intent;
import android.graphics.Canvas;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import etudiant.zidahi.ma.myapplication.Adapter.EtudiantAdapter;
import etudiant.zidahi.ma.myapplication.beans.DemoClass;
import etudiant.zidahi.ma.myapplication.beans.Etudiant;
import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator;


public class ListActivity extends AppCompatActivity {
    private RecyclerView recycle;
    private FloatingActionButton addStudent;
    private  List<Etudiant> etudiants1 = new ArrayList<Etudiant>();
    private static final String TAG ="ListEtudiant" ;
    RequestQueue requestQueue;
    String listUrl = "http://192.168.56.1/PhpProject3/ws/loadEtudiant.php";
    String DeleteUrl = "http://192.168.56.1/PhpProject3/ws/deleteEtudiant.php";
    String loadUrl = "http://192.168.56.1/PhpProject3/ws/loadEtudiant.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        System.out.println("staaart activity");
        recycle = findViewById(R.id.listStudent);
        addStudent = findViewById(R.id.addStdent);

        addStudent.setOnClickListener(v -> startActivity(new Intent(ListActivity.this, addEtudiant.class)));

        loadData();
    }

    public void loadData() {
        requestQueue = Volley.newRequestQueue(getApplicationContext());
        StringRequest request = new StringRequest(Request.Method.POST,
                loadUrl, response -> {
            Log.d("TAG", response);
            Type type = new TypeToken<Collection<Etudiant>>(){}.getType();
            Collection<Etudiant> etudiants = new Gson().fromJson(response, type);
            etudiants1 = (List<Etudiant>) etudiants;
            System.out.println("11111111111111 "+etudiants.size());
            recycle.setAdapter(new EtudiantAdapter(ListActivity.this, (List<Etudiant>) etudiants));
            recycle.setLayoutManager(new LinearLayoutManager(ListActivity.this));
            ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
            itemTouchHelper.attachToRecyclerView(recycle);
        }, error -> {
        });
        requestQueue.add(request);
    }
    ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {

        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(@NonNull final RecyclerView.ViewHolder viewHolder, int direction) {

            final int position = viewHolder.getAdapterPosition();
            System.out.println("hanaaa taanii1111");
            System.out.println(position);
            System.out.println(etudiants1.size());
            switch (direction) {
                case ItemTouchHelper.LEFT:

                    requestQueue = Volley.newRequestQueue(getApplicationContext());
                    StringRequest request1 = new StringRequest(Request.Method.POST,
                            DeleteUrl, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.d(TAG, response);
                            System.out.println("hanaaa taani7777777");
                            System.out.println(position);
                            etudiants1.remove(position);
                            recycle.getAdapter().notifyItemRemoved(position);
                            //etudiantAdapter.notifyItemRemoved();
                            Toast.makeText(getApplicationContext(), "Suppression avec succès", Toast.LENGTH_SHORT).show();
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }) {
                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {
                            System.out.println("hanaaa taanii");
                            System.out.println(etudiants1.get(position).toString());
                            HashMap<String, String> params = new HashMap<>();
                            params.put("id", String.valueOf(etudiants1.get(position).getId()));
                            return params;
                        }
                    };
                    requestQueue.add(request1);
                    break;
                case ItemTouchHelper.RIGHT:


                    Intent intent = new Intent(ListActivity.this, updateEtudiant.class);
                    DemoClass.message = etudiants1.get(position).getId();
                    System.out.println("test list 11111111111111111111111111111111111111111111111111111");
                    System.out.println(DemoClass.message);
                    // intent.putExtra("id",etudiants1.get(position).getId() );
                    startActivity(intent);
                    ListActivity.this.finish();

                    break;
            }


        }


        @Override
        public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {

            new RecyclerViewSwipeDecorator.Builder(ListActivity.this, c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
                    .addSwipeLeftBackgroundColor(ContextCompat.getColor(ListActivity.this, R.color.colorAccent))
                    .addSwipeLeftActionIcon(R.drawable.ic_delete_black_24dp)
                    .addSwipeRightBackgroundColor(ContextCompat.getColor(ListActivity.this, R.color.colorPrimaryDark))

                    .setActionIconTint(ContextCompat.getColor(recyclerView.getContext(), android.R.color.white))
                    .create()
                    .decorate();

            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
        }
    };
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu,menu);
        // MenuItem menuItem = menu.findItem(R.id.app_bar_search);
 /*     SearchView searchView = (SearchView) MenuItemCompat.getActionView(menuItem);
      searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
          @Override
          public boolean onQueryTextSubmit(String query) {
              return true;
          }
          @Override
          public boolean onQueryTextChange(String newText) {
              if (starAdapter != null){
                  starAdapter.getFilter().filter(newText);
              }
              return true;
          }
      });*/
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.share){
        /*    String txt = "Stars";
            String mimeType = "text/plain";
            ShareCompat.IntentBuilder
                    .from(this)
                    .setType(mimeType)
                    .setChooserTitle("Stars")
                    .setText(txt)
                    .startChooser();*/ Intent intent = new Intent(ListActivity.this, addEtudiant.class);
            startActivity(intent);
            ListActivity.this.finish();


        }
        return super.onOptionsItemSelected(item);
    }

}
