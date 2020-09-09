package com.leokongwq.mybatis.mapper;

import com.leokongwq.mybatis.Role;

/**
 * @author : jiexiu
 * @date : 2020-09-07 12:20
 **/
public interface RoleMapper {

	public Role getRole(Long id);
	public Role findRole(String roleName);
	public int deleteRole(Long id);
	public int insertRole(Role role);

	public int updateRole(String note);
}
