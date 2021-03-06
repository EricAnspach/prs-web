package com.prs.web;

import java.util.Optional;

import javax.persistence.EntityManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;

import com.prs.business.purchaserequest.PurchaseRequest;
import com.prs.business.purchaserequest.PurchaseRequestLineItem;
import com.prs.business.purchaserequest.PurchaseRequestLineItemRepository;

@CrossOrigin
@RestController
@RequestMapping("/purchase-request-line-items")
public class PurchaseRequestLineItemController {
	
	@Autowired
	private  PurchaseRequestLineItemRepository purchaseRequestLineItemRepo;
	
	@Autowired
	private EntityManager em;
	
	@Autowired
	private PurchaseRequestController prc;
	
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
	
	@GetMapping("")
	public JsonResponse getPRLIs(@RequestParam int start, int limit) {
		JsonResponse jr = null;
		try {
			jr = JsonResponse.getInstance(purchaseRequestLineItemRepo.findAll(PageRequest.of(start, limit)));			
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
	
	// Get PRLIs by Purchase Request ID; for use when a single purchase request is displayed:
	// Used by reviewer to approve or reject request
	// Or used by user to view request. Use to display after adding, updating, or deleting PRLI.
	@GetMapping("/getPR/{id}")
	public JsonResponse getPRLIsByPRID(@PathVariable int id) {
		JsonResponse jr = null;
		try {
			jr = JsonResponse.getInstance(purchaseRequestLineItemRepo.findByPurchaseRequestId(id));
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
	
	@PutMapping("/")
	public JsonResponse updatePurchaseRequestLineItem(@RequestBody PurchaseRequestLineItem p) {
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
		em.clear();
		recalculateTotal(p);
		return jr;
	}

	private void recalculateTotal(PurchaseRequestLineItem p) {
		PurchaseRequest pr = p.getPurchaseRequest();
		int pRID = pr.getId();
		double pRTotal = 0;
		Iterable<PurchaseRequestLineItem> prlis = purchaseRequestLineItemRepo.findAll();
		
		for (PurchaseRequestLineItem prli : prlis) {
			if (pRID == prli.getPurchaseRequest().getId()) {
				double pRLITotal = prli.getQuantity() * (prli.getProduct().getPrice());
				pRTotal += pRLITotal;
			}
		}		
		pr.setTotal(pRTotal);
		prc.updatePurchaseRequest(pr);
	}
	
	@DeleteMapping("/{id}")
	public JsonResponse deletePurchaseRequestLineItem(@PathVariable int id) {
		JsonResponse jr = null;
		Optional<PurchaseRequestLineItem> p = purchaseRequestLineItemRepo.findById(id);
		PurchaseRequestLineItem prliToDelete = p.get();
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
		em.clear();
		recalculateTotal(prliToDelete);
		return jr;
	}	
	
	@GetMapping(path="/lines-for-pr/{id}")
	public @ResponseBody JsonResponse getAllLineItemsForPR(@PathVariable int id) {
		JsonResponse jr = null;
		try {
			jr = JsonResponse.getInstance(purchaseRequestLineItemRepo.findAllByPurchaseRequestId(id));
		} catch (Exception e) {
			jr = JsonResponse.getInstance(e);
		}
		return jr;
	}
}
