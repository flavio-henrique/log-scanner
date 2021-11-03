package com.dn.logscan.model.logprocessing;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class LogLine {
  private String documentId;
  private String page;
  private String uid;
  private String start;
  private String thread;
}
