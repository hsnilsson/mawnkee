package com.pnasholm.mawnkee.shared;

public class DictionaryEntryVerifier {

  public static boolean isLanguageValid(String language) {
    return true;
  }

  public static boolean isWordValid(String word) {
    return !word.isEmpty();
  }

  public static boolean isTranslationValid(String translation) {
    return !translation.isEmpty();
  }

  public static boolean isExampleValid(String example) {
    return true;
  }
}
