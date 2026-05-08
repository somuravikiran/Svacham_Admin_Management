package com.svacham.Client_Service.service;

import com.svacham.Client_Service.dto.AuthValidationResponseDto;
import com.svacham.Client_Service.entity.Client;
import com.svacham.Client_Service.dto.ClientSummaryDto;

import java.util.List;

public interface ClientService {

    AuthValidationResponseDto validateToken(String token);

    Client addClient(Client client);

    List<Client> getAllClients();

    Client getClientById(Long id);

    Client updateClient(Long id, Client client);

    void deleteClient(Long id);

    Double getTotalBalanceAmount();

    ClientSummaryDto getClientSummary();

    List<Client> getClientsByCity(String city);

    List<Client> getClientsByState(String state);
}