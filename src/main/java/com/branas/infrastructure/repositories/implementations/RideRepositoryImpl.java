package com.branas.infrastructure.repositories.implementations;

import com.branas.api.ports.RideRepository;
import com.branas.domain.entities.Ride;
import com.branas.infrastructure.exceptions.DataBaseException;
import com.branas.infrastructure.jpaEntities.RideEntity;
import com.branas.infrastructure.repositories.RideRepositoryJpa;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

import java.util.Optional;
import java.util.UUID;

@ApplicationScoped
@Transactional
public class RideRepositoryImpl implements RideRepository {

    @Inject
    RideRepositoryJpa rideRepositoryJpa;

    @Override
    public void save(Ride ride) {
        RideEntity rideEntity = RideEntity.create(ride);
        rideRepositoryJpa.persist(rideEntity);
    }

    @Override
    public Optional<Ride> getRideById(UUID rideId) {
      RideEntity rideEntity = rideRepositoryJpa.findByIdOptional(rideId)
              .orElseThrow(() -> new DataBaseException("Error while getting ride by id"));
        return Optional.of(Ride.restore(rideEntity));
    }

    @Override
    public void update(Ride ride) {
        RideEntity rideEntity = rideRepositoryJpa.findByIdOptional(ride.getRideId())
                .orElseThrow(() -> new DataBaseException("Error while getting ride by id"));
        rideEntity.setDriverId(ride.getDriverId());
        rideEntity.setStatus(ride.getStatus());
        rideEntity.setDistance(ride.getDistance());
        rideEntity.setPrice(ride.getPrice());
        rideEntity.setFromLatitude(ride.getFromCoordinate().getLatitude());
        rideEntity.setFromLongitude(ride.getFromCoordinate().getLongitude());
        rideEntity.setToLatitude(ride.getToCoordinate().getLatitude());
        rideEntity.setToLongitude(ride.getToCoordinate().getLongitude());
        rideRepositoryJpa.persist(rideEntity);
    }

    public Ride getRideByPassengerId(UUID passengerId) {
       RideEntity rideEntity = rideRepositoryJpa.findByPassengerId(passengerId)
               .orElseThrow( () -> new DataBaseException("Error while getting ride by passenger id"));
        return Ride.restore(rideEntity);
    }

    public Ride getRideByDriverId(UUID driverId) {
        RideEntity rideEntity = rideRepositoryJpa.findByDriverId(driverId)
                .orElseThrow( () -> new DataBaseException("Error while getting ride by driver id"));
        return Ride.restore(rideEntity);
    }
}

