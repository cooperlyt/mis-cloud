package io.github.cooperlyt.mis.dictionary;


import io.github.cooperlyt.mis.dictionary.fill.Dictionary;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
public class DictionaryTestService {

//  private final WorkCreateService workCreateService;
//
//  public DictionaryTestService(WorkCreateService workCreateService) {
//    this.workCreateService = workCreateService;
//  }
//

  @Data
  public static class TestProvide {

    @Dictionary(value = "test")
    private int dic = 1;

    private String dicLabel;


    @Dictionary(value = "t2222")
    private int dic2 = 2;

    private String dic2Label;

  }

  @EqualsAndHashCode(callSuper = true)
  @ToString(callSuper = true)
  @Data
  public static class TestProvide2 extends TestProvide{

    @Dictionary(value = "t3333", targetName = "dic3Label")
    private Integer dic3 = 1;

    private String dic3Label;

    private List<TestProvide> testProvides;

  }

  @RequestMapping("/test")
  public Mono<String> test2(String p1, long p3){

    return Mono.empty();
  }

//  @RequestMapping(value = "/test", method = {RequestMethod.POST})
//  public Mono<BuildProvide> testBuild(@RequestBody BuildProvide p2){
//
//    return workCreateService.createWork("func.test", "func.test", 1, 1).thenReturn(p2);
//  }

  @RequestMapping(value = "/test", method = {RequestMethod.POST})
  public Mono<TestProvide> test(String p1,@RequestBody TestProvide p2, long p3){





    return Mono.justOrEmpty(p2);
  }
}
