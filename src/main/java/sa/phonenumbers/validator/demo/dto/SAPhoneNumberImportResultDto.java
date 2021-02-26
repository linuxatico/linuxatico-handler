package sa.phonenumbers.validator.demo.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class SAPhoneNumberImportResultDto {
    List<SAPhoneNumberDto> okNumbers;
    List<SAPhoneNumberDto> koNumbers;
    List<SAPhoneNumberFixedDto> fixedNumbers;
}
