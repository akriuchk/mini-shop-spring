package org.akriuchk.minishop.controller;

import lombok.AllArgsConstructor;
import org.akriuchk.minishop.model.BedSheet;
import org.akriuchk.minishop.repository.BedSheetRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@AllArgsConstructor
@RestController
@CrossOrigin(origins = "http://localhost:4200")
public class BedSheetsController {

    private final BedSheetRepository bedSheetRepository;

    @GetMapping("/bedsheet")
    public List<BedSheet> getPictures() {
        return (List<BedSheet>) bedSheetRepository.findAll();
    }

    @PostMapping("/bedsheet")
    public void saveBedSheet(@RequestBody BedSheet bedSheet) {
        bedSheetRepository.save(bedSheet);
    }
}
