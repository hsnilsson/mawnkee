package com.pnasholm.mawnkee.client.sections.practice;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.i18n.client.LocalizableResource.Generate;
import com.google.gwt.i18n.client.Messages;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.RadioButton;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.pnasholm.mawnkee.client.i18n.LanguageInternationalizer;
import com.pnasholm.mawnkee.shared.Constants;
import com.pnasholm.mawnkee.shared.Language;

/**
 * Panel for the Practice.
 */
@Singleton
public class PracticePanel extends Composite implements PracticeView {

  public interface HandlerFactory {
    Handler create(PracticeView view);
  }

  interface Binder extends UiBinder<Widget, PracticePanel> {}
  private static final Binder uiBinder = GWT.create(Binder.class);
  private static final LocalMessages messages = GWT.create(LocalMessages.class);

  private PracticeView.Handler handler;
  private LanguageInternationalizer languageInternationalizer;

  @UiField ListBox languageSelector;
  @UiField Label tooFewWordsLabel;
  @UiField Button startStopButton;

  @UiField HTMLPanel gamePanel;
  @UiField CheckBox instaCheckAnswersCheckBox;
  @UiField Label questionLabel;
  @UiField Label wordLabel;
  @UiField RadioButton suggestionRadioButton1;
  @UiField RadioButton suggestionRadioButton2;
  @UiField RadioButton suggestionRadioButton3;
  @UiField RadioButton suggestionRadioButton4;
  @UiField Button checkAnswerButton;

  @UiField Label timerLabel;
  @UiField Label numCorrectLabel;
  @UiField Label numIncorrectLabel;
  @UiField Label numRemainingLabel;

  @Inject
  public PracticePanel(
      HandlerFactory handlerFactory, LanguageInternationalizer languageInternationalizer) {
    this.languageInternationalizer = languageInternationalizer;
    initWidget(uiBinder.createAndBindUi(this));
    handler = handlerFactory.create(this);
    initPanel();
  }

  private void initPanel() {
    for (Language language : Language.values()) {
      languageSelector.addItem(languageInternationalizer.getLanguage(language));
    }
    startStopButton.setText(messages.start());
    gamePanel.setVisible(false);
    // Simulate a language change to get the UI state right.
    handler.onLanguageChange();
  }

  @UiHandler("startStopButton")
  void onStartStopClick(ClickEvent event) {
    handler.startStop();
  }

  @UiHandler("checkAnswerButton")
  void onCheckAnswerClick(ClickEvent event) {
    handler.checkAnswer();
  }

  @UiHandler("languageSelector")
  void onLanguageSelectorChange(ChangeEvent event) {
    handler.onLanguageChange();
  }

  @Override
  public void setTooFewWordsForSelectedLanguageLabel() {
    tooFewWordsLabel.setText(messages.tooFewWordsForSelectedLanguage(
        languageInternationalizer.getLanguage(getSelectedLanguage())));
  }

  @Override
  public void clearTooFewWordsForSelectedLanguageLabel() {
    tooFewWordsLabel.setText(Constants.EMPTY_STRING);
  }

  @Override
  public void setStartStopEnabled(boolean value) {
    startStopButton.setEnabled(value);
  }

  @Override
  public Language getSelectedLanguage() {
    return Language.values()[languageSelector.getSelectedIndex()];
  }

  @Override
  public void updateStartStopButtonText(boolean start) {
    startStopButton.setText(start ? messages.start() : messages.stop());
  }

  @Override
  public void setWordAndSuggestions(String word, String[] suggestions) {
    wordLabel.setText(word);
    setSuggestions(suggestions);
  }

  @Override
  public void setStats(int numCorrect, int numIncorrect, int numRemaining) {
    numCorrectLabel.setText(messages.numCorrect(numCorrect));
    numIncorrectLabel.setText(messages.numIncorrect(numIncorrect));
    numRemainingLabel.setText(messages.numRemaining(numRemaining));
  }

  @Override
  public void setTimeRemaining(int numSeconds) {
    String timeRemaining = "00:";
    if (numSeconds < 10) {
      timeRemaining += "0";
    }
    timerLabel.setText(timeRemaining + numSeconds);
  }

  private void setSuggestions(String[] suggestions) {
    // Expecting exactly four suggestions.
    suggestionRadioButton1.setText(suggestions[0]);
    suggestionRadioButton2.setText(suggestions[1]);
    suggestionRadioButton3.setText(suggestions[2]);
    suggestionRadioButton4.setText(suggestions[3]);
  }

  @Override
  public int getSelectedSuggestionIndex() {
    if (suggestionRadioButton1.getValue()) {
      return 0;
    }
    if (suggestionRadioButton2.getValue()) {
      return 1;
    }
    if (suggestionRadioButton3.getValue()) {
      return 2;
    }
    if (suggestionRadioButton4.getValue()) {
      return 3;
    }
    return -1;
  }

  @Override
  public void setLanguageSelectorEnabled(boolean value) {
    languageSelector.setEnabled(value);
  }

  @Override
  public void setInstaCheckAnswersCheckBoxEnabled(boolean value) {
    instaCheckAnswersCheckBox.setEnabled(value);
  }

  @Override
  public void setGamePanelVisible(boolean value) {
    gamePanel.setVisible(value);
  }

  @Override
  public void stopSession() {
  }

  @Override
  public void uncheckAllSuggestions() {
    suggestionRadioButton1.setValue(false);
    suggestionRadioButton2.setValue(false);
    suggestionRadioButton3.setValue(false);
    suggestionRadioButton4.setValue(false);
  }

  @Generate(format = "com.google.gwt.i18n.rebind.format.PropertiesFormat")
  public interface LocalMessages extends Messages {
    @DefaultMessage("Start")
    @Description("2do")
    String start();

    @DefaultMessage("Stop")
    @Description("2do")
    String stop();

    @DefaultMessage("{0} right")
    @Description("2do")
    String numCorrect(int numCorrect);

    @DefaultMessage("{0} wrong")
    @Description("2do")
    String numIncorrect(int numIncorrect);

    @DefaultMessage("{0} remaining")
    @Description("2do")
    String numRemaining(int numRemaining);

    @DefaultMessage("Your Dictionary contains too few words in {0} for you to practice it.")
    @Description("2do")
    String tooFewWordsForSelectedLanguage(String language);
  }
}
