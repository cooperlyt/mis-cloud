package io.github.cooperlyt.mis.work;

import cc.coopersoft.common.cloud.TestApplication;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@Disabled
@Slf4j
@SpringBootTest(classes = TestApplication.class)
public class MessageWorkTest {

  @Autowired
  private TestMessageWork testMessageWork;

  @Test
  public void testAspect() {


    testMessageWork.test("hello").subscribe(i -> System.out.println("finally test result:" + i));
  }

  @Autowired
  private WorkCreateService workCreateService;
  @Test
  public void testNew(){
    workCreateService.createWork("1","2",5,7)
        .subscribe(i -> System.out.println("finally test result:" + i));
  }
}
