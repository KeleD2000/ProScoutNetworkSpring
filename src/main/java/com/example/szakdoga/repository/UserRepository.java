package com.example.szakdoga.repository;

import com.example.szakdoga.model.User;
import com.example.szakdoga.model.dto.ReceiverUserDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

    Optional<User> findByUsername(String username);

    @Query("SELECT u.id AS id, u.username AS username FROM User u WHERE u.id IN (SELECT s.receiverUser.id FROM SendMessage s)")
    List<ReceiverUserDto> findAllActiveUsers();


}
