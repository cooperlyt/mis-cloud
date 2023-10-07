package io.github.cooperlyt.mis.service.dictionary;

import io.github.cooperlyt.mis.service.dictionary.services.DictionaryService;
import io.github.cooperlyt.mis.service.dictionary.services.DistrictService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import reactor.test.StepVerifier;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Disabled
@Slf4j
@ExtendWith(SpringExtension.class)
@SpringBootTest
public class DictionaryServiceTest {

    @Autowired
    public DictionaryService dictionaryService;

    @Autowired
    public DistrictService districtService;

    @Test
    public void testWords(){
//        dictionaryService.initPyCode().as(StepVerifier::create)
//            .assertNext(count -> log.info("update to {}", count))
//            .verifyComplete();

        dictionaryService.words("structure").count()
            .as(StepVerifier::create)
                .assertNext(l -> assertEquals(l,8))
            .verifyComplete();

//        assertEquals(words.size(),37);
//        assertEquals(words.get(0).getValue(),"住宅");
//        log.info(words.get(0).getValue() + ":" + words.get(0).getPyCode());
    }

    @Test
    public void testWord(){

        //AtomicReference<String> word = new AtomicReference<>();
        dictionaryService.word("structure",0)
                .as(StepVerifier::create)
                    .assertNext(word->assertEquals(word,"其他结构"))
                        .verifyComplete();

    }



    @Test
    public void testDistrict(){
//        districtService.initDistrictPY().as(StepVerifier::create)
//            .assertNext(count -> log.info("update to {}", count))
//            .verifyComplete();

        log.info("---------------");

        districtService.getAddress(220621).as(StepVerifier::create)
                .assertNext(name -> assertEquals(name, "吉林省白山市抚松县"))
                    .verifyComplete();


    }


}