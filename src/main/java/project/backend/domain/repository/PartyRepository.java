package project.backend.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import project.backend.domain.model.party.Party;
import project.backend.domain.model.party.PartyStatus;

import java.util.Optional;

@Repository
public interface PartyRepository extends JpaRepository<Party, Long> {

    @Query("SELECT p FROM Party p WHERE p.host_user_id.id = :id AND p.id = :partyId")
    Optional<Party> findByHostUserIdAndId(Long id, Long partyId);

    @Modifying(clearAutomatically = true)
    @Query("UPDATE Party p SET p.partyStatus = :status WHERE p.id = :partyId")
    void updatePartyStatus(@Param("partyId") Long partyId, @Param("status") PartyStatus status);


}
