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
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.prs.business.purchaserequest.PurchaseRequest;
import com.prs.business.purchaserequest.PurchaseRequestRepository;

@RestController
@RequestMapping("/purchaserequest")
public class PurchaseRequestController {

	@Autowired
	private  PurchaseRequestRepository purchaseRequestRepo;
	
	@GetMapping("/")
	public JsonResponse getAll() {
		JsonResponse jr = null;
		try {
			jr = JsonResponse.getInstance(purchaseRequestRepo.findAll());
		} catch (Exception e) {
			jr = JsonResponse.getInstance(e);
		}
		return jr;		
	}
	
	@GetMapping("/{id}")
	public JsonResponse get(@PathVariable int id) {
		JsonResponse jr = null;		
		try {
			Optional<PurchaseRequest> p = purchaseRequestRepo.findById(id);
			if (p.isPresent()) {
				jr = JsonResponse.getInstance(p);
			} else {
				jr = JsonResponse.getInstance(new Exception("No purchase request found for id = " + id));
			}
		} catch (Exception e) {
			jr = JsonResponse.getInstance(e);
		}		
		return jr;			
	}
	
	@PostMapping("/")
	public JsonResponse addPurchaseRequest(@RequestBody PurchaseRequest p) {
		JsonResponse jr = null;
		jr = JsonResponse.getInstance(savePurchaseRequest(p));	
		return jr;
	}
	
	@PutMapping("/{id}")
	public JsonResponse updatePurchaseRequest(@RequestBody PurchaseRequest p, @PathVariable int id) {
		return savePurchaseRequest(p);
	}

	private JsonResponse savePurchaseRequest(PurchaseRequest p) {
		JsonResponse jr = null;
		try {
			purchaseRequestRepo.save(p);
			jr = JsonResponse.getInstance(p);
		} catch (DataIntegrityViolationException e) {
			jr = JsonResponse.getInstance(new Exception(e.getMessage()));
		}
		return jr;
	}
	
	@DeleteMapping("/{id}")
	public @ResponseBody JsonResponse deletePurchaseRequest(@PathVariable int id) {
		JsonResponse jr = null;
		Optional<PurchaseRequest> p = purchaseRequestRepo.findById(id);
		try {
			if (p.isPresent()) {
				purchaseRequestRepo.deleteById(id);
				jr = JsonResponse.getInstance(p);
			} else {
				jr = JsonResponse.getInstance(new Exception("Purchase Request delete unsuccessful, purchase request " + id + " does not exist."));
			}
		} catch (Exception e) {
			jr = JsonResponse.getInstance(e);
		}
		return jr;
	}
	
}
