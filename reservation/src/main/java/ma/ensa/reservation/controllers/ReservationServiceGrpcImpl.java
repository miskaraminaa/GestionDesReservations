package ma.ensa.reservation.controllers;

import io.grpc.stub.StreamObserver;
import ma.ensa.reservation.models.*;
import ma.ensa.reservation.repositories.ChambreRepository;
import ma.ensa.reservation.repositories.ClientRepository;
import ma.ensa.reservation.services.ReservationService;
import ma.ensaj.hotel.stubs.ReservationServiceGrpc;
import ma.ensaj.hotel.stubs.ReservationProto;
import net.devh.boot.grpc.server.service.GrpcService;
import org.springframework.beans.factory.annotation.Autowired;

import java.sql.Date;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
@GrpcService
public class ReservationServiceGrpcImpl extends ReservationServiceGrpc.ReservationServiceImplBase {

    @Autowired
    private ReservationService reservationService;

    @Autowired
    private ChambreRepository chambreRepository;

    @Autowired
    private ClientRepository clientRepository;

    // Method to create a reservation
    @Override
    public void createReservation(ReservationProto.CreateReservationRequest request,
                                  StreamObserver<ReservationProto.CreateReservationResponse> responseObserver) {
        // Extract reservation details from the request
        var grpcReservation = request.getReservation();

        // Convert gRPC Reservation to entity
        Reservation reservation = new Reservation();

        // Convert Client (from gRPC) to Entity
        Client client = new Client(
                grpcReservation.getClient().getNom(),
                grpcReservation.getClient().getPrenom(),
                grpcReservation.getClient().getEmail(),
                grpcReservation.getClient().getTelephone()
        );
        reservation.setClient(client);

        // Convert Chambre (from gRPC) to Entity
        Chambre chambre = new Chambre();
        chambre.setTypeChambre(TypeChambre.valueOf(grpcReservation.getChambre().getTypeChambre().name()));
        chambre.setPrix(grpcReservation.getChambre().getPrix());
        chambre.setDispoChambre(DispoChambre.valueOf(grpcReservation.getChambre().getDispoChambre().name()));
        reservation.setChambre(chambre);

        // Parse the dates from String to LocalDate
        DateTimeFormatter formatter = DateTimeFormatter.ISO_DATE;
        reservation.setDateDebut(Date.valueOf(grpcReservation.getDateDebut()));  // Converts LocalDate to Date
        reservation.setDateFin(Date.valueOf(grpcReservation.getDateFin()));
        reservation.setPreferences(grpcReservation.getPreferences());

        // Save the reservation to the database
        reservationService.createReservation(reservation);

        // Send response back
        var response = ReservationProto.CreateReservationResponse.newBuilder()
                .setMessage("Reservation successfully created for client: " + grpcReservation.getClient().getNom())
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    // Method to add a client
    @Override
    public void addClient(ReservationProto.AddClientRequest request,
                          StreamObserver<ReservationProto.AddClientResponse> responseObserver) {
        var grpcClient = request.getClient();

        // Map gRPC client to entity
        Client client = new Client();
        client.setNom(grpcClient.getNom());
        client.setPrenom(grpcClient.getPrenom());
        client.setEmail(grpcClient.getEmail());
        client.setTelephone(grpcClient.getTelephone());

        // Save client to database
        clientRepository.save(client);

        // Send response
        var response = ReservationProto.AddClientResponse.newBuilder()
                .setMessage("Client added successfully: " + client.getNom())
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    // Method to add a chambre
    @Override
    public void addChambre(ReservationProto.AddChambreRequest request,
                           StreamObserver<ReservationProto.AddChambreResponse> responseObserver) {
        var grpcChambre = request.getChambre();

        // Map gRPC chambre to entity
        Chambre chambre = new Chambre();
        chambre.setTypeChambre(TypeChambre.valueOf(grpcChambre.getTypeChambre().name())); // Ensure correct enum mapping
        chambre.setPrix(grpcChambre.getPrix());
        chambre.setDispoChambre(DispoChambre.valueOf(grpcChambre.getDispoChambre().name())); // Ensure correct enum mapping

        // Save chambre to database
        chambreRepository.save(chambre);

        // Send response
        var response = ReservationProto.AddChambreResponse.newBuilder()
                .setMessage("Chambre added successfully: " + chambre.getTypeChambre())
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }
}
