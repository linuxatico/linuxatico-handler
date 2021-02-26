package sa.phonenumbers.validator.demo.controller.api;

import com.google.gson.Gson;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import sa.phonenumbers.validator.demo.dto.SAPhoneNumberImportResultDto;
import sa.phonenumbers.validator.demo.exception.StorageException;
import sa.phonenumbers.validator.demo.service.ImportCsvService;

import java.io.IOException;
import java.util.Objects;

@RestController
@RequestMapping(value = "/v1")
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ImportCsvController {
    ImportCsvService importCsvService;

    @Autowired
    public void setImportCsvService(ImportCsvService importCsvService) {
        this.importCsvService = importCsvService;
    }

    @ResponseBody
    @PostMapping(value = "/import-csv")
    public String importCsv(@RequestParam("file") MultipartFile file) throws IOException {
        if(!Objects.requireNonNull(file.getOriginalFilename()).endsWith(".csv") || !"text/plain".equals(file.getContentType())){
            throw new StorageException("Invalid file submitted: only csv allowed");
        }
        SAPhoneNumberImportResultDto SAPhoneNumberImportResultDto = importCsvService.importCsv(file);
        log.trace("importCsv|{}", SAPhoneNumberImportResultDto);
        return new Gson().toJson(SAPhoneNumberImportResultDto);
    }

    @ExceptionHandler(StorageException.class)
    public ResponseEntity<?> handleStorageException(StorageException exc) {
        return ResponseEntity.badRequest().build();
    }
}
