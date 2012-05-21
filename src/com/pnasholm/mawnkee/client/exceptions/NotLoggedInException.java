package com.pnasholm.mawnkee.client.exceptions;

import java.io.Serializable;

public class NotLoggedInException extends Exception implements Serializable {

  private static final long serialVersionUID = -514729278139554427L;

  public NotLoggedInException() {
    super();
  }

  public NotLoggedInException(String message) {
    super(message);
  }

}
