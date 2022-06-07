package models;

import java.time.LocalDate;
import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import constants.JpaConst;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Table(name = JpaConst.TABLE_REP)
@NamedQueries({
	@NamedQuery(
			name= JpaConst.Q_REP_GET_ALL,
			query = JpaConst.Q_REP_GET_ALL_DEF),
	@NamedQuery(
			name = JpaConst.Q_REP_COUNT,
			query = JpaConst.Q_REP_COUNT_DEF),
	@NamedQuery(
			name = JpaConst.Q_REP_GET_ALL_MINE,
			query = JpaConst.Q_REP_GET_ALL_MINE_DEF),
	@NamedQuery(
			name = JpaConst.Q_REP_COUNT_ALL_MINE,
			query = JpaConst.Q_REP_COUNT_ALL_MINE_DEF)
})

@Getter //모든 클래스필드에대해 겟터 자동생성 (Lombok)
@Setter //모든 클래스필드에 대해 셋터 자동생성
@NoArgsConstructor //인수가 없는 컨스트럭터를 자동생성
@AllArgsConstructor //모든 클래스필드를 인수를 가지는 컨스트럭터를 자동생성
@Entity
public class Report {
	/**
	 * id
	 */

	@Id
	@Column(name = JpaConst.REP_COL_ID)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	//日報を登録した従業員
	@ManyToOne
	@JoinColumn(name = JpaConst.REP_COL_EMP, nullable = false)
	private Employee employee; //로그인한 스태프의 정보를 가져옴

	//언제 작성된 일지인지 (날짜만 표시)
	@Column(name = JpaConst.REP_COL_REP_DATE, nullable = false)
	private LocalDate reportDate;

	@Column(name = JpaConst.REP_COL_TITLE, length = 255, nullable = false)
	private String title;

	@Lob //Textbox(한줄)가 아닌 Textarea(영역)지정
	@Column(name = JpaConst.REP_COL_CONTENT, nullable = false)
	private String content;

	//등록일
	@Column(name = JpaConst.REP_COL_CREATED_AT, nullable = false)
	private LocalDateTime createdAt;

	@Column(name = JpaConst.REP_COL_UPDATED_AT, nullable = false)
	private LocalDateTime updatedAt;

	/**
	 * いいね数
	 */
	@Column(name = JpaConst.REP_COL_LIKE_COUNT, nullable = false)
	private Integer likeCount;


}
