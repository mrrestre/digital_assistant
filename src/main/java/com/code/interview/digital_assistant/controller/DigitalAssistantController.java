package com.code.interview.digital_assistant.controller;

import com.code.interview.digital_assistant.model.DigitalAssistant;
import com.code.interview.digital_assistant.repository.DigitalAssistantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Locale;

@RestController
@RequestMapping("/api/digital_assistants")
public class DigitalAssistantController {

    @Autowired
    private DigitalAssistantRepository digitalAssistantRepository;

    @GetMapping
    public ResponseEntity<List<DigitalAssistant>> getAllAssistants() {
        List<DigitalAssistant> assistants = digitalAssistantRepository.findAll();
        return ResponseEntity.ok(assistants);
    }

    @GetMapping(params = "name")
    public ResponseEntity<DigitalAssistant> getByName(@RequestParam("name") String assistantName) {
        DigitalAssistant digitalAssistant = digitalAssistantRepository.findByAssistantName(assistantName.toLowerCase(Locale.ROOT));

        if (digitalAssistant != null) {
            return ResponseEntity.ok(digitalAssistant);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping(value="/message", params = "name")
    public ResponseEntity<String > getMessageByName(@RequestParam("name") String assistantName) {
        DigitalAssistant digitalAssistant = digitalAssistantRepository.findByAssistantName(assistantName.toLowerCase());

        if (digitalAssistant != null) {
            return ResponseEntity.ok(digitalAssistant.getAssistantMessage());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public ResponseEntity<DigitalAssistant> create(@RequestBody DigitalAssistant digitalAssistant) {
        if (digitalAssistantRepository.findByAssistantName(digitalAssistant.getAssistantName()) != null) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
        digitalAssistant.setAssistantName(digitalAssistant.getAssistantName().toLowerCase());
        return ResponseEntity.status(HttpStatus.CREATED).body(digitalAssistantRepository.save(digitalAssistant));
    }

}
