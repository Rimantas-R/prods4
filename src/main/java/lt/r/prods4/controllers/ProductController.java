package lt.r.prods4.controllers;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;// reikalingas užkomentuotam metodui iš deleteProduct()

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import lt.r.prods4.models.Product;

@RestController
public class ProductController {

	private static final List<Product> PRODUCTS = new ArrayList<>();

	static {
		PRODUCTS.add(new Product(1, "name1", "description1"));
		PRODUCTS.add(new Product(2, "name2", "description2"));
		PRODUCTS.add(new Product(3, "name3", "description3"));
	}

	@GetMapping(value = "/products", produces = MediaType.APPLICATION_JSON_VALUE)
	List<Product> getProducts(@RequestParam(required = false) String prodFrag) {
		if (StringUtils.isEmpty(prodFrag)) {
			return PRODUCTS;
		}
		return PRODUCTS.stream().filter(p -> p.getName().contains(prodFrag)).collect(Collectors.toList());
	}

	@GetMapping(value = "/products/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	ResponseEntity<Product> getProduct(@PathVariable("id") int productId) {
		return getFirstProduct(productId).map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
	}

	private Optional<Product> getFirstProduct(int productId) {
		return PRODUCTS.stream().filter(p -> p.getId() == productId).findFirst();
	}

	@PostMapping(value = "/products", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	ResponseEntity<?> createProduct(@RequestBody Product product) {
		if (product.getId() != null) {
			return ResponseEntity.status(HttpStatus.PRECONDITION_FAILED).body("Id of new product should be null");
		}
		product.setId(generateId());
		PRODUCTS.add(product);
//		return new ResponseEntity<Product>(product, HttpStatus.CREATED);
		return ResponseEntity.status(HttpStatus.CREATED).body(product);
	}

	private Integer generateId() {
		return PRODUCTS.stream().mapToInt(p -> p.getId()).max().orElse(0) + 1;
	}

	@DeleteMapping("/products/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	void deleteProduct(@PathVariable Integer id) {
		// PRODUCTS =
		// PRODUCTS.stream().filter(p->p.getId()!=id).collect(Collectors.toList());
		PRODUCTS.removeIf(p -> p.getId() == id);
	}

	@PutMapping(value = "/products", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	ResponseEntity<Product> updateProduct(@RequestBody Product product) {
		Optional<Product> foundProduct = getFirstProduct(product.getId());
		if (!foundProduct.isPresent()) {
			return ResponseEntity.notFound().build();
		}

		Product updatedProduct = updateProductParams(product, foundProduct);

		return ResponseEntity.ok(updatedProduct);
	}

	@PutMapping(value = "/products/{id}", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	ResponseEntity<Product> updateProduct(@PathVariable Integer id, @RequestBody Product product) {
		Optional<Product> foundProduct = getFirstProduct(id);
		if (!foundProduct.isPresent()) {
			return ResponseEntity.notFound().build();
		}

		Product updatedProduct = updateProductParams(product, foundProduct);

		return ResponseEntity.ok(updatedProduct);
	}

	/**
	 * @param product
	 * @param foundProduct
	 * @return
	 */
	private Product updateProductParams(Product product, Optional<Product> foundProduct) {
		Product updatedProduct = foundProduct.get();
		updatedProduct.setName(product.getName());
		updatedProduct.setDescription(product.getDescription());
		updatedProduct.setCreateDate(product.getCreateDate());
		return updatedProduct;
	}

}
