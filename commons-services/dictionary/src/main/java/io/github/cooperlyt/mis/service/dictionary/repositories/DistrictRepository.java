package io.github.cooperlyt.mis.service.dictionary.repositories;

import io.github.cooperlyt.mis.service.dictionary.model.District;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface DistrictRepository extends ReactiveCrudRepository<District,Integer> {

    Flux<District> findAllByLevelGreaterThanEqual(int level);


    @Query("SELECT address FROM district WHERE id = :id ")
    Mono<String> getDistrictAddress(int id);

    @Query("SELECT id,level,name,address FROM district WHERE (SELECT level + 1 FROM district WHERE id = :code) = level and id like :parent")
    Flux<District> findChildren(@Param("code") int code, @Param("parent") String parent);
//
////    @Query("SELECT ")
////    Mono<String> getName(int id);
//
//    @Select("SELECT id,level,name,py_code,full_name FROM district WHERE level <= #{level} order by id")
//    List<District> district(int level);
//
//    @Update("UPDATE district SET py_code = #{py} WHERE id = #{id}")
//    void updatePy(int id, String py);
//
//    @Select("SELECT full_name FROM district WHERE id = #{id}")
//    Optional<String> getName(int id);
//
//    @Select({
//            "<script>",
//                "SELECT id, full_name from district ",
//                "<where>",
//                    " id in ",
//                    "<foreach collection='ids' item='id' index='index' open='(' separator=',' close=')' >",
//                    "#{id}",
//                    "</foreach>",
//                "</where>",
//            "</script>"
//    })
//    @MapKey("id")
//    Map<Integer,District> getNames(Collection<Integer> ids);
}
