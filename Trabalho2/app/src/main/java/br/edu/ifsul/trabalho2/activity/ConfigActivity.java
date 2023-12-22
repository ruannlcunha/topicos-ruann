package br.edu.ifsul.trabalho2.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;

import br.edu.ifsul.trabalho2.R;

public class ConfigActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;

    private Button sairButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_config);
        bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.configMenu);
        sairButton = findViewById(R.id.sairButton);

        sairButton.setOnClickListener(view -> logout());

        bottomNavigationView.setOnItemSelectedListener(item -> {
            if(item.getItemId() == R.id.partidoMenu) {
                startActivity(new Intent(getApplicationContext(), PartidoActivity.class));
                overridePendingTransition(0,0);
            }
            if(item.getItemId() == R.id.deputadosMenu) {
                startActivity(new Intent(getApplicationContext(), DeputadosActivity.class));
                overridePendingTransition(0,0);
            }

            return false;
        });
    }

    public void logout() {
        FirebaseAuth.getInstance().signOut();
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
        finish();
    }

}