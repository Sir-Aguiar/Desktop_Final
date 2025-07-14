package org.example.gui;

import org.example.entities.Entries;
import org.example.entities.User;
import org.example.utils.JSONManager;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class ViewEntriesFrame extends JDialog {

  private final MainFrame parent;
  private final User currentUser;
  private final JSONManager jsonManager;
  private JTable entriesTable;
  private DefaultTableModel tableModel;
  private JTextArea detailsArea;
  private JTextField searchField;
  private TableRowSorter<DefaultTableModel> sorter;

  public ViewEntriesFrame(MainFrame parent, User currentUser, JSONManager jsonManager) {
    super(parent, "Visualizar Entradas", true);
    this.parent = parent;
    this.currentUser = currentUser;
    this.jsonManager = jsonManager;
    initializeComponents();
    loadEntries();
  }

  private void initializeComponents() {
    setSize(900, 700);
    setLocationRelativeTo(parent);

    // Layout principal
    setLayout(new BorderLayout());

    // Painel superior
    JPanel topPanel = new JPanel(new BorderLayout());
    topPanel.setBackground(new Color(70, 130, 180));
    topPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

    JLabel titleLabel = new JLabel("Minhas Entradas");
    titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
    titleLabel.setForeground(Color.WHITE);
    topPanel.add(titleLabel, BorderLayout.WEST);

    // Painel de busca
    JPanel searchPanel = new JPanel(new FlowLayout());
    searchPanel.setOpaque(false);

    JLabel searchLabel = new JLabel("Buscar:");
    searchLabel.setForeground(Color.WHITE);
    searchPanel.add(searchLabel);

    searchField = new JTextField(20);
    searchField.addKeyListener(new java.awt.event.KeyAdapter() {
      @Override
      public void keyReleased(java.awt.event.KeyEvent evt) {
        filterEntries();
      }
    });
    searchPanel.add(searchField);

    JButton refreshButton = new JButton("Atualizar");
    refreshButton.setBackground(Color.WHITE);
    refreshButton.setFocusPainted(false);
    refreshButton.addActionListener(e -> loadEntries());
    searchPanel.add(refreshButton);

    topPanel.add(searchPanel, BorderLayout.EAST);

    add(topPanel, BorderLayout.NORTH);

    // Painel central dividido
    JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
    splitPane.setDividerLocation(400);
    splitPane.setResizeWeight(0.5);

    // Painel esquerdo - lista de entradas
    JPanel leftPanel = new JPanel(new BorderLayout());
    leftPanel.setBorder(BorderFactory.createTitledBorder("Lista de Entradas"));

    // Tabela de entradas
    String[] columnNames = {"Data", "Título", "Autor"};
    tableModel = new DefaultTableModel(columnNames, 0) {
      @Override
      public boolean isCellEditable(int row, int column) {
        return false;
      }
    };

    entriesTable = new JTable(tableModel);
    entriesTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    entriesTable.setRowHeight(25);
    entriesTable.getTableHeader().setReorderingAllowed(false);

    // Configurar sorter
    sorter = new TableRowSorter<>(tableModel);
    entriesTable.setRowSorter(sorter);

    // Listener para seleção
    entriesTable.addMouseListener(new MouseAdapter() {
      @Override
      public void mouseClicked(MouseEvent e) {
        if (e.getClickCount() == 1) {
          showEntryDetails();
        }
      }
    });

    JScrollPane tableScrollPane = new JScrollPane(entriesTable);
    leftPanel.add(tableScrollPane, BorderLayout.CENTER);

    splitPane.setLeftComponent(leftPanel);

    // Painel direito - detalhes da entrada
    JPanel rightPanel = new JPanel(new BorderLayout());
    rightPanel.setBorder(BorderFactory.createTitledBorder("Detalhes da Entrada"));

    detailsArea = new JTextArea();
    detailsArea.setEditable(false);
    detailsArea.setLineWrap(true);
    detailsArea.setWrapStyleWord(true);
    detailsArea.setFont(new Font("Arial", Font.PLAIN, 14));
    detailsArea.setMargin(new Insets(10, 10, 10, 10));

    JScrollPane detailsScrollPane = new JScrollPane(detailsArea);
    rightPanel.add(detailsScrollPane, BorderLayout.CENTER);

    splitPane.setRightComponent(rightPanel);

    add(splitPane, BorderLayout.CENTER);

    // Painel inferior com botões
    JPanel bottomPanel = new JPanel(new FlowLayout());
    bottomPanel.setBackground(Color.WHITE);

    JButton editButton = new JButton("Editar Selecionada");
    JButton deleteButton = new JButton("Excluir Selecionada");
    JButton newButton = new JButton("Nova Entrada");
    JButton closeButton = new JButton("Fechar");

    editButton.setBackground(new Color(70, 130, 180));
    editButton.setForeground(Color.WHITE);
    editButton.setFocusPainted(false);

    deleteButton.setBackground(new Color(220, 53, 69));
    deleteButton.setForeground(Color.WHITE);
    deleteButton.setFocusPainted(false);

    newButton.setBackground(new Color(40, 167, 69));
    newButton.setForeground(Color.WHITE);
    newButton.setFocusPainted(false);

    closeButton.setBackground(new Color(220, 220, 220));
    closeButton.setFocusPainted(false);

    editButton.addActionListener(e -> editSelectedEntry());
    deleteButton.addActionListener(e -> deleteSelectedEntry());
    newButton.addActionListener(e -> openNewEntryWindow());
    closeButton.addActionListener(e -> dispose());

    bottomPanel.add(newButton);
    bottomPanel.add(editButton);
    bottomPanel.add(deleteButton);
    bottomPanel.add(closeButton);

    add(bottomPanel, BorderLayout.SOUTH);
  }

  private void loadEntries() {
    tableModel.setRowCount(0);
    detailsArea.setText("");

    List<Entries> entries = currentUser.getEntries();
    if (entries != null) {
      for (Entries entry : entries) {
        String formattedDate = formatDate(entry.getDate());
        Object[] row = {formattedDate, entry.getTitle(), entry.getAuthor()};
        tableModel.addRow(row);
      }
    }

    if (tableModel.getRowCount() == 0) {
      detailsArea.setText("Nenhuma entrada encontrada.\nClique em 'Nova Entrada' para criar sua primeira entrada!");
    }
  }

  private void filterEntries() {
    String text = searchField.getText().toLowerCase();
    if (text.trim().length() == 0) {
      sorter.setRowFilter(null);
    } else {
      sorter.setRowFilter(RowFilter.regexFilter("(?i)" + text));
    }
  }

  private void showEntryDetails() {
    int selectedRow = entriesTable.getSelectedRow();
    if (selectedRow == -1) {
      detailsArea.setText("");
      return;
    }

    // Converter índice da view para modelo
    int modelRow = entriesTable.convertRowIndexToModel(selectedRow);

    List<Entries> entries = currentUser.getEntries();
    if (entries != null && modelRow < entries.size()) {
      Entries entry = entries.get(modelRow);
      StringBuilder details = new StringBuilder();
      details.append("TÍTULO: ").append(entry.getTitle()).append("\n\n");
      details.append("DATA: ").append(formatDate(entry.getDate())).append("\n\n");
      details.append("AUTOR: ").append(entry.getAuthor()).append("\n\n");
      details.append("CONTEÚDO:\n");
      details.append(entry.getContent());

      detailsArea.setText(details.toString());
      detailsArea.setCaretPosition(0);
    }
  }

  private void editSelectedEntry() {
    int selectedRow = entriesTable.getSelectedRow();
    if (selectedRow == -1) {
      JOptionPane.showMessageDialog(this, "Por favor, selecione uma entrada para editar!", "Aviso", JOptionPane.WARNING_MESSAGE);
      return;
    }

    int modelRow = entriesTable.convertRowIndexToModel(selectedRow);
    List<Entries> entries = currentUser.getEntries();
    if (entries != null && modelRow < entries.size()) {
      Entries entry = entries.get(modelRow);
      EditEntryFrame editFrame = new EditEntryFrame(this, currentUser, jsonManager, entry);
      editFrame.setVisible(true);
      loadEntries(); // Recarregar após edição
    }
  }

  private void deleteSelectedEntry() {
    int selectedRow = entriesTable.getSelectedRow();
    if (selectedRow == -1) {
      JOptionPane.showMessageDialog(this, "Por favor, selecione uma entrada para excluir!", "Aviso", JOptionPane.WARNING_MESSAGE);
      return;
    }

    int confirm = JOptionPane.showConfirmDialog(this,
            "Tem certeza que deseja excluir esta entrada?",
            "Confirmar Exclusão",
            JOptionPane.YES_NO_OPTION);

    if (confirm == JOptionPane.YES_OPTION) {
      int modelRow = entriesTable.convertRowIndexToModel(selectedRow);
      List<Entries> entries = currentUser.getEntries();
      if (entries != null && modelRow < entries.size()) {
        entries.remove(modelRow);
        jsonManager.saveFile();
        loadEntries();
        JOptionPane.showMessageDialog(this, "Entrada excluída com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
      }
    }
  }

  private void openNewEntryWindow() {
    NewEntryFrame newEntryFrame = new NewEntryFrame(parent, currentUser, jsonManager);
    newEntryFrame.setVisible(true);
    loadEntries(); // Recarregar após criar nova entrada
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
