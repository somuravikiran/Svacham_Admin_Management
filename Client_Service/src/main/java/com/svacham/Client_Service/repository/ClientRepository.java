package com.svacham.Client_Service.repository;
import com.svacham.Client_Service.dto.ClientSummaryDto;
import com.svacham.Client_Service.entity.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ClientRepository extends JpaRepository<Client, Long> {

    List<Client> findByCity(String city);

    List<Client> findByState(String state);
}
