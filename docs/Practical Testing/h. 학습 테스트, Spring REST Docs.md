# 학습 테스트, Spring REST Docs

# 💡 학습 테스트

> 학습 테스트: 잘 모르느 기능, 라이브러리, 프레임워크를 학습하기 위해 작성하는 테스트

- 여러 테스트 케이스를 스스로 정의하고 검증하는 과정을 통해 보다 구체적인 동작과 기능을 학습할 수 있다.
- 관련 문서만 읽는 것보다 훨씬 재미있게 학습할 수 있다.

- [Google Guava 오픈소스](https://github.com/google/guava)

# 💡 Spring REST Docs

> Spring REST Docs: 테스트 코드를 통한 API 문서 자동화 도구

- API 명세를 문서로 만들고 외부에 제공함으로써 협업을 원활하게 한다.
- 기본적으로 `AsciiDoc`을 사용하여 문서를 작성한다.

## ⚡️ Spring REST Docs vs Swagger

- [Spring REST Docs](https://docs.spring.io/spring-restdocs/docs/current/reference/htmlsingle/)
- [Asciidoctor](https://asciidoctor.org/)
- [Swagger](https://swagger.io/)

### 📌 Spring REST Docs

#### ✅ 장점
- 테스트를 통과해야 문서가 만들어진다. (신뢰도가 높다.)
- 프로덕션 코드에 비침투적이다.

#### ❌ 단점
- 코드 양이 많다.
- 설정이 어렵다.

### 📌 Swagger

#### ✅ 장점
- 적용이 쉽다.
- 문서에서 바로 API 호출을 수행해볼 수 있다.

#### ❌ 단점
- 프로덕션 코드에 침투적이다.
- 테스트와 무관하기 때문에 신뢰도가 떨어질 수 있다.

## ⚡️ Spring REST Docs 적용 방법

```groovy
plugins {
	id "org.asciidoctor.jvm.convert" version "3.3.2"
}

configurations {
    compileOnly {
        extendsFrom annotationProcessor
    }
    asciidoctorExt
}

dependencies {
    // RestDocs
    asciidoctorExt 'org.springframework.restdocs:spring-restdocs-asciidoctor'
    testImplementation 'org.springframework.restdocs:spring-restdocs-mockmvc'
}


ext { // 전역 변수
    snippetsDir = file('build/generated-snippets')
}

test {
    outputs.dir snippetsDir
}

asciidoctor {
    inputs.dir snippetsDir
    configurations 'asciidoctorExt'

    sources { // 특정 파일만 html로 만든다.
        include("**/index.adoc")
    }
    baseDirFollowsSourceFile() // 다른 adoc 파일을 include 할 때 경로를 baseDir로 맞춘다.
    dependsOn test
}

bootJar {
    dependsOn asciidoctor
    from("${asciidoctor.outputDir}") {
        into 'static/docs'
    }
}
```

- 순서가 test > asciidoctor > bootJar 으로 차례대로 실행된다.

### 📌 테스트 코드에 적용하는 방법


```java
@ExtendWith(RestDocumentationExtension.class)
@SpringBootTest
public class RestDocsSupport {

	protected MockMvc mockMvc;

	@BeforeEach
	void setUp(WebApplicationContext webApplicationContext,
		   RestDocumentationContextProvider provider) {
		this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
			.apply(documentationConfiguration(provider))
			.build();
	}
}
```

```java
@ExtendWith(RestDocumentationExtension.class)
public abstract class RestDocsSupport {

	protected MockMvc mockMvc;
	protected ObjectMapper objectMapper = new ObjectMapper();

	@BeforeEach
	void setUp(RestDocumentationContextProvider provider) {
		this.mockMvc = MockMvcBuilders.standaloneSetup(initController())
			.apply(documentationConfiguration(provider))
			.build();
	}

	protected abstract Object initController();
}
```

```java
public class ProductControllerDocsTest extends RestDocsSupport {


	private final ProductService productService = mock(ProductService.class);

	@Override
	protected Object initController() {
		return new ProductController(productService);
	}
	
	@Test
	@DisplayName("신규 상품을 등록하는 API")
	void createProduct() throws Exception {
		ProductCreateRequest request = ProductCreateRequest.builder()
			.type(ProductType.HANDMADE)
			.sellingStatus(ProductSellingStatus.SELLING)
			.name("아메리카노")
			.price(4000)
			.build();

		BDDMockito.given(productService.createProduct(any(ProductCreateServiceRequest.class)))
				.willReturn(ProductResponse.builder()
					.id(1L)
					.productNumber("001")
					.type(ProductType.HANDMADE)
					.sellingStatus(ProductSellingStatus.SELLING)
					.name("아메리카노")
					.price(4000)
					.build());

		mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/products/new")
				.content(objectMapper.writeValueAsString(request))
				.contentType(MediaType.APPLICATION_JSON)
			)
			.andDo(MockMvcResultHandlers.print())
			.andExpect(MockMvcResultMatchers.status().isOk())
			.andDo(document("product-create",
				preprocessRequest(prettyPrint()),
				preprocessResponse(prettyPrint()),
				requestFields(
					fieldWithPath("type").type(JsonFieldType.STRING).description("상품 타입"),
					fieldWithPath("sellingStatus").type(JsonFieldType.STRING)
						.optional()
						.description("상품 판매상태"),
					fieldWithPath("name").type(JsonFieldType.STRING).description("상품 이름"),
					fieldWithPath("price").type(JsonFieldType.NUMBER).description("상품 가격")
				),
				responseFields(
					fieldWithPath("code").type(JsonFieldType.NUMBER).description("코드"),
					fieldWithPath("status").type(JsonFieldType.STRING).description("상태"),
					fieldWithPath("message").type(JsonFieldType.STRING).description("메시지"),
					fieldWithPath("data").type(JsonFieldType.OBJECT).description("응답 데이터"),
					fieldWithPath("data.id").type(JsonFieldType.NUMBER).description("상품 ID"),
					fieldWithPath("data.productNumber").type(JsonFieldType.STRING).description("상품 번호"),
					fieldWithPath("data.type").type(JsonFieldType.STRING).description("상품 타입"),
					fieldWithPath("data.sellingStatus").type(JsonFieldType.STRING).description("상품 판매상태"),
					fieldWithPath("data.name").type(JsonFieldType.STRING).description("상품 이름"),
					fieldWithPath("data.price").type(JsonFieldType.NUMBER).description("상품 가격")
				)
			));

	}
}
```

```snippet
====Request Fields
|===
|Path|Type|Optional|Description

{{#fields}}

|{{#tableCellContent}}`+{{path}}+`{{/tableCellContent}}
|{{#tableCellContent}}`+{{type}}+`{{/tableCellContent}}
|{{#tableCellContent}}{{#optional}}O{{/optional}}{{/tableCellContent}}
|{{#tableCellContent}}{{description}}{{/tableCellContent}}

{{/fields}}

|===
```

```snippet
==== Response Fields
|===
|Path|Type|Optional|Description

{{#fields}}

|{{#tableCellContent}}`+{{path}}+`{{/tableCellContent}}
|{{#tableCellContent}}`+{{type}}+`{{/tableCellContent}}
|{{#tableCellContent}}{{#optional}}O{{/optional}}{{/tableCellContent}}
|{{#tableCellContent}}{{description}}{{/tableCellContent}}

{{/fields}}

|===
```

```adoc
ifndef::snippets[]
:snippets: ../../build/generated-snippets
endif::[]
= CafeKiosk REST API 문서
:doctype: book
:icons: font
:source-highlighter: highlightjs
:toc: left
:toclevels: 2
:sectlinks:

[[Product-API]]
== Product API

include::api/product/product.adoc[]
```

```adoc
[[product-create]]
=== 신규 상품 등록

==== HTTP Request
include::{snippets}/product-create/http-request.adoc[]
include::{snippets}/product-create/request-fields.adoc[]

==== HTTP Response
include::{snippets}/product-create/http-response.adoc[]
include::{snippets}/product-create/response-fields.adoc[]
```
