package com.eliszewski.bettersearch.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class SearchParameterMapperTest {

    private SearchParameterMapper searchParameterMapper;

    @BeforeEach
    public void setUp() {
        searchParameterMapper = new SearchParameterMapperImpl();
    }
}
