package io.github.cooperlyt.mis;


import io.github.cooperlyt.mis.dictionary.DictionaryRemoteService;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class MockDictionaryRemoteService implements DictionaryRemoteService {

  @Override
  public Mono<String> districtAddress(int id) {
    return Mono.just("test");
  }

  @Override
  public Mono<String> dictionaryLabel(String category, int key) {

    System.out.println("-------------------> call mock dictionary:" + category + " " + key);
    return Mono.just("test-dictionary:" + category + "-" + key);
  }
}
