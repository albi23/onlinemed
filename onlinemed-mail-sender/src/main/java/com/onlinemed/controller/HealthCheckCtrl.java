package com.onlinemed.controller;

import com.onlinemed.dto.BuildInfoRecord;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/health-check")
public class HealthCheckCtrl {

    private final BuildInfoRecord buildInfo;

    public HealthCheckCtrl(BuildInfoRecord buildInfo) {
        this.buildInfo = buildInfo;
    }

    @GetMapping
    public BuildInfoRecord buildInfo() {
        return buildInfo;
    }
}
