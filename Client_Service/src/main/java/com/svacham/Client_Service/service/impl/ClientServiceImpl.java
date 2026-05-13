package com.svacham.Client_Service.service.impl;

import com.svacham.Client_Service.dto.AuthValidationResponseDto;
import com.svacham.Client_Service.dto.ClientSummaryDto;
import com.svacham.Client_Service.entity.Client;
import com.svacham.Client_Service.repository.ClientRepository;
import com.svacham.Client_Service.service.ClientService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ClientServiceImpl implements ClientService {

    private final ClientRepository clientRepository;

    private final WebClient.Builder webClientBuilder;

    public AuthValidationResponseDto validateToken(String token) {

        try {

            System.out.println("TOKEN FROM CONTROLLER = " + token);

            AuthValidationResponseDto response = webClientBuilder.build()
                    .get()
                    .uri("https://svacham-admin-management-1.onrender.com/auth/validate")
                    .header("Authorization", token)
                    .retrieve()
                    .bodyToMono(AuthValidationResponseDto.class)
                    .block();

            return response;

        } catch (Exception e) {
            System.out.println("Auth service Failed");
            e.printStackTrace();
            return null;
//            throw new RuntimeException("AUTH-SERVICE is unavailable : " + e.getMessage());
        }
    }


    @Override
    public Client addClient(Client client) {
        client.setCreatedDate(LocalDate.now());

        Double total = client.getTotalAmount() == null ? 0.0 : client.getTotalAmount();
        Double paid = client.getAmountPaid() == null ? 0.0 : client.getAmountPaid();

        client.setBalanceAmount(total - paid);

        return clientRepository.save(client);
    }

    @Override
    public List<Client> getAllClients() {
        return clientRepository.findAll();
    }

    @Override
    public Client getClientById(String id) {

        return clientRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Client Not Found"));
    }

    @Override
    public Client updateClient(String id, Client client) {

        Client existing = getClientById(id);

        existing.setClientName(client.getClientName());
        existing.setPhoneNumber(client.getPhoneNumber());
        existing.setAddress(client.getAddress());
        existing.setCity(client.getCity());
        existing.setState(client.getState());
        existing.setTotalAmount(client.getTotalAmount());
        existing.setAmountPaid(client.getAmountPaid());

        Double total = client.getTotalAmount() == null ? 0.0 : client.getTotalAmount();
        Double paid = client.getAmountPaid() == null ? 0.0 : client.getAmountPaid();

        existing.setBalanceAmount(total - paid);

        return clientRepository.save(existing);
    }
    @Override
    public void deleteClient(String id) {

        clientRepository.deleteById(id);
    }

    @Override
    public Double getTotalBalanceAmount() {
        return clientRepository.findAll()
                .stream()
                .mapToDouble(c -> c.getBalanceAmount() == null ? 0.0 : c.getBalanceAmount())
                .sum();
    }

    @Override
    public ClientSummaryDto getClientSummary() {
        List<Client> clients = clientRepository.findAll();

        Long totalClients = (long) clients.size();

        Double totalBusinessAmount = clients.stream()
                .mapToDouble(c -> c.getTotalAmount() == null ? 0.0 : c.getTotalAmount())
                .sum();

        Double totalAmountPaid = clients.stream()
                .mapToDouble(c -> c.getAmountPaid() == null ? 0.0 : c.getAmountPaid())
                .sum();

        Double totalPendingBalance = clients.stream()
                .mapToDouble(c -> c.getBalanceAmount() == null ? 0.0 : c.getBalanceAmount())
                .sum();

        return ClientSummaryDto.builder()
                .totalClients(totalClients)
                .totalBusinessAmount(totalBusinessAmount)
                .totalAmountPaid(totalAmountPaid)
                .totalPendingBalance(totalPendingBalance)
                .build();
    }

    @Override
    public List<Client> getClientsByCity(String city) {
        return clientRepository.findAll()
                .stream()
                .filter(c -> c.getCity() != null && c.getCity().equalsIgnoreCase(city))
                .toList();
    }

    @Override
    public List<Client> getClientsByState(String state) {
        return clientRepository.findAll()
                .stream()
                .filter(c -> c.getState() != null && c.getState().equalsIgnoreCase(state))
                .toList();
    }
}