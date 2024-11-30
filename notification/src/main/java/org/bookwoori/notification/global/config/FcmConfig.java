package org.bookwoori.notification.global.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.messaging.FirebaseMessaging;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;

@Configuration
public class FcmConfig {

    @Value("${fcm.firebase-create-scoped}")
    private String fireBaseCreateScoped;

    @Value("${fcm.key}")
    private String key;

    @Bean
    public FirebaseMessaging init() throws IOException {
        // 디버깅용 로그 추가
        System.out.println("Initializing FirebaseMessaging with key: " + key);
        System.out.println("Firebase Scopes: " + fireBaseCreateScoped);

        try (InputStream serviceAccount = new ClassPathResource(key).getInputStream()) {
            GoogleCredentials googleCredentials = GoogleCredentials.fromStream(serviceAccount)
                    .createScoped(Arrays.asList(fireBaseCreateScoped));

            FirebaseOptions secondaryAppConfig = FirebaseOptions.builder()
                    .setCredentials(googleCredentials)
                    .build();

            // FirebaseApp 초기화 확인
            if (FirebaseApp.getApps().isEmpty()) {
                FirebaseApp app = FirebaseApp.initializeApp(secondaryAppConfig);
                System.out.println("FirebaseApp initialized: " + app.getName());
            } else {
                System.out.println("FirebaseApp already initialized");
            }

            return FirebaseMessaging.getInstance();
        } catch (IOException e) {
            System.err.println("Error initializing FirebaseMessaging: " + e.getMessage());
            throw e;
        }
    }
}

