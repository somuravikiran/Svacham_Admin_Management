package com.svacham.Client_Service.repository;

import com.svacham.Client_Service.entity.Client;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface ClientRepository extends MongoRepository<Client, String> {

    List<Client> findByCity(String city);

    List<Client> findByState(String state);
}
