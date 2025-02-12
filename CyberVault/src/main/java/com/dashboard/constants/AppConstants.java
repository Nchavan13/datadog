package com.dashboard.constants;

public class AppConstants {
    public static final int PASSWORD_EXPIRY_DAYS = 90;
    public static final String AUTH_HEADER = "Authorization";
    public static final String BEARER_PREFIX = "Bearer ";

    // ✅ Using a public mock API instead of a local mock API
    public static final String CYBERARK_API_URL = "https://run.mocky.io/v3/bfcf6737-9ce5-48b6-ade5-a698a43beed4";
    public static final String CYBERARK_AUTH_URL = "https://run.mocky.io/v3/01febced-6601-48c5-95a5-38858c0aef43";

    public static final String DATADOG_API_URL = "https://api.datadoghq.com/api/v1/logs";
}
