package com.dn.logscan.service;

import com.dn.logscan.model.logprocessing.LogLine;
import com.dn.logscan.model.logprocessing.RenderingFlattened;
import com.dn.logscan.model.report.Report;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ReportProcessor {

  public Report processReport(Stream<String> lines) {

    List<String> list = lines.collect(Collectors.toList());

    List<RenderingFlattened> renderingFlattenedList = new ArrayList<>();
    List<String> usefulSubListOfGetRendering = new ArrayList<>();
    list.forEach(s -> {
      if(s.contains("], type=request, name=getRendering}")) {
        usefulSubListOfGetRendering.add(s);
      }

      if (s.contains("Executing request startRendering with arguments")) {
        // 1 - Gather all the startRendering request made by the client
        RenderingFlattened renderingFlattened = fillUID(list, s);
        if (renderingFlattened != null) {
          renderingFlattenedList.add(fillUID(list, s));
        }
      }
    });


    // 2 - Group the flattened list taking into account the startRendering services with the same UID
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

        // 3 - Gather the getRendering services for each particular IUD
        rendering.setGetList(usefulSubListOfGetRendering.stream()
                               .filter(r -> StringUtils.equals(LogLineParser.parseOnlyUID(r).getUid(), rendering.getUid()))
                               .map(r -> LogLineParser.parseOnlyStart(r).getStart())
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

  private RenderingFlattened fillUID(List<String> list, String start) {

    LogLine logLine = LogLineParser.parseDocumentAndPageAndThreadAndUIDAndStart(start);

    RenderingFlattened.RenderingFlattenedBuilder renderingBuilder = RenderingFlattened.builder();
    renderingBuilder.document(logLine.getDocumentId())
        .start(logLine.getStart())
        .page(logLine.getPage());

    int index = list.indexOf(start);

    while (index < list.size()) {
      String lineIteration = list.get(index);
      LogLine logLineIteration = LogLineParser.parseOnlyThreadAndUID(lineIteration);
      if (lineIteration.contains("Service startRendering returned") && StringUtils.equals(logLineIteration.getThread(), logLine.getThread())) {
        renderingBuilder.uid(logLineIteration.getUid());
        return renderingBuilder.build();
      } else {
        index++;
      }
    }
    return null;
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
