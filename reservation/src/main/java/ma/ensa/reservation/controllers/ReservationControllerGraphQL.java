package ma.ensa.reservation.controllers;

import ma.ensa.reservation.models.Reservation;
import ma.ensa.reservation.models.Client;
import ma.ensa.reservation.models.Chambre;
import ma.ensa.reservation.models.ReservationInput;
import ma.ensa.reservation.repositories.ReservationRepository;
import ma.ensa.reservation.repositories.ClientRepository;
import ma.ensa.reservation.repositories.ChambreRepository;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Controller
public class ReservationControllerGraphQL {

    private final ReservationRepository reservationRepository;
    private final ClientRepository clientRepository;
    private final ChambreRepository chambreRepository;

    // Explicit constructor for dependency injection
    public ReservationControllerGraphQL(ReservationRepository reservationRepository,
                                        ClientRepository clientRepository,
                                        ChambreRepository chambreRepository) {
        this.reservationRepository = reservationRepository;
        this.clientRepository = clientRepository;
        this.chambreRepository = chambreRepository;
    }

    // Query to get all reservations
    @QueryMapping
    public List<Reservation> allReservations() {
        return reservationRepository.findAll();
    }

    // Query to get a reservation by ID
    @QueryMapping
    public Reservation reservationById(@Argument Long id) {
        return reservationRepository.findById(id).orElseThrow(() ->
                new RuntimeException("Reservation not found with ID: " + id));
    }

    // Mutation to save a new or existing reservation
    @MutationMapping
    public Reservation saveReservation(@Argument ReservationInput reservationInput) {
        try {
            // Retrieve the client and room by their IDs
            Client client = clientRepository.findById(reservationInput.getClientId())
                    .orElseThrow(() -> new RuntimeException("Client not found with ID: " + reservationInput.getClientId()));
            Chambre chambre = chambreRepository.findById(reservationInput.getChambreId())
                    .orElseThrow(() -> new RuntimeException("Room not found with ID: " + reservationInput.getChambreId()));

            // Convert String to Date
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date dateDebut = dateFormat.parse(reservationInput.getDateDebut());
            Date dateFin = dateFormat.parse(reservationInput.getDateFin());

            // Create a new Reservation object and save it
            Reservation reservation = new Reservation();
            reservation.setClient(client);
            reservation.setChambre(chambre);
            reservation.setDateDebut(dateDebut);
            reservation.setDateFin(dateFin);
            reservation.setPreferences(reservationInput.getPreferences());

            return reservationRepository.save(reservation);
        } catch (ParseException e) {
            // Handle parsing error and rethrow as runtime exception
            throw new RuntimeException("Error parsing dates: " + e.getMessage(), e);
        }
    }

    // Mutation to update an existing reservation
    @MutationMapping
    public Reservation updateReservation(@Argument Long id, @Argument ReservationInput updatedReservationInput) {
        return reservationRepository.findById(id).map(reservation -> {
            // Retrieve the client and room by their IDs
            Client client = clientRepository.findById(updatedReservationInput.getClientId())
                    .orElseThrow(() -> new RuntimeException("Client not found with ID: " + updatedReservationInput.getClientId()));
            Chambre chambre = chambreRepository.findById(updatedReservationInput.getChambreId())
                    .orElseThrow(() -> new RuntimeException("Room not found with ID: " + updatedReservationInput.getChambreId()));

            // Convert String to Date
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date dateDebut = null;
            try {
                dateDebut = dateFormat.parse(updatedReservationInput.getDateDebut());
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }
            Date dateFin = null;
            try {
                dateFin = dateFormat.parse(updatedReservationInput.getDateFin());
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }

            // Update reservation fields
            reservation.setClient(client);
            reservation.setChambre(chambre);
            reservation.setDateDebut(dateDebut);
            reservation.setDateFin(dateFin);
            reservation.setPreferences(updatedReservationInput.getPreferences());

            return reservationRepository.save(reservation);
        }).orElseThrow(() -> new RuntimeException("Reservation not found with ID: " + id));
    }

    // Mutation to delete a reservation by ID
    @MutationMapping
    public String deleteReservation(@Argument Long id) {
        if (reservationRepository.existsById(id)) {
            reservationRepository.deleteById(id);
            return "Reservation with ID " + id + " has been deleted successfully.";
        } else {
            throw new RuntimeException("Reservation not found with ID: " + id);
        }
    }
}
