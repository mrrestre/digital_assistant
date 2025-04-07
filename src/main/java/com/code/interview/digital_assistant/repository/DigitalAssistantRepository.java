package com.code.interview.digital_assistant.repository;

import com.code.interview.digital_assistant.model.DigitalAssistant;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DigitalAssistantRepository extends JpaRepository<DigitalAssistant, Long> {
    // Find a digital assistant by the name
    DigitalAssistant findByAssistantName(String assistantName);
}
