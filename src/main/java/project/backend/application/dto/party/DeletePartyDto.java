package project.backend.application.dto.party;

import project.backend.domain.model.party.PartyStatus;

public record DeletePartyDto(
        Long partyId,
        PartyStatus partyStatus
) {

}
