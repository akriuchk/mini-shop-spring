package org.akriuchk.minishop.controller;

import org.akriuchk.minishop.dto.ImportFileDto;
import org.akriuchk.minishop.model.ImportFile;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Collections;
import java.util.List;

import static org.akriuchk.minishop.TestUtils.postFile;
import static org.akriuchk.minishop.TestUtils.toList;
import static org.assertj.core.api.Assertions.assertThat;


@RunWith(SpringRunner.class)
@ActiveProfiles("dev")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class FileImportControllerTest {

    @Autowired
    private TestRestTemplate template;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Test
    public void importFile() {
        String filename = "KPB_CHEBOKS_SHORT_3_PAGES.xlsx";

        ResponseEntity<ImportFile> response = postFile(template, filename, "/files", ImportFile.class, body -> body.put("category", Collections.singletonList("DEFAULT")));
        assertThat(response.getStatusCode()).isEqualByComparingTo(HttpStatus.CREATED);
        assertThat(response.getBody().getFilename()).isEqualTo(filename);
        assertThat(response.getBody().getStatus()).isEqualTo(ImportFile.IMPORT_STATUS.FINISHED);
    }


    @Test
    public void search() {
        String filename = "KPB_CHEBOKS_SHORT_3_PAGES.xlsx";

        postFile(template, filename, "/files", ImportFile.class, body -> body.put("category", Collections.singletonList("DEFAULT")));

        jdbcTemplate.execute(
                "UPDATE import_files " +
                        "SET status='ERROR'");

        List<ImportFileDto> any = toList(template.withBasicAuth("admin", "admin")
                .getForEntity("/files?status={value}", ImportFileDto[].class, ImportFile.IMPORT_STATUS.ACCEPTED));

        assertThat(any).isEmpty();

        jdbcTemplate.execute(
                "UPDATE import_files " +
                        "SET status='IN_DB'");

        postFile(template, filename, "/files", ImportFile.class, body -> {
            body.put("category", Collections.singletonList("DEFAULT"));
        });
        postFile(template, filename, "/files", ImportFile.class, body -> {
            body.put("category", Collections.singletonList("DEFAULT"));
        });
        postFile(template, filename, "/files", ImportFile.class, body -> {
            body.put("category", Collections.singletonList("DEFAULT"));
        });

        any = toList(template.withBasicAuth("admin", "admin")
                .getForEntity("/files?status={value}", ImportFileDto[].class, ImportFile.IMPORT_STATUS.IN_DB));


        assertThat(any).hasSize(1);
    }

    @Test
    public void deleteCompleted()  {
        String filename = "KPB_CHEBOKS_SHORT_3_PAGES.xlsx";
        postFile(template, filename, "/files", ImportFile.class, body -> {
            body.put("category", Collections.singletonList("DEFAULT"));
        });
        postFile(template, filename, "/files", ImportFile.class, body -> {
            body.put("category", Collections.singletonList("DEFAULT"));
        });
        postFile(template, filename, "/files", ImportFile.class, body -> {
            body.put("category", Collections.singletonList("DEFAULT"));
        });

        jdbcTemplate.execute(
                "UPDATE import_files " +
                        "SET status='ERROR'" +
                        "WHERE id=1 "
        );

        jdbcTemplate.execute(
                "UPDATE import_files " +
                        "SET status='IN_DB'" +
                        "WHERE id=2 "
        );

        List<ImportFileDto> importFileDtos = toList(template.withBasicAuth("admin", "admin")
                .getForEntity("/files?status={value}", ImportFileDto[].class, ImportFile.IMPORT_STATUS.FINISHED));


        assertThat(importFileDtos).hasSize(1);
    }
}