package com.infy.tele.web.rest;

import com.infy.tele.Couchsvc11App;
import com.infy.tele.domain.Acct;
import com.infy.tele.repository.AcctRepository;
import com.infy.tele.service.AcctService;
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
 * Integration tests for the {@Link AcctResource} REST controller.
 */
@EmbeddedKafka
@SpringBootTest(classes = Couchsvc11App.class)
public class AcctResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_ADDR = "AAAAAAAAAA";
    private static final String UPDATED_ADDR = "BBBBBBBBBB";

    private static final Integer DEFAULT_CONTACT_NUMBER = 1;
    private static final Integer UPDATED_CONTACT_NUMBER = 2;

    @Autowired
    private AcctRepository acctRepository;

    @Autowired
    private AcctService acctService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private Validator validator;

    private MockMvc restAcctMockMvc;

    private Acct acct;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final AcctResource acctResource = new AcctResource(acctService);
        this.restAcctMockMvc = MockMvcBuilders.standaloneSetup(acctResource)
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
    public static Acct createEntity() {
        Acct acct = new Acct()
            .name(DEFAULT_NAME)
            .addr(DEFAULT_ADDR)
            .contactNumber(DEFAULT_CONTACT_NUMBER);
        return acct;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Acct createUpdatedEntity() {
        Acct acct = new Acct()
            .name(UPDATED_NAME)
            .addr(UPDATED_ADDR)
            .contactNumber(UPDATED_CONTACT_NUMBER);
        return acct;
    }

    @BeforeEach
    public void initTest() {
        mockAuthentication();
        acctRepository.deleteAll();
        acct = createEntity();
    }

    @Test
    public void createAcct() throws Exception {
        int databaseSizeBeforeCreate = acctRepository.findAll().size();

        // Create the Acct
        restAcctMockMvc.perform(post("/api/accts")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(acct)))
            .andExpect(status().isCreated());

        // Validate the Acct in the database
        List<Acct> acctList = acctRepository.findAll();
        assertThat(acctList).hasSize(databaseSizeBeforeCreate + 1);
        Acct testAcct = acctList.get(acctList.size() - 1);
        assertThat(testAcct.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testAcct.getAddr()).isEqualTo(DEFAULT_ADDR);
        assertThat(testAcct.getContactNumber()).isEqualTo(DEFAULT_CONTACT_NUMBER);
    }

    @Test
    public void createAcctWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = acctRepository.findAll().size();

        // Create the Acct with an existing ID
        acct.setId("existing_id");

        // An entity with an existing ID cannot be created, so this API call must fail
        restAcctMockMvc.perform(post("/api/accts")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(acct)))
            .andExpect(status().isBadRequest());

        // Validate the Acct in the database
        List<Acct> acctList = acctRepository.findAll();
        assertThat(acctList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    public void getAllAccts() throws Exception {
        // Initialize the database
        acctRepository.save(acct);

        // Get all the acctList
        restAcctMockMvc.perform(get("/api/accts?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(acct.getId())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].addr").value(hasItem(DEFAULT_ADDR.toString())))
            .andExpect(jsonPath("$.[*].contactNumber").value(hasItem(DEFAULT_CONTACT_NUMBER)));
    }
    
    @Test
    public void getAcct() throws Exception {
        // Initialize the database
        acctRepository.save(acct);

        // Get the acct
        restAcctMockMvc.perform(get("/api/accts/{id}", acct.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(acct.getId()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.addr").value(DEFAULT_ADDR.toString()))
            .andExpect(jsonPath("$.contactNumber").value(DEFAULT_CONTACT_NUMBER));
    }

    @Test
    public void getNonExistingAcct() throws Exception {
        // Get the acct
        restAcctMockMvc.perform(get("/api/accts/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    public void updateAcct() throws Exception {
        // Initialize the database
        acctService.save(acct);

        int databaseSizeBeforeUpdate = acctRepository.findAll().size();

        // Update the acct
        Acct updatedAcct = acctRepository.findById(acct.getId()).get();
        updatedAcct
            .name(UPDATED_NAME)
            .addr(UPDATED_ADDR)
            .contactNumber(UPDATED_CONTACT_NUMBER);

        restAcctMockMvc.perform(put("/api/accts")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedAcct)))
            .andExpect(status().isOk());

        // Validate the Acct in the database
        List<Acct> acctList = acctRepository.findAll();
        assertThat(acctList).hasSize(databaseSizeBeforeUpdate);
        Acct testAcct = acctList.get(acctList.size() - 1);
        assertThat(testAcct.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testAcct.getAddr()).isEqualTo(UPDATED_ADDR);
        assertThat(testAcct.getContactNumber()).isEqualTo(UPDATED_CONTACT_NUMBER);
    }

    @Test
    public void updateNonExistingAcct() throws Exception {
        int databaseSizeBeforeUpdate = acctRepository.findAll().size();

        // Create the Acct

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAcctMockMvc.perform(put("/api/accts")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(acct)))
            .andExpect(status().isBadRequest());

        // Validate the Acct in the database
        List<Acct> acctList = acctRepository.findAll();
        assertThat(acctList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    public void deleteAcct() throws Exception {
        // Initialize the database
        acctService.save(acct);

        int databaseSizeBeforeDelete = acctRepository.findAll().size();

        // Delete the acct
        restAcctMockMvc.perform(delete("/api/accts/{id}", acct.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Acct> acctList = acctRepository.findAll();
        assertThat(acctList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Acct.class);
        Acct acct1 = new Acct();
        acct1.setId("id1");
        Acct acct2 = new Acct();
        acct2.setId(acct1.getId());
        assertThat(acct1).isEqualTo(acct2);
        acct2.setId("id2");
        assertThat(acct1).isNotEqualTo(acct2);
        acct1.setId(null);
        assertThat(acct1).isNotEqualTo(acct2);
    }
}
