# Redis Data Type

## 📌 **Strings**

> 문자열, 숫자, serialized object(JSON string) 등 저장 

### 📝 명령어 - SET

- `$ SET lecture inflearn-redis` 
- `$ MSET price 100 language ko` /  `MSET`: `Multi-SET`: 다수의 String 값을 한번에 저장
- `$ MGET lecture price language` / `MGET`: `Multi-GET`: 다수의 키의 값을 한번에 조회
- `$ INCR price` : Redis 는 **Integer 타입 없이 String 타입으로 저장한다.** 숫자의 형태의 String 을 저장할 경우 별도의 명령어로 연산이 가능하다.
- `$ INCRBY price 10`
  - `INCR`: 숫자형 String 값에 숫자 1을 더한다.
  - `INCRBY`: 숫자형 String 의 값에 특정 값을 더할 때 사용한다.
- `$ SET ‘{“lecture”: “inflearn-redis”, “language”: “en”}’` : **JSON 형태의 String** 을 직접 저장할 수 있다.
- `$ SET inflearn-redis:ko:price 200`: key 를 만들 때 의미별로`:` 을 사용하여 key 를 계층적으로 만들 수 있다.

---

## 📌 **Lists**

<img width="500" alt="스크린샷 2025-01-09 오후 7 20 09" src="https://github.com/user-attachments/assets/9d99b9bc-cb50-4232-8e23-e8759c4a964f" />

- **왼쪽부터  0,1,2 의 index 를**
- **오른쪽부터 -1, -2, -3 의 index 를 사용한다.**

> String을 `Doubly Linked List`로 저장 -> `push / pop`에 최적화 O(1)
> 
> **Queue(FIFO) / Stack(FILO) 구현에 사용**

### 📝 명령어 - PUSH / POP

- `$ LPUSH queue job1 job2 job3` : `LPUSH`: Left-Push: List의 왼쪽에 값을 추가
- `$ RPOP queue` : `RPOP`: Right-Pop: List의 오른쪽에서 값을 꺼냄
- `$ LPUSH stack job1 job2 job3` 
- `$ LPOP stack`
- `$ LPUSH queue job1 job2 job3` 
- `$ LRANGE queue -2 -1` : `LRANGE`: List의 범위를 조회
- `$ LTRIM queue 0 0` : `LTRIM`: List의 범위를 잘라냄

---

## 📌 **Sets**

<img width="500" alt="스크린샷 2025-01-09 오후 7 20 40" src="https://github.com/user-attachments/assets/cd42bc2f-d3f2-43aa-96e7-e48f7b2de6b8" />

> Unique string을 저장하는 정렬되지 않은 집합 
> 
> 집합이 구현되어 있어서 Set Operation 사용 가능(e.g. intersection, union, difference)

### 📝 명령어 - SET

- `$ SADD user:1:fruits apple banana orange orange`: S 가 Set을 의미하며, 중복된 값을 저장하지 않는다. 
- `$ SMEMBERS user:1:fruits` : Set의 모든 멤버를 출력
- `$ SCARD user:1:fruits` : Set의 멤버 수를 출력(`CARD: Cardinality`)
- `$ SISMEMBER user:1:fruits banana`: Set에 특정 멤버가 존재하는지 확인
- `$ SADD user:2:fruits apple lemon`: 
- `$ SINTER user:1:fruits user:2:fruits`: Set의 교집합을 출력
- `$ SDIFF user:1:fruits user:2:fruits`: Set의 차집합을 출력
- `$ SUNION user:1:fruits user:2:fruits`: Set의 합집합을 출력

---

## 📌 **Hashes**

> field-value 구조를 갖는 데이터 타입
> 
> 다양한 속성을 갖는 객체의 데이터를 저장할 때 유용

### 📝 명령어 - SET

- `$ HSET lecture name inflearn-redis price 100 language ko`: 다수의 field-value를 한번에 저장할 수 있다
- `$ HGET lecture name`
- `$ HMGET lecture price language invalid`: Hash의 다수의 field 값을 조회
- `$ HINCRBY lecture price 10`: 숫자형 String 에만 사용 가능

---

## 📌 **Sorted Sets - ZSets**

<img width="500" alt="스크린샷 2025-01-09 오후 7 21 11" src="https://github.com/user-attachments/assets/a47a5277-3827-4f40-86bd-c0e2083e3b13" />

> **중복 없이 Unique한 String value 를 저장하는 Set 과 유사하지만, score 라는 field 를 통해 데이터를 미리 정렬하는 데이터 타입(Set의 기능 + 추가로 score 속성 저장)**
> 
> Redis 만의 독특한 데이터 타입 중 하나이다.
> 
> 내부적으로 Skip List + Hash Table로 이루어져 있고, score 값에 따라 정렬 유지
> 
> score가 동일하면 lexicographically(사전 편찬 순) 정렬

### 📝 명령어 - ZSets

**Score 를 먼저 써주고, 그에 해당하는 value 를 뒤에 써준다.**

- `$ ZADD points 10 TeamA 10 TeamB 50 TeamC`: Z 가 ZSet을 의미하며, score와 value를 함께 저장  
- `$ ZRANGE points 0 -1`: `0 -1` 가장 처음부터 가장 마지막까지 조회 : Lists 와 동일하게 순서를 갖으므로 `Range`를 통해 index 를 사용하여 조회 가능
- `$ ZRANGE points 0 -1 REV WITHSCORES` : `REV`: Reverse, 역순으로 조회, `WITHSCORES`: score 값도 함께 조회
- `$ ZRANK points TeamA`: 해당 아이템의 Rank 를 조회, 이 값은 0 부터 시작하는 Index 값과 동일하다. 즉, Sorted Set 의 Index 를 반환한다.

---

## 📌 **Streams**

<img width="500" alt="스크린샷 2025-01-09 오후 7 21 45" src="https://github.com/user-attachments/assets/1f698be8-1a84-47f5-b020-900900ec01db" />


> **append-only log에 consumer groups과 같은 기능을 더한 자료 구조**(append-only log: 데이터를 추가만 할 수 있는 로그(데이터베이스나 분산 시스템에 주로 사용되는 데이터 저장 알고리즘))  
> 
> kafka 와 같은 이벤트 스트림 플랫폼과 어느정도 유사한 부분이 있다.

- Streams 에 추가되는 이벤트 또는 메시지는 unique id 갖는다. 이를 통해 하나의 entry를 읽을 때, O(1) 시간 복잡도
  - unique id는 스트림에 추가되는 시간과 순서를 기준으로 레디스에 의해 자동으로 할당된다.
- 분산 시스템의 다수의 컨슈머에서 안전하게 메시지를 소비 할 수 있도록 컨슈머 그룹이라는 기능이 기능이 포함되어 있습니다.
  - Consumer Group을 이용하면 다수의 컨슈머가 메시지를 처리하면서도 동일한 메시지를 중복 처리하는 문제를 쉽게 해결할 수 있다.

### 📝 명령어

- `$ XADD events * action like user_id 1 product_id 1`: **xadd 명령어를 통해 스트림을 추가하게 되고 key 이후에 star 옵션(`*`)을 주면 자동으로 unique id를 생성하여 할당하게 된다.** 
  - 이후에 해쉬와 같은 필드 밸류 형태로 메시지를 구성할 수 있습니다.
- `$ XADD events * action like user_id 2 product_id 1` 
- `$ XRANGE events - +`: 가장 처음 들어갔던 이벤트부터 가장 나중에 들어갔던 이벤트까지 이런 식으로 제가 출력을 할 수 있게 됩니다.
- `$ XDEL events 169206,,,-0(ID)`: 특정 ID의 이벤트를 삭제할 수 있습니다. 

---

## 📌 **Geospatials**

<img width="500" alt="스크린샷 2025-01-09 오후 7 22 00" src="https://github.com/user-attachments/assets/fdfbfd2a-4c32-42ed-8523-e3add64ea49c" />

> `Geospatial Indexes`: 좌표를 저장하고, 검색하는 데이터 타입 거리 계산, 범위 탐색 등 지원

### 📝 명령어

- `$ GEOADD seoul:station 126.923917 37.556944 hong-dae 127.027583 37.497928 gang-nam`
  - redis 의 `Geospatial` 에 데이터를 추가할 때는 위도가 아닌 **경도를 먼저 쓰고 위도를 써줘야 한다.** 
- `$ GEODIST seoul:station hong-dae gang-nam KM`: 두 지점 사이의 거리를 계산할 수 있다.

---

## 📌 **Bitmaps**

<img width="500" alt="스크린샷 2025-01-09 오후 7 23 41" src="https://github.com/user-attachments/assets/2a85b0f3-2c24-4851-928d-7b521d9b486d" />

> `Bitmaps`: 실제 데이터 타입은 아니고, **String에 binary operation을 쉽게 사용할 수 있도록 만들어놓은 인터페이스**이다. 
> 
> 최대 42억개 binary 데이터 표현 = 2^32(4,294,967,296)

**`Bitmap` 은 `적은 메모리`를 사용하여 `Binary 상태값`을 저장하는데 많이 활용된다.**

### 📝 명령어

- `$ SETBIT user:log-in:23-01-01 123 1`: `SETBIT key offset value`: **key 에 대한 offset 위치에 value 를 저장**
- `$ SETBIT user:log-in:23-01-01 456 1` 
- `$ SETBIT user:log-in:23-01-02 123 1`
- `$ BITCOUNT user:log-in:23-01-01`: 해당 key 에서 1의 개수를 세어준다.
- `$ BITOP AND result user:log-in:23-01-01 user:log-in:23-01-02`: 두 개의 key 에 대한 `AND` 연산을 수행한다. 결과는 바로 출력되는 것이 아닌 여기서는 result 라는 `destKey` 의 `Bitmap` 에 저장된다.
  - `BITOP operation destkey key [key ...]`: `operation` 은 `AND`, `OR`, `XOR`, `NOT` 중 하나를 선택할 수 있다.
- `$ GETBIT result 123`

---

## 📌 **HyperLogLog**

<img width="500" alt="스크린샷 2025-01-09 오후 7 24 22" src="https://github.com/user-attachments/assets/18cf7442-1406-4480-8fd2-92aeefaac96a" />

> `HyperLogLog`: **집합의 `cardinality`를 추정할 수 있는 확률형 자료구조**(확률형 자료구조라는 말은 결과값이 실제와 일정 부분 오차가 발생할 수 있다는 의미)
> 
> 정확성을 일부 포기하는 대신 **저장공간을 효율적으로 사용**(평균 에러 **0.81%**)

- `HyperLogLog`의 원리를 간단하게 설명하자면,
  - HyperLogLog는 멤버의 값을 해싱하여 bucket이라는 단위로 분류해서 해시 값에 맞게 표시합니다.
  - 동일한 아이템이 추가된 경우 동일한 해시를 갖기 때문에 카디널리티를 일정하게 계산할 수 있습니다. 
- 다만, HyperLogLog는 근간에 확률적인 계산식이 들어가 있기 때문에 최종 결과 값은 실제와 다를 수 있습니다.
- 예를 들어 다음과 같이 해시 충돌이 발생하는 경우 HyperLogLog는 정확하지 않은 Cardinality 를 반환합니다.
- `Set`을 이용해서도 카디널리티를 계산할 수 있는데 Set은 중복 검사를 위해 실제 값을 메모리에 모두 저장합니다. 
  - 만약, 만 개의 아이템이 존재한다면 아이템 개수에 비례해서 저장 공간이 증가하는데 HyperLogLog를 이용하면 상대적으로 매우 적은 메모리로 카디널리티를 계산할 수 있습니다. 
  - 다만 HyperLogLog는 실제 값을 저장하지 않기 때문에 모든 아이템을 다시 출력해야 하는 경우에는 활용할 수 없습니다.

`Cardinality`: **집합에서 중복을 제거한 후 남아 있는 고유한 값의 개수를 나타냅니다.**

### 📝 명령어

- `$ PFADD fruits apple orange grape kiwi` 
- `$ PFCOUNT fruits`


## 📌 **BloomFilter**

<img width="500" alt="스크린샷 2025-01-09 오후 7 25 00" src="https://github.com/user-attachments/assets/bd5670a9-0e92-4951-a66d-7b9346de0449" />

> `BloomFilter`: **element가 집합 안에 포함되었는지 확인할 수 있는 확률형 자료 구조 (=membership test)**
> 
> 정확성을 일부 포기하는 대신 `저장공간을 효율적`으로 사용, **실제 값을 저장하지 않기 때문에 Set 자료구조에 비해 매우 적은 메모리 사용**
> 
> `false positive`: element가 집합에 실제로 포함되지 않은데 포함되었다고 잘못 예측하는 경우

반대의 상황에서 ***반드시 있는 아이템이 없다고 반환하는 경우는 없다!!!***

### 💡 블룸필터가 데이터를 저장하는 원리

- 블룸필터는 값을 해싱하여 여러 개의 해시 키를 만들어냅니다.
- 여러 개의 키에 해당하는 위치를 블룸필터에 표시하게 됩니다.
- 이후에 어떤 아이템이 존재하는지 여부를 확인할 때는 다시 해시키를 생성하여 해당 위치를 확인하면 됩니다.
- 만약 애플과 오렌지를 블룸필터에 먼저 추가한 다음 그레이프라는 값이 블룸필터에 이미 추가되었는지를 확인해 본다고 하겠습니다.
- 그레이프의 경우 왼쪽 하나의 해시키는 표시가 되어 있지만 오른쪽 다른 해시키는 표시되어 있지 않기 때문에 해당 값이 집합에 포함되지 않았다는 것을 확인할 수 있습니다.
- 대신 문제가 되는 부분은 키위의 예시처럼 다른 해시키의 조합으로 키위에 해당하는 해당 키가 이미 표시된 경우입니다.
- 이때는 앞서 설명드린 false positive로 실제로는 해당 아이템이 집합에 포함되지 않지만 Bloomfilter는 해당 아이템을 포함하고 있다고 잘못된 결과를 반환합니다.
- 따라서 블룸필터를 사용하는 경우에는 반드시 false positive에 대한 문제를 인지하고 있어야 합니다.


### 📝 명령어

- `$ BF.MADD fruits apple orange` 
- `$ BF.EXISTS fruits apple`
- `$ BF.EXISTS fruits grape`
