package br.edu.ifsul.trabalho2.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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

import br.edu.ifsul.trabalho2.R;
import br.edu.ifsul.trabalho2.adapter.PartidoAdapter;
import br.edu.ifsul.trabalho2.domain.Partido;
import br.edu.ifsul.trabalho2.service.RestService;
import br.edu.ifsul.trabalho2.service.RetrofitClient;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PartidoActivity extends AppCompatActivity {

    private final String URL = "https://dadosabertos.camara.leg.br/api/v2/";

    private RecyclerView partidosList;

    private PartidoAdapter partidosArrayAdapter;

    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_partido);

        bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.partidoMenu);

        partidosList = findViewById(R.id.partidosList);
        partidosList.setLayoutManager(new LinearLayoutManager(this));

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

    private void adicionarAdapter(List<Partido> partidos) {
        partidosArrayAdapter = new PartidoAdapter(partidos, new PartidoAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Partido partido) {
                Intent intent = new Intent(getApplicationContext(), PerfilActivity.class);
                intent.putExtra("Partido", partido.getId());
                Log.d("RecyclerView", "ID do Partido clicado: " + partido.getId());
                startActivity(intent);
            }
        });
        partidosList.setAdapter(partidosArrayAdapter);
    }

    private void consultarApi() {
        RestService restService = RetrofitClient.criarApiService();
        Call<ResponseBody> call = restService.listarPartidos();

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    try {
                        String responseData = response.body().string();
                        Log.d("ApiResult", "Raw API Response: " + responseData);

                        List<Partido> partidos = parseJson(responseData);

                        adicionarAdapter(partidos);
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

    private List<Partido> parseJson(String jsonString) {
        List<Partido> partidos = new ArrayList<>();

        try {
            JSONObject json = new JSONObject(jsonString);
            JSONArray dadosArray = json.getJSONArray("dados");

            for (int i = 0; i < dadosArray.length(); i++) {
                JSONObject partidoJson = dadosArray.getJSONObject(i);

                Long id = partidoJson.getLong("id");
                String nome = partidoJson.getString("nome");
                String sigla = partidoJson.getString("sigla");

                Partido partido = new Partido(id, nome, sigla);
                partidos.add(partido);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return partidos;
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