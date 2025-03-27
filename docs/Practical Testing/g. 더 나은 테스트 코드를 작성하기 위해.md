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

- 대신 `순서`를 잘 고려해야 한다! > **`외래키 조건` 주의!**

```java
@AfterEach
void tearDown() {
	orderRepository.deleteAll();
	productRepository.deleteAll();
	stockRepository.deleteAllInBatch();
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

- `@ParameterizedTest` 의 source 로 **자주 사용하는 것**은 위의 2개의 예시와 같이 `Csv source` 와 `Method source` 이다.

Spock Framework(테스트 프레임워크?)에도 비슷한 기능을 하는 것이 있다!

### 📝 Reference

- [Junit5 Parameterized Tests](https://junit.org/junit5/docs/current/user-guide/#writing-tests-parameterized-tests)
- [Spock Data Tables](https://spockframework.org/spock/docs/2.3/data_driven_testing.html#data-tables)

