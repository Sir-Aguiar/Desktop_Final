package org.example;

import javax.swing.*;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class App extends JFrame {

  public App() {
    super();
    initialize();
  }

  private void initialize() {
    setTitle("Hello World");
    setSize(400, 300);
    this.setLayout(null);
    this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
    this.setVisible(true);
    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    this.setLocationRelativeTo(null);
    JLabel label = new JLabel("Hello, World!", SwingConstants.CENTER);
    add(label);
  }
}