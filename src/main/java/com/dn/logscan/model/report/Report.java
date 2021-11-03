package com.dn.logscan.model.report;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class Report {
  private List<Rendering> rendering;
  private Summary summary;


  @Getter
  @Builder
  public static class Rendering {
    private String document;
    private String page;
    private String uid;
    private List<String> start;
    private List<String> get;
  }

  @Getter
  @Builder
  public static class Summary {
    private String count;
    private String duplicates;
    private String unnecessary;
  }
}
