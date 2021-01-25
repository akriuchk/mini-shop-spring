package org.akriuchk.minishop.controller;

import org.akriuchk.minishop.dto.ImageDto;
import org.akriuchk.minishop.dto.ProductDto;
import org.akriuchk.minishop.rest.ApiResponse;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
public class ImageControllerTest {

    private final String DEMO_PRODUCT_NAME = "LUKTJASMIN ЛЮКТЭСМИН";

    @Autowired
    private TestRestTemplate template;

    @Before
    public void createProduct(){

    }

    @Test
    public void importFile() {
        String filename = "анамур.jpg";
        ResponseEntity<ApiResponse> response = uploadImage(filename);
        assertThat(response.getStatusCode()).isEqualByComparingTo(HttpStatus.CREATED);

        List<ProductDto> result = toList(template.withBasicAuth("admin", "admin")
                .getForEntity("/products", ProductDto[].class));

        ProductDto productDto = result.stream().filter(p -> p.getName().equals(DEMO_PRODUCT_NAME)).findFirst().get();
        assertThat(productDto.getImages()).extracting(ImageDto::getFilename).contains(filename);
    }

    @Test
    public void search() {
        String filename = "анамур.jpg";
        ResponseEntity<ApiResponse> response = uploadImage(filename);


        long id = getImageId(filename, DEMO_PRODUCT_NAME);

        ResponseEntity<ImageDto> imageDto = template.getForEntity("/images/{value}", ImageDto.class, id);
        assertThat(imageDto.getBody().getFilename()).isEqualTo(filename);
        assertThat(imageDto.getBody().getId()).isEqualTo(id);
        assertThat(imageDto.getBody().getProduct()).isEqualTo(DEMO_PRODUCT_NAME);
    }


    @Test
    public void findRaw() {
        String filename = "анамур.jpg";
        ResponseEntity<ApiResponse> response = uploadImage(filename);

        ResponseEntity<byte[]> imageDto = template.getForEntity("/images/{value}/raw", byte[].class, 2);
        assertThat(imageDto.getBody()).isNotEmpty();
    }

    @Test
    public void replaceImage() {
        String filename = "анамур.jpg";
        ResponseEntity<ApiResponse> response = uploadImage(filename);
        assertThat(response.getStatusCode()).isEqualByComparingTo(HttpStatus.CREATED);

        long id = getImageId(filename, DEMO_PRODUCT_NAME);

        //change img 2 to img 3
        ResponseEntity<ApiResponse> responseEntity = postFile(template, filename, "/images/" + id, ApiResponse.class);
        assertThat(responseEntity.getStatusCode()).isEqualByComparingTo(HttpStatus.OK);

        List<ProductDto> result = toList(template.withBasicAuth("admin", "admin")
                .getForEntity("/products", ProductDto[].class));
        ProductDto productDto = result.stream().filter(p -> p.getName().equals(DEMO_PRODUCT_NAME)).findFirst().get();
        assertThat(productDto.getImages()).hasSize(2);
        assertThat(productDto.getImages()).extracting(ImageDto::getId).doesNotContain(id);
    }

    private ResponseEntity<ApiResponse> uploadImage(String filename) {
        return postFile(template, filename, "/images", ApiResponse.class, x -> {
            x.put("product", Collections.singletonList(DEMO_PRODUCT_NAME));
        });
    }

    private long getImageId(String filename, String productName) {
        ProductDto productDto = toList(template.getForEntity("/products", ProductDto[].class))
                .stream()
                .filter(p -> p.getName().equals(DEMO_PRODUCT_NAME))
                .findFirst().get();
        return productDto.getImages().stream().filter(dto -> dto.getFilename().equals(filename)).findFirst().get().getId();
    }
}