package br.edu.ifsul.trabalho2.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import br.edu.ifsul.trabalho2.R;

public class CadastroActivity extends AppCompatActivity {

    private EditText emailCadastroEdit, senhaCadastroEdit;

    private Button cadastroButton;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro);
        mAuth = FirebaseAuth.getInstance();

        emailCadastroEdit = findViewById(R.id.emailCadastroEdit);
        senhaCadastroEdit = findViewById(R.id.senhaCadastroEdit);
        cadastroButton = findViewById(R.id.cadastroButton);

        cadastroButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email, password;

                email = emailCadastroEdit.getText().toString();
                password = senhaCadastroEdit.getText().toString();

                //validar se os campos foram preenchidos

                mAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(CadastroActivity.this, "Sucesso!!!", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                                    startActivity(intent);
                                    finish();

                                } else {
                                    Toast.makeText(CadastroActivity.this, "Falha", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });
    }



}