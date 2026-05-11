package com.svacham.Client_Service.service;

import com.svacham.Client_Service.dto.AuthValidationResponseDto;
import com.svacham.Client_Service.entity.Client;
import com.svacham.Client_Service.dto.ClientSummaryDto;

import java.util.List;

public interface ClientService {

    AuthValidationResponseDto validateToken(String token);

    Client addClient(Client client);

    List<Client> getAllClients();

    Client getClientById(String id);

    Client updateClient(String id, Client client);

    void deleteClient(String id);

    Double getTotalBalanceAmount();

    ClientSummaryDto getClientSummary();

    List<Client> getClientsByCity(String city);

    List<Client> getClientsByState(String state);
}