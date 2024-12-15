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

import ma.ensa.retrofitreservation.adapters.ChambreAdapter;
import ma.ensa.retrofitreservation.models.Chambre;
import ma.ensa.retrofitreservation.network.RetrofitInstance;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ListChambreActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ChambreAdapter adapter;
    private List<Chambre> chambres = new ArrayList<>(); // Liste des chambres
    private Button buttonAddChambre;  // Déclarer le bouton

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_chambre);

        recyclerView = findViewById(R.id.list_chambres);
        buttonAddChambre = findViewById(R.id.button_add_chambre);  // Initialiser le bouton

        // Initialiser le RecyclerView avec un LayoutManager
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Récupérer la liste des chambres et la mettre à jour
        getAllChambres();

        // Configurer le clic sur le bouton pour ouvrir AddClientActivity
        buttonAddChambre.setOnClickListener(v -> {
            // Lancer AddClientActivity
            Intent intent = new Intent(ListChambreActivity.this, AddChambreActivity.class);
            startActivity(intent);
        });
    }

    private void getAllChambres() {
        // Appel à l'API pour récupérer toutes les chambres
        Call<List<Chambre>> call = RetrofitInstance.getApi().getAllChambres();

        call.enqueue(new Callback<List<Chambre>>() {
            @Override
            public void onResponse(Call<List<Chambre>> call, Response<List<Chambre>> response) {
                if (response.isSuccessful()) {
                    List<Chambre> fetchedChambres = response.body();
                    if (fetchedChambres != null && !fetchedChambres.isEmpty()) {
                        chambres.clear();  // Effacer la liste actuelle
                        chambres.addAll(fetchedChambres);  // Ajouter les chambres récupérées
                        adapter = new ChambreAdapter(chambres, ListChambreActivity.this);
                        recyclerView.setAdapter(adapter);
                    } else {
                        // Si la liste des chambres est vide
                        Toast.makeText(ListChambreActivity.this, "Aucune chambre trouvée.", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    // Si la réponse de l'API échoue
                    Toast.makeText(ListChambreActivity.this, "Erreur lors de la récupération des chambres.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Chambre>> call, Throwable t) {
                // Si la requête échoue
                Toast.makeText(ListChambreActivity.this, "Erreur : " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
