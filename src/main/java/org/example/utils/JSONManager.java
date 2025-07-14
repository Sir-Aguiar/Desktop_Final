package org.example.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import lombok.Getter;
import org.example.entities.User;

import java.io.*;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

@Getter
public class JSONManager {

  private final File JSONFile = new File("src/main/resources/users.json");
  private List<User> loadedData;

  public JSONManager() {
    loadedData = new ArrayList<>();
    readFile();
  }

  public void saveFile() {
    try {
      // Criar o diretório se não existir
      JSONFile.getParentFile().mkdirs();

      try (Writer writer = new FileWriter(JSONFile)) {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        gson.toJson(loadedData, writer);
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public void readFile() {
    if (!JSONFile.exists()) {
      loadedData = new ArrayList<>();
      return;
    }

    try (Reader reader = new FileReader(JSONFile)) {
      Gson gson = new Gson();
      Type usuariosListType = new TypeToken<List<User>>() {
      }.getType();
      List<User> data = gson.fromJson(reader, usuariosListType);
      loadedData = data != null ? data : new ArrayList<>();
    } catch (IOException e) {
      e.printStackTrace();
      loadedData = new ArrayList<>();
    }
  }

  public void addUser(User user) {
    if (loadedData == null) {
      loadedData = new ArrayList<>();
    }
    loadedData.add(user);
    saveFile();
  }

  public User findUserByLogin(String login) {
    if (loadedData != null) {
      for (User user : loadedData) {
        if (user.getLogin().equals(login)) {
          return user;
        }
      }
    }
    return null;
  }

  public boolean authenticateUser(String login, String password) {
    User user = findUserByLogin(login);
    return user != null && user.getPassword().equals(password);
  }
}
