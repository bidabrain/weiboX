package com.weibox.app.data.db;

import androidx.annotation.NonNull;
import androidx.room.DatabaseConfiguration;
import androidx.room.InvalidationTracker;
import androidx.room.RoomDatabase;
import androidx.room.RoomOpenHelper;
import androidx.room.migration.AutoMigrationSpec;
import androidx.room.migration.Migration;
import androidx.room.util.DBUtil;
import androidx.room.util.TableInfo;
import androidx.sqlite.db.SupportSQLiteDatabase;
import androidx.sqlite.db.SupportSQLiteOpenHelper;
import com.weibox.app.data.db.dao.PostDao;
import com.weibox.app.data.db.dao.PostDao_Impl;
import com.weibox.app.data.db.dao.UserDao;
import com.weibox.app.data.db.dao.UserDao_Impl;
import java.lang.Class;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

@SuppressWarnings({"unchecked", "deprecation"})
public final class AppDatabase_Impl extends AppDatabase {
  private volatile UserDao _userDao;

  private volatile PostDao _postDao;

  @Override
  @NonNull
  protected SupportSQLiteOpenHelper createOpenHelper(@NonNull final DatabaseConfiguration config) {
    final SupportSQLiteOpenHelper.Callback _openCallback = new RoomOpenHelper(config, new RoomOpenHelper.Delegate(1) {
      @Override
      public void createAllTables(@NonNull final SupportSQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS `followed_users` (`id` TEXT NOT NULL, `screenName` TEXT NOT NULL, `description` TEXT NOT NULL, `avatarUrl` TEXT NOT NULL, `coverUrl` TEXT NOT NULL, `followersCount` TEXT NOT NULL, `followCount` INTEGER NOT NULL, `statusesCount` INTEGER NOT NULL, `verified` INTEGER NOT NULL, `verifiedReason` TEXT NOT NULL, `followedAt` INTEGER NOT NULL, PRIMARY KEY(`id`))");
        db.execSQL("CREATE TABLE IF NOT EXISTS `cached_posts` (`id` TEXT NOT NULL, `userId` TEXT NOT NULL, `userName` TEXT NOT NULL, `userAvatar` TEXT NOT NULL, `text` TEXT NOT NULL, `pics` TEXT NOT NULL, `createdAt` TEXT NOT NULL, `createdAtTimestamp` INTEGER NOT NULL, `likesCount` INTEGER NOT NULL, `commentsCount` INTEGER NOT NULL, `repostsCount` INTEGER NOT NULL, `source` TEXT NOT NULL, `isRetweet` INTEGER NOT NULL, `retweetUserId` TEXT NOT NULL, `retweetUserName` TEXT NOT NULL, `retweetText` TEXT NOT NULL, `retweetPics` TEXT NOT NULL, `cachedAt` INTEGER NOT NULL, PRIMARY KEY(`id`))");
        db.execSQL("CREATE TABLE IF NOT EXISTS room_master_table (id INTEGER PRIMARY KEY,identity_hash TEXT)");
        db.execSQL("INSERT OR REPLACE INTO room_master_table (id,identity_hash) VALUES(42, '09ef24da8dfe692f185c844b20d4c9d7')");
      }

      @Override
      public void dropAllTables(@NonNull final SupportSQLiteDatabase db) {
        db.execSQL("DROP TABLE IF EXISTS `followed_users`");
        db.execSQL("DROP TABLE IF EXISTS `cached_posts`");
        final List<? extends RoomDatabase.Callback> _callbacks = mCallbacks;
        if (_callbacks != null) {
          for (RoomDatabase.Callback _callback : _callbacks) {
            _callback.onDestructiveMigration(db);
          }
        }
      }

      @Override
      public void onCreate(@NonNull final SupportSQLiteDatabase db) {
        final List<? extends RoomDatabase.Callback> _callbacks = mCallbacks;
        if (_callbacks != null) {
          for (RoomDatabase.Callback _callback : _callbacks) {
            _callback.onCreate(db);
          }
        }
      }

      @Override
      public void onOpen(@NonNull final SupportSQLiteDatabase db) {
        mDatabase = db;
        internalInitInvalidationTracker(db);
        final List<? extends RoomDatabase.Callback> _callbacks = mCallbacks;
        if (_callbacks != null) {
          for (RoomDatabase.Callback _callback : _callbacks) {
            _callback.onOpen(db);
          }
        }
      }

      @Override
      public void onPreMigrate(@NonNull final SupportSQLiteDatabase db) {
        DBUtil.dropFtsSyncTriggers(db);
      }

      @Override
      public void onPostMigrate(@NonNull final SupportSQLiteDatabase db) {
      }

      @Override
      @NonNull
      public RoomOpenHelper.ValidationResult onValidateSchema(
          @NonNull final SupportSQLiteDatabase db) {
        final HashMap<String, TableInfo.Column> _columnsFollowedUsers = new HashMap<String, TableInfo.Column>(11);
        _columnsFollowedUsers.put("id", new TableInfo.Column("id", "TEXT", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsFollowedUsers.put("screenName", new TableInfo.Column("screenName", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsFollowedUsers.put("description", new TableInfo.Column("description", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsFollowedUsers.put("avatarUrl", new TableInfo.Column("avatarUrl", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsFollowedUsers.put("coverUrl", new TableInfo.Column("coverUrl", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsFollowedUsers.put("followersCount", new TableInfo.Column("followersCount", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsFollowedUsers.put("followCount", new TableInfo.Column("followCount", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsFollowedUsers.put("statusesCount", new TableInfo.Column("statusesCount", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsFollowedUsers.put("verified", new TableInfo.Column("verified", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsFollowedUsers.put("verifiedReason", new TableInfo.Column("verifiedReason", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsFollowedUsers.put("followedAt", new TableInfo.Column("followedAt", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysFollowedUsers = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesFollowedUsers = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoFollowedUsers = new TableInfo("followed_users", _columnsFollowedUsers, _foreignKeysFollowedUsers, _indicesFollowedUsers);
        final TableInfo _existingFollowedUsers = TableInfo.read(db, "followed_users");
        if (!_infoFollowedUsers.equals(_existingFollowedUsers)) {
          return new RoomOpenHelper.ValidationResult(false, "followed_users(com.weibox.app.data.db.entity.UserEntity).\n"
                  + " Expected:\n" + _infoFollowedUsers + "\n"
                  + " Found:\n" + _existingFollowedUsers);
        }
        final HashMap<String, TableInfo.Column> _columnsCachedPosts = new HashMap<String, TableInfo.Column>(18);
        _columnsCachedPosts.put("id", new TableInfo.Column("id", "TEXT", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCachedPosts.put("userId", new TableInfo.Column("userId", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCachedPosts.put("userName", new TableInfo.Column("userName", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCachedPosts.put("userAvatar", new TableInfo.Column("userAvatar", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCachedPosts.put("text", new TableInfo.Column("text", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCachedPosts.put("pics", new TableInfo.Column("pics", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCachedPosts.put("createdAt", new TableInfo.Column("createdAt", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCachedPosts.put("createdAtTimestamp", new TableInfo.Column("createdAtTimestamp", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCachedPosts.put("likesCount", new TableInfo.Column("likesCount", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCachedPosts.put("commentsCount", new TableInfo.Column("commentsCount", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCachedPosts.put("repostsCount", new TableInfo.Column("repostsCount", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCachedPosts.put("source", new TableInfo.Column("source", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCachedPosts.put("isRetweet", new TableInfo.Column("isRetweet", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCachedPosts.put("retweetUserId", new TableInfo.Column("retweetUserId", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCachedPosts.put("retweetUserName", new TableInfo.Column("retweetUserName", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCachedPosts.put("retweetText", new TableInfo.Column("retweetText", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCachedPosts.put("retweetPics", new TableInfo.Column("retweetPics", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCachedPosts.put("cachedAt", new TableInfo.Column("cachedAt", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysCachedPosts = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesCachedPosts = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoCachedPosts = new TableInfo("cached_posts", _columnsCachedPosts, _foreignKeysCachedPosts, _indicesCachedPosts);
        final TableInfo _existingCachedPosts = TableInfo.read(db, "cached_posts");
        if (!_infoCachedPosts.equals(_existingCachedPosts)) {
          return new RoomOpenHelper.ValidationResult(false, "cached_posts(com.weibox.app.data.db.entity.PostEntity).\n"
                  + " Expected:\n" + _infoCachedPosts + "\n"
                  + " Found:\n" + _existingCachedPosts);
        }
        return new RoomOpenHelper.ValidationResult(true, null);
      }
    }, "09ef24da8dfe692f185c844b20d4c9d7", "08fa5a7f0f6a744869a810e9cfe341f4");
    final SupportSQLiteOpenHelper.Configuration _sqliteConfig = SupportSQLiteOpenHelper.Configuration.builder(config.context).name(config.name).callback(_openCallback).build();
    final SupportSQLiteOpenHelper _helper = config.sqliteOpenHelperFactory.create(_sqliteConfig);
    return _helper;
  }

  @Override
  @NonNull
  protected InvalidationTracker createInvalidationTracker() {
    final HashMap<String, String> _shadowTablesMap = new HashMap<String, String>(0);
    final HashMap<String, Set<String>> _viewTables = new HashMap<String, Set<String>>(0);
    return new InvalidationTracker(this, _shadowTablesMap, _viewTables, "followed_users","cached_posts");
  }

  @Override
  public void clearAllTables() {
    super.assertNotMainThread();
    final SupportSQLiteDatabase _db = super.getOpenHelper().getWritableDatabase();
    try {
      super.beginTransaction();
      _db.execSQL("DELETE FROM `followed_users`");
      _db.execSQL("DELETE FROM `cached_posts`");
      super.setTransactionSuccessful();
    } finally {
      super.endTransaction();
      _db.query("PRAGMA wal_checkpoint(FULL)").close();
      if (!_db.inTransaction()) {
        _db.execSQL("VACUUM");
      }
    }
  }

  @Override
  @NonNull
  protected Map<Class<?>, List<Class<?>>> getRequiredTypeConverters() {
    final HashMap<Class<?>, List<Class<?>>> _typeConvertersMap = new HashMap<Class<?>, List<Class<?>>>();
    _typeConvertersMap.put(UserDao.class, UserDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(PostDao.class, PostDao_Impl.getRequiredConverters());
    return _typeConvertersMap;
  }

  @Override
  @NonNull
  public Set<Class<? extends AutoMigrationSpec>> getRequiredAutoMigrationSpecs() {
    final HashSet<Class<? extends AutoMigrationSpec>> _autoMigrationSpecsSet = new HashSet<Class<? extends AutoMigrationSpec>>();
    return _autoMigrationSpecsSet;
  }

  @Override
  @NonNull
  public List<Migration> getAutoMigrations(
      @NonNull final Map<Class<? extends AutoMigrationSpec>, AutoMigrationSpec> autoMigrationSpecs) {
    final List<Migration> _autoMigrations = new ArrayList<Migration>();
    return _autoMigrations;
  }

  @Override
  public UserDao userDao() {
    if (_userDao != null) {
      return _userDao;
    } else {
      synchronized(this) {
        if(_userDao == null) {
          _userDao = new UserDao_Impl(this);
        }
        return _userDao;
      }
    }
  }

  @Override
  public PostDao postDao() {
    if (_postDao != null) {
      return _postDao;
    } else {
      synchronized(this) {
        if(_postDao == null) {
          _postDao = new PostDao_Impl(this);
        }
        return _postDao;
      }
    }
  }
}
