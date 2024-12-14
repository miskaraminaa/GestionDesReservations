package ma.ensa.reservation.repositories;


import ma.ensa.reservation.models.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {

}
