package ma.ensa.reservation.services;

import ma.ensa.reservation.models.Chambre;
import ma.ensa.reservation.repositories.ChambreRepository;
import ma.ensa.reservation.repositories.ClientRepository;
import ma.ensa.reservation.repositories.ReservationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;


@Service
public class ChambreService {

    @Autowired
    private ChambreRepository chambreRepository;


    public boolean existsById(Long id) {
        return chambreRepository.existsById(id);
    }
}
