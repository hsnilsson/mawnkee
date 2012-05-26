package com.pnasholm.mawnkee.client.app;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.pnasholm.mawnkee.client.enums.AppSection;
import com.pnasholm.mawnkee.client.sections.dashboard.DashboardPanel;
import com.pnasholm.mawnkee.client.sections.dictionary.DictionaryPanel;
import com.pnasholm.mawnkee.client.sections.practice.PracticePanel;

/**
 * Panel for the logged in app.
 */
@Singleton
public class AppPanel extends Composite implements AppView {

  public interface HandlerFactory {
    Handler create(AppView view);
  }

  interface Binder extends UiBinder<Widget, AppPanel> {}
  private static final Binder uiBinder = GWT.create(Binder.class);

  // Menu widgets.
  @UiField Label dashboardLabel;
  @UiField Label dictionaryLabel;
  @UiField Label practiceLabel;
  @UiField UiStyle style;
  // App section widgets.
  @UiField(provided = true) DashboardPanel dashboardPanel;
  @UiField(provided = true) DictionaryPanel dictionaryPanel;
  @UiField(provided = true) PracticePanel practicePanel;

  interface UiStyle extends CssResource {
    String selected();
  }

  private Handler handler;

  @Inject
  public AppPanel(
      HandlerFactory handlerFactory,
      DashboardPanel dashboardPanel,
      DictionaryPanel dictionaryPanel,
      PracticePanel practicePanel) {
    this.dashboardPanel = dashboardPanel;
    this.dictionaryPanel = dictionaryPanel;
    this.practicePanel = practicePanel;
    for (AppSection section : AppSection.values()) {
      if (section != AppSection.DASHBOARD) {
        hideSection(section);
      }
    }
    initWidget(uiBinder.createAndBindUi(this));
    handler = handlerFactory.create(this);
  }

  @UiHandler("dashboardLabel")
  public void onDashboardClick(ClickEvent event) {
    handler.onSelect(AppSection.DASHBOARD);
  }

  @UiHandler("dictionaryLabel")
  void onDictionaryClick(ClickEvent event) {
    handler.onSelect(AppSection.DICTIONARY);
  }

  @UiHandler("practiceLabel")
  void onPracticeClick(ClickEvent event) {
    handler.onSelect(AppSection.PRACTICE);
  }

  @Override
  public void deselect(AppSection section) {
    getLabel(section).removeStyleName(style.selected());
    hideSection(section);
  }

  @Override
  public void select(AppSection section) {
    getLabel(section).addStyleName(style.selected());
    showSection(section);
  }

  private void hideSection(AppSection section) {
    getPanel(section).setVisible(false);
  }

  private void showSection(AppSection section) {
    getPanel(section).setVisible(true);
  }

  private Widget getLabel(AppSection section) {
    switch (section) {
      case DASHBOARD:
        return dashboardLabel;
      case DICTIONARY:
        return dictionaryLabel;
      case PRACTICE:
        return practiceLabel;
      default:
        throw new IllegalArgumentException();
    }
  }

  private Widget getPanel(AppSection section) {
    switch (section) {
      case DASHBOARD:
        return dashboardPanel;
      case DICTIONARY:
        return dictionaryPanel;
      case PRACTICE:
        return practicePanel;
      default:
        throw new IllegalArgumentException();
    }
  }
}
