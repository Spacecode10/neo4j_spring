
package com.example.accessingdataneo4j;

import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.neo4j.repository.config.EnableNeo4jRepositories;

@SpringBootApplication
@EnableNeo4jRepositories
public class AccessingDataNeo4jApplication {

	private final static Logger log = LoggerFactory.getLogger(AccessingDataNeo4jApplication.class);

	public static void main(String[] args) throws Exception {
		SpringApplication.run(AccessingDataNeo4jApplication.class, args);
		System.exit(0);
	}

	@Bean
	CommandLineRunner demo(PersonRepository personRepository) {
		return args -> {

			personRepository.deleteAll();

			Person id1 = new Person("123");
			Person id2 = new Person("456");
			Person id3 = new Person("789");

			List<Person> team = Arrays.asList(id1, id2, id3);

			log.info("Before linking up with Neo4j...");

			team.stream().forEach(person -> log.info("\t" + person.toString()));

			personRepository.save(id1);
			personRepository.save(id2);
			personRepository.save(id3);

			id1 = personRepository.findByName(id1.getName());
			id1.worksWith(id2);
			id1.worksWith(id3);
			personRepository.save(id1);

			id2 = personRepository.findByName(id2.getName());
			id2.worksWith(id3);
			// We already know that id2 works with id1
			personRepository.save(id2);

			// We already know id3 works with id2 and id1

			log.info("Lookup each node by id...");
			team.stream().forEach(person -> log.info(
					"\t" + personRepository.findByName(person.getName()).toString()));

			List<Person> teammates = personRepository.findByTeammatesName(id1.getName());
			log.info("The following have id1 as a relation...");
			teammates.stream().forEach(person -> log.info("\t" + person.getName()));
		};
	}

}
