package com.infy.tele.web.rest;

import com.infy.tele.Couchsvc11App;
import com.infy.tele.domain.AcctDetl;
import com.infy.tele.repository.AcctDetlRepository;
import com.infy.tele.service.AcctDetlService;
import com.infy.tele.web.rest.errors.ExceptionTranslator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.validation.Validator;


import java.util.List;
    import static com.infy.tele.web.rest.TestUtil.mockAuthentication;

import static com.infy.tele.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@Link AcctDetlResource} REST controller.
 */
@EmbeddedKafka
@SpringBootTest(classes = Couchsvc11App.class)
public class AcctDetlResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_ADDR = "AAAAAAAAAA";
    private static final String UPDATED_ADDR = "BBBBBBBBBB";

    private static final Integer DEFAULT_CONTACT_NUMBER = 1;
    private static final Integer UPDATED_CONTACT_NUMBER = 2;

    @Autowired
    private AcctDetlRepository acctDetlRepository;

    @Autowired
    private AcctDetlService acctDetlService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private Validator validator;

    private MockMvc restAcctDetlMockMvc;

    private AcctDetl acctDetl;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final AcctDetlResource acctDetlResource = new AcctDetlResource(acctDetlService);
        this.restAcctDetlMockMvc = MockMvcBuilders.standaloneSetup(acctDetlResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter)
            .setValidator(validator).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AcctDetl createEntity() {
        AcctDetl acctDetl = new AcctDetl()
            .name(DEFAULT_NAME)
            .addr(DEFAULT_ADDR)
            .contactNumber(DEFAULT_CONTACT_NUMBER);
        return acctDetl;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AcctDetl createUpdatedEntity() {
        AcctDetl acctDetl = new AcctDetl()
            .name(UPDATED_NAME)
            .addr(UPDATED_ADDR)
            .contactNumber(UPDATED_CONTACT_NUMBER);
        return acctDetl;
    }

    @BeforeEach
    public void initTest() {
        mockAuthentication();
        acctDetlRepository.deleteAll();
        acctDetl = createEntity();
    }

    @Test
    public void createAcctDetl() throws Exception {
        int databaseSizeBeforeCreate = acctDetlRepository.findAll().size();

        // Create the AcctDetl
        restAcctDetlMockMvc.perform(post("/api/acct-detls")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(acctDetl)))
            .andExpect(status().isCreated());

        // Validate the AcctDetl in the database
        List<AcctDetl> acctDetlList = acctDetlRepository.findAll();
        assertThat(acctDetlList).hasSize(databaseSizeBeforeCreate + 1);
        AcctDetl testAcctDetl = acctDetlList.get(acctDetlList.size() - 1);
        assertThat(testAcctDetl.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testAcctDetl.getAddr()).isEqualTo(DEFAULT_ADDR);
        assertThat(testAcctDetl.getContactNumber()).isEqualTo(DEFAULT_CONTACT_NUMBER);
    }

    @Test
    public void createAcctDetlWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = acctDetlRepository.findAll().size();

        // Create the AcctDetl with an existing ID
        acctDetl.setId("existing_id");

        // An entity with an existing ID cannot be created, so this API call must fail
        restAcctDetlMockMvc.perform(post("/api/acct-detls")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(acctDetl)))
            .andExpect(status().isBadRequest());

        // Validate the AcctDetl in the database
        List<AcctDetl> acctDetlList = acctDetlRepository.findAll();
        assertThat(acctDetlList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    public void getAllAcctDetls() throws Exception {
        // Initialize the database
        acctDetlRepository.save(acctDetl);

        // Get all the acctDetlList
        restAcctDetlMockMvc.perform(get("/api/acct-detls?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(acctDetl.getId())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].addr").value(hasItem(DEFAULT_ADDR.toString())))
            .andExpect(jsonPath("$.[*].contactNumber").value(hasItem(DEFAULT_CONTACT_NUMBER)));
    }
    
    @Test
    public void getAcctDetl() throws Exception {
        // Initialize the database
        acctDetlRepository.save(acctDetl);

        // Get the acctDetl
        restAcctDetlMockMvc.perform(get("/api/acct-detls/{id}", acctDetl.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(acctDetl.getId()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.addr").value(DEFAULT_ADDR.toString()))
            .andExpect(jsonPath("$.contactNumber").value(DEFAULT_CONTACT_NUMBER));
    }

    @Test
    public void getNonExistingAcctDetl() throws Exception {
        // Get the acctDetl
        restAcctDetlMockMvc.perform(get("/api/acct-detls/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    public void updateAcctDetl() throws Exception {
        // Initialize the database
        acctDetlService.save(acctDetl);

        int databaseSizeBeforeUpdate = acctDetlRepository.findAll().size();

        // Update the acctDetl
        AcctDetl updatedAcctDetl = acctDetlRepository.findById(acctDetl.getId()).get();
        updatedAcctDetl
            .name(UPDATED_NAME)
            .addr(UPDATED_ADDR)
            .contactNumber(UPDATED_CONTACT_NUMBER);

        restAcctDetlMockMvc.perform(put("/api/acct-detls")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedAcctDetl)))
            .andExpect(status().isOk());

        // Validate the AcctDetl in the database
        List<AcctDetl> acctDetlList = acctDetlRepository.findAll();
        assertThat(acctDetlList).hasSize(databaseSizeBeforeUpdate);
        AcctDetl testAcctDetl = acctDetlList.get(acctDetlList.size() - 1);
        assertThat(testAcctDetl.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testAcctDetl.getAddr()).isEqualTo(UPDATED_ADDR);
        assertThat(testAcctDetl.getContactNumber()).isEqualTo(UPDATED_CONTACT_NUMBER);
    }

    @Test
    public void updateNonExistingAcctDetl() throws Exception {
        int databaseSizeBeforeUpdate = acctDetlRepository.findAll().size();

        // Create the AcctDetl

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAcctDetlMockMvc.perform(put("/api/acct-detls")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(acctDetl)))
            .andExpect(status().isBadRequest());

        // Validate the AcctDetl in the database
        List<AcctDetl> acctDetlList = acctDetlRepository.findAll();
        assertThat(acctDetlList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    public void deleteAcctDetl() throws Exception {
        // Initialize the database
        acctDetlService.save(acctDetl);

        int databaseSizeBeforeDelete = acctDetlRepository.findAll().size();

        // Delete the acctDetl
        restAcctDetlMockMvc.perform(delete("/api/acct-detls/{id}", acctDetl.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<AcctDetl> acctDetlList = acctDetlRepository.findAll();
        assertThat(acctDetlList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(AcctDetl.class);
        AcctDetl acctDetl1 = new AcctDetl();
        acctDetl1.setId("id1");
        AcctDetl acctDetl2 = new AcctDetl();
        acctDetl2.setId(acctDetl1.getId());
        assertThat(acctDetl1).isEqualTo(acctDetl2);
        acctDetl2.setId("id2");
        assertThat(acctDetl1).isNotEqualTo(acctDetl2);
        acctDetl1.setId(null);
        assertThat(acctDetl1).isNotEqualTo(acctDetl2);
    }
}
