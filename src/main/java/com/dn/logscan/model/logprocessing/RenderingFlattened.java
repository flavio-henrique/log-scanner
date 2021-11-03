package com.dn.logscan.model.logprocessing;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RenderingFlattened {
  private String document;
  private String page;
  private String uid;
  private List<String> startList;
  private List<String> getList;
  private String start;
}