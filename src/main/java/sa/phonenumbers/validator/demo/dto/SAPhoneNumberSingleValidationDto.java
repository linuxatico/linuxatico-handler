package sa.phonenumbers.validator.demo.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import sa.phonenumbers.validator.demo.utils.NumberStatus;

@Slf4j
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class SAPhoneNumberSingleValidationDto {
    NumberStatus status;
    String reason;
    String originalNumber;
    String fixedNumber;
}
