package com.pnasholm.mawnkee.client;

import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.inject.client.AbstractGinModule;
import com.google.gwt.inject.client.assistedinject.GinFactoryModuleBuilder;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.google.inject.name.Named;
import com.google.web.bindery.event.shared.EventBus;
import com.google.web.bindery.event.shared.SimpleEventBus;
import com.pnasholm.mawnkee.client.app.AppController;
import com.pnasholm.mawnkee.client.app.AppPanel;
import com.pnasholm.mawnkee.client.app.AppView;
import com.pnasholm.mawnkee.client.sections.dictionary.DictionaryController;
import com.pnasholm.mawnkee.client.sections.dictionary.DictionaryPanel;
import com.pnasholm.mawnkee.client.sections.dictionary.DictionaryService;
import com.pnasholm.mawnkee.client.sections.dictionary.DictionaryView;
import com.pnasholm.mawnkee.client.sections.practice.PracticeController;
import com.pnasholm.mawnkee.client.sections.practice.PracticePanel;
import com.pnasholm.mawnkee.client.sections.practice.PracticeView;

public class MawnkeeGinModule extends AbstractGinModule {

  protected void configure() {
    bind(EventBus.class).to(SimpleEventBus.class).in(Singleton.class);
    bind(DictionaryService.class).in(Singleton.class);

    install(new GinFactoryModuleBuilder()
        .implement(AppView.Handler.class, AppController.class)
        .build(AppPanel.HandlerFactory.class));
    install(new GinFactoryModuleBuilder()
        .implement(DictionaryView.Handler.class, DictionaryController.class)
        .build(DictionaryPanel.HandlerFactory.class));
    install(new GinFactoryModuleBuilder()
        .implement(PracticeView.Handler.class, PracticeController.class)
        .build(PracticePanel.HandlerFactory.class));
  }

  @Provides
  @Singleton
  @Named("oneDecimal")
  NumberFormat providesOneDecimalFormatter() {
    return NumberFormat.getFormat(".000");
  }
}
