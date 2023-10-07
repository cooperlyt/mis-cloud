package io.github.cooperlyt.mis.service.work;

import io.github.cooperlyt.commons.cloud.exception.ExceptionStatusCode;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.data.r2dbc.config.EnableR2dbcAuditing;
import org.springframework.data.r2dbc.repository.config.EnableR2dbcRepositories;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.EnableTransactionManagement;


@SpringBootApplication
@EnableTransactionManagement
@EnableCaching
@EnableR2dbcRepositories("io.github.cooperlyt.mis.service.work.repositories")
@EntityScan("io.github.cooperlyt.mis.service.work.model")
@EnableR2dbcAuditing
public class Application {

  public enum ErrorDefine implements ExceptionStatusCode {

    WORK_DEFINE_NOT_EXISTS(HttpStatus.NOT_FOUND,2),

    WORK_NOT_EXISTS(HttpStatus.NOT_FOUND,1),

    MUST_ATTACH_CANT_MODIFY(HttpStatus.BAD_REQUEST,3);

    private static final int CODE_ROOT = 8010300;
    private final HttpStatus status;
    private final int code;

    @Override
    public HttpStatus getHttpStatus() {
      return status;
    }

    ErrorDefine(HttpStatus status, int code) {
      this.status = status;
      this.code = CODE_ROOT + code;
    }

    @Override
    public int getCode() {
      return code;
    }

    @Override
    public String getMessage() {
      return this.name().toLowerCase();
    }

  }

  public static void main(String[] args){
    SpringApplication.run(Application.class,args);
  }
}
