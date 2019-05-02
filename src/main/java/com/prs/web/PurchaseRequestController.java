package com.prs.web;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.swing.JRadioButton;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;

import com.prs.business.purchaserequest.PurchaseRequest;
import com.prs.business.purchaserequest.PurchaseRequestRepository;
import com.prs.business.user.User;
import com.prs.business.user.UserRepository;

@CrossOrigin
@RestController
@RequestMapping("/purchase-requests")
public class PurchaseRequestController {	

	@Autowired
	private PurchaseRequestRepository purchaseRequestRepo;
	
	@Autowired
	private UserRepository userRepo;
	
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
	
	@GetMapping("")
	public JsonResponse getPurchaseRequests(@RequestParam int start, int limit) {
		JsonResponse jr = null;
		try {
			jr = JsonResponse.getInstance(purchaseRequestRepo.findAll(PageRequest.of(start, limit)));			
		} catch (Exception e) {
			jr = JsonResponse.getInstance(e);
		}		
		return jr;
	}
	
	// Gets one Purchase Request. Displays total $ amount. JsonResponse will include only overall PR information, but not each line item.
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
	
	@PutMapping("/")
	public JsonResponse updatePurchaseRequest(@RequestBody PurchaseRequest p) {
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
	public JsonResponse deletePurchaseRequest(@PathVariable int id) {
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
	
	@PostMapping("/submit-new")
	public JsonResponse submitNewPurchaseRequest(@RequestBody PurchaseRequest p) {				
		p.setStatus("New");
		p.setSubmittedDate(LocalDateTime.now());
		p.setTotal(0.00);
		p.setReasonForRejection("");
		
		JsonResponse jr = null;
		jr = addPurchaseRequest(p);	
		return jr;
	}
	
	@PutMapping("/submit-review/{id}")
	public JsonResponse submitReviewPurchaseRequest(@RequestBody PurchaseRequest p,  @PathVariable int id) {				
		if (p.getTotal() > 50) {
			p.setStatus("Review");
		} else {
			p.setStatus("Approved");
		}		
		
		p.setSubmittedDate(LocalDateTime.now());
		
		JsonResponse jr = null;
		jr = updatePurchaseRequest(p);	
		return jr;
	}
	
	@GetMapping("/list-review")
	public JsonResponse getReviewList(@RequestBody User u) {		
		Iterable<PurchaseRequest> pRList = purchaseRequestRepo.findAll();
		List<PurchaseRequest> pRListToReview = new ArrayList<PurchaseRequest>();
		
		for (PurchaseRequest p : pRList) {
			if (!(p.getUser().equals(u)) && p.getStatus().equalsIgnoreCase("review")) {				
					pRListToReview.add(p);				
			}
		}		
		
		JsonResponse jr = null;
		jr = JsonResponse.getInstance(pRListToReview);		
		return jr;
	}
	
	@PostMapping("/approve/{id}")
	public JsonResponse approvePurchaseRequest(@RequestBody PurchaseRequest p, @PathVariable int id) {
		p.setStatus("Approved");
		
		JsonResponse jr = null;
		jr = updatePurchaseRequest(p);	
		return jr;		
	}
	
	@PostMapping("/reject/{id}/{reason}")
	public JsonResponse rejectPurchaseRequest(@RequestBody PurchaseRequest p, @PathVariable int id, @PathVariable String reason) {
		p.setStatus("Rejected");
		p.setReasonForRejection(reason);
		
		JsonResponse jr = null;
		jr = updatePurchaseRequest(p);	
		return jr;		
	}
	
	@GetMapping("/getByUsername/{userName}")
	public JsonResponse getPurchaserequestByUsername(@PathVariable String userName) {
		User user = userRepo.findByUserName(userName);
		
		JsonResponse jr = null;		
		try {		
			jr = JsonResponse.getInstance(purchaseRequestRepo.findByUser(user));			
		} catch (Exception e) {
			jr = JsonResponse.getInstance(e);
		}		
		return jr;	
	}	
}
