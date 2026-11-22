package tn.gestionstages.backend.repository;

import tn.gestionstages.backend.entity.InternshipOffer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface InternshipOfferRepository
        extends JpaRepository<InternshipOffer, Long> {

    List<InternshipOffer> findByCompanyId(Long companyId);

    List<InternshipOffer> findByCompanyUserId(Long userId);

    List<InternshipOffer> findByPublishedTrue();
}
