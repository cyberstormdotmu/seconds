package fi.korri.epooq.controller;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;
import javax.servlet.ServletContext;
import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.multipart.MultipartFile;

import fi.korri.epooq.Anchor;
import fi.korri.epooq.Community;
import fi.korri.epooq.Key;
import fi.korri.epooq.Story;

@Controller
public abstract class AbstractBaseController {

	private static final Logger log = LoggerFactory
			.getLogger(AbstractBaseController.class);

	public static final Long PUBLIC_COMMUNITY_ID = null;

	public static final String SESSION_COMMUNITY = "communityId";

	@Autowired
	protected ServletContext context;

	@Resource
	private DataSource dataSource;

	/*
	 * Helper methods
	 */

	protected Story getStoryById(String id) {
		log.info("Finding video with id: " + id);

		Connection conn = null;

		Story video = null;

		try {
			conn = dataSource.getConnection();

			String sql = "SELECT * FROM story_tbl WHERE id = ?";
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setString(1, id);

			ResultSet rs = ps.executeQuery();

			while (rs.next()) {
				video = convertResultSetToStory(rs);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
				}
			}
		}

		return video;
	}

	protected void deleteStoryById(String id) {
		log.info("Deleting video with id: " + id);

		Connection conn = null;

		try {
			conn = dataSource.getConnection();

			String sql = "DELETE FROM story_tbl WHERE id = ?";
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setString(1, id);

			if (ps.executeUpdate() > 0) {
				File dir = new File(context.getRealPath("streams"));

				File videoFile = new File(dir, id + ".flv");
				videoFile.delete();
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
				}
			}
		}
	}

	protected void deleteKeyById(long id) {
		log.info("Deleting key with id: " + id);

		Connection conn = null;

		try {
			conn = dataSource.getConnection();

			String sql = "DELETE FROM key_tbl WHERE id = ?";
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setLong(1, id);

			ps.executeUpdate();

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
				}
			}
		}
	}

	protected void deleteCommunityById(long id) {
		log.info("Deleting community with id: " + id);

		Connection conn = null;

		try {
			conn = dataSource.getConnection();
			conn.setAutoCommit(false); // transaction block start

			// Delete Anchors
			String sql = "DELETE FROM anchor_tbl WHERE community_id = ?";
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setLong(1, id);
			ps.executeUpdate();

			// Delete stories
			sql = "DELETE FROM story_tbl WHERE community_id = ?";
			ps = conn.prepareStatement(sql);
			ps.setLong(1, id);
			ps.executeUpdate();

			// Delete keys
			sql = "DELETE FROM key_tbl WHERE community_id = ?";
			ps = conn.prepareStatement(sql);
			ps.setLong(1, id);
			ps.executeUpdate();

			// Delete communities
			sql = "DELETE FROM community_tbl WHERE id = ?";
			ps = conn.prepareStatement(sql);
			ps.setLong(1, id);
			ps.executeUpdate();

			conn.commit();

		} catch (SQLException e) {
			e.printStackTrace();

			try {
				conn.rollback();
			} catch (SQLException e1) {
			}
		} finally {
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
				}
			}
		}
	}

	protected InputStream getPicture(long id) {
		log.info("Finding video with id: " + id);

		Connection conn = null;

		try {
			conn = dataSource.getConnection();

			String sql = "SELECT * FROM picture_tbl WHERE id = ?";
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setLong(1, id);

			ResultSet rs = ps.executeQuery();

			while (rs.next()) {
				return rs.getBinaryStream("data");
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return null;
	}

	protected Community getCommunityById(long id) {
		log.info("Finding community with id: " + id);

		Connection conn = null;

		Community community = null;

		try {
			conn = dataSource.getConnection();

			String sql = "SELECT * FROM community_tbl WHERE id = ?";
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setLong(1, id);

			ResultSet rs = ps.executeQuery();

			while (rs.next()) {
				community = convertResultSetToCommunity(rs);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return community;
	}

	protected Set<Community> getCommunities() {

		Connection conn = null;

		log.info("Finding communities");

		Set<Community> communities = new LinkedHashSet<Community>();

		try {
			conn = dataSource.getConnection();

			String sql = "SELECT * FROM community_tbl ORDER BY id ASC";

			PreparedStatement ps = conn.prepareStatement(sql);

			ResultSet rs = ps.executeQuery();

			while (rs.next()) {
				communities.add(convertResultSetToCommunity(rs));
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

		log.info("Found " + communities.size() + " communities");

		return communities;
	}

	protected Set<Story> getStoriesByCommunity(Long communityId) {
		return getStoriesByCommunity(communityId, null);
	}

	protected Set<Story> getStoriesByCommunity(Long communityId, String search) {

		Connection conn = null;

		log.info("Finding videos for community: " + communityId);

		Set<Story> videos = new LinkedHashSet<Story>();

		try {
			conn = dataSource.getConnection();
			PreparedStatement ps = null;

			if (communityId != null) {
				String sql = "SELECT * FROM story_tbl WHERE community_id = ?";
				if (search != null) {
					sql += " AND title LIKE ?";
				}
				sql += " ORDER BY created DESC";
				ps = conn.prepareStatement(sql);
				ps.setLong(1, communityId);
				if (search != null) {
					ps.setString(2, "%" + search + "%");
				}
			} else {
				String sql = "SELECT * FROM story_tbl WHERE community_id IS NULL";
				if (search != null) {
					sql += " AND title LIKE ?";
				}
				sql += " ORDER BY created DESC";
				ps = conn.prepareStatement(sql);
				if (search != null) {
					ps.setString(1, "%" + search + "%");
				}
			}

			ResultSet rs = ps.executeQuery();

			while (rs.next()) {
				videos.add(convertResultSetToStory(rs));
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return videos;
	}

	protected Set<Anchor> getAnchorsByCommunity(Long communityId) {
		return getAnchorsByCommunity(communityId, null);
	}

	protected Set<Anchor> getAnchorsByCommunity(Long communityId, String search) {

		Connection conn = null;

		log.info("Finding anchors for community: " + communityId + " search: "
				+ search);

		Set<Anchor> anchors = new LinkedHashSet<Anchor>();

		try {
			conn = dataSource.getConnection();
			PreparedStatement ps = null;

			if (communityId != null) {
				String sql = "SELECT * FROM anchor_tbl WHERE community_id = ?";
				if (search != null) {
					sql += " AND title LIKE ?";
				}
				sql += " ORDER BY date DESC";
				ps = conn.prepareStatement(sql);
				ps.setLong(1, communityId);
				if (search != null) {
					ps.setString(2, "%" + search + "%");
				}
				log.info("SQL: " + sql + " search: " + "%" + search + "%");
			} else {
				String sql = "SELECT * FROM anchor_tbl WHERE community_id IS NULL";
				if (search != null) {
					sql += " AND title LIKE ?";
				}
				sql += " ORDER BY date DESC";
				ps = conn.prepareStatement(sql);
				if (search != null) {
					ps.setString(1, "%" + search + "%");
				}
				log.info("SQL: " + sql + " search: " + "'%" + search + "%'");
			}

			ResultSet rs = ps.executeQuery();

			while (rs.next()) {
				anchors.add(convertResultSetToAnchor(rs));
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return anchors;
	}

	protected Set<Key> getKeysByCommunity(Long communityId) {
		log.info("Finding keys  community id: " + communityId);

		Connection conn = null;

		Set<Key> keys = new LinkedHashSet<Key>();

		try {
			conn = dataSource.getConnection();

			String sql = "";
			PreparedStatement ps = null;

			if (communityId != null) {
				sql = "SELECT * FROM key_tbl WHERE community_id = ?";
				ps = conn.prepareStatement(sql);
				ps.setLong(1, communityId);
			} else {
				sql = "SELECT * FROM key_tbl WHERE community_id IS NULL";
				ps = conn.prepareStatement(sql);
			}

			ResultSet rs = ps.executeQuery();

			while (rs.next()) {
				keys.add(convertResultSetToKey(rs));
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return keys;
	}

	protected Set<Key> getAllKeys() {
		log.info("Finding keys");

		Connection conn = null;

		Set<Key> keys = new LinkedHashSet<Key>();

		try {
			conn = dataSource.getConnection();

			String sql = "SELECT * FROM key_tbl";
			PreparedStatement ps = conn.prepareStatement(sql);

			ResultSet rs = ps.executeQuery();

			while (rs.next()) {
				keys.add(convertResultSetToKey(rs));
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return keys;
	}

	private Set<Story> getUnfinishedStories() {

		Connection conn = null;

		log.info("Finding unfinished stories");

		Set<Story> videos = new LinkedHashSet<Story>();

		try {
			conn = dataSource.getConnection();

			String sql = "SELECT * FROM story_tbl WHERE ready = ?";
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setBoolean(1, false);

			ResultSet rs = ps.executeQuery();

			while (rs.next()) {
				videos.add(convertResultSetToStory(rs));
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return videos;
	}

	protected Map<String, String> getSettings() {

		Connection conn = null;

		log.info("Fetching settings");

		Map<String, String> settings = new HashMap<String, String>();

		try {
			conn = dataSource.getConnection();

			String sql = "SELECT * FROM settings_tbl";
			PreparedStatement ps = conn.prepareStatement(sql);

			ResultSet rs = ps.executeQuery();

			while (rs.next()) {
				settings.put(rs.getString("key"), rs.getString("value"));
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return settings;
	}

	private Community convertResultSetToCommunity(ResultSet rs)
			throws SQLException {
		long id = rs.getLong("id");
		String name = rs.getString("name");
		Long pictureId = rs.getLong("picture_id");
		if(pictureId == 0) {
			pictureId = null;
		}

		Community community = new Community();
		community.setId(id);
		community.setName(name);
		community.setPictureId(pictureId);

		return community;
	}

	private Story convertResultSetToStory(ResultSet rs) throws SQLException {
		String id = rs.getString("id");
		String title = rs.getString("title");
		String place = rs.getString("place");
		Long communityId = rs.getLong("community_Id");
		if(communityId == 0) {
			communityId = null;
		}
		boolean ready = rs.getBoolean("ready");
		Date created = rs.getDate("created");

		Story story = new Story(id, title, place, created);
		story.setReady(ready);
		story.setCommunityId(communityId);

		return story;
	}

	private Anchor convertResultSetToAnchor(ResultSet rs) throws SQLException {
		long id = rs.getLong("id");
		String title = rs.getString("title");
		Date date = rs.getDate("date");
		String place = rs.getString("place");
		String content = rs.getString("content");
		long communityId = rs.getLong("community_Id");
		Long pictureId = rs.getLong("picture_id");
		if(pictureId == 0) {
			pictureId = null;
		}

		Anchor anchor = new Anchor();
		anchor.setId(id);
		anchor.setTitle(title);
		anchor.setDate(date);
		anchor.setPlace(place);
		anchor.setContent(content);
		anchor.setCommunityId(communityId);
		anchor.setPictureId(pictureId);

		return anchor;
	}

	private Key convertResultSetToKey(ResultSet rs) throws SQLException {
		long id = rs.getLong("id");
		String question = rs.getString("question");
		String ageString = rs.getString("age");
		long communityId = rs.getLong("community_Id");
		Long pictureId = rs.getLong("picture_id");
		if(pictureId == 0) {
			pictureId = null;
		}

		
		// Try to parse the age
		int age = 0;
		try {
			age = Integer.parseInt(ageString);
		} catch (Exception e) {

		}

		Key key = new Key();
		key.setId(id);
		key.setQuestion(question);
		key.setAge(age);
		key.setCommunityId(communityId);
		key.setPictureId(pictureId);

		return key;
	}

	protected void pruneVideoFiles() {
		for (Story md : getUnfinishedStories()) {
			deleteStoryById(md.getId());
		}
	}

	protected void writeStory(String videoId, Story story) {

		Connection conn = null;
		try {
			conn = dataSource.getConnection();

			String sql = "SELECT * FROM story_tbl WHERE id = ?";
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setString(1, story.getId());

			ResultSet rs = ps.executeQuery();

			if (rs.next()) {
				sql = "UPDATE story_tbl SET title = ?, place = ?, community_id = ?, ready = ?, created = ? WHERE id = ? ";
			} else {
				sql = "INSERT INTO story_tbl (title, place, community_id, ready, created, id) VALUES (?, ?, ?, ?, ?, ?)";
			}
			ps.close();
			conn.close();

			conn = dataSource.getConnection();

			ps = conn.prepareStatement(sql);
			ps.setString(1, story.getTitle());
			ps.setString(2, story.getPlace());
			if (story.getCommunityId() != null) {
				ps.setLong(3, story.getCommunityId());
			} else {
				ps.setNull(3, Types.BIGINT);
			}
			ps.setBoolean(4, story.isReady());
			ps.setDate(5, story.getSqlDate());
			ps.setString(6, story.getId());

			ps.executeUpdate();
			ps.close();

		} catch (SQLException e1) {
			e1.printStackTrace();
		} finally {
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
				}
			}
		}
	}

	protected void writeCommunity(Community community) {

		Connection conn = null;
		try {
			conn = dataSource.getConnection();

			String sql = "SELECT * FROM community_tbl WHERE id = ?";
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setLong(1, community.getId());

			ResultSet rs = ps.executeQuery();
			boolean update = false;

			if (rs.next()) {
				update = true;
				sql = "UPDATE community_tbl SET name = ?, picture_id = ? WHERE id = ? ";
			} else {
				if(community.getPictureId() != null) {
					sql = "INSERT INTO community_tbl (name, picture_id) VALUES (?, ?)";
				} else {
					sql = "INSERT INTO community_tbl (name) VALUES (?)";
				}
				
			}
			ps.close();
			conn.close();

			conn = dataSource.getConnection();

			ps = conn.prepareStatement(sql);
			ps.setString(1, community.getName());
			if(community.getPictureId() != null) {
				ps.setLong(2, community.getPictureId());
			}

			if (update) {
				ps.setLong(3, community.getId());
			}

			ps.executeUpdate();
			ps.close();

		} catch (SQLException e1) {
			e1.printStackTrace();
		} finally {
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
				}
			}
		}
	}

	protected void writeAnchor(Anchor anchor) {

		Connection conn = null;
		try {
			conn = dataSource.getConnection();

			String sql = "SELECT * FROM anchor_tbl WHERE id = ?";
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setLong(1, anchor.getId());

			ResultSet rs = ps.executeQuery();
			boolean update = false;

			if (rs.next()) {
				update = true;
				sql = "UPDATE anchor_tbl SET title = ?, date = ?, place = ?, content = ? , community_id = ?, picture_id = ? WHERE id = ? ";
			} else {
				sql = "INSERT INTO anchor_tbl (title, date, place, content, community_id, picture_id) VALUES (?, ?, ?, ?, ?, ?)";
			}
			ps.close();
			conn.close();

			conn = dataSource.getConnection();

			ps = conn.prepareStatement(sql);
			ps.setString(1, anchor.getTitle());
			ps.setDate(2, anchor.getSqlDate());
			ps.setString(3, anchor.getPlace());
			ps.setString(4, anchor.getContent());
			if (anchor.getCommunityId() != null) {
				ps.setLong(5, anchor.getCommunityId());
			} else {
				ps.setNull(5, Types.BIGINT);
			}

			if (anchor.getPictureId() != null) {
				ps.setLong(6, anchor.getPictureId());
			} else {
				ps.setNull(6, Types.BIGINT);
			}

			if (update) {
				ps.setLong(7, anchor.getId());
			}

			ps.executeUpdate();
			ps.close();

		} catch (SQLException e1) {
			e1.printStackTrace();
		} finally {
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
				}
			}
		}
	}

	protected Long writePicture(MultipartFile file) {

		Long id = null;

		if (file != null && !file.isEmpty()) {
			Connection conn = null;
			PreparedStatement ps = null;
			try {
				conn = dataSource.getConnection();

				String sql = "INSERT INTO picture_tbl (mime_type, data) VALUES (?, ?)";

				ps = conn.prepareStatement(sql);
				ps.setString(1, file.getContentType());
				ps.setBinaryStream(2, file.getInputStream());

				ps.executeUpdate();
				ResultSet rs = ps.getGeneratedKeys();
				if (rs.next()) {
					id = rs.getLong(1);
				} else {
					throw new SQLException("Creating picture failed.");
				}

			} catch (SQLException e1) {
				e1.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				if (ps != null) {
					try {
						ps.close();
					} catch (SQLException e) {
					}
				}
				if (conn != null) {
					try {
						conn.close();
					} catch (SQLException e) {
					}
				}
			}
		}

		return id;
	}

	protected void writeSettings(String key, String value) {

		Connection conn = null;
		try {
			conn = dataSource.getConnection();

			String sql = "SELECT * FROM settings_tbl WHERE key = ?";
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setString(1, key);

			ResultSet rs = ps.executeQuery();
			boolean update = false;
			if (rs.next()) {
				update = true;
			}
			ps.close();

			if (update) {
				sql = "UPDATE settings_tbl SET value = ? WHERE key = ? ";
			} else {
				sql = "INSERT INTO settings_tbl (key, value) VALUES (?, ?)";
			}

			ps = conn.prepareStatement(sql);

			if (update) {
				ps.setString(1, value);
				ps.setString(2, key);
			} else {
				ps.setString(1, key);
				ps.setString(2, value);
			}

			ps.executeUpdate();

		} catch (SQLException e1) {
			e1.printStackTrace();
		} finally {
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
				}
			}
		}
	}

	protected void writeKey(Key key) {

		Connection conn = null;
		try {
			conn = dataSource.getConnection();

			String sql = "SELECT * FROM key_tbl WHERE id = ?";
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setLong(1, key.getId());

			ResultSet rs = ps.executeQuery();
			boolean update = false;

			if (rs.next()) {
				update = true;
				sql = "UPDATE key_tbl SET question = ?, age = ?, community_id = ?, picture_id = ? WHERE id = ? ";
			} else {
				sql = "INSERT INTO key_tbl (question, age, community_id, picture_id) VALUES (?, ?, ?, ?)";
			}
			ps.close();
			conn.close();

			conn = dataSource.getConnection();

			ps = conn.prepareStatement(sql);
			ps.setString(1, key.getQuestion());
			ps.setInt(2, key.getAge());

			if (key.getCommunityId() != null) {
				ps.setLong(3, key.getCommunityId());
			} else {
				ps.setNull(3, Types.BIGINT);
			}

			if (key.getPictureId() != null) {
				ps.setLong(4, key.getPictureId());
			} else {
				ps.setNull(4, Types.BIGINT);
			}

			if (update) {
				ps.setLong(5, key.getId());
			}

			ps.executeUpdate();
			ps.close();

		} catch (SQLException e1) {
			e1.printStackTrace();
		} finally {
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
				}
			}
		}
	}
}
