/*
 * Copyright (c) 2025 Antonio Musarra's Blog.
 * SPDX-License-Identifier: MIT
 */
package it.dontesta.labs.quarkus.graphql.info.contributor;

import io.quarkus.info.runtime.spi.InfoContributor;
import jakarta.enterprise.context.ApplicationScoped;
import java.util.HashMap;
import java.util.Map;

@ApplicationScoped
public class CustomInfoContributor implements InfoContributor {

    private static final String MINIO_CONNECTION_STATUS = "UP";

    @Override
    public String name() {
        return "app-detail-info";
    }

    @Override
    public Map<String, Object> data() {
        Map<String, Object> customData = new HashMap<>();

        // Sezione Feature Flags
        Map<String, Boolean> featureFlags = new HashMap<>();
        featureFlags.put("nuova-dashboard-abilitata", isNuovaDashboardAbilitata());
        customData.put("feature-flags", featureFlags);

        // Sezione MinIO Connection Status
        Map<String, Object> minioInfo = new HashMap<>();
        minioInfo.put("status", getMinIOConnectionStatus());
        customData.put("minio", minioInfo);

        return customData;
    }

    // Metodo di esempio per simulare lo stato del feature flag (invariato)
    private boolean isNuovaDashboardAbilitata() {
        // Sostituisci con la logica reale
        return Boolean.TRUE;
    }

    // Metodo di esempio per simulare lo stato della connessione MinIO 
    // (invariato, ma da implementare health check reale)
    private String getMinIOConnectionStatus() {
        // Sostituisci con la logica di health check MinIO reale
        return MINIO_CONNECTION_STATUS;
    }
}