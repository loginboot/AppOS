package com.xsw.dao;

import java.math.BigDecimal;
import java.sql.Blob;
import java.sql.CallableStatement;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import org.apache.log4j.Logger;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.CallableStatementCallback;
import org.springframework.jdbc.core.CallableStatementCreator;
import org.springframework.jdbc.core.support.JdbcDaoSupport;

/**
 * 
 * @author loginboot.vicp.net
 * 
 * @cretor xiesw
 * @version 1.0.0
 * @date 2015-01-02
 * @description JDBC操作封装类
 *
 */

public class JdbcDao extends JdbcDaoSupport {
    /**
     * 日志
     */
    private static Logger log = Logger.getLogger(JdbcDao.class);

    /**
     * 使用Native SQL去查询结果
     * 
     * @param sql
     * @return List
     */
    public List<Map<String, Object>> queryByNativeSQL(String sql) {
        return getJdbcTemplate().queryForList(sql);
    }

    /**
     * 使用Native SQL去查询结果，条件通过参数传入
     * 
     * @param sql
     * @param args
     * @return
     */
    public List<Map<String, Object>> queryByNativeSQL(String sql, Object... args) {
        return getJdbcTemplate().queryForList(sql, args);
    }

    /**
     * 使用Native SQL去更新记录
     * 
     * @param sql
     * @return int
     */
    public int updateByNativeSQL(String sql) {
        return getJdbcTemplate().update(sql);
    }

    /**
     * 使用Native SQL去更新，条件通过参数传入
     * 
     * @param sql
     * @param args
     * @return
     */
    public int updateByNativeSQL(String sql, Object... args) {
        return getJdbcTemplate().update(sql, args);
    }

    /**
     * 调用带输出参数的存储过程
     * 
     * @param spName 存储过程名
     * @param inVals 输入参数
     * @param outTypes 输出参数类型
     * @return Vector<Object>输出结果值
     */
    public Vector<Object> execStoreProcedureWithOutput(final String spName, final Vector<Object> inVals,
            final Vector<Integer> outTypes) {
        return getJdbcTemplate().execute(new CallableStatementCreator() {

            @Override
            public CallableStatement createCallableStatement(Connection conn) throws SQLException {
                String procName = "";
                procName = spName;
                procName += "(";
                // in values
                for (int i = 0; i < inVals.size(); i++) {
                    procName += "?";
                    if (i != inVals.size() - 1)
                        procName += ",";
                }
                // out values
                for (int i = 0; i < outTypes.size(); i++) {
                    if (inVals.size() > 0 || i > 0) {
                        procName += ",";
                    }
                    procName += "?";
                }
                procName += ")";
                String storedProc = "{call " + procName + "}";// 调用的sql
                log.info("execStoreProcedure - " + storedProc);
                CallableStatement cs = conn.prepareCall(storedProc);
                setStoredProcedureValues(cs, inVals);
                // output
                for (int i = 0; i < outTypes.size(); i++) {
                    cs.registerOutParameter(inVals.size() + i + 1, outTypes.get(i));
                }
                return cs;
            }
        }, new CallableStatementCallback<Vector<Object>>() {
            @Override
            public Vector<Object> doInCallableStatement(CallableStatement cs) throws SQLException, DataAccessException {
                log.info("Running the Stored Procedure " + spName + "--" + cs.execute());
                // output
                Vector<Object> out = new Vector<Object>();
                for (int i = 0; i < outTypes.size(); i++) {
                    out.add(cs.getObject(inVals.size() + i + 1));
                }
                return out;
            }
        });
    }

    /**
     * 调用有返回结果集的存储过程
     * 
     * @param spName 存储过程名称
     * @param inVals 输入参数
     * @return 结果集
     */
    public List<Map<String, Object>> queryStoreProcedure(final String spName, final Vector<Object> inVals) {
        return getJdbcTemplate().execute(new CallableStatementCreator() {

            @Override
            public CallableStatement createCallableStatement(Connection conn) throws SQLException {
                String procName = "";
                procName = spName;
                procName += "(";
                // in values
                for (int i = 0; i < inVals.size(); i++) {
                    procName += "?";
                    if (i != inVals.size() - 1)
                        procName += ",";
                }
                procName += ")";
                String storedProc = "{call " + procName + "}";// 调用的sql
                log.info("execStoreProcedure - " + storedProc);
                CallableStatement cs = conn.prepareCall(storedProc);
                setStoredProcedureValues(cs, inVals);
                return cs;
            }
        }, new CallableStatementCallback<List<Map<String, Object>>>() {
            @Override
            public List<Map<String, Object>> doInCallableStatement(CallableStatement cs) throws SQLException,
                    DataAccessException {
                List<Map<String, Object>> list = new Vector<Map<String, Object>>();
                ResultSet rs = cs.executeQuery();
                while (rs.next()) {
                    ResultSetMetaData rsmd = rs.getMetaData();
                    Map<String, Object> row = new LinkedHashMap<String, Object>();
                    for (int i = 1; i <= rsmd.getColumnCount(); i++) {
                        String colName = rsmd.getColumnName(i);
                        row.put(colName, rs.getObject(colName));
                    }
                    list.add(row);
                }
                return list;
            }
        });
    }

    /**
     * 调用有返回结果集的存储过程
     * 
     * @param spName 存储过程名称
     * @param inVals 输入参数
     * @param outpus 输出参数
     * @return 结果集
     */
    public List<Map<String, Object>> queryStoreProcedure(final String spName, final Vector<Object> inVals,
            final LinkedHashMap<Integer, Object> outputs) {
        return getJdbcTemplate().execute(new CallableStatementCreator() {
            @Override
            public CallableStatement createCallableStatement(Connection conn) throws SQLException {
                String procName = "";
                procName = spName;
                procName += "(";
                // in values
                for (int i = 0; i < inVals.size(); i++) {
                    if (i > 0) {
                        procName += ",";
                    }
                    procName += "?";
                }
                // out values
                for (int i = 0; i < outputs.size(); i++) {
                    if (inVals.size() > 0 || i > 0) {
                        procName += ",";
                    }
                    procName += "?";
                }
                procName += ")";
                String storedProc = "{call " + procName + "}";// 调用的sql
                log.info("execStoreProcedure - " + storedProc);
                CallableStatement cs = conn.prepareCall(storedProc);
                setStoredProcedureValues(cs, inVals);
                // output
                registerStoredProcedureOutputs(cs, inVals.size(), outputs);
                return cs;
            }
        }, new CallableStatementCallback<List<Map<String, Object>>>() {
            @Override
            public List<Map<String, Object>> doInCallableStatement(CallableStatement cs) throws SQLException,
                    DataAccessException {
                List<Map<String, Object>> list = new Vector<Map<String, Object>>();
                ResultSet rs = cs.executeQuery();
                while (rs != null && rs.next()) {
                    ResultSetMetaData rsmd = rs.getMetaData();
                    Map<String, Object> row = new LinkedHashMap<String, Object>();
                    for (int i = 1; i <= rsmd.getColumnCount(); i++) {
                        String colName = rsmd.getColumnName(i);
                        row.put(colName, rs.getObject(colName));
                    }
                    list.add(row);
                }
                // output
                for (int i = 0; i < outputs.size(); i++) {
                    outputs.put(i, cs.getObject(inVals.size() + i + 1));
                }
                return list;
            }
        });
    }

    /**
     * 调用无返回结果集的存储过程
     * 
     * @param spName 存储过程名称
     * @param inVals 输入参数
     * @return int
     */
    public int execStoreProcedure(final String spName, final Vector<Object> inVals) {
        return getJdbcTemplate().execute(new CallableStatementCreator() {

            @Override
            public CallableStatement createCallableStatement(Connection conn) throws SQLException {
                String procName = "";
                procName = spName;
                procName += "(";
                // in values
                for (int i = 0; i < inVals.size(); i++) {
                    procName += "?";
                    if (i != inVals.size() - 1)
                        procName += ",";
                }
                procName += ")";
                String storedProc = "{call " + procName + "}";// 调用的sql
                log.info("execStoreProcedure - " + storedProc);
                CallableStatement cs = conn.prepareCall(storedProc);
                setStoredProcedureValues(cs, inVals);
                return cs;
            }
        }, new CallableStatementCallback<Integer>() {
            @Override
            public Integer doInCallableStatement(CallableStatement cs) throws SQLException, DataAccessException {
                return cs.executeUpdate();
            }
        });
    }

    private void registerStoredProcedureOutputs(CallableStatement cs, int insize, LinkedHashMap<Integer, Object> outputs)
            throws SQLException {
        for (int i = 0; i < outputs.size(); i++) {
            Object tmpVal = outputs.get(i);
            if (tmpVal instanceof Integer) {
                cs.registerOutParameter(insize + i + 1, Types.INTEGER);
            } else if (tmpVal instanceof Double) {
                cs.registerOutParameter(insize + i + 1, Types.DOUBLE);
            } else if (tmpVal instanceof Float) {
                cs.registerOutParameter(insize + i + 1, Types.FLOAT);
            } else if (tmpVal instanceof String) {
                cs.registerOutParameter(insize + i + 1, Types.NVARCHAR);
            } else if (tmpVal instanceof Boolean) {
                cs.registerOutParameter(insize + i + 1, Types.BOOLEAN);
            } else if (tmpVal instanceof Long) {
                cs.registerOutParameter(insize + i + 1, Types.DECIMAL);
            } else if (tmpVal instanceof BigDecimal) {
                cs.registerOutParameter(insize + i + 1, Types.BIGINT);
            } else if (tmpVal instanceof Short) {
                cs.registerOutParameter(insize + i + 1, Types.SMALLINT);
            } else if (tmpVal instanceof Date) {
                cs.registerOutParameter(insize + i + 1, Types.DATE);
            } else if (tmpVal instanceof Byte) {
                cs.registerOutParameter(insize + i + 1, Types.BINARY);
            } else if (tmpVal instanceof byte[]) {
                cs.registerOutParameter(insize + i + 1, Types.ARRAY);
            } else if (tmpVal instanceof Clob) {
                cs.registerOutParameter(insize + i + 1, Types.CLOB);
            } else if (tmpVal instanceof Blob) {
                cs.registerOutParameter(insize + i + 1, Types.BLOB);
            } else {
                cs.registerOutParameter(insize + i + 1, Types.JAVA_OBJECT);
            }
        }
    }

    private void setStoredProcedureValues(CallableStatement cs, Vector<Object> inVals) throws SQLException {
        for (int i = 0; i < inVals.size(); i++) {
            Object tmpVal = inVals.get(i);
            if (tmpVal instanceof Integer) {
                cs.setInt(i + 1, (Integer) tmpVal);
            } else if (tmpVal instanceof Double) {
                cs.setDouble(i + 1, (Double) tmpVal);
            } else if (tmpVal instanceof Float) {
                cs.setFloat(i + 1, (Float) tmpVal);
            } else if (tmpVal instanceof String) {
                cs.setString(i + 1, (String) tmpVal);
            } else if (tmpVal instanceof Boolean) {
                cs.setBoolean(i + 1, (Boolean) tmpVal);
            } else if (tmpVal instanceof Long) {
                cs.setLong(i + 1, (Long) tmpVal);
            } else if (tmpVal instanceof BigDecimal) {
                cs.setBigDecimal(i + 1, (BigDecimal) tmpVal);
            } else if (tmpVal instanceof Short) {
                cs.setShort(i + 1, (Short) tmpVal);
            } else if (tmpVal instanceof Date) {
                cs.setDate(i + 1, new java.sql.Date(((Date) tmpVal).getTime()));
            } else if (tmpVal instanceof Byte) {
                cs.setByte(i + 1, (Byte) tmpVal);
            } else if (tmpVal instanceof byte[]) {
                cs.setBytes(i + 1, (byte[]) tmpVal);
            } else if (tmpVal instanceof Clob) {
                cs.setClob(i + 1, (Clob) tmpVal);
            } else if (tmpVal instanceof Blob) {
                cs.setBlob(i + 1, (Blob) tmpVal);
            } else {
                cs.setObject(i + 1, tmpVal);
            }
        }
    }

}
