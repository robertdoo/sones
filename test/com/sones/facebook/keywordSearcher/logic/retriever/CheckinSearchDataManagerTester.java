package com.sones.facebook.keywordSearcher.logic.retriever;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Set;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.sones.facebook.dao.feed.ICheckinDao;
import com.sones.facebook.dao.hibernate.feed.HibernateCheckinDao;
import com.sones.facebook.dao.hibernate.source.HibernateUserDao;
import com.sones.facebook.dao.source.IUserDao;
import com.sones.facebook.downloader.dao.IFacebookDownloadDao;
import com.sones.facebook.downloader.dao.IFacebookPostDownloadDao;
import com.sones.facebook.downloader.dao.hibernate.HibernateFacebookDownloadDao;
import com.sones.facebook.downloader.dao.hibernate.HibernateFacebookPostDownloadDao;
import com.sones.facebook.downloader.model.FacebookDownload;
import com.sones.facebook.downloader.model.FacebookPostDownload;
import com.sones.facebook.downloader.model.FacebookPostDownloadId;
import com.sones.facebook.keywordSearcher.dao.IKeywordSearchDao;
import com.sones.facebook.keywordSearcher.dao.hibernate.HibernateKeywordSearchDao;
import com.sones.facebook.keywordSearcher.model.KeywordSearch;
import com.sones.facebook.model.feed.Checkin;
import com.sones.facebook.model.feed.FacebookPost;
import com.sones.facebook.model.source.User;
import com.sones.sharedDto.facebook.keywordSearcher.feeds.ISearchableFacebookFeed;
import com.sones.usermanager.dao.hibernate.HibernateApplicationUserDao;
import com.sones.usermanager.dao.IApplicationUserDao;
import com.sones.usermanager.model.ApplicationUser;

public class CheckinSearchDataManagerTester 
{
	private	ICheckinSearchDataManager	dataManager;
	private	ApplicationContext	context;
	
	public	CheckinSearchDataManagerTester()
	{
		context	=	new	ClassPathXmlApplicationContext("KeywordSearcher/spring-keywordSearcher-retriever-logic-nu.xml");
	}
	
	@Before
	public	void	setUp()
	{
		dataManager	=	(CheckinSearchDataManager)context.getBean("checkinDataManager");
	}
	
	@After
	public	void	tearDown()
	{
		dataManager	=	null;
	}
	
	@Test
	public	void	testGetCheckinForSearchWhenUserHasSearchAgainForKeywords()
	{
		int	loopIterations	=	5;
		
		IApplicationUserDao	appUserDao	=	( HibernateApplicationUserDao )context.getBean("appUserDao");
		ApplicationUser	appUser	=	new	ApplicationUser();
		appUser.setId( "1" );
		appUserDao.Save( appUser );
		
		List<FacebookDownload>	downloads	=	new	ArrayList<FacebookDownload>();
		IFacebookDownloadDao	downloadDao	=	( HibernateFacebookDownloadDao )context.getBean( "downloadDao" );
		for( int downloadIndex = 1; downloadIndex < loopIterations; downloadIndex++ )
		{
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			Date	date	=	Calendar.getInstance().getTime();
			FacebookDownload	download	=	new	FacebookDownload( appUser, date );
			download.setId( String.valueOf( downloadIndex ) );
			downloadDao.Save( download );
			downloads.add( download );
		}
		
		User	user	=	new	User();
		user.setId( "1" );
		IUserDao	userDao	=	(HibernateUserDao)context.getBean( "userDao" );
		userDao.Save( user );
		
		List<FacebookPost>	posts	=	new	ArrayList<FacebookPost>();
		ICheckinDao	postDao	=	( HibernateCheckinDao )context.getBean("checkinDao");
		for( int postIndex = 1; postIndex < loopIterations; postIndex++ )
		{
			Checkin	checkin	=	new	Checkin();
			checkin.setId( String.valueOf( postIndex ) );
			checkin.setUser( user );
			checkin.setMessage("Message " + postIndex );
			postDao.Save( checkin );
			posts.add( checkin );
		}
		
		List<FacebookPostDownload> postDownloads	=	new	ArrayList<FacebookPostDownload>();
		IFacebookPostDownloadDao	facebookPostDownload	=	( HibernateFacebookPostDownloadDao )context.getBean( "facebookPostDownloadDao" );
		for( FacebookDownload download : downloads )
		{
			for( FacebookPost post : posts )
			{
				FacebookPostDownloadId	id	=	new	FacebookPostDownloadId( download, post );
				FacebookPostDownload	postDownload	=	new	FacebookPostDownload();
				postDownload.setId( id );
				facebookPostDownload.Save( postDownload );
				postDownloads.add( postDownload );
			}
		}
		
		KeywordSearch	keywordSearch	=	new	KeywordSearch();
		IKeywordSearchDao	keywordSearchDao	=	( HibernateKeywordSearchDao )context.getBean( "keywordSearchDao" );
		keywordSearch.setId( "1" );
		keywordSearch.setUser( appUser );
		keywordSearch.setDate( downloads.get( 1 ).getDate() );
		keywordSearchDao.Save( keywordSearch );
		
		Set<ISearchableFacebookFeed> results	=	(Set<ISearchableFacebookFeed>) dataManager.getCheckinForSearch( appUser, keywordSearch.getDate() );
		assertEquals( 12 , results.size() );
		
		keywordSearchDao.Delete( keywordSearch );
		
		for( FacebookPostDownload postDownload : postDownloads )
		{
			facebookPostDownload.Delete( postDownload );
		}
		
		for( FacebookPost post : posts )
		{
			postDao.Delete( (Checkin) post );
		}
		
		userDao.Delete( user );
		
		for( FacebookDownload download : downloads )
		{
			downloadDao.Delete( download );
		}
		
		appUserDao.Delete( appUser );
	}
}
