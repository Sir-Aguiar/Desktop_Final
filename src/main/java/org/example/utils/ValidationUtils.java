package org.example.utils;

import javax.swing.*;
import java.util.regex.Pattern;

public class ValidationUtils {

  private static final String EMAIL_PATTERN
          = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";

  private static final Pattern emailPattern = Pattern.compile(EMAIL_PATTERN);

  public static boolean isValidEmail(String email) {
    return emailPattern.matcher(email).matches();
  }

  public static boolean isNullOrEmpty(String str) {
    return str == null || str.trim().isEmpty();
  }

  public static boolean isValidLength(String str, int min, int max) {
    if (str == null) {
      return false;
    }
    int length = str.trim().length();
    return length >= min && length <= max;
  }

  public static void showError(JComponent parent, String message) {
    JOptionPane.showMessageDialog(parent, message, "Erro", JOptionPane.ERROR_MESSAGE);
  }

  public static void showSuccess(JComponent parent, String message) {
    JOptionPane.showMessageDialog(parent, message, "Sucesso", JOptionPane.INFORMATION_MESSAGE);
  }

  public static void showWarning(JComponent parent, String message) {
    JOptionPane.showMessageDialog(parent, message, "Aviso", JOptionPane.WARNING_MESSAGE);
  }

  public static boolean showConfirmation(JComponent parent, String message) {
    int result = JOptionPane.showConfirmDialog(parent, message, "Confirmação", JOptionPane.YES_NO_OPTION);
    return result == JOptionPane.YES_OPTION;
  }
}
