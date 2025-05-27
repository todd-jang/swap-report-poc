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


멀티 클라우드 AI/서비스 연계 시 기술적 문제
다음은 AWS, GCP, Azure 등 클라우드의 특정 서비스를 병렬로 연계할 때 발생하는 핵심 기술 이슈들입니다:

문제 영역	기술적 문제	대응 방법
1. 인증	각 클라우드가 서로 다른 인증 체계 (IAM, OAuth, API Key) 사용	Gateway에서 통합 인증 토큰 관리 (ex: 외부 Key Vault + 서비스별 Secret Mount)
2. 네트워크	온프렘↔클라우드 간 VPN or VPC Peering 필요	Cloud Interconnect, VPC Peering, 또는 Istio Federation 활용
3. 데이터 정합성	실시간 분석 위해 여러 클라우드에 동시 전송/복제 필요	Kafka Connect, Debezium CDC, AWS DMS, GCP Datastream 등으로 CDC 구성
4. 장애 복구	특정 클라우드 불능 시, 요청 재전송 또는 Failover 어려움	Circuit Breaker + Retry + Cloud별 Backup Path 구성 (ex: Fallback to GCP if AWS down)
5. 성능 차이	AI 호출 시 응답 지연 발생 가능 (ex: VertexAI vs Bedrock vs Azure OpenAI)	Async 처리 + Response Timeout 조절 + QoS 정책 적용
6. API 사양 불일치	예측 요청 및 응답 구조가 다름 (예: Prompt 형식)	클라우드별 Wrapper 생성 → 표준화된 중간계층에서 추상화 처리

🔧 실현 가능한 아키텍처 예
plaintext
Copy
Edit
              [FastAPI Client (Edge)]
                        ↓
         ┌───────────[Cloud Gateway]────────────┐
         │                                      │
   [Spring Boot - swap-report (OnPrem)]   [Inference Layer (Cloud)]
         │                                      │
         │   Kafka / CDC   ↑       ↓ REST       │
         └──────────────→ [Vertex AI, Bedrock, Azure AI]
✅ 대응 전략 요약
전략	설명
클라이언트는 클라우드 (FastAPI + Web UI)	경량화 + 클라우드 배포로 민첩한 UX 제공
서버는 온프렘 유지 (Spring Boot)	보안/규제 지킨 채 핵심 처리
데이터 복제는 실시간 (CDC + Kafka)	클라우드 AI와 동기화된 판단 가능
AI 서비스 연계는 Wrapper 통해 통합	서로 다른 클라우드 AI를 공통 인터페이스로 추상화
API Gateway + Observability 강화	인증, 라우팅, 로깅, 트레이싱 등 관측성 확보

🧩 결론
✔️ swap-report-poc는 모놀리딕이더라도
→ 클라이언트 분리 + 데이터 동기화 + 클라우드 AI 인터페이스 구성
이 완료되면 실질적으로 하이브리드 멀티 클라우드 아키텍처로 진화 가능합니다.

✅ Step 1: Spring Cloud Gateway - 개요
🎯 목적
외부 클라이언트의 요청을 단일 지점(Gateway)에서 수신

URI 기반 라우팅으로 내부 서비스로 분산

인증, 로깅, CORS, 속도 제한 등의 공통 concern 처리

✅ Step 2: 프로젝트 구성

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


참고사항 

AIInferenceService 병렬 비동기 호출 + fallback + 응답 집계 + 모니터링 통합 설계 및 구현 예시입니다:

✅ 전체 요구사항 요약
✅ 클라우드별 AI 어댑터 구현 (문서 기반)

✅ 비동기 병렬 호출 (GCP + AWS + Azure 동시)

✅ 응답 Aggregation 처리 (가장 빠른 결과 or 다수결 등 전략 선택)

✅ 특정 클라우드 실패 시 fallback 처리

✅ Prometheus + Grafana 응답 시간 모니터링 포함

📦 1. 어댑터 인터페이스 (공통)
java
Copy
Edit
public interface AIInferenceService {
    CompletableFuture<InferenceResponse> inferAsync(InferenceRequest request);
    String getProvider(); // GCP, AWS, AZURE
}
☁️ 2. GCP Vertex AI 어댑터 예시 (문서 기반)
java
Copy
Edit
@Component
public class VertexAIAdapter implements AIInferenceService {

    private final WebClient webClient = WebClient.create();

    @Override
    public CompletableFuture<InferenceResponse> inferAsync(InferenceRequest request) {
        long start = System.currentTimeMillis();

        return webClient.post()
                .uri("https://vertex-ai.googleapis.com/v1/...")
                .header("Authorization", "Bearer ...")
                .bodyValue(request)
                .retrieve()
                .bodyToMono(String.class)
                .map(result -> new InferenceResponse(result, System.currentTimeMillis() - start, getProvider()))
                .toFuture();
    }

    @Override
    public String getProvider() {
        return "GCP";
    }
}
⚠️ AWSBedrockAdapter, AzureOpenAIAdapter 도 동일 구조로 작성
☑️ 실서비스에서는 API 문서 기반 Prompt 전송 포맷 맞춰야 함

⚙️ 3. 병렬 호출 + Aggregation + Fallback 처리 서비스
java
Copy
Edit
@Service
public class ParallelAIInferenceAggregator {

    private final List<AIInferenceService> services;

    public ParallelAIInferenceAggregator(List<AIInferenceService> services) {
        this.services = services;
    }

    public InferenceResponse inferWithAggregation(InferenceRequest request) {
        List<CompletableFuture<InferenceResponse>> futures = services.stream()
                .map(service -> service.inferAsync(request)
                        .exceptionally(e -> new InferenceResponse("Fallback from " + service.getProvider(), -1L, service.getProvider())))
                .collect(Collectors.toList());

        CompletableFuture<Void> allDone = CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]));

        try {
            allDone.get(5, TimeUnit.SECONDS); // timeout 설정
        } catch (Exception e) {
            // 전체 실패 처리
            return new InferenceResponse("All providers failed", -1L, "N/A");
        }

        List<InferenceResponse> results = futures.stream()
                .map(CompletableFuture::join)
                .filter(resp -> resp.getLatencyMs() > 0)
                .sorted(Comparator.comparingLong(InferenceResponse::getLatencyMs))
                .collect(Collectors.toList());

        return results.isEmpty() ? new InferenceResponse("No successful response", -1L, "N/A") : results.get(0); // fastest
    }
}
🔍 4. Prometheus 메트릭 통합
java
Copy
Edit
@Component
public class AIResponseMetrics {

    private final Summary latencySummary = Summary.build()
        .name("ai_inference_latency_ms")
        .help("Latency of AI responses in milliseconds")
        .labelNames("provider")
        .register();

    public void recordLatency(String provider, long latency) {
        latencySummary.labels(provider).observe(latency);
    }
}
➡️ 각 어댑터 또는 Aggregator 내에서 recordLatency(provider, latency) 호출

java
Copy
Edit
metrics.recordLatency("GCP", System.currentTimeMillis() - start);
📈 5. Grafana 대시보드 설정 (예시)
Prometheus 쿼리:

prometheus
Copy
Edit
ai_inference_latency_ms_sum{provider="GCP"}
패널:

응답 시간 평균/최대값

클라우드별 성공률

응답 카운트 (per provider)

✅ 종합 예: Controller
java
Copy
Edit
@RestController
@RequestMapping("/inference")
public class InferenceController {

    private final ParallelAIInferenceAggregator aggregator;

    public InferenceController(ParallelAIInferenceAggregator aggregator) {
        this.aggregator = aggregator;
    }

    @PostMapping
    public InferenceResponse infer(@RequestBody InferenceRequest request) {
        return aggregator.inferWithAggregation(request);
    }
}
  프로젝트 최종 구조

swap-report-poc/
├── swap-report-ai/            <-- 공통 AI 추론 서브모듈
│   ├── adapter/
│   │   ├── VertexAIAdapter.java
│   │   ├── AWSBedrockAdapter.java
│   │   ├── AzureOpenAIAdapter.java
│   │   ├── AnthropicClaudeAdapter.java
│   │   ├── MistralAdapter.java
│   │   └── CohereAdapter.java
│   ├── core/
│   │   ├── AIInferenceService.java
│   │   ├── InferenceRequest.java
│   │   ├── InferenceResponse.java
│   │   └── MultiCloudRouter.java
│   ├── service/
│   │   └── ParallelInferenceAggregator.java
│   ├── monitoring/
│   │   └── AIResponseMetrics.java
│   └── controller/
│       └── InferenceController.java


✅ 2. 어댑터 예시 (VertexAIAdapter.java)
java
Copy
Edit
@Component
public class VertexAIAdapter implements AIInferenceService {

    @Override
    public CompletableFuture<InferenceResponse> inferAsync(InferenceRequest request) {
        long start = System.currentTimeMillis();

        return WebClient.create()
            .post()
            .uri("https://vertex-ai.googleapis.com/v1/...") // 실제 endpoint
            .header("Authorization", "Bearer " + System.getenv("VERTEX_AI_KEY"))
            .bodyValue(Map.of("prompt", request.getPrompt()))
            .retrieve()
            .bodyToMono(String.class)
            .map(result -> new InferenceResponse(result, System.currentTimeMillis() - start, getProvider()))
            .toFuture();
    }

    @Override
    public String getProvider() {
        return "GCP";
    }
}
Cohere / Mistral / Claude도 동일 방식으로 확장 가능

API Key는 Vault 또는 Secrets Manager 연동 (아래 참조)

✅ 3. 병렬 Aggregator (ParallelInferenceAggregator.java)
java
Copy
Edit
@Service
public class ParallelInferenceAggregator {
    private final List<AIInferenceService> services;

    public ParallelInferenceAggregator(List<AIInferenceService> services) {
        this.services = services;
    }

    public InferenceResponse inferWithFallback(InferenceRequest request) {
        List<CompletableFuture<InferenceResponse>> futures = services.stream()
            .map(s -> s.inferAsync(request).exceptionally(e -> new InferenceResponse("FAIL", -1, s.getProvider())))
            .toList();

        CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();

        return futures.stream().map(CompletableFuture::join)
            .filter(r -> r.getLatencyMs() > 0)
            .min(Comparator.comparingLong(InferenceResponse::getLatencyMs))
            .orElse(new InferenceResponse("All failed", -1, "N/A"));
    }
}
✅ 4. 실시간 반응 가능한 아키텍처 설계
최신 데이터 기반 반응성 확보를 위한 2가지 핵심 요소:

① 온프레미스 + 클라우드 이중화 (Storage Layer)
PostgreSQL → AWS RDS + GCP Cloud SQL 양방향 동기화

Kafka (MirrorMaker 2.0) + Debezium → CDC 기반 실시간 이벤트 전달

② 분석 시스템에 최신 상태 반영
클라우드 AI 서비스에는 Kafka 스트림을 통해 최신 이벤트 Push

AIInferenceRequest는 S3 저장 → Notification → Trigger Lambda/Cloud Function → API 호출

✅ 5. 인증 Vault 통합 (HashiCorp Vault 예시)
⬥ Vault 구성
bash
Copy
Edit
vault secrets enable kv
vault kv put secret/ai-keys gcp_key=abc123 aws_key=def456 azure_key=xyz789
⬥ Spring Vault 연동
application.yml

yaml
Copy
Edit
spring:
  cloud:
    vault:
      uri: http://localhost:8200
      authentication: TOKEN
      token: s.abcdef123456
      kv:
        enabled: true
        application-name: ai-keys
⬥ 코드 사용 예시
java
Copy
Edit
@Value("${gcp_key}")
private String gcpApiKey;
이렇게 하면 어댑터에서 System.getenv() 또는 @Value로 키를 안전하게 로딩할 수 있습니다.

📈 6. Prometheus + Grafana 모니터링
/actuator/prometheus endpoint 노출

AIResponseMetrics 클래스에서 provider별 latency observe

Grafana에서 클라우드별 응답시간/성공률/에러율 시각화
