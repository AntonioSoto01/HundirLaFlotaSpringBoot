package com.antonio.hundirlaflota.Controladores;

import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.user.SimpSubscription;
import org.springframework.messaging.simp.user.SimpUserRegistry;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class WebsocketController {
    private final SimpUserRegistry simpUserRegistry;

    @GetMapping("/subcscriptions/{id}")
    public List<String> getSubscriptions(@RequestParam int id) {
        return simpUserRegistry.findSubscriptions(sub -> sub.getSession().getId().equals(Integer.toString(id)))
                .stream()
                .map(SimpSubscription::getDestination)
                .collect(Collectors.toList());
    }
}
