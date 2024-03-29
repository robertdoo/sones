package com.sones.facebook.tokenmanager.dao;

import com.sones.dao.IGenericDao;
import com.sones.facebook.tokenmanager.model.FacebookAccount;
import com.sones.facebook.tokenmanager.model.FacebookToken;
import com.sones.usermanager.model.ApplicationUser;

/**
 * Provides methods for accessing {@link FacebookToken} model.
 * 
 * @author sartios.sones@gmail.com.
 *
 */
public interface IFacebookTokenDao	extends	IGenericDao< FacebookToken, String >
{
	/**
	 * Return the access token of the first facebook user account.
	 * @param appUser
	 * @return access token.
	 */
	public FacebookToken GetByApplicationUser( ApplicationUser appUser );
	
	public FacebookToken GetByAccount( FacebookAccount account );

}
