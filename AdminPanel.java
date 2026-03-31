import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class AdminPanel extends JFrame {

    private JTable logsTable;
    private JTable carsTable;
    private JTable bookingsTable;

    private DefaultTableModel logsModel;
    private DefaultTableModel carsModel;
    private DefaultTableModel bookingsModel;

    public AdminPanel(List<Car> cars) {
        setTitle("Admin Control Panel");
        setSize(850, 550);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        JTabbedPane tabs = new JTabbedPane();

        tabs.add("Security Logs", createLogsPanel());
        tabs.add("Cars List", createCarsPanel(cars));
        tabs.add("Bookings List", createBookingsPanel());

        add(tabs, BorderLayout.CENTER);

        setVisible(true);
    }

    // Displays security events stored in the database
    private JPanel createLogsPanel() {
        JPanel panel = new JPanel(new BorderLayout());

        logsModel = new DefaultTableModel();
        logsModel.addColumn("Time");
        logsModel.addColumn("User");
        logsModel.addColumn("Action");

        logsTable = new JTable(logsModel);
        loadLogs();

        JButton refreshButton = new JButton("Refresh Logs");
        refreshButton.addActionListener(e -> loadLogs());

        panel.add(new JScrollPane(logsTable), BorderLayout.CENTER);
        panel.add(refreshButton, BorderLayout.SOUTH);

        return panel;
    }

    // Displays all available cars in the system
    private JPanel createCarsPanel(List<Car> cars) {
        JPanel panel = new JPanel(new BorderLayout());

        carsModel = new DefaultTableModel();
        carsModel.addColumn("Car");
        carsModel.addColumn("Category");
        carsModel.addColumn("Passengers");
        carsModel.addColumn("Comfort");
        carsModel.addColumn("Type");

        carsTable = new JTable(carsModel);
        loadCars(cars);

        panel.add(new JScrollPane(carsTable), BorderLayout.CENTER);

        return panel;
    }

    // Displays confirmed bookings
    private JPanel createBookingsPanel() {
        JPanel panel = new JPanel(new BorderLayout());

        bookingsModel = new DefaultTableModel();
        bookingsModel.addColumn("Booking ID");
        bookingsModel.addColumn("Username");
        bookingsModel.addColumn("Car");
        bookingsModel.addColumn("Total Price");

        bookingsTable = new JTable(bookingsModel);
        loadBookings();

        JButton refreshButton = new JButton("Refresh Bookings");
        refreshButton.addActionListener(e -> loadBookings());

        panel.add(new JScrollPane(bookingsTable), BorderLayout.CENTER);
        panel.add(refreshButton, BorderLayout.SOUTH);

        return panel;
    }

    private void loadLogs() {
        logsModel.setRowCount(0);

        List<String[]> logs = DatabaseManager.getAllLogs();
        for (String[] log : logs) {
            logsModel.addRow(log);
        }
    }

    private void loadCars(List<Car> cars) {
        carsModel.setRowCount(0);

        for (Car car : cars) {
            carsModel.addRow(new Object[]{
                    car.getName(),
                    car.getCategory(),
                    car.getMaxPassengers(),
                    getComfortText(car.getComfortLevel()),
                    car.getType()
            });
        }
    }

    private void loadBookings() {
        bookingsModel.setRowCount(0);

        List<String[]> bookings = DatabaseManager.getAllBookings();
        for (String[] booking : bookings) {
            bookingsModel.addRow(booking);
        }
    }

    private String getComfortText(int comfortLevel) {
        if (comfortLevel >= 3) {
            return "Good";
        } else if (comfortLevel == 2) {
            return "Medium";
        } else {
            return "Poor";
        }
    }
}