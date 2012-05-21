package com.pnasholm.mawnkee.server;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import com.google.appengine.api.users.User;
import com.pnasholm.mawnkee.shared.Language;

@PersistenceCapable(identityType = IdentityType.APPLICATION)
public class PersistentDictionaryEntry {

  @PrimaryKey
  @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
  private Long id;

  @Persistent
  private User user;
  Language language;

  @Persistent
  private String word;

  @Persistent
  private String translation;

  @Persistent
  private String example;

  public PersistentDictionaryEntry(
      User user, Language language, String word, String translation, String example) {
    this.user = user;
    this.language = language;
    this.word = word;
    this.translation = translation;
    this.example = example;
  }

  public Long getId() {
    return id;
  }

  public User getUser() {
    return user;
  }

  public void setUser(User user) {
    this.user = user;
  }

  public Language getLanguage() {
    return language;
  }

  public void setLanguage(Language language) {
    this.language = language;
  }

  public String getWord() {
    return word;
  }

  public void setWord(String word) {
    this.word = word;
  }

  public String getTranslation() {
    return translation;
  }

  public void setTranslation(String translation) {
    this.translation = translation;
  }

  public String getExample() {
    return example;
  }

  public void setExample(String example) {
    this.example = example;
  }
}
