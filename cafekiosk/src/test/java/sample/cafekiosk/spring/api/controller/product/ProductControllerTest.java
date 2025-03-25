package sample.cafekiosk.spring.api.controller.product;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import sample.cafekiosk.spring.api.service.product.ProductService;
import sample.cafekiosk.spring.api.service.product.response.ProductResponse;
import sample.cafekiosk.spring.domain.product.ProductCreateRequest;
import sample.cafekiosk.spring.domain.product.ProductSellingStatus;
import sample.cafekiosk.spring.domain.product.ProductType;

import java.util.List;

import static org.mockito.Mockito.*;

// @SpringBootTest 는 전체 Spring Bean 컨텍스트를 로드한다.
// @WebMvcTest 는 웹 계층(Controller test)에 초점을 맞추어 테스트하기 위해 Controller 관련 Bean 만 로드한다.
@WebMvcTest(controllers = ProductController.class)
class ProductControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@MockBean
	private ProductService productService;

	@Test
	@DisplayName("신규 상품을 등록한다.")
	void createProduct() throws Exception {
		// given
		ProductCreateRequest request = ProductCreateRequest.builder()
			.type(ProductType.HANDMADE)
			.sellingStatus(ProductSellingStatus.SELLING)
			.name("아메리카노")
			.price(4000)
			.build();

		// when // then
		mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/products/new")
			.content(objectMapper.writeValueAsString(request))
			.contentType(MediaType.APPLICATION_JSON)
		)
			.andDo(MockMvcResultHandlers.print())
			.andExpect(MockMvcResultMatchers.status().isOk());


	}

	@Test
	@DisplayName("신규 상품을 등록할 때 상품 타입은 필수값이다.")
	void createProductWithoutType() throws Exception {
		// given
		ProductCreateRequest request = ProductCreateRequest.builder()
			.sellingStatus(ProductSellingStatus.SELLING)
			.name("아메리카노")
			.price(4000)
			.build();

		// when // then
		mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/products/new")
				.content(objectMapper.writeValueAsString(request))
				.contentType(MediaType.APPLICATION_JSON)
			)
			.andDo(MockMvcResultHandlers.print())
			.andExpect(MockMvcResultMatchers.status().isBadRequest())
			.andExpect(MockMvcResultMatchers.jsonPath("$.code").value("400"))
			.andExpect(MockMvcResultMatchers.jsonPath("$.status").value("BAD_REQUEST"))
			.andExpect(MockMvcResultMatchers.jsonPath("$.message").value("상품 타입은 필수입니다."))
			.andExpect(MockMvcResultMatchers.jsonPath("$.data").isEmpty());


	}

	@Test
	@DisplayName("신규 상품을 등록할 때 상품 판매상태는 필수값이다.")
	void createProductWithoutSellingStatus() throws Exception {
		// given
		ProductCreateRequest request = ProductCreateRequest.builder()
			.type(ProductType.HANDMADE)
			.name("아메리카노")
			.price(4000)
			.build();

		// when // then
		mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/products/new")
				.content(objectMapper.writeValueAsString(request))
				.contentType(MediaType.APPLICATION_JSON)
			)
			.andDo(MockMvcResultHandlers.print())
			.andExpect(MockMvcResultMatchers.status().isBadRequest())
			.andExpect(MockMvcResultMatchers.jsonPath("$.code").value("400"))
			.andExpect(MockMvcResultMatchers.jsonPath("$.status").value("BAD_REQUEST"))
			.andExpect(MockMvcResultMatchers.jsonPath("$.message").value("상품 판매상태는 필수입니다."))
			.andExpect(MockMvcResultMatchers.jsonPath("$.data").isEmpty());


	}

	@Test
	@DisplayName("신규 상품을 등록할 때 상품 이름은 필수값이다.")
	void createProductWithoutName() throws Exception {
		// given
		ProductCreateRequest request = ProductCreateRequest.builder()
			.type(ProductType.HANDMADE)
			.sellingStatus(ProductSellingStatus.SELLING)
			.price(4000)
			.build();

		// when // then
		mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/products/new")
				.content(objectMapper.writeValueAsString(request))
				.contentType(MediaType.APPLICATION_JSON)
			)
			.andDo(MockMvcResultHandlers.print())
			.andExpect(MockMvcResultMatchers.status().isBadRequest())
			.andExpect(MockMvcResultMatchers.jsonPath("$.code").value("400"))
			.andExpect(MockMvcResultMatchers.jsonPath("$.status").value("BAD_REQUEST"))
			.andExpect(MockMvcResultMatchers.jsonPath("$.message").value("상품 이름인 필수입니다."))
			.andExpect(MockMvcResultMatchers.jsonPath("$.data").isEmpty());


	}

	@Test
	@DisplayName("신규 상품을 등록할 때 상품 가격은 양수이다.")
	void createProductWithZeroPrice() throws Exception {
		// given
		ProductCreateRequest request = ProductCreateRequest.builder()
			.type(ProductType.HANDMADE)
			.sellingStatus(ProductSellingStatus.SELLING)
			.name("아메리카노")
			.price(0)
			.build();

		// when // then
		mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/products/new")
				.content(objectMapper.writeValueAsString(request))
				.contentType(MediaType.APPLICATION_JSON)
			)
			.andDo(MockMvcResultHandlers.print())
			.andExpect(MockMvcResultMatchers.status().isBadRequest())
			.andExpect(MockMvcResultMatchers.jsonPath("$.code").value("400"))
			.andExpect(MockMvcResultMatchers.jsonPath("$.status").value("BAD_REQUEST"))
			.andExpect(MockMvcResultMatchers.jsonPath("$.message").value("상품 가격은 양수여야 합니다."))
			.andExpect(MockMvcResultMatchers.jsonPath("$.data").isEmpty());


	}

	@Test
	@DisplayName("판매 상품을 조회한다.")
	void getSellingProducts() throws Exception {
		// given
		List<ProductResponse> result = List.of();

		when(productService.getSellingProducts()).thenReturn(result);

		// when // then
		mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/products/selling")
			)
			.andDo(MockMvcResultHandlers.print())
			.andExpect(MockMvcResultMatchers.status().isOk())
			.andExpect(MockMvcResultMatchers.jsonPath("$.code").value("200"))
			.andExpect(MockMvcResultMatchers.jsonPath("$.status").value("OK"))
			.andExpect(MockMvcResultMatchers.jsonPath("$.message").value("OK"))
			.andExpect(MockMvcResultMatchers.jsonPath("$.data").isArray());


	}
}
