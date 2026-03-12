import javax.swing.*;
import java.awt.*;

public class CarCard extends JPanel {

    public CarCard(Car car){

        setLayout(new BorderLayout());
        setBorder(BorderFactory.createLineBorder(Color.GRAY));
        setPreferredSize(new Dimension(220,260));

        JLabel title = new JLabel(car.getName(),JLabel.CENTER);
        title.setFont(new Font("Arial",Font.BOLD,14));

        ImageIcon icon = new ImageIcon(car.getImagePath());
        Image img = icon.getImage().getScaledInstance(200,120,Image.SCALE_SMOOTH);

        JLabel image = new JLabel(new ImageIcon(img));
        image.setHorizontalAlignment(JLabel.CENTER);

        JPanel info = new JPanel(new GridLayout(3,1));

        info.add(new JLabel("Category: "+car.getCategory()));
        info.add(new JLabel("Passengers: "+car.getMaxPassengers()));
        info.add(new JLabel("Comfort: "+car.getComfortLevel()));

        JButton select = new JButton("Select Car");

        select.addActionListener(e ->
                JOptionPane.showMessageDialog(this,
                        "You selected "+car.getName()));

        add(title,BorderLayout.NORTH);
        add(image,BorderLayout.CENTER);
        add(info,BorderLayout.WEST);
        add(select,BorderLayout.SOUTH);
    }
}