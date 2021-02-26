package sa.phonenumbers.validator.demo.dao;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import sa.phonenumbers.validator.demo.entity.SAPhoneNumberEntity;

@Repository
public interface SAPhoneNumberRepository extends CrudRepository<SAPhoneNumberEntity, String> {
    @Query("from SAPhoneNumberEntity n where n.valid=:validValue")
    Iterable<SAPhoneNumberEntity> findByValidIs(@Param("validValue") boolean validValue);
}
