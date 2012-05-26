package com.pnasholm.mawnkee.shared;


public class Constants {

  // Coding stuff.
  public static final String EMPTY_STRING = "";
  public static final int ONE_SECOND_IN_MILLIS = 1000;

  // Miscellaneous.
  public static final String AUTHOR_EMAIL = "petter.nasholm@gmail.com";
  public static final String REPORT_EMAIL = "petter.nasholm@gmail.com";

  // App specifics.
  public static final int MIN_SIZE_PRACTICEABLE_DICTIONARY = 4;
  public static final int MAX_SIZE_DICTIONARY = 1000;
  public static final int NUM_WORDS_PER_ROUND = 20;
  public static final int NUM_SECONDS_PER_WORD = 15;

  public static class ImpressivenessThresholds {
    public static final double LEVEL_1 = 0.2;
    public static final double LEVEL_2 = 0.4;
    public static final double LEVEL_3 = 0.6;
    public static final double LEVEL_4 = 0.8;
    public static final double LEVEL_5 = 1.0;
  }
}
