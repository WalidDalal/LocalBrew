package com.project.localbrew.service;

import com.project.localbrew.entity.User;
import com.project.localbrew.entity.Venue;
import com.project.localbrew.entity.VenueStatus;
import com.project.localbrew.repository.VenueRepository;
import com.project.localbrew.security.CurrentUserService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class VenueServiceImpl implements VenueService {

    private final VenueRepository venueRepository;
    private final CurrentUserService currentUserService;

    public VenueServiceImpl(
            VenueRepository venueRepository,
            CurrentUserService currentUserService
    ) {
        this.venueRepository = venueRepository;
        this.currentUserService = currentUserService;
    }

    // =========================
    // GET ALL
    // =========================

    @Override
    public List<Venue> findAllVenues() {

        return venueRepository.findAll();
    }

    // =========================
    // GET ACTIVE
    // =========================

    public List<Venue> findActiveVenues() {

        return venueRepository.findByStatus(
                VenueStatus.ACTIVE
        );
    }

    // =========================
    // GET BY ID
    // =========================

    @Override
    public Venue findVenueById(UUID id) {

        validateId(id);

        return venueRepository.findById(id)
                .orElseThrow(() ->
                        new EntityNotFoundException(
                                "Venue non trovato con ID: " + id
                        )
                );
    }

    // =========================
    // CREATE
    // =========================

    @Override
    public Venue saveVenue(Venue venue) {

        validateVenueForCreate(venue);

        // NON deve avere ID
        if (venue.getId() != null) {

            throw new IllegalArgumentException(
                    "Un nuovo venue non deve avere ID"
            );
        }

        // PRENDE OWNER DAL JWT
        User currentUser =
                currentUserService.getCurrentUser();

        // Solo OWNER può creare venue
        if (!currentUser.getRole().name()
                .equals("OWNER")) {

            throw new IllegalArgumentException(
                    "Solo gli OWNER possono creare venue"
            );
        }

        venue.setOwner(currentUser);

        // status automatico
        venue.setStatus(VenueStatus.PENDING);

        return venueRepository.save(venue);
    }

    // =========================
    // UPDATE
    // =========================

    @Override
    public Venue updateVenueById(
            Venue venue,
            UUID id
    ) {

        validateId(id);

        if (venue == null) {

            throw new IllegalArgumentException(
                    "Venue nullo"
            );
        }

        Venue existingVenue =
                findVenueById(id);

        User currentUser =
                currentUserService.getCurrentUser();

        // SOLO owner del locale o ADMIN
        boolean isAdmin =
                currentUser.getRole().name()
                        .equals("ADMIN");

        boolean isOwnerOfVenue =
                existingVenue.getOwner()
                        .getId()
                        .equals(currentUser.getId());

        if (!isAdmin && !isOwnerOfVenue) {

            throw new IllegalArgumentException(
                    "Non puoi modificare questo locale"
            );
        }

        // UPDATE FIELDS

        if (venue.getName() != null
                && !venue.getName().isBlank()) {

            existingVenue.setName(
                    venue.getName()
            );
        }

        if (venue.getDescription() != null) {

            existingVenue.setDescription(
                    venue.getDescription()
            );
        }

        if (venue.getAddress() != null
                && !venue.getAddress().isBlank()) {

            existingVenue.setAddress(
                    venue.getAddress()
            );
        }

        if (venue.getLatitude() != null) {

            validateLatitude(
                    venue.getLatitude()
            );

            existingVenue.setLatitude(
                    venue.getLatitude()
            );
        }

        if (venue.getLongitude() != null) {

            validateLongitude(
                    venue.getLongitude()
            );

            existingVenue.setLongitude(
                    venue.getLongitude()
            );
        }

        if (venue.getType() != null) {

            existingVenue.setType(
                    venue.getType()
            );
        }

        return venueRepository.save(
                existingVenue
        );
    }

    // =========================
    // STATUS
    // =========================

    public Venue updateVenueStatus(
            UUID id,
            VenueStatus status
    ) {

        validateId(id);

        if (status == null) {

            throw new IllegalArgumentException(
                    "Status venue obbligatorio"
            );
        }

        User currentUser =
                currentUserService.getCurrentUser();

        // SOLO ADMIN
        if (!currentUser.getRole().name()
                .equals("ADMIN")) {

            throw new IllegalArgumentException(
                    "Solo ADMIN può modificare lo status"
            );
        }

        Venue venue =
                findVenueById(id);

        venue.setStatus(status);

        return venueRepository.save(venue);
    }

    public Venue activateVenue(UUID id) {

        return updateVenueStatus(
                id,
                VenueStatus.ACTIVE
        );
    }

    public Venue suspendVenue(UUID id) {

        return updateVenueStatus(
                id,
                VenueStatus.SUSPENDED
        );
    }

    // =========================
    // DELETE
    // =========================

    @Override
    public void deleteVenueById(UUID id) {

        validateId(id);

        Venue venue =
                findVenueById(id);

        User currentUser =
                currentUserService.getCurrentUser();

        boolean isAdmin =
                currentUser.getRole().name()
                        .equals("ADMIN");

        boolean isOwnerOfVenue =
                venue.getOwner()
                        .getId()
                        .equals(currentUser.getId());

        if (!isAdmin && !isOwnerOfVenue) {

            throw new IllegalArgumentException(
                    "Non puoi eliminare questo locale"
            );
        }

        venueRepository.delete(venue);
    }

    // =========================
    // VALIDATIONS
    // =========================

    private void validateVenueForCreate(
            Venue venue
    ) {

        if (venue == null) {

            throw new IllegalArgumentException(
                    "Venue nullo"
            );
        }

        if (venue.getName() == null
                || venue.getName().isBlank()) {

            throw new IllegalArgumentException(
                    "Nome venue obbligatorio"
            );
        }

        if (venue.getAddress() == null
                || venue.getAddress().isBlank()) {

            throw new IllegalArgumentException(
                    "Indirizzo venue obbligatorio"
            );
        }

        if (venue.getLatitude() == null) {

            throw new IllegalArgumentException(
                    "Latitudine venue obbligatoria"
            );
        }

        if (venue.getLongitude() == null) {

            throw new IllegalArgumentException(
                    "Longitudine venue obbligatoria"
            );
        }

        if (venue.getType() == null) {

            throw new IllegalArgumentException(
                    "Tipo venue obbligatorio"
            );
        }

        validateLatitude(
                venue.getLatitude()
        );

        validateLongitude(
                venue.getLongitude()
        );
    }

    private void validateId(UUID id) {

        if (id == null) {

            throw new IllegalArgumentException(
                    "ID nullo"
            );
        }
    }

    private void validateLatitude(
            Double latitude
    ) {

        if (latitude < -90
                || latitude > 90) {

            throw new IllegalArgumentException(
                    "Latitudine deve essere tra -90 e 90"
            );
        }
    }

    private void validateLongitude(
            Double longitude
    ) {

        if (longitude < -180
                || longitude > 180) {

            throw new IllegalArgumentException(
                    "Longitudine deve essere tra -180 e 180"
            );
        }
    }
}