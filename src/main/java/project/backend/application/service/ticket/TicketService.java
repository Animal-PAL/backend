package project.backend.application.service.ticket;

import project.backend.application.dto.ticket.CreateInviteLinkRequest;
import project.backend.application.dto.ticket.CreateInviteLinkResponse;
import project.backend.application.dto.ticket.ReceiveTicketRequest;

/**
 * 티켓 및 초대 링크와 관련된 비즈니스 로직을 정의하는 서비스 인터페이스입니다.
 */
public interface TicketService {

    /**
     * 사용자가 보유한 사용 가능한 티켓을 기반으로 새로운 파티 초대 링크를 생성합니다.
     *
     * @param request 초대 링크를 생성할 파티의 ID를 담은 요청 DTO
     * @return 생성된 초대 링크의 URL을 담은 응답 DTO
     */
    CreateInviteLinkResponse createInviteLink(CreateInviteLinkRequest request);

    /**
     * 제공된 초대 코드를 사용하여 파티에 참여하고, 새로운 티켓을 발급받습니다.
     * 이 과정에서 부모 티켓과 초대 링크는 '사용됨' 상태로 변경됩니다.
     *
     * @param request 사용할 초대 링크의 고유 코드를 담은 요청 DTO
     */
    void receiveTicket(ReceiveTicketRequest request);

}