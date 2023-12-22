package br.edu.ifsul.trabalho2.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import br.edu.ifsul.trabalho2.R;
import br.edu.ifsul.trabalho2.service.RestService;
import br.edu.ifsul.trabalho2.service.RetrofitClient;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BiografiaActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;

    private TextView deputadoNome, deputadoPartido, deputadoUf;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_biografia);

        bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.deputadosMenu);
        deputadoNome = findViewById(R.id.deputadoNome);
        deputadoPartido = findViewById(R.id.deputadoPartido);
        deputadoUf = findViewById(R.id.deputadoUf);

        consultarApi();

        bottomNavigationView.setOnItemSelectedListener(item -> {
            if(item.getItemId() == R.id.partidoMenu) {
                startActivity(new Intent(getApplicationContext(), PartidoActivity.class));
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
        Call<ResponseBody> call = apiService.detalharDeputado(getIntent().getLongExtra("Deputado", 0L));

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    try {
                        String responseData = response.body().string();
                        preencherDetalhesDeputado(responseData);
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

    private void preencherDetalhesDeputado(String jsonString) {
        try {
            JSONObject json = new JSONObject(jsonString);
            JSONObject dadosDeputado = json.getJSONObject("dados");
            JSONObject ultimoStatus = dadosDeputado.getJSONObject("ultimoStatus");

            String nome = dadosDeputado.getString("nomeCivil");
            String siglaPartido = ultimoStatus.getString("siglaPartido");
            String uf = ultimoStatus.getString("siglaUf");

            deputadoNome.setText(nome);
            deputadoPartido.setText(siglaPartido);
            deputadoUf.setText(uf);

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