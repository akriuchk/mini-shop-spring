package org.akriuchk.minishop.controller;

import junit.framework.TestCase;
import org.akriuchk.minishop.dto.CategoryDto;
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

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@ActiveProfiles("dev")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class CategoryControllerTest extends TestCase {

    public static <T> List<T> toList(ResponseEntity<T[]> array) {
        return Arrays.asList(array.getBody());
    }

    @Autowired
    private TestRestTemplate template;

    @Test
    public void testGetCategory() {
        List<CategoryDto> result = toList(template.withBasicAuth("admin", "admin")
                .getForEntity("/categories", CategoryDto[].class));

        CategoryDto productDto = result.get(0);
        assertThat(productDto.getLinens()).hasSize(4);
        assertThat(productDto.getName()).isNotNull().isNotBlank();
    }

    @Test
    public void testAddCategory() {
        CategoryDto dto = new CategoryDto();
        dto.setName("test_1");
        dto.setDisplayName("Testable Category");

        ResponseEntity<ApiResponse> apiResponseResponseEntity = template.withBasicAuth("admin", "admin")
                .postForEntity("/categories", dto, ApiResponse.class);
        assertThat(apiResponseResponseEntity.getStatusCode()).isEqualByComparingTo(HttpStatus.CREATED);


        Optional<CategoryDto> any = getCategory(dto.getName());

        assertThat(any).isPresent();
        assertThat(any.get().getLinens()).isEmpty();
    }

    private Optional<CategoryDto> getCategory(String name) {
        Optional<CategoryDto> any = toList(template.withBasicAuth("admin", "admin")
                .getForEntity("/categories", CategoryDto[].class))
                .stream().filter(c -> c.getName().equals(name))
                .findAny();
        return any;
    }

    @Test
    public void testUpdateCategory() {
        CategoryDto dto = new CategoryDto();
        dto.setName("test_1");
        dto.setDisplayName("Testable Category");

        ResponseEntity<ApiResponse> responseEntity = template.withBasicAuth("admin", "admin")
                .postForEntity("/categories", dto, ApiResponse.class);
        assertThat(responseEntity.getStatusCode()).isEqualByComparingTo(HttpStatus.CREATED);


        dto.setDisplayName("New Category Name");
        responseEntity = template.withBasicAuth("admin", "admin")
                .postForEntity("/categories/test_1", dto, ApiResponse.class);

        assertThat(responseEntity.getStatusCode()).isEqualByComparingTo(HttpStatus.OK);

        assertThat(getCategory(dto.getName()).get()
                .getDisplayName())
                .isEqualTo(dto.getDisplayName());
    }
}