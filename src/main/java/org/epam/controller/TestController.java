package org.epam.controller;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping
public class TestController {

    Counter visitCounter;

    public TestController(MeterRegistry registry) {
        visitCounter = Counter.builder("visit_counter")
                .description("Number of visits to the /hello")
                .register(registry);

    }

    @GetMapping("/hello")
    public String sayHello() {
        visitCounter.increment();
        return "Hello, World!";
    }
}