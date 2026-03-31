import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DatabaseManager {

    private static final String URL = "jdbc:sqlite:C:/Users/sarah/OneDrive/Desktop/car_rental_application/car_rental.db";
    // Loads all cars from the database
    public static List<Car> getAllCars() {
        List<Car> cars = new ArrayList<>();
        String sql = "SELECT * FROM cars";

        try (Connection conn = DriverManager.getConnection(URL);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                cars.add(new Car(
                        rs.getString("name"),
                        rs.getString("category"),
                        rs.getInt("passengers"),
                        rs.getInt("comfort"),
                        rs.getDouble("price_day"),
                        rs.getDouble("mpg"),
                        rs.getString("image_path"),
                        rs.getString("type")
                ));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return cars;
    }

    // Authenticates the user and returns the role if credentials are valid
    public static String authenticate(String username, String password) {
        String sql = "SELECT role FROM users WHERE username = ? AND password = ?";

        try (Connection conn = DriverManager.getConnection(URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, username);
            pstmt.setString(2, password);

            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getString("role");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    // Stores security-related events in the logs table
    public static void insertLog(String user, String action) {
        String sql = "INSERT INTO logs (user, action) VALUES (?, ?)";

        try (Connection conn = DriverManager.getConnection(URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, user);
            pstmt.setString(2, action);
            pstmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Stores confirmed booking information
    public static void insertBooking(String bookingID, String user, String car, double price) {
        String sql = "INSERT INTO bookings (booking_id, username, car_name, total_price) VALUES (?, ?, ?, ?)";

        try (Connection conn = DriverManager.getConnection(URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, bookingID);
            pstmt.setString(2, user);
            pstmt.setString(3, car);
            pstmt.setDouble(4, price);
            pstmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Returns all security logs for the admin panel
    public static List<String[]> getAllLogs() {
        List<String[]> logs = new ArrayList<>();
        String sql = "SELECT event_time, user, action FROM logs ORDER BY id DESC";

        try (Connection conn = DriverManager.getConnection(URL);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                logs.add(new String[]{
                        rs.getString("event_time"),
                        rs.getString("user"),
                        rs.getString("action")
                });
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return logs;
    }

    // Returns all bookings for the admin panel
    public static List<String[]> getAllBookings() {
        List<String[]> bookings = new ArrayList<>();
        String sql = "SELECT booking_id, username, car_name, total_price FROM bookings ORDER BY id DESC";
        try (Connection conn = DriverManager.getConnection(URL);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                bookings.add(new String[]{
                        rs.getString("booking_id"),
                        rs.getString("username"),
                        rs.getString("car_name"),
                        String.format("%.2f", rs.getDouble("total_price"))
                });
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return bookings;
    }
}
