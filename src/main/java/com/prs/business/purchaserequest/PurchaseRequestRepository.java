package com.prs.business.purchaserequest;

import java.util.Optional;

import org.springframework.data.repository.PagingAndSortingRepository;

import com.prs.business.user.User;

public interface PurchaseRequestRepository extends PagingAndSortingRepository<PurchaseRequest, Integer> {
	Optional<PurchaseRequest> findByUser(User user);
}
