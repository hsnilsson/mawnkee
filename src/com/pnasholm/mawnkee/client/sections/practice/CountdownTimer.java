package com.pnasholm.mawnkee.client.sections.practice;

import com.google.gwt.user.client.Timer;

/**
 * Timer counting down the time given to answer the current word.
 */
public class CountdownTimer extends Timer {

  private PracticeController controller;
  private PracticeView view;

  private int numInitialSeconds;
  private int numSecondsRemaining;

  public CountdownTimer(PracticeController controller, PracticeView view, int numSeconds) {
    this.controller = controller;
    this.view = view;

    numInitialSeconds = numSeconds;
    numSecondsRemaining = numSeconds;
  }

  public int getNumSecondsUsed() {
    return numInitialSeconds - numSecondsRemaining;
  }

  @Override
  public void run() {
    numSecondsRemaining--;
    if (numSecondsRemaining < 0) {
      controller.checkAnswer();
    } else {
      view.setTimeRemaining(numSecondsRemaining);
    }
  }
}
