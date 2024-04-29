package com.mds.customer;

import com.project.clients.fraud.FraudCheckResponse;
import com.project.clients.fraud.FraudClient;
import com.project.clients.notification.NotificationClient;
import com.project.clients.notification.NotificationRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class CustomerService {

    private final CustomerRepository customerRepository;
    private final RestTemplate restTemplate;
    private final FraudClient fraudClient;
    private final NotificationClient notificationClient;


    public void registerCustomer(CustomerRegistrationRequest request) {
        Customer customer = Customer.builder().firstName(request.firstName()).lastName(request.lastName()).email(request.email()).build();
        customerRepository.saveAndFlush(customer);
        // todo: check if email valid
        // todo: check if email not taken

        FraudCheckResponse fraudCheckResponse = fraudClient.isFraudster(customer.getId());

        notificationClient.createNotification(NotificationRequest.builder()
                .toCustomerId(customer.getId())
                .toCustomerName(customer.getFirstName())
                .message(String.format("Hi %s, welcome to Himanshu's microservice...",
                        customer.getFirstName()))
                .build());

        if (fraudCheckResponse.isFraudster()) {
            throw new IllegalStateException("Fraudster");
        }
    }
}
