package com.eliszewski.bettersearch.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.eliszewski.bettersearch.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class SearchParameterTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(SearchParameter.class);
        SearchParameter searchParameter1 = new SearchParameter();
        searchParameter1.setId(1L);
        SearchParameter searchParameter2 = new SearchParameter();
        searchParameter2.setId(searchParameter1.getId());
        assertThat(searchParameter1).isEqualTo(searchParameter2);
        searchParameter2.setId(2L);
        assertThat(searchParameter1).isNotEqualTo(searchParameter2);
        searchParameter1.setId(null);
        assertThat(searchParameter1).isNotEqualTo(searchParameter2);
    }
}
