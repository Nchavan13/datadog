package com.dashboard.controller;

import com.dashboard.model.ServiceAccount;
import com.dashboard.repository.ServiceAccountRepository;
import com.dashboard.service.CyberArkService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/accounts")
public class ServiceAccountController {

    private static final Logger log = LoggerFactory.getLogger(ServiceAccountController.class);

    private final ServiceAccountRepository repository;
    private final CyberArkService cyberArkService;

    public ServiceAccountController(ServiceAccountRepository repository, CyberArkService cyberArkService) {
        this.repository = repository;
        this.cyberArkService = cyberArkService;
    }

    @GetMapping
    public List<ServiceAccount> getAllAccounts() {
        log.info("📢 Fetching all service accounts from database...");
        List<ServiceAccount> accounts = repository.findAll();
        log.info("✅ Retrieved {} accounts", accounts.size());
        return accounts;
    }

    @GetMapping("/fetch")
    public String fetchDataFromCyberArk() {
        log.info("📢 Fetching data from CyberArk API...");
        cyberArkService.fetchCyberArkData();
        log.info("✅ CyberArk data fetched and stored.");
        return "✅ Data fetched from CyberArk!";
    }
}
