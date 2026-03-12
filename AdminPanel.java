import javax.swing.*;
import java.awt.*;
import java.io.*;

public class AdminPanel extends JFrame {

    JTextArea logArea = new JTextArea();

    public AdminPanel(){

        setTitle("Admin Control Panel");
        setSize(500,400);

        logArea.setEditable(false);

        JButton loadLogs = new JButton("View Security Logs");

        loadLogs.addActionListener(e -> loadLogs());

        add(new JScrollPane(logArea),BorderLayout.CENTER);
        add(loadLogs,BorderLayout.SOUTH);

        setVisible(true);
    }

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
}