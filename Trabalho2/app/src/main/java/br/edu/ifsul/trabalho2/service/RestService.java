package br.edu.ifsul.trabalho2.service;

import java.util.List;

import br.edu.ifsul.trabalho2.domain.Deputado;
import br.edu.ifsul.trabalho2.domain.Partido;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface RestService {

    @GET("deputados")
    Call<ResponseBody> listarDeputados();

    @GET("deputados/{id}")
    Call<ResponseBody> detalharDeputado(@Path("id") Long id);

    @GET("partidos")
    Call<ResponseBody> listarPartidos();

    @GET("partidos/{id}")
    Call<ResponseBody> detalharPartido(@Path("id") Long id);

}
