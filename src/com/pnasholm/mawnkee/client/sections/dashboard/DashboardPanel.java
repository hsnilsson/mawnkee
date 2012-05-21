package com.pnasholm.mawnkee.client.sections.dashboard;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gwt.core.client.GWT;
import com.google.gwt.i18n.client.LocalizableResource.Generate;
import com.google.gwt.i18n.client.Messages;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.web.bindery.event.shared.EventBus;
import com.pnasholm.mawnkee.client.MawnkeeUser;
import com.pnasholm.mawnkee.client.events.DictionaryChangedEvent;
import com.pnasholm.mawnkee.client.events.DictionaryChangedHandler;
import com.pnasholm.mawnkee.client.sections.dictionary.DictionaryEntry;
import com.pnasholm.mawnkee.shared.Constants;
import com.pnasholm.mawnkee.shared.Language;

/**
 * Panel for the Dashboard.
 */
@Singleton
public class DashboardPanel extends Composite implements DictionaryChangedHandler {

  interface Binder extends UiBinder<Widget, DashboardPanel> {}
  private static final Binder uiBinder = GWT.create(Binder.class);
  private static final LocalMessages messages = GWT.create(LocalMessages.class);

  private List<DictionaryEntry> dictionary;

  @UiField Label greetingMessageLabel;
  @UiField FlowPanel dictionaryPanel;
  @UiField Label dictionaryMessageLabel;
  @UiField FlowPanel practicePanel;
  @UiField Label practiceMessageLabel;

  @Inject
  public DashboardPanel(EventBus eventBus, MawnkeeUser user) {
    eventBus.addHandler(DictionaryChangedEvent.TYPE, this);
    dictionary = user.getDictionary();
    initWidget(uiBinder.createAndBindUi(this));
    initMessages(user.getLoginInfo().getNickname(), dictionary);
  }

  private void initMessages(String userName, List<DictionaryEntry> dictionary) {
    greetingMessageLabel.setText(messages.welcome(userName));
    setDictionaryMessage();
    practiceMessageLabel.setText(messages.noPractices());
  }

  private void setDictionaryMessage() {
    if (dictionary.isEmpty()) {
      dictionaryMessageLabel.setText(messages.zeroEntries());
    } else if (!isDictionaryPracticeable()) {
      dictionaryMessageLabel.setText(
          messages.tooFewEntries(Constants.MIN_SIZE_PRACTICEABLE_DICTIONARY));
    } else {
      dictionaryMessageLabel.setText(messages.sufficientlyManyEntries(dictionary.size()));
    }
  }

  private boolean isDictionaryPracticeable() {
    Map<Language, Integer> wordCountsPerLanguage = new HashMap<Language, Integer>();
    for (DictionaryEntry entry : dictionary) {
      Integer wordCount = wordCountsPerLanguage.get(entry.getLanguage());
      if (wordCount == null) {
        wordCount = 0;
      } else if (wordCount == Constants.MIN_SIZE_PRACTICEABLE_DICTIONARY - 1) {
        // We're just about to increment the word count to the practiceable size, so we might as
        // well return true already.
        return true;
      }
      wordCount++;
      wordCountsPerLanguage.put(entry.getLanguage(), wordCount);
    }
    return false;
  }

  @Override
  public void onDictionaryChanged() {
    setDictionaryMessage();
  }

  @Generate(format = "com.google.gwt.i18n.rebind.format.PropertiesFormat")
  public interface LocalMessages extends Messages {
    @DefaultMessage("Hello, {0}!")
    @Description("Name of the user.")
    String welcome(String name);

    @DefaultMessage("Your Dictionary is empty.")
    @Description("2do")
    String emptyDictionary();

    @DefaultMessage("Add some words!")
    @Description("2do")
    String addWords();

    @DefaultMessage("Your Dictionary is empty -- go add some words right away!")
    @Description("2do")
    String zeroEntries();

    @DefaultMessage("You should add some more words to your Dictionary -- you need at least {0} " +
        "in a single language to practice.")
    @Description("2do")
    String tooFewEntries(int minPracticeableDictionarySize);

    @DefaultMessage("There are {0} entries in your Dictionary. Not bad, ay!")
    @Description("2do")
    String sufficientlyManyEntries(int numEntries);

    @DefaultMessage("You have yet to practice for the first time.")
    @Description("2do")
    String noPractices();
  }
}
