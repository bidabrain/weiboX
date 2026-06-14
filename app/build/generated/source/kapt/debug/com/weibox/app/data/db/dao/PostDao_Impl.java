package com.weibox.app.data.db.dao;

import android.database.Cursor;
import android.os.CancellationSignal;
import androidx.annotation.NonNull;
import androidx.room.CoroutinesRoom;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.SharedSQLiteStatement;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import com.weibox.app.data.db.entity.PostEntity;
import com.weibox.app.data.db.entity.StringListConverter;
import java.lang.Class;
import java.lang.Exception;
import java.lang.Integer;
import java.lang.Object;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlinx.coroutines.flow.Flow;

@SuppressWarnings({"unchecked", "deprecation"})
public final class PostDao_Impl implements PostDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<PostEntity> __insertionAdapterOfPostEntity;

  private final StringListConverter __stringListConverter = new StringListConverter();

  private final SharedSQLiteStatement __preparedStmtOfDeleteByUser;

  private final SharedSQLiteStatement __preparedStmtOfDeleteOlderThan;

  private final SharedSQLiteStatement __preparedStmtOfDeleteOldest;

  public PostDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfPostEntity = new EntityInsertionAdapter<PostEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `cached_posts` (`id`,`userId`,`userName`,`userAvatar`,`text`,`pics`,`createdAt`,`createdAtTimestamp`,`likesCount`,`commentsCount`,`repostsCount`,`source`,`isRetweet`,`retweetUserId`,`retweetUserName`,`retweetText`,`retweetPics`,`cachedAt`) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final PostEntity entity) {
        if (entity.getId() == null) {
          statement.bindNull(1);
        } else {
          statement.bindString(1, entity.getId());
        }
        if (entity.getUserId() == null) {
          statement.bindNull(2);
        } else {
          statement.bindString(2, entity.getUserId());
        }
        if (entity.getUserName() == null) {
          statement.bindNull(3);
        } else {
          statement.bindString(3, entity.getUserName());
        }
        if (entity.getUserAvatar() == null) {
          statement.bindNull(4);
        } else {
          statement.bindString(4, entity.getUserAvatar());
        }
        if (entity.getText() == null) {
          statement.bindNull(5);
        } else {
          statement.bindString(5, entity.getText());
        }
        final String _tmp = __stringListConverter.fromList(entity.getPics());
        if (_tmp == null) {
          statement.bindNull(6);
        } else {
          statement.bindString(6, _tmp);
        }
        if (entity.getCreatedAt() == null) {
          statement.bindNull(7);
        } else {
          statement.bindString(7, entity.getCreatedAt());
        }
        statement.bindLong(8, entity.getCreatedAtTimestamp());
        statement.bindLong(9, entity.getLikesCount());
        statement.bindLong(10, entity.getCommentsCount());
        statement.bindLong(11, entity.getRepostsCount());
        if (entity.getSource() == null) {
          statement.bindNull(12);
        } else {
          statement.bindString(12, entity.getSource());
        }
        final int _tmp_1 = entity.isRetweet() ? 1 : 0;
        statement.bindLong(13, _tmp_1);
        if (entity.getRetweetUserId() == null) {
          statement.bindNull(14);
        } else {
          statement.bindString(14, entity.getRetweetUserId());
        }
        if (entity.getRetweetUserName() == null) {
          statement.bindNull(15);
        } else {
          statement.bindString(15, entity.getRetweetUserName());
        }
        if (entity.getRetweetText() == null) {
          statement.bindNull(16);
        } else {
          statement.bindString(16, entity.getRetweetText());
        }
        final String _tmp_2 = __stringListConverter.fromList(entity.getRetweetPics());
        if (_tmp_2 == null) {
          statement.bindNull(17);
        } else {
          statement.bindString(17, _tmp_2);
        }
        statement.bindLong(18, entity.getCachedAt());
      }
    };
    this.__preparedStmtOfDeleteByUser = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM cached_posts WHERE userId = ?";
        return _query;
      }
    };
    this.__preparedStmtOfDeleteOlderThan = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM cached_posts WHERE cachedAt < ?";
        return _query;
      }
    };
    this.__preparedStmtOfDeleteOldest = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "\n"
                + "        DELETE FROM cached_posts WHERE id IN (\n"
                + "            SELECT id FROM cached_posts ORDER BY createdAtTimestamp ASC LIMIT ?\n"
                + "        )\n"
                + "    ";
        return _query;
      }
    };
  }

  @Override
  public Object insertAll(final List<PostEntity> posts,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfPostEntity.insert(posts);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object deleteByUser(final String userId, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfDeleteByUser.acquire();
        int _argIndex = 1;
        if (userId == null) {
          _stmt.bindNull(_argIndex);
        } else {
          _stmt.bindString(_argIndex, userId);
        }
        try {
          __db.beginTransaction();
          try {
            _stmt.executeUpdateDelete();
            __db.setTransactionSuccessful();
            return Unit.INSTANCE;
          } finally {
            __db.endTransaction();
          }
        } finally {
          __preparedStmtOfDeleteByUser.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Object deleteOlderThan(final long before, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfDeleteOlderThan.acquire();
        int _argIndex = 1;
        _stmt.bindLong(_argIndex, before);
        try {
          __db.beginTransaction();
          try {
            _stmt.executeUpdateDelete();
            __db.setTransactionSuccessful();
            return Unit.INSTANCE;
          } finally {
            __db.endTransaction();
          }
        } finally {
          __preparedStmtOfDeleteOlderThan.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Object deleteOldest(final int n, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfDeleteOldest.acquire();
        int _argIndex = 1;
        _stmt.bindLong(_argIndex, n);
        try {
          __db.beginTransaction();
          try {
            _stmt.executeUpdateDelete();
            __db.setTransactionSuccessful();
            return Unit.INSTANCE;
          } finally {
            __db.endTransaction();
          }
        } finally {
          __preparedStmtOfDeleteOldest.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Flow<List<PostEntity>> getTimeline() {
    final String _sql = "SELECT * FROM cached_posts ORDER BY createdAtTimestamp DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"cached_posts"}, new Callable<List<PostEntity>>() {
      @Override
      @NonNull
      public List<PostEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfUserId = CursorUtil.getColumnIndexOrThrow(_cursor, "userId");
          final int _cursorIndexOfUserName = CursorUtil.getColumnIndexOrThrow(_cursor, "userName");
          final int _cursorIndexOfUserAvatar = CursorUtil.getColumnIndexOrThrow(_cursor, "userAvatar");
          final int _cursorIndexOfText = CursorUtil.getColumnIndexOrThrow(_cursor, "text");
          final int _cursorIndexOfPics = CursorUtil.getColumnIndexOrThrow(_cursor, "pics");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "createdAt");
          final int _cursorIndexOfCreatedAtTimestamp = CursorUtil.getColumnIndexOrThrow(_cursor, "createdAtTimestamp");
          final int _cursorIndexOfLikesCount = CursorUtil.getColumnIndexOrThrow(_cursor, "likesCount");
          final int _cursorIndexOfCommentsCount = CursorUtil.getColumnIndexOrThrow(_cursor, "commentsCount");
          final int _cursorIndexOfRepostsCount = CursorUtil.getColumnIndexOrThrow(_cursor, "repostsCount");
          final int _cursorIndexOfSource = CursorUtil.getColumnIndexOrThrow(_cursor, "source");
          final int _cursorIndexOfIsRetweet = CursorUtil.getColumnIndexOrThrow(_cursor, "isRetweet");
          final int _cursorIndexOfRetweetUserId = CursorUtil.getColumnIndexOrThrow(_cursor, "retweetUserId");
          final int _cursorIndexOfRetweetUserName = CursorUtil.getColumnIndexOrThrow(_cursor, "retweetUserName");
          final int _cursorIndexOfRetweetText = CursorUtil.getColumnIndexOrThrow(_cursor, "retweetText");
          final int _cursorIndexOfRetweetPics = CursorUtil.getColumnIndexOrThrow(_cursor, "retweetPics");
          final int _cursorIndexOfCachedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "cachedAt");
          final List<PostEntity> _result = new ArrayList<PostEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final PostEntity _item;
            final String _tmpId;
            if (_cursor.isNull(_cursorIndexOfId)) {
              _tmpId = null;
            } else {
              _tmpId = _cursor.getString(_cursorIndexOfId);
            }
            final String _tmpUserId;
            if (_cursor.isNull(_cursorIndexOfUserId)) {
              _tmpUserId = null;
            } else {
              _tmpUserId = _cursor.getString(_cursorIndexOfUserId);
            }
            final String _tmpUserName;
            if (_cursor.isNull(_cursorIndexOfUserName)) {
              _tmpUserName = null;
            } else {
              _tmpUserName = _cursor.getString(_cursorIndexOfUserName);
            }
            final String _tmpUserAvatar;
            if (_cursor.isNull(_cursorIndexOfUserAvatar)) {
              _tmpUserAvatar = null;
            } else {
              _tmpUserAvatar = _cursor.getString(_cursorIndexOfUserAvatar);
            }
            final String _tmpText;
            if (_cursor.isNull(_cursorIndexOfText)) {
              _tmpText = null;
            } else {
              _tmpText = _cursor.getString(_cursorIndexOfText);
            }
            final List<String> _tmpPics;
            final String _tmp;
            if (_cursor.isNull(_cursorIndexOfPics)) {
              _tmp = null;
            } else {
              _tmp = _cursor.getString(_cursorIndexOfPics);
            }
            _tmpPics = __stringListConverter.toList(_tmp);
            final String _tmpCreatedAt;
            if (_cursor.isNull(_cursorIndexOfCreatedAt)) {
              _tmpCreatedAt = null;
            } else {
              _tmpCreatedAt = _cursor.getString(_cursorIndexOfCreatedAt);
            }
            final long _tmpCreatedAtTimestamp;
            _tmpCreatedAtTimestamp = _cursor.getLong(_cursorIndexOfCreatedAtTimestamp);
            final int _tmpLikesCount;
            _tmpLikesCount = _cursor.getInt(_cursorIndexOfLikesCount);
            final int _tmpCommentsCount;
            _tmpCommentsCount = _cursor.getInt(_cursorIndexOfCommentsCount);
            final int _tmpRepostsCount;
            _tmpRepostsCount = _cursor.getInt(_cursorIndexOfRepostsCount);
            final String _tmpSource;
            if (_cursor.isNull(_cursorIndexOfSource)) {
              _tmpSource = null;
            } else {
              _tmpSource = _cursor.getString(_cursorIndexOfSource);
            }
            final boolean _tmpIsRetweet;
            final int _tmp_1;
            _tmp_1 = _cursor.getInt(_cursorIndexOfIsRetweet);
            _tmpIsRetweet = _tmp_1 != 0;
            final String _tmpRetweetUserId;
            if (_cursor.isNull(_cursorIndexOfRetweetUserId)) {
              _tmpRetweetUserId = null;
            } else {
              _tmpRetweetUserId = _cursor.getString(_cursorIndexOfRetweetUserId);
            }
            final String _tmpRetweetUserName;
            if (_cursor.isNull(_cursorIndexOfRetweetUserName)) {
              _tmpRetweetUserName = null;
            } else {
              _tmpRetweetUserName = _cursor.getString(_cursorIndexOfRetweetUserName);
            }
            final String _tmpRetweetText;
            if (_cursor.isNull(_cursorIndexOfRetweetText)) {
              _tmpRetweetText = null;
            } else {
              _tmpRetweetText = _cursor.getString(_cursorIndexOfRetweetText);
            }
            final List<String> _tmpRetweetPics;
            final String _tmp_2;
            if (_cursor.isNull(_cursorIndexOfRetweetPics)) {
              _tmp_2 = null;
            } else {
              _tmp_2 = _cursor.getString(_cursorIndexOfRetweetPics);
            }
            _tmpRetweetPics = __stringListConverter.toList(_tmp_2);
            final long _tmpCachedAt;
            _tmpCachedAt = _cursor.getLong(_cursorIndexOfCachedAt);
            _item = new PostEntity(_tmpId,_tmpUserId,_tmpUserName,_tmpUserAvatar,_tmpText,_tmpPics,_tmpCreatedAt,_tmpCreatedAtTimestamp,_tmpLikesCount,_tmpCommentsCount,_tmpRepostsCount,_tmpSource,_tmpIsRetweet,_tmpRetweetUserId,_tmpRetweetUserName,_tmpRetweetText,_tmpRetweetPics,_tmpCachedAt);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public Flow<List<PostEntity>> getByUser(final String userId) {
    final String _sql = "SELECT * FROM cached_posts WHERE userId = ? ORDER BY createdAtTimestamp DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    if (userId == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, userId);
    }
    return CoroutinesRoom.createFlow(__db, false, new String[] {"cached_posts"}, new Callable<List<PostEntity>>() {
      @Override
      @NonNull
      public List<PostEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfUserId = CursorUtil.getColumnIndexOrThrow(_cursor, "userId");
          final int _cursorIndexOfUserName = CursorUtil.getColumnIndexOrThrow(_cursor, "userName");
          final int _cursorIndexOfUserAvatar = CursorUtil.getColumnIndexOrThrow(_cursor, "userAvatar");
          final int _cursorIndexOfText = CursorUtil.getColumnIndexOrThrow(_cursor, "text");
          final int _cursorIndexOfPics = CursorUtil.getColumnIndexOrThrow(_cursor, "pics");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "createdAt");
          final int _cursorIndexOfCreatedAtTimestamp = CursorUtil.getColumnIndexOrThrow(_cursor, "createdAtTimestamp");
          final int _cursorIndexOfLikesCount = CursorUtil.getColumnIndexOrThrow(_cursor, "likesCount");
          final int _cursorIndexOfCommentsCount = CursorUtil.getColumnIndexOrThrow(_cursor, "commentsCount");
          final int _cursorIndexOfRepostsCount = CursorUtil.getColumnIndexOrThrow(_cursor, "repostsCount");
          final int _cursorIndexOfSource = CursorUtil.getColumnIndexOrThrow(_cursor, "source");
          final int _cursorIndexOfIsRetweet = CursorUtil.getColumnIndexOrThrow(_cursor, "isRetweet");
          final int _cursorIndexOfRetweetUserId = CursorUtil.getColumnIndexOrThrow(_cursor, "retweetUserId");
          final int _cursorIndexOfRetweetUserName = CursorUtil.getColumnIndexOrThrow(_cursor, "retweetUserName");
          final int _cursorIndexOfRetweetText = CursorUtil.getColumnIndexOrThrow(_cursor, "retweetText");
          final int _cursorIndexOfRetweetPics = CursorUtil.getColumnIndexOrThrow(_cursor, "retweetPics");
          final int _cursorIndexOfCachedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "cachedAt");
          final List<PostEntity> _result = new ArrayList<PostEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final PostEntity _item;
            final String _tmpId;
            if (_cursor.isNull(_cursorIndexOfId)) {
              _tmpId = null;
            } else {
              _tmpId = _cursor.getString(_cursorIndexOfId);
            }
            final String _tmpUserId;
            if (_cursor.isNull(_cursorIndexOfUserId)) {
              _tmpUserId = null;
            } else {
              _tmpUserId = _cursor.getString(_cursorIndexOfUserId);
            }
            final String _tmpUserName;
            if (_cursor.isNull(_cursorIndexOfUserName)) {
              _tmpUserName = null;
            } else {
              _tmpUserName = _cursor.getString(_cursorIndexOfUserName);
            }
            final String _tmpUserAvatar;
            if (_cursor.isNull(_cursorIndexOfUserAvatar)) {
              _tmpUserAvatar = null;
            } else {
              _tmpUserAvatar = _cursor.getString(_cursorIndexOfUserAvatar);
            }
            final String _tmpText;
            if (_cursor.isNull(_cursorIndexOfText)) {
              _tmpText = null;
            } else {
              _tmpText = _cursor.getString(_cursorIndexOfText);
            }
            final List<String> _tmpPics;
            final String _tmp;
            if (_cursor.isNull(_cursorIndexOfPics)) {
              _tmp = null;
            } else {
              _tmp = _cursor.getString(_cursorIndexOfPics);
            }
            _tmpPics = __stringListConverter.toList(_tmp);
            final String _tmpCreatedAt;
            if (_cursor.isNull(_cursorIndexOfCreatedAt)) {
              _tmpCreatedAt = null;
            } else {
              _tmpCreatedAt = _cursor.getString(_cursorIndexOfCreatedAt);
            }
            final long _tmpCreatedAtTimestamp;
            _tmpCreatedAtTimestamp = _cursor.getLong(_cursorIndexOfCreatedAtTimestamp);
            final int _tmpLikesCount;
            _tmpLikesCount = _cursor.getInt(_cursorIndexOfLikesCount);
            final int _tmpCommentsCount;
            _tmpCommentsCount = _cursor.getInt(_cursorIndexOfCommentsCount);
            final int _tmpRepostsCount;
            _tmpRepostsCount = _cursor.getInt(_cursorIndexOfRepostsCount);
            final String _tmpSource;
            if (_cursor.isNull(_cursorIndexOfSource)) {
              _tmpSource = null;
            } else {
              _tmpSource = _cursor.getString(_cursorIndexOfSource);
            }
            final boolean _tmpIsRetweet;
            final int _tmp_1;
            _tmp_1 = _cursor.getInt(_cursorIndexOfIsRetweet);
            _tmpIsRetweet = _tmp_1 != 0;
            final String _tmpRetweetUserId;
            if (_cursor.isNull(_cursorIndexOfRetweetUserId)) {
              _tmpRetweetUserId = null;
            } else {
              _tmpRetweetUserId = _cursor.getString(_cursorIndexOfRetweetUserId);
            }
            final String _tmpRetweetUserName;
            if (_cursor.isNull(_cursorIndexOfRetweetUserName)) {
              _tmpRetweetUserName = null;
            } else {
              _tmpRetweetUserName = _cursor.getString(_cursorIndexOfRetweetUserName);
            }
            final String _tmpRetweetText;
            if (_cursor.isNull(_cursorIndexOfRetweetText)) {
              _tmpRetweetText = null;
            } else {
              _tmpRetweetText = _cursor.getString(_cursorIndexOfRetweetText);
            }
            final List<String> _tmpRetweetPics;
            final String _tmp_2;
            if (_cursor.isNull(_cursorIndexOfRetweetPics)) {
              _tmp_2 = null;
            } else {
              _tmp_2 = _cursor.getString(_cursorIndexOfRetweetPics);
            }
            _tmpRetweetPics = __stringListConverter.toList(_tmp_2);
            final long _tmpCachedAt;
            _tmpCachedAt = _cursor.getLong(_cursorIndexOfCachedAt);
            _item = new PostEntity(_tmpId,_tmpUserId,_tmpUserName,_tmpUserAvatar,_tmpText,_tmpPics,_tmpCreatedAt,_tmpCreatedAtTimestamp,_tmpLikesCount,_tmpCommentsCount,_tmpRepostsCount,_tmpSource,_tmpIsRetweet,_tmpRetweetUserId,_tmpRetweetUserName,_tmpRetweetText,_tmpRetweetPics,_tmpCachedAt);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public Object count(final Continuation<? super Integer> $completion) {
    final String _sql = "SELECT COUNT(*) FROM cached_posts";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<Integer>() {
      @Override
      @NonNull
      public Integer call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final Integer _result;
          if (_cursor.moveToFirst()) {
            final Integer _tmp;
            if (_cursor.isNull(0)) {
              _tmp = null;
            } else {
              _tmp = _cursor.getInt(0);
            }
            _result = _tmp;
          } else {
            _result = null;
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, $completion);
  }

  @NonNull
  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }
}
