package org.akriuchk.minishop.controller;

import org.akriuchk.minishop.dto.ProductDto;
import org.akriuchk.minishop.model.Product;
import org.akriuchk.minishop.rest.ApiResponse;
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

import java.util.List;
import java.util.Optional;

import static org.akriuchk.minishop.TestUtils.postFile;
import static org.akriuchk.minishop.TestUtils.toList;
import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@ActiveProfiles("dev")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class ProductControllerTest {

    private final String DEMO_PRODUCT_NAME = "LUKTJASMIN ЛЮКТЭСМИН";

    @Autowired
    private TestRestTemplate template;

    @Test
    public void testGetProducts() {
        ProductDto dto = new ProductDto();
        dto.setName("AAA");
        dto.setSmallAvailable(true);
        dto.setMiddleAvailable(false);
        dto.setEuroAvailable(false);
        dto.setDuoAvailable(true);
        dto.setCategory("test_cat");

        ResponseEntity<ApiResponse> apiResponseResponseEntity = template.withBasicAuth("admin", "admin")
                .postForEntity("/products", dto, ApiResponse.class);
        assertThat(apiResponseResponseEntity.getStatusCode()).isEqualByComparingTo(HttpStatus.CREATED);


        dto.setName("AAB");
        template.withBasicAuth("admin", "admin")
                .postForEntity("/products", dto, ApiResponse.class);
        assertThat(apiResponseResponseEntity.getStatusCode()).isEqualByComparingTo(HttpStatus.CREATED);

        dto.setName("ABC");
        template.withBasicAuth("admin", "admin")
                .postForEntity("/products", dto, ApiResponse.class);
        assertThat(apiResponseResponseEntity.getStatusCode()).isEqualByComparingTo(HttpStatus.CREATED);


        List<ProductDto> result = toList(template.withBasicAuth("admin", "admin")
                .getForEntity("/products?namePart={values}", ProductDto[].class, "AAA"));
        assertThat(result).hasSize(1);

        result = toList(template.withBasicAuth("admin", "admin")
                .getForEntity("/products?namePart={values}", ProductDto[].class, "AB"));
        assertThat(result).hasSize(2);

        result = toList(template.withBasicAuth("admin", "admin")
                .getForEntity("/products?namePart={values}", ProductDto[].class, "BC"));
        assertThat(result).hasSize(1);
    }

    @Test
    public void testAddProduct() {
        ProductDto dto = new ProductDto();
        dto.setName("AAA");
        dto.setSmallAvailable(true);
        dto.setMiddleAvailable(false);
        dto.setEuroAvailable(false);
        dto.setDuoAvailable(true);
        dto.setCategory("test_cat");

        ResponseEntity<ApiResponse> apiResponseResponseEntity = template.withBasicAuth("admin", "admin")
                .postForEntity("/products", dto, ApiResponse.class);
        assertThat(apiResponseResponseEntity.getStatusCode()).isEqualByComparingTo(HttpStatus.CREATED);


        Optional<ProductDto> any = toList(template.withBasicAuth("admin", "admin")
                .getForEntity("/products?namePart={values}", ProductDto[].class, "AAA"))
                .stream().filter(p -> p.getName().equals("AAA"))
                .findAny();

        assertThat(any).isPresent();
        assertThat(any.get().getId()).isNotNegative().isNotNull();
        assertThat(any.get().isSmallAvailable()).isTrue();
    }


    @Test
    public void testUpdateProduct() {
        Product dto = new Product();
        dto.setName("AAA");
        dto.setSmallAvailable(true);
        dto.setMiddleAvailable(false);
        dto.setEuroAvailable(false);
        dto.setDuoAvailable(true);

        ResponseEntity<ApiResponse> apiResponseResponseEntity = template.withBasicAuth("admin", "admin")
                .postForEntity("/products/1", dto, ApiResponse.class);
        assertThat(apiResponseResponseEntity.getStatusCode()).isEqualByComparingTo(HttpStatus.OK);
    }

    @Test
    public void getEmptyProductImageSuggestions() {
        String filename = "1001_анамур.jpg";

        postFile(template, filename, "/images", ApiResponse.class, x -> {});

        ProductDto dto = new ProductDto();
        dto.setName("1001_Anamur");
        dto.setSmallAvailable(true);
        dto.setMiddleAvailable(false);
        dto.setEuroAvailable(false);
        dto.setDuoAvailable(true);
        dto.setCategory("test_cat");

        ResponseEntity<ApiResponse> apiResponseResponseEntity = template.withBasicAuth("admin", "admin")
                .postForEntity("/products", dto, ApiResponse.class);
        assertThat(apiResponseResponseEntity.getStatusCode()).isEqualByComparingTo(HttpStatus.CREATED);

        List<ProductDto> productDtos = toList(template.getForEntity("/products/suggestedImages", ProductDto[].class));

        System.out.println(productDtos);

        assertThat(productDtos).hasSize(1);
        assertThat(productDtos.get(0).getImages().get(0).getFilename()).isEqualTo(filename);

    }
}