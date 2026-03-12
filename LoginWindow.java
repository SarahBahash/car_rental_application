import javax.swing.*;
import java.awt.*;

public class LoginWindow extends JFrame {

    JTextField username = new JTextField();
    JPasswordField password = new JPasswordField();

    public LoginWindow(){

        setTitle("Login");
        setSize(300,200);
        setLayout(new GridLayout(5,1));

        add(new JLabel("Username"));
        add(username);

        add(new JLabel("Password"));
        add(password);

        JButton login = new JButton("Login");

        login.addActionListener(e -> authenticate());

        add(login);

        setVisible(true);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
    }

    private void authenticate(){

        String user = username.getText();
        String pass = new String(password.getPassword());

        if(user.equals("admin") && pass.equals("admin123")){

            SecureLogger.log("Admin logged in");
            new RentalAppGUI(true);
            dispose();

        }
        else if(user.equals("user") && pass.equals("user123")){

            SecureLogger.log("User logged in");
            new RentalAppGUI(false);
            dispose();

        }
        else{

            JOptionPane.showMessageDialog(this,"Invalid login");

        }
    }

    public static void main(String[] args){

        new LoginWindow();

    }
}