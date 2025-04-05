package com.glvov.mtlsclient.functional;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Log4j2
@RequiredArgsConstructor
public class MtlsClientController {

    private final MtlsServerCaller serverCaller;


    @GetMapping("/mtls-client")
    @ResponseStatus(HttpStatus.OK)
    public String client() {
        return serverCaller.call();
    }
}
