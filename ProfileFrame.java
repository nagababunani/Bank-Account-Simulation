package bank;

import javax.swing.*;
import java.awt.*;

public class ProfileFrame extends JFrame {

    private AccountDAO dao = new AccountDAO();

    public ProfileFrame(String accountNo) {
        setTitle("My Profile");
        setSize(420, 400);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(null);
        getContentPane().setBackground(new Color(220, 220, 220));

        JLabel title = new JLabel("MY PROFILE", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 16));
        title.setBounds(0, 15, 420, 28);
        add(title);

        JSeparator sep = new JSeparator();
        sep.setBounds(20, 48, 370, 2);
        add(sep);

        String[] info = dao.getCustomerInfo(accountNo);

        if (info == null) {
            JLabel err = new JLabel("Could not load profile.", SwingConstants.CENTER);
            err.setBounds(0, 100, 420, 25);
            add(err);
            return;
        }

        String[] labels = {
            "Account No  :",
            "Full Name    :",
            "Email            :",
            "Phone           :",
            "Balance        :",
            "Status           :",
            "Member Since:",
            "Last Active   :"
        };

        String[] values = {
            info[0],
            info[1],
            info[2].isEmpty() ? "Not provided" : info[2],
            info[3].isEmpty() ? "Not provided" : info[3],
            "Rs. " + info[4],
            info[5],
            info[6],
            info[7]
        };

        int y = 60;
        for (int i = 0; i < labels.length; i++) {

            JLabel lbl = new JLabel(labels[i]);
            lbl.setFont(new Font("Arial", Font.BOLD, 12));
            lbl.setBounds(30, y, 140, 24);
            add(lbl);

            JLabel val = new JLabel(values[i]);
            val.setFont(new Font("Arial", Font.PLAIN, 12));
            if (i == 4) val.setForeground(new Color(0, 128, 0));
            if (i == 5) val.setForeground(
                values[i].equals("ACTIVE") ? new Color(0, 128, 0) : Color.RED);
            val.setBounds(185, y, 210, 24);
            add(val);

            y += 30;
        }

        JButton closeBtn = new JButton("Close");
        closeBtn.setBounds(155, 335, 110, 32);
        closeBtn.setFont(new Font("Arial", Font.PLAIN, 13));
        add(closeBtn);

        closeBtn.addActionListener(e -> dispose());
    }
}