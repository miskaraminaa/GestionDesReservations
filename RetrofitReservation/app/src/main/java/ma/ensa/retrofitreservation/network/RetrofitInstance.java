package ma.ensa.retrofitreservation.network;

import java.util.concurrent.TimeUnit;

import ma.ensa.retrofitreservation.service.ApiInterface;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class RetrofitInstance {

    private static Retrofit retrofit;
    private static final String BASE_URL = "http://10.0.2.2:8082/";

    // Configuration du client HTTP avec des délais personnalisés
    private static final OkHttpClient client = new OkHttpClient.Builder()
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .build();

    // Méthode pour obtenir une instance de Retrofit
    public static Retrofit getRetrofitInstance() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(client) // Ajout du client OkHttp avec les délais configurés
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }

    // Méthode pour obtenir une instance de l'API Interface
    public static ApiInterface getApi() {
        return getRetrofitInstance().create(ApiInterface.class);
    }
}
