package com.dashboard.service;

import com.dashboard.constants.AppConstants;
import com.dashboard.dto.CyberArkAccountDTO;
import com.dashboard.exceptions.CustomException;
import com.dashboard.model.ServiceAccount;
import com.dashboard.repository.ServiceAccountRepository;
import com.dashboard.utility.DateUtil;
import org.springframework.http.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Service
public class CyberArkService {

    private static final Logger log = LoggerFactory.getLogger(CyberArkService.class);

    private final ServiceAccountRepository repository;
    private final RestTemplate restTemplate;

    public CyberArkService(ServiceAccountRepository repository, RestTemplate restTemplate) {
        this.repository = repository;
        this.restTemplate = restTemplate;
    }

    private String getAuthToken() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> request = new HttpEntity<>(headers);

        ResponseEntity<Map> response = restTemplate.postForEntity(AppConstants.CYBERARK_AUTH_URL, request, Map.class);

        if (!response.getStatusCode().is2xxSuccessful() || !response.hasBody()) {
            throw new CustomException("Failed to authenticate with CyberArk");
        }

        return response.getBody().get("token").toString();
    }

    /**
     * Fetches data from CyberArk and stores it in the database.
     */
    @Scheduled(fixedRate = 86400000) // Runs every 24 hours
    @Transactional // Ensures database commits the changes
    public void fetchCyberArkData() {
        log.info("📢 Fetching CyberArk Data...");

        String token = getAuthToken(); // Get auth token

        HttpHeaders headers = new HttpHeaders();
        headers.set(AppConstants.AUTH_HEADER, AppConstants.BEARER_PREFIX + token);
        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<CyberArkAccountDTO[]> response = restTemplate.exchange(
                AppConstants.CYBERARK_API_URL, HttpMethod.GET, entity, CyberArkAccountDTO[].class
        );

        if (!response.getStatusCode().is2xxSuccessful() || response.getBody() == null) {
            log.error("❌ Failed to fetch CyberArk data. Status: {}", response.getStatusCode());
            throw new CustomException("Failed to fetch CyberArk data");
        }

        List<CyberArkAccountDTO> accounts = Arrays.asList(response.getBody());
        log.info("✅ Retrieved {} accounts from CyberArk", accounts.size());

        for (CyberArkAccountDTO account : accounts) {
            LocalDate lastModifiedDate = DateUtil.parseDate(account.getLastModifiedDate());

            ServiceAccount serviceAccount = new ServiceAccount();
            serviceAccount.setAccountName(account.getName());
            serviceAccount.setLastModifiedDate(lastModifiedDate);
            serviceAccount.calculateExpiryDate();

            repository.save(serviceAccount);
            log.info("💾 Saved account: {} | Expiry Date: {}", serviceAccount.getAccountName(), serviceAccount.getExpiryDate());
        }

        log.info("✅ All data successfully stored in the database.");
    }
}
