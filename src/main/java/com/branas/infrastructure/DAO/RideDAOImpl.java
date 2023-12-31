package com.branas.infrastructure.DAO;

import com.branas.domain.entities.Coordinate;
import com.branas.domain.entities.Ride;
import com.branas.api.ports.RideDAO;
import com.branas.infrastructure.exceptions.DataBaseException;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@ApplicationScoped
public class RideDAOImpl implements RideDAO {

    @Inject
    DataSource dataSource;

    private Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }

    @Override
    public void save(Ride ride) {
        try (PreparedStatement preparedStatement = getConnection().prepareStatement("""
                insert into cccat13.ride (ride_id, passenger_id, driver_id, status, fare, distance, date, from_lat, from_long, to_lat, to_long)
                values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)""")) {
            preparedStatement.setObject(1, ride.getRideId());
            preparedStatement.setObject(2, ride.getPassengerId());
            preparedStatement.setObject(3, ride.getDriverId());
            preparedStatement.setString(4, ride.getStatus());
            preparedStatement.setBigDecimal(5, ride.getPrice());
            preparedStatement.setDouble(6, ride.getDistance());
            preparedStatement.setObject(7, ride.getTimestamp());
            preparedStatement.setObject(8, ride.getFromCoordinate().getLatitude());
            preparedStatement.setObject(9, ride.getFromCoordinate().getLongitude());
            preparedStatement.setObject(10, ride.getToCoordinate().getLatitude());
            preparedStatement.setObject(11, ride.getToCoordinate().getLongitude());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new DataBaseException("Error while saving ride", e);
        }

    }

    @Override
    public Optional<Ride> getRideById(UUID rideId) {
        try (PreparedStatement preparedStatement = getConnection().prepareStatement("select * from cccat13.ride where ride_id = ?")) {
            preparedStatement.setObject(1, rideId);
            ResultSet resultSet = preparedStatement.executeQuery();
            return Optional.ofNullable(getRide(resultSet));
        } catch (SQLException e) {
            throw new DataBaseException("Error while getting ride by id", e);
        }
    }

    @Override
    public void update(Ride ride) {
        try (PreparedStatement preparedStatement = getConnection().prepareStatement("""
                update cccat13.ride set passenger_id = ?, driver_id = ?, status = ?, fare = ?, distance = ?, date = ?, from_lat = ?, from_long = ?, to_lat = ?, to_long = ?
                where ride_id = ?""")) {
            preparedStatement.setObject(1, ride.getPassengerId());
            preparedStatement.setObject(2, ride.getDriverId());
            preparedStatement.setString(3, ride.getStatus());
            preparedStatement.setBigDecimal(4, ride.getPrice());
            preparedStatement.setDouble(5, ride.getDistance());
            preparedStatement.setObject(6, ride.getTimestamp());
            preparedStatement.setDouble(7, ride.getFromCoordinate().getLatitude());
            preparedStatement.setDouble(8, ride.getFromCoordinate().getLongitude());
            preparedStatement.setDouble(9, ride.getToCoordinate().getLatitude());
            preparedStatement.setDouble(10, ride.getToCoordinate().getLongitude());
            preparedStatement.setObject(11, ride.getRideId());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new DataBaseException("Error while updating ride", e);
        }
    }

    public Ride getRideByPassengerId(UUID passengerId) {
        try (PreparedStatement preparedStatement = getConnection().prepareStatement("SELECT * FROM cccat13.ride WHERE passenger_id = ? ORDER BY date DESC LIMIT 1")) {
            preparedStatement.setObject(1, passengerId);
            ResultSet resultSet = preparedStatement.executeQuery();
            return getRide(resultSet);
        } catch (SQLException e) {
            throw new DataBaseException("Error while getting ride by passenger id", e);
        }
    }

    public Ride getRideByDriverId(UUID driverId) {
        try (PreparedStatement preparedStatement = getConnection().prepareStatement("SELECT * FROM cccat13.ride WHERE driver_id = ? ORDER BY date DESC LIMIT 1")) {
            preparedStatement.setObject(1, driverId);
            ResultSet resultSet = preparedStatement.executeQuery();
            return getRide(resultSet);
        } catch (SQLException e) {
            throw new DataBaseException("Error while getting ride by driver id", e);
        }
    }

    private Ride getRide(ResultSet resultSet) throws SQLException {
        if (resultSet.next()) {
            return Ride.restore(
                    resultSet.getObject("ride_id", UUID.class),
                    resultSet.getObject("passenger_id", UUID.class),
                    resultSet.getObject("driver_id", UUID.class),
                    resultSet.getString("status"),
                    resultSet.getDouble("distance"),
                    resultSet.getBigDecimal("fare"),
                    resultSet.getObject("date", LocalDateTime.class),
                    new Coordinate(
                            resultSet.getDouble("from_lat"),
                            resultSet.getDouble("from_long")),
                    new Coordinate(
                            resultSet.getDouble("to_lat"),
                            resultSet.getDouble("to_long"))
        );
        } else {
            return null;
        }
    }
}
