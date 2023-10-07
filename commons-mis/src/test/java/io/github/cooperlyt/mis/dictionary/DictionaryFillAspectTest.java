package io.github.cooperlyt.mis.dictionary;


import io.github.cooperlyt.mis.TestApplication;
import lombok.extern.slf4j.Slf4j;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import java.util.ArrayList;
import java.util.List;

@Disabled
@Slf4j
@SpringBootTest(classes = TestApplication.class)
public class DictionaryFillAspectTest {

  @Autowired
  private DictionaryTestService testService;

//  @Test
//  public void test2Aspect() {
//    testService.test2("hello", 1L)
//        .subscribe(i -> System.out.println("finally test result:" + i));
//  }


  @Autowired
  private DictionaryRemoteService dictionaryRemoteService;

//  @Test
//  public void test3Aspect() {
//
//    BuildConstructInfoProvide buildConstructInfoProvide = new BuildConstructInfoProvide();
//    buildConstructInfoProvide.setBuildOrder("44");
//    buildConstructInfoProvide.setStructureKey(66);
//    buildConstructInfoProvide.setType(BuildConstructInfoProvide.Type.BUNGALOW);
//    buildConstructInfoProvide.setFloorCount(1);
//    buildConstructInfoProvide.setFloorDown(1);
//
//    BuildLocationInfoProvide buildLocationInfoProvide = new BuildLocationInfoProvide();
//    buildLocationInfoProvide.setBuildNumber("55");
//
//    BuildProvide buildProvide = new BuildProvide();
//    buildProvide.setConstructInfo(buildConstructInfoProvide);
//    buildProvide.setLocationInfo(buildLocationInfoProvide);
//
//
//    testService.testBuild(buildProvide)
//        .subscribe(i -> System.out.println("finally test result:" + i));
//  }

  @Test
  public void testAspect() {

    List<DictionaryTestService.TestProvide> list = new ArrayList<>();
    list.add(new DictionaryTestService.TestProvide());

    list.add(new DictionaryTestService.TestProvide());

    list.add(new DictionaryTestService.TestProvide());

    list.add(new DictionaryTestService.TestProvide());

    DictionaryTestService.TestProvide2 testProvide2 = new DictionaryTestService.TestProvide2();
    testProvide2.setTestProvides(list);

    testService.test("hello", testProvide2, 1L)
        .subscribe(i -> System.out.println("finally test result:" + i));
  }
}
