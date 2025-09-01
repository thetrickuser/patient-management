package org.example.patientservice.service.grpc;

import billing.BillingRequest;
import billing.BillingResponse;
import billing.BillingServiceGrpc;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class BillingServiceGrpcClient {

    private static final Logger log = LoggerFactory.getLogger(BillingServiceGrpcClient.class);

    private final BillingServiceGrpc.BillingServiceBlockingStub stub;

    public BillingServiceGrpcClient(
            @Value("${billing.service.address:localhost}")
            String billingServiceAddress,

            @Value("${billing.service.grpc.port:9001}")
            int billingServiceGrpcPort
    ) {
        log.info("Connecting to billing service grpc server at {}:{}", billingServiceAddress, billingServiceGrpcPort);

        ManagedChannel channel = ManagedChannelBuilder.forAddress(billingServiceAddress, billingServiceGrpcPort)
                .usePlaintext().build();
        stub = BillingServiceGrpc.newBlockingStub(channel);
    }

    public BillingResponse createBillingAccount(String patientId, String name, String email) {
        BillingRequest request = BillingRequest.newBuilder().setName(name).setEmail(email).build();
        BillingResponse response = stub.createBillingAccount(request);

        log.info("Received billing account response via GRPC: {}", response);
        return response;
    }


}
