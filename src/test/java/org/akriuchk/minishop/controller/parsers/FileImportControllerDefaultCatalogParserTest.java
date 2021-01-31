package org.akriuchk.minishop.controller.parsers;

import org.akriuchk.minishop.dto.ImportFileDto;
import org.akriuchk.minishop.dto.importinfo.ImportResultDto;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

import static org.akriuchk.minishop.TestUtils.mapFromJson;
import static org.akriuchk.minishop.TestUtils.postFile;
import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@ActiveProfiles("dev")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class FileImportControllerDefaultCatalogParserTest {

    @Autowired
    private TestRestTemplate template;

    @Test
    public void testInitialCheboksaryImport() throws IOException {
        String filename = "KPB_CHEBOKS_SHORT_3_PAGES.xlsx";
        ResponseEntity<ImportFileDto> response = postFile(template, filename, "/files", ImportFileDto.class, body -> body.put("category", Collections.singletonList("DEFAULT")));

        List<ImportResultDto> result = response.getBody().getResult();
        ImportFileDto expected = mapFromJson(new ClassPathResource("expected/cheboks_short_3_pages.json").getFile(), ImportFileDto.class);

        assertThat(result).hasSameElementsAs(expected.getResult());

    }
}
