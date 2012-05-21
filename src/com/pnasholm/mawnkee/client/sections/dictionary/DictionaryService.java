package com.pnasholm.mawnkee.client.sections.dictionary;

import java.util.List;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import com.pnasholm.mawnkee.client.exceptions.NotLoggedInException;
import com.pnasholm.mawnkee.shared.Language;

@RemoteServiceRelativePath("dictionary")
public interface DictionaryService extends RemoteService {
  public List<DictionaryEntry> getEntries() throws NotLoggedInException;
  public void addEntry(Language language, String word, String translation, String example)
      throws NotLoggedInException;
  public void removeEntries(List<DictionaryEntry> entries) throws NotLoggedInException;
}
