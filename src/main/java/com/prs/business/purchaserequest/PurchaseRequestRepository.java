package com.prs.business.purchaserequest;

import java.util.List;

import org.springframework.data.repository.PagingAndSortingRepository;

import com.prs.business.user.User;

public interface PurchaseRequestRepository extends PagingAndSortingRepository<PurchaseRequest, Integer> {
	List<PurchaseRequest> findByUser(User user);
}
