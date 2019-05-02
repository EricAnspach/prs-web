package com.prs.business.purchaserequest;

import java.util.List;

import org.springframework.data.repository.PagingAndSortingRepository;

public interface PurchaseRequestLineItemRepository extends PagingAndSortingRepository<PurchaseRequestLineItem, Integer> {
	List<PurchaseRequestLineItem> findByPurchaseRequestId(int id);
	List<PurchaseRequestLineItem> findAllByPurchaseRequestId(int purchaseRequestId);
}
