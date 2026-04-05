package bank;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import java.awt.Color;
import java.awt.Font;

public class WithdrawFrame extends JFrame {

    private JTextField amountField;
    private AccountDAO dao = new AccountDAO();
    private String accountNo;

    public WithdrawFrame(String accountNo) {
        this.accountNo = accountNo;
        setTitle("Withdraw");
        setSize(450, 280);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(null);
        getContentPane().setBackground(new Color(220, 220, 220));

        JLabel label = new JLabel("Enter Amount:");
        label.setFont(new Font("Arial", Font.PLAIN, 14));
        label.setBounds(80, 100, 150, 25);
        add(label);

        amountField = new JTextField();
        amountField.setBounds(240, 100, 130, 25);
        add(amountField);

        JButton submitBtn = new JButton("Submit");
        submitBtn.setBounds(80, 170, 120, 35);
        add(submitBtn);

        JButton backBtn = new JButton("Back");
        backBtn.setBounds(240, 170, 120, 35);
        add(backBtn);

        submitBtn.addActionListener(e -> {
            try {
                double amount = Double.parseDouble(amountField.getText().trim());
                if (amount <= 0) {
                    JOptionPane.showMessageDialog(this, "Enter a valid amount.");
                    return;
                }
                if (dao.withdraw(accountNo, amount)) {
                    JOptionPane.showMessageDialog(this,
                        "Withdrawn Rs." + String.format("%.2f", amount) + " successfully!");
                    amountField.setText("");
                } else {
                    JOptionPane.showMessageDialog(this,
                        "Insufficient balance or error!", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Please enter a valid number.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        backBtn.addActionListener(e -> {
            dispose();
            new MainMenu(accountNo).setVisible(true);
        });
    }
}