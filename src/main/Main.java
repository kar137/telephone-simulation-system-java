package main;

import main.gui.WelcomePage;
import javax.swing.SwingUtilities;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new WelcomePage());
    }
}
