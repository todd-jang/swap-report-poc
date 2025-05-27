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
