package org.example.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Entries {

  private String title;
  private String content;
  private String author;
  private String date;

  public String getFormattedDate() {
    try {
      LocalDate localDate = LocalDate.parse(date);
      return localDate.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
    } catch (Exception e) {
      return date;
    }
  }
}
