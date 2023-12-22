package br.edu.ifsul.trabalho2.activity;




import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

import br.edu.ifsul.trabalho2.R;


public class LoginActivity extends AppCompatActivity {

    private EditText emailLoginEdit, senhaLoginEdit;

    private Button entrarLoginButton;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mAuth = FirebaseAuth.getInstance();

        emailLoginEdit = findViewById(R.id.emailLoginEdit);
        senhaLoginEdit = findViewById(R.id.senhaLoginEdit);
        entrarLoginButton = findViewById(R.id.entrarLoginButton);

        entrarLoginButton.setOnClickListener(view -> {
            String email, password;

            email = emailLoginEdit.getText().toString();
            password = senhaLoginEdit.getText().toString();

            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Toast.makeText(LoginActivity.this, "Sucesso!!!", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(getApplicationContext(), PartidoActivity.class);
                            startActivity(intent);
                            finish();

                        } else {
                            Toast.makeText(LoginActivity.this, "Falha", Toast.LENGTH_SHORT).show();
                        }
                    });
        });
    }

}