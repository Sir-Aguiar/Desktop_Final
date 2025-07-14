package org.example.utils;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;

public class ModernUIUtils {

  // Paleta Material Design - cores principais
  public static final Color PRIMARY_COLOR = new Color(33, 150, 243);        // Material Blue
  public static final Color PRIMARY_HOVER = new Color(25, 118, 210);        // Material Blue Dark
  public static final Color SECONDARY_COLOR = new Color(96, 125, 139);      // Material Blue Grey
  public static final Color SECONDARY_HOVER = new Color(84, 110, 122);      // Material Blue Grey Dark

  // Cores de sistema
  public static final Color SUCCESS_COLOR = new Color(76, 175, 80);         // Material Green
  public static final Color ERROR_COLOR = new Color(244, 67, 54);           // Material Red

  // Tons de cinza Material
  public static final Color GRAY_50 = new Color(250, 250, 250);             // Fundo mais claro
  public static final Color GRAY_100 = new Color(245, 245, 245);            // Fundo claro
  public static final Color GRAY_300 = new Color(224, 224, 224);            // Bordas
  public static final Color GRAY_500 = new Color(158, 158, 158);            // Texto secundário
  public static final Color GRAY_700 = new Color(97, 97, 97);               // Texto principal
  public static final Color GRAY_900 = new Color(33, 33, 33);               // Texto escuro

  // Cores básicas
  public static final Color WHITE = Color.WHITE;
  public static final Color BLACK = new Color(33, 33, 33);                  // Preto Material

  // Fontes modernas
  public static final Font TITLE_FONT = new Font("Segoe UI", Font.BOLD, 28);
  public static final Font SUBTITLE_FONT = new Font("Segoe UI", Font.BOLD, 18);
  public static final Font BODY_FONT = new Font("Segoe UI", Font.PLAIN, 14);
  public static final Font SMALL_FONT = new Font("Segoe UI", Font.PLAIN, 12);
  public static final Font BUTTON_FONT = new Font("Segoe UI", Font.BOLD, 14);

  /**
   * Cria um botão moderno com bordas arredondadas e melhor contraste
   */
  public static JButton createModernButton(String text, Color backgroundColor, Color hoverColor, Color textColor) {
    JButton button = new JButton(text) {
      @Override
      protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Cor de fundo
        if (getModel().isPressed()) {
          g2.setColor(hoverColor.darker());
        } else if (getModel().isRollover()) {
          g2.setColor(hoverColor);
        } else {
          g2.setColor(backgroundColor);
        }

        // Desenhar retângulo arredondado
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), 4, 4);

        // Sombra sutil
        if (!getModel().isPressed()) {
          g2.setColor(new Color(0, 0, 0, 20));
          g2.fillRoundRect(0, 2, getWidth(), getHeight(), 4, 4);
        }

        // Texto
        g2.setColor(textColor);
        g2.setFont(BUTTON_FONT);
        FontMetrics fm = g2.getFontMetrics();
        int textWidth = fm.stringWidth(getText());
        int textHeight = fm.getHeight();
        int x = (getWidth() - textWidth) / 2;
        int y = (getHeight() - textHeight) / 2 + fm.getAscent();
        g2.drawString(getText(), x, y);

        g2.dispose();
      }
    };

    button.setPreferredSize(new Dimension(140, 40));
    button.setFocusPainted(false);
    button.setBorderPainted(false);
    button.setContentAreaFilled(false);
    button.setCursor(new Cursor(Cursor.HAND_CURSOR));

    return button;
  }

  /**
   * Cria um botão primário
   */
  public static JButton createPrimaryButton(String text) {
    return createModernButton(text, PRIMARY_COLOR, PRIMARY_HOVER, WHITE);
  }

  /**
   * Cria um botão de sucesso
   */
  public static JButton createSuccessButton(String text) {
    return createModernButton(text, SUCCESS_COLOR, SUCCESS_COLOR.darker(), WHITE);
  }

  /**
   * Cria um botão de erro
   */
  public static JButton createErrorButton(String text) {
    return createModernButton(text, ERROR_COLOR, ERROR_COLOR.darker(), WHITE);
  }

  /**
   * Cria um botão secundário
   */
  public static JButton createSecondaryButton(String text) {
    return createModernButton(text, SECONDARY_COLOR, SECONDARY_HOVER, WHITE);
  }

  /**
   * Cria um botão de texto (outline)
   */
  public static JButton createTextButton(String text) {
    JButton button = new JButton(text) {
      @Override
      protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Cor de fundo
        if (getModel().isPressed()) {
          g2.setColor(GRAY_100);
        } else if (getModel().isRollover()) {
          g2.setColor(GRAY_50);
        } else {
          g2.setColor(WHITE);
        }

        // Desenhar fundo
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), 4, 4);

        // Borda
        g2.setColor(GRAY_300);
        g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 4, 4);

        // Texto
        g2.setColor(GRAY_700);
        g2.setFont(BUTTON_FONT);
        FontMetrics fm = g2.getFontMetrics();
        int textWidth = fm.stringWidth(getText());
        int textHeight = fm.getHeight();
        int x = (getWidth() - textWidth) / 2;
        int y = (getHeight() - textHeight) / 2 + fm.getAscent();
        g2.drawString(getText(), x, y);

        g2.dispose();
      }
    };

    button.setPreferredSize(new Dimension(140, 40));
    button.setFocusPainted(false);
    button.setBorderPainted(false);
    button.setContentAreaFilled(false);
    button.setCursor(new Cursor(Cursor.HAND_CURSOR));

    return button;
  }

  /**
   * Cria um campo de texto moderno
   */
  public static JTextField createModernTextField(int columns) {
    JTextField textField = new JTextField(columns);
    textField.setFont(BODY_FONT);
    textField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(GRAY_300, 1),
            BorderFactory.createEmptyBorder(12, 16, 12, 16)
    ));
    textField.setBackground(WHITE);
    textField.setForeground(GRAY_900);

    // Adicionar efeito de foco
    textField.addFocusListener(new java.awt.event.FocusAdapter() {
      @Override
      public void focusGained(java.awt.event.FocusEvent evt) {
        textField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(PRIMARY_COLOR, 2),
                BorderFactory.createEmptyBorder(11, 15, 11, 15)
        ));
      }

      @Override
      public void focusLost(java.awt.event.FocusEvent evt) {
        textField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(GRAY_300, 1),
                BorderFactory.createEmptyBorder(12, 16, 12, 16)
        ));
      }
    });

    return textField;
  }

  /**
   * Cria um campo de senha moderno
   */
  public static JPasswordField createModernPasswordField(int columns) {
    JPasswordField passwordField = new JPasswordField(columns);
    passwordField.setFont(BODY_FONT);
    passwordField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(GRAY_300, 1),
            BorderFactory.createEmptyBorder(12, 16, 12, 16)
    ));
    passwordField.setBackground(WHITE);
    passwordField.setForeground(GRAY_900);

    // Adicionar efeito de foco
    passwordField.addFocusListener(new java.awt.event.FocusAdapter() {
      @Override
      public void focusGained(java.awt.event.FocusEvent evt) {
        passwordField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(PRIMARY_COLOR, 2),
                BorderFactory.createEmptyBorder(11, 15, 11, 15)
        ));
      }

      @Override
      public void focusLost(java.awt.event.FocusEvent evt) {
        passwordField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(GRAY_300, 1),
                BorderFactory.createEmptyBorder(12, 16, 12, 16)
        ));
      }
    });

    return passwordField;
  }

  /**
   * Cria uma área de texto moderna
   */
  public static JTextArea createModernTextArea(int rows, int columns) {
    JTextArea textArea = new JTextArea(rows, columns);
    textArea.setFont(BODY_FONT);
    textArea.setLineWrap(true);
    textArea.setWrapStyleWord(true);
    textArea.setBorder(BorderFactory.createEmptyBorder(12, 16, 12, 16));
    textArea.setBackground(WHITE);
    textArea.setForeground(GRAY_900);

    return textArea;
  }

  /**
   * Cria um painel com gradiente
   */
  public static JPanel createGradientPanel(Color startColor, Color endColor) {
    return new JPanel() {
      @Override
      protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        int w = getWidth();
        int h = getHeight();
        GradientPaint gp = new GradientPaint(0, 0, startColor, 0, h, endColor);
        g2d.setPaint(gp);
        g2d.fillRect(0, 0, w, h);
      }
    };
  }

  /**
   * Cria um rótulo moderno
   */
  public static JLabel createModernLabel(String text, Font font, Color color) {
    JLabel label = new JLabel(text);
    label.setFont(font);
    label.setForeground(color);
    return label;
  }

  /**
   * Cria uma borda moderna
   */
  public static Border createModernBorder() {
    return BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(GRAY_300, 1),
            BorderFactory.createEmptyBorder(16, 16, 16, 16)
    );
  }

  /**
   * Cria um painel com cantos arredondados
   */
  public static JPanel createRoundedPanel(Color backgroundColor, int radius) {
    return new JPanel() {
      @Override
      protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(backgroundColor);
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), radius, radius);
        g2.dispose();
      }
    };
  }
}
