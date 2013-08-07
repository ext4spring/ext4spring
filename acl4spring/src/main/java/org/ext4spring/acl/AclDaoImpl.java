package org.ext4spring.acl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.ext4spring.acl.model.AclPermission;
import org.ext4spring.acl.model.AclPermissionImpl;
import org.ext4spring.acl.model.Cache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 * @see AclDao
 * @author borbasp
 * 
 */

@Repository
@Transactional
public class AclDaoImpl implements AclDao {

	private static final String CACHE_KEY = "#type.getClass().getName() + #id.toString()";

	private JdbcTemplate jdbcTemplate;
	private NamedParameterJdbcTemplate namedJdbcTemplate;

	@Autowired
	public void setDataSource(AclContext aclContext) {
		this.jdbcTemplate = new JdbcTemplate(aclContext.getDataSource());
		this.namedJdbcTemplate = new NamedParameterJdbcTemplate(aclContext.getDataSource());
	}

	@Override
	@Cacheable(value = Cache.ACL_REGION, key = CACHE_KEY)
	public Map<String, List<AclPermission>> query(Class<?> type, String id) {
		return this.jdbcTemplate.query("SELECT USER_ID,ACL_PERMISSION FROM EFS_ACL WHERE OBJECT_TYPE=? AND OBJECT_ID=? ORDER BY USER_ID, ACL_PERMISSION", new Object[] { type.getName(), id },
				new ResultSetExtractor<Map<String, List<AclPermission>>>() {
					@Override
					public Map<String, List<AclPermission>> extractData(ResultSet rs) throws SQLException, DataAccessException {
						Map<String, List<AclPermission>> result = new LinkedHashMap<String, List<AclPermission>>();
						while (rs.next()) {
							String userId = rs.getString("USER_ID");
							List<AclPermission> permissions = result.get(userId);
							if (permissions == null) {
								permissions = new LinkedList<AclPermission>();
								result.put(userId, permissions);
							}
							permissions.add(new AclPermissionImpl(rs.getString("ACL_PERMISSION")));
						}
						return result;
					}
				});
	}

	@Override
	@CacheEvict(value = Cache.ACL_REGION, key = CACHE_KEY)
	public void insertEntry(Class<?> type, String id, String userId, AclPermission permission) {
		this.jdbcTemplate.update("INSERT INTO EFS_ACL (OBJECT_TYPE,OBJECT_ID,USER_ID,ACL_PERMISSION) values (?,?,?,?)", type.getName(), id, userId, permission.getName());
	}

	@Override
	@CacheEvict(value = Cache.ACL_REGION, key = CACHE_KEY)
	public void remove(Class<?> type, String id) {
		this.jdbcTemplate.update("DELETE FROM EFS_ACL WHERE OBJECT_TYPE=? AND OBJECT_ID=?", type.getName(), id);
	}

	@CacheEvict(value = Cache.ACL_REGION, key = CACHE_KEY)
	@Override
	public void remove(Class<?> type, String id, String userId) {
		this.jdbcTemplate.update("DELETE FROM EFS_ACL WHERE OBJECT_TYPE=? AND OBJECT_ID=? AND USER_ID=?", type.getName(), id, userId);
	}

	@Override
	@CacheEvict(value = Cache.ACL_REGION, key = CACHE_KEY)
	public void remove(Class<?> type, String id, String userId, AclPermission... permissions) {
		Set<String> permissionNames = new HashSet<String>();
		for (AclPermission permission : permissions) {
			permissionNames.add(permission.getName());
		}
		MapSqlParameterSource parameters = new MapSqlParameterSource();
		parameters.addValue("type", type.getName());
		parameters.addValue("id", id);
		parameters.addValue("userId", userId);
		parameters.addValue("permissionNames", permissionNames);
		this.namedJdbcTemplate.update("DELETE FROM EFS_ACL WHERE OBJECT_TYPE=:type AND OBJECT_ID=:id AND USER_ID=:userId AND ACL_PERMISSION IN (:permissionNames)", parameters);
	}

}
