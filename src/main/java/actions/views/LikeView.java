package actions.views;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class LikeView {

	private Integer id;

	private ReportView report;

	private EmployeeView employee;

	private LocalDateTime createdAt;

	private LocalDateTime updatedAt;


}
