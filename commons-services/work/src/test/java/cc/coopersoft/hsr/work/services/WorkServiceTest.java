package cc.coopersoft.hsr.work.services;

import io.github.cooperlyt.mis.service.work.services.WorkService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@Disabled
@Slf4j
@ExtendWith(SpringExtension.class)
@SpringBootTest
public class WorkServiceTest {

  @Autowired
  private WorkService workService;

  @Test
  public void testCreateWork(){

//        workService.createWork(WorkCreateProvide.builder()
//            .defineId("func.corp.create")
//            .workId(9L)
//            .status(WorkInfo.Status.PREPARE)
//            .items(List.of(new WorkEmployee(WorkItem.Type.WORKER,
//                ReactiveKeycloakSecurityContextHolder.UserInfo.builder()
//                    .id("test")
//                    .firstName("lee")
//                    .lastName("pp")
//                    .build())))
//            .build()
//
//    ).flatMap(id -> workService.changeWork(WorkChangeProvide.builder()
//          .workId(id)
//          .status(WorkInfo.Status.COMPLETE)
//                .items(List.of(new WorkEmployee(WorkItem.Type.WORKER,
//                    ReactiveKeycloakSecurityContextHolder.UserInfo.builder()
//                        .id("test")
//                        .firstName("lee")
//                        .lastName("pp")
//                        .build())))
//          .build()
//        )
//      ).flatMap(id -> workService.getWork(id))
//        .as(StepVerifier::create)
//        .assertNext(work -> assertEquals(work.getItems().size() , 2))
//        .verifyComplete();
  }


}