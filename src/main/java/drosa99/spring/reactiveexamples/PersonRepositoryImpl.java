package drosa99.spring.reactiveexamples;

import drosa99.spring.reactiveexamples.domain.Person;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public class PersonRepositoryImpl implements PersonRepository {

		Person michael = new Person(1, "Michael", "Weston");
		Person fiona = new Person(2, "Fiona", "Glenane");
		Person jesse = new Person(3, "Jesse", "Porter");

		@Override
		public Mono<Person> getById(Integer id) {
				return Mono.just(michael);
		}

		@Override
		public Flux<Person> findAll() {
				return Flux.just(michael, fiona, jesse);
		}
}
