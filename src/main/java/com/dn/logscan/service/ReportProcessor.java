package com.dn.logscan.service;

import com.dn.logscan.model.logprocessing.LogLine;
import com.dn.logscan.model.logprocessing.RenderingFlattened;
import com.dn.logscan.model.report.Report;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ReportProcessor {

  public Report processReport(Stream<String> lines) {

    List<String> list = lines.collect(Collectors.toList());

    List<RenderingFlattened> renderingFlattenedList = new ArrayList<>();
    List<LogLine> usefulSubListOfGetRendering = new ArrayList<>();

    list.forEach(s -> {
      if(s.contains("], type=request, name=getRendering}")) {
        // This sublist will be used latter
        usefulSubListOfGetRendering.add(LogLineParser.parseOnlyStartAndUID(s));
      }

      if (s.contains("Executing request startRendering with arguments")) {
        // Gather all the startRendering request made by the client
        retrieveRendering(list, s).ifPresent(renderingFlattenedList::add);
      }
    });


    // Group the flattened list taking into account the startRendering services with the same UID
    List<RenderingFlattened> renderingGroupedList = new ArrayList<>();
    renderingFlattenedList.forEach(rendering -> {
      Optional<RenderingFlattened> renderingOptional = renderingGroupedList.stream()
          .filter(renderingFlat -> StringUtils.equals(renderingFlat.getUid(), rendering.getUid())
              && StringUtils.equals(renderingFlat.getDocument(), rendering.getDocument())
              && StringUtils.equals(renderingFlat.getPage(), rendering.getPage()))
          .findFirst();

      if (renderingOptional.isPresent()) {
        renderingOptional.get().getStartList().add(rendering.getStart());
      } else {
        List<String> starts = new ArrayList<>();
        starts.add(rendering.getStart());
        rendering.setStartList(starts);

        // Gather the getRendering services for each particular IUD
        rendering.setGetList(usefulSubListOfGetRendering.stream()
                               .filter(r -> StringUtils.equals(r.getUid(), rendering.getUid()))
                               .map(LogLine::getStart)
                               .collect(Collectors.toList()));
        renderingGroupedList.add(rendering);
      }
    });

    List<Report.Rendering> result = renderingGroupedList.stream()
        .map(rendering -> Report.Rendering.builder()
            .get(rendering.getGetList())
            .start(rendering.getStartList())
            .uid(rendering.getUid())
            .page(rendering.getPage())
            .document(rendering.getDocument())
            .build())
        .collect(Collectors.toList());

    return Report.builder()
        .rendering(result)
        .summary(buildSummary(result))
        .build();
  }

  private Optional<RenderingFlattened> retrieveRendering(List<String> list, String start) {

    LogLine logLine = LogLineParser.parseDocumentAndPageAndThreadAndUIDAndStart(start);

    RenderingFlattened.RenderingFlattenedBuilder renderingBuilder = RenderingFlattened.builder();
    renderingBuilder.document(logLine.getDocumentId())
        .start(logLine.getStart())
        .page(logLine.getPage());

    int index = list.indexOf(start);
    // Loop to link the startRendering to its result by following the same thread
    while (index < list.size()) {
      String lineIteration = list.get(index);
      LogLine logLineIteration = LogLineParser.parseOnlyThreadAndUID(lineIteration);
      if (lineIteration.contains("Service startRendering returned") && StringUtils.equals(logLineIteration.getThread(), logLine.getThread())) {
        renderingBuilder.uid(logLineIteration.getUid());
        return Optional.ofNullable(renderingBuilder.build());
      } else {
        index++;
      }
    }
    return Optional.empty();
  }


  private Report.Summary buildSummary(List<Report.Rendering> renderingList) {
    return Report.Summary.builder()
        .count(String.valueOf(renderingList.size()))
        .duplicates(String.valueOf(renderingList.stream()
                                       .filter(rendering -> rendering.getStart() != null && rendering.getStart().size() > 1)
                                       .count()))
        .unnecessary(String.valueOf(renderingList.stream()
                                        .filter(rendering -> rendering.getGet() == null || rendering.getGet().isEmpty())
                                        .count()))
        .build();
  }
}
