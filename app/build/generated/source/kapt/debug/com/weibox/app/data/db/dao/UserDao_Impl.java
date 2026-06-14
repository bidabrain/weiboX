package com.weibox.app.data.db.dao;

import android.database.Cursor;
import android.os.CancellationSignal;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.CoroutinesRoom;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.SharedSQLiteStatement;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import com.weibox.app.data.db.entity.UserEntity;
import java.lang.Boolean;
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
public final class UserDao_Impl implements UserDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<UserEntity> __insertionAdapterOfUserEntity;

  private final SharedSQLiteStatement __preparedStmtOfDelete;

  public UserDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfUserEntity = new EntityInsertionAdapter<UserEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `followed_users` (`id`,`screenName`,`description`,`avatarUrl`,`coverUrl`,`followersCount`,`followCount`,`statusesCount`,`verified`,`verifiedReason`,`followedAt`) VALUES (?,?,?,?,?,?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final UserEntity entity) {
        if (entity.getId() == null) {
          statement.bindNull(1);
        } else {
          statement.bindString(1, entity.getId());
        }
        if (entity.getScreenName() == null) {
          statement.bindNull(2);
        } else {
          statement.bindString(2, entity.getScreenName());
        }
        if (entity.getDescription() == null) {
          statement.bindNull(3);
        } else {
          statement.bindString(3, entity.getDescription());
        }
        if (entity.getAvatarUrl() == null) {
          statement.bindNull(4);
        } else {
          statement.bindString(4, entity.getAvatarUrl());
        }
        if (entity.getCoverUrl() == null) {
          statement.bindNull(5);
        } else {
          statement.bindString(5, entity.getCoverUrl());
        }
        if (entity.getFollowersCount() == null) {
          statement.bindNull(6);
        } else {
          statement.bindString(6, entity.getFollowersCount());
        }
        statement.bindLong(7, entity.getFollowCount());
        statement.bindLong(8, entity.getStatusesCount());
        final int _tmp = entity.getVerified() ? 1 : 0;
        statement.bindLong(9, _tmp);
        if (entity.getVerifiedReason() == null) {
          statement.bindNull(10);
        } else {
          statement.bindString(10, entity.getVerifiedReason());
        }
        statement.bindLong(11, entity.getFollowedAt());
      }
    };
    this.__preparedStmtOfDelete = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM followed_users WHERE id = ?";
        return _query;
      }
    };
  }

  @Override
  public Object insert(final UserEntity user, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfUserEntity.insert(user);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object delete(final String userId, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfDelete.acquire();
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
          __preparedStmtOfDelete.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Flow<List<UserEntity>> getAll() {
    final String _sql = "SELECT * FROM followed_users ORDER BY followedAt DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"followed_users"}, new Callable<List<UserEntity>>() {
      @Override
      @NonNull
      public List<UserEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfScreenName = CursorUtil.getColumnIndexOrThrow(_cursor, "screenName");
          final int _cursorIndexOfDescription = CursorUtil.getColumnIndexOrThrow(_cursor, "description");
          final int _cursorIndexOfAvatarUrl = CursorUtil.getColumnIndexOrThrow(_cursor, "avatarUrl");
          final int _cursorIndexOfCoverUrl = CursorUtil.getColumnIndexOrThrow(_cursor, "coverUrl");
          final int _cursorIndexOfFollowersCount = CursorUtil.getColumnIndexOrThrow(_cursor, "followersCount");
          final int _cursorIndexOfFollowCount = CursorUtil.getColumnIndexOrThrow(_cursor, "followCount");
          final int _cursorIndexOfStatusesCount = CursorUtil.getColumnIndexOrThrow(_cursor, "statusesCount");
          final int _cursorIndexOfVerified = CursorUtil.getColumnIndexOrThrow(_cursor, "verified");
          final int _cursorIndexOfVerifiedReason = CursorUtil.getColumnIndexOrThrow(_cursor, "verifiedReason");
          final int _cursorIndexOfFollowedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "followedAt");
          final List<UserEntity> _result = new ArrayList<UserEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final UserEntity _item;
            final String _tmpId;
            if (_cursor.isNull(_cursorIndexOfId)) {
              _tmpId = null;
            } else {
              _tmpId = _cursor.getString(_cursorIndexOfId);
            }
            final String _tmpScreenName;
            if (_cursor.isNull(_cursorIndexOfScreenName)) {
              _tmpScreenName = null;
            } else {
              _tmpScreenName = _cursor.getString(_cursorIndexOfScreenName);
            }
            final String _tmpDescription;
            if (_cursor.isNull(_cursorIndexOfDescription)) {
              _tmpDescription = null;
            } else {
              _tmpDescription = _cursor.getString(_cursorIndexOfDescription);
            }
            final String _tmpAvatarUrl;
            if (_cursor.isNull(_cursorIndexOfAvatarUrl)) {
              _tmpAvatarUrl = null;
            } else {
              _tmpAvatarUrl = _cursor.getString(_cursorIndexOfAvatarUrl);
            }
            final String _tmpCoverUrl;
            if (_cursor.isNull(_cursorIndexOfCoverUrl)) {
              _tmpCoverUrl = null;
            } else {
              _tmpCoverUrl = _cursor.getString(_cursorIndexOfCoverUrl);
            }
            final String _tmpFollowersCount;
            if (_cursor.isNull(_cursorIndexOfFollowersCount)) {
              _tmpFollowersCount = null;
            } else {
              _tmpFollowersCount = _cursor.getString(_cursorIndexOfFollowersCount);
            }
            final int _tmpFollowCount;
            _tmpFollowCount = _cursor.getInt(_cursorIndexOfFollowCount);
            final int _tmpStatusesCount;
            _tmpStatusesCount = _cursor.getInt(_cursorIndexOfStatusesCount);
            final boolean _tmpVerified;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfVerified);
            _tmpVerified = _tmp != 0;
            final String _tmpVerifiedReason;
            if (_cursor.isNull(_cursorIndexOfVerifiedReason)) {
              _tmpVerifiedReason = null;
            } else {
              _tmpVerifiedReason = _cursor.getString(_cursorIndexOfVerifiedReason);
            }
            final long _tmpFollowedAt;
            _tmpFollowedAt = _cursor.getLong(_cursorIndexOfFollowedAt);
            _item = new UserEntity(_tmpId,_tmpScreenName,_tmpDescription,_tmpAvatarUrl,_tmpCoverUrl,_tmpFollowersCount,_tmpFollowCount,_tmpStatusesCount,_tmpVerified,_tmpVerifiedReason,_tmpFollowedAt);
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
  public Object getAllIds(final Continuation<? super List<String>> $completion) {
    final String _sql = "SELECT id FROM followed_users";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<List<String>>() {
      @Override
      @NonNull
      public List<String> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final List<String> _result = new ArrayList<String>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final String _item;
            if (_cursor.isNull(0)) {
              _item = null;
            } else {
              _item = _cursor.getString(0);
            }
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, $completion);
  }

  @Override
  public Object getById(final String userId, final Continuation<? super UserEntity> $completion) {
    final String _sql = "SELECT * FROM followed_users WHERE id = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    if (userId == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, userId);
    }
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<UserEntity>() {
      @Override
      @Nullable
      public UserEntity call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfScreenName = CursorUtil.getColumnIndexOrThrow(_cursor, "screenName");
          final int _cursorIndexOfDescription = CursorUtil.getColumnIndexOrThrow(_cursor, "description");
          final int _cursorIndexOfAvatarUrl = CursorUtil.getColumnIndexOrThrow(_cursor, "avatarUrl");
          final int _cursorIndexOfCoverUrl = CursorUtil.getColumnIndexOrThrow(_cursor, "coverUrl");
          final int _cursorIndexOfFollowersCount = CursorUtil.getColumnIndexOrThrow(_cursor, "followersCount");
          final int _cursorIndexOfFollowCount = CursorUtil.getColumnIndexOrThrow(_cursor, "followCount");
          final int _cursorIndexOfStatusesCount = CursorUtil.getColumnIndexOrThrow(_cursor, "statusesCount");
          final int _cursorIndexOfVerified = CursorUtil.getColumnIndexOrThrow(_cursor, "verified");
          final int _cursorIndexOfVerifiedReason = CursorUtil.getColumnIndexOrThrow(_cursor, "verifiedReason");
          final int _cursorIndexOfFollowedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "followedAt");
          final UserEntity _result;
          if (_cursor.moveToFirst()) {
            final String _tmpId;
            if (_cursor.isNull(_cursorIndexOfId)) {
              _tmpId = null;
            } else {
              _tmpId = _cursor.getString(_cursorIndexOfId);
            }
            final String _tmpScreenName;
            if (_cursor.isNull(_cursorIndexOfScreenName)) {
              _tmpScreenName = null;
            } else {
              _tmpScreenName = _cursor.getString(_cursorIndexOfScreenName);
            }
            final String _tmpDescription;
            if (_cursor.isNull(_cursorIndexOfDescription)) {
              _tmpDescription = null;
            } else {
              _tmpDescription = _cursor.getString(_cursorIndexOfDescription);
            }
            final String _tmpAvatarUrl;
            if (_cursor.isNull(_cursorIndexOfAvatarUrl)) {
              _tmpAvatarUrl = null;
            } else {
              _tmpAvatarUrl = _cursor.getString(_cursorIndexOfAvatarUrl);
            }
            final String _tmpCoverUrl;
            if (_cursor.isNull(_cursorIndexOfCoverUrl)) {
              _tmpCoverUrl = null;
            } else {
              _tmpCoverUrl = _cursor.getString(_cursorIndexOfCoverUrl);
            }
            final String _tmpFollowersCount;
            if (_cursor.isNull(_cursorIndexOfFollowersCount)) {
              _tmpFollowersCount = null;
            } else {
              _tmpFollowersCount = _cursor.getString(_cursorIndexOfFollowersCount);
            }
            final int _tmpFollowCount;
            _tmpFollowCount = _cursor.getInt(_cursorIndexOfFollowCount);
            final int _tmpStatusesCount;
            _tmpStatusesCount = _cursor.getInt(_cursorIndexOfStatusesCount);
            final boolean _tmpVerified;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfVerified);
            _tmpVerified = _tmp != 0;
            final String _tmpVerifiedReason;
            if (_cursor.isNull(_cursorIndexOfVerifiedReason)) {
              _tmpVerifiedReason = null;
            } else {
              _tmpVerifiedReason = _cursor.getString(_cursorIndexOfVerifiedReason);
            }
            final long _tmpFollowedAt;
            _tmpFollowedAt = _cursor.getLong(_cursorIndexOfFollowedAt);
            _result = new UserEntity(_tmpId,_tmpScreenName,_tmpDescription,_tmpAvatarUrl,_tmpCoverUrl,_tmpFollowersCount,_tmpFollowCount,_tmpStatusesCount,_tmpVerified,_tmpVerifiedReason,_tmpFollowedAt);
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

  @Override
  public Flow<Boolean> isFollowed(final String userId) {
    final String _sql = "SELECT EXISTS(SELECT 1 FROM followed_users WHERE id = ?)";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    if (userId == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, userId);
    }
    return CoroutinesRoom.createFlow(__db, false, new String[] {"followed_users"}, new Callable<Boolean>() {
      @Override
      @NonNull
      public Boolean call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final Boolean _result;
          if (_cursor.moveToFirst()) {
            final Integer _tmp;
            if (_cursor.isNull(0)) {
              _tmp = null;
            } else {
              _tmp = _cursor.getInt(0);
            }
            _result = _tmp == null ? null : _tmp != 0;
          } else {
            _result = null;
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

  @NonNull
  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }
}
