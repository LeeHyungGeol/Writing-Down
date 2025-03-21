# TDD: Test Driven Development

# 💡 TDD란?

> **프로적션 코드보다 `테스트 코드를 먼저 작성`하여 테스트가 구현 과정을 주도하도록 하는 방법론**

## ⚡️ Red-Green-Refactor

1. **Red**: `실패하는 테스트`를 작성한다.
2. **Green**: **테스트를 통과**하는 `최소한의 프로적션 코드`를 작성한다.
3. **Refactor**: 테스트를 `통과하면서 리팩토링`한다.

## ⚡️ TDD의 가장 큰 가치: 피드백

내가 구현한 프로덕션 코드에 대한 빠르고 확실한 피드백이다!

### 💥 선 기능 구현, 후 테스트 작성을 할 경우의 문제점들

- 테스트 자체의 누락 가능성 ⬆️
- 예외 케이스에 대한 테스트 가능성 ⬇️ 
- 잘못된 구현을 다소 늦게 발견할 가능성 ⬆️

## 👍🏻 선 테스트 작성, 후 기능 구현의 장점!

**관점이 테스트를 먼저 작성한다면 테스트를 하기 위한 구조를 고민하게 된다!**

- **복잡도가 낮고(유연하며 유지보수하기 쉬운), 테스트 가능한 코드를 구현할 수 있게 한다.**
- 쉽게 발견하기 어려운 **엣지(Edge) 케이스**를 놓치지 않게 해준다.
- 구현에 대한 **빠른 피드백**을 받을 수 있다.
- **과감한 리팩토링**이 가능해진다.

## ⚡️ TDD: 관점의 변화

> `클라이언트` 관점에서의 `피드백`을 주는 Test Driven
- 클라이언트 관점에서 실제로 사용 단계까지 가기 이전에 테스트 코드에서 프로덕션 코드의 메서드를 호출해 보면서 피드백을 주는 것이다.

---

# 💡 키워드 정리

<img width="500" alt="Image" src="https://github.com/user-attachments/assets/42ca678d-9dc7-45c9-ac9c-ec4acad0bfe5"/>

- 익스트림 프로그래밍(XP, eXtreme Programming)의 실천 방법 중 하나가 TDD 이다!
- 팀으로 일할 때 일이나 과제들을 어떻게 다룰 것인가 > 스크럼(Scurm), 칸반(Kanban) 등
