package fr.eni.papeterie.ihm;

import javax.swing.*;
import java.sql.SQLException;

public class SwingRun {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                Ecran frame = null;
                try {
                    frame = new Ecran();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }

                frame.setVisible(true);
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            }
        });
    }
}
