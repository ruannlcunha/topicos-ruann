package br.edu.ifsul.trabalho2.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import br.edu.ifsul.trabalho2.R;
import br.edu.ifsul.trabalho2.domain.Partido;
import br.edu.ifsul.trabalho2.service.RestService;
import br.edu.ifsul.trabalho2.service.RetrofitClient;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PerfilActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;

    private TextView partidoNome, partidoSigla;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil);

        bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.partidoMenu);
        partidoNome = findViewById(R.id.partidoNome);
        partidoSigla = findViewById(R.id.siglaTextView);
        consultarApi();

        bottomNavigationView.setOnItemSelectedListener(item -> {
            if(item.getItemId() == R.id.deputadosMenu) {
                startActivity(new Intent(getApplicationContext(), DeputadosActivity.class));
                overridePendingTransition(0,0);
            }
            if(item.getItemId() == R.id.configMenu) {
                startActivity(new Intent(getApplicationContext(), ConfigActivity.class));
                overridePendingTransition(0,0);
            }

            return false;
        });
    }


    private void consultarApi() {
        RestService apiService = RetrofitClient.criarApiService();
        Call<ResponseBody> call = apiService.detalharPartido(getIntent().getLongExtra("Partido", 0));

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    try {
                        String responseData = response.body().string();
                        preencherDetalhesPartido(responseData);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    handleApiError(response);
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                handleConnectionError(t);
            }
        });
    }

    private void preencherDetalhesPartido(String jsonString) {
        try {
            JSONObject json = new JSONObject(jsonString);
            JSONObject dados = json.getJSONObject("dados");

            String nome = dados.getString("nome");
            String sigla = dados.getString("sigla");

            partidoNome.setText(nome);
            partidoSigla.setText(sigla);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void handleApiError(Response<?> response) {
        try {
            Log.e("API", "Código de erro: " + response.code());
            String errorBody = "Corpo da resposta de erro indisponível";

            if (response.errorBody() != null) {
                errorBody = response.errorBody().string();
                Log.e("API", "Corpo da resposta de erro: " + errorBody);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void handleConnectionError(Throwable t) {
        Log.e("API", "Erro de conexão", t);
    }


}