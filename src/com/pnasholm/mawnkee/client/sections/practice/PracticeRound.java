package com.pnasholm.mawnkee.client.sections.practice;


public class PracticeRound {

  private int numWordsRemaining;
  private int numWordsCorrect;
  private int numWordsIncorrect;
  private int totalTimeElapsed;

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

  public int getTotalTimeElapsed() {
    return totalTimeElapsed;
  }

  public void addElapsedTime(int numSeconds) {
    totalTimeElapsed += numSeconds;
  }
}
