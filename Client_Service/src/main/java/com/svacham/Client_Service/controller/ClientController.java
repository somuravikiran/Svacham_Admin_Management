package com.svacham.Client_Service.controller;
import com.svacham.Client_Service.dto.ClientSummaryDto;
import com.svacham.Client_Service.entity.Client;
import com.svacham.Client_Service.service.ClientService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/clients")
@RequiredArgsConstructor
public class ClientController {

    private final ClientService clientService;
   // private final AuthServiceClient authServiceClient;
//
//    private void validateToken(String token) {
//        var response = clientService.validateToken(token);
//
//        if (response == null || !response.isValid()) {
//            throw new RuntimeException("Unauthorized Access - Invalid Token");
//        }
//    }
//
//    @PostMapping("/add")
//    public Client addClient(@RequestHeader("Authorization") String token,
//                            @RequestBody Client client) {
////
////        System.out.println("STEP 1 : REQUEST ENTERED CONTROLLER");
////        System.out.println("STEP 2 : TOKEN = " + token);
//
//        validateToken(token);
//
//        System.out.println("STEP 3 : TOKEN VALIDATED SUCCESS");
//
//        Client saved = clientService.addClient(client);
//
//        System.out.println("STEP 4 : CLIENT SAVED");
//
//        return saved;
//    }
//
    @PostMapping("/add")
    public Client addClient(@RequestHeader("Authorization") String token,
                            @RequestBody Client client) {
        // validate token before performing action
        clientService.validateToken(token);

        // proceed with saving the client
        return clientService.addClient(client);
    }

    @GetMapping("/all")
    public List<Client> getAllClients(@RequestHeader("Authorization") String token) {
        clientService.validateToken(token);
//        validateToken(token);
        return clientService.getAllClients();
    }

    @GetMapping("/{id}")
    public Client getClientById(@RequestHeader("Authorization") String token,
                                @PathVariable String id) {
        clientService.validateToken(token);
//        validateToken(token);
        return clientService.getClientById(id);
    }

    @PutMapping("/update/{id}")
    public Client updateClient(@RequestHeader("Authorization") String token,
                               @PathVariable String id,
                               @RequestBody Client client) {
        clientService.validateToken(token);
//        validateToken(token);
        return clientService.updateClient(id, client);
    }

    @DeleteMapping("/delete/{id}")
    public String deleteClient(@RequestHeader("Authorization") String token,
                               @PathVariable String id) {
        clientService.validateToken(token);
//        validateToken(token);
        clientService.deleteClient(id);
        return "Client Deleted Successfully";
    }
//    @GetMapping("/summary")
//    public Map<String, Object> getSummary() {
//
////        Map<String, Object> response = new HashMap<>();
////
////        Long totalClients = clientRepository.count();
////        Double totalPending = clientRepository.getTotalPendingAmount();
////
////        response.put("totalClients", totalClients != null ? totalClients : 0);
////        response.put("totalPendingAmount", totalPending != null ? totalPending : 0.0);
////
////        return response;
//    }
    @GetMapping("/summary")
    public ClientSummaryDto getClientSummary(@RequestHeader("Authorization") String token) {
        clientService.validateToken(token);
//        validateToken(token);
        return clientService.getClientSummary();
    }

    @GetMapping("/city/{city}")
    public List<Client> getClientsByCity(@RequestHeader("Authorization") String token,
                                         @PathVariable String city) {
        clientService.validateToken(token);
//        validateToken(token);
        return clientService.getClientsByCity(city);
    }

    @GetMapping("/state/{state}")
    public List<Client> getClientsByState(@RequestHeader("Authorization") String token,
                                          @PathVariable String state) {
        clientService.validateToken(token);
//        validateToken(token);
        return clientService.getClientsByState(state);
    }
}
