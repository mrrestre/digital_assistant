package com.code.interview.digital_assistant.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.code.interview.digital_assistant.model.DigitalAssistant;
import com.code.interview.digital_assistant.repository.DigitalAssistantRepository;
import com.fasterxml.jackson.databind.ObjectMapper;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest
@AutoConfigureMockMvc
public class DigitalAssistantControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private DigitalAssistantRepository digitalAssistantRepository;

    @Autowired
    private ObjectMapper objectMapper;

    /////// Helper variables ///////
    private final String baseEndpoint = "/api/digital_assistants";
    private final String assistantMessageEndpoint = baseEndpoint + "/message";
    private final String chatEndpoint = baseEndpoint + "/chat";

    private final String paramAssistantName = "assistantName";
    private final String paramUserMessage = "userMessage";

    private final String jsonAssistantBasePath = "$";
    private final String jsonAssistantNamePath = "$.assistantName";
    private final String jsonAssistantMessagePath = "$.assistantMessage";
    private final String jsonAssistantNameListPath = "$[0].assistantName";
    private final String jsonAssistantMessageListPath = "$[0].assistantMessage";

    private final String plainTextUTF8 = "text/plain;charset=UTF-8";

    /////// Test data ///////
    private final String assistantName1 = "TestAssistant1";
    private final String assistantMessage1 = "Hello from TestAssistant1";

    private final String assistantName2 = "TestAssistant2";
    private final String assistantMessage2 = "Hello from TestAssistant2";

    private final String nonExistantAssistantName = "NonExistantsName";

    private final String newAssistantName = "NewAssistant3000";
    private final String newAssistantMessage = "The better and improved assistant 3000";

    private final String userMessage = "Hello Assistant!";

    private final String assistantChatResponse = "User: " + userMessage + ", Assistant Message: " + assistantMessage1;

    @BeforeEach
    void setup() {
        digitalAssistantRepository.deleteAll(); // Clean up before each test
        DigitalAssistant assistant1 = new DigitalAssistant();
        assistant1.setAssistantName(assistantName1);
        assistant1.setAssistantMessage(assistantMessage1);
        digitalAssistantRepository.save(assistant1);

        DigitalAssistant assistant2 = new DigitalAssistant();
        assistant2.setAssistantName(assistantName2);
        assistant2.setAssistantMessage(assistantMessage2);
        digitalAssistantRepository.save(assistant2);
    }

    /////// Get Base Endpoint ///////
    @Test
    void getAllAssistants_shouldReturnListOfAssistants() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(baseEndpoint))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath(jsonAssistantBasePath, hasSize(2)))
                .andExpect(jsonPath(jsonAssistantNameListPath, is(assistantName1.toLowerCase())))
                .andExpect(jsonPath(jsonAssistantMessageListPath, is(assistantMessage1)));
    }

    @Test
    void getByName_existingAssistant_shouldReturnAssistant() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(baseEndpoint)
                .param(paramAssistantName, assistantName1))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath(jsonAssistantNamePath, is(assistantName1.toLowerCase())))
                .andExpect(jsonPath(jsonAssistantMessagePath, is(assistantMessage1)));
    }

    @Test
    void getByName_nonExistingAssistant_shouldReturnNotFound() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(baseEndpoint)
                .param(paramAssistantName, nonExistantAssistantName))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    /////// Get Assistant Message Endpoint ///////
    @Test
    void getMessageByName_existingAssistant_shouldReturnMessage() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(assistantMessageEndpoint)
                .param(paramAssistantName, assistantName2))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(plainTextUTF8))
                .andExpect(MockMvcResultMatchers.content().string(assistantMessage2));
    }

    @Test
    void getMessageByName_nonExistingAssistant_shouldReturnNotFound() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(assistantMessageEndpoint)
                .param(paramAssistantName, nonExistantAssistantName))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    /////// Get Chat Endpoint ///////
    @Test
    void chattingWithNamedAssistant_shouldReturnAssistantMessage() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(chatEndpoint)
                .param(paramAssistantName, assistantName1)
                .param(paramUserMessage, userMessage))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(plainTextUTF8))
                .andExpect(MockMvcResultMatchers.content().string(assistantChatResponse));
    }

    @Test
    void chattingWitNonExistingAssistant_shouldReturnNotFound() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(chatEndpoint)
                .param(paramAssistantName, nonExistantAssistantName)
                .param(paramUserMessage, userMessage))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    /////// Post Base Endpoint ///////
    @Test
    void createAssistant_uniqueName_shouldReturnCreatedAssistant() throws Exception {
        DigitalAssistant newAssistant = new DigitalAssistant();
        newAssistant.setAssistantName(newAssistantName);
        newAssistant.setAssistantMessage(newAssistantMessage);

        mockMvc.perform(MockMvcRequestBuilders.post(baseEndpoint)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(newAssistant)))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath(jsonAssistantNamePath, is(newAssistantName.toLowerCase())))
                .andExpect(jsonPath(jsonAssistantMessagePath, is(newAssistantMessage)));
    }

    @Test
    void createAssistant_duplicateName_shouldReturnConflict() throws Exception {
        DigitalAssistant duplicateAssistant = new DigitalAssistant();
        duplicateAssistant.setAssistantName(assistantName1);
        duplicateAssistant.setAssistantMessage(assistantMessage1);

        mockMvc.perform(MockMvcRequestBuilders.post(baseEndpoint)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(duplicateAssistant)))
                .andExpect(MockMvcResultMatchers.status().isConflict());
    }
}
