package com.pnasholm.mawnkee.client.app;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.inject.assistedinject.Assisted;
import com.pnasholm.mawnkee.client.enums.AppSection;

/**
 * Top-level controller responsible for the entire app when the user is logged in.
 */
@Singleton
public class AppController implements AppView.Handler {

  private static final AppSection DEFAULT_SECTION = AppSection.DASHBOARD;

  private AppSection currentSection;
  private AppView view;

  @Inject
  public AppController(@Assisted AppView view) {
    this.view = view;
    currentSection = DEFAULT_SECTION;
  }

  @Override
  public void onSelect(AppSection section) {
    if (currentSection != section) {
      view.deselect(currentSection);
      view.select(section);
      currentSection = section;
    }
  }
}
