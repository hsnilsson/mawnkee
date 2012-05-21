package com.pnasholm.mawnkee.client.sections.dictionary;

import java.io.Serializable;

import com.pnasholm.mawnkee.shared.Language;


public class DictionaryEntry implements Serializable {

  private static final long serialVersionUID = -253454707356029030L;

  private Language language;
  private String word;
  private String translation;
  private String example;

  public DictionaryEntry() {
  }

  public DictionaryEntry(Language language, String word, String translation, String example) {
    this.language = language;
    this.word = word;
    this.translation = translation;
    this.example = example;
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
