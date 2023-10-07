package io.github.cooperlyt.commons.cloud.elasticsearch;

import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.core.search.TotalHits;
import co.elastic.clients.elasticsearch.core.search.TotalHitsRelation;
import co.elastic.clients.json.JsonData;
import co.elastic.clients.json.jackson.JacksonJsonpGenerator;
import co.elastic.clients.json.jackson.JacksonJsonpMapper;
import com.fasterxml.jackson.core.JsonFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Slf4j
public class ElasticsearchUtils {

  private static final Pattern snakeJsonLabelPattern = Pattern.compile("\"([A-Za-z0-9]*_{1,}\\w*)\":");

  private static String snakeToCamel(String source){
    if (!StringUtils.hasText(source) || source.trim().toCharArray()[0] == '_') {
      return source;
    }
    String[] split = source.split("_");
    StringBuilder sb = new StringBuilder(source.length());
    boolean isFirst = true;
    for (String s : split) {
      if (!StringUtils.hasText(s))
        break;
      char[] chars = s.trim().toCharArray();

      if (isFirst){
//        if(chars[0] >= 'A' && chars[0] <='Z'){
//          chars[0] +=32;
//        }
        isFirst = false;
      }else{
        if(chars[0] >='a' && chars[0] <= 'z'){
          chars[0] -=32;
        }
      }
      sb.append(chars);
    }
    return sb.toString();
  }

  private static Map<String, List<String>> camelHighlight(Map<String, List<String>> highlight){
    Map<String, List<String>> result = new HashMap<>(highlight.size());
    highlight.forEach((k,v) -> result.put(snakeToCamel(k),v));
    return result;
  }

  public static String camelJson(String source){

    if (!StringUtils.hasText(source)) {
      return "{}";
    }

    StringBuilder sb = new StringBuilder(source.length());
    Matcher matcher = snakeJsonLabelPattern.matcher(source);
    while (matcher.find()){
      matcher.appendReplacement(sb,"\"" + snakeToCamel(matcher.group(1)) + "\":");
    }
    matcher.appendTail(sb);
    return sb.toString();
  }

  public static String toJson(SearchResponse<JsonData> response) {
    final StringWriter writer = new StringWriter();
    try (final JacksonJsonpGenerator generator = new JacksonJsonpGenerator(new JsonFactory().createGenerator(writer))) {
      response.serialize(generator, new JacksonJsonpMapper());
    } catch (IOException e) {
      log.error("elasticsearch error", e);
      throw new IllegalArgumentException(e);
    }
    return writer.toString();
  }

  public static DataPage<ResultHint> toDataPage(SearchResponse<JsonData> response ,
                                                int pageNum, int pageSize){
    return toDataPage(response,pageNum,pageSize,true);
  }

  public static DataPage<ResultHint> toDataPage(SearchResponse<JsonData> response ,
                                                int pageNum, int pageSize, boolean snakeToCamel){
    log.debug("--elasticsearch result---");
    log.debug(toJson(response));
    TotalHits total = response.hits().total();
    boolean isExactResult = total.relation() == TotalHitsRelation.Eq;
    List<ResultHint> data = response.hits().hits().stream().map(h ->
            ResultHint.builder()
                .highlight(snakeToCamel ? camelHighlight(h.highlight()) : h.highlight())
                .source(snakeToCamel ? camelJson(h.source().toJson().toString()) : h.source().toJson().toString())
                .build())
        .collect(Collectors.toList());
    return new DataPage<>(data,isExactResult,pageNum,pageSize,total.value());
  }

}
