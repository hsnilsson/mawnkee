package com.pnasholm.mawnkee.client.events;

import com.google.web.bindery.event.shared.Event;

public class DictionaryChangedEvent extends Event<DictionaryChangedHandler> {

  public static final Type<DictionaryChangedHandler> TYPE = new Type<DictionaryChangedHandler>();

  @Override
  public Type<DictionaryChangedHandler> getAssociatedType() {
    return TYPE;
  }

  @Override
  protected void dispatch(DictionaryChangedHandler handler) {
    handler.onDictionaryChanged();
  }
}
