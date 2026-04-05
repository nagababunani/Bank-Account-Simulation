package bank;

import javax.swing.*;
import java.awt.*;

public class LoginFrame extends JFrame {

    private JTextField accountField;
    private JPasswordField pinField;
    private AccountDAO dao = new AccountDAO();

    public LoginFrame() {
        setTitle("Bank Simulation System");
        setSize(500, 340);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(null);
        getContentPane().setBackground(new Color(220, 220, 220));

        JLabel title = new JLabel("LOGIN / CREATE ACCOUNT", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 18));
        title.setBounds(50, 25, 380, 30);
        add(title);

        JLabel accLabel = new JLabel("Account No:");
        accLabel.setFont(new Font("Arial", Font.PLAIN, 13));
        accLabel.setBounds(80, 90, 120, 25);
        add(accLabel);

        accountField = new JTextField();
        accountField.setBounds(220, 90, 180, 25);
        add(accountField);

        JLabel pinLabel = new JLabel("PIN:");
        pinLabel.setFont(new Font("Arial", Font.PLAIN, 13));
        pinLabel.setBounds(80, 135, 120, 25);
        add(pinLabel);

        pinField = new JPasswordField();
        pinField.setBounds(220, 135, 180, 25);
        add(pinField);

        JButton loginBtn = new JButton("Login");
        loginBtn.setBounds(50, 200, 120, 35);
        add(loginBtn);

        JButton createBtn = new JButton("Create Account");
        createBtn.setBounds(185, 200, 140, 35);
        add(createBtn);

        JButton adminBtn = new JButton("Admin Login");
        adminBtn.setBounds(340, 200, 120, 35);
        add(adminBtn);

        

        // Login
        loginBtn.addActionListener(e -> {
            String acc = accountField.getText().trim();
            String pin = new String(pinField.getPassword()).trim();
            if (acc.isEmpty() || pin.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please enter Account No and PIN.");
                return;
            }
            if (dao.login(acc, pin)) {
                dispose();
                new MainMenu(acc).setVisible(true);
            } else {
                JOptionPane.showMessageDialog(this,
                    "Invalid Account No or PIN!\nAccount may be deactivated.",
                    "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        // Create Account — opens full form
createBtn.addActionListener(e -> {
    dispose();
    new CreateAccountFrame().setVisible(true);
});

        // Admin Login — no icon, clean input dialogs
        adminBtn.addActionListener(e -> {
            JPanel panel = new JPanel(null);
            panel.setPreferredSize(new java.awt.Dimension(280, 90));

            JLabel userLbl = new JLabel("Username:");
            userLbl.setBounds(10, 10, 90, 25);
            panel.add(userLbl);

            JTextField userField = new JTextField();
            userField.setBounds(110, 10, 160, 25);
            panel.add(userField);

            JLabel passLbl = new JLabel("Password:");
            passLbl.setBounds(10, 50, 90, 25);
            panel.add(passLbl);

            JPasswordField passField = new JPasswordField();
            passField.setBounds(110, 50, 160, 25);
            panel.add(passField);

            int result = JOptionPane.showConfirmDialog(
                this,
                panel,
                "Admin Login",
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE  // ← PLAIN_MESSAGE removes the ? icon
            );

            if (result != JOptionPane.OK_OPTION) return;

            String user = userField.getText().trim();
            String pass = new String(passField.getPassword()).trim();

            if (user.isEmpty() || pass.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please enter username and password.");
                return;
            }

            if (dao.adminLogin(user, pass)) {
                dispose();
                new AdminDashboard().setVisible(true);
            } else {
                JOptionPane.showMessageDialog(this,
                    "Invalid admin credentials!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new LoginFrame().setVisible(true));
    }
}