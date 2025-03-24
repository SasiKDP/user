package com.dataquadinc.repository;

import com.dataquadinc.model.UserDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

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
        SELECT COUNT(*) 
        FROM bdm_client_prod 
        WHERE on_boarded_by = (SELECT user_name FROM user_details_prod WHERE user_id = :userId)
    """, nativeQuery = true)
    long countClientsByUserId(@Param("userId") String userId);



    // ✅ Fetch client names for a specific BDM (based on userId)
    @Query(value = """
        SELECT client_name 
        FROM bdm_client_prod 
        WHERE on_boarded_by = (SELECT user_name FROM user_details_prod WHERE user_id = :userId)
    """, nativeQuery = true)
    List<String> findClientNamesByUserId(@Param("userId") String userId);

    // ✅ Count ALL submissions across all job IDs and clients (across all users)
    @Query(value = """
        SELECT COUNT(*) 
        FROM candidates_prod c 
        JOIN requirements_model_prod r ON c.job_id = r.job_id
        JOIN bdm_client_prod b ON r.client_name = b.client_name
    """, nativeQuery = true)
    long countAllSubmissionsAcrossAllJobsAndClients();

    // ✅ Count ALL submissions for a specific client across all job IDs
    @Query(value = """
        SELECT COUNT(*) 
        FROM candidates_prod c 
        JOIN requirements_model_prod r ON c.job_id = r.job_id
        JOIN bdm_client_prod b ON r.client_name = b.client_name
        WHERE b.client_name = :clientName
    """, nativeQuery = true)
    long countAllSubmissionsByClientName(@Param("clientName") String clientName);

    // ✅ Count ALL interviews for a client across ALL job IDs
    @Query(value = """
        SELECT COUNT(*) 
        FROM candidates_prod c 
        JOIN requirements_model_prod r ON c.job_id = r.job_id
        LEFT JOIN bdm_client_prod b ON r.client_name = b.client_name
        WHERE (b.client_name = :clientName OR r.client_name = :clientName 
               OR (:clientName IS NULL AND EXISTS (
                    SELECT 1 FROM candidates_prod c2 
                    WHERE c2.job_id = r.job_id
               )))
        AND c.timestamp IS NOT NULL
    """, nativeQuery = true)
    long countAllInterviewsByClientName(@Param("clientName") String clientName);

    // ✅ Count ALL placements for a client across ALL job IDs
    @Query(value = """
    SELECT COUNT(*) 
    FROM candidates_prod c
    JOIN requirements_model_prod r ON c.job_id = r.job_id
    JOIN bdm_client_prod b ON r.client_name = b.client_name
    WHERE b.client_name = :clientName
    AND (
        (JSON_VALID(c.interview_status) AND 
         JSON_SEARCH(c.interview_status, 'one', 'PLACED', NULL, '$[*].status') IS NOT NULL)
        OR UPPER(c.interview_status) = 'PLACED'
    )
""", nativeQuery = true)
    long countAllPlacementsByClientName(@Param("clientName") String clientName);


    // ✅ Count Requirements for a given client
    @Query(value = """
        SELECT COUNT(*) 
        FROM requirements_model_prod r
        WHERE r.client_name = :clientName
    """, nativeQuery = true)
    long countRequirementsByClientName(@Param("clientName") String clientName);
}
