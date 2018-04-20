package com.exam.ojdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @Description: TODO 添加类/接口描述.
 * @Author:lujx
 * @Date:2017年3月9日
 */
public class ProjectTemplate {
    /**
     * @Description: TODO 添加方法功能描述.
     * @Author: xuyajun
     * @Date: 2017年3月8日
     * @param args .
     */
    public static void main(final String[] args) {
        String url = "jdbc:oracle:thin:@192.168.6.232:1521:orcl";
        String user = "hims2017";
        String password = "hims2017";
        String tableName = "candidate";
        String author = "lujx";
        reverse(url, user, password, tableName, author);
    }

    /** @Description: .
     * @Author lujx
     * @Date 2016-12-5
     * @throws
     * @param url .
     * @param user .
     * @param password .
     * @param tableName .
     * @param author .
     */
    private static void reverse(final String url, final String user, final String password, final String tableName, final String author) {
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
            strBuilder.append("   and a.table_name = upper(?)                ");
            String sql = strBuilder.toString();
            pre = con.prepareStatement(sql);
            pre.setString(1, tableName);
            result = pre.executeQuery();
            dealResult(result, author);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (result != null) {
                    result.close();
                }
                if (pre != null) {
                    pre.close();
                }
                if (con != null) {
                    con.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /** @Description: .
     * @Author lujx
     * @Date 2016-12-5
     * @throws
     * @param result .
     * @param author .
     */
    private static void dealResult(final ResultSet result, final String author) {
        try {
            String date = new SimpleDateFormat("yyyy年M月d日").format(new Date());
            StringBuilder strBuilder = new StringBuilder();
            StringBuilder funBuilder = new StringBuilder();
            while (result.next()) {
                String columnName = result.getString("column_name").toLowerCase();
                String dataType = result.getString("data_type").toLowerCase();
                String comments = result.getString("comments");

                String val_1 = "";
                String[] vals = columnName.split("_");
                for (String val : vals) {
                    val = val.substring(0, 1).toUpperCase() + val.substring(1);
                    val_1 += val;
                }
                String val_2 = val_1.substring(0, 1).toLowerCase() + val_1.substring(1);

                if ("varchar2".equals(dataType)) {
                    dataType = "String";
                } else if ("number".equals(dataType)) {
                    dataType = "Decimal";
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

                strBuilder.append("\t").append("/**").append("\n");
                strBuilder.append("\t").append(" * ").append(comments).append(".\n");
                strBuilder.append("\t").append(" */").append("\n");
                strBuilder.append("\t").append("private ").append(dataType).append(" ").append(val_2).append(";").append("\n");
                funBuilder.append("\t").append("/**").append("\n");
                funBuilder.append("\t").append(" * @Description: 获取").append(comments).append(".").append("\n");
                funBuilder.append("\t").append(" * @Author: ").append(author).append("\n");
                funBuilder.append("\t").append(" * @Date: ").append(date).append("\n");
                funBuilder.append("\t").append(" * @return ").append(comments).append("\n");
                funBuilder.append("\t").append(" */").append("\n");
                funBuilder.append("\t").append("public ").append(dataType).append(" get").append(val_1).append("() {").append("\n");
                funBuilder.append("\t").append("    return ").append(val_2).append(";").append("\n");
                funBuilder.append("\t").append("}").append("\n");
                funBuilder.append("\t").append("/**").append("\n");
                funBuilder.append("\t").append(" * @Description: 设置").append(comments).append(".").append("\n");
                funBuilder.append("\t").append(" * @Author: ").append(author).append("\n");
                funBuilder.append("\t").append(" * @Date: ").append(date).append("\n");
                funBuilder.append("\t").append(" * @param ").append(val_2).append(" ").append(comments).append("").append("\n");
                funBuilder.append("\t").append(" */").append("\n");
                funBuilder.append("\t").append("public void set").append(val_1).append("(final ").append(dataType).append(" ").append(val_2).append(") {").append("\n");
                funBuilder.append("\t").append("    this.").append(val_2).append(" = ").append(val_2).append(";").append("\n");
                funBuilder.append("\t").append("}").append("\n");
            }
            System.out.print(strBuilder.toString());
            System.out.print(funBuilder.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
