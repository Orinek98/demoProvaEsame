package com.dsbd.project.service;

import com.dsbd.project.entity.Trip;
import com.dsbd.project.entity.TripRepository;
import com.dsbd.project.entity.User;
import com.dsbd.project.entity.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.Sort;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
@Transactional
// La @Transactional si assicura che se il save fallisce, tutto venga annullato.
public class ProjectTripService {

    @Autowired
    TripRepository tripRepository;
    @Autowired
    UserRepository userRepository;

    public List<Trip> findTrips(Integer id, String origin, String destination, Double maxPrice, String sortBy) {

        if (id != null) {
            Optional<Trip> trip = tripRepository.findById(id);

            // Restituisci una lista con l'elemento trovato (o una lista vuota)
            return trip.map(List::of).orElseGet(List::of);
        }

        // Imposta l'ordinamento. Default su "departureTime" è più utile per i viaggi.
        String fieldToSort = sortBy != null && !sortBy.isEmpty() ? sortBy : "departureTime";
        Sort sort = Sort.by(fieldToSort);

        // Converte maxPrice in BigDecimal per il confronto nel DB, se presente
        BigDecimal priceFilter = (maxPrice != null) ? BigDecimal.valueOf(maxPrice) : null;

        // --- LOGICA DI FILTRAGGIO ---

        // 1. Caso più specifico: Filtra per TUTTI E TRE i parametri
        if (origin != null && destination != null && priceFilter != null) {

            // Filtro per Origine E Destinazione
            List<Trip> results = tripRepository.findByOriginAndDestination(origin, destination, sort);

            // Filtro manuale (in memoria) per il prezzo se richiesto
            return results.stream()
                    .filter(trip -> trip.getPrice().compareTo(priceFilter) <= 0)
                    .collect(Collectors.toList());
        }

        // 2. Filtra per Destinazione E Prezzo
        if (destination != null && priceFilter != null) {
            return tripRepository.findByDestinationAndPriceLessThanEqual(destination, priceFilter, sort);
        }

        // 3. Filtra solo per Origine E Destinazione
        if (origin != null && destination != null) {
            return tripRepository.findByOriginAndDestination(origin, destination, sort);
        }

        // 4. Filtra solo per Destinazione
        if (destination != null) {
            return tripRepository.findByDestination(destination, sort);
        }

        // 5. Filtra solo per Prezzo Massimo
        if (priceFilter != null) {
            return tripRepository.findByPriceLessThanEqual(priceFilter, sort);
        }

        // 6. Caso di default: Nessun filtro, solo ordinamento
        Iterable<Trip> tripsIterable = tripRepository.findAll(sort);
        return StreamSupport.stream(tripsIterable.spliterator(), false)
                .collect(Collectors.toList());
    }

    public void buyTrip(String userEmail, Integer tripId) throws Exception {

        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("Utente loggato non trovato."));

        Trip trip = tripRepository.findById(tripId)
                .orElseThrow(() -> new RuntimeException("Viaggio non trovato con ID: " + tripId));


        if (user.getPurchasedTrips().contains(trip)) {
            throw new Exception("Hai già acquistato questo viaggio.");
        }

        BigDecimal price = trip.getPrice();
        BigDecimal userCredit = user.getCredit() != null ? user.getCredit() : BigDecimal.ZERO;

        if (userCredit.compareTo(price) < 0) {
            throw new Exception("Credito insufficiente. Credito attuale: " + userCredit);
        }

        BigDecimal newCredit = userCredit.subtract(price);
        user.setCredit(newCredit);

        user.getPurchasedTrips().add(trip);

        userRepository.save(user);

    }

    public Trip addTrip(Trip trip) {
        return tripRepository.save(trip);
    }

    public Optional<Trip> getById(Integer id) {
        return tripRepository.findById(id);
    }
}
