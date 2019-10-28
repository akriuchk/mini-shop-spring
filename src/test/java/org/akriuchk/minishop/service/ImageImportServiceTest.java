package org.akriuchk.minishop.service;

import org.akriuchk.minishop.model.Linen;
import org.akriuchk.minishop.repository.ImageRepository;
import org.akriuchk.minishop.repository.LinenRepository;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;

@RunWith(SpringRunner.class)
@ActiveProfiles("dev")
@SpringBootTest
public class ImageImportServiceTest {

    @Autowired
    private ImageRepository imageRepository;

    @Test
    public void testPutLinenImage() throws IOException {
        LinenRepository linenRepositoryMock = Mockito.mock(LinenRepository.class);
        Linen l = new Linen();
        Mockito.when(linenRepositoryMock.findByName(any())).thenReturn(Optional.of(l));
        ImageImportService imageImportService = new ImageImportService(imageRepository, linenRepositoryMock);
        String filename = "анамур.jpg";
        InputStream inputStream = new ClassPathResource(filename).getInputStream();
        MockMultipartFile multipartFile = new MockMultipartFile(filename, inputStream);

        imageImportService.putLinenImage("some_name", multipartFile);
        Assert.assertNotNull(l.getImage());
    }
}