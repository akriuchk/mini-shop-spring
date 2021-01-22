package org.akriuchk.minishop.controller;

import org.akriuchk.minishop.dto.ImportFileDto;
import org.akriuchk.minishop.model.ImportFile;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.*;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.io.IOException;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.List;

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

    public static <T> List<T> toList(ResponseEntity<T[]> array) {
        return Arrays.asList(array.getBody());
    }

    @Test
    public void importFile() throws IOException {
        String filename = "KPB_CHEBOKS_SHORT_3_PAGES.xlsx";

        ResponseEntity<ImportFile> response = postFile(filename, "/files", ImportFile.class);

        assertThat(response.getStatusCode()).isEqualByComparingTo(HttpStatus.CREATED);
        assertThat(response.getBody().getFilename()).isEqualTo(filename);
        assertThat(response.getBody().getStatus()).isEqualTo(ImportFile.IMPORT_STATUS.IN_DB);
    }


    @Test
    public void search() throws IOException {
        String filename = "KPB_CHEBOKS_SHORT_3_PAGES.xlsx";

        postFile(filename, "/files", ImportFile.class);

        jdbcTemplate.execute(
                "UPDATE import_files " +
                        "SET status='ERROR'");

        List<ImportFileDto> any = toList(template.withBasicAuth("admin", "admin")
                .getForEntity("/files?status={value}", ImportFileDto[].class, ImportFile.IMPORT_STATUS.ACCEPTED));

        assertThat(any).isEmpty();


        jdbcTemplate.execute(
                "UPDATE import_files " +
                        "SET status='IN_DB'");

        postFile(filename, "/files", ImportFile.class);
        postFile(filename, "/files", ImportFile.class);
        postFile(filename, "/files", ImportFile.class);

        any = toList(template.withBasicAuth("admin", "admin")
                .getForEntity("/files?status={value}", ImportFileDto[].class, ImportFile.IMPORT_STATUS.IN_DB));


        assertThat(any).hasSize(1);
    }

    @Test
    public void deleteCompleted() throws IOException {
        String filename = "KPB_CHEBOKS_SHORT_3_PAGES.xlsx";
        postFile(filename, "/files", ImportFile.class);
        postFile(filename, "/files", ImportFile.class);
        postFile(filename, "/files", ImportFile.class);

        jdbcTemplate.execute(
                "UPDATE import_files " +
                        "SET status='ERROR'" +
                        "WHERE id=1 "
        );

        jdbcTemplate.execute(
                "UPDATE import_files " +
                        "SET status='FINISHED'" +
                        "WHERE id=2 "
        );


        List<ImportFileDto> importFileDtos = toList(template.withBasicAuth("admin", "admin")
                .getForEntity("/files?status={value}", ImportFileDto[].class, ImportFile.IMPORT_STATUS.ACCEPTED));


        assertThat(importFileDtos).hasSize(1);

    }

    public <T> ResponseEntity<T> postFile(String filename, String url, Class<T> responseClass) throws IOException {
        byte[] bytes = Files.readAllBytes(new ClassPathResource(filename).getFile().toPath());
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        // This nested HttpEntiy is important to create the correct
        // Content-Disposition entry with metadata "name" and "filename"
        MultiValueMap<String, String> fileMap = new LinkedMultiValueMap<>();
        ContentDisposition contentDisposition = ContentDisposition
                .builder("form-data")
                .name("file")
                .filename(filename)
                .build();
        fileMap.add(HttpHeaders.CONTENT_DISPOSITION, contentDisposition.toString());
        HttpEntity<byte[]> fileEntity = new HttpEntity<>(bytes, fileMap);

        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("file", fileEntity);

        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);
        return template
                .withBasicAuth("admin", "admin")
                .exchange(
                        url,
                        HttpMethod.POST,
                        requestEntity,
                        responseClass);
    }
}