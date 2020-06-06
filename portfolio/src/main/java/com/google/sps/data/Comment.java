package com.google.sps.data;

import java.util.Date;

public final class Comment {

  private final long id;
  private final String comment;
  private final String email;
  private final Date timestamp;

  /**
   * @param {!id} id The unique identifier for each comment.
   * @param {!String} comment The comment.
   * @param {!String} email The user email.
   * @param {!Date} timeStamp The time at the instant of initiation.
   */
  public Comment(long id, String comment, String email,  Date timestamp) {
    this.id = id;
    this.comment = comment;
    this.email = email;
    this.timestamp = timestamp;
  }
}