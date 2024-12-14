package ma.ensa.reservation.services;


import ma.ensa.reservation.models.Client;
import ma.ensa.reservation.models.Reservation;
import ma.ensa.reservation.repositories.ClientRepository;
import ma.ensa.reservation.repositories.ReservationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class ReservationService {

    @Autowired
    private ReservationRepository repository;

    @Autowired
    private ClientRepository clientRepository;


    public boolean existsById(Long id) {
        return repository.existsById(id);
    }
}
