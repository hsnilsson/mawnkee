package com.pnasholm.mawnkee.client.sections.dictionary;

import java.util.List;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.pnasholm.mawnkee.shared.Language;

public interface DictionaryServiceAsync {
  public void getEntries(AsyncCallback<List<DictionaryEntry>> callback);
  public void addEntry(
      Language language,
      String word,
      String translation,
      String example,
      AsyncCallback<Void> callback);
  public void removeEntries(List<DictionaryEntry> entries, AsyncCallback<Void> callback);
}
