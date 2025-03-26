# Mock을 마주하는 자세

# 💡 Mockito로 Stubbing하기

## 📝 요구사항 추가

- 주문 통계 총 금액에 관해 해당 계산 결과를 원하는 날짜에 메일을 발송하는 기능 추가

## ⚡️ 외부 API 호출 시에 Email 전송과 같은 경우, 테스트할 때 마다 진짜 메일을 전송할 것인가?

- 기본적으로 Mock 이라는 것은 가짜 객체를 넣어놓고 이 가짜 객체가 어떻게 동작하고 어떤 결과를 줄 것인지를 정의해놓는 것이다.

> 외부 API(ex: MailSendClient)를 @MockBean 으로 처리했고, 이 행위에 대한 가짜 행위로 원하는 결과를 정의해놓는다. 이를 Stubbing 이라고 한다.

```java
@SpringBootTest
class OrderStatisticsServiceTest {

	@Autowired
	private OrderStatisticsService orderStatisticsService;

	@Autowired
	private OrderRepository orderRepository;

	@Autowired
	private ProductRepository productRepository;

	@Autowired
	private OrderProductRepository orderProductRepository;

	@Autowired
	private MailSendHistoryRepository mailSendHistoryRepository;

	@MockBean
	private MailSendClient mailSendClient;

	@AfterEach
	void tearDown() {
		orderProductRepository.deleteAllInBatch();
		orderRepository.deleteAllInBatch();
		productRepository.deleteAllInBatch();
		mailSendHistoryRepository.deleteAllInBatch();
	}

	@Test
	@DisplayName("결제완료 주문들을 조회하여 매출 통계 메일을 전송한다.")
	void sendOrderStatisticsMail() {
		// given
		LocalDateTime now = LocalDateTime.of(2023, 3, 5, 0, 0);

		Product product1 = createProduct("001", HANDMADE, 1000);
		Product product2 = createProduct("002", HANDMADE, 2000);
		Product product3 = createProduct("003", HANDMADE, 3000);
		List<Product> products = List.of(product1, product2, product3);
		productRepository.saveAll(products);

		Order order1 = createPaymentCompletedOrder(products, LocalDateTime.of(2023, 3, 4, 23, 59, 59));
		Order order2 = createPaymentCompletedOrder(products, now);
		Order order3 = createPaymentCompletedOrder(products, LocalDateTime.of(2023, 3, 5, 23, 59, 59));
		Order order4 = createPaymentCompletedOrder(products, LocalDateTime.of(2023, 3, 6, 0, 0));

		// Stubbing
		Mockito.when(mailSendClient.sendEmail(any(String.class), any(String.class), any(String.class), any(String.class)))
			.thenReturn(true);

		// when
		boolean result = orderStatisticsService.sendOrderStatisticsEmail(LocalDate.of(2023, 3, 5), "test@test.com");

		// then
		assertThat(result).isTrue();

		List<MailSendHistory> histories = mailSendHistoryRepository.findAll();
		assertThat(histories).hasSize(1)
			.extracting("content")
			.contains("총 매출 합계는 12000원입니다.");
	}
}
```

```java
@Slf4j
@Component
public class MailSendClient {


	public boolean sendEmail(String fromEmail, String toEmail, String subject, String content) {
		// 메일 전송
		log.info("메일 전송 from: {}, to: {}, subject: {}, content: {}", fromEmail, toEmail, subject, content);
		throw new IllegalArgumentException("메일 전송에 실패했습니다.");
//		return true;
	}
}
```

## ✅ Mail 전송과 같은 로직에는 @Transactional 을 안붙이는 것이 좋다!

- 트랜잭션을 갖고 DB 로직 수행을 하게 되는데, 이때 DB 와 연결을 위해 Connection 을 맺고, Connection 자원을 계속 소유하고 있게 된다. 이는 작업이 끝마칠 때 Connection을 반환한다.
- 이때 Mail 전송과 같은 `긴 네트워크`를 타거나 `외부 API 호출`과 같은 ***긴 작업***이 실제로 트랜잭션에는 참여하지 않아도 되는 작업이기 때문에 **`@Transactional` 을 붙이지 않는 것이 좋다.**
- 어차피 조회할 때는 `조회용 Transaction` 이 `Repository` 단에서 따로 걸리기 때문에 상관없다. 

# 💡 Test Double: Test 대역

- `Dummy`: 아무것도 하지 않는 깡통 객체
- `Fake`: 단순한 형태로 동일한 기능은 수행하나, 프로덕션에서 쓰기에는 부족한 객체 (ex: Map<Integer, Entity> MemoryMapRepository, fakeRepository)
- `Stub`: 테스트에서 요청한 것에 대해 미리 준비한 결과를 제공하는 객체, 그외에는 응답하지 않는다.
- `Spy`: Stub이면서 호출된 내용을 내부적으로 기록(몇번 호출되었는지, Timeout은 몇번 일어났는지 등을 Tracking한다.)하여 보여줄 수 있는 객체, 일부는 실제 객체처럼 동작시키고 일부만 Stubbing할 수 있다.
- `Mock`: 행위에 대한 기대를 명세하고, 그에 따라 동작하도록 만들어진 객체

**Stub과 Mock이 헷갈린다!**

## 📌 Stub과 Mock의 차이

- `Stub`: ***`상태` 검증(State Verification)***: 테스트에서 요청한 것에 대해 미리 준비한 결과를 제공하는 객체, 그외에는 응답하지 않는다.
  - 어떤 메서드, 어떤 기능에 대한 요청을 했을 때, 이 Stub 이 상태가 어떻게 바뀌었고, 그 상태 검증을 하는 것이다. 내부적인 상태가 어떻게 바뀌었어에 대한 초점이 맞춰져있다.
- `Mock`: ***`행위` 검증(Behavior Verification)***: 행위에 대한 기대를 명세하고, 그에 따라 동작하도록 만들어진 객체
  - Mock 은 결국 **호출 여부**나 **호출 횟수** 같은 것을 검증한다.
