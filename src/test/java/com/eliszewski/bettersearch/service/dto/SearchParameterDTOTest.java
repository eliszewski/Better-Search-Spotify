package com.eliszewski.bettersearch.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.eliszewski.bettersearch.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class SearchParameterDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(SearchParameterDTO.class);
        SearchParameterDTO searchParameterDTO1 = new SearchParameterDTO();
        searchParameterDTO1.setId(1L);
        SearchParameterDTO searchParameterDTO2 = new SearchParameterDTO();
        assertThat(searchParameterDTO1).isNotEqualTo(searchParameterDTO2);
        searchParameterDTO2.setId(searchParameterDTO1.getId());
        assertThat(searchParameterDTO1).isEqualTo(searchParameterDTO2);
        searchParameterDTO2.setId(2L);
        assertThat(searchParameterDTO1).isNotEqualTo(searchParameterDTO2);
        searchParameterDTO1.setId(null);
        assertThat(searchParameterDTO1).isNotEqualTo(searchParameterDTO2);
    }
}
