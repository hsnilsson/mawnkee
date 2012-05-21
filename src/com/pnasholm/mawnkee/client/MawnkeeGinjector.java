package com.pnasholm.mawnkee.client;

import com.google.gwt.inject.client.GinModules;
import com.google.gwt.inject.client.Ginjector;
import com.pnasholm.mawnkee.client.app.AppPanel;
import com.pnasholm.mawnkee.client.sections.dictionary.DictionaryServiceAsync;

@GinModules(MawnkeeGinModule.class)
public interface MawnkeeGinjector extends Ginjector {

  AppPanel getAppPanel();
  DictionaryServiceAsync getDictionaryService();
  MawnkeeUser getUser();
}
