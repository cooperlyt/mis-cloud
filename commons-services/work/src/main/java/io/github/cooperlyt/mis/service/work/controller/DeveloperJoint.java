package io.github.cooperlyt.mis.service.work.controller;

import io.github.cooperlyt.mis.service.work.services.WorkService;
import io.github.cooperlyt.mis.work.data.WorkAttachment;
import io.github.cooperlyt.mis.work.data.WorkFileImpl;
import io.github.cooperlyt.mis.work.data.WorkFileInfo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.List;

@Tag(name = "ExternalJoint",description = "外部联合办公接口")
@RestController
@RequestMapping("protected/corp/developer")
public class DeveloperJoint {

  //TODO 使用网关 限制合同备案业务是否可上传文件
  private final WorkService workService;

  public DeveloperJoint(WorkService workService) {
    this.workService = workService;
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
  @RequestMapping(value = "file/{fileId}" ,method = RequestMethod.DELETE)
  public Mono<Void> removeFile(@PathVariable("fileId") String fileId){
    return workService.removeWorkFile(fileId);
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
