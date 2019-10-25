package org.akriuchk.minishop;

import org.akriuchk.minishop.model.LinenCatalog;
import org.akriuchk.minishop.repository.LinenCatalogRepository;
import org.akriuchk.minishop.service.FileImportService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mock.web.MockMultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.stream.IntStream;

@RunWith(MockitoJUnitRunner.class)
public class MiniShopApplicationTests {

    @Mock
    private LinenCatalogRepository linenCatalogRepository;

    @Test
    public void parseTest() throws IOException {
        String filename = "КПБ+ЧЕБОКСАРЫ 17.05..19(1).xlsx";
        InputStream inputStream = new ClassPathResource(filename).getInputStream();
        MockMultipartFile multipartFile = new MockMultipartFile(filename, inputStream);

        FileImportService fileImportService = new FileImportService(linenCatalogRepository);
        List<LinenCatalog> linenCatalogs = fileImportService.parse(multipartFile);
        Assert.assertEquals("check size of catalogs list", 3, linenCatalogs.size());
        long actualCount = linenCatalogs.stream().flatMapToInt(catalog -> IntStream.of(catalog.getLinens().size()))
                .sum();

        Assert.assertEquals("check size of linen list", 100, actualCount);
    }

    @Test
    public void parseTestMultipleCollectionOneSheey() throws IOException {
        String filename = "КПБ+Василиса+сатин+17.05.19 (1).xls";
        InputStream inputStream = new ClassPathResource(filename).getInputStream();
        MockMultipartFile multipartFile = new MockMultipartFile(filename, inputStream);

        FileImportService fileImportService = new FileImportService(linenCatalogRepository);
        List<LinenCatalog> linenCatalogs = fileImportService.parse(multipartFile);
        Assert.assertEquals("check size of catalogs list", 5, linenCatalogs.size());
        long actualCount = linenCatalogs.stream().flatMapToInt(catalog -> IntStream.of(catalog.getLinens().size()))
                .sum();

        Assert.assertEquals("check size of linen list", 56, actualCount);
    }

}
