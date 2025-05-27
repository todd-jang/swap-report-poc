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
