package sa.phonenumbers.validator.demo.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class SAPhoneNumberDto {
    String id;
    String originalSmsPhone;
    boolean isValid;
}