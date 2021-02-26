package sa.phonenumbers.validator.demo.service;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import sa.phonenumbers.validator.demo.utils.NumberStatus;
import sa.phonenumbers.validator.demo.dto.SAPhoneNumberFixedDto;
import sa.phonenumbers.validator.demo.dto.SAPhoneNumberImportResultDto;
import sa.phonenumbers.validator.demo.dto.SAPhoneNumberDto;
import sa.phonenumbers.validator.demo.dto.SAPhoneNumberSingleValidationDto;
import sa.phonenumbers.validator.demo.entity.SAPhoneNumberEntity;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ValidationService {

    public static final String SA_PHONE_NUMBER_PREFIX = "27";
    public static final String REGEX_VALID_SA_PHONE_NUMBER_WITH_PREFIX = "^("+SA_PHONE_NUMBER_PREFIX+")([0-9]{9})$";
    public static final String REGEX_VALID_SA_PHONE_NUMBER_WITHOUT_PREFIX = "^([0-9]{9})$";

    public SAPhoneNumberImportResultDto validateSAPhoneNumbers(List<SAPhoneNumberEntity> phoneNumberEntityList){
        List<SAPhoneNumberDto> valids = phoneNumberEntityList
                .stream()
                .filter(saPhoneNumberEntity -> saPhoneNumberEntity.getOriginalSmsPhone().matches(REGEX_VALID_SA_PHONE_NUMBER_WITH_PREFIX))
                .map(saPhoneNumberEntity -> new SAPhoneNumberDto(saPhoneNumberEntity.getId(), saPhoneNumberEntity.getOriginalSmsPhone(), true))
                .collect(Collectors.toUnmodifiableList());
        List<SAPhoneNumberFixedDto> validsWithoutPrefix = phoneNumberEntityList
                .stream()
                .filter(saPhoneNumberEntity -> saPhoneNumberEntity.getOriginalSmsPhone().matches(REGEX_VALID_SA_PHONE_NUMBER_WITHOUT_PREFIX))
                .map(saPhoneNumberEntity ->
                        new SAPhoneNumberFixedDto(
                                saPhoneNumberEntity.getId(), saPhoneNumberEntity.getOriginalSmsPhone(),
                                saPhoneNumberEntity.getValid(), SA_PHONE_NUMBER_PREFIX.concat(saPhoneNumberEntity.getOriginalSmsPhone()))
                )
                .collect(Collectors.toUnmodifiableList());

        List<SAPhoneNumberDto> invalids = phoneNumberEntityList
                .stream()
                .filter(saPhoneNumberEntity ->
                        !saPhoneNumberEntity.getOriginalSmsPhone().matches(REGEX_VALID_SA_PHONE_NUMBER_WITH_PREFIX) &&
                        !saPhoneNumberEntity.getOriginalSmsPhone().matches(REGEX_VALID_SA_PHONE_NUMBER_WITHOUT_PREFIX))
                .map(saPhoneNumberEntity -> new SAPhoneNumberDto(saPhoneNumberEntity.getId(), saPhoneNumberEntity.getOriginalSmsPhone(), false))
                .collect(Collectors.toUnmodifiableList());

        return new SAPhoneNumberImportResultDto(valids, invalids, validsWithoutPrefix);
    }

    public SAPhoneNumberSingleValidationDto validateSAPhoneNumber(String phoneNumber){
        SAPhoneNumberSingleValidationDto saPhoneNumberSingleValidationDto = null;

        if(phoneNumber.matches(REGEX_VALID_SA_PHONE_NUMBER_WITH_PREFIX)){
            saPhoneNumberSingleValidationDto = new SAPhoneNumberSingleValidationDto(NumberStatus.OK, null, phoneNumber, null);
        } else if(phoneNumber.matches(REGEX_VALID_SA_PHONE_NUMBER_WITHOUT_PREFIX)){
            saPhoneNumberSingleValidationDto = new SAPhoneNumberSingleValidationDto(
                    NumberStatus.FIXED,
                    "Missing prefix '27'", phoneNumber,
                    SA_PHONE_NUMBER_PREFIX.concat(phoneNumber));
        } else {
            saPhoneNumberSingleValidationDto = new SAPhoneNumberSingleValidationDto(NumberStatus.KO, "Not recoverable number", phoneNumber, null);
        }

        return saPhoneNumberSingleValidationDto;
    }
}
