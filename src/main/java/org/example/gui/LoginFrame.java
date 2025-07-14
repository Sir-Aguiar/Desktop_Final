package org.example.gui;

import org.example.entities.User;
import org.example.utils.JSONManager;
import org.example.utils.ModernUIUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LoginFrame extends JDialog {

  private MainFrame parent;
  private JSONManager jsonManager;
  private JTextField loginField;
  private JPasswordField passwordField;

  public LoginFrame(MainFrame parent, JSONManager jsonManager) {
    super(parent, "Login", true);
    this.parent = parent;
    this.jsonManager = jsonManager;
    initializeComponents();
  }

  private void initializeComponents() {
    setSize(450, 350);
    setLocationRelativeTo(parent);
    setResizable(false);

    // Layout principal
    setLayout(new BorderLayout());

    // Painel superior com gradiente
    JPanel topPanel = ModernUIUtils.createGradientPanel(
            ModernUIUtils.PRIMARY_COLOR,
            ModernUIUtils.PRIMARY_HOVER
    );
    topPanel.setPreferredSize(new Dimension(450, 80));
    topPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 25));

    JLabel titleLabel = ModernUIUtils.createModernLabel(
            "Login do Usuário",
            ModernUIUtils.TITLE_FONT,
            ModernUIUtils.WHITE
    );
    topPanel.add(titleLabel);

    add(topPanel, BorderLayout.NORTH);

    // Painel central com formulário
    JPanel centerPanel = new JPanel(new GridBagLayout());
    centerPanel.setBackground(ModernUIUtils.GRAY_50);
    centerPanel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));

    GridBagConstraints gbc = new GridBagConstraints();
    gbc.insets = new Insets(15, 15, 15, 15);

    // Campo login
    gbc.gridx = 0;
    gbc.gridy = 0;
    gbc.anchor = GridBagConstraints.WEST;
    JLabel loginLabel = ModernUIUtils.createModernLabel(
            "Login:",
            ModernUIUtils.SUBTITLE_FONT,
            ModernUIUtils.GRAY_700
    );
    centerPanel.add(loginLabel, gbc);

    gbc.gridx = 1;
    gbc.fill = GridBagConstraints.HORIZONTAL;
    gbc.weightx = 1.0;
    loginField = ModernUIUtils.createModernTextField(20);
    centerPanel.add(loginField, gbc);

    // Campo senha
    gbc.gridx = 0;
    gbc.gridy = 1;
    gbc.fill = GridBagConstraints.NONE;
    gbc.weightx = 0;
    JLabel passwordLabel = ModernUIUtils.createModernLabel(
            "Senha:",
            ModernUIUtils.SUBTITLE_FONT,
            ModernUIUtils.GRAY_700
    );
    centerPanel.add(passwordLabel, gbc);

    gbc.gridx = 1;
    gbc.fill = GridBagConstraints.HORIZONTAL;
    gbc.weightx = 1.0;
    passwordField = ModernUIUtils.createModernPasswordField(20);
    centerPanel.add(passwordField, gbc);

    add(centerPanel, BorderLayout.CENTER);

    // Painel inferior com botões
    JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 20));
    bottomPanel.setBackground(ModernUIUtils.WHITE);
    bottomPanel.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, ModernUIUtils.GRAY_300));

    JButton loginButton = ModernUIUtils.createPrimaryButton("Entrar");
    JButton cancelButton = ModernUIUtils.createTextButton("Cancelar");

    loginButton.setPreferredSize(new Dimension(120, 45));
    cancelButton.setPreferredSize(new Dimension(120, 45));

    loginButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        performLogin();
      }
    });

    cancelButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        dispose();
      }
    });

    bottomPanel.add(loginButton);
    bottomPanel.add(cancelButton);

    add(bottomPanel, BorderLayout.SOUTH);

    // Configurar Enter para fazer login
    getRootPane().setDefaultButton(loginButton);

    // Focar no campo login
    SwingUtilities.invokeLater(() -> loginField.requestFocus());
  }

  private void performLogin() {
    String login = loginField.getText().trim();
    String password = new String(passwordField.getPassword()).trim();

    // Validações
    if (login.isEmpty()) {
      JOptionPane.showMessageDialog(this, "Por favor, informe o login!", "Erro", JOptionPane.ERROR_MESSAGE);
      loginField.requestFocus();
      return;
    }

    if (password.isEmpty()) {
      JOptionPane.showMessageDialog(this, "Por favor, informe a senha!", "Erro", JOptionPane.ERROR_MESSAGE);
      passwordField.requestFocus();
      return;
    }

    if (login.length() < 3) {
      JOptionPane.showMessageDialog(this, "Login deve ter pelo menos 3 caracteres!", "Erro", JOptionPane.ERROR_MESSAGE);
      loginField.requestFocus();
      return;
    }

    if (password.length() < 4) {
      JOptionPane.showMessageDialog(this, "Senha deve ter pelo menos 4 caracteres!", "Erro", JOptionPane.ERROR_MESSAGE);
      passwordField.requestFocus();
      return;
    }

    // Autenticar usuário
    if (jsonManager.authenticateUser(login, password)) {
      User user = jsonManager.findUserByLogin(login);
      parent.setCurrentUser(user);
      JOptionPane.showMessageDialog(this,
              "Login realizado com sucesso!\n\n"
                      + "Bem-vindo, " + user.getName() + "!",
              "Sucesso",
              JOptionPane.INFORMATION_MESSAGE);
      dispose();
    } else {
      JOptionPane.showMessageDialog(this,
              "Login ou senha incorretos!\n\n"
                      + "Verifique suas credenciais e tente novamente.",
              "Erro",
              JOptionPane.ERROR_MESSAGE);
      passwordField.setText("");
      passwordField.requestFocus();
    }
  }
}
