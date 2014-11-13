package com.ctrip.di.dao.user;

import org.springframework.stereotype.Repository;

/**
 * Authenticat user Mapper
 * @author xgliao
 *
 */
@Repository
public interface AuthUserMapper {

	public AuthUser getAuthUser(String username);

}
