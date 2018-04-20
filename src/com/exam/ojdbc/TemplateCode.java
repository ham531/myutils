package com.exam.ojdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class TemplateCode {
    
    public static void main(String[] args) {
        String url = "jdbc:oracle:thin:@localhost:1521:orcl";
        String user = "runner";
        String password = "runner";
        String tableName = "data";
        
        System.out.println(tableName + "@" + user + ":");
        reverse(url, user, password, tableName);        
    }

    /** @Description: 查询列名，类型，注释
     * @Author lujx
     * @Date 2016-12-5
     * @throws
     * @param url
     * @param user
     * @param password
     * @param tableName
     */
    public static void reverse(String url, String user, String password, String tableName) {
        Connection con = null;
        PreparedStatement pre = null;
        ResultSet result = null;
        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
            con = DriverManager.getConnection(url, user, password);
            StringBuilder strBuilder = new StringBuilder();
            strBuilder.append("select a.column_name, a.data_type, b.comments ");
            strBuilder.append("  from user_tab_columns a, user_col_comments b");
            strBuilder.append(" where a.table_name = b.table_name            ");
            strBuilder.append("   and a.column_name = b.column_name          ");
            strBuilder.append("   and a.table_name = upper(?)                 ");
            String sql = strBuilder.toString();
            pre = con.prepareStatement(sql);
            pre.setString(1, tableName);
            result = pre.executeQuery();
            dealResult(result);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (result != null)
                    result.close();
                if (pre != null)
                    pre.close();
                if (con != null)
                    con.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /** @Description: 处理查询结果
     * @Author lujx
     * @Date 2016-12-5
     * @throws
     * @param result
     * @param tableName
     */
    public static void dealResult(ResultSet result) {
        try {
            StringBuilder strBuilder = new StringBuilder();
            while (result.next()) {
                String columnName = result.getString("column_name").toLowerCase();
                String dataType = result.getString("data_type").toLowerCase();
                String comments = result.getString("comments");

                String variable = "";
                String[] vars = columnName.split("_");
                for (String var : vars) {
                    var = var.substring(0, 1).toUpperCase() + var.substring(1);
                    variable += var;
                }
                variable = variable.substring(0, 1).toLowerCase() + variable.substring(1);

                if ("varchar2".equals(dataType)) {
                    dataType = "String";
                } else if ("number".equals(dataType)) {
                    dataType = "BigDecimal";
                } else if ("date".equals(dataType)) {
                    dataType = "Date";
                } else if ("timestamp".equals(dataType)) {
                    dataType = "Timestamp";
                } else if ("char".equals(dataType)) {
                    dataType = "String";
                } else if ("blob".equals(dataType)) {
                    dataType = "Blob";
                } else if ("clob".equals(dataType)) {
                    dataType = "Clob";
                } else if ("raw".equals(dataType)) {
                    dataType = "byte[]";
                } else {
                    dataType = "Object";
                }

                strBuilder.append("\t").append("private").append(" ").append(dataType).append(" ");
                strBuilder.append(variable).append(";");
                if (comments != null) {
                    strBuilder.append("  \t//").append(comments.toLowerCase());
                }
                strBuilder.append("\n");
            }
            String str = strBuilder.toString();
            System.out.print(str);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
