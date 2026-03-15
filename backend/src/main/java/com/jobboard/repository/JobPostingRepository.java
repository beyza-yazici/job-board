package com.jobboard.repository;

import com.jobboard.entity.JobPosting;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface JobPostingRepository extends JpaRepository<JobPosting, Long> {
    List<JobPosting> findByCompanyId(Long companyId);

    @Query("SELECT j FROM JobPosting j WHERE " +
            "(:city IS NULL OR j.city = :city) AND " +
            "(:isRemote IS NULL OR j.isRemote = :isRemote) AND " +
            "(:positionType IS NULL OR j.positionType = :positionType)")
    
    Page<JobPosting> findByFilters(@Param("city") String city,
                            @Param("isRemote") Boolean isRemote,
                            @Param("positionType") JobPosting.PositionType positionType,
                            Pageable pageable);
}