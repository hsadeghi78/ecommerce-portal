package com.hs.ec.portal.domain;

import static com.hs.ec.portal.domain.FileDocumentTestSamples.*;
import static com.hs.ec.portal.domain.ProductTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.hs.ec.portal.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class FileDocumentTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(FileDocument.class);
        FileDocument fileDocument1 = getFileDocumentSample1();
        FileDocument fileDocument2 = new FileDocument();
        assertThat(fileDocument1).isNotEqualTo(fileDocument2);

        fileDocument2.setId(fileDocument1.getId());
        assertThat(fileDocument1).isEqualTo(fileDocument2);

        fileDocument2 = getFileDocumentSample2();
        assertThat(fileDocument1).isNotEqualTo(fileDocument2);
    }

    @Test
    void pricesTest() throws Exception {
        FileDocument fileDocument = getFileDocumentRandomSampleGenerator();
        Product productBack = getProductRandomSampleGenerator();

        fileDocument.addPrices(productBack);
        assertThat(fileDocument.getPrices()).containsOnly(productBack);
        assertThat(productBack.getDocuments()).containsOnly(fileDocument);

        fileDocument.removePrices(productBack);
        assertThat(fileDocument.getPrices()).doesNotContain(productBack);
        assertThat(productBack.getDocuments()).doesNotContain(fileDocument);

        fileDocument.prices(new HashSet<>(Set.of(productBack)));
        assertThat(fileDocument.getPrices()).containsOnly(productBack);
        assertThat(productBack.getDocuments()).containsOnly(fileDocument);

        fileDocument.setPrices(new HashSet<>());
        assertThat(fileDocument.getPrices()).doesNotContain(productBack);
        assertThat(productBack.getDocuments()).doesNotContain(fileDocument);
    }
}
