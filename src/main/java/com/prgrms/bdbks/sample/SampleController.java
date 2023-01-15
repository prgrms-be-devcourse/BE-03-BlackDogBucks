package com.prgrms.bdbks.sample;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SampleController {

    @GetMapping("/restdocs")
    public ResponseEntity<TestResponse> ok(@RequestParam String hello, @RequestParam Integer world) {
        return ResponseEntity.ok(new TestResponse(hello, world));
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TestResponse {

        private String hello;

        private Integer world;

    }

}
