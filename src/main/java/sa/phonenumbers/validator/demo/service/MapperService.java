package sa.phonenumbers.validator.demo.service;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sa.phonenumbers.validator.demo.dto.SAPhoneNumberDto;
import sa.phonenumbers.validator.demo.dto.SAPhoneNumberFixedDto;
import sa.phonenumbers.validator.demo.entity.SAPhoneNumberEntity;
import sa.phonenumbers.validator.demo.entity.SAPhoneNumberFixedEntity;

@Service
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE)
public class MapperService {
    ModelMapper modelMapper;

    @Autowired
    public void setModelMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    public SAPhoneNumberEntity convertToEntity(SAPhoneNumberDto SAPhoneNumberDto) {
        SAPhoneNumberEntity post = modelMapper.map(SAPhoneNumberDto, SAPhoneNumberEntity.class);
        return post;
    }

    public static SAPhoneNumberDto convertToDtoDirect(SAPhoneNumberEntity saPhoneNumberEntity) {
        SAPhoneNumberDto SAPhoneNumberDto = new SAPhoneNumberDto(saPhoneNumberEntity.getId(), saPhoneNumberEntity.getOriginalSmsPhone(), saPhoneNumberEntity.getValid());
        return SAPhoneNumberDto;
    }

    public SAPhoneNumberFixedEntity convertToEntity(SAPhoneNumberFixedDto SAPhoneNumberDto) {
        SAPhoneNumberFixedEntity post = modelMapper.map(SAPhoneNumberDto, SAPhoneNumberFixedEntity.class);
        return post;
    }

    public static SAPhoneNumberFixedDto convertToDtoDirect(SAPhoneNumberFixedEntity saPhoneNumberEntity) {
        SAPhoneNumberFixedDto saPhoneNumberFixedDto = new SAPhoneNumberFixedDto(saPhoneNumberEntity.getId(), saPhoneNumberEntity.getOriginalSmsPhone(),saPhoneNumberEntity.getValid(), saPhoneNumberEntity.getFixedSmsPhone());
        return saPhoneNumberFixedDto;
    }
}
