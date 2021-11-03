package com.dn.logscan.service;

import com.dn.logscan.model.logprocessing.LogLine;
import org.apache.commons.lang3.StringUtils;

public class LogLineParser {

  public static LogLine parseDocumentAndPageAndThreadAndUIDAndStart(String logLine) {
    LogLine.LogLineBuilder builder = LogLine.builder();

    if (isNotValidLog(logLine)) {
      return builder.build();
    }

    setUID(logLine, builder);

    // set document and page
    if (logLine.contains("Executing request startRendering with arguments [")) {
      String[] s = logLine.split("Executing request startRendering with arguments \\[")[1].split("]")[0].split(",");
      String document = StringUtils.trim(s[0]);
      String page = StringUtils.trim(s[1]);
      builder.documentId(document).page(page);
    }

    // set thread
    builder.thread(logLine.split("\\[")[1].split("]")[0]);

    // set start
    builder.start(logLine.substring(0, 23));

    return builder.build();
  }

  public static LogLine parseOnlyThreadAndUID(String logLine) {

    LogLine.LogLineBuilder builder = LogLine.builder();

    if (isNotValidLog(logLine)) {
      return builder.build();
    }

    // set uid
    setUID(logLine, builder);

    // set thread
    builder.thread(logLine.split("\\[")[1].split("]")[0]);

    return builder.build();
  }

  public static LogLine parseOnlyStartAndUID(String logLine) {

    LogLine.LogLineBuilder builder = LogLine.builder();

    if (isNotValidLog(logLine)) {
      return builder.build();
    }

    // set start
    builder.start(logLine.substring(0, 23));

    // set uid
    setUID(logLine, builder);

    return builder.build();
  }

  private static boolean isNotValidLog(String logLine) {
    try {
      if (logLine.startsWith("Caused by: ") || logLine.startsWith("\t... ")) {
        return true;
      }
      return !"[".equals(String.valueOf(logLine.charAt(24)));
    } catch (Exception e) {
      return true;
    }
  }

  private static void setUID(String logLine, LogLine.LogLineBuilder builder) {
    if (logLine.contains("Checking to add command to queue: { RenderingCommand - uid: ")) {
      String slice = logLine.split("Checking to add command to queue: \\{ RenderingCommand - uid: ")[1];
      builder.uid(slice.substring(0, slice.length() - 2));
    } else if (logLine.contains("], type=request, name=getRendering")) {
      builder.uid(logLine.split("], type=request, name=getRendering")[0].split(
          "Processing command object: \\{arguments=\\[")[1]);
    } else if (logLine.contains("Service startRendering returned")) {
      builder.uid(logLine.split("Service startRendering returned ")[1]);
    }
  }

}
