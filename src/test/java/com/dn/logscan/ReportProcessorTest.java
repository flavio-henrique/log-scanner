package com.dn.logscan;

import com.dn.logscan.model.report.Report;
import com.dn.logscan.service.FileImporter;
import com.dn.logscan.service.ReportProcessor;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;
import java.util.stream.Stream;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class ReportProcessorTest {

  @Test
  public void givenFilePath_whenUsingFilesLines_thenReportRendering() throws IOException, URISyntaxException {
    // Given
    ReportProcessor reportProcessor = new ReportProcessor();
    FileImporter fileImporter = new FileImporter();
    Stream<String> logLines = fileImporter.getFileFromResource("server.log");

    // When
    Report report = reportProcessor.processReport(logLines);

    // Then
    assertNotNull(report);
    assertEquals("872", report.getSummary().getCount());
    assertEquals("58", report.getSummary().getDuplicates());
    assertEquals("202", report.getSummary().getUnnecessary());

    ObjectMapper mapper = new ObjectMapper();
    try {
      String json = mapper.writeValueAsString(report);
      System.out.println("ResultingJSONstring = " + json);

      URL url = getClass().getClassLoader().getResource("report.json");
      URI uri = Objects.requireNonNull(url).toURI();
      Path path = Paths.get(uri);

      Files.write(path, json.getBytes(StandardCharsets.UTF_8));

      Files.write(path, json.getBytes(StandardCharsets.UTF_8));
    } catch (JsonProcessingException e) {
      e.printStackTrace();
    }
  }


}
