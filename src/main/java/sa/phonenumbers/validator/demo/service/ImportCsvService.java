package sa.phonenumbers.validator.demo.service;

import com.opencsv.CSVParser;
import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import sa.phonenumbers.validator.demo.dao.SAPhoneNumberFixedRepository;
import sa.phonenumbers.validator.demo.dao.SAPhoneNumberRepository;
import sa.phonenumbers.validator.demo.dto.SAPhoneNumberFixedDto;
import sa.phonenumbers.validator.demo.dto.SAPhoneNumberImportResultDto;
import sa.phonenumbers.validator.demo.dto.SAPhoneNumberDto;
import sa.phonenumbers.validator.demo.entity.SAPhoneNumberEntity;
import sa.phonenumbers.validator.demo.entity.SAPhoneNumberFixedEntity;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ImportCsvService {
    SAPhoneNumberRepository saPhoneNumberRepository;
    SAPhoneNumberFixedRepository saPhoneNumberFixedRepository;
    ValidationService validationService;
    MapperService mapperService;

    @Autowired
    public void setSaPhoneNumberRepository(SAPhoneNumberRepository saPhoneNumberRepository) {
        this.saPhoneNumberRepository = saPhoneNumberRepository;
    }

    @Autowired
    public void setValidationService(ValidationService validationService) {
        this.validationService = validationService;
    }

    @Autowired
    public void setMapperService(MapperService mapperService) {
        this.mapperService = mapperService;
    }

    @Autowired
    public void setSaPhoneNumberFixedRepository(SAPhoneNumberFixedRepository saPhoneNumberFixedRepository) {
        this.saPhoneNumberFixedRepository = saPhoneNumberFixedRepository;
    }

    @Transactional
    public List<SAPhoneNumberEntity> parseCsv(MultipartFile pFile) throws IOException {
        CSVParser parser = new CSVParserBuilder().withSeparator(',').withIgnoreQuotations(true).build();

        List<SAPhoneNumberEntity> saPhoneNumberEntities = new ArrayList<>();
        try ( CSVReader csvReader = new CSVReaderBuilder(new InputStreamReader(pFile.getInputStream()))
                .withSkipLines(1)
                .withCSVParser(parser)
                .build()) {
            String[] values;
            while ((values = csvReader.readNext()) != null) {
                saPhoneNumberEntities.add(new SAPhoneNumberEntity(values[0], values[1], true));
            }
        }
        return saPhoneNumberEntities;
    }

    @Transactional
    public SAPhoneNumberImportResultDto importCsv(MultipartFile pFile) throws IOException {
        //TODO: validate mime type or extension at least
        List<SAPhoneNumberEntity> parsedSAPhoneNumbers = this.parseCsv(pFile);
        SAPhoneNumberImportResultDto SAPhoneNumberImportResultDto = validationService.validateSAPhoneNumbers(parsedSAPhoneNumbers);
        log.trace("importCsv|phoneNumberImportResultDto|{}", SAPhoneNumberImportResultDto);
        saPhoneNumberRepository.deleteAll();
        saPhoneNumberFixedRepository.deleteAll();
        saPhoneNumberRepository.saveAll(SAPhoneNumberImportResultDto.getOkNumbers().stream()
                .map(mapperService::convertToEntity)
                .collect(Collectors.toList()));
        saPhoneNumberRepository.saveAll(SAPhoneNumberImportResultDto.getKoNumbers().stream()
                .map(mapperService::convertToEntity)
                .collect(Collectors.toList()));
        List<SAPhoneNumberFixedDto> fixedSAPhoneNumbers = SAPhoneNumberImportResultDto.getFixedNumbers().stream()
                .collect(Collectors.toUnmodifiableList());
        saPhoneNumberFixedRepository.saveAll(fixedSAPhoneNumbers.stream()
                .map(mapperService::convertToEntity)
                .collect(Collectors.toList()));
        return SAPhoneNumberImportResultDto;
    }

    @Transactional
    public List<SAPhoneNumberDto> getAllValidPhoneNumbers(){
        List<SAPhoneNumberEntity> saPhoneNumberEntities = new ArrayList<>();
        saPhoneNumberRepository.findByValidIs(true).forEach(saPhoneNumberEntities::add);

        return saPhoneNumberEntities.stream().map(MapperService::convertToDtoDirect).collect(Collectors.toList());
    }

    @Transactional
    public List<SAPhoneNumberDto> getAllInvalidPhoneNumbers(){
        List<SAPhoneNumberEntity> saPhoneNumberEntities = new ArrayList<>();
        saPhoneNumberRepository.findByValidIs(false).forEach(saPhoneNumberEntities::add);

        return saPhoneNumberEntities.stream().map(MapperService::convertToDtoDirect).collect(Collectors.toList());
    }

    @Transactional
    public List<SAPhoneNumberFixedDto> getAllFixedPhoneNumbers(){
        List<SAPhoneNumberFixedEntity> saPhoneNumberEntities = new ArrayList<>();
        saPhoneNumberFixedRepository.findAll().forEach(saPhoneNumberEntities::add);

        return saPhoneNumberEntities.stream().map(MapperService::convertToDtoDirect).collect(Collectors.toList());
    }
}
