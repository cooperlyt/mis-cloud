package io.github.cooperlyt.mis.service.dictionary.services

import io.github.cooperlyt.mis.service.dictionary.model.District
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono

@Service
class DistrictService(
  private val districtDao: DistrictDao
) {

  fun districts(level: Int): Mono<List<District>> {
    return districtDao.districts(level)
  }

  fun fullDistrict(id: Int): Mono<List<District>> {
    return districtDao.fullDistrict(id)
  }

  fun fullDistrictName(id: Int): Mono<List<String>> {
    return districtDao.fullDistrict(id)
      .map { it.stream().map(District::getName).toList() }
  }

  fun getAddress(id: Int): Mono<String> {
    return districtDao.getAddress(id)
  }

  fun children(code: Int): Mono<List<District>> {
    return districtDao.children(code)
  }
}