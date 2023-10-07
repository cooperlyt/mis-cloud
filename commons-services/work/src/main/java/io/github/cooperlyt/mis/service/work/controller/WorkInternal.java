package io.github.cooperlyt.mis.service.work.controller;

import io.github.cooperlyt.mis.service.work.services.WorkService;
import io.github.cooperlyt.mis.work.data.WorkDefine;
import io.github.cooperlyt.mis.work.data.WorkDefineForCreate;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@Slf4j
@Tag(name = "WorkInternal", description = "内部调用接口")
@RestController
@RequestMapping("internal")
public class WorkInternal {

  private final WorkService workService;

  public WorkInternal(WorkService workService) {
    this.workService = workService;
  }

  @Operation(summary = "功能创建定义", description = "附加一个生成的WORK_ID",
    parameters = {@Parameter(name = "defineId", description = "操作定义ID")})
  @ApiResponses({@ApiResponse(responseCode = "2xx", description = "成功")})
  @RequestMapping(value = "create/{defineId}" ,method = RequestMethod.GET)
  public Mono<WorkDefineForCreate> prepareCreate(@PathVariable("defineId") String defineId){
    log.info("prepare create work {}" , defineId);
    return workService.prepareCreate(defineId);
  }

  @RequestMapping(value = "define/{defineId}" ,method = RequestMethod.GET)
  public Mono<WorkDefine> workDefine(@PathVariable("defineId") String defineId){
    return workService.workDefine(defineId);
  }

}
