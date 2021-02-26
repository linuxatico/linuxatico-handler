package sa.phonenumbers.validator.demo.controller;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@FieldDefaults(level = AccessLevel.PRIVATE)
class ImportCsvControllerTests {

    @Autowired
    WebApplicationContext webApplicationContext;

    @Test
    public void whenFileUploaded_thenVerifyStatusOk1() throws Exception {
        MockMultipartFile file = new MockMultipartFile("file", "hello.csv", MediaType.TEXT_PLAIN_VALUE, "id,sms_phone\n103343262,6478342944".getBytes());

        MockMvc mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        mockMvc.perform(multipart("/v1/import-csv").file(file)).andExpect(status().isOk());
    }

    @Test
    public void whenFileUploaded_thenVerifyStatusKo1() throws Exception {
        MockMultipartFile file = new MockMultipartFile("file", "hello.txt", MediaType.TEXT_PLAIN_VALUE, "Hello, World!".getBytes());

        MockMvc mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        mockMvc.perform(multipart("/v1/import-csv").file(file)).andExpect(status().isBadRequest());
    }

    @Test
    public void whenFileUploaded_thenVerifyStatusKo2() throws Exception {
        MockMultipartFile file = new MockMultipartFile("file", "hello.txt", MediaType.APPLICATION_JSON_VALUE, "Hello, World!".getBytes());

        MockMvc mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        mockMvc.perform(multipart("/v1/import-csv").file(file)).andExpect(status().is4xxClientError());
    }

    @Test
    public void whenFileUploaded_thenVerifyStatusKo3() throws Exception {
        MockMultipartFile file = new MockMultipartFile("file", "hello.csv", MediaType.APPLICATION_JSON_VALUE, "id,sms_phone\n103343262,6478342944".getBytes());

        MockMvc mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        mockMvc.perform(multipart("/v1/import-csv").file(file)).andExpect(status().is4xxClientError());
    }

    @Test
    public void whenFileUploaded_thenVerifyStatusKo4() throws Exception {
        MockMultipartFile file = new MockMultipartFile("file", null, MediaType.ALL_VALUE, "id,sms_phone\n103343262,6478342944".getBytes());

        MockMvc mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        mockMvc.perform(multipart("/v1/import-csv").file(file)).andExpect(status().is4xxClientError());
    }

    @Test
    public void whenFileUploaded_thenVerifyStatusKo5() throws Exception {
        MockMultipartFile file = new MockMultipartFile("file", null, null, "id,sms_phone\n103343262,6478342944".getBytes());

        MockMvc mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        mockMvc.perform(multipart("/v1/import-csv").file(file)).andExpect(status().is4xxClientError());
    }

    @Test()
    public void whenFileUploaded_thenVerifyStatusKo6() {
        MockMultipartFile file = null;

        MockMvc mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        assertThrows(IllegalArgumentException.class, () -> {
            mockMvc.perform(multipart("/v1/import-csv").file(file)).andExpect(status().is4xxClientError());
        });
    }

    @Test
    public void whenFileUploaded_thenVerifyStatusKo7() throws Exception {
        MockMultipartFile file = new MockMultipartFile("fisle", "hello.txt", MediaType.TEXT_PLAIN_VALUE, "id,sms_phone\n103343262,6478342944".getBytes());

        MockMvc mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        mockMvc.perform(multipart("/v1/import-csv").file(file)).andExpect(status().isBadRequest());
    }

}
