package models;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import constants.JpaConst;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Table(name = JpaConst.TABLE_LIKE)
@NamedQueries({
    @NamedQuery(
            name = JpaConst.Q_LIKE_GET_ALL,
            query = JpaConst.Q_LIKE_GET_ALL_DEF),
    @NamedQuery(
            name = JpaConst.Q_LIKE_COUNT,
            query = JpaConst.Q_LIKE_COUNT_DEF)
})
   /** @NamedQuery(
    		name = JpaConst.Q_LIKE_GET_ALL_MINE,
    		query = JpaConst.Q_LIKE_GET_ALL_MINE_DEF)**/

@Getter //모든 클래스필드에대해 겟터 자동생성 (Lombok)
@Setter //모든 클래스필드에 대해 셋터 자동생성
@NoArgsConstructor //인수가 없는 컨스트럭터를 자동생성
@AllArgsConstructor //모든 클래스필드를 인수를 가지는 컨스트럭터를 자동생성
@Entity
public class Like {
	/**
	 * id
	 */

	@Id
	@Column(name = JpaConst.LIKE_COL_ID)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@ManyToOne
	@JoinColumn(name = "report_id", nullable = false)
	private Report report; //レポートid

	//日報をいいねした従業員
	@ManyToOne
	@JoinColumn(name = "employee_id", nullable = false)
	private Employee employee; //employeeid



	//등록일
	@Column(name = JpaConst.LIKE_COL_CREATED_AT, nullable = false)
	private LocalDateTime createdAt;

	@Column(name = JpaConst.LIKE_COL_UPDATED_AT, nullable = false)
	private LocalDateTime updatedAt;

}
