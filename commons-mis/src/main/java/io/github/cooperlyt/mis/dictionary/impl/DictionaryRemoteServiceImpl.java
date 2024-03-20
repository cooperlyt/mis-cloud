package io.github.cooperlyt.mis.dictionary.impl;

import io.github.cooperlyt.mis.RemoteResponseService;
import io.github.cooperlyt.mis.dictionary.DictionaryRemoteService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;

import static io.github.cooperlyt.mis.MisCommonsErrorDefine.DICTIONARY_NOT_FOUND;
import static io.github.cooperlyt.mis.MisCommonsErrorDefine.DISTRICT_NOT_FOUND;

@Slf4j
public class DictionaryRemoteServiceImpl extends RemoteResponseService implements DictionaryRemoteService {


  private final WebClient webClient;

  //TODO 接收清空缓存的消息
  private final Map<String, Map<Integer,String>> dictionaryCache = new HashMap<>();

  private final Map<Integer,String> districtAddressCache = new HashMap<>();



  public DictionaryRemoteServiceImpl(WebClient webClient) {
    this.webClient = webClient;
  }

  @Value("${mis.internal.dictionary.serverName}")
  private String serverName;

  private Mono<String> getRemoteDistrictAddress(int id, boolean must){
    return webClient
        .get()
        .uri("http://" + serverName + "/public/district/{code}", id)
        .accept(MediaType.TEXT_PLAIN)
        .exchangeToMono(response -> sourceResponse(String.class, response))
        .switchIfEmpty(must ? Mono.error(DISTRICT_NOT_FOUND.exception()) : Mono.empty());
  }

  @Override
  public Mono<String> districtAddress(int id, boolean must) {
    if (districtAddressCache.containsKey(id)){
      return Mono.just(districtAddressCache.get(id));
    }
    return getRemoteDistrictAddress(id, must).doOnNext(r -> districtAddressCache.put(id,r));
  }

  private Mono<Map<Integer,String>> getDictionary(String category, boolean must) {
    return webClient
        .get()
        .uri("http://" + serverName + "/public/label/{category}/all", category)
        .accept(MediaType.TEXT_PLAIN, MediaType.APPLICATION_JSON)
        .exchangeToMono(response -> sourceResponse(new ParameterizedTypeReference<Map<Integer,String>>() {}, response))
        .switchIfEmpty(must ? Mono.error(DICTIONARY_NOT_FOUND.exception()) : Mono.empty());
  }

  @Override
  public Mono<String> dictionaryLabel(String category, int key, boolean must) {
    if (dictionaryCache.containsKey(category)){
      log.info("load dictionary from cache:{}",category);
      return Mono.justOrEmpty(dictionaryCache.get(category).get(key));
    }
    return getDictionary(category, must)
        .doOnNext(r -> dictionaryCache.put(category,r))
        .flatMap(r -> Mono.justOrEmpty(r.get(key)));
  }

//  @Override
//  public Mono<Map<String, String>> getDistrictName(Set<Integer> ids) {
//    return webClient
//        .post()
//        .uri("http://" + serverName + "/public/district")
//        .body(Mono.just(ids), Set.class)
//        .accept(MediaType.APPLICATION_JSON)
//        .exchangeToMono(response -> {
//          if (response.statusCode().isError()) {
//            return responseError(response);
//          }
//
//          return response.bodyToMono(Map.class).flatMap(
//              r -> Mono.just((Map<String,String>)r) );
//        });
//  }
//
//  @Override
//  public Mono<String> getWordValue(String category, String key) {
//    return webClient
//        .get()
//        .uri("http://" + serverName + "/public/word/{category}/{id}", category, key)
//        .accept(MediaType.TEXT_PLAIN)
//        .exchangeToMono(response -> sourceResponse(String.class, response));
//  }
//
//  public Mono<Map<String,String>> getWordValue(Map<String,String> keys){
//    return webClient
//        .post()
//        .uri("http://" + serverName + "/public/words")
//        .body(Mono.just(keys),Map.class)
//        .accept(MediaType.APPLICATION_JSON)
//        .exchangeToMono(response -> {
//          if (response.statusCode().isError()) {
//            return responseError(response);
//          }
//          return response.bodyToMono(Map.class).flatMap(
//              r -> Mono.just((Map<String,String>)r) );
//        });
//  }
}
