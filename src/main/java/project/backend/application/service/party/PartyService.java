package project.backend.application.service.party;

import project.backend.application.dto.party.CreatePartyDto;
import project.backend.application.dto.party.GetPartyResponse;
import project.backend.application.dto.party.UpdatePartyRequest;

import java.util.List;

public interface PartyService {

    // 파티 생성
    void createParty(CreatePartyDto createPartyDto);

    // 파티 조회
    GetPartyResponse getPartyById(Long partyId);

    // 파티 목록 조회
    List<GetPartyResponse> getAllParties(Long userId);

    // 파티 수정
    void updateParty(UpdatePartyRequest updatePartyDto);

    void cancelParty(Long partyId);

}
