package org.example.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class User {

  private String login;
  private String password;
  private String email;
  private String name;
  private List<Entries> entries;

  public void addEntry(Entries entry) {
    if (entries == null) {
      throw new IllegalStateException("Entries list is not initialized.");
    }

    entries.add(entry);
  }
}
