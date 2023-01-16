package com.prgrms.bdbks.sample;

import com.prgrms.bdbks.CustomDataJpaTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@CustomDataJpaTest
class SampleRepositoryTest {

    @Autowired
    private SampleRepository sampleRepository;

    @Test
    void findByIdTest() {
        sampleRepository.findById(50L);
    }

}