package io.github.cooperlyt.commons.cloud.serialize.test;


import io.github.cooperlyt.commons.cloud.serialize.JsonRawDeserializer;
import io.github.cooperlyt.commons.cloud.serialize.JsonRawSerialize;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import static org.junit.jupiter.api.Assertions.*;

public class JsonRawDeserializerTest {

    static class LongS {
        private List<Long>  longArray;

        public List<Long> getLongArray() {
            return longArray;
        }

        public void setLongArray(List<Long> longArray) {
            this.longArray = longArray;
        }
    }

    static class PP {

        public PP() {

            ll.add("{\"name\":\"小王\",\"country\":\"中国\",\"courseList\":\"\"}");
            ll.add("{\"name\":\"小王1\",\"country\":\"中国\",\"courseList\":\"\"}");
            ll.add("{\"name\":\"小王2\",\"country\":\"中国\",\"courseList\":\"\"}");
            ll.add("{\"name\":\"小王3\",\"country\":\"中国\",\"courseList\":\"\"}");
        }

        @JsonSerialize(using = JsonRawSerialize.class)
        @JsonDeserialize(using = JsonRawDeserializer.class)
        private String aa;

        public String getAa() {
            return aa;
        }

        public void setAa(String aa) {
            this.aa = aa;
        }

        @JsonSerialize(contentUsing = JsonRawSerialize.class)
        public List<String> ll = new ArrayList<>();

        public List<String> getLl() {
            return ll;
        }

        public void setLl(List<String> ll) {
            this.ll = ll;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            PP pp = (PP) o;

            return aa != null ? aa.equals(pp.aa) : pp.aa == null;
        }

        @Override
        public int hashCode() {
            return aa != null ? aa.hashCode() : 0;
        }
    }

    @Data
    public static class Testt{

        @JsonSerialize(using = JsonRawSerialize.class)
        @JsonDeserialize(using = JsonRawDeserializer.class)
        private String diagram;
    }

    @Test
    public void ttt(){
       String ss = "{\"diagram\":[{\"x\":1,\"y\":1,\"rows\":1,\"cols\":1},{\"x\":2,\"y\":1,\"rows\":1,\"cols\":1},{\"x\":3,\"y\":1,\"rows\":1,\"cols\":1},{\"x\":4,\"y\":1,\"rows\":1,\"cols\":1},{\"x\":5,\"y\":1,\"rows\":1,\"cols\":1},{\"x\":6,\"y\":1,\"rows\":1,\"cols\":1},{\"x\":7,\"y\":1,\"rows\":1,\"cols\":1},{\"x\":8,\"y\":1,\"rows\":1,\"cols\":1}]}";

        ObjectMapper mapper = new ObjectMapper();

        try {
            Testt tt = mapper.readValue(ss,Testt.class);
            System.out.println(tt.diagram);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void deserialize() {


        ObjectMapper mapper = new ObjectMapper();
        try {
            PP pp = new PP();
            pp.setAa( "{\"option\":[{\"label\":\"第一个\",\"key\":\"第一个\"},{\"label\":\"第二个\",\"key\":\"第二个\"}]}");
            String ss = mapper.writeValueAsString(pp);
            System.out.println(ss);

            // assertEquals(ss , "{\"aa\":{\"option\":[{\"label\":\"第一个\",\"key\":\"第一个\"},{\"label\":\"第二个\",\"key\":\"第二个\"}]}}");

            PP  p = mapper.readValue(ss,PP.class);

            assertEquals(pp,p);
            System.out.println(p.getAa());


            PP p2 = new PP();
            String s2 = mapper.writeValueAsString(p2);
            System.out.println("out=>"  +  s2);

            assertEquals(s2,"{\"aa\":null}");
            PP  p2r = mapper.readValue(s2,PP.class);



            System.out.println();

            String tt = "{\"aa\":{\"option\":[{\"label\":\"第一个\",\"key\":\"第一个\"},{\"label\":\"第二个\",\"key\":\"第二个\"}]}}";




        } catch (JsonProcessingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testSnakeToCamel(){


        Pattern p = Pattern.compile("\"[A-Za-z0-9]*_{1,}\\w*\":");

        Assertions.assertFalse(p.matcher("\"id\"").matches() );

        Assertions.assertFalse(p.matcher("\"i_d\"").matches() );
        Assertions.assertFalse(p.matcher("\"id\":").matches() );

        Assertions.assertTrue(p.matcher("\"id_ss\":").matches() );

        Assertions.assertTrue(p.matcher("\"_id\":").matches() );
        Assertions.assertTrue(p.matcher("\"id_\":").matches() );
        Assertions.assertTrue(p.matcher("\"_id_\":").matches() );
        Assertions.assertTrue(p.matcher("\"dd_id\":").matches() );
        Assertions.assertTrue(p.matcher("\"dd_id_dsa_dsasd\":").matches() );
        Assertions.assertTrue(p.matcher("\"_dd_id_dsa_dsasd\":").matches() );
        Assertions.assertTrue(p.matcher("\"_dd_id_dsa_dsasd_\":").matches() );

        Assertions.assertTrue(p.matcher("\"_i33d\":").matches() );
        Assertions.assertTrue(p.matcher("\"i332d_\":").matches() );
        Assertions.assertTrue(p.matcher("\"_i234d_\":").matches() );
        Assertions.assertTrue(p.matcher("\"dd_i234d\":").matches() );
        Assertions.assertTrue(p.matcher("\"dd_id_ds234a_dsasd\":").matches() );
        Assertions.assertTrue(p.matcher("\"_dd_i234d_dsa_dsasd\":").matches() );
        Assertions.assertTrue(p.matcher("\"_dd_id_dsa_ds234asd_\":").matches() );
    }


    @Test
    public void testFlux(){
        List<Integer> a = new ArrayList<>();
        a.add(1);a.add(2);a.add(3);
        Flux.fromIterable(a)
            .flatMap(i -> Mono.just(i ).filter(c -> c < 7)

            .switchIfEmpty(Mono.error(new IllegalArgumentException()))
                    .thenReturn(i)
            )
            .map(d -> {System.out.println(d) ; return d;})
            .then().subscribe(c -> System.out.println(c));
    }

    @Test
    public void testLongJson(){
        String testJson = "{\"longArray\": [ \"99\",\"88\" ] }";

        ObjectMapper objectMapper = new ObjectMapper();
        try {
            System.out.println(objectMapper.readValue(testJson,LongS.class).getLongArray().get(1));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    private LocalDateTime convert(LocalDateTime value){
        return value.atZone(ZoneId.systemDefault())
            .withZoneSameInstant(ZoneId.of("UTC")).toLocalDateTime();
    }

}