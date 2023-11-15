package cab.tapsi.pack.example.kafkaexample

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.data.mongodb.config.EnableReactiveMongoAuditing
import org.springframework.data.mongodb.repository.config.EnableReactiveMongoRepositories

@SpringBootApplication
@EnableReactiveMongoAuditing
@EnableReactiveMongoRepositories
class KafkaExampleApplication

fun main(args: Array<String>) {
	runApplication<KafkaExampleApplication>(*args)
}
