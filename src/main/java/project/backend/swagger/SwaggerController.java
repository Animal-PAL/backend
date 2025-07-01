package project.backend.swagger;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Tag(name = "Example Controller", description = "Controller for example endpoints")
public class SwaggerController {

     @Operation(summary = "Example endpoint", description = "An example endpoint that returns a simple message")
     @ApiResponses(value = {
             @ApiResponse(responseCode = "200", description = "Successful operation"),
             @ApiResponse(responseCode = "400", description = "Bad request"),
             @ApiResponse(responseCode = "500", description = "Internal server error")
     })
     @GetMapping("/test1")
     public String test1() {
         return "고생했어??";
     }

     @Operation(summary = "비밀번호 재설정 성공 응답", description = "성공 응답 예시를 반환합니다")
     @ApiResponses(value = {
             @ApiResponse(responseCode = "200", description = "Successful operation",
                     content = @Content(
                             mediaType = "application/json",
                             schema = @Schema(implementation = SwaggerApiResponse.class)
                     )
             ),
             @ApiResponse(responseCode = "400", description = "Bad request"),
             @ApiResponse(responseCode = "500", description = "Internal server error")
     })
     @GetMapping("/test2")
     public SwaggerApiResponse test2() {
          return new SwaggerApiResponse(true, "비밀번호 재설정 성공");
     }

     @Operation(summary = "예시 요청", description = "클라이언트로부터 JSON 형태의 요청을 받아 처리합니다.")
     @ApiResponses(value = {
             @ApiResponse(responseCode = "200", description = "요청 성공",
                         content = @Content(mediaType = "application/json",
                             schema = @Schema(implementation = SwaggerDto.class))),
             @ApiResponse(responseCode = "400", description = "잘못된 요청"),
             @ApiResponse(responseCode = "500", description = "서버 오류")
     })
     @PostMapping("/test3")
     public SwaggerDto test3(
             @io.swagger.v3.oas.annotations.parameters.RequestBody(
                     description = "요청 JSON",
                     required = true,
                     content = @Content(schema = @Schema(implementation = SwaggerDto.class))
             )
             @org.springframework.web.bind.annotation.RequestBody SwaggerDto requestDto
     ) {
          return new SwaggerDto("이름");
     }
}
