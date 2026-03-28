import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.*;

import javax.swing.table.DefaultTableModel;

import java.util.List;
import java.util.ArrayList;



public class RentalAppGUI extends JFrame {

    JTextField passengersField = new JTextField();
    JTextField daysField = new JTextField();
    JTextField mileageField = new JTextField();

    JPanel carsPanel;
    boolean isAdmin;

    java.util.List<Car> cars = new ArrayList<>();

    public RentalAppGUI(boolean admin){
        this.isAdmin = admin;

        setTitle("Car Rental Dashboard");
        setSize(1100,650);
        setLayout(new BorderLayout());
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        getContentPane().setBackground(new Color(235,245,255));

        createHeader();
        createSidebar();
        createCarsPanel();

        initializeCars();
        displayCars();

        setVisible(true);
    }

    private void createHeader(){

    JPanel header = new JPanel(new BorderLayout());

    header.setBackground(new Color(25,90,180));
    header.setPreferredSize(new Dimension(100,70));

    JLabel title = new JLabel(" CAR RENTAL SYSTEM");
    title.setForeground(Color.WHITE);
    title.setFont(new Font("Segoe UI",Font.BOLD,26));

    JPanel rightPanel = new JPanel();
    rightPanel.setBackground(new Color(25,90,180));

    if(isAdmin){

        JButton adminBtn = new JButton("Admin Panel");

        adminBtn.addActionListener(e -> new AdminPanel(cars));

        rightPanel.add(adminBtn);
    }

    JButton logout = new JButton("Logout");

    logout.addActionListener(e -> {

        dispose();
        new LoginWindow();

    });

    rightPanel.add(logout);

    header.add(title,BorderLayout.WEST);
    header.add(rightPanel,BorderLayout.EAST);

    add(header,BorderLayout.NORTH);
}

    private void createSidebar(){

        JPanel sidebar = new JPanel();

        sidebar.setLayout(new GridLayout(10,1,10,10));
        sidebar.setBackground(new Color(210,225,250));
        sidebar.setPreferredSize(new Dimension(220,600));

        sidebar.setBorder(BorderFactory.createEmptyBorder(20,20,20,20));

        JLabel label1 = new JLabel("Passengers");
        JLabel label2 = new JLabel("Days");
        JLabel label3 = new JLabel("Mileage");

        JButton search = new JButton("Find Best Car");

        search.setBackground(new Color(40,120,220));
        search.setForeground(Color.WHITE);

        search.addActionListener(e -> filterCars());

        sidebar.add(label1);
        sidebar.add(passengersField);

        sidebar.add(label2);
        sidebar.add(daysField);

        sidebar.add(label3);
        sidebar.add(mileageField);

        sidebar.add(search);

        add(sidebar,BorderLayout.WEST);
    }

    private void createCarsPanel(){

        carsPanel = new JPanel();

        carsPanel.setLayout(new FlowLayout(FlowLayout.LEFT,20,20));
        carsPanel.setBackground(new Color(235,245,255));
        

        JScrollPane scroll = new JScrollPane(carsPanel);
        scroll.getVerticalScrollBar().setUnitIncrement(16);

        add(scroll,BorderLayout.CENTER);
    }

    private void initializeCars(){

        cars.add(new Car("2026 Honda CR-V","SUV",5,3,55,30,"images/crv.jpg"));
        cars.add(new Car("Ford Edge","Crossover",5,3,55,23,"images/edge.jpg"));
        cars.add(new Car("2026 Honda Accord","Sedan",4,2,50,33,"images/accord.jpeg"));
        cars.add(new Car("2026 Ford F150","Truck",5,3,55,23,"images/f150.jpg"));
        cars.add(new Car("Chevrolet Corvette","Coupe",2,1,45,19,"images/corvette.jpg"));
        cars.add(new Car("Lexus RX Hybrid","Hybrid",5,2,60,36,"images/rx.jpg"));
        cars.add(new Car("Toyota Sienna","Minivan",7,2,70,36,"images/sienna.jpg"));
    }

    private void displayCars(){

        carsPanel.removeAll();

        for(Car car : cars){

            carsPanel.add(createCarCard(car));

        }

        carsPanel.revalidate();
        carsPanel.repaint();
    }

    private JPanel createCarCard(Car car){

        JPanel card = new JPanel();

        card.setPreferredSize(new Dimension(230,320));
        card.setLayout(new BorderLayout());
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createLineBorder(new Color(180,200,230)));

        card.addMouseListener(new MouseAdapter(){

            public void mouseEntered(MouseEvent e){

                card.setBorder(BorderFactory.createLineBorder(new Color(40,120,220),2));

            }

            public void mouseExited(MouseEvent e){

                card.setBorder(BorderFactory.createLineBorder(new Color(180,200,230)));

            }

        });

        JLabel title = new JLabel(car.getName(),JLabel.CENTER);
        title.setFont(new Font("Segoe UI",Font.BOLD,14));

        ImageIcon icon = new ImageIcon(car.getImagePath());
        Image img = icon.getImage().getScaledInstance(200,120,Image.SCALE_SMOOTH);

        JLabel image = new JLabel(new ImageIcon(img));
        image.setHorizontalAlignment(JLabel.CENTER);

        JPanel info = new JPanel();
        info.setLayout(new GridLayout(4,1));

        info.add(new JLabel("Category: "+car.getCategory()));
        info.add(new JLabel("Passengers: "+car.getMaxPassengers()));

        info.add(createSeatVisualization(car.getMaxPassengers()));

        JButton select = new JButton("Select");

        select.setBackground(new Color(40,120,220));
        select.setForeground(Color.WHITE);

        select.addActionListener(e -> showReceipt(car));

        info.add(select);

        card.add(title,BorderLayout.NORTH);
        card.add(image,BorderLayout.CENTER);
        card.add(info,BorderLayout.SOUTH);

        return card;
    }

    private JPanel createSeatVisualization(int passengers){

        JPanel seats = new JPanel(new GridLayout(2,4,3,3));

        seats.setBackground(Color.WHITE);

        for(int i=0;i<passengers;i++){

            JLabel seat = new JLabel("●",JLabel.CENTER);
            seat.setForeground(new Color(40,120,220));
            seats.add(seat);

        }

        for(int i=passengers;i<8;i++){

            seats.add(new JLabel(""));

        }

        return seats;
    }

    private void showCarDetails(Car car){

        String message =
                "Car: "+car.getName()+"\n"+
                "Category: "+car.getCategory()+"\n"+
                "Passengers: "+car.getMaxPassengers()+"\n"+
                "Comfort Level: "+car.getComfortLevel();

        JOptionPane.showMessageDialog(this,message);
    }
    private void showReceipt(Car car){

    try{

        int days = SecurityValidator.validateDays(
                Integer.parseInt(daysField.getText()));

        double mileage = SecurityValidator.validateMileage(
                Double.parseDouble(mileageField.getText()));

        double gasPrice = 2.25;

        double rental = car.rentalCost(days);
        double gas = car.gasCost(mileage, gasPrice);
        double total = rental + gas;

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

    private void filterCars(){

    try{

        int passengers = SecurityValidator.validatePassengers(
                Integer.parseInt(passengersField.getText()));

        int days = SecurityValidator.validateDays(
                Integer.parseInt(daysField.getText()));

        double mileage = SecurityValidator.validateMileage(
                Double.parseDouble(mileageField.getText()));

        List<Car> bestCars =
                RentalCalculator.findBestCar(cars, passengers, days, mileage);

        if(bestCars.isEmpty()){

            JOptionPane.showMessageDialog(this,
                    "No cars available for this passenger number");

            return;
        }

        carsPanel.removeAll();

        for(Car car : bestCars){

            carsPanel.add(createCarCard(car));

        }

        carsPanel.revalidate();
        carsPanel.repaint();

    }
    catch(Exception e){

        JOptionPane.showMessageDialog(this,e.getMessage());

    }
}
}