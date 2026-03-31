import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;

public class LoginWindow extends JFrame {

    private JTextField usernameField;
    private JPasswordField passwordField;

    private final Color BG_DARK = new Color(15, 23, 42);
    private final Color FIELD_BG = new Color(30, 41, 59);
    private final Color ACCENT_ORANGE = new Color(249, 115, 22);
    private final Color TEXT_SLATE = new Color(148, 163, 184);

    public LoginWindow() {
        setTitle("Drive Elite - Secure Login");
        setSize(480, 620);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBackground(BG_DARK);
        setContentPane(mainPanel);

        JPanel loginCard = new JPanel();
        loginCard.setLayout(new BoxLayout(loginCard, BoxLayout.Y_AXIS));
        loginCard.setOpaque(false);
        loginCard.setPreferredSize(new Dimension(350, 520));

        JLabel title = new JLabel("DRIVE ELITE");
        title.setFont(new Font("Segoe UI", Font.BOLD, 36));
        title.setForeground(Color.WHITE);
        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel subtitle = new JLabel("Enterprise Rental System");
        subtitle.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        subtitle.setForeground(ACCENT_ORANGE);
        subtitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        loginCard.add(title);
        loginCard.add(Box.createRigidArea(new Dimension(0, 5)));
        loginCard.add(subtitle);
        loginCard.add(Box.createRigidArea(new Dimension(0, 50)));

        loginCard.add(createStyledLabel("USERNAME"));
        usernameField = new JTextField();
        styleField(usernameField);
        loginCard.add(usernameField);

        loginCard.add(Box.createRigidArea(new Dimension(0, 25)));

        loginCard.add(createStyledLabel("PASSWORD"));
        passwordField = new JPasswordField();
        styleField(passwordField);
        loginCard.add(passwordField);

        loginCard.add(Box.createRigidArea(new Dimension(0, 35)));

        JButton loginBtn = new JButton("LOGIN TO SYSTEM");
        styleButton(loginBtn);
        loginBtn.addActionListener(e -> authenticate());
        loginCard.add(loginBtn);

        loginCard.add(Box.createRigidArea(new Dimension(0, 15)));

        JButton registerBtn = new JButton("CREATE ACCOUNT");
        styleButton(registerBtn);
        registerBtn.addActionListener(e -> {
            new RegisterWindow();
            this.dispose();
        });
        loginCard.add(registerBtn);

        mainPanel.add(loginCard);
        setVisible(true);
    }

    private JLabel createStyledLabel(String text) {
        JLabel label = new JLabel(text);
        label.setForeground(TEXT_SLATE);
        label.setFont(new Font("Segoe UI", Font.BOLD, 11));
        label.setAlignmentX(Component.CENTER_ALIGNMENT);
        label.setBorder(new EmptyBorder(0, 0, 8, 0));
        return label;
    }

    private void styleField(JTextField field) {
        field.setBackground(FIELD_BG);
        field.setForeground(Color.WHITE);
        field.setCaretColor(ACCENT_ORANGE);
        field.setFont(new Font("Segoe UI", Font.BOLD, 16));
        field.setHorizontalAlignment(JTextField.CENTER);
        field.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(51, 65, 85), 2),
                BorderFactory.createEmptyBorder(10, 15, 10, 15)
        ));
        field.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));
    }

    private void styleButton(JButton button) {
        button.setFont(new Font("Segoe UI", Font.BOLD, 15));
        button.setBackground(ACCENT_ORANGE);
        button.setForeground(Color.BLACK);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setOpaque(true);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setMaximumSize(new Dimension(Integer.MAX_VALUE, 55));
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
    }

    // Handles user authentication
    private void authenticate() {
        String user = usernameField.getText().trim();
        String pass = new String(passwordField.getPassword());

        if (user.isEmpty() || pass.isEmpty()) {
            showErrorBorder();
            JOptionPane.showMessageDialog(
                    this,
                    "Please fill all fields",
                    "Validation Error",
                    JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        String role = DatabaseManager.authenticate(user, pass);

        if (role != null) {
            DatabaseManager.insertLog(user, "Successful login");
            new RentalAppGUI(role.equalsIgnoreCase("Admin"), user);
            this.dispose();
        } else {
            DatabaseManager.insertLog(user, "Failed login attempt");
            showErrorBorder();
            JOptionPane.showMessageDialog(
                    this,
                    "Invalid credentials. Access Denied.",
                    "Security Alert",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private void showErrorBorder() {
        LineBorder errorBorder = new LineBorder(Color.RED, 2);

        usernameField.setBorder(BorderFactory.createCompoundBorder(
                errorBorder,
                BorderFactory.createEmptyBorder(10, 15, 10, 15)
        ));

        passwordField.setBorder(BorderFactory.createCompoundBorder(
                errorBorder,
                BorderFactory.createEmptyBorder(10, 15, 10, 15)
        ));
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(LoginWindow::new);
    }
}