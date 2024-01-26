package com.example.szakdoga.repository;

import com.example.szakdoga.model.Bid;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BidRepository extends JpaRepository<Bid, Integer> {
    Optional<Bid> findTopByReceiverUserIdOrderByOfferDesc(Integer receiverUserId);
}
