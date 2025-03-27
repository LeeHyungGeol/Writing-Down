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

