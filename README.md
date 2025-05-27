# swap-report-poc
Swagger URL:

```bash
http://localhost:8080/swagger-ui/index.html
```
Alert.java에
@JsonCreator + @JsonProperty는 KafkaConsumer 등에서 JSON 역직렬화를 보장하는 데 매우 유용합니다.


 Prometheus 스크랩 URL: http://localhost:8080/actuator/prometheus

helm/
└── swap-report/
    ├── Chart.yaml
    ├── values.yaml
    └── templates/
        ├── deployment.yaml
        ├── service.yaml
        └── ingress.yaml

확장 아이디어:

Spring Cloud Gateway를 통해 API Gateway 분리

Istio 기반 서비스 메쉬 도입

S3 / GCS 기반 보고서 아카이빙

Elasticsearch + Kibana 기반 검색 로그 시스템


확장 구성의 첫 단계인 Spring Cloud Gateway 기반 API Gateway 분리를 실 코드와 설정 중심으로 하나씩 완성해 나가겠습니다.

✅ Step 1: Spring Cloud Gateway - 개요
🎯 목적
외부 클라이언트의 요청을 단일 지점(Gateway)에서 수신

URI 기반 라우팅으로 내부 서비스로 분산

인증, 로깅, CORS, 속도 제한 등의 공통 concern 처리

✅ Step 2: 프로젝트 구성
🔧 폴더 구조 예시
css
Copy
Edit
swap-report-gateway/
├── src/main/java/com/swap/gateway
│   ├── GatewayApplication.java
│   └── config/
│       ├── RouteConfig.java
│       └── JwtAuthenticationFilter.java
├── src/main/resources/
│   └── application.yml
└── pom.xml
✅ Step 3: pom.xml
xml
Copy
Edit
<project>
  <dependencies>
    <dependency>
      <groupId>org.springframework.cloud</groupId>
      <artifactId>spring-cloud-starter-gateway</artifactId>
    </dependency>
    <dependency>
      <groupId>org.springframework.boot</groupId>
      <artifactId>spring-boot-starter-security</artifactId>
    </dependency>
    <!-- Optional: Actuator, config client, etc -->
  </dependencies>

  <dependencyManagement>
    <dependencies>
      <dependency>
        <groupId>org.springframework.cloud</groupId>
        <artifactId>spring-cloud-dependencies</artifactId>
        <version>2021.0.8</version>
        <type>pom</type>
        <scope>import</scope>
      </dependency>
    </dependencies>
  </dependencyManagement>
</project>
✅ Step 4: application.yml
yaml
Copy
Edit
server:
  port: 8080

spring:
  cloud:
    gateway:
      default-filters:
        - AddResponseHeader=X-Gateway-Header, Swap-Gateway
      routes:
        - id: report-service
          uri: http://localhost:8081
          predicates:
            - Path=/report/**
        - id: inference-service
          uri: http://localhost:8082
          predicates:
            - Path=/inference/**
        - id: alert-service
          uri: http://localhost:8083
          predicates:
            - Path=/alert/**
이 구성은 향후 Eureka/Consul 같은 서비스 디스커버리와 연동 가능 (uri: lb://report-service)

✅ Step 5: GatewayApplication.java
java
Copy
Edit
@SpringBootApplication
public class GatewayApplication {
    public static void main(String[] args) {
        SpringApplication.run(GatewayApplication.class, args);
    }
}
✅ Step 6: 필터 구현 예시 (JWT 인증)
java
Copy
Edit
@Component
public class JwtAuthenticationFilter implements GlobalFilter {
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        String authHeader = exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        }
        // 실제 JWT 검증 로직 삽입 가능
        return chain.filter(exchange);
    }
}
✅ Step 7: 테스트
bash
Copy
Edit
curl http://localhost:8080/report/status
curl http://localhost:8080/inference/check
curl http://localhost:8080/alert/recent
📌 다음 단계
Gateway에서 JWT 인증 필터를 확장하여 사용자 역할(Role)에 따른 라우팅 분기

Circuit Breaker, Rate Limiter 설정 추가

로깅 및 Request/Response trace 기록 추가 (→ Zipkin 연계 예정)

▶ 다음 예정: Istio 서비스 메시 설정 (2단계)
서비스 등록

트래픽 분할

mTLS 설정 등

필요하시면 위 코드를 GitHub에 push할 수 있도록 도와드릴 수도 있습니다.
다음으로 바로 Istio 구성 넘어갈까요, 아니면 Gateway에 인증/로깅 등 세부 기능을 더 채워볼까요?







You said:
swap-report는 onprem이고 swap-report-gateway는 하이브리드 멀티 클라우드를 목적으로한거지?


ChatGPT said:
맞습니다, 정확하게 짚으셨습니다.

✅ 아키텍처적 차이 정리
🏢 swap-report (온프렘 중심)
운영 위치: 주로 온프레미스 환경 (사내 IDC, 폐쇄망 등)

특징:

보안/규제 요건이 까다로운 금융 거래 보고 시스템

내부 DB, Kafka, Redis 등 로컬 네트워크 기반으로 운영

주로 모놀리틱 또는 내부 마이크로서비스

☁️ swap-report-gateway (하이브리드 멀티 클라우드 게이트웨이)
운영 위치: 외부망 또는 클라우드 게이트웨이 위치 (AWS, GCP, Azure 등)

역할:

온프렘 swap-report와 외부 클라우드 인프라 사이 보안/라우팅 경계층

외부 사용자의 요청을 정책 기반으로 내부에 프록시

향후 클라우드로 일부 기능(예: 비동기 inference, 검색, 대시보드 등)을 분리 가능

추가 가능성:

Istio와 연계하여 Cross-Mesh Federation 구성

Cloud CDN, Auth0, Cloud NAT, Global LB 연계

✅ 예시 시나리오
🔐 외부 → Gateway → 내부 온프렘
scss
Copy
Edit
[인터넷 사용자]
   ↓
[Cloud Gateway (Spring Cloud Gateway)]
   ↓     (URI Path, JWT 인증, 속도 제한)
[VPN 또는 Cloud Interconnect]
   ↓
[OnPrem `swap-report`]
🧩 왜 이 구성이 필요한가?
이유	설명
보안 분리	인터넷 노출은 Gateway만, swap-report는 비공개 네트워크에만 존재
멀티 클라우드 연계	AWS의 Alert AI, GCP의 VertexAI, Azure의 Search Service 등을 병렬 연동
확장성 확보	보고서 저장은 GCS, 검색은 Elasticsearch, 트래픽은 Istio로 분산 가능
규제 준수 + 유연성	데이터를 온프렘에 두면서 클라우드 기능 활용 가능 (하이브리드 전략)

✅ 결론
swap-report는 보안을 중시하는 온프렘 기반 핵심 시스템,
swap-report-gateway는 클라우드 친화적 확장성과 통합을 위한 프론트 컨트롤러입니다.

이 철학에 따라 다음 단계인 Istio 기반 서비스 메시 설계에 진입하면, 내부 서비스 간 제어와 클라우드 연동 확장 모두 수월해집니다.
