package bank;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import java.awt.Color;
import java.awt.Font;

public class TransferFrame extends JFrame {

    private JTextField toAccountField;
    private JTextField amountField;
    private AccountDAO dao = new AccountDAO();
    private String accountNo;

    public TransferFrame(String accountNo) {
        this.accountNo = accountNo;

        // Window Settings
        setTitle("Transfer Money");
        setSize(450, 300);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(null);
        getContentPane().setBackground(new Color(220, 220, 220));

        // To Account Label
        JLabel toLabel = new JLabel("To Account:");
        toLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        toLabel.setBounds(60, 80, 130, 25);
        add(toLabel);

        // To Account Field
        toAccountField = new JTextField();
        toAccountField.setBounds(220, 80, 150, 25);
        add(toAccountField);

        // Amount Label
        JLabel amtLabel = new JLabel("Amount:");
        amtLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        amtLabel.setBounds(60, 130, 130, 25);
        add(amtLabel);

        // Amount Field
        amountField = new JTextField();
        amountField.setBounds(220, 130, 150, 25);
        add(amountField);

        // Transfer Button
        JButton transferBtn = new JButton("Transfer");
        transferBtn.setBounds(80, 200, 120, 35);
        add(transferBtn);

        // Back Button
        JButton backBtn = new JButton("Back");
        backBtn.setBounds(240, 200, 120, 35);
        add(backBtn);

        // Transfer Button Action
        transferBtn.addActionListener(e -> {
            String toAcc = toAccountField.getText().trim();
            String amtText = amountField.getText().trim();

            // Check empty fields
            if (toAcc.isEmpty() || amtText.isEmpty()) {
                JOptionPane.showMessageDialog(this,
                    "Please fill all fields.",
                    "Warning",
                    JOptionPane.WARNING_MESSAGE);
                return;
            }

            // Check same account
            if (toAcc.equals(accountNo)) {
                JOptionPane.showMessageDialog(this,
                    "Cannot transfer to your own account.",
                    "Warning",
                    JOptionPane.WARNING_MESSAGE);
                return;
            }

            // Check receiver exists
            if (!dao.accountExists(toAcc)) {
                JOptionPane.showMessageDialog(this,
                    "Receiver account does not exist!",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
                return;
            }

            try {
                double amount = Double.parseDouble(amtText);

                // Check valid amount
                if (amount <= 0) {
                    JOptionPane.showMessageDialog(this,
                        "Enter a valid amount.",
                        "Warning",
                        JOptionPane.WARNING_MESSAGE);
                    return;
                }

                // Do transfer
                if (dao.transfer(accountNo, toAcc, amount)) {
                    JOptionPane.showMessageDialog(this,
                        "Transferred Rs." + String.format("%.2f", amount) +
                        " to Account " + toAcc + " successfully!",
                        "Success",
                        JOptionPane.INFORMATION_MESSAGE);
                    toAccountField.setText("");
                    amountField.setText("");
                } else {
                    JOptionPane.showMessageDialog(this,
                        "Transfer failed! Check your balance.",
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this,
                    "Please enter a valid number.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            }
        });

        // Back Button Action
        backBtn.addActionListener(e -> {
            dispose();
            new MainMenu(accountNo).setVisible(true);
        });
    }
}