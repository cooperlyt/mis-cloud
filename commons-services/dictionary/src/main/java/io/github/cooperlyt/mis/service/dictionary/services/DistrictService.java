package io.github.cooperlyt.mis.service.dictionary.services;

import io.github.cooperlyt.mis.service.dictionary.Application;
import io.github.cooperlyt.mis.service.dictionary.repositories.DistrictRepository;
import io.github.cooperlyt.mis.service.dictionary.model.District;

import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.*;

@Service
@CacheConfig(cacheNames = "district")
public class DistrictService {

    private final DistrictRepository districtRepository;

    public DistrictService(DistrictRepository districtRepository) {
        this.districtRepository = districtRepository;
    }


    @Cacheable(key = "#level")
    public Mono<List<District>> districts(int level){
        return districtRepository.findAllByLevelGreaterThanEqual(level)
            .reduce(new District(District.ROOT_LEVEL),(root ,district) -> {
                root.putChild(district);
                return root;
            }).map(District::getChildren).cache();
    }

    public Mono<String> getAddress(int id){
        return districtRepository.getDistrictAddress(id)
            .switchIfEmpty(Mono.error(Application.ErrorDefine.DISTRICT_CODE_INVALID::exception));
    }


}
