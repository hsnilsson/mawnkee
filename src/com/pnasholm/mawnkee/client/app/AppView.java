package com.pnasholm.mawnkee.client.app;

import com.pnasholm.mawnkee.client.enums.AppSection;

public interface AppView {

  public interface Handler {
    void onSelect(AppSection section);
  }

  void deselect(AppSection section);
  void select(AppSection section);
}
