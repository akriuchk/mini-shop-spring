package org.akriuchk.minishop;

import lombok.extern.slf4j.Slf4j;
import org.akriuchk.minishop.model.Category;
import org.akriuchk.minishop.model.Image;
import org.akriuchk.minishop.model.Product;
import org.akriuchk.minishop.repository.CategoriesRepository;
import org.akriuchk.minishop.repository.ImageRepository;
import org.akriuchk.minishop.repository.ProductRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

@Slf4j
@SpringBootApplication(scanBasePackages = "org.akriuchk.minishop")
public class MiniShopApplication {

    public static void main(String[] args) {
        SpringApplication.run(MiniShopApplication.class, args);
    }

    @Bean
    public CommandLineRunner demoData(ProductRepository repo,
                                      CategoriesRepository catRepo,
                                      ImageRepository imageRepo) {
        return args -> {
            Category vasi_satin = new Category();
            vasi_satin.setName("Василиса  САТИН");
            vasi_satin.setDisplayName("Василиса САТИН");
            catRepo.save(vasi_satin);

            Category rai = new Category();
            rai.setName("КПБ БЯЗЬ\"ХЛОПКОВЫЙ КРАЙ\"");
            rai.setDisplayName("КПБ БЯЗЬ \"ХЛОПКОВЫЙ КРАЙ\"");
            catRepo.save(rai);

            Category perkal = new Category();
            perkal.setName("КПБ ПЕРКАЛЬ MIRAROSSI");
            perkal.setDisplayName("КПБ ПЕРКАЛЬ MIRAROSSI");
            catRepo.save(perkal);

            Category category = new Category();
            category.setName("test_cat");
            category.setDisplayName("Ikea");
            catRepo.save(category);

            AtomicLong id = new AtomicLong(0);
            List<Product> productList = Arrays.asList(
                    new Product(id.incrementAndGet(), "LUKTJASMIN ЛЮКТЭСМИН", true, false, true, false, null, category),
                    new Product(id.incrementAndGet(), "KUNGSBLOMMA КУНГСБЛОММА", true, false, true, false, null, category),
                    new Product(id.incrementAndGet(), "ÄNGSLILJA ЭНГСЛИЛЬЯ", true, false, true, false, null, category),
                    new Product(id.incrementAndGet(), "BERGPALM БЕРГПАЛМ", true, false, true, false, null, category)
            );
            repo.saveAll(productList);

            imageFor("luktjasmin-.jpg", productList.get(0), imageRepo);
            imageFor("kungsblomma.jpg", productList.get(1), imageRepo);
            imageFor("aengslilja.jpg", productList.get(2), imageRepo);
            imageFor("aengslilja-engslilya.jpg", productList.get(3), imageRepo);
        };
    }

    private void imageFor(String imgName, Product product, ImageRepository imageRepo) {
        Path imgPath = Paths.get("initFiles", imgName);
        Image img = new Image();
        img.setProduct(product);
        img.setFilename(imgPath.getFileName().toString());
        try {
            img.setContent(Files.readAllBytes(imgPath));
        } catch (IOException e) {
            e.printStackTrace();
        }
        img.setAssigned(true);
        imageRepo.save(img);
    }
}
