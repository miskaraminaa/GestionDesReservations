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

    // Constructor for dependency injection
    public ReservationControllerGraphQL(ReservationRepository reservationRepository,
                                        ClientRepository clientRepository,
                                        ChambreRepository chambreRepository) {
        this.reservationRepository = reservationRepository;
        this.clientRepository = clientRepository;
        this.chambreRepository = chambreRepository;
    }

    // Query to fetch all reservations
    @QueryMapping
    public List<Reservation> allReservations() {
        return reservationRepository.findAll();
    }

    // Query to fetch reservation by ID
    @QueryMapping
    public Reservation reservationById(@Argument Long id) {
        return reservationRepository.findById(id).orElseThrow(() ->
                new RuntimeException("Reservation not found with ID: " + id));
    }

    // Mutation to save a new reservation
    @MutationMapping
    public Reservation saveReservation(@Argument ReservationInput reservationInput) {
        try {
            // Fetch client and room from database using provided IDs
            Client client = clientRepository.findById(reservationInput.getClientId())
                    .orElseThrow(() -> new RuntimeException("Client not found with ID: " + reservationInput.getClientId()));
            Chambre chambre = chambreRepository.findById(reservationInput.getChambreId())
                    .orElseThrow(() -> new RuntimeException("Room not found with ID: " + reservationInput.getChambreId()));

            // Parse dates from string to Date object
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date dateDebut = dateFormat.parse(reservationInput.getDateDebut());
            Date dateFin = dateFormat.parse(reservationInput.getDateFin());

            // Create new reservation and save it
            Reservation reservation = new Reservation();
            reservation.setClient(client);
            reservation.setChambre(chambre);
            reservation.setDateDebut(dateDebut);
            reservation.setDateFin(dateFin);
            reservation.setPreferences(reservationInput.getPreferences());

            return reservationRepository.save(reservation);
        } catch (ParseException e) {
            throw new RuntimeException("Error parsing dates: " + e.getMessage(), e);
        }
    }

    // Mutation to update an existing reservation
    @MutationMapping
    public Reservation updateReservation(@Argument Long id, @Argument ReservationInput updatedReservationInput) {
        return reservationRepository.findById(id).map(reservation -> {
            try {
                // Fetch client and room from database
                Client client = clientRepository.findById(updatedReservationInput.getClientId())
                        .orElseThrow(() -> new RuntimeException("Client not found with ID: " + updatedReservationInput.getClientId()));
                Chambre chambre = chambreRepository.findById(updatedReservationInput.getChambreId())
                        .orElseThrow(() -> new RuntimeException("Room not found with ID: " + updatedReservationInput.getChambreId()));

                // Parse dates from string to Date object
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                Date dateDebut = dateFormat.parse(updatedReservationInput.getDateDebut());
                Date dateFin = dateFormat.parse(updatedReservationInput.getDateFin());

                // Update reservation details
                reservation.setClient(client);
                reservation.setChambre(chambre);
                reservation.setDateDebut(dateDebut);
                reservation.setDateFin(dateFin);
                reservation.setPreferences(updatedReservationInput.getPreferences());

                return reservationRepository.save(reservation);
            } catch (ParseException e) {
                throw new RuntimeException("Error parsing dates: " + e.getMessage(), e);
            }
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
