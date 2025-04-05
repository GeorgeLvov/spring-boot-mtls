package com.glvov.mtlsserver.functional;

import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Log4j2
public class MtlsServerController {

    @GetMapping("/ping-server")
    @ResponseStatus(HttpStatus.OK)
    public String ping() {
        log.info("Ping request received");
        return "Server is up and running!";
    }
}
