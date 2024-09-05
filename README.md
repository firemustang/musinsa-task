---
# Java(Kotlin) Backend Engineer - 과제
- Java 21 version
- Gradle
- H2 Database : H2 인메모리 데이터베이스 사용
---

## 구현 범위
1. **조회 기능**
    - **구현 1) 카테고리 별 최저가격 브랜드와 상품 가격, 총액 조회**: 각 카테고리별로 가장 저렴한 가격을 제공하는 브랜드와 해당 상품의 가격을 조회하였습니다.
    - **구현 2) 단일 브랜드로 모든 카테고리 상품을 구매할 때 최저가격에 판매하는 브랜드와 카테고리의 상품가격, 총액 조회**: 특정 브랜드가 모든 카테고리의 상품을 최저 가격으로 판매하는 경우의 총액을 조회하였습니다.
    - **구현 3) 카테고리 이름으로 최저, 최고 가격 브랜드와 상품 가격 조회**: 특정 카테고리에서 가장 높은 가격과 낮은 가격을 제공하는 브랜드 정보를 조회하였습니다.
    - **전체 브랜드 조회**: 등록된 모든 브랜드 정보를 조회하였습니다.
    - **브랜드 및 상품 조회**: 브랜드와 그 하위에 속한 상품 정보를 함께 조회하였습니다.
   
2. **추가/업데이트/삭제 기능**
    - **구현 4) 브랜드 및 상품을 추가 / 업데이트 / 삭제하는 API** 
      - **구현 4-1) 추가/업데이트**: 브랜드와 그 하위의 상품 정보를 저장 및 업데이트할 수 있습니다. 기존에 있는 브랜드는 업데이트 시키고, 없다면 인서트 시킵니다.
      - **구현 4-2) 삭제**: 브랜드 또는 특정 상품을 삭제할 수 있습니다. 브랜드 삭제 시 하위 상품 전부 삭제 됩니다.
      
3. **에러 처리 및 유효성 검사**
    - 유효하지 않은 카테고리 값을 입력할 경우 에러 응답을 반환하도록 하였습니다.
    - 만약 필수 입력 필드가 비어 있을 경우 프론트에서 유효성 검사를 통해 사용자에게 에러 메시지를 제공하고, Null 데이터가 서버단으로 들어오지 못하도록 설계하였습니다.
    - 모든 에러 응답은 `CommonResponse` 객체를 통해 통일된 형식으로 반환하였습니다.

## 코드 빌드, 테스트, 실행 방법


### 빌드 및 실행 방법
1. 프로젝트 디렉터리로 이동합니다:
    ```bash
    cd 프로젝트_디렉토리
    ```

2. Gradle을 사용해 프로젝트를 빌드합니다:
    ```bash
    ./gradlew build
    ```

3. 애플리케이션을 실행합니다:
    ```bash
    ./gradlew bootRun
    ```

4. API 요청은 **Postman**을 사용하여 테스트할 수 있습니다.

### API 테스트
- 전체 테스트는 JUnit 5 기반의 유닛 테스트로 작성되었습니다.
- 테스트 실행:
    ```bash
    ./gradlew test
    ```
- 테스트는 `BrandServiceUnitTest`와 같은 단위 테스트 클래스에서 개별 메소드의 동작을 검증하였습니다.

### API Endpoint
1. **카테고리 별 최저가격 브랜드와 상품 가격, 총액**
    ```http
    GET /api/brand/lowest-price
    ```
2. **단일 브랜드의 모든 카테고리 상품을 구매할 때 최저가격에 판매하는 브랜드와 카테고리의 상품가격, 총액**
    ```http
    GET /api/brand/lowest-total-price
    ```
3. **카테고리 이름으로 최저, 최고 가격 브랜드와 상품 가격 조회**
    ```http
    GET /api/brand/price-info?categoryName={categoryName}
    ```
4. **브랜드 및 상품 추가/업데이트**
    ```http
    POST /api/brand
    ```
5. **브랜드 삭제**
    ```http
    DELETE /api/brand/delete-brand/{brandId}
    ```
6. **상품 삭제**
    ```http
    DELETE /api/brand/delete-product/{productId}
    ```


## Integration Test 시나리오

### 서버 시작
   ```
   ./gradlew build
   java -jar build/libs/api-task-0.0.1-SNAPSHOT.jar
   ```
실행 후 `http://localhost:8080/web` 으로 접속.(초기 데이터는 서버 시작시 data.sql로 설정)  

### 시나리오 1: 카테고리별 최저 가격 조회 테스트
1. **카테고리별 최저 가격 조회**:
   - 웹 페이지에서 "조회" 버튼을 클릭하여 데이터를 가져옵니다.
   - 각 카테고리별로 최저 가격을 제공하는 브랜드 정보와 가격이 정상적으로 테이블에 표시되는지 확인합니다.

### 시나리오 2: 단일 브랜드의 모든 카테고리 상품을 구매할 때 최저가격에 판매하는 브랜드와 카테고리의 상품가격, 총액 조회 테스트
1. **브랜드 마다 모든 카테고리 상품의 총합을 비교하여 가장 가격이 낮은 브랜드와 그 카테고리 상품을 조회**:
   - 웹 페이지에서 "조회" 버튼을 클릭하여 데이터를 가져옵니다.
   - 각 카테고리별로 최저 가격을 제공하는 브랜드 정보와 가격이 정상적으로 테이블에 표시되는지 확인합니다.

### 시나리오 3: 카테고리 이름을 파라미터로 받아 해당 카테고리의 최저, 최고 가격 브랜드와 상품 가격 조회 테스트
1. **파라미터로 넘어온 카테고리의 최저 가격 조회**:
   - 웹 페이지에서 "조회" 버튼을 클릭하여 데이터를 가져옵니다.
2. **파라미터로 넘어온 카테고리의 최고 가격 조회**:
   - 웹 페이지에서 "조회" 버튼을 클릭하여 데이터를 가져옵니다.

### 시나리오 4: 브랜드 및 상품 추가/업데이트 테스트
1. **브랜드 추가(기존에 브랜드 데이터가 없다면 insert, 있으면 update)**:
   - 웹 페이지에서 브랜드 명과 카테고리, 가격을 입력한 후 "저장" 버튼을 클릭합니다.
   - 성공적으로 저장되면, 저장된 브랜드와 상품 정보가 테이블에 나타납니다.
2. **유효성 검사**:
   - 브랜드 명을 입력하지 않고 저장 버튼을 클릭하면, "브랜드명은 필수 입력 사항입니다."라는 메시지가 표시됩니다.
   - 카테고리 명이나 가격이 비어 있을 때도 유효성 검사가 적용됩니다.

### 시나리오 5: 브랜드 삭제 테스트
1. **브랜드 삭제**:
   - 브랜드 조회 버튼을 클릭해 전체 브랜드 목록을 가져옵니다.
   - 각 브랜드 행의 "삭제" 버튼을 클릭하여 브랜드를 삭제할 수 있습니다.
   - 삭제 후 브랜드가 목록에서 사라지는지 확인합니다.

### 시나리오 6: 상품 삭제 테스트
1. **상품 삭제**:
   - 브랜드 조회 버튼을 클릭해 해당 브랜드의 상품 목록을 확인합니다.
   - 상품 옆의 "삭제" 버튼을 클릭하여 해당 상품을 삭제할 수 있습니다.
   - 삭제 후 페이지에서 alert를 "확인"버튼을 클릭릭 한 후 2초 뒤 페이지가 리로드 됩니다. 이후 다시 조회 버튼을 눌러 상품이 목록에서 사라지는지 확인합니다.


## 기타 추가 정보

### 엔티티 구조
- **Brand**: 브랜드 정보를 저장하는 테이블입니다.
- **ProductPrice**: 브랜드 하위에 속한 상품 정보와 각 상품의 카테고리 및 가격을 저장합니다.
> 처음 엔티티 설계 당시, 브랜드 엔티티 안에 @ElementCollection을 사용하여, Brand entity 와 product_price 간의 관계를 간단하게 표현하려 했습니다.

예시)
```@ElementCollection
@CollectionTable(name = "product_price", joinColumns = @JoinColumn(name = "brand_id"))
@MapKeyColumn(name = "category")
@Column(name = "price")
private Map<String, Integer> productPrices = new HashMap<>();
```

>하지만 구현 1, 구현2 과제의 테스트를 하면서 (상의, 아우터, 바지, 스니커즈, 가방, 모자, 양말, 액세서리) 의 정렬이 필요하다고 판단하여 설계를 변경하였습니다.
물론 프론트에서도 정렬을 할 수 있지만 추후 설계를 고려하여 백엔드에서 정렬된 응답을 주는 것이 맞다고 판단하였습니다. 
> 
> ElementCollection 사용하여 추가적인 엔터티를 사용하지 않고도 데이터베이스에서 일대다 관계를 관리하려 했던 초기 목적을 포기하고
> 정렬을 해결하기 위하여 엔터티 기반 설계, product_price를 별도의 엔티티로 만들고, Brand와 ProductPrice 간의 일대다 관계를 설정하였습니다.
> 
> 그리고 카테고리 목록을 Enum 을 사용하여 상수 값을 그룹화 시키고 타입 안정성을 고려하였습니다. 

### immutable class 사용
- Dto 객체 작성시 record 클래스를 사용하여 불변성을 보장하였습니다.
- 의존성 주입 방식을 생성자 주입 방식(private final 사용)으로 사용하였습니다.
- 함수 파라미터 재할당을 막기위해 final 키워드 명시하였습니다.

### Transactional 속성 명시 
- readOnly 속성 명시하여 영속상태 관리(Dirty checking) 및 메모리 최적화, 가독성 향상을 고려하였습니다.
  
### Response 객체 반환
- Entity 보호 및 Entity 변경 사항이 발생할 때 API 의 스펙이 변할 것을 고려햐여 Response record 사용하였습니다.
- 에러 발생 시에 CommonResponse.fail() 메소드를 통해 실패 응답을 메시지와 함께 일관되게 반환하도록 설계하였습니다. 

### 프론트 엔드
- 이전 프로젝트에서 사용하였던 `thymeleaf`와 `vue.js`를 사용하여 간단하게 웹에서 테스트 할 수 있는 환경을 제공 하였습니다.
> 구현 4-2) 브랜드 및 상품을 삭제 하는 API에서 4-2) 전체 상품 조회를 웹 페이지에서 테스트 하실때 삭제 버튼을 누르고 alert가 뜬 뒤 2초 후에 전체 페이지가 reload 됩니다.
> 
> product 삭제 이후 재조회를 하는데 dataTables의 reload를 구현하지 못해 개인적으로 아쉬웠습니다.
> 
