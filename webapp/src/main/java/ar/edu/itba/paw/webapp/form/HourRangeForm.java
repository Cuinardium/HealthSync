package ar.edu.itba.paw.webapp.form;

import ar.edu.itba.paw.models.ThirtyMinuteBlock;

public class HourRangeForm {
  private ThirtyMinuteBlock from;
  private ThirtyMinuteBlock to;

  public HourRangeForm(ThirtyMinuteBlock from, ThirtyMinuteBlock to) {
    this.from = from;
    this.to = to;
  }

  public ThirtyMinuteBlock getFrom() {
    return from;
  }

  public void setFrom(ThirtyMinuteBlock from) {
    this.from = from;
  }

  public ThirtyMinuteBlock getTo() {
    return to;
  }

  public void setTo(ThirtyMinuteBlock to) {
    this.to = to;
  }
}
