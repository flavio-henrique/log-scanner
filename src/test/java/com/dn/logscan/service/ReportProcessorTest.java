package com.dn.logscan.service;

import com.dn.logscan.model.report.Report;
import org.junit.Test;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.stream.Stream;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;


public class ReportProcessorTest {

  @Test
  public void givenServerLog_whenProcessReport_thenReturnReport() throws IOException, URISyntaxException {
    // Given
    ReportProcessor reportProcessor = new ReportProcessor();
    FileImporter fileImporter = new FileImporter();
    Stream<String> logLines = fileImporter.getFileFromResource("server.log");

    // When
    Report report = reportProcessor.processReport(logLines);

    // Then
    assertEquals("872", report.getSummary().getCount());
    assertEquals("58", report.getSummary().getDuplicates());
    assertEquals("202", report.getSummary().getUnnecessary());

  }

}
