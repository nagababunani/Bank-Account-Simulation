package bank;

import javax.swing.*;
import java.awt.*;

public class CreateAccountFrame extends JFrame {

    private JTextField accField;
    private JTextField nameField;
    private JTextField emailField;
    private JTextField phoneField;
    private JTextField depositField;
    private JPasswordField pinField;
    private JPasswordField confirmPinField;
    private AccountDAO dao = new AccountDAO();

    public CreateAccountFrame() {
        setTitle("Create New Account");
        setSize(450, 470);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(null);
        getContentPane().setBackground(new Color(220, 220, 220));

        // Title
        JLabel title = new JLabel("CREATE NEW ACCOUNT", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 16));
        title.setBounds(0, 12, 450, 28);
        add(title);

        JSeparator sep = new JSeparator();
        sep.setBounds(20, 44, 400, 2);
        add(sep);

        JLabel minNote = new JLabel("* Minimum opening deposit of Rs.1000 is required");
        minNote.setFont(new Font("Arial", Font.ITALIC, 10));
        minNote.setForeground(Color.RED);
        minNote.setBounds(50, 48, 380, 18);
        add(minNote);

        // Labels
        String[] labelNames = {
            "Account No    :",
            "Full Name      :",
            "Email             :",
            "Phone            :",
            "Initial Deposit :",
            "PIN (4 digit)   :",
            "Confirm PIN   :"
        };

        int y = 70;
        for (String labelName : labelNames) {
            JLabel lbl = new JLabel(labelName);
            lbl.setFont(new Font("Arial", Font.PLAIN, 13));
            lbl.setBounds(35, y, 140, 25);
            add(lbl);
            y += 44;
        }

        // Fields
        accField = new JTextField();
        accField.setBounds(185, 70, 200, 27);
        add(accField);

        nameField = new JTextField();
        nameField.setBounds(185, 114, 200, 27);
        add(nameField);

        emailField = new JTextField();
        emailField.setBounds(185, 158, 200, 27);
        add(emailField);

        phoneField = new JTextField();
        phoneField.setBounds(185, 202, 200, 27);
        add(phoneField);

        // Initial deposit field with note
        depositField = new JTextField();
        depositField.setBounds(185, 246, 130, 27);
        add(depositField);

        JLabel minLabel = new JLabel("Min: Rs.1000");
        minLabel.setFont(new Font("Arial", Font.ITALIC, 11));
        minLabel.setForeground(new Color(150, 0, 0));
        minLabel.setBounds(322, 250, 100, 20);
        add(minLabel);

        pinField = new JPasswordField();
        pinField.setBounds(185, 290, 200, 27);
        add(pinField);

        confirmPinField = new JPasswordField();
        confirmPinField.setBounds(185, 334, 200, 27);
        add(confirmPinField);

        // Create Button
        JButton createBtn = new JButton("Create Account");
        createBtn.setBounds(75, 385, 155, 36);
        createBtn.setFont(new Font("Arial", Font.PLAIN, 13));
        add(createBtn);

        // Cancel Button
        JButton cancelBtn = new JButton("Cancel");
        cancelBtn.setBounds(245, 385, 120, 36);
        cancelBtn.setFont(new Font("Arial", Font.PLAIN, 13));
        add(cancelBtn);

        createBtn.addActionListener(e -> handleCreate());

        cancelBtn.addActionListener(e -> {
            dispose();
            new LoginFrame().setVisible(true);
        });
    }

    private void handleCreate() {
        String acc        = accField.getText().trim();
        String name       = nameField.getText().trim();
        String email      = emailField.getText().trim();
        String phone      = phoneField.getText().trim();
        String depositStr = depositField.getText().trim();
        String pin        = new String(pinField.getPassword()).trim();
        String confirmPin = new String(confirmPinField.getPassword()).trim();

        // Check Account No
        if (acc.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "Account No is required!", "Warning", JOptionPane.WARNING_MESSAGE);
            accField.requestFocus();
            return;
        }

        // Check Full Name
        if (name.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "Full Name is required!", "Warning", JOptionPane.WARNING_MESSAGE);
            nameField.requestFocus();
            return;
        }

        // Check Initial Deposit
        if (depositStr.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "Initial deposit amount is required!", "Warning", JOptionPane.WARNING_MESSAGE);
            depositField.requestFocus();
            return;
        }

        // Validate deposit amount
        double depositAmount;
        try {
            depositAmount = Double.parseDouble(depositStr);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this,
                "Enter a valid deposit amount!", "Error", JOptionPane.ERROR_MESSAGE);
            depositField.requestFocus();
            return;
        }

        // Check minimum deposit
        if (depositAmount < 1000) {
            JOptionPane.showMessageDialog(this,
                "Minimum opening deposit is Rs.1000!\n" +
                "You entered: Rs." + String.format("%.2f", depositAmount),
                "Error", JOptionPane.ERROR_MESSAGE);
            depositField.requestFocus();
            return;
        }

        // Check PIN empty
        if (pin.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "PIN is required!", "Warning", JOptionPane.WARNING_MESSAGE);
            pinField.requestFocus();
            return;
        }

        // Check PIN length
        if (pin.length() != 4) {
            JOptionPane.showMessageDialog(this,
                "PIN must be exactly 4 digits!", "Warning", JOptionPane.WARNING_MESSAGE);
            pinField.requestFocus();
            return;
        }

        // Check PIN is numeric
        if (!pin.matches("[0-9]+")) {
            JOptionPane.showMessageDialog(this,
                "PIN must contain digits only!", "Warning", JOptionPane.WARNING_MESSAGE);
            pinField.requestFocus();
            return;
        }

        // Check PIN match
        if (!pin.equals(confirmPin)) {
            JOptionPane.showMessageDialog(this,
                "PINs do not match!", "Error", JOptionPane.ERROR_MESSAGE);
            confirmPinField.requestFocus();
            return;
        }

        // Check account already exists
        if (dao.accountExists(acc)) {
            JOptionPane.showMessageDialog(this,
                "Account No " + acc + " already exists!",
                "Error", JOptionPane.ERROR_MESSAGE);
            accField.requestFocus();
            return;
        }

        // Create account in database
        if (dao.createAccount(acc, pin, name, email, phone)) {

            // Deposit the initial amount
            dao.deposit(acc, depositAmount);

            JOptionPane.showMessageDialog(this,
                "Account created successfully!\n\n" +
                "Account No    : " + acc + "\n" +
                "Name              : " + name + "\n" +
                "Opening Balance: Rs." + String.format("%.2f", depositAmount) + "\n\n" +
                "You can now login with your Account No and PIN.",
                "Account Created",
                JOptionPane.INFORMATION_MESSAGE);

            dispose();
            new LoginFrame().setVisible(true);

        } else {
            JOptionPane.showMessageDialog(this,
                "Failed to create account! Try again.",
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}