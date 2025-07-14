package org.example.gui;

import org.example.entities.User;
import org.example.utils.JSONManager;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.regex.Pattern;

public class RegisterFrame extends JDialog {

  private final MainFrame parent;
  private final JSONManager jsonManager;
  private JTextField loginField;
  private JPasswordField passwordField;
  private JPasswordField confirmPasswordField;
  private JTextField emailField;
  private JTextField nameField;

  public RegisterFrame(MainFrame parent, JSONManager jsonManager) {
    super(parent, "Cadastro de Usuário", true);
    this.parent = parent;
    this.jsonManager = jsonManager;
    initializeComponents();
  }

  private void initializeComponents() {
    setSize(450, 400);
    setLocationRelativeTo(parent);
    setResizable(false);

    // Layout principal
    setLayout(new BorderLayout());

    // Painel superior
    JPanel topPanel = new JPanel(new FlowLayout());
    topPanel.setBackground(new Color(70, 130, 180));

    JLabel titleLabel = new JLabel("Cadastro de Usuário");
    titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
    titleLabel.setForeground(Color.WHITE);
    topPanel.add(titleLabel);

    add(topPanel, BorderLayout.NORTH);

    // Painel central com formulário
    JPanel centerPanel = new JPanel(new GridBagLayout());
    centerPanel.setBackground(Color.WHITE);
    centerPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

    GridBagConstraints gbc = new GridBagConstraints();
    gbc.insets = new Insets(10, 10, 10, 10);

    // Campo nome
    gbc.gridx = 0;
    gbc.gridy = 0;
    gbc.anchor = GridBagConstraints.WEST;
    centerPanel.add(new JLabel("Nome Completo:"), gbc);

    gbc.gridx = 1;
    gbc.fill = GridBagConstraints.HORIZONTAL;
    gbc.weightx = 1.0;
    nameField = new JTextField(20);
    centerPanel.add(nameField, gbc);

    // Campo login
    gbc.gridx = 0;
    gbc.gridy = 1;
    gbc.fill = GridBagConstraints.NONE;
    gbc.weightx = 0;
    centerPanel.add(new JLabel("Login:"), gbc);

    gbc.gridx = 1;
    gbc.fill = GridBagConstraints.HORIZONTAL;
    gbc.weightx = 1.0;
    loginField = new JTextField(20);
    centerPanel.add(loginField, gbc);

    // Campo email
    gbc.gridx = 0;
    gbc.gridy = 2;
    gbc.fill = GridBagConstraints.NONE;
    gbc.weightx = 0;
    centerPanel.add(new JLabel("Email:"), gbc);

    gbc.gridx = 1;
    gbc.fill = GridBagConstraints.HORIZONTAL;
    gbc.weightx = 1.0;
    emailField = new JTextField(20);
    centerPanel.add(emailField, gbc);

    // Campo senha
    gbc.gridx = 0;
    gbc.gridy = 3;
    gbc.fill = GridBagConstraints.NONE;
    gbc.weightx = 0;
    centerPanel.add(new JLabel("Senha:"), gbc);

    gbc.gridx = 1;
    gbc.fill = GridBagConstraints.HORIZONTAL;
    gbc.weightx = 1.0;
    passwordField = new JPasswordField(20);
    centerPanel.add(passwordField, gbc);

    // Campo confirmar senha
    gbc.gridx = 0;
    gbc.gridy = 4;
    gbc.fill = GridBagConstraints.NONE;
    gbc.weightx = 0;
    centerPanel.add(new JLabel("Confirmar Senha:"), gbc);

    gbc.gridx = 1;
    gbc.fill = GridBagConstraints.HORIZONTAL;
    gbc.weightx = 1.0;
    confirmPasswordField = new JPasswordField(20);
    centerPanel.add(confirmPasswordField, gbc);

    add(centerPanel, BorderLayout.CENTER);

    // Painel inferior com botões
    JPanel bottomPanel = new JPanel(new FlowLayout());
    bottomPanel.setBackground(Color.WHITE);

    JButton registerButton = new JButton("Cadastrar");
    JButton cancelButton = new JButton("Cancelar");

    registerButton.setBackground(new Color(70, 130, 180));
    registerButton.setForeground(Color.WHITE);
    registerButton.setFocusPainted(false);

    cancelButton.setBackground(new Color(220, 220, 220));
    cancelButton.setFocusPainted(false);

    registerButton.addActionListener(e -> performRegister());
    cancelButton.addActionListener(e -> dispose());

    bottomPanel.add(registerButton);
    bottomPanel.add(cancelButton);

    add(bottomPanel, BorderLayout.SOUTH);

    // Configurar Enter para cadastrar
    getRootPane().setDefaultButton(registerButton);
  }

  private void performRegister() {
    String name = nameField.getText().trim();
    String login = loginField.getText().trim();
    String email = emailField.getText().trim();
    String password = new String(passwordField.getPassword()).trim();
    String confirmPassword = new String(confirmPasswordField.getPassword()).trim();

    // Validações
    if (name.isEmpty()) {
      JOptionPane.showMessageDialog(this, "Por favor, informe o nome completo!", "Erro", JOptionPane.ERROR_MESSAGE);
      nameField.requestFocus();
      return;
    }

    if (name.length() < 3) {
      JOptionPane.showMessageDialog(this, "Nome deve ter pelo menos 3 caracteres!", "Erro", JOptionPane.ERROR_MESSAGE);
      nameField.requestFocus();
      return;
    }

    if (login.isEmpty()) {
      JOptionPane.showMessageDialog(this, "Por favor, informe o login!", "Erro", JOptionPane.ERROR_MESSAGE);
      loginField.requestFocus();
      return;
    }

    if (login.length() < 3) {
      JOptionPane.showMessageDialog(this, "Login deve ter pelo menos 3 caracteres!", "Erro", JOptionPane.ERROR_MESSAGE);
      loginField.requestFocus();
      return;
    }

    if (login.length() > 20) {
      JOptionPane.showMessageDialog(this, "Login deve ter no máximo 20 caracteres!", "Erro", JOptionPane.ERROR_MESSAGE);
      loginField.requestFocus();
      return;
    }

    if (email.isEmpty()) {
      JOptionPane.showMessageDialog(this, "Por favor, informe o email!", "Erro", JOptionPane.ERROR_MESSAGE);
      emailField.requestFocus();
      return;
    }

    if (!isValidEmail(email)) {
      JOptionPane.showMessageDialog(this, "Por favor, informe um email válido!", "Erro", JOptionPane.ERROR_MESSAGE);
      emailField.requestFocus();
      return;
    }

    if (password.isEmpty()) {
      JOptionPane.showMessageDialog(this, "Por favor, informe a senha!", "Erro", JOptionPane.ERROR_MESSAGE);
      passwordField.requestFocus();
      return;
    }

    if (password.length() < 4) {
      JOptionPane.showMessageDialog(this, "Senha deve ter pelo menos 4 caracteres!", "Erro", JOptionPane.ERROR_MESSAGE);
      passwordField.requestFocus();
      return;
    }

    if (!password.equals(confirmPassword)) {
      JOptionPane.showMessageDialog(this, "Senhas não coincidem!", "Erro", JOptionPane.ERROR_MESSAGE);
      confirmPasswordField.requestFocus();
      return;
    }

    // Verificar se login já existe
    if (jsonManager.findUserByLogin(login) != null) {
      JOptionPane.showMessageDialog(this, "Login já existe! Escolha outro login.", "Erro", JOptionPane.ERROR_MESSAGE);
      loginField.requestFocus();
      return;
    }

    // Criar novo usuário
    User newUser = new User(login, password, email, name, new ArrayList<>());
    jsonManager.addUser(newUser);

    JOptionPane.showMessageDialog(this, "Usuário cadastrado com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
    dispose();
  }

  private boolean isValidEmail(String email) {
    String emailPattern = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
    Pattern pattern = Pattern.compile(emailPattern);
    return pattern.matcher(email).matches();
  }
}
