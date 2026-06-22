---
name: test
description: >
  Spring Boot 프로젝트(was)의 테스트 코드를 자동으로 작성하고 JaCoCo 커버리지를 검증하는 스킬.
  다음 상황에서 반드시 이 스킬을 사용한다:
  "테스트 써줘", "테스트 코드 작성", "테스트 추가", "커버리지 올려줘", "JaCoCo", "테스트 보강",
  "변경된 파일 테스트", "미커버 케이스", "test code", "coverage" 등의 요청.
  변경된 소스 파일이 있고 테스트가 필요한 상황이라면 명시적 언급 없이도 이 스킬을 적용한다.
---

# Spring Test Generator

이 프로젝트(현재 저장소 루트)의 테스트 코드를 작성하고 JaCoCo로 커버리지를 검증하는 자동화 스킬이다.

## 전체 흐름

```text
1. 변경 파일 식별 → 2. 테스트 작성/보강 → 3. JaCoCo 검증 → (미달 시 2로 반복) → 4. 결과 리포트
```

---

## STEP 1: 변경 파일 식별

기본 브랜치 대비 변경된 소스 파일을 찾고, 테스트가 필요한 레이어만 필터링한다.

```bash
cd "$(git rev-parse --show-toplevel)"

# 변경된 파일 목록 (기본 브랜치 대비)
git diff --name-only $(git merge-base HEAD origin/main) HEAD -- 'src/main/java/**'
# 또는 스테이징/언스테이징 포함
git diff --name-only HEAD
```

### 레이어별 테스트 전략

패키지 경로로 레이어를 판별한다:

| 패키지 경로 | 레이어 | 테스트 방식 |
|------------|--------|-----------|
| `presentation/*/controller/` | Controller | `@WebMvcTest` |
| `application/*/service/` | Service | `@IntegrationTest` |
| `domain/*/repository/` | Repository | `@RepositoryTest` |
| `domain/*/entity/` | Domain/Entity | 순수 JUnit (Spring 컨텍스트 없음) |

**테스트 불필요 파일** (스킵):
- `*/dto/**`, `*/request/**`, `*/response/**` — 단순 데이터 클래스
- `*/config/**`, `*/common/**` — 설정 클래스
- `*/enums/**`, `*/type/**` — 열거형

---

## STEP 2: 테스트 작성

### 2-1. 기존 테스트 확인

테스트 파일 경로: `src/main/java/kr/co/webee/X/Y.java` → `src/test/java/kr/co/webee/X/YTest.java`

기존 파일이 있으면 미커버 케이스만 추가. 없으면 신규 작성.

### 2-2. 공통 컨벤션 (반드시 준수)

**Assertion**: AssertJ만 사용. JUnit5 `assertEquals`, `assertThrows`, `assertNotNull` 금지.

```java
// ✅ 올바른 방식
assertThat(result).isEqualTo(expected);
assertThat(result).isNotNull();
assertThatThrownBy(() -> service.doSomething())
        .isInstanceOf(BusinessException.class)
        .extracting("type")
        .isEqualTo(ErrorType.SOME_ERROR);

// ❌ 금지
assertEquals(expected, result);
assertThrows(BusinessException.class, () -> ...);
```

**Import**: JUnit5 `Assertions.*` import 금지.
```java
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
```

**DisplayName**: `@DisplayName` 한국어 작성.
- 성공: `"상품 리뷰를 등록한다."`
- 실패: `"존재하지 않는 상품에 리뷰를 등록하려는 경우 예외가 발생한다."`

**구조**:
```java
@Test
@DisplayName("...")  // @Test 바로 아래
void methodName() {
    //given

    //when

    //then
}

// 예외 케이스는 when-then 통합 가능
void failCase() {
    //given

    //when - then
    assertThatThrownBy(...)...;
}
```

**그룹핑**: 관련 테스트가 3개 이상이면 `@Nested` + `@DisplayName`으로 묶는다.
```java
@Nested
@DisplayName("회원가입")
class SignUp {
    @Test
    @DisplayName("성공")
    void signUpSuccess() { ... }

    @Test
    @DisplayName("실패 - 동일한 아이디가 존재하는 경우")
    void signUpFailSameUsername() { ... }
}
```

**픽스처 헬퍼**: 반복 생성 객체는 `private` 메서드로 분리.
```java
private User createUser() {
    return User.builder().username("test").password("pw").name("이름").build();
}
```

### 2-3. 레이어별 템플릿

#### Controller (`@WebMvcTest`)

```java
@WebMvcTest(
        controllers = XxxController.class,
        excludeAutoConfiguration = SecurityAutoConfiguration.class,
        excludeFilters = {
                @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = OncePerRequestFilter.class),
                @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = UserIdArgumentResolver.class),
                @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = WebConfig.class)
        }
)
@Import(TestWebConfig.class)
@ActiveProfiles("test")
class XxxControllerTest {
    @Autowired MockMvc mockMvc;
    @Autowired ObjectMapper objectMapper;
    @MockitoBean XxxService xxxService;

    @Test
    @DisplayName("...")
    void test() throws Exception {
        //given
        when(xxxService.method(any())).thenReturn(response);

        //when - then
        mockMvc.perform(post("/api/v1/...")
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("요청이 성공적으로 처리되었습니다."))
                .andDo(print());
    }
}
```

#### Service (`@IntegrationTest`)

```java
@IntegrationTest
class XxxServiceTest {
    @Autowired XxxService xxxService;
    @Autowired XxxRepository xxxRepository;

    @BeforeEach
    void setUp() {
        // DB에 필요한 픽스처 저장
    }

    @Test
    @DisplayName("...")
    void test() {
        //given
        //when
        //then
    }
}
```

#### Repository (`@RepositoryTest`)

```java
@RepositoryTest
class XxxRepositoryTest {
    @Autowired XxxRepository xxxRepository;

    @Test
    @DisplayName("...")
    void test() {
        //given
        // 엔티티 저장

        //when
        List<Xxx> result = xxxRepository.findAllByXxx(...);

        //then
        assertThat(result).hasSize(2)
                .extracting("field")
                .containsExactlyInAnyOrder("value1", "value2");
    }
}
```

#### Entity/Domain (순수 JUnit)

```java
class XxxTest {
    @Test
    @DisplayName("...")
    void test() {
        //given
        Xxx entity = createXxx();

        //when
        entity.updateSomething(newValue);

        //then
        assertThat(entity.getField()).isEqualTo(newValue);
    }
}
```

---

## STEP 3: JaCoCo 검증

### 3-1. 테스트 실행 및 리포트 생성

```bash
cd "$(git rev-parse --show-toplevel)"
./gradlew test jacocoTestReport --tests "kr.co.webee.path.to.XxxTest"
```

특정 클래스만 빠르게 실행할 때:
```bash
./gradlew test --tests "kr.co.webee.*.XxxTest" jacocoTestReport
```

### 3-2. 커버리지 파싱

HTML 리포트 위치: `build/reports/jacoco/test/html/`

대상 클래스 리포트 경로 패턴:
```text
build/reports/jacoco/test/html/kr.co.webee.path/XxxClass.html
```

HTML에서 커버리지 추출:
```bash
# 미커버 라인/브랜치 확인 (class summary)
grep -o 'Total[^<]*<td[^>]*>[0-9]*%' build/reports/jacoco/test/html/index.html

# 특정 클래스 상세 (fc=covered, fc nc=missed)
grep -c 'class="nc"' build/reports/jacoco/test/html/kr.co.webee.xxx/XxxClass.java.html
```

또는 XML 리포트가 있다면 더 간편:
```bash
# build/reports/jacoco/test/jacocoTestReport.xml
grep 'name="XxxClass"' build/reports/jacoco/test/jacocoTestReport.xml
```

### 3-3. 목표 커버리지

| 구분 | 라인 커버리지 | 브랜치 커버리지 |
|------|------------|--------------|
| Service | 80% 이상 | 70% 이상 |
| Repository | 90% 이상 | — |
| Controller | 80% 이상 | 60% 이상 |
| Entity/Domain | 80% 이상 | 70% 이상 |

미달 시 → STEP 2로 돌아가 미커버 라인/브랜치에 대응하는 케이스를 추가한다.

---

## STEP 4: 결과 리포트

작업 완료 후 아래 형식으로 결과를 출력한다.

```text
## 테스트 작성 결과

### 처리 대상 파일
- `XxxService.java` → `XxxServiceTest.java` (신규 작성 / 보강)
- `XxxController.java` → `XxxControllerTest.java` (신규 작성 / 보강)

### 작성된 테스트 케이스
| 파일 | 추가된 케이스 |
|------|------------|
| XxxServiceTest | "xxx를 등록한다.", "존재하지 않는 xxx 조회 시 예외 발생" 외 N건 |

### JaCoCo 커버리지
| 클래스 | 라인 | 브랜치 | 상태 |
|--------|------|--------|------|
| XxxService | 85% | 75% | ✅ |
| XxxController | 78% | 65% | ✅ |

### 미처리 항목
- (있다면 사유와 함께 기록)
```

---

## 주의사항

- `@Transactional`은 `@IntegrationTest`, `@RepositoryTest`에서 이미 롤백 처리됨 — 별도 선언 불필요
- `UserIdArgumentResolver`가 있는 Controller는 TestWebConfig로 모킹 필요
- Testcontainers는 `@IntegrationTest`, `@RepositoryTest`에 이미 포함됨
- AWS S3, FCM, AI 관련 빈은 `@IntegrationTest`의 TestConfig들로 이미 처리됨
