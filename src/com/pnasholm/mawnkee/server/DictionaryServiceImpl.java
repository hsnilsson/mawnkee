package com.pnasholm.mawnkee.server;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.jdo.JDOHelper;
import javax.jdo.PersistenceManager;
import javax.jdo.PersistenceManagerFactory;
import javax.jdo.Query;

import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserServiceFactory;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.pnasholm.mawnkee.client.exceptions.NotLoggedInException;
import com.pnasholm.mawnkee.client.sections.dictionary.DictionaryEntry;
import com.pnasholm.mawnkee.client.sections.dictionary.DictionaryService;
import com.pnasholm.mawnkee.shared.Language;

public class DictionaryServiceImpl extends RemoteServiceServlet implements DictionaryService {

  private static final long serialVersionUID = -2775041582201104992L;
  private static final Logger LOG = Logger.getLogger(DictionaryServiceImpl.class.getName());
  private static final PersistenceManagerFactory PMF =
      JDOHelper.getPersistenceManagerFactory("transactions-optional");

  @Override
  @SuppressWarnings("unchecked")
  public List<DictionaryEntry> getEntries() throws NotLoggedInException {
    checkLoggedIn();
    PersistenceManager pm = getPersistenceManager();
    try {
      Query query = pm.newQuery(PersistentDictionaryEntry.class, "user == u");
      query.declareParameters("com.google.appengine.api.users.User u");
      query.setOrdering("word");
      return convertToClientEntries((List<PersistentDictionaryEntry>) query.execute(getUser()));
    } finally {
      pm.close();
    }
  }

  @Override
  public void addEntry(Language language, String word, String translation, String example)
      throws NotLoggedInException {
    checkLoggedIn();
    PersistenceManager pm = getPersistenceManager();
    try {
      pm.makePersistent(
          new PersistentDictionaryEntry(getUser(), language, word, translation, example));
      LOG.log(Level.INFO, "user " + getUser().getUserId() + " added 1 entries");
    } finally {
      pm.close();
    }
  }

  @Override
  @SuppressWarnings("unchecked")
  public void removeEntries(List<DictionaryEntry> entries) throws NotLoggedInException {
    checkLoggedIn();
    PersistenceManager pm = getPersistenceManager();
    try {
      Query q = pm.newQuery(PersistentDictionaryEntry.class, "user == u");
      q.declareParameters("com.google.appengine.api.users.User u");
      List<PersistentDictionaryEntry> existingEntries =
          (List<PersistentDictionaryEntry>) q.execute(getUser());
      Set<String> encodedEntries = encodeEntries(entries);
      for (PersistentDictionaryEntry entry : existingEntries) {
        if (encodedEntries.contains(encodeEntry(entry))) {
          pm.deletePersistent(entry);
        }
      }
      LOG.log(
          Level.INFO, "user " + getUser().getUserId() + " deleted " + entries.size() + " entries");
    } finally {
      pm.close();
    }
  }

  private Set<String> encodeEntries(List<DictionaryEntry> entries) {
    Set<String> set = new HashSet<String>();
    for (DictionaryEntry entry : entries) {
      set.add(encodeEntry(entry));
    }
    return set;
  }

  private String encodeEntry(DictionaryEntry entry) {
    return entry.getLanguage() + "." + entry.getWord();
  }

  private String encodeEntry(PersistentDictionaryEntry entry) {
    return entry.getLanguage() + "." + entry.getWord();
  }

  private void checkLoggedIn() throws NotLoggedInException {
    if (getUser() == null) {
      throw new NotLoggedInException("Not logged in.");
    }
  }

  private User getUser() {
    return UserServiceFactory.getUserService().getCurrentUser();
  }

  private PersistenceManager getPersistenceManager() {
    return PMF.getPersistenceManager();
  }

  private List<DictionaryEntry> convertToClientEntries(List<PersistentDictionaryEntry> entries) {
    List<DictionaryEntry> clientEntries = new ArrayList<DictionaryEntry>(entries.size());
    for (PersistentDictionaryEntry entry : entries) {
      clientEntries.add(new DictionaryEntry(
          entry.getLanguage(), entry.getWord(), entry.getTranslation(), entry.getExample()));
    }
    return clientEntries;
  }
}
