package project.backend.swagger;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

import java.util.List;

@Schema(description = "요청 바디 DTO")
@Getter
public class SwaggerDto {

    @Schema(description = "이름", example = "홍길동")
    private String name;

    @Schema(description = "설명", example = "[\"강아지\", \"고양이\"]")
    private List<String> description;

    public SwaggerDto(String name, List<String> description) {
        this.name = name;
        this.description = description;
    }

    public SwaggerDto(String name) {
        this.name = name;
    }
}
