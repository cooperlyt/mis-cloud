package io.github.cooperlyt.mis.service.dictionary.services;

import io.github.cooperlyt.mis.service.dictionary.model.Word;
import io.github.cooperlyt.mis.service.dictionary.repositories.DictionaryRepository;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.*;

/**
 *  字典不能由用户添加，因为会影响上报时的转换。
 *
 *  可以设置 enabled 和 disabled
 *
 *  可以排序
 */
@Service
public class DictionaryService {

    @Schema(title = "分组字典")
    @Getter
    @Setter
    @EqualsAndHashCode(onlyExplicitlyIncluded = true)
    public static class WordGroup implements Comparable<WordGroup>, java.io.Serializable{

        public WordGroup(String group) {
            this.group = group;
        }

        @EqualsAndHashCode.Include()
        private String group;

        private SortedSet<Word> words = new TreeSet<>();

        private int getSeq(){
            if (words.isEmpty())
                return Integer.MAX_VALUE;
            return words.first().getSeq();
        }

        @Override
        public int compareTo(WordGroup o) {
            return Integer.compare(this.getSeq(),o.getSeq());
        }
    }

    private final DictionaryRepository dictionaryRepository;

    public DictionaryService(DictionaryRepository dictionaryRepository) {
        this.dictionaryRepository = dictionaryRepository;
    }


    public Flux<Word> allWords(String category){
        return dictionaryRepository.findAllByCategory(category);
    }


    public Flux<Word> words(String category){
        return dictionaryRepository.findAllByCategoryAndEnabledIsTrue(category);
    }

    public Mono<SortedSet<WordGroup>> words(String category,boolean onlyEnabled){
        return
            Mono.just(onlyEnabled).map(only -> only ? words(category) : allWords(category))
                .flatMap(words ->
                    words.reduceWith(() -> new HashMap<String,WordGroup>() ,(result, word) -> {
                        WordGroup group = Optional.ofNullable(result.get(word.getGroup()))
                            .orElseGet(() -> {
                                WordGroup wordGroup = new WordGroup(word.getGroup());
                                result.put(wordGroup.getGroup(),wordGroup);
                                return wordGroup;
                            });
                        group.getWords().add(word);
                        return result;
                    })
            ).map(map -> new TreeSet<>(map.values()));
    }

    public Mono<Map<Integer,String>> dictionary(String category , boolean onlyEnabled){
        return Mono.just(onlyEnabled).map(only -> only ? words(category) : allWords(category))
            .flatMap(words -> words.collectMap(Word::getValue,Word::getLabel));
    }

    public Mono<String> word(String category, int value){
        return allWords(category).filter(word -> word.getValue() == value)
            .singleOrEmpty()
            .map(Word::getLabel);
    }

    //TODO 发送消息通知其他服务刷新缓存
    public Mono<Void> refresh(String category){
        return Mono.empty();
    }

//    public Mono<Long> initPyCode(){
//        return dictionaryRepository.findAll()
//            .flatMap(word -> {
//                word.setPin(PinYinSearchHelper.spell(word.getLabel()));
//                return dictionaryRepository.updateWord(word).then();
//            })
//            .count();
//    }

}



