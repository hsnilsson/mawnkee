package com.pnasholm.mawnkee.client.sections.dictionary;

import java.util.List;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.inject.assistedinject.Assisted;
import com.google.web.bindery.event.shared.EventBus;
import com.pnasholm.mawnkee.client.MawnkeeUser;
import com.pnasholm.mawnkee.client.events.DictionaryChangedEvent;
import com.pnasholm.mawnkee.shared.DictionaryEntryVerifier;
import com.pnasholm.mawnkee.shared.Language;

@Singleton
public class DictionaryController implements DictionaryView.Handler {

  private EventBus eventBus;
  private DictionaryServiceAsync dictionaryService;
  private DictionaryView view;
  private List<DictionaryEntry> dictionary;

  @Inject
  public DictionaryController(
      @Assisted DictionaryView view,
      EventBus eventBus,
      DictionaryServiceAsync dictionaryService,
      MawnkeeUser user) {
    this.view = view;
    this.eventBus = eventBus;
    this.dictionaryService = dictionaryService;
    dictionary = user.getDictionary();
    view.setDictionary(dictionary);
  }

  @Override
  public void onAdd(Language language, String word, String translation, String example) {
    view.clearErrors();
    boolean isValid = true;
    // Language is always valid because it's enforced in the UI. Example is always valid because
    // it's optional. We check the word and the translation though.
    if (!DictionaryEntryVerifier.isWordValid(word)) {
      view.setWordInvalid();
      isValid = false;
    }
    if (!DictionaryEntryVerifier.isTranslationValid(translation)) {
      view.setTranslationInvalid();
      isValid = false;
    }

    if (isValid) {
      if (existsInDictionary(language, word)) {
        view.setWordAlreadyExists();
      } else {
        view.resetInputs();
        addEntry(language, word, translation, example);
      }
    }
  }

  private boolean existsInDictionary(Language language, String word) {
    for (DictionaryEntry entry : dictionary) {
      if (entry.getLanguage() == language && entry.getWord().equals(word)) {
        return true;
      }
    }
    return false;
  }

  @Override
  public void onRemove() {
    final List<DictionaryEntry> selectedEntries = view.getSelectedEntries();
    dictionaryService.removeEntries(selectedEntries, new AsyncCallback<Void>() {
      @Override
      public void onSuccess(Void ignore) {
        dictionary.removeAll(selectedEntries);
        eventBus.fireEvent(new DictionaryChangedEvent());
        view.updateCountLabel(dictionary.size());
      }

      @Override
      public void onFailure(Throwable caught) {
        // TODO: Show error in UI.
      }
    });
  }

  private void addEntry(
      final Language language, final String word, final String translation, final String example) {
    dictionaryService.addEntry(language, word, translation, example, new AsyncCallback<Void>() {
      public void onSuccess(Void ignore) {
        DictionaryEntry entry = new DictionaryEntry(language, word, translation, example);
        view.addEntry(entry);
        dictionary.add(entry);
        eventBus.fireEvent(new DictionaryChangedEvent());
        view.updateCountLabel(dictionary.size());
      }

      public void onFailure(Throwable error) {
        // TODO: Show error in UI.
      }
    });
  }
}
