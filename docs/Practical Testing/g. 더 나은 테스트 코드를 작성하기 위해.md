# 더 나은 테스트 코드를 작성하기 위해

# 💡 한 문단에 한 주제!

- 분기문, 반복문이 테스트 코드에 들어간다는 것 자체가 한 문단에 두 가지 이상의 내용이 들어가 있다는 뜻이다.

> **분기문, 반복문과 같은 논리 구조가 테스트 코드에 포함되면 테스트 코드가 지향하는 무엇을 테스트하고자 하는지 어떤 환경을 구성하고자 하는지에 대한 것들을 방해하게 된다!** 
- 만약에 이런 **Case 확장**이 필요하다면 `@ParameterizedTest` 를 사용하자.
- `@DisplayName` 을 한 문장으로 치환해서 사용할 수 있는가로 판단해봐도 좋을 것이다!

분기문, 반복문 요런 것들을 지양하는 것이 좋다.

# 💡 완벽하게 제어하기

> **`현재 시간`, `랜덤 값`, `외부 API 호출` 등과 같이 같이 내가 제어할 수 없는 값들은 (가능하다면) 메서드 파라미터로 분리하여 외부로부터 주입하도록 하고, 테스트 시에는 제어할 수 있는 시간값을 주입하여 테스트하자!**

- **제어할 수 없는 값을 분리하여 상위 계층으로 올리고, 그에 따라 하위 계층을 테스트에 유연한 구조로 만들자!**
- 다만, 이 제어할 수 없는 값을 분리하여 상위 계층으로 올리는 설계가, 무조건 최상단 계층까지 올려야 한다는 의미는 아니다!
- 어느 계층까지 분리하여도 비즈니스 로직의 맥락이 깨지지 않는지, 어느 수준까지 테스트 가능하도록 만들 것인지 고민이 필요하다!
- 보통 자연스러운 맥락 안에서는, Controller에서 현재 시간값을 생성하여 Service에 넘겨주도록 하거나, 가능하다면 아예 외부에서 요청이 들어올 때 시간값을 받도록 고민하며 설계한다!

# 💡 테스트 환경의 독립성을 보장하자

```java
@Test
@DisplayName("재고가 부족한 상품으로 주문을 생성하려는 경우 예외가 발생한다.")
void createOrderWithNoStock() {
	// given
	LocalDateTime registeredDateTime = LocalDateTime.now();
	
	Product product1 = createProduct("001", BOTTLE, 1000);
	Product product2 = createProduct("002", BAKERY, 3000);
	Product product3 = createProduct("003", HANDMADE, 5000);
	productRepository.saveAll(List.of(product1, product2, product3));
	
	Stock stock1 = Stock.create("001", 2);
	Stock stock2 = Stock.create("002", 2);
	stock1.deductQuantity(1); // todo: 이런식으로 테스트하는게 맞을까?
	stockRepository.saveAll(List.of(stock1, stock2));
	
	OrderCreateServiceRequest request = OrderCreateServiceRequest.builder()
            .productNumbers(List.of("001", "001", "002", "003"))
            .build();	
	
	// when // then
	assertThatThrownBy(() -> orderService.createOrder(request, registeredDateTime))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("재고가 부족한 상품이 있습니다.");
}
```

위의 테스트 코드에서 given 절을 읽을 때
1. `stock.deductQuantity(1);` 라는 코드 때문에 논리 구조가 생겨서 테스트 코드의 맥락을 한번 더 이해해야 하는 상황이 발생한다.
2. `stock.deductQuantity(3);` 만약에 다음과 같이 3 으로 변경한다면 테스트에 실패하게 되는데 원래 테스트는 실패를 해도 when 절, then 절에서 실패해야 한다. 하지만, given 절에서 실패하게 된 것이다.
  - 즉, 이런 경우는 테스트 주제와 맞지 않는 부분에서 테스트가 실패한 것이다!

> **따라서, 최대한 테스트 환경을 조성할 때는 `순수 생성자 기반`으로 조성하는 것이 좋다!**
- **팩토리 메서드도 가급적이면 지양한다!**
  - 생성자가 아닌 굳이 팩토리 메서드를 만들었다는 얘기는 팩토리 메서드 내에서 **검증을 하거나 필요한 인자만 받아서 구성을 하는** 등 `목적이 들어간 생성 구문`이기 때문이다.
- **`순수 Builder` 패턴을 사용하는 것도 좋은 방법이다!**
- 검증 대상인 when절에서 팩토리 메서드를 사용하지 말라는 것이 아니라, `given절의 fixture`를 만들 때 **그러지 않기를 이야기하는 것이다.**

# 💡 2개 이상의 테스트 간 독립성을 보장하자

```java
class StockTest {
	
	private static final Stock stock = Stock.create("001", 1);

	@Test
	@DisplayName("재고의 수량이 제공된 수량보다 작은지 확인한다.")
	void isQuantityLessThan() {
		// given
		int quantity = 2;

		// when
		boolean result = stock.isQuantityLessThan(quantity);

		// then
		assertThat(result).isTrue();
	}

	@Test
	@DisplayName("재고를 주어진 개수만큼 차감할 수 있다.")
	void deductQuantity() {
		// given
		int quantity = 1;

		// when
		stock.deductQuantity(quantity);

		// then
		assertThat(stock.getQuantity()).isZero();
	}
}
```

```java
private static final Stock stock = Stock.create("001", 1);
```

> 2개 이상의 테스트가 공유 자원인 static 변수를 공유한다!

- 이렇게 공유 자원을 사용하는 경우 기본적으로 `테스트 간에 순서`라는 것이 발생해버린다!

> 하지만, 테스트 간에 순서라는 것 자체가 없어야 되고, 각각 독립적으로 언제 수행되든 항상 같은 결과를 내야 한다.

- 만약, 하나의 인스턴스가 변화하는 모습을 테스트 하고 싶다면 `@DynamixTest` 를 사용하자!

# 💡 한 눈에 들어오는 Test Fixture 구성하기

> `Test Fixture`: 테스트를 위해 원하는 상태로 고정시킨 일련의 객체

- 보통 given 절을 구성할 때 사용한다.

## ⚡️ @BeforeAll, @BeforeEach 사용하지 않기!

```java
@BeforeAll
static void beforeAll() {
	// before class
}

@BeforeEach
void setUp() {
  // before method
}

// 1. 각 테스트 입장에서 봤을 때, 아예 몰라도 내용을 이해하는데 문제가 없는가?
// 2. 수정해도 모든 테스트에 영향을 주지 않는가?
```

- setUp 의 경우, 각 메서드 간에 동일한 자원을 공유하는 효과를 갖게 된다. **이렇게 되면 테스트 간 독립성을 보장하지 못하게 된다!**
1. setUp 에 있는 fixture 를 수정할 경우에 **모든 메서드에 영향을 미치게 된다!**
2. 앞서 말했듯이, 테스트는 `문서`다! 그런데, beforeAll(), setUp() 을 사용하게 되면 **정보가 파편화**가 되어 **가독성이 떨어지고, 흐름을 파악하기 번거롭게 된다!**

> **fixture들은 웬만하면 given 절에 구성하는 것이 좋다!** _given 절이 많이 길어질 수도 있지만_ `문서`로서의 **테스트 코드 기능이 더 중요**하다는 것을 명심하자!


## ⚡️ data.sql 로 테스트 데이터 구성하지 말기! 굉장히 추천하지 않는 방법이다!!

> 마찬가지로, `정보의 파편화`가 일어나기 때문이다!

- 또한, 서비스가 커지면 커질수록 다루는 데이터 및 엔티티도 많아지게 되는데, 그때마다 data.sql 을 관리할 수 있나?에 대한 문제이다. 또 다른 관리 포인트가 생긴다.

## ⚡️ 빌더를 만들 때 각 테스트 클래스에 `꼭 필요한 필드`만 명시하기!

```java
@ActiveProfiles("test")
@SpringBootTest
class ProductServiceTest {

	private Product createProduct(String productNumber, ProductType type, ProductSellingStatus sellingStatus, String name, int price) {
		return Product.builder()
			.productNumber(productNumber)
			.type(type)
			.sellingStatus(sellingStatus)
			.name(name)
			.price(price)
			.build();
	}
}
```

```java
@ActiveProfiles("test")
@SpringBootTest
class OrderServiceTest {

	private Product createProduct(String productNumber, ProductType type, int price) {
		return Product.builder()
			.productNumber(productNumber)
			.type(type)
			.sellingStatus(SELLING)
			.name("메뉴 이름")
			.price(price)
			.build();
	}
}
```

## ⚡️ 귀찮지만 테스트 클래스 마다 빌더를 구성해서 fixture를 만들자!

마찬가지로, 하나의 클래스에서 빌더를 구성하는 것을 담당할 시에 빌더의 필드 순서만 다르거나 하나의 필드만 달라도 종류가 수십가지로 불어나게 된다.    
똑같이 관리가 어려워지고 오히려 더 귀찮아진다.

> 이 문제는 kotlin 언어로 가면 문제가 조금은 해소될 수가 있다. kotlin 을 사용하면 lombok도 필요 없다.

# 💡 Test Fixture 클렌징

> **결론! `deleteAllInBatch()` 혹은 `@Transactional` 을 사용하자!**
- 다만, @Transactional 의 경우는 주의하면서 사용하자!
- EX) Spring Batch 와 배치 통합 테스트와 같은 것을 사용하면 여러 트랜잭션의 경계가 겹치기 때문에 그럴 때는 deleteAllInBatch() 를 사용하자.

## 📌 deleteAllInBatch() vs deleteAll()

```java
@AfterEach
void tearDown() {
	orderProductRepository.deleteAllInBatch();
	orderRepository.deleteAllInBatch();
	productRepository.deleteAllInBatch();
	stockRepository.deleteAllInBatch();
}
```

> `deleteAllInBatch()`: **테이블 전체**를 **bulk delete** 한다. (`truncate table`)

- 대신 `순서`를 잘 고려해야 한다! -> **`외래키 조건` 주의!**

```java
@AfterEach
void tearDown() {
	orderRepository.deleteAll();
	productRepository.deleteAll();
	stockRepository.deleteAll();
}
```

> `deleteAll()`: 전체 테이블을 select 한 후에, **하나씩 delete** 한다. **성능 차이 발생!**  

# 💡 @ParameterizedTest

> **단순하게 딱 하나의 test case 인데, 값이나 어떤 환경에 대한 데이터들을 여러 개로 바꿔보면서 테스트하고 싶을 때 사용하자!**

**AS-IS**

```java
class ProductTypeTest { 
	
	@Test
	@DisplayName("상품 타입이 재고 관련 타입인지를 체크한다.")
	void containsStockType2() {
		// given
		ProductType givenType1 = ProductType.HANDMADE;
		ProductType givenType2 = ProductType.BOTTLE;
		ProductType givenType3 = ProductType.BAKERY;
		
		// when
		boolean result1 = ProductType.containsStockType(givenType1);
		boolean result2 = ProductType.containsStockType(givenType2);
		boolean result3 = ProductType.containsStockType(givenType3);
		
		// then
		assertThat(result1).isFalse();
		assertThat(result2).isTrue();
		assertThat(result3).isTrue();
	}
}
```

**TO-BE**

```java
class ProductTypeTest {

	@CsvSource({"HANDMADE, false", "BOTTLE, true", "BAKERY, true"})
	@ParameterizedTest
	@DisplayName("상품 타입이 재고 관련 타입인지를 체크한다.")
	void containsStockType(ProductType productType, boolean expected) {
		// when
		boolean result = ProductType.containsStockType(productType);
		// then
		assertThat(result).isEqualTo(expected);
	}
}
```

```java
private static Stream<Arguments> provideProductTypesForCheckingStockType() {
	return Stream.of(
		Arguments.of(ProductType.HANDMADE, false), 
            Arguments.of(ProductType.BOTTLE, true), 
            Arguments.of(ProductType.BAKERY, true)
    );
}

@DisplayName("상품 타입이 재고 관련 타입인지를 체크한다.") 
@MethodSource("provideProductTypesForCheckingStockType") 
@ParameterizedTest 
void containsStockType5(ProductType productType, boolean expected) {
	// when
	boolean result = ProductType.containsStockType(productType);
	// then
	assertThat(result).isEqualTo(expected);
}
```

- `@ParameterizedTest` 의 source 로 **자주 사용하는 것**은 위의 2개의 예시와 같이 `@CsvSource` 와 `@MethodSource` 이다.

`Spock Framework`(테스트 프레임워크?)에도 비슷한 기능을 하는 것이 있다!

### 📝 Reference

- [Junit5 Parameterized Tests](https://junit.org/junit5/docs/current/user-guide/#writing-tests-parameterized-tests)
- [Spock Data Tables](https://spockframework.org/spock/docs/2.3/data_driven_testing.html#data-tables)

# 💡 @DynamicTest

> `@DynamicTest`: 어떤 환경을 설정해놓고 이 환경에 변화를 주면서 중간중간에 검증을 하고 또 이런 행위를 했을 때 이런 검증이 되고 하는 일련의 시나리오를 테스트할 때 사용한다. 어떤 하나의 환경을 설정하고 사용자 시나리오를 테스트할 때 사용한다. 

> **일련의 시나리오가 있을 때 사용하자!**

```java
@DisplayName("재고 차감 시나리오")
@TestFactory
Collection<DynamicTest> stockDeductionDynamicTest() {
	// given
	Stock stock = Stock.create("001", 1);

	return List.of(
		DynamicTest.dynamicTest("재고를 주어진 개수만큼 차감할 수 있다.", () -> {
			// given
			int quantity = 1;

			// when
			stock.deductQuantity(quantity);

			// then
			assertThat(stock.getQuantity()).isZero();
		}),
		DynamicTest.dynamicTest("재고보다 많은 수의 수량으로 차감 시도하는 경우 예외가 발생한다.", () -> {
			// given
			int quantity = 1;

			// when // then
			assertThatThrownBy(() -> stock.deductQuantity(quantity))
				.isInstanceOf(IllegalStateException.class)
				.hasMessage("차감할 재고 수량이 없습니다.");
		})
	);
}
```

- 어떤 시나리오를 기반으로 상태를 공유하면서 환경을 공유하면서 테스트를 작성할 수 있다.

# 💡 테스트 수행도 비용이다. 환경 통합하기

이 테스트 자체를 수행하려면 테스트가 수행되는 시간도 다 비용이다. 이런 것들도 다 관리가 필요하다.

> `gradle > application(cafekiosk) > Tasks > verification > test` 을 통해 **전체 테스트**를 한번에 수행할 수 있다.

Test code 에서 `@SpringBootTest` 를 사용하면 **Spring Context** 를 띄우기 때문에 **테스트 수행 시간이 오래 걸린다.**
- 테스트 코드가 있는 클래스에 `@SpringBootTest` 를 붙인 수 만큼 **매번 새롭게** `Spring Context`를 띄우기 때문에 그만큼 시간이 더 걸리게 된다!
- 같은 Spring Boot 테스트여도 이런 Profile 지정 차이 등, 띄우는 환경이 달라지면 **매번 새롭게** 띄우게 된다.

현재는 `OrderControllerTest`, `ProductControllerTest`, `OrderServiceTest`, `ProductServiceTest`, `OrderStatisticsServiceTest`, `ProductRepositoryTest`, `StockRepositoryTest` 총 6번 Spring Server 를 띄우고 있다.

> **`공통 테스트 환경을 추출`해서 Spring Context 를 한번만 띄우고, 그것을 공유해서 사용하도록 하자!**


## ⚡️ 공통 부분을 뽑아낸 상위 클래스를 만들자!

```java
@ActiveProfiles("test")
@SpringBootTest
public abstract class IntegrationTestSupport {

	@MockBean
	protected MailSendClient mailSendClient;
}
```

`@MockBean` 의 경우도 Spring Context 에 등록되어있는 컴포넌트를 Mocking 해서 `Mock 객체`로 교체하는 것이기 때문에 새로 **Spring Context** 를 띄우는 것이다.

1. `@MockBean` 처리한 것을 **상위 클래스**로 올려서 처리한다.
2. 다른 클래스의 테스트에서는 MockBean 이 필요 없을 수 있기 때문에, 이럴 때는 **상위 클래스를 2개** 만들어서 케이스에 맞게 사용할 수도 있다.

### ✅ @DataJpaTest 도 서버를 새로 띄우게 한다!

**@SpringBootTest 와 마찬가지로 @DataJpaTest 도 어쨌든 Spring 서버를 띄우기 때문에 그만큼 시간이 더 걸리게 된다.**

- `@DataJpaTest`: JPA 관련된 빈만 등록하여 테스트를 진행한다. `@SpringBootTest` 보다 빠르게 테스트를 진행할 수 있고, @Transactional 이 붙어 있어서 트랜잭션 롤백도 자연스럽게 사용할 수 있다.

> JPA 관련 빈들만을 위한 Spring 서버를 띄우는 것이 특별한 장점이 있지 않다면, **Service 랑 Repository 둘 다 `@SpringBootTest` 를 사용해서 Test 를 실행하자!**


### ✅ Controller 는 따로 상위 클래스를 만들어서 사용하자!

`Controller` 는 MockMvc 를 사용한 `@WebMvcTest` 를 사용하기 때문에 **테스트 환경을 위한 상위 클래스를 따로 만들자**
- Integration Test 와 달리 Controller Test는 Service 를 Mocking 헤서 Controller layer 만 가볍게 빠르게 검증한 것이기 때문이다.

```java
@WebMvcTest(controllers = {
	ProductController.class,
	OrderController.class
})
public abstract class ControllerTestSupport {

	@Autowired
	protected MockMvc mockMvc;

	@MockBean
	protected ProductService productService;

	@Autowired
	protected ObjectMapper objectMapper;

	@MockBean
	protected OrderService orderService;

}
```

# 💡 private 메서드의 테스트는 어떻게 하나?

## 📌 private 메서드는 테스트할 필요가 없다!

> 다만, private 메서드를 테스트 하고 싶은 시점에 고민할 포인트가 있다. **객체를 분리할 시점인가?**

어떤 클래스가 public 메서드를 가지고 있다는 것은, 해당 클래스를 사용하는 client 입장에서는 그 public 메서드 즉, `public api`만 알고 있으면 된다는 의미이다. private 메서드는 내부 구현 세부사항으로, client가 알 필요도 없고, 알아서도 안 된다.

## 🧐 그렇다면 객체 분리의 기준은?

> 일단 private 메서드를 객체로 분리하는 것 이전에 고려되어야 할 것은, **해당 private 메서드를 사용하는 public 메서드를 통해 충분히 테스팅을 시도하는 것입니다.**
만약 그렇게 하더라도 충분치 않고 뭔가 과도한 책임을 가지고 있는 것 같다면, 그 때 객체 분리를 고려하는 것이죠.

> **가장 좋은 접근 방법은, 테스트 코드와 함께 먼저 비즈니스 로직을 구현하고, 이후 분리해야겠다는 생각이 들었을 때 테스트 코드의 지지를 받으면서 객체를 분리하고 적절한 책임을 할당하는 방법입니다.
즉, 필요하지 않은 시점에 미리 객체를 잘게 쪼개어 두는 방법은 오히려 때에 따라 오버 엔지니어링일 수 있다는 뜻입니다.**


**AS-IS**

```java
public class ProductService {

	private final ProductRepository productRepository;
	
	public ProductResponse createProduct(ProductCreateServiceRequest request) {
		// nextProductNumber
		String nextProductNumber = productNumberFactory.createNextProductNumber();
		
		// ...
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

```

**TO-BE**

```java
@RequiredArgsConstructor
@Component
public class ProductNumberFactory {
	
	private final ProductRepository productRepository;

	public String createNextProductNumber() {
		String latestProductNumber = productRepository.findLatestProductNumber();
		if (latestProductNumber == null) {
			return "001";
		}

		int nextProductNumberInt = Integer.parseInt(latestProductNumber) + 1;

		return String.format("%03d", nextProductNumberInt);
	}
}
```

```java
public class ProductService { 
	
	private final ProductRepository productRepository;
	private final ProductNumberFactory productNumberFactory;
	
	public ProductResponse createProduct(ProductCreateServiceRequest request) {
		String nextProductNumber = productNumberFactory.createNextProductNumber();
	}
}
```

- `createNextNumber` 에 대한 `책임`을 `ProductService` 에서 담당할 책임이 아니라고 보고 `ProductNumberFactory` 를 **객체를 따로 분리하였다.**

# 💡 테스트에서만 필요한 메서드가 생겼는데 프로덕션 코드에서는 필요 없다면?

## 📌 만들어도 된다. 하지만 `보수적`으로 접근하기

> getter, 기본 생성자, 생성자 빌더, 사이즈 등 **즉, 어떤 객체가 마땅히 가져도 되는 행위라고 생각 되면서 미래에 충분히 사용될 수 있는 성격의 메소드라면 사용해도 된다.**

# 💡 키워드 정리

<img width="500" alt="" src="https://github.com/user-attachments/assets/6bf997b3-9b70-4f32-8113-32319896ec5e"/>
