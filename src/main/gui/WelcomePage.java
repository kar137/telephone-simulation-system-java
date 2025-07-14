package main.gui;

import javax.swing.*;
import java.awt.*;

public class WelcomePage extends JFrame {
    public WelcomePage() {
        setTitle("Telephone Simulation System");
        setSize(400, 300);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        JLabel welcomeLabel = new JLabel("📞 Welcome to the Telephone Simulator!", JLabel.CENTER);
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 18));
        add(welcomeLabel, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        JButton lostCallBtn = new JButton("📉 Lost Call Model");
        JButton delayedCallBtn = new JButton("⏳ Delayed Call Model");

        buttonPanel.add(lostCallBtn);
        buttonPanel.add(delayedCallBtn);
        add(buttonPanel, BorderLayout.SOUTH);

        lostCallBtn.addActionListener(e -> {
            new LostCallFrame().setVisible(true);
            dispose();
        });

        delayedCallBtn.addActionListener(e -> {
            new DelayedCallFrame().setVisible(true);
            dispose();
        });

        setVisible(true);
    }
}
