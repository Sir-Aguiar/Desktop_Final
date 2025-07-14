package org.example.gui;

import org.example.entities.Entries;
import org.example.entities.User;
import org.example.utils.JSONManager;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ReportsFrame extends JDialog {

  private final MainFrame parent;
  private final User currentUser;
  private final JSONManager jsonManager;
  private JTextArea summaryArea;
  private JTable monthlyTable;
  private DefaultTableModel monthlyTableModel;
  private JList<String> recentEntriesList;
  private DefaultListModel<String> recentListModel;

  public ReportsFrame(MainFrame parent, User currentUser, JSONManager jsonManager) {
    super(parent, "Relatórios", true);
    this.parent = parent;
    this.currentUser = currentUser;
    this.jsonManager = jsonManager;
    initializeComponents();
    generateReports();
  }

  private void initializeComponents() {
    setSize(800, 600);
    setLocationRelativeTo(parent);

    // Layout principal
    setLayout(new BorderLayout());

    // Painel superior
    JPanel topPanel = new JPanel(new FlowLayout());
    topPanel.setBackground(new Color(70, 130, 180));

    JLabel titleLabel = new JLabel("Relatórios do Diário");
    titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
    titleLabel.setForeground(Color.WHITE);
    topPanel.add(titleLabel);

    add(topPanel, BorderLayout.NORTH);

    // Painel central com abas
    JTabbedPane tabbedPane = new JTabbedPane();

    // Aba 1: Resumo Geral
    JPanel summaryPanel = createSummaryPanel();
    tabbedPane.addTab("Resumo Geral", summaryPanel);

    // Aba 2: Relatório Mensal
    JPanel monthlyPanel = createMonthlyPanel();
    tabbedPane.addTab("Relatório Mensal", monthlyPanel);

    // Aba 3: Entradas Recentes
    JPanel recentPanel = createRecentPanel();
    tabbedPane.addTab("Entradas Recentes", recentPanel);

    add(tabbedPane, BorderLayout.CENTER);

    // Painel inferior com botões
    JPanel bottomPanel = new JPanel(new FlowLayout());
    bottomPanel.setBackground(Color.WHITE);

    JButton refreshButton = new JButton("Atualizar Relatórios");
    JButton exportButton = new JButton("Exportar Resumo");
    JButton closeButton = new JButton("Fechar");

    refreshButton.setBackground(new Color(70, 130, 180));
    refreshButton.setForeground(Color.WHITE);
    refreshButton.setFocusPainted(false);

    exportButton.setBackground(new Color(40, 167, 69));
    exportButton.setForeground(Color.WHITE);
    exportButton.setFocusPainted(false);

    closeButton.setBackground(new Color(220, 220, 220));
    closeButton.setFocusPainted(false);

    refreshButton.addActionListener(e -> generateReports());
    exportButton.addActionListener(e -> exportSummary());
    closeButton.addActionListener(e -> dispose());

    bottomPanel.add(refreshButton);
    bottomPanel.add(exportButton);
    bottomPanel.add(closeButton);

    add(bottomPanel, BorderLayout.SOUTH);
  }

  private JPanel createSummaryPanel() {
    JPanel panel = new JPanel(new BorderLayout());
    panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

    JLabel headerLabel = new JLabel("Estatísticas Gerais");
    headerLabel.setFont(new Font("Arial", Font.BOLD, 16));
    headerLabel.setHorizontalAlignment(SwingConstants.CENTER);
    panel.add(headerLabel, BorderLayout.NORTH);

    summaryArea = new JTextArea();
    summaryArea.setEditable(false);
    summaryArea.setFont(new Font("Courier New", Font.PLAIN, 14));
    summaryArea.setBackground(new Color(248, 249, 250));
    summaryArea.setMargin(new Insets(10, 10, 10, 10));

    JScrollPane scrollPane = new JScrollPane(summaryArea);
    scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
    panel.add(scrollPane, BorderLayout.CENTER);

    return panel;
  }

  private JPanel createMonthlyPanel() {
    JPanel panel = new JPanel(new BorderLayout());
    panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

    JLabel headerLabel = new JLabel("Entradas por Mês");
    headerLabel.setFont(new Font("Arial", Font.BOLD, 16));
    headerLabel.setHorizontalAlignment(SwingConstants.CENTER);
    panel.add(headerLabel, BorderLayout.NORTH);

    String[] columnNames = {"Mês/Ano", "Quantidade", "Palavras (Aprox.)"};
    monthlyTableModel = new DefaultTableModel(columnNames, 0) {
      @Override
      public boolean isCellEditable(int row, int column) {
        return false;
      }
    };

    monthlyTable = new JTable(monthlyTableModel);
    monthlyTable.setRowHeight(25);
    monthlyTable.getTableHeader().setReorderingAllowed(false);

    JScrollPane scrollPane = new JScrollPane(monthlyTable);
    panel.add(scrollPane, BorderLayout.CENTER);

    return panel;
  }

  private JPanel createRecentPanel() {
    JPanel panel = new JPanel(new BorderLayout());
    panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

    JLabel headerLabel = new JLabel("Últimas 10 Entradas");
    headerLabel.setFont(new Font("Arial", Font.BOLD, 16));
    headerLabel.setHorizontalAlignment(SwingConstants.CENTER);
    panel.add(headerLabel, BorderLayout.NORTH);

    recentListModel = new DefaultListModel<>();
    recentEntriesList = new JList<>(recentListModel);
    recentEntriesList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    recentEntriesList.setFont(new Font("Arial", Font.PLAIN, 12));

    JScrollPane scrollPane = new JScrollPane(recentEntriesList);
    panel.add(scrollPane, BorderLayout.CENTER);

    return panel;
  }

  private void generateReports() {
    List<Entries> entries = currentUser.getEntries();
    if (entries == null || entries.isEmpty()) {
      summaryArea.setText("Nenhuma entrada encontrada para gerar relatórios.");
      monthlyTableModel.setRowCount(0);
      recentListModel.clear();
      return;
    }

    generateSummaryReport(entries);
    generateMonthlyReport(entries);
    generateRecentEntriesReport(entries);
  }

  private void generateSummaryReport(List<Entries> entries) {
    StringBuilder summary = new StringBuilder();
    summary.append("==========================================\n");
    summary.append("           RELATÓRIO GERAL DO DIÁRIO\n");
    summary.append("==========================================\n\n");

    summary.append("Usuário: ").append(currentUser.getName()).append("\n");
    summary.append("Email: ").append(currentUser.getEmail()).append("\n\n");

    summary.append("ESTATÍSTICAS GERAIS:\n");
    summary.append("────────────────────────────────────────\n");
    summary.append("Total de Entradas: ").append(entries.size()).append("\n");

    // Calcular estatísticas
    int totalWords = entries.stream()
            .mapToInt(entry -> entry.getContent().split("\\s+").length)
            .sum();

    int totalCharacters = entries.stream()
            .mapToInt(entry -> entry.getContent().length())
            .sum();

    double avgWordsPerEntry = entries.isEmpty() ? 0 : (double) totalWords / entries.size();
    double avgCharsPerEntry = entries.isEmpty() ? 0 : (double) totalCharacters / entries.size();

    summary.append("Total de Palavras: ").append(totalWords).append("\n");
    summary.append("Total de Caracteres: ").append(totalCharacters).append("\n");
    summary.append("Média de Palavras por Entrada: ").append(String.format("%.1f", avgWordsPerEntry)).append("\n");
    summary.append("Média de Caracteres por Entrada: ").append(String.format("%.1f", avgCharsPerEntry)).append("\n\n");

    // Entrada mais longa
    Entries longestEntry = entries.stream()
            .max((e1, e2) -> Integer.compare(e1.getContent().length(), e2.getContent().length()))
            .orElse(null);

    if (longestEntry != null) {
      summary.append("ENTRADA MAIS LONGA:\n");
      summary.append("────────────────────────────────────────\n");
      summary.append("Título: ").append(longestEntry.getTitle()).append("\n");
      summary.append("Data: ").append(formatDate(longestEntry.getDate())).append("\n");
      summary.append("Caracteres: ").append(longestEntry.getContent().length()).append("\n\n");
    }

    // Primeira e última entrada
    try {
      LocalDate firstDate = entries.stream()
              .map(entry -> LocalDate.parse(entry.getDate()))
              .min(LocalDate::compareTo)
              .orElse(null);

      LocalDate lastDate = entries.stream()
              .map(entry -> LocalDate.parse(entry.getDate()))
              .max(LocalDate::compareTo)
              .orElse(null);

      if (firstDate != null && lastDate != null) {
        summary.append("PERÍODO DE ESCRITA:\n");
        summary.append("────────────────────────────────────────\n");
        summary.append("Primeira Entrada: ").append(firstDate.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))).append("\n");
        summary.append("Última Entrada: ").append(lastDate.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))).append("\n");
        summary.append("Dias de Escrita: ").append(java.time.temporal.ChronoUnit.DAYS.between(firstDate, lastDate) + 1).append("\n\n");
      }
    } catch (Exception e) {
      // Ignora erros de parsing de data
    }

    summary.append("Relatório gerado em: ").append(LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));

    summaryArea.setText(summary.toString());
    summaryArea.setCaretPosition(0);
  }

  private void generateMonthlyReport(List<Entries> entries) {
    monthlyTableModel.setRowCount(0);

    // Agrupar entradas por mês/ano
    Map<String, List<Entries>> entriesByMonth = entries.stream()
            .collect(Collectors.groupingBy(entry -> {
              try {
                LocalDate date = LocalDate.parse(entry.getDate());
                return date.format(DateTimeFormatter.ofPattern("MM/yyyy"));
              } catch (Exception e) {
                return "Data Inválida";
              }
            }));

    // Adicionar dados à tabela
    for (Map.Entry<String, List<Entries>> monthEntry : entriesByMonth.entrySet()) {
      String monthYear = monthEntry.getKey();
      List<Entries> monthEntries = monthEntry.getValue();
      int quantity = monthEntries.size();
      int words = monthEntries.stream()
              .mapToInt(entry -> entry.getContent().split("\\s+").length)
              .sum();

      Object[] row = {monthYear, quantity, words};
      monthlyTableModel.addRow(row);
    }
  }

  private void generateRecentEntriesReport(List<Entries> entries) {
    recentListModel.clear();

    // Ordenar entradas por data (mais recentes primeiro)
    List<Entries> sortedEntries = entries.stream()
            .sorted((e1, e2) -> {
              try {
                LocalDate date1 = LocalDate.parse(e1.getDate());
                LocalDate date2 = LocalDate.parse(e2.getDate());
                return date2.compareTo(date1); // Ordem decrescente
              } catch (Exception e) {
                return 0;
              }
            })
            .limit(10)
            .collect(Collectors.toList());

    for (Entries entry : sortedEntries) {
      String displayText = String.format("%s - %s (%d chars)",
              formatDate(entry.getDate()),
              entry.getTitle(),
              entry.getContent().length());
      recentListModel.addElement(displayText);
    }
  }

  private void exportSummary() {
    JFileChooser fileChooser = new JFileChooser();
    fileChooser.setDialogTitle("Exportar Resumo");
    fileChooser.setSelectedFile(new java.io.File("relatorio_diario.txt"));

    int result = fileChooser.showSaveDialog(this);
    if (result == JFileChooser.APPROVE_OPTION) {
      try {
        java.io.File file = fileChooser.getSelectedFile();
        java.nio.file.Files.write(file.toPath(), summaryArea.getText().getBytes());
        JOptionPane.showMessageDialog(this, "Resumo exportado com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
      } catch (Exception e) {
        JOptionPane.showMessageDialog(this, "Erro ao exportar resumo: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
      }
    }
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
