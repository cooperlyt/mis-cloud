package io.github.cooperlyt.mis.dictionary;

import reactor.core.publisher.Mono;

public interface DictionaryRemoteService {

    Mono<String> districtAddress(int id, boolean must);

    default Mono<String> districtAddress(int id){
        return districtAddress(id, true);
    }

    Mono<String> dictionaryLabel(String category, int key, boolean must);

    default Mono<String> dictionaryLabel(String category, int key){
        return dictionaryLabel(category, key, true);
    }

//    Mono<Map<String,String>> getDistrictName(Set<Integer> ids);
//
//    Mono<String> getWordValue(String category,String key);
//
//    Mono<Map<String,String>> getWordValue(Map<String,String> keys);

}
