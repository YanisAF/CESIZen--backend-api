package com.example.CESIZen.controller.quiz;

import com.example.CESIZen.dto.result.ResultMessageDtoRequest;
import com.example.CESIZen.dto.result.ResultMessageDtoResponse;
import com.example.CESIZen.service.quiz.ResultMessageConfigService;
import com.example.CESIZen.utils.Routes;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/admin")
public class ResultMessageConfigController {

    private final ResultMessageConfigService service;

    public ResultMessageConfigController(ResultMessageConfigService service) {
        this.service = service;
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping(Routes.RESULT_MESSAGE_CREATE)
    public ResponseEntity<ResultMessageDtoResponse> createConfig(
            @RequestBody ResultMessageDtoRequest dto) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(service.createConfig(dto));
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping(Routes.RESULT_MESSAGE_UPDATE)
    public ResponseEntity<ResultMessageDtoResponse> updateConfig(
            @RequestParam Long id,
            @RequestBody ResultMessageDtoRequest dto) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(service.updateConfig(id, dto));
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping(Routes.RESULT_MESSAGE_GET_ALL)
    public ResponseEntity<List<ResultMessageDtoResponse>> getAllConfigs() {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(service.getAllConfigs());
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping(Routes.RESULT_MESSAGE_DELETE)
    public ResponseEntity<Void> deleteConfig(@RequestParam Long id) {
        service.deleteConfig(id);
        return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .build();
    }
}
