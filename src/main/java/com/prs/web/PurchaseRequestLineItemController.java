package com.prs.web;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.prs.business.purchaserequest.PurchaseRequest;
import com.prs.business.purchaserequest.PurchaseRequestLineItem;
import com.prs.business.purchaserequest.PurchaseRequestLineItemRepository;
import com.prs.business.purchaserequest.PurchaseRequestRepository;
import com.prs.business.user.UserRepository;

@RestController
@RequestMapping("/purchaserequestlineitem")
public class PurchaseRequestLineItemController {
	
	@Autowired
	private  PurchaseRequestLineItemRepository purchaseRequestLineItemRepo;
	
	@GetMapping("/")
	public JsonResponse getAll() {
		JsonResponse jr = null;
		try {
			jr = JsonResponse.getInstance(purchaseRequestLineItemRepo.findAll());
		} catch (Exception e) {
			jr = JsonResponse.getInstance(e);
		}
		return jr;		
	}
	
	@GetMapping("/{id}")
	public JsonResponse get(@PathVariable int id) {
		JsonResponse jr = null;		
		try {
			Optional<PurchaseRequestLineItem> p = purchaseRequestLineItemRepo.findById(id);
			if (p.isPresent()) {
				jr = JsonResponse.getInstance(p);
			} else {
				jr = JsonResponse.getInstance(new Exception("No purchase request line item found for id = " + id));
			}
		} catch (Exception e) {
			jr = JsonResponse.getInstance(e);
		}		
		return jr;			
	}
	
	@PostMapping("/")
	public JsonResponse addPurchaseRequestLineItem(@RequestBody PurchaseRequestLineItem p) {
		JsonResponse jr = null;
		jr = JsonResponse.getInstance(savePurchaseRequestLineItem(p));
		return jr;
	}
	
	@PutMapping("/{id}")
	public JsonResponse updatePurchaseRequestLineItem(@RequestBody PurchaseRequestLineItem p, @PathVariable int id) {
		return savePurchaseRequestLineItem(p);
	}

	private JsonResponse savePurchaseRequestLineItem(PurchaseRequestLineItem p) {
		JsonResponse jr = null;
		try {
			purchaseRequestLineItemRepo.save(p);
			jr = JsonResponse.getInstance(p);
		} catch (DataIntegrityViolationException e) {
			jr = JsonResponse.getInstance(new Exception(e.getMessage()));
		}
		return jr;
	}
	
	@DeleteMapping("/{id}")
	public JsonResponse deletePurchaseRequestLineItem(@PathVariable int id) {
		JsonResponse jr = null;
		Optional<PurchaseRequestLineItem> p = purchaseRequestLineItemRepo.findById(id);
		try {
			if (p.isPresent()) {
				purchaseRequestLineItemRepo.deleteById(id);
				jr = JsonResponse.getInstance(p);
			} else {
				jr = JsonResponse.getInstance(new Exception("Purchase Request Line Item delete unsuccessful, purchase request line item " + id + " does not exist."));
			}
		} catch (Exception e) {
			jr = JsonResponse.getInstance(e);
		}
		return jr;
	}
	
}
