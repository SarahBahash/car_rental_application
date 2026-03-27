import javax.swing.*;
import java.awt.*;

public class CarCard extends JPanel {

    private Car car;
    private JTextField daysField;
    private JTextField mileageField;

    public CarCard(Car car, JTextField daysField, JTextField mileageField){

        this.car = car;
        this.daysField = daysField;
        this.mileageField = mileageField;

        setLayout(new BorderLayout());
        setBorder(BorderFactory.createLineBorder(new Color(180,200,230)));
        setPreferredSize(new Dimension(240,300));
        setBackground(Color.WHITE);

        // ===== TITLE =====
        JLabel title = new JLabel(car.getName(),JLabel.CENTER);
        title.setFont(new Font("Segoe UI",Font.BOLD,14));

        // ===== IMAGE =====
        ImageIcon icon = new ImageIcon(car.getImagePath());
        Image img = icon.getImage().getScaledInstance(200,120,Image.SCALE_SMOOTH);
        JLabel image = new JLabel(new ImageIcon(img));
        image.setHorizontalAlignment(JLabel.CENTER);

        // ===== INFO =====
        JPanel info = new JPanel(new GridLayout(3,1));
        info.setBackground(Color.WHITE);

        info.add(new JLabel("Category: "+car.getCategory()));
        info.add(new JLabel("Passengers: "+car.getMaxPassengers()));
        info.add(new JLabel("Comfort: "+car.getComfortLevel()));

        // ===== BUTTON =====
        JButton select = new JButton("Select Car");
        select.setBackground(new Color(40,120,220));
        select.setForeground(Color.WHITE);

        select.addActionListener(e -> showReceipt());

        add(title,BorderLayout.NORTH);
        add(image,BorderLayout.CENTER);
        add(info,BorderLayout.WEST);
        add(select,BorderLayout.SOUTH);
    }

    // ===== RECEIPT POPUP =====
    private void showReceipt(){

        try{

            int days = SecurityValidator.validateDays(
                    Integer.parseInt(daysField.getText()));

            double mileage = SecurityValidator.validateMileage(
                    Double.parseDouble(mileageField.getText()));

            double gasPrice = 2.25;

            double rental = car.rentalCost(days);
            double gas = car.gasCost(mileage, gasPrice);
            double total = rental + gas;

            // ===== Receipt UI =====
            JPanel receipt = new JPanel(new GridLayout(12,1,5,5));

            receipt.add(new JLabel("🚗 Car: " + car.getName()));
            receipt.add(new JLabel("Category: " + car.getCategory()));
            receipt.add(new JLabel("Passengers: " + car.getMaxPassengers()));

            receipt.add(new JLabel("-----------------------------"));

            receipt.add(new JLabel("Days: " + days));
            receipt.add(new JLabel("Mileage: " + mileage + " miles"));
            receipt.add(new JLabel("Price/Day: $" + car.rentalCost(1)));

            receipt.add(new JLabel("-----------------------------"));

            receipt.add(new JLabel("Rental Cost: $" + String.format("%.2f", rental)));
            receipt.add(new JLabel("Fuel Cost: $" + String.format("%.2f", gas)));

            receipt.add(new JLabel("-----------------------------"));

            receipt.add(new JLabel("TOTAL: $" + String.format("%.2f", total)));

            int choice = JOptionPane.showConfirmDialog(
                    this,
                    receipt,
                    "Confirm Booking",
                    JOptionPane.OK_CANCEL_OPTION,
                    JOptionPane.INFORMATION_MESSAGE
            );

            // ===== AFTER CONFIRM =====
            if(choice == JOptionPane.OK_OPTION){

                String bookingID = "BK" + System.currentTimeMillis();

                SecureLogger.log("Booking confirmed: " + bookingID + " | " + car.getName());

                JOptionPane.showMessageDialog(
                        this,
                        "✅ Booking Confirmed!\n\n" +
                        "Car: " + car.getName() + "\n" +
                        "Total Paid: $" + String.format("%.2f", total) + "\n" +
                        "Booking ID: " + bookingID,
                        "Success",
                        JOptionPane.INFORMATION_MESSAGE
                );
            }

        }
        catch(Exception e){

            JOptionPane.showMessageDialog(this,
                    "❌ Please enter valid Days and Mileage");

        }
    }
}