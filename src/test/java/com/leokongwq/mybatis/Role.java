package com.leokongwq.mybatis;

/**
 * @author : jiexiu
 * @date : 2020-09-07 12:20
 **/
public class Role {
	private Long id;
	private String roleName;
	private String note;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder("Role{");
		sb.append("id=").append(id);
		sb.append(", roleName='").append(roleName).append('\'');
		sb.append(", note='").append(note).append('\'');
		sb.append('}');
		return sb.toString();
	}
}
