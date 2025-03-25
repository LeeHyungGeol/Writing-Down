package sample.cafekiosk.spring.api.service.product;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sample.cafekiosk.spring.api.service.product.request.ProductCreateServiceRequest;
import sample.cafekiosk.spring.api.service.product.response.ProductResponse;
import sample.cafekiosk.spring.domain.product.Product;
import sample.cafekiosk.spring.domain.product.ProductRepository;
import sample.cafekiosk.spring.domain.product.ProductSellingStatus;

import java.util.List;

/**
 * readOnly = true : 읽기전용
 * CRUD 에서 CUD 동작 X / only R
 * JPA : CUD 스냅샷 저장 X, 변경감지 X -> 성능 향상
 *
 * CQRS: Command 랑 Query 를 분리 (2:8) Read 가 보통 더 많다.
 */
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class ProductService {

	private final ProductRepository productRepository;

	// 동시성 이슈
	// 1. 동시 요청이 엄청 높지는 않는 경우, 시스템 상에서 재시도를 3번 정도 하게 만든다.
	// 2. 동시 요청이 엄청 높은 경우, 정책을 바꿔서 int 1 증가가 아닌 UUID 로 바꾼다.
	@Transactional
	public ProductResponse createProduct(ProductCreateServiceRequest request) {
		// nextProductNumber
		String nextProductNumber = createNextProductNumber();

		Product product = request.toEntity(nextProductNumber);
		Product savedProduct = productRepository.save(product);

		return ProductResponse.of(savedProduct);
	}

	public List<ProductResponse> getSellingProducts() {
		List<Product> products = productRepository.findAllBySellingStatusIn(ProductSellingStatus.forDisplay());

		return products.stream()
			.map(ProductResponse::of)
			.toList();
	}

	private String createNextProductNumber() {
		String latestProductNumber = productRepository.findLatestProductNumber();
		if (latestProductNumber == null) {
			return "001";
		}

		int nextProductNumberInt = Integer.parseInt(latestProductNumber) + 1;

		return String.format("%03d", nextProductNumberInt);
	}

}
