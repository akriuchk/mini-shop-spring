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
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@ActiveProfiles("dev")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ProductControllerTest {

    public static <T> List<T> toList(ResponseEntity<T[]> array) {
        return Arrays.asList(array.getBody());
    }

    @Autowired
    private TestRestTemplate template;

    @Test
    public void testGetProducts() {
        List<ProductDto> result = toList(template.withBasicAuth("admin", "admin")
                .getForEntity("/product", ProductDto[].class));

        ProductDto productDto = result.get(1);
        assertThat(result).hasSize(4);

    }

    @Test
    public void testAddProduct() {
        Product dto = new Product();
        dto.setName("AAA");
        dto.setSmallAvailable(true);
        dto.setMiddleAvailable(false);
        dto.setEuroAvailable(false);
        dto.setDuoAvailable(true);

        ResponseEntity<ApiResponse> apiResponseResponseEntity = template.withBasicAuth("admin", "admin")
                .postForEntity("/product", dto, ApiResponse.class);
        assertThat(apiResponseResponseEntity.getStatusCode()).isEqualByComparingTo(HttpStatus.CREATED);


        Optional<ProductDto> any = toList(template.withBasicAuth("admin", "admin")
                .getForEntity("/product", ProductDto[].class)).stream().filter(p -> p.getName().equals("AAA")).findAny();

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
                .postForEntity("/product/1", dto, ApiResponse.class);
        assertThat(apiResponseResponseEntity.getStatusCode()).isEqualByComparingTo(HttpStatus.OK);
    }
}