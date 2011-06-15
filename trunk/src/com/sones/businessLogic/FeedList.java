package com.sones.businessLogic;

import java.util.HashSet;
import java.util.Set;

public class FeedList {

	private String userID_;
	private Set<Feed> feeds_ = new HashSet<Feed>();
	
	public FeedList(){
		userID_ = new String();
		feeds_ =  new HashSet<Feed>();
	}
	
	public FeedList(final String userID,final Set<Feed> feeds){
		userID_ = userID;
		feeds_ = feeds;
	}

	public String getUserID() {
		return userID_;
	}

	public void setUserID(String userID) {
		userID_ = userID;
	}

	public Set<Feed> getFeeds_() {
		return feeds_;
	}

	public void setFeeds(Set<Feed> feeds) {
		feeds_ = feeds;
	}
	
	public boolean setFeed(final Feed feed){
		if(null==feed){
			return false;
		}
		return feeds_.add(feed);
	}
	
	public String getFeedID(final int index){
		if(index<feeds_.size()){
			return ((Feed)(feeds_.toArray())[index]).getId_();
		}
		return null;
	}
	
	public String[] getFeedsToArray(){
		return (String[]) feeds_.toArray();
	}
	
	public Feed getFeed(final int index){
		if(index<feeds_.size()){
			return ((Feed)(feeds_.toArray())[index]);
		}
		return null;
	}
	
	public int getSize(){
		return feeds_.size();
	}
	
	
	public boolean contains(final Feed feed){
		if(null!=feeds_){
			return feeds_.contains(feed);
		}
		return false;
	}
	
	public Set<String> getIdFromFeeds(){
		Set<String> ids = new HashSet<String>();
		for(int i=0;i<this.getSize();i++){
			ids.add(this.getFeedID(i));
		}
		return ids;
	}
	
	public boolean deleteFeed(final Feed feedToDelete){
		return feeds_.remove(feedToDelete);
	}
	
	/**
	 * Returns the most recent creation time of feed which is the 1st feed of the set.
	 */
	public String getMostRecentCreationTime(){
		if(null!=feeds_){
			Feed feed = (Feed) (feeds_.toArray())[0];
			return feed.getCreatedTime_();
		}
		return null;
	}
}