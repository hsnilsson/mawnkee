package com.pnasholm.mawnkee.client;

import java.util.List;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.i18n.client.LocalizableResource.Generate;
import com.google.gwt.i18n.client.Messages;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.RootPanel;
import com.pnasholm.mawnkee.client.login.LoginInfo;
import com.pnasholm.mawnkee.client.login.LoginService;
import com.pnasholm.mawnkee.client.login.LoginServiceAsync;
import com.pnasholm.mawnkee.client.menus.HeaderMenuPanel;
import com.pnasholm.mawnkee.client.sections.dictionary.DictionaryEntry;
import com.pnasholm.mawnkee.shared.Constants;

/**
 * Main class and entry point for the app.
 */
public class Mawnkee implements EntryPoint {

  private enum Page { LOGIN, APP };

  private static final MawnkeeGinjector injector = GWT.create(MawnkeeGinjector.class);
  private static final LocalMessages messages = GWT.create(LocalMessages.class);
  private static final LoginServiceAsync loginService = GWT.create(LoginService.class);

  private static final Panel TITLE_PANEL = RootPanel.get("title");
  private static final Panel SUBTITLE_PANEL = RootPanel.get("subtitle");
  private static final Panel MAIN_PANEL = RootPanel.get("main");
  private static final Panel FOOTER_PANEL = RootPanel.get("footer");
  private static final String FOOTER_BORDER_STYLE_NAME = "footer-border";

  private Label loadingLabel = new Label(messages.loading());

  public void onModuleLoad() {
    loginService.login(GWT.getHostPageBaseURL(), new AsyncCallback<LoginInfo>() {
      @Override
      public void onSuccess(LoginInfo result) {
        if (!result.isLoggedIn()) {
          load(Page.LOGIN, result);
        } else {
          load(Page.APP, result);
        }
      }

      @Override
      public void onFailure(Throwable error) {
      }
    });
  }

  private void load(Page page, LoginInfo loginInfo) {
    // Add titles.
    TITLE_PANEL.add(new Label(messages.title()));
    SUBTITLE_PANEL.add(new Label(messages.subtitle()));
    // Add footer border now -- if it's kept in the static CSS there's an empty page only showing
    // the border before the app has loaded.
    FOOTER_PANEL.addStyleName(FOOTER_BORDER_STYLE_NAME);
    FOOTER_PANEL.add(new HTML(messages.authorCredits()));

    // Load content.
    switch (page) {
      case LOGIN:
        loadLogin(loginInfo);
        break;
      case APP:
        loadApp(loginInfo);
        break;
    }
  }

  private void loadLogin(LoginInfo loginInfo) {
    Panel mainPanel = MAIN_PANEL;
    mainPanel.addStyleName("loginPanel");
    mainPanel.add(new Anchor(messages.signIn(), loginInfo.getLoginUrl()));
    mainPanel.add(new Label(messages.usingYourGoogleAccount()));
  }

  private void loadApp(final LoginInfo loginInfo) {
    RootPanel.get("headerMenu").add(new HeaderMenuPanel(loginInfo));

    // Show loading message while loading dictionary.
    MAIN_PANEL.add(loadingLabel);
    injector.getDictionaryService().getEntries(new AsyncCallback<List<DictionaryEntry>>() {
      @Override
      public void onSuccess(List<DictionaryEntry> dictionary) {
        loadApp(loginInfo, dictionary);
      }

      @Override
      public void onFailure(Throwable caught) {
        dictionaryFailedLoading();
      }
    });
  }

  private void loadApp(LoginInfo loginInfo, List<DictionaryEntry> loadedDictionary) {
    // Remove the 'Loading...' label.
    MAIN_PANEL.remove(loadingLabel);

    MawnkeeUser user = injector.getUser();
    user.setLoginInfo(loginInfo);
    user.setDictionary(loadedDictionary);

    MAIN_PANEL.add(injector.getAppPanel());
  }

  private void dictionaryFailedLoading() {
    Label loadingFailedLabel = new Label(messages.loadingFailed());
    loadingFailedLabel.addStyleName("loadingFailed");
    MAIN_PANEL.add(loadingFailedLabel);
    MAIN_PANEL.add(new HTML(messages.tryAgainOrReport()));
  }

  @Generate(format = "com.google.gwt.i18n.rebind.format.PropertiesFormat")
  public interface LocalMessages extends Messages {
    @DefaultMessage("/ˈmʌŋki/")
    @Description("2do")
    String title();

    @DefaultMessage("Repetitio Est Mater Studiorum.")
    @Description("2do")
    String subtitle();

    @DefaultMessage("By <a href=\"mailto:" + Constants.AUTHOR_EMAIL + "\">Petter Näsholm</a>.")
    @Description("2do")
    String authorCredits();

    @DefaultMessage("Sign In")
    @Description("2do")
    String signIn();

    @DefaultMessage("using your Google account.")
    @Description("2do")
    String usingYourGoogleAccount();

    @DefaultMessage("Loading...")
    @Description("2do")
    String loading();

    @DefaultMessage("Oh noes -- loading failed!")
    @Description("2do")
    String loadingFailed();

    @DefaultMessage("<div>You can always <a href=\"javascript:location.reload()\">try</a> again."
        + " Or <a href=\"mailto:" + Constants.REPORT_EMAIL + "\">report</a> this problem.")
    @Description("2do")
    String tryAgainOrReport();
  }
}
