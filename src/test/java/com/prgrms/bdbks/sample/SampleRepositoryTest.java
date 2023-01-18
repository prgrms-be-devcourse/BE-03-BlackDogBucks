package com.prgrms.bdbks.sample;

import javax.sql.DataSource;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.prgrms.bdbks.CustomDataJpaTest;

@CustomDataJpaTest
class SampleRepositoryTest {

    @Autowired
    private SampleRepository sampleRepository;

    @Autowired
    private DataSource dataSource;

    @Test
    void findByIdTest() {
        sampleRepository.findById(50L);

        System.out.println(dataSource);
    }

}