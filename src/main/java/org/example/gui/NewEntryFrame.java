package org.example.gui;

import org.example.entities.Entries;
import org.example.entities.User;
import org.example.utils.JSONManager;
import org.example.utils.ModernUIUtils;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class NewEntryFrame extends JDialog {

  private final MainFrame parent;
  private final User currentUser;
  private final JSONManager jsonManager;
  private JTextField titleField;
  private JTextArea contentArea;
  private JSpinner dateSpinner;
  private JButton selectDateButton;

  public NewEntryFrame(MainFrame parent, User currentUser, JSONManager jsonManager) {
    super(parent, "Nova Entrada", true);
    this.parent = parent;
    this.currentUser = currentUser;
    this.jsonManager = jsonManager;
    initializeComponents();
  }

  private void initializeComponents() {
    setSize(700, 600);
    setLocationRelativeTo(parent);
    setResizable(false);

    // Layout principal
    setLayout(new BorderLayout());

    // Painel superior com gradiente
    JPanel topPanel = ModernUIUtils.createGradientPanel(
            ModernUIUtils.PRIMARY_COLOR,
            ModernUIUtils.PRIMARY_HOVER
    );
    topPanel.setPreferredSize(new Dimension(700, 80));
    topPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 25));

    JLabel titleLabel = ModernUIUtils.createModernLabel(
            "Nova Entrada no Diário",
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

    // Campo data
    gbc.gridx = 0;
    gbc.gridy = 0;
    gbc.anchor = GridBagConstraints.WEST;
    JLabel dateLabel = ModernUIUtils.createModernLabel(
            "Data:",
            ModernUIUtils.SUBTITLE_FONT,
            ModernUIUtils.GRAY_700
    );
    centerPanel.add(dateLabel, gbc);

    gbc.gridx = 1;
    gbc.fill = GridBagConstraints.HORIZONTAL;
    gbc.weightx = 1.0;

    // Painel para data com botão
    JPanel datePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
    datePanel.setBackground(ModernUIUtils.GRAY_50);

    // Spinner para data
    SpinnerDateModel dateModel = new SpinnerDateModel();
    dateSpinner = new JSpinner(dateModel);
    dateSpinner.setEditor(new JSpinner.DateEditor(dateSpinner, "dd/MM/yyyy"));
    dateSpinner.setFont(ModernUIUtils.BODY_FONT);
    dateSpinner.setPreferredSize(new Dimension(150, 40));
    dateSpinner.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(ModernUIUtils.GRAY_300, 1),
            BorderFactory.createEmptyBorder(8, 12, 8, 12)
    ));

    selectDateButton = ModernUIUtils.createTextButton("Hoje");
    selectDateButton.setPreferredSize(new Dimension(80, 40));
    selectDateButton.addActionListener(e -> {
      dateSpinner.setValue(new java.util.Date());
    });

    datePanel.add(dateSpinner);
    datePanel.add(Box.createHorizontalStrut(10));
    datePanel.add(selectDateButton);

    centerPanel.add(datePanel, gbc);

    // Campo título
    gbc.gridx = 0;
    gbc.gridy = 1;
    gbc.fill = GridBagConstraints.NONE;
    gbc.weightx = 0;
    JLabel titleLbl = ModernUIUtils.createModernLabel(
            "Título:",
            ModernUIUtils.SUBTITLE_FONT,
            ModernUIUtils.GRAY_700
    );
    centerPanel.add(titleLbl, gbc);

    gbc.gridx = 1;
    gbc.fill = GridBagConstraints.HORIZONTAL;
    gbc.weightx = 1.0;
    titleField = ModernUIUtils.createModernTextField(30);
    titleField.setPreferredSize(new Dimension(300, 40));
    centerPanel.add(titleField, gbc);

    // Campo conteúdo
    gbc.gridx = 0;
    gbc.gridy = 2;
    gbc.fill = GridBagConstraints.NONE;
    gbc.weightx = 0;
    gbc.anchor = GridBagConstraints.NORTHWEST;
    JLabel contentLbl = ModernUIUtils.createModernLabel(
            "Conteúdo:",
            ModernUIUtils.SUBTITLE_FONT,
            ModernUIUtils.GRAY_700
    );
    centerPanel.add(contentLbl, gbc);

    gbc.gridx = 1;
    gbc.fill = GridBagConstraints.BOTH;
    gbc.weightx = 1.0;
    gbc.weighty = 1.0;
    contentArea = ModernUIUtils.createModernTextArea(12, 40);

    JScrollPane scrollPane = new JScrollPane(contentArea);
    scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
    scrollPane.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(ModernUIUtils.GRAY_300, 1),
            BorderFactory.createEmptyBorder(4, 4, 4, 4)
    ));
    scrollPane.setBackground(ModernUIUtils.WHITE);
    centerPanel.add(scrollPane, gbc);

    // Informações adicionais
    gbc.gridx = 0;
    gbc.gridy = 3;
    gbc.gridwidth = 2;
    gbc.fill = GridBagConstraints.HORIZONTAL;
    gbc.weighty = 0;
    JLabel infoLabel = ModernUIUtils.createModernLabel(
            "Autor: " + currentUser.getName(),
            ModernUIUtils.SMALL_FONT,
            ModernUIUtils.GRAY_500
    );
    centerPanel.add(infoLabel, gbc);

    add(centerPanel, BorderLayout.CENTER);

    // Painel inferior com botões
    JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 20));
    bottomPanel.setBackground(ModernUIUtils.WHITE);
    bottomPanel.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, ModernUIUtils.GRAY_300));

    JButton saveButton = ModernUIUtils.createSuccessButton("Salvar");
    JButton cancelButton = ModernUIUtils.createTextButton("Cancelar");

    saveButton.setPreferredSize(new Dimension(120, 45));
    cancelButton.setPreferredSize(new Dimension(120, 45));

    saveButton.addActionListener(e -> saveEntry());
    cancelButton.addActionListener(e -> dispose());

    bottomPanel.add(saveButton);
    bottomPanel.add(cancelButton);

    add(bottomPanel, BorderLayout.SOUTH);

    // Configurar Enter para salvar
    getRootPane().setDefaultButton(saveButton);

    // Focar no campo título
    SwingUtilities.invokeLater(() -> titleField.requestFocus());
  }

  private void saveEntry() {
    String title = titleField.getText().trim();
    String content = contentArea.getText().trim();

    // Obter data do spinner
    java.util.Date selectedDate = (java.util.Date) dateSpinner.getValue();
    LocalDate localDate = selectedDate.toInstant().atZone(java.time.ZoneId.systemDefault()).toLocalDate();
    String date = localDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));

    // Validações
    if (title.isEmpty()) {
      JOptionPane.showMessageDialog(this, "Por favor, informe o título da entrada!", "Erro", JOptionPane.ERROR_MESSAGE);
      titleField.requestFocus();
      return;
    }

    if (title.length() < 3) {
      JOptionPane.showMessageDialog(this, "Título deve ter pelo menos 3 caracteres!", "Erro", JOptionPane.ERROR_MESSAGE);
      titleField.requestFocus();
      return;
    }

    if (title.length() > 100) {
      JOptionPane.showMessageDialog(this, "Título deve ter no máximo 100 caracteres!", "Erro", JOptionPane.ERROR_MESSAGE);
      titleField.requestFocus();
      return;
    }

    if (content.isEmpty()) {
      JOptionPane.showMessageDialog(this, "Por favor, informe o conteúdo da entrada!", "Erro", JOptionPane.ERROR_MESSAGE);
      contentArea.requestFocus();
      return;
    }

    if (content.length() < 10) {
      JOptionPane.showMessageDialog(this, "Conteúdo deve ter pelo menos 10 caracteres!", "Erro", JOptionPane.ERROR_MESSAGE);
      contentArea.requestFocus();
      return;
    }

    if (content.length() > 5000) {
      JOptionPane.showMessageDialog(this, "Conteúdo deve ter no máximo 5000 caracteres!", "Erro", JOptionPane.ERROR_MESSAGE);
      contentArea.requestFocus();
      return;
    }

    // Criar nova entrada
    Entries newEntry = new Entries(title, content, currentUser.getName(), date);
    currentUser.addEntry(newEntry);

    // Salvar no arquivo
    jsonManager.saveFile();

    // Mostrar mensagem de sucesso com estilo moderno
    JOptionPane.showMessageDialog(this,
            "Entrada salva com sucesso!\n\n"
                    + "Data: " + localDate.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) + "\n"
                    + "Título: " + title + "\n"
                    + "Conteúdo: " + content.length() + " caracteres",
            "Sucesso",
            JOptionPane.INFORMATION_MESSAGE);
    dispose();
  }
}
