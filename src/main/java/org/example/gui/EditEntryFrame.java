package org.example.gui;

import org.example.entities.Entries;
import org.example.entities.User;
import org.example.utils.JSONManager;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class EditEntryFrame extends JDialog {

  private final MainFrame parent;
  private final User currentUser;
  private final JSONManager jsonManager;
  private final Entries entryToEdit;
  private JTextField titleField;
  private JTextArea contentArea;
  private JTextField dateField;
  private JComboBox<String> entryComboBox;

  // Construtor para edição direta de uma entrada específica
  public EditEntryFrame(JDialog parent, User currentUser, JSONManager jsonManager, Entries entry) {
    super(parent, "Editar Entrada", true);
    this.parent = null;
    this.currentUser = currentUser;
    this.jsonManager = jsonManager;
    this.entryToEdit = entry;
    initializeComponents();
    loadEntryData();
  }

  // Construtor para seleção de entrada
  public EditEntryFrame(MainFrame parent, User currentUser, JSONManager jsonManager) {
    super(parent, "Editar Entrada", true);
    this.parent = parent;
    this.currentUser = currentUser;
    this.jsonManager = jsonManager;
    this.entryToEdit = null;
    initializeComponents();
    setupEntrySelection();
  }

  private void initializeComponents() {
    setSize(600, 550);
    setLocationRelativeTo(getParent());
    setResizable(false);

    // Layout principal
    setLayout(new BorderLayout());

    // Painel superior
    JPanel topPanel = new JPanel(new FlowLayout());
    topPanel.setBackground(new Color(70, 130, 180));

    JLabel titleLabel = new JLabel("Editar Entrada do Diário");
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

    // Seletor de entrada (apenas se não for edição direta)
    if (entryToEdit == null) {
      gbc.gridx = 0;
      gbc.gridy = 0;
      gbc.anchor = GridBagConstraints.WEST;
      centerPanel.add(new JLabel("Selecionar Entrada:"), gbc);

      gbc.gridx = 1;
      gbc.fill = GridBagConstraints.HORIZONTAL;
      gbc.weightx = 1.0;
      entryComboBox = new JComboBox<>();
      entryComboBox.addActionListener(e -> loadSelectedEntry());
      centerPanel.add(entryComboBox, gbc);
    }

    // Campo data
    int currentRow = entryToEdit == null ? 1 : 0;
    gbc.gridx = 0;
    gbc.gridy = currentRow;
    gbc.fill = GridBagConstraints.NONE;
    gbc.weightx = 0;
    gbc.anchor = GridBagConstraints.WEST;
    centerPanel.add(new JLabel("Data:"), gbc);

    gbc.gridx = 1;
    gbc.fill = GridBagConstraints.HORIZONTAL;
    gbc.weightx = 1.0;
    dateField = new JTextField(20);
    dateField.setEditable(false);
    dateField.setBackground(new Color(240, 240, 240));
    centerPanel.add(dateField, gbc);

    // Campo título
    currentRow++;
    gbc.gridx = 0;
    gbc.gridy = currentRow;
    gbc.fill = GridBagConstraints.NONE;
    gbc.weightx = 0;
    centerPanel.add(new JLabel("Título:"), gbc);

    gbc.gridx = 1;
    gbc.fill = GridBagConstraints.HORIZONTAL;
    gbc.weightx = 1.0;
    titleField = new JTextField(20);
    centerPanel.add(titleField, gbc);

    // Campo conteúdo
    currentRow++;
    gbc.gridx = 0;
    gbc.gridy = currentRow;
    gbc.fill = GridBagConstraints.NONE;
    gbc.weightx = 0;
    gbc.anchor = GridBagConstraints.NORTHWEST;
    centerPanel.add(new JLabel("Conteúdo:"), gbc);

    gbc.gridx = 1;
    gbc.fill = GridBagConstraints.BOTH;
    gbc.weightx = 1.0;
    gbc.weighty = 1.0;
    contentArea = new JTextArea(12, 40);
    contentArea.setLineWrap(true);
    contentArea.setWrapStyleWord(true);
    contentArea.setFont(new Font("Arial", Font.PLAIN, 14));
    JScrollPane scrollPane = new JScrollPane(contentArea);
    scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
    centerPanel.add(scrollPane, gbc);

    // Informações adicionais
    currentRow++;
    gbc.gridx = 0;
    gbc.gridy = currentRow;
    gbc.gridwidth = 2;
    gbc.fill = GridBagConstraints.HORIZONTAL;
    gbc.weighty = 0;
    JLabel infoLabel = new JLabel("Autor: " + currentUser.getName());
    infoLabel.setFont(new Font("Arial", Font.ITALIC, 12));
    infoLabel.setForeground(Color.GRAY);
    centerPanel.add(infoLabel, gbc);

    add(centerPanel, BorderLayout.CENTER);

    // Painel inferior com botões
    JPanel bottomPanel = new JPanel(new FlowLayout());
    bottomPanel.setBackground(Color.WHITE);

    JButton saveButton = new JButton("Salvar Alterações");
    JButton cancelButton = new JButton("Cancelar");

    saveButton.setBackground(new Color(70, 130, 180));
    saveButton.setForeground(Color.WHITE);
    saveButton.setFocusPainted(false);

    cancelButton.setBackground(new Color(220, 220, 220));
    cancelButton.setFocusPainted(false);

    saveButton.addActionListener(e -> saveChanges());
    cancelButton.addActionListener(e -> dispose());

    bottomPanel.add(saveButton);
    bottomPanel.add(cancelButton);

    add(bottomPanel, BorderLayout.SOUTH);

    // Configurar Enter para salvar
    getRootPane().setDefaultButton(saveButton);
  }

  private void setupEntrySelection() {
    List<Entries> entries = currentUser.getEntries();
    if (entries != null && !entries.isEmpty()) {
      for (Entries entry : entries) {
        String displayText = formatDate(entry.getDate()) + " - " + entry.getTitle();
        entryComboBox.addItem(displayText);
      }
      loadSelectedEntry();
    } else {
      JOptionPane.showMessageDialog(this, "Nenhuma entrada encontrada para editar!", "Aviso", JOptionPane.WARNING_MESSAGE);
      dispose();
    }
  }

  private void loadSelectedEntry() {
    if (entryComboBox == null || entryComboBox.getSelectedIndex() == -1) {
      return;
    }

    int selectedIndex = entryComboBox.getSelectedIndex();
    List<Entries> entries = currentUser.getEntries();
    if (entries != null && selectedIndex < entries.size()) {
      Entries entry = entries.get(selectedIndex);
      loadEntryData(entry);
    }
  }

  private void loadEntryData() {
    if (entryToEdit != null) {
      loadEntryData(entryToEdit);
    }
  }

  private void loadEntryData(Entries entry) {
    titleField.setText(entry.getTitle());
    contentArea.setText(entry.getContent());
    dateField.setText(formatDate(entry.getDate()));

    // Focar no campo título
    titleField.requestFocus();
    titleField.selectAll();
  }

  private void saveChanges() {
    String title = titleField.getText().trim();
    String content = contentArea.getText().trim();

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

    // Obter a entrada a ser editada
    Entries entryToUpdate = entryToEdit;
    if (entryToUpdate == null && entryComboBox != null) {
      int selectedIndex = entryComboBox.getSelectedIndex();
      List<Entries> entries = currentUser.getEntries();
      if (entries != null && selectedIndex < entries.size()) {
        entryToUpdate = entries.get(selectedIndex);
      }
    }

    if (entryToUpdate == null) {
      JOptionPane.showMessageDialog(this, "Erro ao identificar a entrada para edição!", "Erro", JOptionPane.ERROR_MESSAGE);
      return;
    }

    // Atualizar a entrada
    entryToUpdate.setTitle(title);
    entryToUpdate.setContent(content);

    // Salvar no arquivo
    jsonManager.saveFile();

    JOptionPane.showMessageDialog(this, "Entrada atualizada com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
    dispose();
  }

  private String formatDate(String date) {
    try {
      LocalDate localDate = LocalDate.parse(date);
      return localDate.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
    } catch (Exception e) {
      return date;
    }
  }
}
