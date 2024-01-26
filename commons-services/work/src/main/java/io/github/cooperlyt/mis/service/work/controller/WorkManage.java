package io.github.cooperlyt.mis.service.work.controller;

import io.github.cooperlyt.mis.service.work.services.WorkService;
import io.github.cooperlyt.mis.work.data.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.List;

@Slf4j
@Tag(name = "WorkController", description = "工作操作接口")
@RestController
@RequestMapping("protected/gov")
public class WorkManage {

  private final WorkService workService;

  public WorkManage(WorkService workService) {
    this.workService = workService;
  }

  @Operation(summary = "列出业务操作定义", description = "列出业务操作定义",
      parameters = {@Parameter(name = "type", description = "操作类型")})
  @ApiResponses({@ApiResponse(responseCode = "2xx", description = "成功")})
  @RequestMapping(value = "defines/{type}" ,method = RequestMethod.GET)
  private Mono<List<WorkDefine>> defineByType(@PathVariable("type") String type){

    return workService.defineByType(type);
  }

  @Operation(summary = "功能创建定义", description = "附加一个生成的WORK_ID",
      parameters = {@Parameter(name = "defineId", description = "操作定义ID")})
  @ApiResponses({@ApiResponse(responseCode = "2xx", description = "成功")})
  @RequestMapping(value = "create/{defineId}" ,method = RequestMethod.GET)
  public Mono<WorkDefineForProcess> prepareProcess(@PathVariable("defineId") String defineId){
    return workService.prepareProcess(defineId);
  }

  @Operation(summary = "添加文件", description = "添加文件",
      parameters = {@Parameter(name = "attachId", description = "附件ID")})
  @RequestMapping(value = "attach/{attachId}/file" ,method = RequestMethod.PUT)
  public Mono<Void> addFile(@PathVariable("attachId") long attachId,
                            @RequestBody WorkFileImpl file){
    return workService.addWorkFile(attachId,file);
  }

  @Operation(summary = "移除文件", description = "移除文件",
      parameters = {@Parameter(name = "attachId", description = "附件ID"),
                    @Parameter(name = "fileId", description = "文件ID")})
  @RequestMapping(value = "file/{attachId}/{fileId}" ,method = RequestMethod.DELETE)
  public Mono<Void> removeFile(@PathVariable("attachId")long attachId,
                               @PathVariable("fileId") String fileId){
    return workService.removeWorkFile(attachId,fileId);
  }

  @Operation(summary = "工作文件列表", description = "工作文件列表",
      parameters = {@Parameter(name = "attachId", description = "附件ID")})
  @RequestMapping(value = "attach/{attachId}/files" ,method = RequestMethod.GET)
  public Mono<List<WorkFileInfo>> workFiles(@PathVariable("attachId") long attachId){
    return workService.workFiles(attachId);
  }

  @Operation(summary = "工作附件列表", description = "工作附件列表",
      parameters = {@Parameter(name = "workId", description = "工作ID")})
  @RequestMapping(value = "work/{workId}/attachments" ,method = RequestMethod.GET)
  public Mono<List<WorkAttachment>> workAttachments(@PathVariable("workId") long workId){
    return workService.workAttachments(workId);
  }

  @Operation(summary = "添加附件", description = "添加附件",
      parameters = {@Parameter(name = "workId", description = "工作ID")})
  @RequestMapping(value = "work/{workId}/attachment" ,method = RequestMethod.PUT)
  public Mono<Long> addAttachment(@PathVariable("workId") long workId,
                                  @RequestBody String name){
    return workService.addWorkAttachment(workId,name);
  }

  @Operation(summary = "删除附件", description = "删除附件",
      parameters = {@Parameter(name = "attachmentId", description = "附件ID")})
  @RequestMapping(value = "attach/{attachmentId}" ,method = RequestMethod.PATCH)
  public Mono<Void> editAttachmentName(@PathVariable("attachmentId") long attachmentId,
                                       @RequestBody String name){
    return workService.renameWorkAttachment(attachmentId,name);
  }

  @Operation(summary = "删除附件", description = "删除附件",
      parameters = {@Parameter(name = "attachmentId", description = "附件ID")})
  @RequestMapping(value = "attach/{attachmentId}" ,method = RequestMethod.DELETE)
  public Mono<Void> deleteAttachment(@PathVariable("attachmentId") long attachmentId){
    return workService.removeWorkAttachment(attachmentId);
  }

}
