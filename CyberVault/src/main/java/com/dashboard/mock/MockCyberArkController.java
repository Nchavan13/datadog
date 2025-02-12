package com.dashboard.mock;

import com.dashboard.dto.CyberArkAccountDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/mock")
public class MockCyberArkController {

    @PostMapping("/authenticate")
    public ResponseEntity<String> authenticate(@RequestBody Map<String, String> credentials) {
        return ResponseEntity.ok("mock-auth-token-12345");
    }

    @GetMapping("/accounts")
    public ResponseEntity<List<CyberArkAccountDTO>> getMockAccounts() {
        List<CyberArkAccountDTO> accounts = Arrays.asList(
                new CyberArkAccountDTO("ServiceAccount1", "2024-01-01"),
                new CyberArkAccountDTO("ServiceAccount2", "2024-01-05")
        );
        return ResponseEntity.ok(accounts);
    }
}
