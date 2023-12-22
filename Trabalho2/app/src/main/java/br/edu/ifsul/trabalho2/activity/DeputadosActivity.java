package br.edu.ifsul.trabalho2.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import br.edu.ifsul.trabalho2.adapter.DeputadoAdapter;
import br.edu.ifsul.trabalho2.domain.Deputado;
import br.edu.ifsul.trabalho2.R;
import br.edu.ifsul.trabalho2.service.RestService;
import br.edu.ifsul.trabalho2.service.RetrofitClient;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class DeputadosActivity extends AppCompatActivity {

    private final String URL = "https://dadosabertos.camara.leg.br/api/v2/";

    private RecyclerView deputadosList;

    private DeputadoAdapter deputadosArrayAdapter;

    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deputados);
        bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.deputadosMenu);

        deputadosList = findViewById(R.id.deputadosList);
        deputadosList.setLayoutManager(new LinearLayoutManager(this));

        consultarApi();

        bottomNavigationView.setOnItemSelectedListener(item -> {
            if (item.getItemId() == R.id.partidoMenu) {
                startActivity(new Intent(getApplicationContext(), PartidoActivity.class));
                overridePendingTransition(0, 0);
            }
            if (item.getItemId() == R.id.configMenu) {
                startActivity(new Intent(getApplicationContext(), ConfigActivity.class));
                overridePendingTransition(0, 0);
            }
            return false;
        });

    }

    private void adicionarAdapter(List<Deputado> deputados) {
        deputadosArrayAdapter = new DeputadoAdapter(deputados, new DeputadoAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Deputado deputado) {
                Intent intent = new Intent(getApplicationContext(), BiografiaActivity.class);
                intent.putExtra("Deputado", deputado.getId());
                startActivity(intent);
            }
        });
        deputadosList.setAdapter(deputadosArrayAdapter);
    }

    private void consultarApi() {
        RestService restService = RetrofitClient.criarApiService();
        Call<ResponseBody> call = restService.listarDeputados();

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    try {
                        String responseData = response.body().string();
                        Log.d("ApiResult", "Raw API Response: " + responseData);

                        List<Deputado> deputados = parseJson(responseData);
                        adicionarAdapter(deputados);
                    }
                    catch (IOException e) {
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

    private List<Deputado> parseJson(String jsonString) {
        List<Deputado> deputados = new ArrayList<>();

        try {
            JSONObject json = new JSONObject(jsonString);
            JSONArray dadosArray = json.getJSONArray("dados");

            for (int i = 0; i < dadosArray.length(); i++) {
                JSONObject partidoJson = dadosArray.getJSONObject(i);

                Long id = partidoJson.getLong("id");
                String uf = partidoJson.getString("siglaUf");
                String nome = partidoJson.getString("nome");
                String partido = partidoJson.getString("siglaPartido");

                Deputado deputado = new Deputado(id, nome, partido, uf);
                deputados.add(deputado);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return deputados;
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