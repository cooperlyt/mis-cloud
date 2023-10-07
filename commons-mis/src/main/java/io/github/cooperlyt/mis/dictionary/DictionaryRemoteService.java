package io.github.cooperlyt.mis.dictionary;

import reactor.core.publisher.Mono;

public interface DictionaryRemoteService {

    Mono<String> districtAddress(int id);

    Mono<String> dictionaryLabel(String category, int key);

//    Mono<Map<String,String>> getDistrictName(Set<Integer> ids);
//
//    Mono<String> getWordValue(String category,String key);
//
//    Mono<Map<String,String>> getWordValue(Map<String,String> keys);

}
