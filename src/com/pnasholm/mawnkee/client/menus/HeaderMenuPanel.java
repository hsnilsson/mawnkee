package com.pnasholm.mawnkee.client.menus;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;
import com.pnasholm.mawnkee.client.login.LoginInfo;

/**
 * Panel for the header menu containing 'Settings' and 'Log Out' links.
 */
public class HeaderMenuPanel extends Composite {

  interface Binder extends UiBinder<Widget, HeaderMenuPanel> {}
  private static final Binder uiBinder = GWT.create(Binder.class);
  
  @UiField Anchor signOutLink;
  
  public HeaderMenuPanel(LoginInfo loginInfo) {
    initWidget(uiBinder.createAndBindUi(this));
    signOutLink.setHref(loginInfo.getLogoutUrl());
  }
}
