package com.kennedy.picpay.services;

import com.kennedy.picpay.client.NotificationClient;
import com.kennedy.picpay.entities.Transfer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class NotificationService {

    private static final Logger logger = LoggerFactory.getLogger(NotificationService.class);
    private final NotificationClient notificationClient;

    public NotificationService(NotificationClient notificationClient) {
        this.notificationClient = notificationClient;
    }

    public void setNotification(Transfer transfer) {
        try {
            logger.info("Sending notification...");

            var resp = notificationClient.sendNotification(transfer);

            if(resp.getStatusCode().isError()) {
                logger.error("Error while sending notification, status code is not ok");
            }

        } catch (Exception e) {
            logger.error("Error while sending notification", e);
        }
    }
}
