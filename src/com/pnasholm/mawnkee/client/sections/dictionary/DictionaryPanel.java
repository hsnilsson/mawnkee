package com.pnasholm.mawnkee.client.sections.dictionary;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.i18n.client.LocalizableResource.Generate;
import com.google.gwt.i18n.client.Messages;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.pnasholm.mawnkee.client.i18n.LanguageInternationalizer;
import com.pnasholm.mawnkee.shared.Constants;
import com.pnasholm.mawnkee.shared.Language;

/**
 * Panel for the Dictionary.
 */
@Singleton
public class DictionaryPanel extends Composite
    implements DictionaryView, ValueChangeHandler<Boolean> {

  public interface HandlerFactory {
    Handler create(DictionaryView view);
  }

  interface Binder extends UiBinder<Widget, DictionaryPanel> {}
  private static final Binder uiBinder = GWT.create(Binder.class);
  private static final LocalMessages messages = GWT.create(LocalMessages.class);

  private static final int MAX_LENGTH_WORD = 40;
  private static final int MAX_LENGTH_TRANSLATION = 40;
  private static final int MAX_LENGTH_EXAMPLE = 100;

  private static final int CHECKBOX_COLUMN = 0;
  private static final int LANGUAGE_COLUMN = 1;
  private static final int WORD_COLUMN = 2;
  private static final int TRANSLATION_COLUMN = 3;
  private static final int EXAMPLE_COLUMN = 4;

  interface Style extends CssResource {

    String errorInput();
  }

  private Handler handler;
  private LanguageInternationalizer languageInternationalizer;
  private CheckBox selectAllOrNoEntriesCheckBox;
  private List<CheckBox> entryCheckBoxes;
  private EntryCheckBoxValueChangeHandler entryCheckBoxValueChangeHandler;
  // This class has its own view of the dictionary for presentation reasons (i.e., it's not
  // necessarily the case that index i in this dictionary and the singleton dictionary represent the
  // same entry).
  private List<DictionaryEntry> dictionary;

  @UiField Label quickAddTitleLabel;
  @UiField Label languageLabel;
  @UiField ListBox languageSelector;
  @UiField Label wordLabel;
  @UiField TextBox wordInput;
  @UiField Label wordErrorLabel;
  @UiField Label translationLabel;
  @UiField TextBox translationInput;
  @UiField Label translationErrorLabel;
  @UiField Label exampleLabel;
  @UiField TextBox exampleInput;
  @UiField Button addButton;

  @UiField Label entriesTitleLabel;
  @UiField Button removeSelectedButton;
  @UiField Label entriesCountLabel;
  @UiField FlexTable entriesTable;

  @UiField Style style;

  @Inject
  public DictionaryPanel(
      HandlerFactory handlerFactory,
      LanguageInternationalizer languageInternationalizer) {
    this.languageInternationalizer = languageInternationalizer;
    initWidget(uiBinder.createAndBindUi(this));
    entryCheckBoxes = new ArrayList<CheckBox>();
    entryCheckBoxValueChangeHandler = new EntryCheckBoxValueChangeHandler();
    dictionary = new ArrayList<DictionaryEntry>();
    initQuickAdd();
    initEntries();
    handler = handlerFactory.create(this);
  }

  private void initQuickAdd() {
    quickAddTitleLabel.setText(messages.quickAddTitle());

    languageLabel.setText(messages.language());
    for (Language language : Language.values()) {
      languageSelector.addItem(languageInternationalizer.getLanguage(language));
    }
    // TODO: Set selected language to whatever was used for the last added word.
    languageSelector.setItemSelected(0, true);

    wordLabel.setText(messages.word());
    wordInput.setMaxLength(MAX_LENGTH_WORD);

    translationLabel.setText(messages.translation());
    translationInput.setMaxLength(MAX_LENGTH_TRANSLATION);

    exampleLabel.setText(messages.example());
    exampleInput.setMaxLength(MAX_LENGTH_EXAMPLE);

    addButton.setText(messages.add());
  }

  private void initEntries() {
    entriesTitleLabel.setText(messages.entriesTitle());
    removeSelectedButton.setText(messages.removeSelected());
    for (int i = 0; i < 5; i++) {
      entriesTable.getCellFormatter().addStyleName(0, i, "dictionaryEntriesHeaderCell");
    }
    entriesTable.getColumnFormatter().addStyleName(
        CHECKBOX_COLUMN, "dictionaryEntriesCheckBoxColumn");
    entriesTable.getColumnFormatter().addStyleName(
        LANGUAGE_COLUMN, "dictionaryEntriesLanguageColumn");
    entriesTable.getColumnFormatter().addStyleName(WORD_COLUMN, "dictionaryEntriesWordColumn");
    entriesTable.getColumnFormatter().addStyleName(
        TRANSLATION_COLUMN, "dictionaryEntriesTranslationColumn");
    selectAllOrNoEntriesCheckBox = new CheckBox();
    selectAllOrNoEntriesCheckBox.addValueChangeHandler(this);
    entriesTable.setWidget(0, CHECKBOX_COLUMN, selectAllOrNoEntriesCheckBox);
    entriesTable.setText(0, LANGUAGE_COLUMN, messages.languageColumnHeader());
    entriesTable.setText(0, WORD_COLUMN, messages.wordColumnHeader());
    entriesTable.setText(0, TRANSLATION_COLUMN, messages.translationColumnHeader());
    entriesTable.setText(0, EXAMPLE_COLUMN, messages.exampleColumnHeader());
  }

  @UiHandler("addButton")
  void onAddButtonClick(ClickEvent event) {
    selectAllOrNoEntriesCheckBox.setValue(false);
    handler.onAdd(
        Language.values()[languageSelector.getSelectedIndex()],
        wordInput.getText(),
        translationInput.getText(),
        exampleInput.getText());
  }

  @UiHandler("removeSelectedButton")
  void onRemoveSelectedButtonClick(ClickEvent event) {
    handler.onRemove();

    selectAllOrNoEntriesCheckBox.setValue(false);
    for (int i = entryCheckBoxes.size() - 1; i >= 0; i--) {
      if (entryCheckBoxes.get(i).getValue()) {
        entriesTable.removeRow(i + 1);
        entryCheckBoxes.remove(i);
        dictionary.remove(i);
      }
    }
  }

  @Override
  public void resetInputs() {
    // Don't touch the language selector here -- we want it to reflect the same language as before.
    wordInput.setText(Constants.EMPTY_STRING);
    translationInput.setText(Constants.EMPTY_STRING);
    exampleInput.setText(Constants.EMPTY_STRING);
  }

  @Override
  public void clearErrors() {
    wordInput.removeStyleName(style.errorInput());
    translationInput.removeStyleName(style.errorInput());
    wordErrorLabel.setText(Constants.EMPTY_STRING);
    translationErrorLabel.setText(Constants.EMPTY_STRING);
  }

  @Override
  public void setWordInvalid() {
    wordInput.addStyleName(style.errorInput());
    wordErrorLabel.setText(messages.requiredField());
  }

  @Override
  public void setWordAlreadyExists() {
    wordInput.addStyleName(style.errorInput());
    wordErrorLabel.setText(messages.wordAlreadyExists());
  }

  @Override
  public void setTranslationInvalid() {
    translationInput.addStyleName(style.errorInput());
    translationErrorLabel.setText(messages.requiredField());
  }

  @Override
  public void addEntry(DictionaryEntry entry) {
    int row = entriesTable.getRowCount();
    CheckBox entryCheckBox = new CheckBox();
    entryCheckBox.addValueChangeHandler(entryCheckBoxValueChangeHandler);
    entriesTable.setWidget(row, CHECKBOX_COLUMN, entryCheckBox);
    entryCheckBoxes.add(entryCheckBox);
    entriesTable.setText(
        row, LANGUAGE_COLUMN, languageInternationalizer.getLanguage(entry.getLanguage()));
    entriesTable.setText(row, WORD_COLUMN, entry.getWord());
    entriesTable.setText(row, TRANSLATION_COLUMN, entry.getTranslation());
    entriesTable.setText(row, EXAMPLE_COLUMN, entry.getExample());
    dictionary.add(entry);
  }

  @Override
  public void setDictionary(List<DictionaryEntry> entries) {
    entryCheckBoxes.clear();
    dictionary.clear();
    for (DictionaryEntry entry : entries) {
      addEntry(entry);
    }
    updateCountLabel(entries.size());
  }

  @Override
  public void updateCountLabel(int dictionarySize) {
    entriesCountLabel.setText(messages.entriesCount(dictionarySize));
  }

  @Override
  public List<DictionaryEntry> getSelectedEntries() {
    List<DictionaryEntry> selectedEntries = new ArrayList<DictionaryEntry>();
    for (int i = 0; i < entryCheckBoxes.size(); i++) {
      if (entryCheckBoxes.get(i).getValue()) {
        selectedEntries.add(dictionary.get(i));
      }
    }
    return selectedEntries;
  }

  /*private void enableQuickAdd() {
    languageSelector.setEnabled(true);
    wordInput.setEnabled(true);
    translationInput.setEnabled(true);
    exampleInput.setEnabled(true);
    addButton.setEnabled(true);
  }*/

  @Override
  public void onValueChange(ValueChangeEvent<Boolean> event) {
    for (CheckBox entryCheckBox : entryCheckBoxes) {
      entryCheckBox.setValue(event.getValue());
    }
  }

  private class EntryCheckBoxValueChangeHandler implements ValueChangeHandler<Boolean> {

    @Override
    public void onValueChange(ValueChangeEvent<Boolean> event) {
      if (!event.getValue() && selectAllOrNoEntriesCheckBox.getValue()) {
        selectAllOrNoEntriesCheckBox.setValue(false);
      }
    }
  }

  @Generate(format = "com.google.gwt.i18n.rebind.format.PropertiesFormat")
  public interface LocalMessages extends Messages {
    @DefaultMessage("Quick Add")
    @Description("Title for Quick Add section.")
    String quickAddTitle();

    @DefaultMessage("Language*")
    @Description("2do")
    String language();

    @DefaultMessage("Word*")
    @Description("2do")
    String word();

    @DefaultMessage("Translation*")
    @Description("2do")
    String translation();

    @DefaultMessage("Example usage")
    @Description("2do")
    String example();

    @DefaultMessage("Add")
    @Description("Button text for adding a new dictionary entry.")
    String add();

    @DefaultMessage("Field is required.")
    @Description("2do")
    String requiredField();

    @DefaultMessage("That''s already in the dictionary for the chosen language.")
    @Description("2do")
    String wordAlreadyExists();

    @DefaultMessage("Entries")
    @Description("Title for Entries section.")
    String entriesTitle();

    @DefaultMessage("Remove selected")
    @Description("Button text for removing selected dictionary entries.")
    String removeSelected();

    @DefaultMessage("Number of entries: {0}")
    @Description("2do")
    String entriesCount(int count);

    @DefaultMessage("Language")
    @Description("2do")
    String languageColumnHeader();

    @DefaultMessage("Word")
    @Description("2do")
    String wordColumnHeader();

    @DefaultMessage("Translation")
    @Description("2do")
    String translationColumnHeader();

    @DefaultMessage("Example usage")
    @Description("2do")
    String exampleColumnHeader();
  }
}
