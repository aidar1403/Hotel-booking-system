package org.example.repository;

import org.example.model.Booking;
import org.example.model.BookingStatus;
import org.example.model.RoomType;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PostgresBookingRepository implements BookingRepository {

    private final String URL = "jdbc:postgresql://localhost:5433/hotel";
    private final String USER = "admin";
    private final String PASSWORD = "admin";

    public PostgresBookingRepository() {
        createTableIfNotExists();
    }

    private void createTableIfNotExists() {
        String sql = """
            CREATE TABLE IF NOT EXISTS bookings (
                id SERIAL PRIMARY KEY,
                client_name VARCHAR(100) NOT NULL,
                number_of_guests INT NOT NULL,
                requested_room_type VARCHAR(20) NOT NULL,
                start_date VARCHAR(20) NOT NULL,
                end_date VARCHAR(20) NOT NULL,
                status VARCHAR(20) NOT NULL,
                assigned_room_id INT
            )
            """;
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
            System.out.println("Таблица bookings готова.");
        } catch (SQLException e) {
            System.out.println("Ошибка создания таблицы bookings: " + e.getMessage());
        }
    }

    @Override
    public Booking save(Booking booking) {
        String sql = """
            INSERT INTO bookings (client_name, number_of_guests, requested_room_type,
                                  start_date, end_date, status, assigned_room_id)
            VALUES (?, ?, ?, ?, ?, ?, ?) RETURNING id
            """;
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, booking.getClientName());
            stmt.setInt(2, booking.getNumberOfGuests());
            stmt.setString(3, booking.getRequestedRoomType().name());
            stmt.setString(4, booking.getStartDate());
            stmt.setString(5, booking.getEndDate());
            stmt.setString(6, booking.getStatus().name());
            if (booking.getAssignedRoom() != null) {
                stmt.setInt(7, booking.getAssignedRoom());
            } else {
                stmt.setNull(7, Types.INTEGER);
            }
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                booking.setId(rs.getInt("id"));
            }
        } catch (SQLException e) {
            System.out.println("Ошибка сохранения брони: " + e.getMessage());
        }
        return booking;
    }

    @Override
    public List<Booking> findAll() {
        List<Booking> bookings = new ArrayList<>();
        String sql = "SELECT * FROM bookings ORDER BY id";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) bookings.add(mapRowToBooking(rs));
        } catch (SQLException e) {
            System.out.println("Ошибка чтения броней: " + e.getMessage());
        }
        return bookings;
    }

    @Override
    public Booking findById(int id) {
        String sql = "SELECT * FROM bookings WHERE id = ?";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) return mapRowToBooking(rs);
        } catch (SQLException e) {
            System.out.println("Ошибка поиска брони: " + e.getMessage());
        }
        return null;
    }

    @Override
    public void deleteById(int id) {
        String sql = "DELETE FROM bookings WHERE id = ?";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Ошибка удаления брони: " + e.getMessage());
        }
    }

    @Override
    public void update(Booking booking) {
        String sql = """
            UPDATE bookings SET client_name=?, number_of_guests=?, requested_room_type=?,
                                start_date=?, end_date=?, status=?, assigned_room_id=?
            WHERE id=?
            """;
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, booking.getClientName());
            stmt.setInt(2, booking.getNumberOfGuests());
            stmt.setString(3, booking.getRequestedRoomType().name());
            stmt.setString(4, booking.getStartDate());
            stmt.setString(5, booking.getEndDate());
            stmt.setString(6, booking.getStatus().name());
            if (booking.getAssignedRoom() != null) {
                stmt.setInt(7, booking.getAssignedRoom());
            } else {
                stmt.setNull(7, Types.INTEGER);
            }
            stmt.setInt(8, booking.getId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Ошибка обновления брони: " + e.getMessage());
        }
    }

    private Booking mapRowToBooking(ResultSet rs) throws SQLException {
        Booking booking = new Booking(
                rs.getString("client_name"),
                rs.getInt("number_of_guests"),
                RoomType.valueOf(rs.getString("requested_room_type")),
                rs.getString("start_date"),
                rs.getString("end_date")
        );
        booking.setId(rs.getInt("id"));
        booking.setStatus(BookingStatus.valueOf(rs.getString("status")));
        int roomId = rs.getInt("assigned_room_id");
        if (!rs.wasNull()) {
            booking.setAssignedRoom(roomId);
        }
        return booking;
    }
}