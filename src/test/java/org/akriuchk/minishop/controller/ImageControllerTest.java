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

import java.util.List;

import static org.akriuchk.minishop.TestUtils.*;
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
        ResponseEntity<ApiResponse> response = uploadImage(template, filename, DEMO_PRODUCT_NAME);
        assertThat(response.getStatusCode()).isEqualByComparingTo(HttpStatus.CREATED);

        List<ProductDto> result = toList(template.withBasicAuth("admin", "admin")
                .getForEntity("/products?namePart={values}", ProductDto[].class, DEMO_PRODUCT_NAME));

        ProductDto productDto = result.stream().filter(p -> p.getName().equals(DEMO_PRODUCT_NAME)).findFirst().get();
        assertThat(productDto.getImages()).extracting(ImageDto::getFilename).contains(filename);
    }

    @Test
    public void search() {
        String filename = "анамур.jpg";
        ResponseEntity<ApiResponse> response = uploadImage(template, filename, DEMO_PRODUCT_NAME);

        long id = getImageId(filename, DEMO_PRODUCT_NAME);

        ResponseEntity<ImageDto> imageDto = template.getForEntity("/images/{value}", ImageDto.class, id);
        assertThat(imageDto.getBody().getFilename()).isEqualTo(filename);
        assertThat(imageDto.getBody().getId()).isEqualTo(id);
        assertThat(imageDto.getBody().getProduct()).isEqualTo(DEMO_PRODUCT_NAME);
    }

    @Test
    public void suggestPictures() {
        String filename = "1001_анамур.jpg";
        ResponseEntity<ApiResponse> response = uploadImage(template, filename, DEMO_PRODUCT_NAME);

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

        List<ImageDto> imageDtos = toList(template.getForEntity("/images?productName={values}", ImageDto[].class, "1001_Anamur"));
        assertThat(imageDtos).hasSize(1);
        assertThat(imageDtos.get(0).getFilename()).isEqualTo(filename);
//        assertThat(imageDtos.get(0).getProduct()).isEqualTo("1001_Anamur"); //this will be not checked because image suggestion on this level does not search product
    }


    @Test
    public void findRaw() {
        String filename = "анамур.jpg";
        ResponseEntity<ApiResponse> response = uploadImage(template, filename, DEMO_PRODUCT_NAME);

        ResponseEntity<byte[]> imageDto = template.getForEntity("/images/{value}/raw", byte[].class, 2);
        assertThat(imageDto.getBody()).isNotEmpty();
    }

    @Test
    public void replaceImage() {
        String filename = "анамур.jpg";
        ResponseEntity<ApiResponse> response = uploadImage(template, filename, DEMO_PRODUCT_NAME);
        assertThat(response.getStatusCode()).isEqualByComparingTo(HttpStatus.CREATED);

        long id = getImageId(filename, DEMO_PRODUCT_NAME);

        //change img 2 to img 3
        ResponseEntity<ApiResponse> responseEntity = postFile(template, filename, "/images/" + id, ApiResponse.class);
        assertThat(responseEntity.getStatusCode()).isEqualByComparingTo(HttpStatus.OK);

        List<ProductDto> result = toList(template.withBasicAuth("admin", "admin")
                .getForEntity("/products?namePart={values}", ProductDto[].class, DEMO_PRODUCT_NAME));
        ProductDto productDto = result.get(0);
        assertThat(productDto.getImages()).hasSize(2);
        assertThat(productDto.getImages()).extracting(ImageDto::getId).doesNotContain(id);
    }

    @Test
    public void patchImageToNoProduct() {
        List<ProductDto> result = toList(template.getForEntity(
                "/products?namePart={values}", ProductDto[].class, DEMO_PRODUCT_NAME));

        List<ImageDto> productImages = result.get(0).getImages();
        productImages.forEach(img -> img.setProduct(null));

        template.withBasicAuth("admin", "admin")
                .patchForObject("/images", productImages, ImageDto[].class);

        List<ProductDto> newResult = toList(template.getForEntity(
                "/products?namePart={values}", ProductDto[].class, DEMO_PRODUCT_NAME));

        assertThat(newResult.get(0).getImages()).isEmpty();
    }

    private long getImageId(String filename, String productName) {
        ProductDto productDto = toList(template.getForEntity("/products?namePart={values}", ProductDto[].class, productName))
                .stream()
                .filter(p -> p.getName().equals(productName))
                .findFirst().get();
        return productDto.getImages().stream().filter(dto -> dto.getFilename().equals(filename)).findFirst().get().getId();
    }
}