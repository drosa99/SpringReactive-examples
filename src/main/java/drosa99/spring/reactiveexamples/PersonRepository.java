package drosa99.spring.reactiveexamples;

import drosa99.spring.reactiveexamples.domain.Person;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface PersonRepository {
		Mono<Person> getById(Integer id);

		Flux<Person> findAll();
}
