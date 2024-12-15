package ma.ensa.retrofitreservation;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
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
    private Button buttonAddClient;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_client);

        recyclerView = findViewById(R.id.list_clients);
        buttonAddClient = findViewById(R.id.button_add_client);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        getAllClients();

        // Set up swipe-to-delete functionality
        setUpSwipeToDelete();

        buttonAddClient.setOnClickListener(v -> {
            Intent intent = new Intent(ListClientActivity.this, AddClientActivity.class);
            startActivity(intent);
        });
    }

    private void getAllClients() {
        Call<List<Client>> call = RetrofitInstance.getApi().getAllClients();
        call.enqueue(new Callback<List<Client>>() {
            @Override
            public void onResponse(Call<List<Client>> call, Response<List<Client>> response) {
                if (response.isSuccessful()) {
                    List<Client> fetchedClients = response.body();
                    if (fetchedClients != null && !fetchedClients.isEmpty()) {
                        clients.clear();
                        clients.addAll(fetchedClients);
                        adapter = new ClientAdapter(clients, ListClientActivity.this);
                        recyclerView.setAdapter(adapter);
                    } else {
                        Toast.makeText(ListClientActivity.this, "Aucun client trouvé.", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(ListClientActivity.this, "Erreur lors de la récupération des clients.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Client>> call, Throwable t) {
                Toast.makeText(ListClientActivity.this, "Erreur : " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void deleteClient(long clientId, int position) {
        Call<Void> call = RetrofitInstance.getApi().deleteClient(clientId);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    clients.remove(position);
                    adapter.notifyItemRemoved(position);
                    Toast.makeText(ListClientActivity.this, "Client supprimé", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(ListClientActivity.this, "Erreur lors de la suppression", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(ListClientActivity.this, "Erreur : " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setUpSwipeToDelete() {
        ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getAdapterPosition();
                Client client = clients.get(position);

                new android.app.AlertDialog.Builder(ListClientActivity.this)
                        .setMessage("Voulez-vous vraiment supprimer ce client ?")
                        .setCancelable(false)
                        .setPositiveButton("Oui", (dialog, id) -> {
                            deleteClient(client.getId(), position);
                        })
                        .setNegativeButton("Non", (dialog, id) -> {
                            adapter.notifyItemChanged(position);  // Restore the item
                        })
                        .create()
                        .show();
            }
        };

        new ItemTouchHelper(simpleCallback).attachToRecyclerView(recyclerView);
    }
}
