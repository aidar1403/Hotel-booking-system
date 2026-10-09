package org.example.repository;

import org.example.model.Room;
import org.example.model.RoomStatus;
import org.example.model.RoomType;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PostgresRoomRepository implements RoomRepository {

    private final String URL = "jdbc:postgresql://localhost:5433/hotel";
    private final String USER = "admin";
    private final String PASSWORD = "admin";

    public PostgresRoomRepository() {
        createTableIfNotExists();
    }

    private void createTableIfNotExists() {
        String sql = """
            CREATE TABLE IF NOT EXISTS rooms (
                id SERIAL PRIMARY KEY,
                room_number VARCHAR(10) NOT NULL,
                type VARCHAR(20) NOT NULL,
                capacity INT NOT NULL,
                status VARCHAR(20) NOT NULL
            )
            """;
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
            System.out.println("Таблица rooms готова.");
        } catch (SQLException e) {
            System.out.println("Ошибка создания таблицы: " + e.getMessage());
        }
    }

    @Override
    public List<Room> getRooms() {
        List<Room> rooms = new ArrayList<>();
        String sql = "SELECT * FROM rooms ORDER BY id";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                rooms.add(mapRowToRoom(rs));
            }
        } catch (SQLException e) {
            System.out.println("Ошибка чтения: " + e.getMessage());
        }
        return rooms;
    }

    @Override
    public Room findRoomById(int id) {
        String sql = "SELECT * FROM rooms WHERE id = ?";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) return mapRowToRoom(rs);
        } catch (SQLException e) {
            System.out.println("Ошибка поиска: " + e.getMessage());
        }
        return null;
    }

    @Override
    public void addRooms(int startNumber, int endNumber, RoomType type, int capacity) {
        if (startNumber < 1 || endNumber < 1) {
            System.out.println("Ошибка: номера комнат не могут быть меньше 1");
            return;
        }
        if (endNumber < startNumber) {
            System.out.println("Ошибка: первый номер не может быть больше последнего");
            return;
        }
        if (capacity <= 0) {
            System.out.println("Ошибка: вместимость должна быть больше 0");
            return;
        }
        if (type == null) {
            System.out.println("Ошибка: тип комнаты не указан");
            return;
        }

        String sql = "INSERT INTO rooms (room_number, type, capacity, status) VALUES (?, ?, ?, ?)";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            for (int i = startNumber; i <= endNumber; i++) {
                stmt.setString(1, String.valueOf(i));
                stmt.setString(2, type.name());
                stmt.setInt(3, capacity);
                stmt.setString(4, RoomStatus.AVAILABLE.name());
                stmt.executeUpdate();
            }
            System.out.println("Добавлено комнат: " + (endNumber - startNumber + 1));
        } catch (SQLException e) {
            System.out.println("Ошибка добавления: " + e.getMessage());
        }
    }

    @Override
    public List<Room> findAvailableByType(RoomType type) {
        List<Room> rooms = new ArrayList<>();
        String sql = "SELECT * FROM rooms WHERE type = ? AND status = ?";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, type.name());
            stmt.setString(2, RoomStatus.AVAILABLE.name());
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) rooms.add(mapRowToRoom(rs));
        } catch (SQLException e) {
            System.out.println("Ошибка поиска: " + e.getMessage());
        }
        return rooms;
    }

    @Override
    public void updateRoom(Room room) {
        String sql = "UPDATE rooms SET room_number=?, type=?, capacity=?, status=? WHERE id=?";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, room.getRoomNumber());
            stmt.setString(2, room.getType().name());
            stmt.setInt(3, room.getCapacity());
            stmt.setString(4, room.getStatus().name());
            stmt.setInt(5, room.getId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Ошибка обновления: " + e.getMessage());
        }
    }

    @Override
    public void deleteRoom(int id) {
        String sql = "DELETE FROM rooms WHERE id = ?";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Ошибка удаления: " + e.getMessage());
        }
    }

    private Room mapRowToRoom(ResultSet rs) throws SQLException {
        Room room = new Room(
                rs.getString("room_number"),
                RoomType.valueOf(rs.getString("type")),
                rs.getInt("capacity"),
                RoomStatus.valueOf(rs.getString("status"))
        );
        room.setId(rs.getInt("id"));
        return room;
    }
}