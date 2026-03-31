import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;

public class CarCard extends JPanel {

    private final Color COLOR_PRIMARY = new Color(15, 23, 42);
    private final Color COLOR_ACCENT = new Color(249, 115, 22);
    private final Color COLOR_SUBTEXT = new Color(100, 116, 139);
    private final int ROUNDNESS = 30;

    // Constructor: receives car data and calculated total cost
    public CarCard(Car car, double calculatedTotal, ActionListener onBook) {
        setLayout(new BorderLayout(0, 10));
        setOpaque(false);
        setPreferredSize(new Dimension(320, 500));
        setBorder(new EmptyBorder(18, 18, 18, 18));

        // --- Image Section ---
        JLabel imgLabel = new JLabel();
        try {
            File imgFile = new File(car.getImagePath());
            BufferedImage buffered = ImageIO.read(imgFile);
            Image img = buffered.getScaledInstance(280, 145, Image.SCALE_SMOOTH);
            imgLabel.setIcon(new ImageIcon(img));
        } catch (Exception e) {
            imgLabel.setText("No Image Found");
        }
        imgLabel.setHorizontalAlignment(JLabel.CENTER);
        add(imgLabel, BorderLayout.NORTH);

        // --- Info Section ---
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.setOpaque(false);

        // Shows type (Economy, Standard...) and category (SUV, Sedan...)
        JLabel categoryTag = new JLabel("  " + car.getType().toUpperCase() + " • " + car.getCategory().toUpperCase() + "  ");
        categoryTag.setOpaque(true);
        categoryTag.setBackground(new Color(37, 99, 235, 15));
        categoryTag.setForeground(new Color(37, 99, 235));
        categoryTag.setFont(new Font("Segoe UI", Font.BOLD, 10));
        categoryTag.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Car name
        JLabel nameLabel = new JLabel(car.getName());
        nameLabel.setFont(new Font("Segoe UI", Font.BOLD, 22));
        nameLabel.setForeground(COLOR_PRIMARY);
        nameLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Comfort badge (Poor / Medium / Good)
        JPanel comfortBadge = createComfortBadge(car.getComfortLevel());
        comfortBadge.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Specs: passengers + MPG
        JLabel specsLabel = new JLabel("<html><body style='font-family: Segoe UI; color: #64748b; font-size: 11px;'>" +
                "<table width='240' cellpadding='0' cellspacing='0'>" +
                "<tr>" +
                "<td width='45%' align='right'>👤 <b style='color:#1e293b;'>" + car.getMaxPassengers() + "</b> Max Seats</td>" +
                "<td width='10%' align='center'>&nbsp;•&nbsp;</td>" +
                "<td width='45%' align='left'>⛽ <b style='color:#1e293b;'>" + car.getMpg() + "</b> MPG</td>" +
                "</tr></table>" +
                "</body></html>");
        specsLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Price display (daily or total trip)
        JPanel priceContainer = new JPanel(new FlowLayout(FlowLayout.LEFT, 2, 0));
        priceContainer.setOpaque(false);
        priceContainer.setAlignmentX(Component.LEFT_ALIGNMENT);

        if (calculatedTotal > 0) {
            JLabel totalLabel = new JLabel("Total Trip: ");
            totalLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
            totalLabel.setForeground(COLOR_SUBTEXT);

            JLabel totalValue = new JLabel("$" + String.format("%.2f", calculatedTotal));
            totalValue.setFont(new Font("Segoe UI", Font.BOLD, 28));
            totalValue.setForeground(COLOR_ACCENT);

            priceContainer.add(totalLabel);
            priceContainer.add(totalValue);
        } else {
            JLabel priceValue = new JLabel("$" + (int) car.getPriceDay());
            priceValue.setFont(new Font("Segoe UI", Font.BOLD, 32));
            priceValue.setForeground(COLOR_PRIMARY);

            JLabel perDayLabel = new JLabel(" / day");
            perDayLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
            perDayLabel.setForeground(COLOR_SUBTEXT);

            priceContainer.add(priceValue);
            priceContainer.add(perDayLabel);
        }

        infoPanel.add(categoryTag);
        infoPanel.add(Box.createRigidArea(new Dimension(0, 8)));
        infoPanel.add(nameLabel);
        infoPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        infoPanel.add(comfortBadge);
        infoPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        infoPanel.add(specsLabel);
        infoPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        infoPanel.add(priceContainer);

        add(infoPanel, BorderLayout.CENTER);

        // Booking button
        JButton bookBtn = new JButton("Confirm Booking");
        bookBtn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        bookBtn.setForeground(Color.WHITE);
        bookBtn.setBackground(COLOR_ACCENT);
        bookBtn.setFocusPainted(false);
        bookBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        bookBtn.setPreferredSize(new Dimension(0, 48));
        bookBtn.addActionListener(onBook);

        add(bookBtn, BorderLayout.SOUTH);
    }

    // Creates colored badge based on comfort level
    private JPanel createComfortBadge(int level) {
        String text;
        Color bg, fg;

        if (level >= 3) {
            text = "GOOD COMFORT";
            bg = new Color(22, 163, 74, 25);
            fg = new Color(22, 163, 74);
        } else if (level == 2) {
            text = "MEDIUM COMFORT";
            bg = new Color(234, 179, 8, 25);
            fg = new Color(161, 98, 7);
        } else {
            text = "POOR COMFORT";
            bg = new Color(220, 38, 38, 15);
            fg = new Color(220, 38, 38);
        }

        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 4));
        panel.setOpaque(true);
        panel.setBackground(bg);
        panel.setBorder(BorderFactory.createEmptyBorder(0, 2, 0, 8));

        JLabel label = new JLabel("✨ " + text);
        label.setFont(new Font("Segoe UI", Font.BOLD, 10));
        label.setForeground(fg);

        panel.add(label);
        panel.setMaximumSize(new Dimension(165, 25));

        return panel;
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();

        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        g2.setColor(new Color(0, 0, 0, 10));
        g2.fillRoundRect(3, 3, getWidth() - 6, getHeight() - 6, ROUNDNESS, ROUNDNESS);

        g2.setColor(Color.WHITE);
        g2.fillRoundRect(0, 0, getWidth() - 4, getHeight() - 4, ROUNDNESS, ROUNDNESS);

        g2.setColor(new Color(230, 235, 245));
        g2.drawRoundRect(0, 0, getWidth() - 4, getHeight() - 4, ROUNDNESS, ROUNDNESS);

        g2.dispose();
    }
}