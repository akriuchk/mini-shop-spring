package org.akriuchk.minishop;

import lombok.extern.slf4j.Slf4j;
import org.akriuchk.minishop.model.Image;
import org.akriuchk.minishop.repository.ImageRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Slf4j
@SpringBootApplication(scanBasePackages = "org.akriuchk.minishop")
public class MiniShopApplication {

    public static void main(String[] args) {
        SpringApplication.run(MiniShopApplication.class, args);
    }
    public static final String SA = "/Users/alexkruk/IdeaProjects/mini-shop/webcatalogparser/sa";

//    @Bean
    CommandLineRunner init(ImageRepository imageRepository) {
        return args -> {
            Files.walk(Paths.get(SA)).map(Path::toFile).filter(File::isFile).forEach(
                    file -> {
                        String name = file.getName().replace(".jpg", "").replace("-", "");
                        log.info("Importing {}", name);

                        Image image = new Image();
                        image.setName(name);
                        image.setLinked(false);

                        byte[] bytes = new byte[0];
                        try {
                            bytes = Files.readAllBytes(file.toPath());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        image.setImageContent(bytes);

                        imageRepository.save(image);
                    }
            );

        };
    }

}
