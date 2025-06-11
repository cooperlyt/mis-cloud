package io.github.cooperlyt.mis.service.dictionary.services;

import io.github.cooperlyt.mis.service.dictionary.Application;
import io.github.cooperlyt.mis.service.dictionary.repositories.DistrictRepository;
import io.github.cooperlyt.mis.service.dictionary.model.District;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.*;

@Service
public class DistrictDao {

    private final DistrictRepository districtRepository;

    public DistrictDao(DistrictRepository districtRepository) {
        this.districtRepository = districtRepository;
    }


    @Cacheable(key = "#level", cacheNames = "districts")
    public Mono<List<District>> districts(int level){
        return districtRepository.findAllByLevelGreaterThanEqual(level)
            .reduce(new District(District.ROOT_LEVEL),(root ,district) -> {
                root.putChild(district);
                return root;
            }).map(District::getChildren);
    }


    @Cacheable(key = "#id", cacheNames = "district")
    public Mono<List<District>> fullDistrict(int id) {
        return districtRepository.getFullDistrict(id)
            .collectSortedList(Comparator.comparing(District::getLevel));
            //.map(it -> it.stream().map(District::getName).toList());
    }

    public Mono<String> getAddress(int id){
        return districtRepository.getDistrictAddress(id)
            .switchIfEmpty(Mono.error(Application.ErrorDefine.DISTRICT_CODE_INVALID::exception));
    }

    public Mono<List<District>> children(int code){
        return districtRepository.findChildren(code, code + "%").collectList();
    }




}
