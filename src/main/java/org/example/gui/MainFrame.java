package org.example.gui;

import org.example.entities.User;
import org.example.utils.JSONManager;
import org.example.utils.ModernUIUtils;

import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {

  private User currentUser;
  private JSONManager jsonManager;

  public MainFrame() {
    this.jsonManager = new JSONManager();
    initializeComponents();
  }

  private void initializeComponents() {
    setTitle("Diário Digital - Tela Principal");
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setSize(1000, 700);
    setLocationRelativeTo(null);

    // Layout principal
    setLayout(new BorderLayout());

    // Painel superior com gradiente
    JPanel topPanel = ModernUIUtils.createGradientPanel(
            ModernUIUtils.PRIMARY_COLOR,
            ModernUIUtils.PRIMARY_HOVER
    );
    topPanel.setPreferredSize(new Dimension(1000, 70));
    topPanel.setLayout(new BorderLayout());

    // Lado esquerdo - título
    JPanel leftPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 18));
    leftPanel.setOpaque(false);
    JLabel welcomeLabel = ModernUIUtils.createModernLabel(
            "Diário Digital",
            ModernUIUtils.TITLE_FONT,
            ModernUIUtils.WHITE
    );
    leftPanel.add(welcomeLabel);

    // Lado direito - botões de usuário
    JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 15));
    rightPanel.setOpaque(false);

    JButton loginButton = ModernUIUtils.createTextButton("Login");
    JButton registerButton = ModernUIUtils.createPrimaryButton("Cadastrar");
    JButton logoutButton = ModernUIUtils.createSecondaryButton("Logout");

    loginButton.addActionListener(e -> openLoginWindow());
    registerButton.addActionListener(e -> openRegisterWindow());
    logoutButton.addActionListener(e -> logout());

    rightPanel.add(loginButton);
    rightPanel.add(registerButton);
    rightPanel.add(logoutButton);

    topPanel.add(leftPanel, BorderLayout.WEST);
    topPanel.add(rightPanel, BorderLayout.EAST);

    add(topPanel, BorderLayout.NORTH);

    // Painel central com menu principal
    JPanel centerPanel = new JPanel(new GridBagLayout());
    centerPanel.setBackground(ModernUIUtils.GRAY_50);

    GridBagConstraints gbc = new GridBagConstraints();
    gbc.insets = new Insets(30, 30, 30, 30);

    // Título principal
    JLabel titleLabel = ModernUIUtils.createModernLabel(
            "Bem-vindo ao seu Diário Digital",
            new Font("Segoe UI", Font.BOLD, 36),
            ModernUIUtils.PRIMARY_COLOR
    );
    gbc.gridx = 0;
    gbc.gridy = 0;
    gbc.gridwidth = 2;
    centerPanel.add(titleLabel, gbc);

    // Subtítulo
    JLabel subtitleLabel = ModernUIUtils.createModernLabel(
            "Organize seus pensamentos e memórias de forma elegante",
            ModernUIUtils.SUBTITLE_FONT,
            ModernUIUtils.GRAY_500
    );
    gbc.gridy = 1;
    gbc.insets = new Insets(0, 30, 40, 30);
    centerPanel.add(subtitleLabel, gbc);

    // Botões do menu com novo estilo
    gbc.insets = new Insets(15, 15, 15, 15);
    gbc.gridwidth = 1;

    JButton newEntryButton = createModernMenuButton("Nova Entrada", "Criar uma nova entrada no diário", ModernUIUtils.SUCCESS_COLOR);
    JButton viewEntriesButton = createModernMenuButton("Visualizar Entradas", "Ver todas as suas entradas", ModernUIUtils.PRIMARY_COLOR);
    JButton editEntryButton = createModernMenuButton("Editar Entrada", "Editar uma entrada existente", ModernUIUtils.SECONDARY_COLOR);
    JButton reportsButton = createModernMenuButton("Relatórios", "Gerar relatórios das suas entradas", ModernUIUtils.ERROR_COLOR);

    newEntryButton.addActionListener(e -> openNewEntryWindow());
    viewEntriesButton.addActionListener(e -> openViewEntriesWindow());
    editEntryButton.addActionListener(e -> openEditEntryWindow());
    reportsButton.addActionListener(e -> openReportsWindow());

    gbc.gridy = 2;
    gbc.gridx = 0;
    centerPanel.add(newEntryButton, gbc);

    gbc.gridx = 1;
    centerPanel.add(viewEntriesButton, gbc);

    gbc.gridy = 3;
    gbc.gridx = 0;
    centerPanel.add(editEntryButton, gbc);

    gbc.gridx = 1;
    centerPanel.add(reportsButton, gbc);

    add(centerPanel, BorderLayout.CENTER);

    // Painel inferior com informações
    JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
    bottomPanel.setBackground(ModernUIUtils.GRAY_700);
    bottomPanel.setPreferredSize(new Dimension(1000, 40));

    JLabel infoLabel = ModernUIUtils.createModernLabel(
            "© 2025 Diário Digital - Versão 2.0 | Feito com ❤️ em Java",
            ModernUIUtils.SMALL_FONT,
            ModernUIUtils.WHITE
    );
    bottomPanel.add(infoLabel);

    add(bottomPanel, BorderLayout.SOUTH);

    updateUIForUserState();
  }

  private JButton createModernMenuButton(String text, String tooltip, Color color) {
    JButton button = new JButton(text) {
      @Override
      protected void paintComponent(java.awt.Graphics g) {
        java.awt.Graphics2D g2 = (java.awt.Graphics2D) g.create();
        g2.setRenderingHint(java.awt.RenderingHints.KEY_ANTIALIASING, java.awt.RenderingHints.VALUE_ANTIALIAS_ON);

        // Cor de fundo
        if (getModel().isPressed()) {
          g2.setColor(color.darker());
        } else if (getModel().isRollover()) {
          g2.setColor(color.brighter());
        } else {
          g2.setColor(color);
        }

        // Desenhar retângulo arredondado
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);

        // Sombra
        g2.setColor(new Color(0, 0, 0, 30));
        g2.fillRoundRect(2, 2, getWidth() - 2, getHeight() - 2, 20, 20);

        // Texto
        g2.setColor(Color.WHITE);
        g2.setFont(new Font("Segoe UI", Font.BOLD, 16));
        java.awt.FontMetrics fm = g2.getFontMetrics();
        int textWidth = fm.stringWidth(getText());
        int textHeight = fm.getHeight();
        int x = (getWidth() - textWidth) / 2;
        int y = (getHeight() - textHeight) / 2 + fm.getAscent();
        g2.drawString(getText(), x, y);

        g2.dispose();
      }
    };

    button.setPreferredSize(new Dimension(280, 80));
    button.setFocusPainted(false);
    button.setBorderPainted(false);
    button.setContentAreaFilled(false);
    button.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
    button.setToolTipText(tooltip);

    return button;
  }

  private JButton createMenuButton(String text, String tooltip) {
    JButton button = new JButton(text);
    button.setPreferredSize(new Dimension(200, 60));
    button.setFont(new Font("Arial", Font.BOLD, 14));
    button.setBackground(new Color(70, 130, 180));
    button.setForeground(Color.WHITE);
    button.setFocusPainted(false);
    button.setBorder(BorderFactory.createRaisedBevelBorder());
    button.setToolTipText(tooltip);

    button.addMouseListener(new java.awt.event.MouseAdapter() {
      public void mouseEntered(java.awt.event.MouseEvent evt) {
        button.setBackground(new Color(100, 149, 237));
      }

      public void mouseExited(java.awt.event.MouseEvent evt) {
        button.setBackground(new Color(70, 130, 180));
      }
    });

    return button;
  }

  private void openLoginWindow() {
    LoginFrame loginFrame = new LoginFrame(this, jsonManager);
    loginFrame.setVisible(true);
  }

  private void openRegisterWindow() {
    RegisterFrame registerFrame = new RegisterFrame(this, jsonManager);
    registerFrame.setVisible(true);
  }

  private void openNewEntryWindow() {
    if (currentUser == null) {
      JOptionPane.showMessageDialog(this, "Por favor, faça login primeiro!", "Aviso", JOptionPane.WARNING_MESSAGE);
      return;
    }
    NewEntryFrame newEntryFrame = new NewEntryFrame(this, currentUser, jsonManager);
    newEntryFrame.setVisible(true);
  }

  private void openViewEntriesWindow() {
    if (currentUser == null) {
      JOptionPane.showMessageDialog(this, "Por favor, faça login primeiro!", "Aviso", JOptionPane.WARNING_MESSAGE);
      return;
    }
    ViewEntriesFrame viewEntriesFrame = new ViewEntriesFrame(this, currentUser, jsonManager);
    viewEntriesFrame.setVisible(true);
  }

  private void openEditEntryWindow() {
    if (currentUser == null) {
      JOptionPane.showMessageDialog(this, "Por favor, faça login primeiro!", "Aviso", JOptionPane.WARNING_MESSAGE);
      return;
    }
    EditEntryFrame editEntryFrame = new EditEntryFrame(this, currentUser, jsonManager);
    editEntryFrame.setVisible(true);
  }

  private void openReportsWindow() {
    if (currentUser == null) {
      JOptionPane.showMessageDialog(this, "Por favor, faça login primeiro!", "Aviso", JOptionPane.WARNING_MESSAGE);
      return;
    }
    ReportsFrame reportsFrame = new ReportsFrame(this, currentUser, jsonManager);
    reportsFrame.setVisible(true);
  }

  private void logout() {
    currentUser = null;
    updateUIForUserState();
    JOptionPane.showMessageDialog(this, "Logout realizado com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
  }

  public void setCurrentUser(User user) {
    this.currentUser = user;
    updateUIForUserState();
  }

  private void updateUIForUserState() {
    // Atualizar interface baseado no estado do usuário
    Component[] components = ((JPanel) getContentPane().getComponent(0)).getComponents();
    for (Component component : components) {
      if (component instanceof JLabel) {
        JLabel label = (JLabel) component;
        if (currentUser != null) {
          label.setText("Bem-vindo, " + currentUser.getName() + "!");
        } else {
          label.setText("Bem-vindo ao Diário Digital!");
        }
        break;
      }
    }
  }
}
