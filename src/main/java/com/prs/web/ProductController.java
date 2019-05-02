package com.prs.web;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;

import com.prs.business.product.Product;
import com.prs.business.product.ProductRepository;

@CrossOrigin
@RestController
@RequestMapping("/products")
public class ProductController {
	
	@Autowired
	private ProductRepository productRepo;
	
	@GetMapping("/")
	public JsonResponse getAll() {
		JsonResponse jr = null;
		try {
			jr = JsonResponse.getInstance(productRepo.findAll());
		} catch (Exception e) {
			jr = JsonResponse.getInstance(e);
		}
		return jr;		
	}
	
	@GetMapping("")
	public JsonResponse getProducts(@RequestParam int start, int limit) {
		JsonResponse jsonResponse = null;
		try {
			jsonResponse = JsonResponse.getInstance(productRepo.findAll(PageRequest.of(start, limit)));			
		} catch (Exception e) {
			jsonResponse = JsonResponse.getInstance(e);
		}		
		return jsonResponse;
	}
	
	@GetMapping("/{id}")
	public JsonResponse get(@PathVariable int id) {
		JsonResponse jr = null;		
		try {
			Optional<Product> p = productRepo.findById(id);
			if (p.isPresent()) {
				jr = JsonResponse.getInstance(p);
			} else {
				jr = JsonResponse.getInstance(new Exception("No product found for id = " + id));
			}
		} catch (Exception e) {
			jr = JsonResponse.getInstance(e);
		}		
		return jr;			
	}
	
	@PostMapping("/")
	public JsonResponse addProduct(@RequestBody Product p) {
		JsonResponse jr = null;
		jr = JsonResponse.getInstance(saveProduct(p));	
		return jr;
	}
	
	@PutMapping("/")
	public JsonResponse updateProduct(@RequestBody Product p) {
		return saveProduct(p);
	}

	private JsonResponse saveProduct(Product p) {
		JsonResponse jr = null;
		try {
			productRepo.save(p);
			jr = JsonResponse.getInstance(p);
		} catch (DataIntegrityViolationException e) {
			jr = JsonResponse.getInstance(new Exception(e.getMessage()));
		}
		return jr;
	}
	
	@DeleteMapping("/{id}")
	public @ResponseBody JsonResponse deleteProduct(@PathVariable int id) {
		JsonResponse jr = null;
		Optional<Product> p = productRepo.findById(id);
		try {
			if (p.isPresent()) {
				productRepo.deleteById(id);
				jr = JsonResponse.getInstance(p);
			} else {
				jr = JsonResponse.getInstance(new Exception("Product delete unsuccessful, product " + id + " does not exist."));
			}
		} catch (Exception e) {
			jr = JsonResponse.getInstance(e);
		}
		return jr;
	}
}
