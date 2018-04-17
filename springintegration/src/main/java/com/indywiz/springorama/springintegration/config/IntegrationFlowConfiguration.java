package com.indywiz.springorama.springintegration.config;

import com.indywiz.springorama.springintegration.model.Person;
import com.indywiz.springorama.springintegration.repository.PersonRepository;
import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.integration.dsl.Pollers;
import org.springframework.integration.dsl.channel.MessageChannels;
import org.springframework.integration.file.dsl.Files;
import org.springframework.integration.handler.GenericHandler;
import org.springframework.integration.handler.LoggingHandler.Level;

@Configuration
public class IntegrationFlowConfiguration {

  @Autowired
  PersonRepository personRepository;

  @Bean
  public IntegrationFlow fileInputFlow() {
    return IntegrationFlows.from(
        //Setting up the inbound adapter for the flow
        Files
            .inboundAdapter(new File("/tmp/in"))
            .autoCreateDirectory(true)
            .patternFilter("*.txt"), p -> p.poller(Pollers.fixedDelay(10, TimeUnit.SECONDS)
            .errorChannel(MessageChannels.direct().get())))
        // Transform the file content to string
        .transform(Files.toStringTransformer())
        //Transform the file content to list of lines in the file
        .<String, List<String>>transform(wholeText -> Arrays.asList(wholeText.split(Pattern.quote("\n"))))
        //Split the list to a single person record line
        .split()
        //Transform each line in the file and map to a Person record
        .<String, Person>transform(eachPersonText -> {
          List<String> tokenizedString = Arrays.asList(eachPersonText.split(Pattern.quote("|")));
          try {
            return Person.builder()
                .personId(Long.parseLong(tokenizedString.get(0).trim()))
                .personName(tokenizedString.get(1).trim())
                .personPhoneNumber(tokenizedString.get(2).trim())
                .build();
          } catch (Exception e) {
            return null;
          }
        })
        .filter(Objects::nonNull)
        // Save the record to the database.
        .handle((GenericHandler<Person>) (personRecordToSave, headers) -> personRepository
            .save(personRecordToSave))
        .log(Level.INFO)
        .get();
  }

}
