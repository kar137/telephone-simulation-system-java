package main.gui;

import javax.swing.*;
import java.awt.*;
import javax.swing.border.EmptyBorder;

public class WelcomePage extends JFrame {
    public WelcomePage() {
        setTitle("Telephone Simulation System");
        setSize(600, 450);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));
        getContentPane().setBackground(new Color(240, 245, 255));

        // Load emoji-supporting font with fallback
        Font emojiFont = loadEmojiFont();
        Font titleFont = emojiFont.deriveFont(Font.BOLD, 24);
        Font infoFont = emojiFont.deriveFont(Font.PLAIN, 16);
        Font buttonFont = emojiFont.deriveFont(Font.BOLD, 14);

        // Header Panel with gradient
        JPanel headerPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                Color color1 = new Color(0, 102, 204);
                Color color2 = new Color(0, 153, 255);
                GradientPaint gp = new GradientPaint(0, 0, color1, getWidth(), 0, color2);
                g2d.setPaint(gp);
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        headerPanel.setLayout(new BorderLayout());
        headerPanel.setPreferredSize(new Dimension(getWidth(), 80));

        // Welcome Title
        JLabel welcomeLabel = new JLabel("📞 Welcome to Telephone Simulator", JLabel.CENTER);
        welcomeLabel.setFont(titleFont);
        welcomeLabel.setForeground(Color.WHITE);
        welcomeLabel.setBorder(new EmptyBorder(20, 0, 20, 0));
        headerPanel.add(welcomeLabel, BorderLayout.CENTER);
        add(headerPanel, BorderLayout.NORTH);

        // Main Content Panel
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBackground(new Color(240, 245, 255));
        contentPanel.setBorder(new EmptyBorder(20, 40, 20, 40));

        // Info Panel with card-like appearance
        JPanel infoCard = new JPanel();
        infoCard.setLayout(new BoxLayout(infoCard, BoxLayout.Y_AXIS));
        infoCard.setBackground(Color.WHITE);
        infoCard.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 220, 255), 1),
            new EmptyBorder(20, 30, 20, 30)
        ));
        infoCard.setMaximumSize(new Dimension(400, Integer.MAX_VALUE));
        infoCard.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel nameLabel = createInfoLabel("👤 Submitted By: Karan Bista", infoFont);
        JLabel deptLabel = createInfoLabel("🏛️ Department: Computer Science & Engineering", infoFont);
        JLabel crnLabel = createInfoLabel("📘 CRN: 022-328", infoFont);

        infoCard.add(nameLabel);
        infoCard.add(Box.createVerticalStrut(15));
        infoCard.add(deptLabel);
        infoCard.add(Box.createVerticalStrut(15));
        infoCard.add(crnLabel);

        contentPanel.add(infoCard);
        contentPanel.add(Box.createVerticalStrut(30));

        // Button Panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 30, 0));
        buttonPanel.setBackground(new Color(240, 245, 255));

        JButton lostCallBtn = createStyledButton("📉 Lost Call Model", buttonFont, new Color(255, 102, 102));
        JButton delayedCallBtn = createStyledButton("⏳ Delayed Call Model", buttonFont, new Color(102, 178, 255));

        buttonPanel.add(lostCallBtn);
        buttonPanel.add(delayedCallBtn);

        contentPanel.add(buttonPanel);
        add(contentPanel, BorderLayout.CENTER);

        // Action Listeners
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

    private Font loadEmojiFont() {
        String[] emojiFonts = {"Segoe UI Emoji", "Noto Color Emoji", "Apple Color Emoji"};
        for (String fontName : emojiFonts) {
            try {
                Font font = new Font(fontName, Font.PLAIN, 12);
                if (font.canDisplay('📞')) {
                    return font;
                }
            } catch (Exception e) {
                continue;
            }
        }
        return new Font("SansSerif", Font.PLAIN, 12);
    }

    private JLabel createInfoLabel(String text, Font font) {
        JLabel label = new JLabel(text);
        label.setFont(font);
        label.setAlignmentX(Component.CENTER_ALIGNMENT);
        label.setForeground(new Color(60, 60, 60));
        return label;
    }

    private JButton createStyledButton(String text, Font font, Color bgColor) {
        JButton button = new JButton(text);
        button.setFont(font);
        button.setBackground(bgColor);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(bgColor.darker(), 1),
            new EmptyBorder(10, 20, 10, 20)
        ));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(bgColor.brighter());
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(bgColor);
            }
        });
        
        return button;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new WelcomePage());
    }
}