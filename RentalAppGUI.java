import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.util.List;
import java.util.ArrayList;
import java.util.Comparator;

public class RentalAppGUI extends JFrame {

    private JTextField passengersField, daysField, mileageField;
    private JPanel carsPanel;
    private boolean isAdmin;
    private String currentUser;
    private List<Car> cars = new ArrayList<>();

    private final Color SIDEBAR_DARK = new Color(15, 23, 42);
    private final Color ACCENT_ORANGE = new Color(249, 115, 22);
    private final Color BG_BODY = new Color(241, 245, 249);

    public RentalAppGUI(boolean admin, String user) {
        this.isAdmin = admin;
        this.currentUser = user;

        setTitle("Elite Car Rental - Dashboard");
        setSize(1250, 850);
        setLayout(new BorderLayout());
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        createHeader();
        createSidebar();
        createCarsPanel();

        loadDataFromDatabase();
        displayCars();

        setVisible(true);
    }

    // Load all cars from database
    private void loadDataFromDatabase() {
        this.cars = DatabaseManager.getAllCars();
    }

    // Create the top header section
    private void createHeader() {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(Color.WHITE);
        header.setPreferredSize(new Dimension(100, 70));

        JLabel title = new JLabel("   DRIVE ELITE SYSTEM");
        title.setFont(new Font("Segoe UI", Font.BOLD, 22));
        title.setForeground(SIDEBAR_DARK);

        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 15));
        rightPanel.setOpaque(false);

        if (isAdmin) {
            JButton adminBtn = createStyledButton("ADMIN PANEL", new Color(30, 58, 138), Color.WHITE);
            adminBtn.addActionListener(e -> {
                DatabaseManager.insertLog(currentUser, "Opened admin panel");
                new AdminPanel(cars);
            });
            rightPanel.add(adminBtn);
        }

        JButton logout = createStyledButton("LOGOUT", new Color(220, 38, 38), Color.WHITE);
        logout.addActionListener(e -> {
            dispose();
            new LoginWindow();
        });

        rightPanel.add(logout);

        header.add(title, BorderLayout.WEST);
        header.add(rightPanel, BorderLayout.EAST);

        add(header, BorderLayout.NORTH);
    }

    // Create sidebar inputs
    private void createSidebar() {
        JPanel sidebar = new JPanel();
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setBackground(SIDEBAR_DARK);
        sidebar.setPreferredSize(new Dimension(300, 0));
        sidebar.setBorder(new EmptyBorder(30, 20, 30, 20));

        sidebar.add(createInputLabel("Number of Passengers:"));
        passengersField = createStyledTextField();
        sidebar.add(passengersField);

        sidebar.add(createInputLabel("Rental Duration (Days):"));
        daysField = createStyledTextField();
        sidebar.add(daysField);

        sidebar.add(createInputLabel("Estimated Mileage:"));
        mileageField = createStyledTextField();
        sidebar.add(mileageField);

        JButton search = createStyledButton("SEARCH VEHICLES", ACCENT_ORANGE, Color.BLACK);
        search.addActionListener(e -> filterCars());

        sidebar.add(Box.createRigidArea(new Dimension(0, 20)));
        sidebar.add(search);

        add(sidebar, BorderLayout.WEST);
    }

    private void createCarsPanel() {
        carsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 30, 30));
        carsPanel.setBackground(BG_BODY);

        JScrollPane scroll = new JScrollPane(carsPanel);
        scroll.setBorder(null);
        add(scroll, BorderLayout.CENTER);
    }

    private JLabel createInputLabel(String text) {
        JLabel label = new JLabel(text);
        label.setForeground(Color.LIGHT_GRAY);
        label.setBorder(new EmptyBorder(10, 0, 5, 0));
        return label;
    }

    private JTextField createStyledTextField() {
        JTextField field = new JTextField();
        field.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        field.setBorder(new LineBorder(Color.GRAY));
        return field;
    }

    private JButton createStyledButton(String text, Color bg, Color fg) {
        JButton btn = new JButton(text);
        btn.setBackground(bg);
        btn.setForeground(fg);
        return btn;
    }

    // Display all cars initially sorted by daily price then comfort
    private void displayCars() {
        carsPanel.removeAll();

        cars.sort(Comparator.comparingDouble(Car::getPriceDay)
                .thenComparing(Comparator.comparingInt(Car::getComfortLevel).reversed()));

        for (Car car : cars) {
            carsPanel.add(new CarCard(car, 0.0, e -> showReceipt(car)));
        }

        carsPanel.revalidate();
        carsPanel.repaint();
    }

    // Filter cars using secure input validation
    private void filterCars() {
        try {
            int passengers = SecurityValidator.validatePassengers(
                    Integer.parseInt(passengersField.getText()));

            int days = SecurityValidator.validateDays(
                    Integer.parseInt(daysField.getText()));

            double mileage = SecurityValidator.validateMileage(
                    Double.parseDouble(mileageField.getText()));

            carsPanel.removeAll();

            List<Car> bestCars = RentalCalculator.findBestCar(cars, passengers, days, mileage);

            for (Car car : bestCars) {
                double tripCost = car.totalCost(days, mileage, 2.25);
                carsPanel.add(new CarCard(car, tripCost, e -> showReceipt(car)));
            }

            carsPanel.revalidate();
            carsPanel.repaint();

            if (bestCars.isEmpty()) {
                JOptionPane.showMessageDialog(this, "No vehicles found.");
            }

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Please enter valid numbers.");
        } catch (IllegalArgumentException e) {
            JOptionPane.showMessageDialog(this, e.getMessage());
        }
    }

    // Show booking confirmation and validate input again before saving
    private void showReceipt(Car car) {
        try {
            int days = SecurityValidator.validateDays(
                    Integer.parseInt(daysField.getText()));

            double mileage = SecurityValidator.validateMileage(
                    Double.parseDouble(mileageField.getText()));

            double total = car.totalCost(days, mileage, 2.25);

            String receipt = "Vehicle: " + car.getName() +
                    "\nTotal Cost: $" + String.format("%.2f", total) +
                    "\nConfirm booking?";

            int choice = JOptionPane.showConfirmDialog(this, receipt);

            if (choice == JOptionPane.YES_OPTION) {
                String bookingId = "BK" + System.currentTimeMillis();

                DatabaseManager.insertBooking(bookingId, currentUser, car.getName(), total);
                DatabaseManager.insertLog(currentUser, "Booked car: " + car.getName());

                JOptionPane.showMessageDialog(this, "Booking Confirmed!");
            }

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Please enter valid numbers.");
        } catch (IllegalArgumentException e) {
            JOptionPane.showMessageDialog(this, e.getMessage());
        }
    }
}