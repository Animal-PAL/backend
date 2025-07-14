package project.backend.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import project.backend.domain.model.participation.PartyParticipation;

import java.util.List;
import java.util.Optional;

public interface ParticipationRepoisotry extends JpaRepository<PartyParticipation, Long> {

    @Query("SELECT p FROM PartyParticipation p WHERE p.user.id = :userId")
    Optional<PartyParticipation> findByUserId(Long userId);

    // userId로 참여한 모든 파티 조회
    @Query("SELECT p FROM PartyParticipation p WHERE p.user.id = :userId")
    List<PartyParticipation> findAllByUserId(Long userId);

    boolean existsByParty_IdAndUser_Id(Long partyId, Long userId);


    @Query("SELECT p FROM PartyParticipation p WHERE p.party.id = :partyId")
    List<PartyParticipation> findAllByPartyId(Long partyId);
}