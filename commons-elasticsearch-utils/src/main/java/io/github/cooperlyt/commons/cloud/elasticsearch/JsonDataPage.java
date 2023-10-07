package io.github.cooperlyt.commons.cloud.elasticsearch;


import com.fasterxml.jackson.annotation.JsonRawValue;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.github.cooperlyt.commons.cloud.serialize.JsonRawSerialize;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Schema(title = "Elasticsearch分页")
public class JsonDataPage extends DataPage<String>{

  public JsonDataPage(List<String> data, boolean isExactResult, int pageNum, int pageSize, long total) {
    super(data, isExactResult, pageNum, pageSize, total);
  }

  @Schema(title = "数据", type = "object")
  @Override
  @JsonRawValue
  public List<String> getList(){
    return super.getList();
  }
}
