package com.payflow.repository;

import com.payflow.entity.User;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUpiId(String upiId);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select u from User u where u.upiId = :upiId")
    Optional<User> findByUpiIdForUpdate(@Param("upiId") String upiId);
}
