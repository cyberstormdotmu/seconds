package com.ishoal.core.persistence.repository;

import com.ishoal.core.persistence.entity.UserEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface UserEntityRepository extends CrudRepository<UserEntity, Long> {

    UserEntity findByUsernameIgnoreCase(String username);

    @Query("select u from User u where u.registrationToken='PENDING'")
    List<UserEntity> findAllWithoutARole();
    
    UserEntity findByRegistrationTokenIgnoreCase(String token);
    
    @Query("select u from User u where u.registrationToken=?1")
    List<UserEntity> findUsersByConfirmationStatus(String confirm);
    
    @Query("select u from User u where length(u.registrationToken)=36")
    List<UserEntity> findUsersWithPendingAuthentication();

    @Query("select u from User u where u.registrationToken = 'CONFIRM' AND LOWER(u.forename) LIKE LOWER(CONCAT('%',:forename,'%'))"+
            " AND LOWER(u.surname) LIKE LOWER(CONCAT('%',:surname,'%')) AND LOWER(u.username) LIKE LOWER(CONCAT('%',:username,'%'))")
    List<UserEntity> getUserListByCriteria(@Param("forename") String forename,@Param("surname")  String surname,@Param("username")  String username);
}
