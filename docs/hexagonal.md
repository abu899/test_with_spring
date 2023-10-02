# 진화하는 아키텍쳐

> 아키텍쳐란 어떤 비지니스 문제를 해결하기 위해 준수해야하는 제약

아키텍쳐는 종착지가 아닌 여정

## 아키텍처와 테스트의 상호 관계

- 테스트하기 힘들다면 아키텍쳐의 문제가 있다고 볼 수 있다
- 테스트하기 쉬운쪽으로 개발하면서 아키텍처를 개선해 나가야 한다
- 테스트하기 쉬운 코드가 좋은 코드일 확률이 높다

## 헥사고날 아키텍쳐

### 장점

- 외부에서 도메인으로 향하는 방향이 `단방향`으로 유지된다
- 도메인에만 집중할 수 있게되어 외부에 의존하지 않게된다
- 테스트에 유리하다

자세한 내용은 https://github.com/abu899/SummaryNote/tree/master/book/BuildCleanArchitecture/doc
의 만들면서 배우는 클린 아키텍쳐 정리 참고

## 테스트 어디까지 해야하나?

### 무엇을 테스트해야할까? Right-BICEP

- Right
  - 결과가 올바른가?
- B: Boundary Conditions
  - Corner case와 Edge 케이스를 테스트하자
  - corner case
    - 시스템 내,외부 조건에 의해 발생하는 특별한 케이스
    - 네트워크가 끊어지거나, 서버 메모리가 적은 경우
  - edge case
    - 엣지 케이스는 시스템 내부 조건에 의해 발생하는 특별한 케이스
    - 언어마다 다른 Long 값에 대한 테스트
  - CORRECT
    - Conformance(적합성)
      - 명세에 적힌대로 동작하는가?
      - 이메일같은 형식이 제대로 되었는지 확인
    - Ordering
      - 순서에 따라서 동작하는가?
      - 정렬이 들어갔다면 정렬이 제대로 됬는가를 확인
    - Range
      - 범위에 따라서 동작하는가?
      - input에 양 끝점이 있다면 양 끝점도 테스트
    - Reference
      - 참조에 따라서 동작하는가?
      - 협력 객체 상태에 따라 어떻게 동작하는지 확인
      - 사용자 초대의 경우 활성화 되지 않는 사용자에 대해서 어떻게 동작하나
    - Existence
      - 존재에 따라서 동작하는가?
      - 입력 값이 null 이나 blank 인 경우 어떻게 동작하나 확인 
    - Cardinality
      - 기수에 따라서 동작하는가?
      - 입력값의 길이가 0, 1, n 인 경우를 확인
    - Time
      - 시간에 따라서 동작하는가?
      - 만약 병렬처리를 한다면 순서가 제대로 지켜지는지 확인
- I: Inverse Relationships(Optional)
  - 역 관계를 테스트할 수 있다면 테스트하자
  - 제곱근을 테스트한다고 가정하면, 결과값에 대해 다시 역으로 검증
- C: Cross-Check Results(Optional)
  - 내가 만든 코드와 다른 방법으로 만든 코드를 비교해보자
  - 내가 구현한 코드와 유사한 라이브러리가 있다면 비교 검증하라
- E: Error Conditions(Optional)
  - 장애상황이나 극한상황에 대한 테스트
- P: Performance Characteristics(Optional)
  - 성능에 대한 테스트
  - 성능이나 부하 테스트는 ngrinder 같은 툴을 많이 이용

## 테스트 팁

- @ParameterizedTest
  - 반복되는 테스트를 줄일 수 있다
  - 가시성이 올라가고 어느 부분에서 문제가 생겼는지 확인이 가능
- AssertAll
  - 중간에 값이 틀리더라도 모든 검증을 실행
- 한 개의 테스트에서는 한 개만 테스트하자
  - 물론 when/then을 반복해서 썼을 때 가독성이 늘어나는 테스트의 경우는 예외
  - 무엇을 테스트하는지 표현하는게 제일 중요하다
- 테스트에 Thread sleep을 넣지 말자
  - 데스크탑 성능에 따라 테스트 결과가 달라질 수 있음
  - 테스트가 비결정적이 될 수 있음
  - 간단하게 Thread.join을 적용할 수 있지만 범용적이지 않음
    - Awaitility라는 라이브러리를 사용하는 것도 검토하자
- FIRST 원칙
  - Fast: 테스트는 빨라야한다
  - Independent: 테스트는 서로 독립적이여야 한다
  - Repeatable: 테스트는 반복 수행해도 결과가 같아야한다(결정적- Deterministic)
  - Self-Validating: 테스트는를 수행하면 시스템의 성공, 실패를 알 수 있어야한다
  - Timely: 테스트는 적시(코드 구현 전)에 작성되어야 한다