package com.billy.controller.lib.core;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.text.TextUtils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * SharedPreference存储数据的工具类
 */
@SuppressLint("NewApi")
public class PreferenceUtil {

	private static Context sContext;

	private final int mMode;

	private final String mName;

	public PreferenceUtil(String name, int mode) {
		mName = name;
		mMode = mode;
	}

	public void init(Context context) {
		sContext = context;
	}

	public SharedPreferences open() {
		return sContext.getSharedPreferences(mName, mMode);
	}

	public static boolean commit(SharedPreferences.Editor editor) {
//		if (Build.VERSION.SDK_INT < 9) {
			return editor.commit();
//		}
//		editor.apply();
//		return true;
	}

	public SharedPreference<Boolean> value(String key, Boolean defaultValue) {
		return new SharedPreference<Boolean>(this, key, defaultValue) {

			@Override
			protected Boolean read(SharedPreferences sp) {
				if (sp.contains(mKey)) {
					return sp.getBoolean(mKey, false);
				}
				return mDefaultValue;
			}

			@Override
			protected void write(Editor editor, Boolean value) {
				if (value == null) {
					throw new IllegalArgumentException("null cannot be written for <Boolean>");
				}
				editor.putBoolean(mKey, value);
			}

		};
	}

	public SharedPreference<int[]> value(String key, int[] defaultValue) {
		return new SharedPreference<int[]>(this, key, defaultValue) {
			@Override
			protected int[] read(SharedPreferences sp) {
				if (sp.contains(mKey)) {
					String str = sp.getString(mKey, "");
					if (!TextUtils.isEmpty(str)) {
						String[] arr = str.split("_");
						int[] result = new int[arr.length];
						for (int i = 0; i < arr.length; i++) {
							result[i] = Integer.parseInt(arr[i]);
						}
						return result;
					}
				}
				return mDefaultValue;
			}

			@Override
			protected void write(Editor editor, int[] value) {
				if (value == null) {
					throw new IllegalArgumentException("null cannot be written for <Integer>");
				}
				StringBuilder sb = new StringBuilder();
				for (int i = 0; i < value.length; i++) {
					if (i > 0) {
						sb.append("_");
					}
					sb.append(value[i]);
				}
				editor.putString(mKey, sb.toString());
			}

		};
	}
	public SharedPreference<Integer> value(String key, Integer defaultValue) {
		return new SharedPreference<Integer>(this, key, defaultValue) {

			@Override
			protected Integer read(SharedPreferences sp) {
				if (sp.contains(mKey)) {
					return sp.getInt(mKey, 0);
				}
				return mDefaultValue;
			}

			@Override
			protected void write(Editor editor, Integer value) {
				if (value == null) {
					throw new IllegalArgumentException("null cannot be written for <Integer>");
				}
				editor.putInt(mKey, value);
			}

		};
	}

	public SharedPreference<Long> value(String key, Long defaultValue) {
		return new SharedPreference<Long>(this, key, defaultValue) {

			@Override
			protected Long read(SharedPreferences sp) {
				if (sp.contains(mKey)) {
					return sp.getLong(mKey, 0);
				}
				return mDefaultValue;
			}

			@Override
			protected void write(Editor editor, Long value) {
				if (value == null) {
					throw new IllegalArgumentException("null cannot be written for <Long>");
				}
				editor.putLong(mKey, value);
			}

		};
	}

	public SharedPreference<String> value(String key, String defaultValue) {
		return new SharedPreference<String>(this, key, defaultValue) {

			@Override
			protected String read(SharedPreferences sp) {
				if (sp.contains(mKey)) {
					return sp.getString(mKey, null);
				}
				return mDefaultValue;
			}

			@Override
			protected void write(Editor editor, String value) {
				editor.putString(mKey, value);
			}
		};
	}

	public SharedPreference<List<String>> value(String key, List<String> defaultValue) {

		return new SharedPreference<List<String>>(this, key, defaultValue) {

			@Override
			protected List<String> read(SharedPreferences sp) {
				String str = sp.getString(mKey, null);
				if (null == str) {
					return mDefaultValue;
				}
				String value;
				int index = 0;
				List<String> resultSet = new ArrayList<>();
				while ((value = sp.getString(getKey(index), null)) != null) {
					resultSet.add(value);
					index++;
				}
				return resultSet;
			}

			@Override
			protected void write(Editor editor, List<String> value) {
				//清除旧数据
				int index = 0;
				SharedPreferences sp = mFile.open();
				String key = getKey(index++);
				while (sp.getString(key, null) != null) {
					editor.remove(key);
					key = getKey(index++);
				}
				if (null != value) {
					editor.putString(mKey, mKey);//储存key
					//储存新数据
					Iterator<String> iterator = value.iterator();
					index = 0;
					while (iterator.hasNext()) {
						editor.putString(getKey(index), iterator.next());
						index++;
					}
				}
			}

			@Override
			public void remove() {
				Editor editor = mFile.open().edit();
				write(editor, null);//清除数据
				editor.remove(mKey);//移除key
				commit(editor);
			}

			private String getKey(int index) {
				return mKey + "_" + index;
			}
		};
	}

	public abstract class SharedPreference<T> {

		final T mDefaultValue;

		PreferenceUtil mFile;

		final String mKey;

		protected SharedPreference(PreferenceUtil file, String key, T defaultValue) {
			mFile = file;
			mKey = key;
			mDefaultValue = defaultValue;
		}

		public final boolean exists() {
			return mFile.open().contains(mKey);
		}

		public final T get() {
			return read(mFile.open());
		}

		public final String getKey() {
			return mKey;
		}

		public final void put(T value) {
			SharedPreferences sp = mFile.open();
			SharedPreferences.Editor editor = sp.edit();
			write(editor, value);
			commit(editor);
		}

		public void remove() {
			commit(mFile.open().edit().remove(mKey));
		}

		protected abstract T read(SharedPreferences sp);

		protected abstract void write(SharedPreferences.Editor editor, T value);
	}
}
