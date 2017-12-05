package com.pulan.util;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by puLan on 2016/12/8.
 */
public class SQLiteHelper {
    //数据库操作对象
    private SQLiteDatabase db;
    //数据库存储路径
    private String path;
    //表名称
    private String table;
    //sql语句
    private String sql;

    /**
     * 创建数据库
     *
     * @param path 数据库存储路径
     */
    public void createDB(String path) {
        this.db = SQLiteDatabase.openOrCreateDatabase(path, null);
    }

    /**
     * 创建数据表
     *
     * @param table 数据表表名称
     */
    public void createTable(String table) {
        if (this.db != null) {
        }
    }

    /**
     * 创建数据表
     *
     * @param sql sql语句
     */
    public void createTableBySQL(String sql) {
        if (this.db != null) {
            this.db.execSQL(sql);
        }
    }

    /**
     * 向指定表中插入数据
     *
     * @param table  指定表名称
     * @param cloumn 字段列名称
     * @param values 值
     */
    public void insert(String table, String cloumn, ContentValues values) {
        if (this.db != null) {
            this.db.insert(table, cloumn, values);
        }
    }

    public SQLiteDatabase getDb() {
        return db;
    }

    public void setDb(SQLiteDatabase db) {
        this.db = db;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getTable() {
        return table;
    }

    public void setTable(String table) {
        this.table = table;
    }

    public String getSql() {
        return sql;
    }

    public void setSql(String sql) {
        this.sql = sql;
    }
}
