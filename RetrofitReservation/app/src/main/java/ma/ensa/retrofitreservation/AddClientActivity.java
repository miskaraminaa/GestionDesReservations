package ma.ensa.retrofitreservation;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import ma.ensa.retrofitreservation.models.Client;
import ma.ensa.retrofitreservation.network.RetrofitInstance;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddClientActivity extends AppCompatActivity {

    Button btnAddClient;
    EditText nom, prenom, email, telephone;
    private String Tag = "Client";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_add_client);

        btnAddClient = findViewById(R.id.btnAddClient);
        nom = findViewById(R.id.nom);
        prenom = findViewById(R.id.prenom);
        email = findViewById(R.id.email);
        telephone = findViewById(R.id.telephone);

        btnAddClient.setOnClickListener(v -> createClient());
    }

    private void createClient() {
        Client client = new Client();
        client.setNom(nom.getText().toString());
        client.setPrenom(prenom.getText().toString());
        client.setEmail(email.getText().toString());
        client.setTelephone(telephone.getText().toString());

        Call<Client> call = RetrofitInstance.getApi().createClient(client);

        call.enqueue(new Callback<Client>() {
            @Override
            public void onResponse(Call<Client> call, Response<Client> response) {
                if (response.isSuccessful()) {
                    // Affichage du Toast de succès
                    Toast.makeText(AddClientActivity.this, "Client ajouté avec succès", Toast.LENGTH_SHORT).show();
                    Log.d(Tag, response.toString());
                } else {
                    // Si l'ajout échoue, afficher un Toast d'erreur
                    Toast.makeText(AddClientActivity.this, "Erreur lors de l'ajout du client.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Client> call, Throwable t) {
                Toast.makeText(AddClientActivity.this, "Erreur : " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
