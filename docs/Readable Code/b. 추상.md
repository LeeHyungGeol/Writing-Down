# 💡 추상 (抽象)

<br/>

## ⚡️ 목차
- 우리가 클린 코드를 추구하는 이유
- 프로그램의 정의
- 추상 (抽象)이란?(추상과 구체)
- 이름 짓기
- 메서드 선언부
- 추상화 레벨
- 매직 넘버, 매직 스트링

<br/>

<br/>

<br/>

# 💡 우리가 클린 코드를 추구하는 이유

> `가독성` 때문이다.

- 글이 잘 읽힌다 -> **이해가 잘 된다.**
- 코드가 잘 읽힌다 -> **이해가 잘 된다** -> 유지보수 하기가 수월하다 -> 시간과 자원이 절약된다.

`클린 코드`를 위한 다양한 원칙들: **`왜` 지켜야 하는가?**

클린 코드를 위한 당양한 원칙들을 관통하는 중요한 주제 하나가 바로 `추상 (抽象)` 이다.

<br/>

<br/>

<br/>


# 💡 프로그램의 정의

> `데이터 + 코드`의 조합이다.

데이터를 요리에서의 재료로 봤을 때, 코드는 그 재료를 가공하는 다양한 방법이다.

- 프로그램의 `데이터` 부분을 가지고 `추상`을 얘기할 수 있다.
- 프로그램의 `코드` 부분을 가지고 `논리`를 얘기할 수 있다.

<br/>

<br/>

<br/>


# 💡 추상(抽象)이란?

***`추상 (抽象)`은 뽑을 추, 코끼리 상(형상 상)의 조합으로 이루어진 단어이다.***

> 구체적인 정보들에서 어떤 이미지(형상)을 뽑아낸다는 뜻이다.   
> -> 중요한 정보는 `가려내어` 남기고, 덜 중요한 정보는 생략하여 `버린다.`

결국은 **구체의 기반**에서 **추상이라는 단계**까지 올라갈 수 있는 것이다.

<img width="500" src="https://github.com/user-attachments/assets/1779b2fc-a7fa-48e8-84c0-0f78fa04245c">

여기서 `추상화 레벨`이라 개념이 생긴다.

<img width="500" src="https://github.com/user-attachments/assets/d730e45f-6a39-4860-aafc-2b5ece7c819e">

**일상 생활**에서 예시를 들어보면,
- `추상`: 친구랑 노래방 갔어
- `구체`: 가깝게 오래 사귀어 정이 두터운 사람 중 한명과 함께 폐에서 나온 공기가 성대를 통과...

---

## ⚡️ Computer Science 에서의 추상

컴퓨터는 `0 과 1` 로 이루어진 `비트`로 이루어져 있다. 어떻게 고수준의 작업을 할 수 있나?
- `1 bit`의 의미: 존재성
- 불이 켜지면 존재하는 것: 1
- 불이 꺼지면 존재하지 않는 것: 0

`1 byte = 8 bit`
- bit 8개를 `묶어서` 표현한다.

**bit 가 여러개 있을 때, 데이터 덩어리(`bit`의 덩어리)를 `묶어서` 어떻게 표현할 것인가?**
- `Type(자료형)`은 하나의 예시
- `묶어서 추상화` 해서 표현하겠다.
- `논리 연산`: OR 연산, AND 연산 등등.. -> **bit 덩어리와 bit 덩어리를 어떤 논리에 따라서 계산(연산)을 하면 새로운 bit 덩어리가 나온다.**

`고수준 언어`와 `저수준 언어`

<img width="500" src="https://github.com/user-attachments/assets/d57d67e8-103f-4cf8-97a0-3f22fe59765a">

- `고수준 언어`: 추상화 레벨이 높다. 인간이 알아듣기 쉽게 추상화가 잘 되어 있다.
- `저수준 언어`: 추상화 레벨이 낮다. 인간이 알아듣기 어렵다.

> **`추상화 과정`은 Computer Science 의 본질이다.**

---

## ⚡️ 읽기 좋은 코드(Readable Code)와 추상화 과정의 관계

***그렇다면, `읽기 좋은 코드`와 `추상화 과정`이 무슨 관계가 있는건가?***

-> **적절한 추상화**는 복잡한 로직과 데이터를 단순화시켜서 이해하기 쉽게 한다. -> **읽기가 좋다.**

---

## ⚡️ 잘못된 추상화의 예시

***추상으로부터 구체를 유추하지 못할 때도 있다!***

> ex) `노래방`을 `지름방`으로 불렀을 때..
- 노래를 부르는 장소인데, 상대적으로 덜 중요한 정보인 소리를 지른다는 정보만 남겼다.
- 같은 단어라도 도메인(문맥)이 다르면 그 의미가 달라질 수도 있다.

***추상으로부터 구체를 유추하지 못한 이유***
1. 추상화 과정에서 **중요한 정보를 부각시키지 못했다.**
- 상대적으로 덜 중요한 정보를 남기고 중요한 정보 제거했다.

2. 해석자가 동일하게 공유하는 `문맥(Context)`이 없다.
- 중요한 정보의 기준이 다를 수 있다.
- 즉, 도메인 영역 별 추상화 기준이 다를 수 있다.

잘못된 추상화가 일으키는 `side-effect`는 생각보다 정말로 크다.      
반대로 얘기하면,          
->  **적절한 추상화**는 해당 `도메인의 문맥` 안에서, 정말 중요한 핵심 개념만 표현하는 것

> 결국, 해당 `도메인`안에서 `추상화`를 얼마나 적절하게 하는가가 핵심이다.

---

## ⚡️ 추상화의 대표적인 행위: 이름 짓기

> 추상화의 대표적인 행위가 바로 `이름을 짓는 것`이다.
- 엄청 단순하지만, 엄청 중요한, **고도의 추상적 사고 행위**이다.

<br/>

<br/>

<br/>

# 💡 이름 짓기

> ***이름을 짓는다는 행위는 `추상적 사고`를 기반으로 한다.***

1. 표현하고자 하는 `구체에서 정말 중요한 부분만`을 추출하여 잘 드러내는 표현
2. 해당 `도메인의 문맥` 안에서 이해되는 표현

---

## ⚡️ 단수와 복수를 구분하기

**끝에 `-(e)s` 를 붙여 어떤 데이터가 단수이지, 복수인지 나타내는 것만으로도 읽는 이에게 중요한 정보를 같이 전달할 수 있다.**

---

## ⚡️ 이름 줄이지 않기

- **줄임말이라는 것은 가독성을 제물로 바쳐 효율성 얻는 것으로, 대부분 잃는 것에 비해 얻는 것이 적다.**
- 자제하는 것은 좋으나, `관용어`처럼 많은 사람들이 **자주 사용하는 줄임말 정도는 존재한다.**
    - ex) column -> col, latitude -> lat, longitude -> lon
    - **ex) count -> cnt (추천 X)**: 사실 cnt 를 보고 지체없이 count 라고 잘 읽혀지지 않는다. 5글자를 3글자로 줄인 것이어서 크게 줄인 것 같지도 않다.

**자주 사용하는 줄임말이 이해될 수 있는 것은 사실 `문맥(context)` 때문이다.**

**EX)**

```java
class Sample {
	private int row;
}

class TableCell {
	private int row;
	private int col; // 컬럼인가 보다.
}

class GeoPoint {
	private double lat;
    private double lon; // 위도, 경도인가 보다.
}
```

---

## ⚡️ 은어/방언 사용하지 않기

- **농담에서 파생된 용어, 일부 팀원/현재의 우리 팀만 아는 용어 금지**
    - 새로운 사람이 팀에 합류했을 때 이 용어를 단번에 이해할 수 있는가?
- **도메인 용어 사용하기**
    - 도메인 용어를 먼저 정의하는 과정(ex. 도메인 용어 사전)이 먼저 필요할 수도 있다.
        - EX) **`매장`이라는 `도메인`**을 우리 회사에서 사용할 때, 이것을 `Shop` 이라고 할 것인지, `Store` 라고 할 것인지 미리 정의해두어야 한다.

---

## ⚡️ 좋은 코드를 보고 습득하기

비슷한 상황에서 자주 사용하는 단어, 개념 습득하기
- 이름을 짓는 것이 일상 생활에서 쓰는 표현과 코드에서 쓰는 표현들이 조금씩 다르다.
- 좋은 오픈소스, 라이브러리, 프레임워크 등에 좋은 코드가 사용되어 있기 때문에 이런 코드나 단어들을 보고 습득하는 것이 좋다.
    - ex) `pool`, `candidates`, `threshold` 등등..
    - connection pool, thread pool, candidate key, threshold(한계점, 임계값, 임계지점) 등등..

---

```java
for (int i = 0; i < 8; i++) {
	for (int j = 0; j < 10; j++) {
		board[i][j] = "□";
	}
}
```

```java
for (int row = 0; row < 8; row++) {     
	for (int col = 0; col < 10; col++) {
		board[row][col] = "□";
	}
}
```

- board 라는 이차원의 바둑판 모양이기 때문에, `i, j` 를 `row, col` 로 변경했다.

---

**AS-IS**

```java
System.out.println("선택할 좌표를 입력하세요. (예: a1)");
String input = scanner.nextLine();
System.out.println("선택한 셀에 대한 행위를 선택하세요. (1: 오픈, 2: 깃발 꽂기)");
String input2 = scanner.nextLine();
char c = input.charAt(0);
char r = input.charAt(1);
int col;
...
int row = Character.getNumericValue(cellInputRow) - 1;
```

**TO-BE**

```java
System.out.println("선택할 좌표를 입력하세요. (예: a1)");
String cellInput = scanner.nextLine();
System.out.println("선택한 셀에 대한 행위를 선택하세요. (1: 오픈, 2: 깃발 꽂기)");
String userActionInput = scanner.nextLine();
char cellInputCol = cellInput.charAt(0);
char cellInputRow = cellInput.charAt(1);
int selectedColumnIndex;
int selectedRowIndex = Character.getNumericValue(cellInputRow) - 1;
```

- `input, input2` 라는 이름은 너무 추상적이다. 무엇에 대한 input 인지 명확히 하여 `cellInput, userActionInput` 으로 변경했다.
- `c, r` 은 무엇을 의미하는지 알기 어렵다. cellInput 의 row, column 을 의미하므로 `cellInputCol, cellInputRow` 로 변경했다.
- `col, row` 은 자주 사용해서 어떤 row, col 에 대한 것인지 그 의미를 바로 이해할 수 없다. `selectedColumnIndex, selectedRowIndex` 로 변경했다.

---

**AS-IS**

```java
if (input2.equals("2")) {
    board[selectedRowIndex][selectedColumnIndex] = "⚑";
    boolean open = true;
    for (int i = 0; i < 8; i++) {
        for (int j = 0; j < 10; j++) {
            if (board[i][j].equals("□")) {
                open = false;
            }
        }
    }
    if (open) {
        gameStatus = 1;
    }
}
```

**TO-BE**

```java
if (userActionInput.equals("2")) {
	board[selectedRowIndex][selectedColumnIndex] = "⚑";
	boolean isAllOpened = true;
	for (int row = 0; row < 8; row++) {
		for (int col = 0; col < 10; col++) {
            if (board[row][col].equals("□")) {
                isAllOpened = false;
            }
        }
    }
	if (isAllOpened) {
		gameStatus = 1;
	}
}
```

- `open` 은 단순히 `열리다` 라는 의미로 사용되었는데, `모든 셀이 열렸는지` 를 의미하는 것이므로 `isAllOpened` 로 변경했다.

<br/>

<br/>

<br/>

# 💡 메서드와 추상화

> 잘 쓰여진 코드라면, 한 메서드의 주제는 반드시 하나다.
- 0개도 2개도 아닌, 무조건 1개

<img width="500" src="https://github.com/user-attachments/assets/c4d09456-bfee-4282-b67a-94d1c9d14646">

- 메서드명은 메서드 구현에 대한 부분을 추상화해 한 문단의 주제처럼 축약된 중요한 내용을 표현해야 한다.

---

> 결국엔 `메서드명`도 **생략할 정보와 의미를 부여하고 드러낼 정보를 구분해야 한다.**

**만약 이게 어렵다면, 메서드가 2가지 이상의 일을 하고 있을 가능성이 높다.**

<img width="500" src="https://github.com/user-attachments/assets/af3c67cb-739a-4bf7-8cce-1b41b66f5fd4">

**의미를 담을 수 있는 더 작은 단위의 메서드로 쪼개어 보고, 이들을 더 큰 맥락 안에서 포괄적인 의미를 담는 메서드 묶어보자.**

<br/>

<br/>

<br/>

# 💡 메서드 선언부

```
반환타입 메서드명 (파라미터) {}
```

## ⚡️ 메서드명

> **메서드명을 동사냐 명사냐 보다 질적으로 좋은 추상화를 했는지 제일 중요하다.**

- **추상화된 구체를 유추할 수 있는, 적절한 의미가 담긴 이름**
- 파라미터와 연결지어 더 풍부한 의미를 전달할 수 있다.

---

## ⚡️ 파라미터

> **메서드를 설계하는 입장에서 이 메서드를 사용할 때 파라미터에 어떤 정보들이 드러아면 좋을지를 고민해야 한다.**

- **파라미터의 타입, 개수, 순서를 통해 의미를 전달**
- 파라미터는 외부 세계와 소통하는 창
    - 메서드를 사용하는 사람한테 이 메서드에 어떠한 데이터가 필요하고 어떠한 데이터를 반환할지 알려주는 소통의 창이다.

---

## ⚡️ 반환타입

- **메서드 시그니처에 납득이 가는, 적절한 타입의 반환값 돌려주기**
    - EX) 반환 타입이 boolean 인데, 이게 이 메서드에서 무엇을 의미하는건지?
- **void 대신 충분히 반환할 만한 값이 있는지 고민해보기**
    - 반환값이 있다면 테스트도 용이해진다.
    - 물론, 항상 그런 것은 아니다.

---

> EX) 도메인에 존재하는 개념(도메인 용어): 매장, 매출 매장별, 일자별 매출 집계를 위한 어떤 unique key 를 만들 때.

**AS-IS**

```java
public String createDailyShopKey(String shopId, String localDateString) {
    return String.format("%s_%s", shopId, localDateString);
}
```

**TO-BE**

```java
public String createDailyShopKey(String shopId, LocalDate sellingDate) {
    return String.format("%s_%s", shopId, localDate);
}
```

- 반환값을 정확하게 함으로써 어떤 `Type` 이 필요한지 명확하게 한다.
- `sellingDate` 라는 매출 날짜로 정확하게 이름을 명시한다.

---

```java
private static void checkIfGameIsOver() {
	boolean isAllOpened = isAllCellOpened();
	if (isAllOpened) {
		gameStatus = 1;
	}
}
```

- `checkIfGameIsOver` 와 같이 메서드 이름에 `check` 가 들어가는 메서드는 반환타입이 `void`인 것이 일반적이다.

<br/>

<br/>

<br/>

# 💡 추상화 레벨

<img width="500" src="https://github.com/user-attachments/assets/3b1e5104-3af1-499f-8028-9bb8eaa33f0f">

<img width="500" src="https://github.com/user-attachments/assets/b8cdbb79-986b-4b3a-841a-08e41f7ccf05">

<img width="500" src="https://github.com/user-attachments/assets/60af43cb-2ae7-4927-94dc-25439ffbd4fd">

> **하나의 세계 안에서는 추상화 레벨이 동등해야 한다.**

- 만약 이것이 깨지면 코드가 어색해지고, 그것이 티가 잘난다.

<img width="500" src="https://github.com/user-attachments/assets/489f1408-eb47-40bb-a55f-8e79cbaa0575">

- 추상화 레벨이 10 인 코드를 계속 읽다가, 갑자기 추상화 레벨이 5인 코드가 나온다.
- 추상화 레벨이 맞지 않으면, 이 코드가 무슨 의미인지 알기 어렵다.

<img width="500" src="https://github.com/user-attachments/assets/ad0136ad-aad9-46aa-a522-8fd7c1933663">

- 따라서 이런 식으로 추상화 레벨을 동등하게 맞춰줘야 한다.

**다른 코드들이 다 추상적인데, 혼자 구체화 레벨에 가까우면 안된다.**

> **추상화 레벨을 맞춰줌으로써 읽는 사람이 하여금 자연스럽게 사고가 흘러갈 수 있도록 갑자기 너무 구체화된 코드가 나와서 이게 무슨 뜻이지 하고 멈추지 않도록 만드는 기법이다.**

<br/>

<br/>

<br/>

# 💡 매직 넘버, 매직 스트링

> **`매직 넘버`, `매직 스트링`: 의미를 갖고 있으나. 상수로 추출되지 않은 숫자, 문자열 등**         
> **상수 추출로 이름을 짓고 의미를 부여함으로써 가독성, 유지보수성 향상**

- 복잡한 로직 사이에 있으면 잘 알아보지 못한다.
- 하지만, 상수로 추출해서 두게 되면 나중에 값을 바꾸더라도 한 곳만 바꾸면 되기 때문에 유지보수성이 향상된다.

***코드를 읽는 사람으로 하여금 이 숫자, 문자는 중요하니까 `유지보수 할 때 주의 깊게 봐야해` 라는 의미를 담아 줄 수 있기 때문에 되게 중요하다.***

---

**AS-IS**

```java
public class MinesweeperGame {
	private static String[][] board = new String[8][10];
	private static Integer[][] landMineCounts = new Integer[8][10];
	private static boolean[][] landMines = new boolean[8][10];
	private static int gameStatus = 0; // 0: 게임 중, 1: 승리, -1: 패배
}
```

**TO-BE**

```java
public class MinesweeperGame {
  public static final int BOARD_ROW_SIZE = 8;
  public static final int BOARD_COL_SIZE = 10;
  public static final int LAND_MINE_COUNT = 10;
  public static final String FLAG_SIGN = "⚑";
  public static final String LAND_MINE_SIGN = "☼";
  public static final String CLOSED_CELL_SIGN = "□";
  public static final String OPENED_CELL_SIGN = "■";
  private static String[][] BOARD = new String[BOARD_ROW_SIZE][BOARD_COL_SIZE];
  private static Integer[][] NEARBY_LAND_MINE_COUNTS = new Integer[BOARD_ROW_SIZE][BOARD_COL_SIZE];
  private static boolean[][] LAND_MINES = new boolean[BOARD_ROW_SIZE][BOARD_COL_SIZE];
  private static int gameStatus = 0; // 0: 게임 중, 1: 승리, -1: 패배
}
```

- `gameStatus` 는 변하는 값이지만, `board`, `landMineCounts`, `landMines` 는 변하지 않는 값이다.
- **언어마다 상수에 대한 네이밍 컨벤션이 따로 있다.** JAVA 에서는 `상수에 대한 컨벤션`은 **`대문자`**와 **`언더스코어(_)`**로 표현한다.
- `8, 10` 이라는 숫자도 보드 로우 사이즈로 계속 작업을 하고 있기 때문에 `매직 넘버`로 본다. `BOARD_ROW_SIZE`, `BOARD_COL_SIZE` 로 추출할 수 있다.
- `"⚑", "☼", "□", "■"` 해당 특정 모양의 문자열도 지뢰 찾기 게임에서 의미를 갖고 있는 모양이고 해당 모양이 바뀔 경우 모든 코드의 모양을 변경해줘야 하므로 `매직 스트링`으로 본다.
    - 각각 `FLAG_SIGN`, `LAND_MINE_SIGN`, `CLOSED_CELL_SIGN`, `OPENED_CELL_SIGN` 으로 추출할 수 있다.

<br/>

<br/>

<br/>
