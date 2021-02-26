package sa.phonenumbers.validator.demo.entity;

import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Entity
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table(name = "SA_PHONE_NUMBER")
public class SAPhoneNumberEntity {
    @Id
    @Column(name = "ID")
    String id;
    @Column(name = "ORIGINAL_PHONE_NUMBER")
    String originalSmsPhone;
    @Column(name = "VALID")
    Boolean valid;
}
