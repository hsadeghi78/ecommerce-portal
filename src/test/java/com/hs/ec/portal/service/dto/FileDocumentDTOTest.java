package com.hs.ec.portal.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.hs.ec.portal.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class FileDocumentDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(FileDocumentDTO.class);
        FileDocumentDTO fileDocumentDTO1 = new FileDocumentDTO();
        fileDocumentDTO1.setId(1L);
        FileDocumentDTO fileDocumentDTO2 = new FileDocumentDTO();
        assertThat(fileDocumentDTO1).isNotEqualTo(fileDocumentDTO2);
        fileDocumentDTO2.setId(fileDocumentDTO1.getId());
        assertThat(fileDocumentDTO1).isEqualTo(fileDocumentDTO2);
        fileDocumentDTO2.setId(2L);
        assertThat(fileDocumentDTO1).isNotEqualTo(fileDocumentDTO2);
        fileDocumentDTO1.setId(null);
        assertThat(fileDocumentDTO1).isNotEqualTo(fileDocumentDTO2);
    }
}
