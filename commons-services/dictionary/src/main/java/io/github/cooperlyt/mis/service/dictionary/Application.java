package io.github.cooperlyt.mis.service.dictionary;

import io.github.cooperlyt.commons.cloud.exception.ExceptionStatusCode;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.data.r2dbc.repository.config.EnableR2dbcRepositories;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableTransactionManagement
@EnableCaching
@EnableR2dbcRepositories("io.github.cooperlyt.mis.service.dictionary.repositories")
@EntityScan("io.github.cooperlyt.mis.service.dictionary.model")
public class Application {

    public enum ErrorDefine implements ExceptionStatusCode {

        WORD_KEY_INVALID(HttpStatus.NOT_FOUND, 1),

        WORD_CATEGORY_INVALID(HttpStatus.NOT_FOUND, 2),

        DICTIONARY_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, 2),
        DISTRICT_CODE_INVALID(HttpStatus.NOT_FOUND, 3);

        private static final int CODE_ROOT = 8010100;
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
