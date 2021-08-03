package drosa99.spring.reactiveexamples;

import drosa99.spring.reactiveexamples.domain.Person;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class PersonRepositoryImplTest {

    PersonRepositoryImpl personRepository;

    @BeforeEach
    void setUp() {
        personRepository = new PersonRepositoryImpl();
    }

    @Test
    void getByIdBlock() {
        Mono<Person> personMono = personRepository.getById(1);

        Person person = personMono.block();

        System.out.println(person.toString());
    }

    @Test
    void getByIdSubscribe() {
        Mono<Person> personMono = personRepository.getById(1);

        StepVerifier.create(personMono).expectNextCount(1).verifyComplete();

        personMono.subscribe(person -> {
            System.out.println(person.toString());
        });
    }

    @Test
    void getByIdSubscribeNotFound() {
        Mono<Person> personMono = personRepository.getById(9);

        //expectNextCount conta quantas iteracoes na stream
        StepVerifier.create(personMono).expectNextCount(0).verifyComplete();

        personMono.subscribe(person -> {
            System.out.println(person.toString());
        });
    }

    @Test
    void getByIdMapFunction() {
        Mono<Person> personMono = personRepository.getById(1);

        personMono.map(person -> {
            System.out.println(person.toString());
            return person.getFirstName();
        }).subscribe(firtName -> {
            System.out.println("from map: " + firtName);
        });
    }

    @Test
    void fluxTestBlockFirst() {
        Flux<Person> personFlux = personRepository.findAll();

        Person person = personFlux.blockFirst();

        System.out.println(person.toString());
    }

    @Test
    void fluxTestFluxSubscribe() {
        Flux<Person> personFlux = personRepository.findAll();

        personFlux.subscribe(person -> {
            System.out.println(person.toString());
        });
    }

    @Test
    void fluxTestToListMono() {
        Flux<Person> personFlux = personRepository.findAll();

        Mono<List<Person>> personListMono = personFlux.collectList();

        personListMono.subscribe(list -> {
            list.forEach(person -> {
                System.out.println(person.toString());
            });
        });
    }

    @Test
    void testFindPersonById() {
        Flux<Person> personFlux = personRepository.findAll();
        final Integer id = 3;
        Mono<Person> personMono = personFlux.filter(person -> person.getId().equals(id)).next();
        personMono.subscribe(person -> {
            System.out.println(person.toString());
        });
    }

    @Test
    void testFindPersonByIdNotFound() {
        Flux<Person> personFlux = personRepository.findAll();
        final Integer id = 8;
        Mono<Person> personMono = personFlux.filter(person -> person.getId().equals(id)).next();
        personMono.subscribe(person -> {
            System.out.println(person.toString());
        });
    }

    @Test
    void testFindPersonByIdNotFoundWithException() {
        Flux<Person> personFlux = personRepository.findAll();
        final Integer id = 8;
        Mono<Person> personMono = personFlux.filter(person -> person.getId().equals(id)).single();
        personMono.doOnError(throwable -> {
            System.out.println("I went boom");
        }).onErrorReturn(Person.builder().build()).subscribe(person -> {
            System.out.println(person.toString());
        });
    }

    @Test
    void testGetByIdMethod() {
        Mono<Person> person = personRepository.getById(1);
        assertEquals(person.block().getId(), 1);
    }

    @Test
    void testGetByIdMethoException() {
        Mono<Person> person = personRepository.getById(10);
        Person nullPerson = person.block();
        assertNull(nullPerson);
    }
}