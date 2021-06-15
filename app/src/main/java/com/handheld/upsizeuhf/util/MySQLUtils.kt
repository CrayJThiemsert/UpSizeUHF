package com.handheld.upsizeuhf.util

import java.sql.*
import java.sql.DriverManager
import java.util.*

class MySQLUtils {
    companion object {
        internal var conn: Connection? = null
        internal var username = "admin" // provide the username
        internal var password = "14mP455w0rd" //

        fun getConnection() : Boolean {
            var result: Boolean = false
            val connectionProps = Properties()
            connectionProps.put("user", username)
            connectionProps.put("password", password)
            try {
//                Class.forName("com.mysql.jdbc.Driver").newInstance()
                Class.forName("com.mysql.cj.jdbc.Driver").newInstance()

                // "127.0.0.1" +
                // "192.168.1.101" +
                conn = DriverManager.getConnection(
                        "jdbc:" + "mysql" + "://" +
                                "192.168.1.101" +
                                ":" + "3306" + "/" +
                                "",
                        connectionProps)
                result = true
            } catch (ex: SQLException) {
                // handle any errors
                ex.printStackTrace()
            } catch (ex: Exception) {
                // handle any errors
                ex.printStackTrace()
            } finally {
                return result
            }

        }

        fun executeMySQLQuery(query: String) {
            var stmt: Statement? = null
            var resultset: ResultSet? = null

            try {
                stmt = conn!!.createStatement()
//                resultset = stmt!!.executeQuery("SHOW DATABASES;")
                resultset = stmt!!.executeQuery(query)

//                if (stmt.execute("SHOW DATABASES;")) {
                if (stmt.execute(query)) {
                    resultset = stmt.resultSet
                }

                while (resultset!!.next()) {
                    println(resultset.getString("Database"))
                }
            } catch (ex: SQLException) {
                // handle any errors
                ex.printStackTrace()
            } finally {
                // release resources
                if (resultset != null) {
                    try {
                        resultset.close()
                    } catch (sqlEx: SQLException) {
                    }

                    resultset = null
                }

                if (stmt != null) {
                    try {
                        stmt.close()
                    } catch (sqlEx: SQLException) {
                    }

                    stmt = null
                }

                if (conn != null) {
                    try {
                        conn!!.close()
                    } catch (sqlEx: SQLException) {
                    }

                    conn = null
                }
            }
        }
    }
}