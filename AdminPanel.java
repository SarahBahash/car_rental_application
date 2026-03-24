import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.*;
import java.util.List;

public class AdminPanel extends JFrame {

    JTextArea logArea = new JTextArea();
    JTable carTable;
    DefaultTableModel tableModel;

    public AdminPanel(List<Car> cars){

        setTitle("Admin Control Panel");
        setSize(700,500);
        setLayout(new BorderLayout());

        JTabbedPane tabs = new JTabbedPane();

        // ===== LOG PANEL =====
        JPanel logPanel = new JPanel(new BorderLayout());

        logArea.setEditable(false);

        JButton loadLogs = new JButton("Load Security Logs");

        loadLogs.addActionListener(e -> loadLogs());

        logPanel.add(new JScrollPane(logArea),BorderLayout.CENTER);
        logPanel.add(loadLogs,BorderLayout.SOUTH);

        // ===== CAR PANEL =====
        JPanel carPanel = new JPanel(new BorderLayout());

        tableModel = new DefaultTableModel();

        tableModel.addColumn("Car");
        tableModel.addColumn("Category");
        tableModel.addColumn("Passengers");
        tableModel.addColumn("Comfort");

        carTable = new JTable(tableModel);

        loadCars(cars);

        carPanel.add(new JScrollPane(carTable),BorderLayout.CENTER);

        // ===== ADD TABS =====
        tabs.add("Security Logs", logPanel);
        tabs.add("Cars List", carPanel);

        add(tabs,BorderLayout.CENTER);

        setVisible(true);
    }

    // ===== LOAD LOGS =====
    private void loadLogs(){

        try{

            BufferedReader reader =
                    new BufferedReader(new FileReader("security_log.txt"));

            logArea.read(reader,null);

        }
        catch(Exception e){

            logArea.setText("No logs available");

        }
    }

    // ===== LOAD CARS =====
    private void loadCars(List<Car> cars){

        tableModel.setRowCount(0);

        for(Car car : cars){

            tableModel.addRow(new Object[]{
                    car.getName(),
                    car.getCategory(),
                    car.getMaxPassengers(),
                    car.getComfortLevel()
            });
        }
    }
}