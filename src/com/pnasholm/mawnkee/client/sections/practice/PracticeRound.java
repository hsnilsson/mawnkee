package com.pnasholm.mawnkee.client.sections.practice;

import java.util.List;

import com.pnasholm.mawnkee.client.sections.dictionary.DictionaryEntry;
import com.pnasholm.mawnkee.shared.Constants;

/**
 * Models a practice round -- used for keeping score and also storing the round's entries.
 */
public class PracticeRound {

  private long startTime;
  private int numWordsRemaining;
  private int numWordsCorrect;
  private int numWordsIncorrect;
  private List<DictionaryEntry> entries;

  public PracticeRound(int numWordsTotal) {
    numWordsRemaining = numWordsTotal;
  }

  public int getNumWordsRemaining() {
    return numWordsRemaining;
  }

  public void decrementNumWordsRemaining() {
    numWordsRemaining--;
  }

  public int getNumWordsCorrect() {
    return numWordsCorrect;
  }

  public void incrementNumWordsCorrect() {
    numWordsCorrect++;
  }

  public int getNumWordsIncorrect() {
    return numWordsIncorrect;
  }

  public void incrementNumWordsIncorrect() {
    numWordsIncorrect++;
  }

  public int getNumWordsDone() {
    return numWordsCorrect + numWordsIncorrect;
  }

  public void startRound() {
    startTime = System.currentTimeMillis();
  }

  public int getNumSecondsElapsed() {
    return (int) (System.currentTimeMillis() - startTime) / Constants.ONE_SECOND_IN_MILLIS;
  }

  public List<DictionaryEntry> getEntries() {
    return entries;
  }

  public void setEntries(List<DictionaryEntry> entries) {
    this.entries = entries;
  }
}
