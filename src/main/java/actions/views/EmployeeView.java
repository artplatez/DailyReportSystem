package actions.views;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 종업원 정보에관해 화면의 입력값, 출력값을 조정하는 View모델
 *
 */

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class EmployeeView {

	private Integer id;

	private String code;

	private String name;

	private String password;

	private Integer adminFlag;

	private LocalDateTime createdAt;

	private LocalDateTime updatedAt;

	private Integer deleteFlag;
}
