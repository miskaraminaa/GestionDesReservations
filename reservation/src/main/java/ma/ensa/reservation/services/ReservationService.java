package ma.ensa.reservation.services;

import ma.ensa.reservation.models.Client;
import ma.ensa.reservation.models.Reservation;
import ma.ensa.reservation.repositories.ClientRepository;
import ma.ensa.reservation.repositories.ReservationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ReservationService {

    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private ClientRepository clientRepository; // Ajout du ClientRepository

    // Récupérer toutes les réservations
    public List<Reservation> getAllReservations() {
        return reservationRepository.findAll();
    }

    // Créer une réservation
    public Reservation createReservation(Reservation reservation) {
        return reservationRepository.save(reservation);
    }

    // Récupérer une réservation par ID
    public Optional<Reservation> getReservationById(Long id) {
        return reservationRepository.findById(id);
    }

    // Mettre à jour une réservation
    public Reservation updateReservation(Long id, Reservation updatedReservation) {
        return reservationRepository.findById(id).map(reservation -> {
            reservation.setClient(updatedReservation.getClient());
            reservation.setChambre(updatedReservation.getChambre());
            reservation.setDateDebut(updatedReservation.getDateDebut());
            reservation.setDateFin(updatedReservation.getDateFin());
            reservation.setPreferences(updatedReservation.getPreferences());
            return reservationRepository.save(reservation);
        }).orElseThrow(() -> new RuntimeException("Reservation not found"));
    }

    // Supprimer une réservation
    public void deleteReservation(Long id) {
        reservationRepository.deleteById(id);
    }

    // Ajouter un client
    public Client addClient(Client client) {
        return clientRepository.save(client); // Enregistrer le client dans la base de données
    }

    // Récupérer tous les clients (facultatif)
    public List<Client> getAllClients() {
        return clientRepository.findAll();
    }
}
