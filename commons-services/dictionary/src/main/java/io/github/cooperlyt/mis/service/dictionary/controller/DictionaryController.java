package io.github.cooperlyt.mis.service.dictionary.controller;

import io.github.cooperlyt.mis.service.dictionary.Application;
import io.github.cooperlyt.mis.service.dictionary.model.District;
import io.github.cooperlyt.mis.service.dictionary.services.DictionaryService;
import io.github.cooperlyt.mis.service.dictionary.services.DistrictService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;
import java.util.SortedSet;

@Tag(name = "DictionaryController", description = "字典使用接口")
@RestController
@RequestMapping("public")
public class DictionaryController {

    private final DictionaryService dictionaryService;
    private final DistrictService districtService;

    public DictionaryController(DictionaryService dictionaryService, DistrictService districtService) {
        this.dictionaryService = dictionaryService;
        this.districtService = districtService;
    }

    @Operation(summary = "可用字典列表", parameters = {@Parameter(name = "category",description = "字典种类")})
    @RequestMapping(value = "/words/{category}" , method = RequestMethod.GET)
    public Mono<SortedSet<DictionaryService.WordGroup>> words(@PathVariable("category") String category){
        return dictionaryService.words(category,true);
    }

    @Operation(summary = "字典值", parameters = {@Parameter(name = "category",description = "字典种类",in = ParameterIn.PATH, required = true)})
    @RequestMapping(value = "label/{category}/all", method = RequestMethod.GET )
    public Mono<Map<Integer,String>> allDictionary(@PathVariable("category") String category){
        return dictionaryService.dictionary(category,false)
            .switchIfEmpty(Mono.error(Application.ErrorDefine.WORD_CATEGORY_INVALID::exception));
    }

    @Operation(summary = "所有字典列表", parameters = {@Parameter(name = "category",description = "字典种类")})
    @RequestMapping(value = "/words/{category}/all" , method = RequestMethod.GET)
    public Mono<SortedSet<DictionaryService.WordGroup>> allWords(@PathVariable("category") String category){
        return dictionaryService.words(category,false);
    }

    @Operation(summary = "刷新字典列表", parameters = {@Parameter(name = "category",description = "字典种类")})
    @RequestMapping(value = "/words/{category}/refresh" , method = RequestMethod.GET)
    public Mono<Void> refresh(@PathVariable("category") String category){
        return dictionaryService.refresh(category);
    }

    @Operation(summary = "字典值", parameters = {@Parameter(name = "category",description = "字典种类",in = ParameterIn.PATH,required = true)})
    @ApiResponses({@ApiResponse(responseCode = "2xx", description = "成功"),@ApiResponse(responseCode = "404",description = "字典不存在")})
    @RequestMapping(value = "/word/{category}/{value}", method = RequestMethod.GET ,produces= {MediaType.TEXT_PLAIN_VALUE , MediaType.APPLICATION_JSON_VALUE})
    public Mono<String> word(@PathVariable("category") String category,
                             @PathVariable("value") int value){
        return dictionaryService.word(category,value);
    }

    @Operation(summary = "行政区列表",parameters = {@Parameter(name = "level",description = "级别", in = ParameterIn.PATH)})
    @RequestMapping(value = "/districts/{level}", method = RequestMethod.GET)
    public Mono<List<District>> district(@PathVariable("level") int level){
       return  districtService.districts(level);
    }

    @Operation(summary = "行政区地址", parameters = {@Parameter(name = "code",description = "区划代码", in = ParameterIn.PATH)})
    @ApiResponses({@ApiResponse(responseCode = "2xx",description = "成功"),@ApiResponse(responseCode = "202", description = "区划代码不存在")})
    @RequestMapping(value = "/district/{code}", method = RequestMethod.GET,produces= {MediaType.TEXT_PLAIN_VALUE , MediaType.APPLICATION_JSON_VALUE})
    public Mono<String> districtAddress(@PathVariable("code") int code){
        return districtService.getAddress(code);
    }



//    @ApiOperation("批量取得区划代码")
//    @ApiImplicitParams({
//        @ApiImplicitParam(name = "ids",value = "区划代码数组",paramType = "body",dataTypeClass = Set.class,required = true),
//    })
//    @RequestMapping(value = "/district", method = RequestMethod.POST)
//    public Mono<Map<Integer,String>> batchDistrictName(@RequestBody Set<Integer> ids){
//        return districtService.getName(ids);
//    }

//    @ApiOperation("批量取得字典值")
//    @ApiImplicitParams({
//        @ApiImplicitParam(name = "keys",value = "字典键",paramType = "body",dataTypeClass = Set.class,required = true),
//    })
//    @RequestMapping(value = "/words", method = RequestMethod.POST)
//    public Mono<Map<String,String>> batchWordName(@RequestBody Map<String,String> keys){
//        return dictionaryService.words(keys);
//    }


}
