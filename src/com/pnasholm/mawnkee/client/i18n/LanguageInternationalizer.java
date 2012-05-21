package com.pnasholm.mawnkee.client.i18n;

import com.google.gwt.core.client.GWT;
import com.google.gwt.i18n.client.LocalizableResource.Generate;
import com.google.gwt.i18n.client.Messages;
import com.google.inject.Singleton;
import com.pnasholm.mawnkee.shared.Language;

/**
 * Help class for internationalizing language names.
 */
@Singleton
public class LanguageInternationalizer {

  private static final LocalMessages messages = GWT.create(LocalMessages.class);

  public String getLanguage(Language language) {
    switch (language) {
      case ENGLISH:
        return messages.english();
      case SWEDISH:
        return messages.swedish();
      default:
        throw new IllegalArgumentException();
    }
  }

  @Generate(format = "com.google.gwt.i18n.rebind.format.PropertiesFormat")
  public interface LocalMessages extends Messages {
    @DefaultMessage("English")
    @Description("The name of the English language")
    String english();

    @DefaultMessage("Swedish")
    @Description("The name of the Swedish language")
    String swedish();
  }
}
