package com.example.szakdoga.controller;

import com.example.szakdoga.model.Bid;
import com.example.szakdoga.repository.BidRepository;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@CrossOrigin(maxAge = 0, value = "*")
@RequestMapping("/api")
public class BidController {

    private final BidRepository bidRepository;

    public BidController(BidRepository bidRepository) {
        this.bidRepository = bidRepository;
    }

    @GetMapping("/bids/{receiverUserId}/maxOffer")
    public Integer getMaxOfferByReceiverUserId(@PathVariable Integer receiverUserId) {
        Optional<Bid> maxOfferBid = bidRepository.findTopByReceiverUserIdOrderByOfferDesc(receiverUserId);
        return maxOfferBid.map(Bid::getOffer).orElse(null);
    }

}
