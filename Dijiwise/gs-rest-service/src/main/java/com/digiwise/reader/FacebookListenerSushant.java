package com.digiwise.reader;

import hello.DijiWiseResultsArray;
import hello.Greeting;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import com.dijiwise.utils.DijiwiseConstants;
import com.mongodb.MongoClient;
import com.restfb.types.Comment;
import com.restfb.types.NamedFacebookType;
import com.restfb.types.Post;
import com.restfb.types.Post.Likes;

public class FacebookListenerSushant {
	// facebook handler
	final static Logger logger = Logger.getLogger(FacebookListenerSushant.class);

	public static DijiWiseResultsArray newQueueMessage(String parent_id, String child_user_id, String access_token,
								String social_media_type, String social_site_user_id, DijiWiseResultsArray array) {

		System.out.println("start of the facebook webservice call: " + new Date());
		
		//String accessTokenValue = null;
		MongoOperations mongoOps = null;
		//List<Post> data = null;
		//String PERSON_COLLECTION = "";
		//String FB_COMMENT_COLLECTION = "";
		//String PERSON_LOGIN_STATUS = "";
		//String PERSON_FOLLOWING_FOLLOWS = "";
		//Token secretToken = null;
		//com.rabbitmq.client.ConnectionFactory factory1 = null;
		MongoClient mongo = null;
		//JSONObject obj = new JSONObject();
		DateTimeZone zone = DateTimeZone.getDefault();
		//DateTimeFormatter df = DateTimeFormat.forPattern("yyyy-mm-dd hh-mm-ss");
		List<Greeting> arrayGreetings = new ArrayList<Greeting>();
		List<Post> dataFeed = null;
		//Person p1 = null;
		ErrorPerson errorPerson = null;
		ErrorPerson p2 = null;
		try {

			//PERSON_COLLECTION = "dijiwisesocialcollection";
			//FB_COMMENT_COLLECTION = "dijiwiseComment";
			 
			//final String MONGO_HOST = "173.244.67.213";
			//final String MONGO_HOST = DijiwiseConstants.MONGO_HOST;
			//final int MONGO_PORT = Integer.parseInt(DijiwiseConstants.MONGO_PORT);
			
			//PERSON_FOLLOWING_FOLLOWS = "dijiwiseFollowFollowing";
			//PERSON_LOGIN_STATUS = "dijiwiseerrorstatus";
			
			// accessTokenValue = access_token;

			try {
				
				mongo = new MongoClient(DijiwiseConstants.MONGO_HOST, Integer.parseInt(DijiwiseConstants.MONGO_PORT));
				mongoOps = new MongoTemplate(mongo, DijiwiseConstants.DB_NAME);
				
				GraphReaderExample fbObject = new GraphReaderExample(access_token);
				//data = fbObject.FilteredPosts();
				dataFeed = fbObject.FilteredFeedPosts();

			} catch (Exception e) {
				logger.error("Exception: " + e.getMessage() + "--"+ e.getStackTrace() + " e.tostring: " + e.toString());
				errorPerson = new ErrorPerson();
				p2= new ErrorPerson();

				errorPerson.setChildId(child_user_id);
				errorPerson.setParent_id(parent_id);
				errorPerson.setErrorOccuredDate(new Date().toString());
				errorPerson.setErrorMessage(e.getMessage());
				errorPerson.setUuid((UUID.randomUUID() + ""));
				errorPerson.setSocialSiteUserId(social_site_user_id);
				errorPerson.setErrorMessage(e.getMessage());
				errorPerson.setSocialMediaType(social_media_type);
				errorPerson.setErrorFlag("1");	
				mongoOps.save(errorPerson, DijiwiseConstants.PERSON_LOGIN_STATUS);
			}
			try {
				if (errorPerson == null) {
				try {
					logger.info("Thread  Got data  w.r.t accesstoken :"+ access_token + "\n Thread Name:");
					// int countFeed = 0;
					for (Post i : dataFeed) {
						logger.info("###############Start#############" + i.getId());
						logger.info("Thread started reading post with id : "+i.getId() +"_"+child_user_id + "   updatged ptime "+ i.getUpdatedTime().toString());
						logger.info("fb testing...");
						Person p = new Person();
						p.setUuid((UUID.randomUUID() + ""));
						p.setId(i.getId()+"_"+child_user_id+"_"+parent_id);
						p.setName(i.getName());

						p.setInsertDate((new DateTime().toDateTimeISO().toString()));
						p.setMessage(i.getMessage());
						logger.info("fb testing for toLocaleString...");

						p.setCommentCount(0L);
						try {
							// logger.info("fb testing for toLocaleString. i.getComments().getData().."+i.getComments().getData());
							if (i.getComments() != null && i.getComments().getData() != null && i.getComments().getData().size() > 0) {
								String uuidCommentId = UUID.randomUUID() + "";
								long j = 0L;
								for (Comment c : i.getComments().getData()) {
									try {
										FBcomments comment = new FBcomments();
										comment.setUuidComment(uuidCommentId);
										//comment.setPostId(i.getId() +"_"+child_user_id);
										comment.setPostId(p.getId());
										comment.setCommentId((c.getId() != null ? ( c.getId() +"_"+ p.getId()): ""));
										comment.setCommentMessage((c.getMessage() != null ? c.getMessage() : ""));
										comment.setCommentFrom((c.getFrom() != null ? c.getFrom().getName() != null ? c.getFrom().getName() : "" : ""));
										comment.setProfileImage("");
										comment.setCommentedById((c.getFrom().getId() != null ? c.getFrom().getId() : ""));
										comment.setAttachmentType((c.getAttachment() != null ? c.getAttachment().getType() : ""));
										comment.setAttachmentUrl((c.getAttachment() != null ? c.getAttachment().getUrl() : ""));
										comment.setSocialMediaType(social_media_type);
										comment.setChildUserId(child_user_id);
										comment.setParent_id(parent_id);

										FBcomments dbcomment = new FBcomments();
										try {
											dbcomment = mongoOps.findById(comment.getCommentId(), FBcomments.class,  DijiwiseConstants.FB_COMMENT_COLLECTION);
											if (dbcomment != null) {
												comment.setReadFlag(dbcomment.getReadFlag());
												comment.setSaveFlag(dbcomment.getSaveFlag());
												comment.setDeleteFlag(dbcomment.getDeleteFlag());
												comment.setErrorFlag(dbcomment.getErrorFlag());
												// comment.setErrorMessage("");
												comment.setNotificationFlag(dbcomment.getNotificationFlag());
											} else {
												//adding flags to Comments
												comment.setReadFlag(0);
												comment.setSaveFlag(0);
												comment.setDeleteFlag(0);
												comment.setErrorFlag(0);
												comment.setErrorMessage("");
												comment.setNotificationFlag(0);
											}
										} catch (Exception e) {
											logger.info("Twitter flag modification exception.."+ e.toString());
										}
										//end  of adding flags to comment
										mongoOps.save(comment, DijiwiseConstants.FB_COMMENT_COLLECTION);
									} catch (Exception e) {
										logger.info("fb Comments list exception..."+ e.toString());
									}
								}
								Query query5 = new Query();
								query5.addCriteria(Criteria.where("postId").is(p.getId()));
								j=(mongoOps.count(query5, DijiwiseConstants.FB_COMMENT_COLLECTION));
								p.setCommentCount(j);
								p.setFbComments(uuidCommentId);
							}
						} catch (Exception e1) {
							logger.info("fb Comments list exception..."+ e1.toString());
						}
						Date local = i.getCreatedTime();
						long utc = zone.convertLocalToUTC(local.getTime(), false);
						p.setCreatedDate(utc);
						p.setCreatedDateApi(i.getCreatedTime().toString());
						p.setCreatedDateTimestamp(local.getTime());
						p.setPostType((i.getType() != null ? i.getType() : ""));
						p.setName((i.getName() != null ? i.getName() : ""));
/*						try {
							if (i.getType() == "video") {
								i.getObjectId();
							}
						} catch (Exception e2) {

						}
						*/
						p.setFacebookSource((i.getSource() != null ? i.getSource() : ""));
						p.setFacebookActionList((i.getActions() != null ? i.getActions() : null));
						p.setTotalCommentCount(0);
						p.setMyLiked(0);
						try {
							if (i.getLikes() != null && i.getLikes() instanceof Likes && i.getLikes().getData() != null) {
								Long likeCountFromLikes = i.getLikes().getTotalCount();
								List<NamedFacebookType> likes = (i.getLikes().getData() != null ? i.getLikes().getData() : null);
															
								if (likes != null) {
									for (NamedFacebookType e:likes) {
										String personLiked=e.getId();
										personLiked.equalsIgnoreCase(social_site_user_id);
										p.setMyLiked(1);
									}
									long dataCount = likes.size();
									p.setLikeCount(likeCountFromLikes >= dataCount ? likeCountFromLikes: dataCount);

								}
							} else {
								p.setLikeCount(0L);
							}
						} catch (Exception e) {
							logger.error("Like count not exception...." + e.toString());
						}

						p.setPhotoUrl((i.getPicture()) != null ? (i.getPicture()): "");

						p.setLink((i.getLink() != null ? i.getLink() : ""));
						p.setSocialMediaType(social_media_type);
						p.setPostSource(DijiwiseConstants.FACEBOOK_FEED);
						p.setChild_user_id(child_user_id);
						
						try {
							Person p3 = mongoOps.findById(p.getId(), Person.class,	DijiwiseConstants.PERSON_COLLECTION);
							if (p3 != null) {
								
								p.setReadFlag(p3.getReadFlag());
								p.setSaveFlag(p3.getSaveFlag());
								p.setDeleteFlag(p3.getDeleteFlag());
								p.setErrorFlag(p3.getErrorFlag());
								//p.setErrorMessage("");
								p.setNotificationFlag(p3.getNotificationFlag());
								p.setDelete_users_posts(p3.getDelete_users_posts());
								p.setDelete_users_likes(p3.getDelete_users_likes());
								p.setDelete_users_events(p3.getDelete_users_events());
								p.setDelete_users_comments(p3.getDelete_users_comments());
								p.setRead_users_posts(p3.getRead_users_posts());
								p.setRead_users_likes(p3.getRead_users_likes());
								p.setRead_users_events(p3.getRead_users_events());
								p.setRead_users_comments(p3.getRead_users_comments());
							} else {
								p.setReadFlag(0);
								p.setSaveFlag(0);
								p.setDeleteFlag(0);
								p.setErrorFlag(0);
								p.setErrorMessage("");
								p.setNotificationFlag(0);
								
								//read and delete flags for filters
								p.setDelete_users_posts(0);
								p.setDelete_users_likes(0);
								p.setDelete_users_events(0);
								p.setDelete_users_comments(0);
								p.setRead_users_posts(0);
								p.setRead_users_likes(0);
								p.setRead_users_events(0);
								p.setRead_users_comments(0);
							}
						} catch (Exception e) {
							logger.info("facebook flag modification exception.."+ e.toString());
						}

						DateTime retriveDate = new DateTime(i.getUpdatedTime());
						DateTime sendingDate = new DateTime(retriveDate.getYear(), retriveDate.getMonthOfYear(), retriveDate.getDayOfMonth(), 0, 0, 0);
						p.setUpdatedDate(sendingDate.getMillis());
						p.setUpdateDateTime(retriveDate.getMillis());
						p.setCaption((i.getCaption() != null ? i.getCaption() : ""));
						p.setDescription((i.getDescription() != null ? i.getDescription() : ""));
						p.setStory((i.getStory() != null ? i.getStory() : ""));
						p.setIcon((i.getIcon() != null ? i.getIcon() : ""));
						p.setPicture((i.getPicture() != null ? i.getPicture() : ""));
						p.setFromId((i.getFrom().getId() != null ? i.getFrom().getId() : ""));
						p.setAttribution((i.getAttribution() != null ? i.getAttribution() : ""));
						p.setParentId(parent_id);
						p.setSocialSiteUserId(social_site_user_id);

						logger.info("fb testing person object creted successfully.");
						logger.info("New post with Data: " + p.toString()+ " for social type:" + p.getSocialMediaType());
						mongoOps.save(p, DijiwiseConstants.PERSON_COLLECTION);

						/*obj.put("id", p.getId());
						obj.put("uuid", p.getUuid());
						obj.put("socialmediaType", p.getSocialMediaType());
						obj.put("child_user_id", p.getChild_user_id());
						obj.put("parent_id", p.getParentId());
						logger.info("Json object that will be sent to response queue :    "+ obj.toJSONString());

						countFeed++;
						Greeting result = new Greeting();
						result.setId((String) obj.get("id"));
						result.setUuid((String) obj.get("uuid"));
						result.setChild_user_id((String) obj.get("child_user_id"));
						result.setParent_id((String) obj.get("parent_id"));
						result.setSocialmediaType((String) obj.get("socialmediaType"));
						System.out.println("$$$$$$$$$$$$countFeed$$$$$$$$$"+ countFeed);*/
						
						Greeting result = new Greeting();
						result.setId(p.getId());
						result.setUuid(p.getUuid());
						result.setChild_user_id(p.getChild_user_id());
						result.setParent_id(p.getParentId());
						result.setSocialmediaType(p.getSocialMediaType());
						
						arrayGreetings.add(result);
						//System.out.println("\n arrayGreetings**********"+ arrayGreetings.size());

						logger.info("###############End#############" + i.getId() +"_"+child_user_id);

					}
					
				} catch (Exception e) {
					logger.error("Error in main body  ");
				}
				if (p2 == null)	{
					errorPerson = new ErrorPerson();
					errorPerson.setErrorOccuredDate(new Date().toString());
					errorPerson.setChildId(child_user_id);
					errorPerson.setUuid((UUID.randomUUID() + ""));
					errorPerson.setInsertDate(new Date().toString());
					errorPerson.setSocialSiteUserId(social_site_user_id);
					errorPerson.setSocialMediaType(social_media_type);
					errorPerson.setErrorFlag("0");
					mongoOps.save(errorPerson, DijiwiseConstants.PERSON_LOGIN_STATUS);
				}
			}
			} catch (Exception e) {
				
			}
			logger.info("Thread  ending ");
		} catch (Exception e) {
			logger.error("Exception processing post :" + e.getMessage());
			e.printStackTrace();
		} finally {
			if (mongo != null && mongo instanceof MongoClient) {
				try {
					mongo.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		array.setList(arrayGreetings);
		return array;
	}
}