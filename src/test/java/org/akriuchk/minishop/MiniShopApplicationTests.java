package org.akriuchk.minishop;

import org.akriuchk.minishop.model.Linen;
import org.akriuchk.minishop.model.LinenCatalog;
import org.akriuchk.minishop.repository.LinenCatalogRepository;
import org.akriuchk.minishop.repository.LinenRepository;
import org.akriuchk.minishop.service.FileImportService;
import org.junit.Assert;
import org.junit.BeforeClass;
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
import java.util.List;
import java.util.stream.IntStream;

@RunWith(SpringRunner.class)
@ActiveProfiles("dev")
@SpringBootTest
public class MiniShopApplicationTests {

    @Autowired
    private LinenCatalogRepository linenCatalogRepository;

    private static LinenRepository linenRepository;

    @BeforeClass
    public static void setUp() {
        linenRepository = Mockito.mock(LinenRepository.class);
    }

    @Test
    public void parseTest() throws IOException {
        String filename = "КПБ+ЧЕБОКСАРЫ 17.05..19(1).xlsx";
        InputStream inputStream = new ClassPathResource(filename).getInputStream();
        MockMultipartFile multipartFile = new MockMultipartFile(filename, inputStream);

        FileImportService fileImportService = new FileImportService(linenCatalogRepository, linenRepository);
        List<LinenCatalog> linenCatalogs = fileImportService.parse(multipartFile);
        Assert.assertEquals("check size of catalogs list", 3, linenCatalogs.size());

        long actualCount = linenCatalogs.stream().flatMapToInt(catalog -> IntStream.of(catalog.getLinens().size()))
                .sum();
        Assert.assertEquals("check size of linen list", 100, actualCount);

        Linen linen = linenCatalogs.get(1)
                .getLinens()
                .stream().filter(l -> l.getName().contentEquals("Валенсия"))
                .findFirst().orElseThrow(AssertionError::new);
        Assert.assertEquals("Валенсия", linen.getName());
        Assert.assertTrue(linen.isSmallAvailable());
        Assert.assertTrue(linen.isMiddleAvailable());
        Assert.assertTrue(linen.isEuroAvailable());
        Assert.assertFalse(linen.isDuoAvailable());
    }

    @Test
    public void parseTestMultipleCollectionOneSheet() throws IOException {
        String filename = "КПБ+Василиса+сатин+17.05.19 (1).xls";
        InputStream inputStream = new ClassPathResource(filename).getInputStream();
        MockMultipartFile multipartFile = new MockMultipartFile(filename, inputStream);

        FileImportService fileImportService = new FileImportService(linenCatalogRepository, linenRepository);
        List<LinenCatalog> linenCatalogs = fileImportService.parse(multipartFile);
        Assert.assertEquals("check size of catalogs list", 5, linenCatalogs.size());

        long actualCount = linenCatalogs.stream().flatMapToInt(catalog -> IntStream.of(catalog.getLinens().size()))
                .sum();
        Assert.assertEquals("check size of linen list", 52, actualCount);

        Linen linen = linenCatalogs.stream()
                .filter(c -> c.getName().contentEquals("Василиса  САТИН"))
                .findFirst().orElseThrow(AssertionError::new)
                .getLinens()
                .stream().filter(l -> l.getName().contentEquals("655.0"))
                .findFirst().orElseThrow(AssertionError::new);
        Assert.assertEquals("655.0", linen.getName());
        Assert.assertFalse(linen.isSmallAvailable());
        Assert.assertFalse(linen.isMiddleAvailable());
        Assert.assertTrue(linen.isEuroAvailable());
        Assert.assertFalse(linen.isDuoAvailable());
    }
}
