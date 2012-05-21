package com.pnasholm.mawnkee.client;

import java.util.List;

import com.google.inject.Singleton;
import com.pnasholm.mawnkee.client.login.LoginInfo;
import com.pnasholm.mawnkee.client.sections.dictionary.DictionaryEntry;

@Singleton
public class MawnkeeUser {

  private LoginInfo loginInfo;
  private List<DictionaryEntry> dictionary;

  public LoginInfo getLoginInfo() {
    return loginInfo;
  }

  public void setLoginInfo(LoginInfo loginInfo) {
    this.loginInfo = loginInfo;
  }

  public List<DictionaryEntry> getDictionary() {
    return dictionary;
  }

  public void setDictionary(List<DictionaryEntry> dictionary) {
    this.dictionary = dictionary;
  }
}
