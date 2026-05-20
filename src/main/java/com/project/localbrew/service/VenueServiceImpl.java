package com.project.localbrew.service;

import com.project.localbrew.entity.User;
import com.project.localbrew.entity.Venue;
import com.project.localbrew.entity.VenueStatus;
import com.project.localbrew.repository.UserRepository;
import com.project.localbrew.repository.VenueRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class VenueServiceImpl implements VenueService {
    private final VenueRepository venueRepository;
    private final UserRepository userRepository;

    public VenueServiceImpl(VenueRepository venueRepository, UserRepository userRepository) {
        this.venueRepository = venueRepository;
        this.userRepository = userRepository;
    }

    @Override
    public List<Venue> findAllVenues() {
        return venueRepository.findAll();
    }

    @Override
    public Venue findVenueById(UUID id) {
        if (id == null) {
            throw new IllegalArgumentException("ID non può essere null");
        }
        Optional<Venue> optVenue = venueRepository.findById(id);

        return optVenue.orElseThrow(() -> new EntityNotFoundException("Venue non trovato con ID: " + id));
    }

    @Transactional
    @Override
    public Venue saveVenue(Venue venue) {
        if (venue == null) {
            throw new IllegalArgumentException("Venue nullo");
        }

        venue.setStatus(VenueStatus.PENDING);
        venue.setCreatedAt(LocalDateTime.now());

        return venueRepository.save(venue);
    }

    @Transactional
    @Override
    public Venue updateVenueById(Venue venue, UUID id) {
        if (id == null) {
            throw new IllegalArgumentException("ID nullo");
        }
        if (venue == null) {
            throw new IllegalArgumentException("Venue nullo");
        }

        //verifica se esiste la Venue
        Venue savedVenue = venueRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Venue non trovato con ID: " + id));

        //controlli
        if (venue.getName() != null && !venue.getName().isBlank())
            savedVenue.setName(venue.getName());
        if (venue.getAddress() != null && !venue.getAddress().isBlank())
            savedVenue.setAddress(venue.getAddress());
        if (venue.getLatitude() != null)
            savedVenue.setLatitude(venue.getLatitude());
        if (venue.getLongitude() != null)
            savedVenue.setLongitude(venue.getLongitude());
        if (venue.getType() != null)
            savedVenue.setType(venue.getType());
        if (venue.getStatus() != null)
            savedVenue.setStatus(venue.getStatus());
        if (venue.getCreatedAt() != null)
            savedVenue.setCreatedAt(venue.getCreatedAt());
        if (venue.getOwner() != null) {
            User user = findUserInsideVenue(venue);
            savedVenue.setOwner(user);
        }

        return venueRepository.save(savedVenue);
    }

    @Transactional
    @Override
    public void deleteVenueById(UUID id) {
        if (id == null) {
            throw new IllegalArgumentException("ID nullo");
        }

        //cambiare exception con un eccezione personalizata
        Venue delVenue = venueRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Venue non trovato con ID: " + id));
        venueRepository.delete(delVenue);
    }

    // metodi privati
    // validazione
    // serve per controllare se lo user esiste
    private User findUserInsideVenue(Venue venue) {
        return userRepository.findById(venue.getOwner().getId())
                .orElseThrow(() -> new EntityNotFoundException("User non trovato con id:" + venue.getOwner().getId()));
    }
}
