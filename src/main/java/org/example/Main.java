package org.example;

import org.example.gui.MainFrame;

import javax.swing.*;

public class Main {

  public static void main(String[] args) {
    try {
      UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
    } catch (Exception e) {
      e.printStackTrace();
    }

    SwingUtilities.invokeLater(() -> {
      try {
        MainFrame mainFrame = new MainFrame();
        mainFrame.setVisible(true);
      } catch (Exception e) {
        e.printStackTrace();
        JOptionPane.showMessageDialog(null, "Erro ao inicializar a aplicação: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
      }
    });
  }
}
