package io.github.cooperlyt.mis.service.work.controller;


import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "WorkPublicity",description = "操作信息公共查询接口")
@RestController
@RequestMapping("public")
public class Publicity {




//  @Operation(summary = "列出业务操作记录", description = "列出业务操作记录",
//      parameters = {@Parameter(name = "workId", description = "业务ID")})
//  @ApiResponses({@ApiResponse(responseCode = "2xx", description = "成功")})
//  @RequestMapping(value = "work/{workId}/items" ,method = RequestMethod.GET)
//  public Mono<List<WorkTaskItem>> listWorkItems(@PathVariable("workId") long workId){
//    return workService.listWorkItems(workId);
//  }


}
