package io.incondensable.application.web.controllers;

import io.incondensable.application.business.TokenFacade;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * @author abbas
 */
@RestController
@RequestMapping("/v1/token")
public class TokenController {

    private final TokenFacade tokenFacade;

    public TokenController(TokenFacade tokenFacade) {
        this.tokenFacade = tokenFacade;
    }

    @GetMapping("/{userId}")
    public ResponseEntity<String> getUserToken(@PathVariable String userId) {
        return ResponseEntity.ok(tokenFacade.getToken(userId));
    }

    @GetMapping("/still-valid")
    public ResponseEntity<Boolean> isUserTokenValid(@RequestParam String userId, @RequestParam String token) {
        return ResponseEntity.ok(tokenFacade.isTokenStillValid(userId, token));
    }

}
