package io.github.cooperlyt.mis.service.dictionary.repositories;

import io.github.cooperlyt.mis.service.dictionary.model.Word;
import org.springframework.data.r2dbc.repository.Modifying;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface DictionaryRepository extends ReactiveCrudRepository<Word, Word.FakePK> {

  Flux<Word> findAllByCategory(String category);

  Flux<Word> findAllByCategoryAndEnabledIsTrue(String category);

  @Query("SELECT value from Word where category = :category and key = :key")
  Mono<String> getWordValue(String category, String key);

  @Modifying
  @Query("INSERT INTO dictionary_word(category,word_key,word_value,description,py_code,word_order) " +
      "VALUES(:#{#word.category},:#{#word.key},:#{#word.value},:#{#word.description},:#{#word.pyCode},:#{#word.order})")
  Mono<Long> insertWord(Word word);

  @Modifying
  @Query("UPDATE dictionary_word set word_value=:#{#word.value} ,description=:#{#word.description},py_code=:#{#word.pyCode},word_order=:#{#word.order}  WHERE category=:#{#word.category} AND word_key=:#{#word.key}")
  Mono<Long> updateWord(Word word);


//  @Data
//  @AllArgsConstructor
//  @NoArgsConstructor
//  class KeyValueModel {
//    private String key;
//    private String value;
//  }
//
//  @Select(  "SELECT word_key,word_value,py_code,description FROM dictionary_word WHERE category = #{category} ORDER BY word_order")
//  @Results(id = "wordMap",value = {
//      @Result(property = "key", column = "word_key"),
//      @Result(property = "value", column = "word_value")
//
//  })
//  List<Word> words(String category);
//
//  @Select("SELECT word_value FROM dictionary_word WHERE category = #{category} AND word_key = #{key}")
//  Optional<String> word(String category, String key);
//
//  @Select("SELECT category,word_key,word_value,py_code,description FROM dictionary_word")
//  @ResultMap("wordMap")
//  List<Word> allWords();
//
//  @Update("UPDATE dictionary_word SET py_code = #{py} WHERE category = #{category} AND word_key = #{key}")
//  void setPyCode(String category, String key, String py);
//
//
//  @Select({
//      "<script>",
//      "   SELECT category,word_value from dictionary_word ",
//      "   <where>",
//      "     <foreach collection='keys' item='value' index='key' separator=' or ' >",
//      "       (category = #{key} and word_key = #{value})",
//      "     </foreach>",
//      "   </where>",
//      "</script>"
//  })
//  @Results({
//      @Result(property = "key" , column = "category"),
//      @Result(property = "value" , column = "word_value")
//  })
//  @MapKey("key")
//  Map<String, KeyValueModel> batchWords(@Param("keys") Map<String,String> keys);
}
