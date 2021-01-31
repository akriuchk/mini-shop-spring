package org.akriuchk.minishop;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.akriuchk.minishop.rest.ApiResponse;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.io.File;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;

public class TestUtils {

    public static <T> ResponseEntity<T> postFile(TestRestTemplate template, String filename, String url, Class<T> responseClass) {
        return postFile(template, filename, url, responseClass, x -> {});
    }

    @SneakyThrows
    public static <T> ResponseEntity<T> postFile(TestRestTemplate template, String filename, String url, Class<T> responseClass, Consumer<MultiValueMap<String, Object>> bodyMutator) {
        byte[] bytes = Files.readAllBytes(new ClassPathResource(filename).getFile().toPath());
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        // This nested HttpEntity is important to create the correct
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
        bodyMutator.accept(body);

        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);
        return template
                .withBasicAuth("admin", "admin")
                .exchange(
                        url,
                        HttpMethod.POST,
                        requestEntity,
                        responseClass);
    }

    public static <T> List<T> toList(ResponseEntity<T[]> array) {
        return Arrays.asList(array.getBody());
    }

    @SneakyThrows
    public static <T> T mapFromJson(File f, Class<T> clazz) {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(f, clazz);
    }

    public static ResponseEntity<ApiResponse> uploadImage(TestRestTemplate template, String filename, String product) {
        return postFile(template, filename, "/images", ApiResponse.class, x -> {
            x.put("product", Collections.singletonList(product));
        });
    }
}
