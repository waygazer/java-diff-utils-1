/*
 * Copyright 2022 hp.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.trejhara;

/**
 *
 * @author hp
 */
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.sql.DataSource;

public class DataSourceTest {

    public static void main(String[] args) {

        //testDataSource("mysql");
        //System.out.println("**********");
        testDataSource("oracle");

    }

    private static void testDataSource(String dbType) {
        DataSource ds = null;
        if ("mysql".equals(dbType)) {
            ds = MyDataSourceFactory.getMySQLDataSource();
        } else if ("oracle".equals(dbType)) {
            ds = MyDataSourceFactory.getOracleDataSource();
        } else {
            System.out.println("invalid db type");
            return;
        }

        Connection con = null;
        Statement stmt = null;
        ResultSet rs = null;
        try {
            con = ds.getConnection();
            stmt = con.createStatement();
            rs = stmt.executeQuery("ALTER SESSION SET CONTAINER=XEPDB1");
            rs = stmt.executeQuery("SELECT EMPID, NAME FROM EMPLOYEE");
            while (rs.next()) {
                System.out.println("Employee ID=" + rs.getInt("EMPID") + ", Name=" + rs.getString("NAME"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (stmt != null) {
                    stmt.close();
                }
                if (con != null) {
                    con.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

}
