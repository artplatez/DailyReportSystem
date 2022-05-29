package actions.views;

import java.util.ArrayList;
import java.util.List;

import constants.AttributeConst;
import constants.JpaConst;
import models.Employee;

public class EmployeeConverter {

	/**
	 * View Model의 인스턴스에서 DTO모델의 인스턴스를 작성함
	 * @param ev EmployeeView의 인스턴스
	 * @return Employee의 인스턴스
	 */
	public static Employee toModel(EmployeeView ev) {
		return new Employee(
				ev.getId(),
				ev.getCode(),
				ev.getName(),
				ev.getPassword(),
				ev.getAdminFlag() == null
				? null
						: ev.getAdminFlag() == AttributeConst.ROLE_ADMIN.getIntegerValue()
						? JpaConst.ROLE_ADMIN
						: JpaConst.ROLE_GENERAL,
				ev.getCreatedAt(),
				ev.getUpdatedAt(),
				ev.getDeleteFlag() == null
					? null
							:ev.getDeleteFlag() == AttributeConst.DEL_FLAG_TRUE.getIntegerValue()
							? JpaConst.EMP_DEL_TRUE
							: JpaConst.EMP_DEL_FALSE);
	}

	/**
	 * DTO모델의 인스턴스에서 뷰 모델의 인스턴스를 작성
	 * @param e Employee 의 인스턴스
	 * @return EmployeeView 의 인스턴스
	 */

	public static EmployeeView toView(Employee e) {
		if(e == null) {
			return null;

		}
		return new EmployeeView(
				e.getId(),
				e.getCode(),
				e.getName(),
				e.getPassword(),
				e.getAdminFlag() == null
					? null
							:e.getAdminFlag() == JpaConst.ROLE_ADMIN
							? AttributeConst.ROLE_ADMIN.getIntegerValue()
							: AttributeConst.ROLE_GENERAL.getIntegerValue(),
				e.getCreatedAt(),
				e.getUpdatedAt(),
				e.getDeleteFlag() == null
				? null
						: e.getDeleteFlag() == JpaConst.EMP_DEL_TRUE
						? AttributeConst.DEL_FLAG_TRUE.getIntegerValue()
						: AttributeConst.DEL_FLAG_FALSE.getIntegerValue());

	}

	/**
	 * DTO모델의 리스트에서 뷰모델의 리스트를 작성
	 * @param list DTO모델의 리스트
	 * @return View모델의 리스트
	 */

	public static List<EmployeeView> toViewList(List<Employee> list) {
		List<EmployeeView> evs = new ArrayList<>();

		for (Employee e : list) {
			evs.add(toView(e));
		}
		return evs;
	}
	/**
	 * View모델의 모든필드의 내용을 DTO모델의 필드에 복사하기
	 * @param e DTO모델(붙여넣을곳)
	 * @param ev View모델(복사를 따올곳)
	 */
	public static void copyViewToModel(Employee e, EmployeeView ev) {
		e.setId(ev.getId());
		e.setCode(ev.getCode());
		e.setName(ev.getName());
		e.setPassword(ev.getPassword());
		e.setAdminFlag(ev.getAdminFlag());
		e.setCreatedAt(ev.getCreatedAt());
		e.setUpdatedAt(ev.getUpdatedAt());
		e.setDeleteFlag(ev.getDeleteFlag());

	}




}
