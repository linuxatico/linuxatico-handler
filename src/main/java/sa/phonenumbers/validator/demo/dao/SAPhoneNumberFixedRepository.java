package sa.phonenumbers.validator.demo.dao;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import sa.phonenumbers.validator.demo.entity.SAPhoneNumberFixedEntity;

@Repository
public interface SAPhoneNumberFixedRepository extends CrudRepository<SAPhoneNumberFixedEntity, String> {

}
