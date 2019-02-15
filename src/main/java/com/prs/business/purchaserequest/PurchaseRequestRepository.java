package com.prs.business.purchaserequest;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import com.prs.business.user.User;

public interface PurchaseRequestRepository extends CrudRepository<PurchaseRequest, Integer> {
	Optional<PurchaseRequest> findByUser(User user);
}
