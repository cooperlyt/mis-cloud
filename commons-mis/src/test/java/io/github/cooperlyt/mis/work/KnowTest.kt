package io.github.cooperlyt.mis.work

import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono



class KnowTest {

    private fun testFlux(): Flux<Long> {
        return Mono.deferContextual { Mono.just<String>(it.getOrDefault<String>("key", "none")) }
            .doOnNext { println(it) }
            .thenMany (          Flux.range(1, 10)
                .doOnNext { println("test: $it") }
                .map { it.toLong() })




    }

    private fun testMono(): Mono<Void> {
        return  Mono.deferContextual { Mono.just<String>(it.getOrDefault<String>("key", "none")) }
            .doOnNext { println(it) }
            .then()
    }

    private fun testCompleted(): Mono<Void> {
        return Mono.just(3L).doOnNext { println("do completed") }.then()
    }

    @Test
    fun test1() {

        testFlux()
            .contextWrite { it.put("key", "keykeykey") }

            .subscribe()

    }

    @Test
    fun test3() {

        testMono()
            .contextWrite { it.put("key", "keykeykey") }
            .subscribe()

    }
}