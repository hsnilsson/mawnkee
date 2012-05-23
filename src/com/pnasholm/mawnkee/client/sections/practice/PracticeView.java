package com.pnasholm.mawnkee.client.sections.practice;

import com.pnasholm.mawnkee.shared.Language;


public interface PracticeView {

  interface Handler {
    void onLanguageChange();
    void startStop();
    void checkAnswer();
  }

  Language getSelectedLanguage();

  void setTooFewWordsForSelectedLanguageLabel();
  void clearTooFewWordsForSelectedLanguageLabel();
  void setLanguageSelectorEnabled(boolean value);
  void setInstaCheckAnswersCheckBoxEnabled(boolean value);
  void setStartStopEnabled(boolean value);
  void updateStartStopButtonText(boolean start);
  void setGamePanelVisible(boolean value);

  void uncheckAllSuggestions();
  void setWordAndSuggestions(String word, String[] suggestions);
  void stopSession();
  void setTimeRemaining(int numSeconds);
  void setStats(int numCorrect, int numIncorrect, int numRemaining);
  int getSelectedSuggestionIndex();
}
