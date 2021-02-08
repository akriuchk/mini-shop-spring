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

import java.util.List;

import static org.akriuchk.minishop.TestUtils.toList;
import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@ActiveProfiles("dev")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class CategoryControllerTest extends TestCase {

    @Autowired
    private TestRestTemplate template;

    @Test
    public void testGetCategory() {
        List<CategoryDto> result = toList(template.getForEntity("/categories", CategoryDto[].class));

        CategoryDto productDto = result.stream().filter(categoryDto -> categoryDto.getName().equals("test_cat")).findFirst().get();
        assertThat(productDto.getProducts()).isNull();
        assertThat(productDto.getName()).isNotNull().isNotBlank();
    }

    @Test
    public void testGetOneCategory() {
        assertThat(
                template.getForEntity("/categories/test_cat", CategoryDto.class).getBody().getName()
        ).isEqualTo("test_cat");
    }

    @Test
    public void testAddCategory() {
        CategoryDto dto = new CategoryDto();
        dto.setName("test_1");
        dto.setDisplayName("Testable Category");

        ResponseEntity<ApiResponse> apiResponseResponseEntity = template.withBasicAuth("admin", "admin")
                .postForEntity("/categories", dto, ApiResponse.class);
        assertThat(apiResponseResponseEntity.getStatusCode()).isEqualByComparingTo(HttpStatus.CREATED);


        CategoryDto category = getCategory(dto.getName());

        assertThat(category.getProducts()).isEmpty();
    }

    private CategoryDto getCategory(String name) {
        return template.getForEntity("/categories/" + name, CategoryDto.class).getBody();
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

        assertThat(getCategory(dto.getName())
                .getDisplayName())
                .isEqualTo(dto.getDisplayName());
    }
}