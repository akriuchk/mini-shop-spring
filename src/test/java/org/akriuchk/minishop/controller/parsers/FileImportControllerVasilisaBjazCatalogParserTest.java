package org.akriuchk.minishop.controller.parsers;

import org.akriuchk.minishop.dto.ImportFileDto;
import org.akriuchk.minishop.model.SupportedCategories;
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

import static org.akriuchk.minishop.TestUtils.mapFromJson;
import static org.akriuchk.minishop.TestUtils.postFile;
import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@ActiveProfiles("dev")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class FileImportControllerVasilisaBjazCatalogParserTest {

    @Autowired
    private TestRestTemplate template;

    @Test
    public void testInitialVasilImport() throws IOException {
        String filename = "KPB_VASIL_BJAZ_22.05.20.xlsx";
        ResponseEntity<ImportFileDto> response = upload(filename);

        ImportFileDto expected = mapFromJson(new ClassPathResource("expected/KPB_VASIL_BJAZ_22_05_20.json").getFile(), ImportFileDto.class);

        assertThat(response.getBody().getResult())
                .hasSameElementsAs(expected.getResult());
    }

    @Test
    public void testDeliveryVasilImport() throws IOException {
        upload("KPB_VASIL_BJAZ_22.05.20_SHORT_INIT.xlsx");
        ResponseEntity<ImportFileDto> deliveryUpload = upload("KPB_VASIL_BJAZ_22.05.20_SHORT_DELIVERY.xlsx");

        ImportFileDto expected = mapFromJson(new ClassPathResource("expected/KPB_VASIL_BJAZ_22_05_20_DELIVERY.json").getFile(), ImportFileDto.class);

        assertThat(deliveryUpload.getBody().getResult())
                .hasSameElementsAs(expected.getResult());
    }

    private ResponseEntity<ImportFileDto> upload(String filename) {
        return postFile(template, filename, "/files", ImportFileDto.class,
                body -> body.put("category", Collections.singletonList(SupportedCategories.VASILISA_BYAZ.toString())));
    }
}
