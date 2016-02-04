package com.compscieddy.reading_logger.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.HashMap;

/**
 * Created by elee on 2/2/16.
 */
@JsonIgnoreProperties({
    "bookMappings"
})
public class User {

  private String email;
  private HashMap<String, Object> timestampJoined;
  private boolean hasLoggedInWithPassword;

  public User() {
  }

  public User(String email, HashMap<String, Object> timestampJoined) {
    this.email = email;
    this.timestampJoined = timestampJoined;
    this.hasLoggedInWithPassword = false;
  }

  public String getEmail() {
    return email;
  }

  public HashMap<String, Object> getTimestampJoined() {
    return timestampJoined;
  }

  public boolean isHasLoggedInWithPassword() {
    return hasLoggedInWithPassword;
  }
}
