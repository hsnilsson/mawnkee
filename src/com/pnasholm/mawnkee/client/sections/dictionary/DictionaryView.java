package com.pnasholm.mawnkee.client.sections.dictionary;

import java.util.List;

import com.pnasholm.mawnkee.shared.Language;

public interface DictionaryView {

  interface Handler {
    void onAdd(Language language, String word, String translation, String example);
    void onRemove();
  }

  void resetInputs();
  void clearErrors();
  void setWordInvalid();
  void setWordAlreadyExists();
  void setTranslationInvalid();

  void addEntry(DictionaryEntry entry);
  List<DictionaryEntry> getSelectedEntries();

  void setDictionary(List<DictionaryEntry> dictionary);
  void updateCountLabel(int dictionarySize);
}
