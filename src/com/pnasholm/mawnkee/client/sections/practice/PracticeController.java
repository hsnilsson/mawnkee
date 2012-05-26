package com.pnasholm.mawnkee.client.sections.practice;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.user.client.Random;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.inject.assistedinject.Assisted;
import com.google.web.bindery.event.shared.EventBus;
import com.pnasholm.mawnkee.client.MawnkeeUser;
import com.pnasholm.mawnkee.client.events.DictionaryChangedEvent;
import com.pnasholm.mawnkee.client.events.DictionaryChangedHandler;
import com.pnasholm.mawnkee.client.sections.dictionary.DictionaryEntry;
import com.pnasholm.mawnkee.shared.Constants;
import com.pnasholm.mawnkee.shared.Language;

@Singleton
public class PracticeController implements PracticeView.Handler, DictionaryChangedHandler {

  private List<DictionaryEntry> dictionary;
  private PracticeView view;

  private boolean roundRunning;
  private PracticeRound currentRound;
  private CountdownTimer roundTimer;
  private int correctSuggestionIndex;

  @Inject
  public PracticeController(
      @Assisted PracticeView view,
      EventBus eventBus,
      MawnkeeUser user) {
    this.view = view;
    eventBus.addHandler(DictionaryChangedEvent.TYPE, this);
    dictionary = user.getDictionary();
    roundRunning = false;
  }

  @Override
  public void onDictionaryChanged() {
    onLanguageChange();
  }

  @Override
  public void onLanguageChange() {
    boolean isLanguagePracticeable =
        getEntriesByLanguage(dictionary, view.getSelectedLanguage()).size()
            >= Constants.MIN_SIZE_PRACTICEABLE_DICTIONARY;
    if (isLanguagePracticeable) {
      view.clearTooFewWordsForSelectedLanguageLabel();
    } else {
      view.setTooFewWordsForSelectedLanguageLabel();
    }
    view.setStartStopEnabled(isLanguagePracticeable);
    view.setInstaCheckAnswersCheckBoxEnabled(isLanguagePracticeable);
  }

  @Override
  public void startStop() {
    if (!roundRunning) {
      startRound();
    } else {
      forceEndRound();
    }
  }

  private void startRound() {
    currentRound = new PracticeRound(Constants.NUM_WORDS_PER_ROUND);
    currentRound.setEntries(getEntriesByLanguage(dictionary, view.getSelectedLanguage()));
    currentRound.startRound();

    view.setLanguageSelectorEnabled(false);
    view.updateStartStopButtonText(false);
    view.setStats(0, 0, currentRound.getNumWordsRemaining());
    view.setGamePanelVisible(true);
    setNewWord();
    roundRunning = true;
  }

  private void forceEndRound() {
    roundRunning = false;
    view.setLanguageSelectorEnabled(true);
    view.updateStartStopButtonText(true);
    view.setGamePanelVisible(false);
  }

  private void endRound() {
    forceEndRound();
    view.setPreviousRoundStats(
        currentRound.getNumWordsCorrect(),
        currentRound.getNumWordsDone(),
        Math.max(1, currentRound.getNumSecondsElapsed()));
  }

  @Override
  public void checkAnswer() {
    roundTimer.cancel();
    if (correctSuggestionIndex == view.getSelectedSuggestionIndex()) {
      currentRound.incrementNumWordsCorrect();
    } else {
      currentRound.incrementNumWordsIncorrect();
    }
    view.setStats(
        currentRound.getNumWordsCorrect(),
        currentRound.getNumWordsIncorrect(),
        currentRound.getNumWordsRemaining());

    if (currentRound.getNumWordsRemaining() == 0) {
      endRound();
    } else {
      setNewWord();
    }
  }

  private void setNewWord() {
    if (roundTimer != null) {
      roundTimer.cancel();
    }
    view.uncheckAllSuggestions();

    int correctIndex = Random.nextInt(currentRound.getEntries().size());
    DictionaryEntry correctEntry = dictionary.get(correctIndex);
    List<Integer> validIndexes = getRange(correctIndex, currentRound.getEntries().size() - 1);
    List<DictionaryEntry> suggestions = new ArrayList<DictionaryEntry>(4);
    for (int i = 0; i < 3; i++) {
       suggestions.add(dictionary.get(validIndexes.remove(Random.nextInt(validIndexes.size()))));
    }
    correctSuggestionIndex = Random.nextInt(4);
    suggestions.add(correctSuggestionIndex, correctEntry);

    view.setWordAndSuggestions(
        correctEntry.getWord(),
        new String[] {
            suggestions.get(0).getTranslation(),
            suggestions.get(1).getTranslation(),
            suggestions.get(2).getTranslation(),
            suggestions.get(3).getTranslation() });
    view.setTimeRemaining(Constants.NUM_SECONDS_PER_WORD);
    roundTimer = new CountdownTimer(this, view, Constants.NUM_SECONDS_PER_WORD);
    roundTimer.scheduleRepeating(Constants.ONE_SECOND_IN_MILLIS);
    currentRound.decrementNumWordsRemaining();
  }

  private List<DictionaryEntry> getEntriesByLanguage(
      List<DictionaryEntry> entries, Language language) {
    List<DictionaryEntry> filteredEntries = new ArrayList<DictionaryEntry>();
    for (DictionaryEntry entry : entries) {
      if (entry.getLanguage().equals(language)) {
        filteredEntries.add(entry);
      }
    }
    return filteredEntries;
  }

  private List<Integer> getRange(int exclude, int upperBound) {
    List<Integer> range = new ArrayList<Integer>(upperBound);
    for (int i = 0; i <= upperBound; i++) {
      if (i != exclude) {
        range.add(i);
      }
    }
    return range;
  }
}
