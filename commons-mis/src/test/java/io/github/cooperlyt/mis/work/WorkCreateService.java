package io.github.cooperlyt.mis.work;


import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;

@Service
@WorkMessageBinding("binding.test")
public class WorkCreateService {


  @WorkCreate(value = "func.test", transactionalRequired = false)
  public Mono<Map<String,Object>> createWork(String workKey, String defineKey, @OrgIdParam long orgId, @EmployeeIdParam long empId){
    System.out.println("===================================> createWork");
    return Mono.just("test").flatMap(s -> Mono.deferContextual(ctx -> {
      System.out.println("===================================> createWork context:" + ctx);
      Long workId = ctx.get("workId");
      System.out.println("===================================> createWork context:" + workId);
      return Mono.just(new HashMap<>(Map.of("workId",workId)));
    }));
  }
}
