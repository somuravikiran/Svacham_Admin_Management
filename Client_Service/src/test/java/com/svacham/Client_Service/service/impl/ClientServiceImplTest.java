package com.svacham.Client_Service.service.impl;

import com.svacham.Client_Service.dto.ClientSummaryDto;
import com.svacham.Client_Service.entity.Client;
import com.svacham.Client_Service.repository.ClientRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ClientServiceImplTest {

    @Mock
    private ClientRepository clientRepository;

    @InjectMocks
    private ClientServiceImpl clientService;

    private Client sampleClient;

    @BeforeEach
    void setUp() {
        sampleClient = new Client();
        sampleClient.setClientName("Test Client");
        sampleClient.setTotalAmount(1000.0);
        sampleClient.setAmountPaid(200.0);
    }

    @Test
    void addClient_setsCreatedDateAndBalanceAndSaves() {
        when(clientRepository.save(any(Client.class))).thenAnswer(i -> i.getArgument(0));

        Client saved = clientService.addClient(sampleClient);

        assertNotNull(saved.getCreatedDate());
        assertEquals(800.0, saved.getBalanceAmount());
        verify(clientRepository, times(1)).save(saved);
    }

    @Test
    void getClientById_found_returnsClient() {
        when(clientRepository.findById("1")).thenReturn(Optional.of(sampleClient));

        Client c = clientService.getClientById("1");

        assertNotNull(c);
        assertEquals("Test Client", c.getClientName());
    }

    @Test
    void getClientById_notFound_throwsRuntimeException() {
        when(clientRepository.findById("2")).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class, () -> clientService.getClientById("2"));
        assertEquals("Client Not Found", ex.getMessage());
    }

    @Test
    void updateClient_updatesFieldsAndBalance() {
        Client existing = new Client();
        existing.setClientName("Old Name");
        existing.setTotalAmount(500.0);
        existing.setAmountPaid(100.0);

        Client update = new Client();
        update.setClientName("New Name");
        update.setPhoneNumber("9999999999");
        update.setAddress("New Address");
        update.setCity("CityX");
        update.setState("StateY");
        update.setTotalAmount(2000.0);
        update.setAmountPaid(500.0);

        when(clientRepository.findById("5")).thenReturn(Optional.of(existing));
        when(clientRepository.save(any(Client.class))).thenAnswer(i -> i.getArgument(0));

        Client result = clientService.updateClient("5", update);

        assertEquals("New Name", result.getClientName());
        assertEquals(1500.0, result.getBalanceAmount());
        assertEquals("CityX", result.getCity());
        verify(clientRepository, times(1)).save(any(Client.class));
    }

    @Test
    void deleteClient_callsRepository() {
        doNothing().when(clientRepository).deleteById("10");

        clientService.deleteClient("10");

        verify(clientRepository, times(1)).deleteById("10");
    }

    @Test
    void getTotalBalanceAmount_sumsBalancesSkippingNulls() {
        Client c1 = new Client(); c1.setBalanceAmount(100.0);
        Client c2 = new Client(); c2.setBalanceAmount(null);
        Client c3 = new Client(); c3.setBalanceAmount(300.0);

        when(clientRepository.findAll()).thenReturn(List.of(c1, c2, c3));

        Double total = clientService.getTotalBalanceAmount();

        assertEquals(400.0, total);
    }

    @Test
    void getClientSummary_computesAggregates() {
        Client c1 = new Client();
        c1.setTotalAmount(1000.0);
        c1.setAmountPaid(200.0);
        c1.setBalanceAmount(800.0);

        Client c2 = new Client();
        c2.setTotalAmount(500.0);
        c2.setAmountPaid(100.0);
        c2.setBalanceAmount(400.0);

        when(clientRepository.findAll()).thenReturn(List.of(c1, c2));

        ClientSummaryDto summary = clientService.getClientSummary();

        assertEquals(2L, summary.getTotalClients());
        assertEquals(1500.0, summary.getTotalBusinessAmount());
        assertEquals(300.0, summary.getTotalAmountPaid());
        assertEquals(1200.0, summary.getTotalPendingBalance());
    }

    @Test
    void getClientsByCity_andByState_filtersCorrectly() {
        Client c1 = new Client(); c1.setCity("Mumbai"); c1.setState("Maharashtra");
        Client c2 = new Client(); c2.setCity("Pune"); c2.setState("Maharashtra");
        Client c3 = new Client(); c3.setCity("Mumbai"); c3.setState("Gujarat");

        when(clientRepository.findAll()).thenReturn(List.of(c1, c2, c3));

        var byCity = clientService.getClientsByCity("Mumbai");
        assertEquals(2, byCity.size());

        var byState = clientService.getClientsByState("Maharashtra");
        assertEquals(2, byState.size());
    }
}


