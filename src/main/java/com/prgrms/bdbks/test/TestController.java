package com.prgrms.bdbks.test;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    @GetMapping("/restdocs")
    public ResponseEntity<?> ok(@RequestParam String hello, @RequestParam Integer world) {
        return ResponseEntity.ok(new TestResponse(hello, world));
    }

    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    public static class TestResponse {
        private String hello;
        private Integer world;
    }

}
