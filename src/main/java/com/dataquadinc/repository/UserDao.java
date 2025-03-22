
package com.dataquadinc.repository;

import com.dataquadinc.model.UserDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import  java.util.*;

@Repository
public interface UserDao extends JpaRepository<UserDetails, Integer> {

    UserDetails findByEmail(String email);
    UserDetails findByUserId(String userId);
    UserDetails findByUserName(String userName);
    List<UserDetails> findByRolesId(Long id);

    @Query("SELECT u FROM UserDetails u WHERE u.personalemail = :personalemail")
    UserDetails findByPersonalEmail(@Param("personalemail") String personalemail);

    // ✅ Fetch only BDM employees
    @Query("SELECT u FROM UserDetails u JOIN u.roles r WHERE r.name = 'BDM'")
    List<UserDetails> findBdmEmployees();

    // ✅ Count Clients based on User ID
    @Query(value = """
        SELECT COUNT(*) FROM bdm_client_prod 
        WHERE on_boarded_by = (SELECT user_name FROM user_details_prod WHERE user_id = :userId)
    """, nativeQuery = true)
    long countClientsByUserId(@Param("userId") String userId);


    // ✅ Get Job ID from candidates_prod
    @Query(value = """
    SELECT client_name 
    FROM bdm_client_prod 
    WHERE on_boarded_by = (SELECT user_name FROM user_details_prod WHERE user_id = :userId)
""", nativeQuery = true)
    List<String> findClientNamesByUserId(@Param("userId") String userId);

    // ✅ Get Client Name from requirements_model_prod

    @Query(value = """
    SELECT job_id 
    FROM requirements_model_prod 
    WHERE client_name = :clientName
""", nativeQuery = true)
    List<String> findJobIdsByClientName(@Param("clientName") String clientName);

    // ✅ Count Submissions using Job ID and Client Name
    @Query(value = """
        SELECT COUNT(*) 
        FROM candidates_prod c 
        JOIN requirements_model_prod r ON c.job_id = r.job_id
        JOIN bdm_client_prod b ON r.client_name = b.client_name
        WHERE c.job_id = :jobId 
        AND b.client_name = :clientName
    """, nativeQuery = true)
    long countSubmissionsByJobIdAndClientName(@Param("jobId") String jobId, @Param("clientName") String clientName);

    // ✅ Count Submissions with case-insensitive client name
    @Query(value = """
        SELECT COUNT(*) 
        FROM candidates_prod c 
        JOIN requirements_model_prod r ON c.job_id = r.job_id
        JOIN bdm_client_prod b ON LOWER(r.client_name) = LOWER(b.client_name)
        WHERE c.job_id = :jobId 
        AND LOWER(b.client_name) = LOWER(:clientName)
    """, nativeQuery = true)
    long countSubmissionsByJobIdAndClientNameIgnoreCase(@Param("jobId") String jobId, @Param("clientName") String clientName);

    // ✅ For debugging: Retrieve raw data to verify existence
    @Query(value = """
        SELECT c.candidate_id, c.job_id, r.client_name AS req_client_name, b.client_name AS bdm_client_name 
        FROM candidates_prod c 
        JOIN requirements_model_prod r ON c.job_id = r.job_id
        JOIN bdm_client_prod b ON r.client_name = b.client_name
        WHERE c.job_id = :jobId 
        AND b.client_name = :clientName
    """, nativeQuery = true)
    List<Map<String, Object>> verifyDataExistence(@Param("jobId") String jobId, @Param("clientName") String clientName);

    // ✅ Count Interviews (Candidates mapped to client)
    @Query(value = """
        SELECT COUNT(*) 
        FROM candidates_prod c 
        JOIN requirements_model_prod r ON c.job_id = r.job_id
        WHERE r.client_name = :clientName 
        AND c.timestamp IS NOT NULL
    """, nativeQuery = true)
    long countInterviewsByClientName(@Param("clientName") String clientName);

    // ✅ Count Interviews with case-insensitive client name
    @Query(value = """
        SELECT COUNT(*) 
        FROM candidates_prod c 
        JOIN requirements_model_prod r ON c.job_id = r.job_id
        WHERE LOWER(r.client_name) = LOWER(:clientName)
        AND c.timestamp IS NOT NULL
    """, nativeQuery = true)
    long countInterviewsByClientNameIgnoreCase(@Param("clientName") String clientName);

    // ✅ Count Placements (Candidates with "PLACED" in Interview Status JSON)
    @Query(value = """
        SELECT COUNT(*) 
        FROM candidates_prod c
        JOIN requirements_model_prod r ON c.job_id = r.job_id
        WHERE r.client_name = :clientName
        AND JSON_VALID(c.interview_status) = 1  
        AND JSON_SEARCH(c.interview_status, 'one', 'PLACED', NULL, '$[*].status') IS NOT NULL
    """, nativeQuery = true)
    long countPlacementsByClientName(@Param("clientName") String clientName);

}
