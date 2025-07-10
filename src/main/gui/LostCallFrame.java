package main.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import model.*;

public class LostCallFrame extends JFrame {

    private JLabel clockLabel;
    private int currentTime = 0;
    private Timer simulationTimer;
    
    private Switchboard switchboard;
    private ArrayList<Call> activeCalls = new ArrayList<>();

    public LostCallFrame() {
        setTitle("📉 Lost Call Simulation");
        setSize(800, 500);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        switchboard = new Switchboard(8, 3); // 8 lines, 3 switch links

        // Top clock
        clockLabel = new JLabel("⏰ Time: 0", JLabel.CENTER);
        clockLabel.setFont(new Font("Courier New", Font.BOLD, 24));
        add(clockLabel, BorderLayout.NORTH);

        // Main simulation panel (just a placeholder for now)
        JTextArea simulationLog = new JTextArea();
        simulationLog.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(simulationLog);
        add(scrollPane, BorderLayout.CENTER);

        // Bottom control
        JPanel bottomPanel = new JPanel();
        JButton stopBtn = new JButton("🛑 Stop Simulation");
        bottomPanel.add(stopBtn);
        add(bottomPanel, BorderLayout.SOUTH);

        // Timer for automatic clock
        simulationTimer = new Timer(1000, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                currentTime++;
                clockLabel.setText("⏰ Time: " + currentTime);

                // Simulate new call generation and connection attempt
                Call call = Call.generateRandomCall(currentTime, 5); // max 5s duration
                boolean success = switchboard.connectCall(call);

                if (success) {
                    simulationLog.append("✅ Connected: " + call + "\n");
                    activeCalls.add(call);
                } else {
                    simulationLog.append("❌ Rejected:  " + call + "\n");
                }

                // Disconnect completed calls
                ArrayList<Call> toRemove = new ArrayList<>();
                for (Call c : activeCalls) {
                    if (currentTime - c.getStartTime() >= c.getDuration()) {
                        switchboard.disconnectCall(c);
                        simulationLog.append("🔌 Disconnected: " + c + "\n");
                        toRemove.add(c);
                    }
                }
                activeCalls.removeAll(toRemove);
            }
        });

        stopBtn.addActionListener(e -> simulationTimer.stop());

        simulationTimer.start();
        setVisible(true);
    }
}

