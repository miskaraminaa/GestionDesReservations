package ma.ensa.retrofitreservation;



import androidx.appcompat.app.AppCompatActivity;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialisation des boutons
        Button btnReservation = findViewById(R.id.btn_reservation);
        Button btnChambre = findViewById(R.id.btn_chambre);
        Button btnClient = findViewById(R.id.btn_client);

        // On clic sur le bouton Reservation
        btnReservation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Lancer l'activité ReservationActivity
                Intent intent = new Intent(MainActivity.this, ListReservationActivity.class);
                startActivity(intent);
            }
        });

        // On clic sur le bouton Chambre
        btnChambre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Lancer l'activité ListChambreActivity
                Intent intent = new Intent(MainActivity.this, ListChambreActivity.class);
                startActivity(intent);
            }
        });

        // On clic sur le bouton Client
        btnClient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Lancer l'activité ListClientActivity
                Intent intent = new Intent(MainActivity.this, ListClientActivity.class);
                startActivity(intent);
            }
        });
    }
}
