package io.github.cooperlyt.mis;

import io.github.cooperlyt.mis.work.invocation.MessageWorkScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@EnableTransactionManagement
@SpringBootApplication(
  scanBasePackages = "cc.coopersoft.common.cloud",
    exclude = {
        org.springframework.cloud.client.serviceregistry.AutoServiceRegistrationAutoConfiguration.class,
    cc.coopersoft.common.cloud.construction.AuthRemoteServiceAutoConfigure.class,
    org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration.class,
    org.springframework.boot.actuate.autoconfigure.security.servlet.ManagementWebSecurityAutoConfiguration.class
})
@MessageWorkScan(basePackages = "cc.coopersoft.common.cloud.work")

public class TestApplication {

  public static void main(String[] args) {
    SpringApplication.run(TestApplication.class, args);
  }
}
