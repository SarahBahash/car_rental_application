import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;

public class RegisterWindow extends JFrame {

    private JTextField usernameField;
    private JPasswordField passwordField;
    private JPasswordField confirmPasswordField;

    private final Color BG_DARK = new Color(15, 23, 42);
    private final Color FIELD_BG = new Color(30, 41, 59);
    private final Color ACCENT_ORANGE = new Color(249, 115, 22);
    private final Color TEXT_SLATE = new Color(148, 163, 184);

    public RegisterWindow() {
        setTitle("Drive Elite - Create Account");
        setSize(480, 680);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBackground(BG_DARK);
        setContentPane(mainPanel);

        JPanel registerCard = new JPanel();
        registerCard.setLayout(new BoxLayout(registerCard, BoxLayout.Y_AXIS));
        registerCard.setOpaque(false);
        registerCard.setPreferredSize(new Dimension(350, 560));

        JLabel title = new JLabel("CREATE ACCOUNT");
        title.setFont(new Font("Segoe UI", Font.BOLD, 30));
        title.setForeground(Color.WHITE);
        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel subtitle = new JLabel("New users will be registered as User");
        subtitle.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        subtitle.setForeground(ACCENT_ORANGE);
        subtitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        registerCard.add(title);
        registerCard.add(Box.createRigidArea(new Dimension(0, 5)));
        registerCard.add(subtitle);
        registerCard.add(Box.createRigidArea(new Dimension(0, 40)));

        registerCard.add(createStyledLabel("USERNAME"));
        usernameField = new JTextField();
        styleField(usernameField);
        registerCard.add(usernameField);

        registerCard.add(Box.createRigidArea(new Dimension(0, 20)));

        registerCard.add(createStyledLabel("PASSWORD"));
        passwordField = new JPasswordField();
        styleField(passwordField);
        registerCard.add(passwordField);

        registerCard.add(Box.createRigidArea(new Dimension(0, 20)));

        registerCard.add(createStyledLabel("CONFIRM PASSWORD"));
        confirmPasswordField = new JPasswordField();
        styleField(confirmPasswordField);
        registerCard.add(confirmPasswordField);

        registerCard.add(Box.createRigidArea(new Dimension(0, 30)));

        JButton createBtn = new JButton("CREATE ACCOUNT");
        styleButton(createBtn);
        createBtn.addActionListener(e -> registerUser());
        registerCard.add(createBtn);

        registerCard.add(Box.createRigidArea(new Dimension(0, 15)));

        JButton backBtn = new JButton("BACK TO LOGIN");
        styleButton(backBtn);
        backBtn.addActionListener(e -> {
            new LoginWindow();
            this.dispose();
        });
        registerCard.add(backBtn);

        mainPanel.add(registerCard);
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

    // Handles new account registration
    private void registerUser() {
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword()).trim();
        String confirmPassword = new String(confirmPasswordField.getPassword()).trim();

        if (username.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            JOptionPane.showMessageDialog(
                    this,
                    "Please fill all fields.",
                    "Validation Error",
                    JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        if (!password.equals(confirmPassword)) {
            JOptionPane.showMessageDialog(
                    this,
                    "Passwords do not match.",
                    "Validation Error",
                    JOptionPane.ERROR_MESSAGE
            );
            return;
        }

        if (DatabaseManager.userExists(username)) {
            JOptionPane.showMessageDialog(
                    this,
                    "This username already exists.",
                    "Registration Error",
                    JOptionPane.ERROR_MESSAGE
            );
            return;
        }

        boolean registered = DatabaseManager.registerUser(username, password);

        if (registered) {
            DatabaseManager.insertLog(username, "New account created");
            JOptionPane.showMessageDialog(
                    this,
                    "Account created successfully. You can now log in.",
                    "Success",
                    JOptionPane.INFORMATION_MESSAGE
            );
            new LoginWindow();
            this.dispose();
        } else {
            JOptionPane.showMessageDialog(
                    this,
                    "Failed to create account.",
                    "Registration Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }
}