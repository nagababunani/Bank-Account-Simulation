package bank;

import javax.swing.*;

public class HistoryFrame extends JFrame {

    AccountDAO dao = new AccountDAO();

    public HistoryFrame(String accountNo) {
        setTitle("Transaction History");
        setSize(500, 400);
        setLocationRelativeTo(null);
        setLayout(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        JTextArea area = new JTextArea();
        area.setEditable(false);
        area.setText(dao.getHistory(accountNo));

        JScrollPane scroll = new JScrollPane(area);
        scroll.setBounds(20, 20, 440, 300);
        add(scroll);

        JButton back = new JButton("Back");
        back.setBounds(180, 330, 120, 35);
        add(back);

        back.addActionListener(e -> dispose());
    }
}