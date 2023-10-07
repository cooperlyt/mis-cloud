package io.github.cooperlyt.mis.service.work.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "ExternalJoint",description = "外部联合办公接口")
@RestController
@RequestMapping("protected/external")
public class ExternalJoint {

}
