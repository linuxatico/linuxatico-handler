package sa.phonenumbers.validator.demo.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
@ToString
public class SAPhoneNumberFixedDto extends SAPhoneNumberDto{
    String fixedSmsPhone;

    public SAPhoneNumberFixedDto(String id, String originalSmsPhone, boolean isValid, String fixedSmsPhone) {
        super(id, originalSmsPhone, isValid);
        this.fixedSmsPhone = fixedSmsPhone;
    }
}
