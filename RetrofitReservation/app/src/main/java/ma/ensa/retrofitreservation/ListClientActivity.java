package ma.ensa.retrofitreservation;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
import java.util.ArrayList;

import ma.ensa.retrofitreservation.adapters.ClientAdapter;
import ma.ensa.retrofitreservation.models.Client;
import ma.ensa.retrofitreservation.network.RetrofitInstance;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ListClientActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ClientAdapter adapter;
    private List<Client> clients = new ArrayList<>();
    private Button buttonAddClient;  // Déclarer le bouton

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_client);

        recyclerView = findViewById(R.id.list_clients);
        buttonAddClient = findViewById(R.id.button_add_client);  // Initialiser le bouton

        // Initialiser le RecyclerView avec un LayoutManager
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Récupérer la liste des clients et la mettre à jour
        getAllClients();

        // Configurer le clic sur le bouton pour ouvrir AddClientActivity
        buttonAddClient.setOnClickListener(v -> {
            // Lancer AddClientActivity
            Intent intent = new Intent(ListClientActivity.this, AddClientActivity.class);
            startActivity(intent);
        });
    }

    private void getAllClients() {
        // Appel à l'API pour récupérer tous les clients
        Call<List<Client>> call = RetrofitInstance.getApi().getAllClients();

        call.enqueue(new Callback<List<Client>>() {
            @Override
            public void onResponse(Call<List<Client>> call, Response<List<Client>> response) {
                if (response.isSuccessful()) {
                    List<Client> fetchedClients = response.body();
                    if (fetchedClients != null && !fetchedClients.isEmpty()) {
                        clients.clear();  // Effacer la liste actuelle
                        clients.addAll(fetchedClients);  // Ajouter les clients récupérés
                        adapter = new ClientAdapter(clients, ListClientActivity.this);
                        recyclerView.setAdapter(adapter);
                    } else {
                        // Si la liste des clients est vide
                        Toast.makeText(ListClientActivity.this, "Aucun client trouvé.", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    // Si la réponse de l'API échoue
                    Toast.makeText(ListClientActivity.this, "Erreur lors de la récupération des clients.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Client>> call, Throwable t) {
                // Si la requête échoue
                Toast.makeText(ListClientActivity.this, "Erreur : " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
