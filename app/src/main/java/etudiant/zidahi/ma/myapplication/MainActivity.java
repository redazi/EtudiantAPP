package etudiant.zidahi.ma.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();
        ImageView logo = findViewById(R.id.imageView5);
        logo.animate().rotation(360f).setDuration(2000);
        logo.animate().scaleX(0.5f).scaleY(0.5f).setDuration(3000);
        logo.animate().translationYBy(1000f).setDuration(2000);
        logo.animate().alpha(0f).setDuration(6000);
        t1.start();
    }

    Thread t1 = new Thread(){
        @Override
        public void run() {
            try {
                sleep(3500);
                Intent intent = new Intent(MainActivity.this, ListActivity.class);
                startActivity(intent);
                MainActivity.this.finish();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    };
}
