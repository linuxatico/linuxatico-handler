package sa.phonenumbers.validator.demo.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import sa.phonenumbers.validator.demo.entity.SAPhoneNumberEntity;

import java.io.InputStream;
import java.util.List;

@SpringBootTest
class ImportCsvServiceTests {

    @Autowired
    public ImportCsvService importCsvService;

    @Test
    void whenFileParse_thenVerifyParseResultOk1() throws Exception {
        InputStream is = this.getFileFromResourceAsStream("csv/1number_ok_si_prefix1.csv");

        MockMultipartFile mockMultipartFile = new MockMultipartFile("file", "1number_ok_si_prefix1.csv", MediaType.TEXT_PLAIN_VALUE, is);
        List<SAPhoneNumberEntity> csvBeans = importCsvService.parseCsv(mockMultipartFile);
        Assertions.assertEquals(1, csvBeans.size(), "wrong number of phone number parsed");
    }

    @Test
    void whenFileParse_thenVerifyParseResultOk2() throws Exception {
        InputStream is = this.getFileFromResourceAsStream("csv/1number_ko_no_prefix1.csv");

        MockMultipartFile mockMultipartFile = new MockMultipartFile("file", "1number_ko_no_prefix1.csv", MediaType.TEXT_PLAIN_VALUE, is);
        List<SAPhoneNumberEntity> csvBeans = importCsvService.parseCsv(mockMultipartFile);
        Assertions.assertEquals(1, csvBeans.size(), "wrong number of phone number parsed");
    }

    @Test
    void whenFileParse_thenVerifyParseResultOk3() throws Exception {
        InputStream is = this.getFileFromResourceAsStream("csv/full.csv");

        MockMultipartFile mockMultipartFile = new MockMultipartFile("file", "full.csv", MediaType.TEXT_PLAIN_VALUE, is);
        List<SAPhoneNumberEntity> csvBeans = importCsvService.parseCsv(mockMultipartFile);
        Assertions.assertEquals(1000, csvBeans.size(), "wrong number of phone number parsed");
    }

    private InputStream getFileFromResourceAsStream(String fileName) {
        // The class loader that loaded the class
        ClassLoader classLoader = getClass().getClassLoader();
        InputStream inputStream = classLoader.getResourceAsStream(fileName);

        // the stream holding the file content
        if (inputStream == null) {
            throw new IllegalArgumentException("file not found! " + fileName);
        } else {
            return inputStream;
        }
    }
}
