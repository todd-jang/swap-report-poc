package com.swapreporting.adapter.incoming.kafka;

//import com.swapreporting.domain.alert.model.Alert;
//import org.apache.kafka.clients.consumer.ConsumerRecord;
//import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class AlertConsumer {

//    @KafkaListener(topics = "alert-topic", groupId = "swap-report-group")
//    public void consume(ConsumerRecord<String, Alert> record) {
//        Alert alert = record.value();
//        System.out.println("Consumed alert: " + alert.getTradeId() + " - " + alert.getMessage());
//        // TODO: 저장 또는 대응 로직
//    }
}