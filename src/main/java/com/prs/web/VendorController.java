package com.prs.web;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.prs.business.vendor.Vendor;
import com.prs.business.vendor.VendorRepository;

@RestController
@RequestMapping("/vendors")
public class VendorController {

	@Autowired
	private VendorRepository vendorRepo;
	
	@GetMapping("/")
	public JsonResponse getAll() {
		JsonResponse jr = null;
		try {
			jr = JsonResponse.getInstance(vendorRepo.findAll());
		} catch (Exception e) {
			jr = JsonResponse.getInstance(e);
		}
		return jr;		
	}
	
	@GetMapping("")
	public JsonResponse getVendors(@RequestParam int start, int limit) {
		JsonResponse jr = null;
		try {
			jr = JsonResponse.getInstance(vendorRepo.findAll(PageRequest.of(start, limit)));
			
		} catch (Exception e) {
			jr = JsonResponse.getInstance(e);
		}		
		return jr;
	}
	
	@GetMapping("/{id}")
	public JsonResponse get(@PathVariable int id) {
		JsonResponse jr = null;		
		try {
			Optional<Vendor> v = vendorRepo.findById(id);
			if (v.isPresent()) {
				jr = JsonResponse.getInstance(v);
			} else {
				jr = JsonResponse.getInstance(new Exception("No vendor found for id = " + id));
			}
		} catch (Exception e) {
			jr = JsonResponse.getInstance(e);
		}		
		return jr;			
	}
	
	@PostMapping("/")
	public JsonResponse addVendor(@RequestBody Vendor v) {
		JsonResponse jr = null;
		jr = JsonResponse.getInstance(saveVendor(v));	
		return jr;
	}
	
	@PutMapping("/{id}")
	public JsonResponse updateVendor(@RequestBody Vendor v, @PathVariable int id) {
		return saveVendor(v);
	}

	private JsonResponse saveVendor(Vendor v) {
		JsonResponse jr = null;
		try {
			vendorRepo.save(v);
			jr = JsonResponse.getInstance(v);
		} catch (DataIntegrityViolationException e) {
			jr = JsonResponse.getInstance(new Exception(e.getMessage()));
		}
		return jr;
	}
	
	@DeleteMapping("/{id}")
	public @ResponseBody JsonResponse deleteVendor(@PathVariable int id) {
		JsonResponse jr = null;
		Optional<Vendor> v = vendorRepo.findById(id);
		try {
			if (v.isPresent()) {
				vendorRepo.deleteById(id);
				jr = JsonResponse.getInstance(v);
			} else {
				jr = JsonResponse.getInstance(new Exception("Vendor delete unsuccessful, vendor " + id + " does not exist."));
			}
		} catch (Exception e) {
			jr = JsonResponse.getInstance(e);
		}
		return jr;
	}
	
}
