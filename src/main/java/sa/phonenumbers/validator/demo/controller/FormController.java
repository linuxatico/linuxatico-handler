package sa.phonenumbers.validator.demo.controller;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import sa.phonenumbers.validator.demo.dto.*;
import sa.phonenumbers.validator.demo.exception.StorageException;
import sa.phonenumbers.validator.demo.exception.StorageFileNotFoundException;
import sa.phonenumbers.validator.demo.service.ImportCsvService;
import sa.phonenumbers.validator.demo.service.ValidationService;
import sa.phonenumbers.validator.demo.storage.StorageService;
import sa.phonenumbers.validator.demo.utils.NumberStatus;

import javax.ws.rs.FormParam;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@Controller
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE)
public class FormController {
    StorageService storageService;
    ImportCsvService importCsvService;
    ValidationService validationService;

    @Autowired
    public void setStorageService(StorageService storageService) {
        this.storageService = storageService;
    }

    @Autowired
    public void setImportCsvService(ImportCsvService importCsvService) {
        this.importCsvService = importCsvService;
    }

    @Autowired
    public void setValidationService(ValidationService validationService) {
        this.validationService = validationService;
    }

    @GetMapping("/")
    public String form(Model model) {
        return "home";
    }

    @PostMapping("/")
    public String handleFileUpload(@RequestParam("file") MultipartFile file, RedirectAttributes redirectAttributes, Model model) throws IOException {
        if(!Objects.requireNonNull(file.getOriginalFilename()).endsWith(".csv") || !Arrays.asList("text/plain", "text/csv").contains(file.getContentType())){
            throw new StorageException("Invalid file submitted: only csv allowed");
        }
        storageService.store(file);
        SAPhoneNumberImportResultDto sAPhoneNumberImportResultDto = importCsvService.importCsv(file);
        log.trace("handleFileUpload|sAPhoneNumberImportResultDto|{}", sAPhoneNumberImportResultDto);
        redirectAttributes.addFlashAttribute("message", "You successfully uploaded " + file.getOriginalFilename() + "!");
        return "redirect:/list" ;
    }

    @GetMapping("/list")
    public String list(Model model) {

        List<SAPhoneNumberDto> saPhoneNumberDtoList = importCsvService.getAllValidPhoneNumbers();
        List<SAPhoneNumberDto> saPhoneNumberInvalidDtoList = importCsvService.getAllInvalidPhoneNumbers();
        List<SAPhoneNumberFixedDto> saPhoneNumberFixedDtoList = importCsvService.getAllFixedPhoneNumbers();
        model.addAttribute("okNumbers", saPhoneNumberDtoList);
        model.addAttribute("koNumbers", saPhoneNumberInvalidDtoList);
        model.addAttribute("fixedNumbers", saPhoneNumberFixedDtoList);
        return "list";
    }


    @GetMapping("/single")
    public String formSingleNumber(Model model) {
        return "single";
    }

    @PostMapping("/single")
    public String handleSingleNumber(@FormParam("phoneNumber") String phoneNumber, RedirectAttributes redirectAttributes, Model model) throws IOException {

        SAPhoneNumberSingleValidationDto saPhoneNumberSingleValidationDto = validationService.validateSAPhoneNumber(phoneNumber);
        log.trace("handleSingleNumber|saPhoneNumberSingleValidationDto|{}", saPhoneNumberSingleValidationDto);
        String resultMessage = buildResultMessage(saPhoneNumberSingleValidationDto);
        redirectAttributes.addFlashAttribute("message", resultMessage);
        return "redirect:/single" ;
    }

    private String buildResultMessage(SAPhoneNumberSingleValidationDto saPhoneNumberSingleValidationDto) {
        String resultMessage;
        if(saPhoneNumberSingleValidationDto.getStatus().equals(NumberStatus.OK)){
            resultMessage = "status: "+ saPhoneNumberSingleValidationDto.getStatus().name();
        } else if(saPhoneNumberSingleValidationDto.getStatus().equals(NumberStatus.KO)){
            resultMessage = "status: "+ saPhoneNumberSingleValidationDto.getStatus().name()+", reason: "+ saPhoneNumberSingleValidationDto.getReason();
        } else {
            resultMessage =
                    "status: "+ saPhoneNumberSingleValidationDto.getStatus().name()+
                    ", reason: "+ saPhoneNumberSingleValidationDto.getReason()+
                    ", input: "+ saPhoneNumberSingleValidationDto.getOriginalNumber()+
                    ", fixed: "+ saPhoneNumberSingleValidationDto.getFixedNumber();
        }
        return resultMessage;
    }

    @ExceptionHandler(StorageFileNotFoundException.class)
    public ResponseEntity<?> handleStorageFileNotFound(StorageFileNotFoundException exc) {
        return ResponseEntity.notFound().build();
    }

    @ExceptionHandler(StorageException.class)
    public ResponseEntity<?> handleStorageException(StorageException exc) {
        return ResponseEntity.badRequest().build();
    }
}
