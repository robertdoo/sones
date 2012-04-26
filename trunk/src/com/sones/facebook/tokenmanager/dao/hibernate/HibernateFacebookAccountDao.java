package com.sones.facebook.tokenmanager.dao.hibernate;

import org.apache.log4j.Logger;

import com.sones.dao.hibernate.HibernateGenericDao;
import com.sones.facebook.tokenmanager.model.FacebookAccount;
import com.sones.facebook.tokenmanager.dao.IFacebookAccountDao;

/**
 * ...
 * @see IFacebookAccountDao
 * @author sartios.sones@gmail.com.
 *
 */
public class HibernateFacebookAccountDao	extends	HibernateGenericDao< FacebookAccount, String >	implements	IFacebookAccountDao
{
	/**
	 * The class logger.
	 */
	private	final	Logger	_LOGGER;
	
	/**
	 * Initializes the object.
	 */
	public	HibernateFacebookAccountDao()
	{
		super( FacebookAccount.class );
		_LOGGER	=	Logger.getLogger( HibernateFacebookAccountDao.class );
	}
}
