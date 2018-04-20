package com.exam.hims;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;

public class AddData {
	
	
	public static void main(String[] args) {	
		String url = "jdbc:oracle:thin:@192.168.6.232:1521:orcl";
		String user = "hims2017";
		String password = "hims2017";
		Connection connection = null;
		PreparedStatement statement = null;
		String project = "cnepm2016";
		StringBuilder strB = new StringBuilder();
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
			connection = DriverManager.getConnection(url, user, password);
			
			updateCandidate(connection, statement, project, strB);
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (statement != null)
					statement.close();
				if (connection != null)
					connection.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}		
	}
	//PROJECT
	private static void addProject(Connection connection, PreparedStatement statement,String project,StringBuilder strB) throws SQLException {
		//创建表空间
		String dataspace = "TS_HIMS_" + project.toUpperCase();
		strB.setLength(0);
		strB.append("create tablespace ").append(dataspace).append(" datafile 'F:/oracledata/")
		.append(dataspace).append(".dbf'size 10M autoextend on next 5M");
		statement = connection.prepareStatement(strB.toString());
		statement.executeQuery();
		//创建表分区
		String partition = "P_PROJECT_ID_" + project.toUpperCase();
		String[] tables = {"ORGANIZATION","CANDIDATE","REGISTER","ANSWER","EXAMRECORD","SCORE","BLACKLIST","MARKLINE","PAPER"};
		for (String table : tables) {
			strB.setLength(0);
			strB.append("alter table ").append(table).append(" add partition ").append(partition).append(" values ('").append(project.toUpperCase()).append("') tablespace ").append(dataspace);
			statement = connection.prepareStatement(strB.toString());
			statement.executeQuery();			
		}	
	}
	//CANDIDATE
	private static void addCandidate(Connection connection, PreparedStatement statement, String project,StringBuilder strB) throws SQLException, ParseException {
		for (int i = 0; i < 4; i++) {
		StringBuilder sqlBuff = new StringBuilder();
		sqlBuff.append("insert into candidate                                                                   ");
		sqlBuff.append("  select seq_candidate_id.nextval candidateId,                                          ");
		sqlBuff.append("         'CNEPM2016' projectId,                                                          ");
		sqlBuff.append("         'NO' || seq_candidate_id.nextval archiveno,                                    ");
		sqlBuff.append("         'stu' || seq_candidate_id.nextval name,                                        ");
		sqlBuff.append("         '1' cardtypeCode,                                                              ");
		sqlBuff.append("         '身份证' cardtypeName,                                                         ");
		sqlBuff.append("         '123' cardno,                                                                     ");
		sqlBuff.append("         decode(trunc(dbms_random.value(0, 2)), 0, '男', '女') gender,                  ");
		sqlBuff.append("         null birthdate,                                                                  ");
		sqlBuff.append("         '01' nationCode,                                                               ");
		sqlBuff.append("         '汉族' nationName,                                                             ");
		sqlBuff.append("         '中国' nationality,                                                            ");
		sqlBuff.append("         '大陆' nationalityRemark,                                                      ");
		sqlBuff.append("         '良好' healthCondition,                                                          ");
		sqlBuff.append("         '100' qualifyCode,                                                               ");
		sqlBuff.append("         '初级（士）' qualifyName,                                                        ");
		sqlBuff.append("         2 yearofqualify,                                                                 ");
		sqlBuff.append("         null certifyTypeCode,                                                            ");
		sqlBuff.append("         null certifyTypeName,                                                            ");
		sqlBuff.append("         '北京市海淀区' || seq_candidate_id.nextval address,                              ");
		sqlBuff.append("         '1' || trunc(dbms_random.value(10000, 99999)) postcode,                          ");
		sqlBuff.append("         '135' || trunc(dbms_random.value(10000000, 99999999)) mobile,                    ");
		sqlBuff.append("         '010-' || trunc(dbms_random.value(1000000, 9999999)) tel,                        ");
		sqlBuff.append("         'stu' || seq_candidate_id.nextval || '@163.com' email,                           ");
		sqlBuff.append("         '海淀医院' || trunc(dbms_random.value(100, 999)) orgname,                        ");
		sqlBuff.append("         '6500' orgpropertyCode,                                                          ");
		sqlBuff.append("         '私有企业' orgpropertyName,                                                      ");
		sqlBuff.append("         '4555' orgBelongCode,                                                            ");
		sqlBuff.append("         '北京' orgBelongName,                                                            ");
		sqlBuff.append("         2 yearofwork,                                                                     ");
		sqlBuff.append("         '1' workingSign,                                                                  ");
		sqlBuff.append("         add_months(sysdate, -12 * 2) workTime,                                            ");
		sqlBuff.append("         '朝阳医院' practice,                                                              ");
		sqlBuff.append("         '13654' schoolCode,                                                               ");
		sqlBuff.append("         '北京大学' schoolName,                                                            ");
		sqlBuff.append("         '综合性大学' schoolRemark,                                                        ");
		sqlBuff.append("         trunc(dbms_random.value(1, 6)) byzyCode,                                          ");
		sqlBuff.append("         '专业' || trunc(dbms_random.value(1, 6)) byzyName,                                ");
		sqlBuff.append("         '科技' byzyRemark,                                                                ");
		sqlBuff.append("         add_months(sysdate, -12 * 3) certtime,                                            ");
		sqlBuff.append("         '3' edulevelCode,                                                                 ");
		sqlBuff.append("         '本科' edulevelName,                                                              ");
		sqlBuff.append("         trunc(dbms_random.value(10000, 99999)) eduCertNo,                                 ");
		sqlBuff.append("         '1' degreeCode,                                                                   ");
		sqlBuff.append("         '学士' degreeName,                                                                ");
		sqlBuff.append("         '4' eduYearCode,                                                                  ");
		sqlBuff.append("         '四年' eduYearName,                                                               ");
		sqlBuff.append("         null eduHistory,                                                                  ");
		sqlBuff.append("         '3' xlCode,                                                                       ");
		sqlBuff.append("         '本科' xlName,                                                                    ");
		sqlBuff.append("         trunc(dbms_random.value(0, 2)) graduatingStudent,                                 ");
		sqlBuff.append("         trunc(dbms_random.value(0, 2)) historySign,                                       ");
		sqlBuff.append("         trunc(dbms_random.value(0, 3)) passStatus,                                        ");
		sqlBuff.append("         trunc(dbms_random.value(1, 4)) regionCode,                                        ");
		sqlBuff.append("         trunc(dbms_random.value(0, 2)) examineeType,                                      ");
		sqlBuff.append("         trunc(dbms_random.value(10000, 99999)) || seq_candidate_id.nextval examineeno,    ");
		sqlBuff.append("         jb.subject_id subJbId,                                                            ");
		sqlBuff.append("         jb.subject_name subJbName,                                                        ");
		sqlBuff.append("         zy.subject_id subZyId,                                                            ");
		sqlBuff.append("         zy.subject_name subZyName,                                                        ");
		sqlBuff.append("         null kwZyCode,                                                                    ");
		sqlBuff.append("         null kwZyName,                                                                    ");
		sqlBuff.append("         bmd.org_id orgBmdId,                                                              ");
		sqlBuff.append("         bmd.org_name orgBmdName,                                                          ");
		sqlBuff.append("         shi.org_id orgShijiId,                                                            ");
		sqlBuff.append("         shi.org_name orgShijiName,                                                        ");
		sqlBuff.append("         sheng.org_id orgShengjiId,                                                        ");
		sqlBuff.append("         sheng.org_name orgShengjiName,                                                    ");
		sqlBuff.append("         '1' compmode,                                                                     ");
		sqlBuff.append("         '现场报名' compmodeName,                                                          ");
		sqlBuff.append("         1 isValid                                                                         ");
		sqlBuff.append("    from subject      jb,                                                                  ");
		sqlBuff.append("         subject      zy,                                                                  ");
		sqlBuff.append("         organization bmd,                                                                 ");
		sqlBuff.append("         organization shi,                                                                 ");
		sqlBuff.append("         organization sheng                                                                ");
		sqlBuff.append("   where jb.sub_level = '2'                                                                ");
		sqlBuff.append("     and zy.parent_id = jb.subject_id                                                      ");
		sqlBuff.append("     and zy.subject_standard_code < 300                                                    ");
		sqlBuff.append("     and sheng.org_level = '2'                                                             ");
		sqlBuff.append("     and shi.parent_id = sheng.org_id                                                      ");
		sqlBuff.append("     and bmd.parent_id = shi.org_id                                                        ");
		sqlBuff.append("     and bmd.project_id = 'CNEPM2016'                                                        ");
		statement = connection.prepareStatement(sqlBuff.toString());
		int cnt = statement.executeUpdate();
		System.out.println(cnt);			
		}		
	}

	//ANSWER
	private static void addAnswer(Connection connection, PreparedStatement statement, String project,StringBuilder strB) throws SQLException {
		StringBuilder sqlBuff = new StringBuilder();
		statement = connection.prepareStatement("select distinct re.sub_km_id id from register re");
		ResultSet resultSet = statement.executeQuery();
		while (resultSet.next()) {
			String id = resultSet.getString("ID");
			sqlBuff.setLength(0);
			sqlBuff.append("insert into answer                                               ");
			sqlBuff.append("  select seq_answer_id.nextval,                                  ");
			sqlBuff.append("         ca.project_id,                                          ");
			sqlBuff.append("         ca.examineeno,                                          ");
			sqlBuff.append("         ca.sub_zy_id,                                           ");
			sqlBuff.append("         ca.sub_zy_name,                                         ");
			sqlBuff.append("         re.sub_km_id,                                           ");
			sqlBuff.append("         re.sub_km_name,                                         ");
			sqlBuff.append("         p.paper_id,                                             ");
			sqlBuff.append("         p.papername,                                            ");
			sqlBuff.append("         '答题信息' answer,                                      ");
			sqlBuff.append("         re.subject_standard_code,                               ");
			sqlBuff.append("         '1' isValid                                             ");
			sqlBuff.append("    from paper p, candidate ca, register re                      ");
			sqlBuff.append("   where ca.candidate_id = re.candidate_id                       ");
			sqlBuff.append("     and re.sub_km_id = '").append(id.trim()).append("' ");
			statement = connection.prepareStatement(sqlBuff.toString());
			int cnt = statement.executeUpdate();
			System.out.println(cnt);
		}		
	}
	//EXAMRECORD
	private static void addExamRecord(Connection connection, PreparedStatement statement, String project,StringBuilder strB) throws SQLException {
		StringBuilder sqlBF = new StringBuilder();
		sqlBF.append("insert into candidate values(2,'two','123456','noe') ");
		String sql = sqlBF.toString();
		statement = connection.prepareStatement(sql);
		statement.executeQuery();		
	}

	// SCORE
	private static void addScore(Connection connection, PreparedStatement statement, String project, StringBuilder strB) throws SQLException {
		StringBuilder sqlBuff = new StringBuilder();
		statement = connection.prepareStatement("select distinct an.sub_km_id id from answer an");
		ResultSet resultSet = statement.executeQuery();
		while (resultSet.next()) {
			String id = resultSet.getString("ID");
			sqlBuff.setLength(0);
			sqlBuff.append("insert into score                                       ");
			sqlBuff.append("  select seq_score_id.nextval,                          ");
			sqlBuff.append("         ca.project_id,                                 ");
			sqlBuff.append("         ca.archiveno,                                  ");
			sqlBuff.append("         ca.name,                                       ");
			sqlBuff.append("         ca.cardno,                                     ");
			sqlBuff.append("         an.examineeno,                                 ");
			sqlBuff.append("         an.sub_zy_id,                                  ");
			sqlBuff.append("         an.sub_zy_name,                                ");
			sqlBuff.append("         an.sub_km_id,                                  ");
			sqlBuff.append("         an.sub_km_name,                                ");
			sqlBuff.append("         an.paper_id,                                   ");
			sqlBuff.append("         an.papername,                                  ");
			sqlBuff.append("         78,                                            ");
			sqlBuff.append("         100,                                           ");
			sqlBuff.append("         78,                                            ");
			sqlBuff.append("         80,                                            ");
			sqlBuff.append("         85,                                            ");
			sqlBuff.append("         80,                                            ");
			sqlBuff.append("         '' scoreremark,                                ");
			sqlBuff.append("         null,                                          ");
			sqlBuff.append("         null,                                          ");
			sqlBuff.append("         an.subject_standard_code,                      ");
			sqlBuff.append("         '1'                                            ");
			sqlBuff.append("    from candidate ca, answer an                        ");
			sqlBuff.append("   where ca.examineeno = an.examineeno                  ");
			sqlBuff.append("     and an.sub_km_id = '").append(id.trim()).append("' ");
			statement = connection.prepareStatement(sqlBuff.toString());
			int cnt = statement.executeUpdate();
			System.out.println(cnt);
		}
	}
	//BLACKLIST
	private static void addBlackList(Connection connection, PreparedStatement statement, String project,StringBuilder strB) throws SQLException {
		StringBuilder sqlBF = new StringBuilder();
		sqlBF.append("insert into blacklist values(2,'two','123456','noe') ");
		String sql = sqlBF.toString();
		statement = connection.prepareStatement(sql);
		statement.executeQuery();		
	}
	//MARKLINE
	private static void addMarkLine(Connection connection, PreparedStatement statement, String project,StringBuilder strB) throws SQLException {
		StringBuilder sqlBF = new StringBuilder();
		sqlBF.append("insert into candidate values(2,'two','123456','noe') ");
		String sql = sqlBF.toString();
		statement = connection.prepareStatement(sql);
		statement.executeQuery();		
	}
	//PAPER
	private static void addPaper(Connection connection, PreparedStatement statement, String project,StringBuilder strB) throws SQLException {
		StringBuilder sqlBF = new StringBuilder();
		sqlBF.append("insert into candidate values(2,'two','123456','noe') ");
		String sql = sqlBF.toString();
		statement = connection.prepareStatement(sql);
		statement.executeQuery();		
	}
	//REGISTER
	private static void updateRegister(Connection connection, PreparedStatement statement, String project,StringBuilder strB) throws SQLException, ParseException {
		StringBuilder sqlBuff = new StringBuilder();
		statement = connection.prepareStatement("select distinct an.sub_km_id id from answer an");
		ResultSet resultSet = statement.executeQuery();
		while (resultSet.next()) {
			String id = resultSet.getString("ID");
			sqlBuff.setLength(0);
			sqlBuff.append("insert into register    ");
			sqlBuff.append("  select seq_register_id.nextval register_id,    ");
			sqlBuff.append("         upper(ca.project_id) project_id,    ");
			sqlBuff.append("         ca.candidate_id,    ");
			sqlBuff.append("         ca.sub_jb_name,    ");
			sqlBuff.append("         ca.sub_zy_name,    ");
			sqlBuff.append("         km.subject_id,    ");
			sqlBuff.append("         km.subject_name,    ");
			sqlBuff.append("         km.exam_type,    ");
			sqlBuff.append("         km.exam_type_name,    ");
			sqlBuff.append("         '考场' || seq_register_id.nextval examfld_name,    ");
			sqlBuff.append("         '考场地址' || seq_register_id.nextval examfld_address,    ");
			sqlBuff.append("         '试室' || seq_register_id.nextval examroom_name,    ");
			sqlBuff.append("         trunc(dbms_random.value(1, 99)) seatno,    ");
			sqlBuff.append("         sysdate - 1 exam_starttime,      ");
			sqlBuff.append("         sysdate - 3 exam_endtime,      ");
			sqlBuff.append("         '1' examtimes,      ");
			sqlBuff.append("         km.subject_standard_code      ");
			sqlBuff.append("    from candidate ca, subject km      ");
			sqlBuff.append("   where km.parent_id = ca.sub_zy_id      ");
			sqlBuff.append("     and ca.sub_zy_id = '").append(id.trim()).append("' ");
			statement = connection.prepareStatement(sqlBuff.toString());
			int cnt = statement.executeUpdate();
			System.out.println(cnt);
		}
	}


	//CANDIDATE
	private static void updateCandidate(Connection connection, PreparedStatement statement, String project,StringBuilder strB) throws SQLException, ParseException {
		StringBuilder sqlBuff = new StringBuilder();
        statement = connection.prepareStatement("select distinct ca.org_shiji_id id from candidate ca where ca.birthdate=to_date('19871107','yyyyMMdd') ");
        ResultSet resultSet = statement.executeQuery();
        while (resultSet.next()) {
            String id = resultSet.getString("ID");
            sqlBuff.setLength(0);
            sqlBuff.append("update candidate ca set ca.birthdate = to_date(substr(ca.cardno,7,8),'yyyyMMdd') ");
            sqlBuff.append("      where ca.org_shiji_id =  '").append(id.trim()).append("' ");
            statement = connection.prepareStatement(sqlBuff.toString());
            int cnt = statement.executeUpdate();
            System.out.println(cnt);
        }
	}
	
	//表主键值
	private static Long getSeq(Connection connection, PreparedStatement statement,String table) throws SQLException {
		String sqlStr = "select seq_" + table + "_id.nextval id from dual";
		statement = connection.prepareStatement(sqlStr);
		ResultSet resultSet = statement.executeQuery();
		String columnName = resultSet.getString("id");
		return null;		
	}
	//随机生成身份证号
	public static String getRandomCardNo() {
		String id = "";
		// 随机生成省、自治区、直辖市代码 1-2
		String provinces[] = { "11", "12", "13", "14", "15", "21", "22", "23",
				"31", "32", "33", "34", "35", "36", "37", "41", "42", "43",
				"44", "45", "46", "50", "51", "52", "53", "54", "61", "62",
				"63", "64", "65", "71", "81", "82" };
		String province = provinces[new Random().nextInt(provinces.length - 1)];
		// 随机生成地级市、盟、自治州代码 3-4
		String citys[] = { "01", "02", "03", "04", "05", "06", "07", "08",
				"09", "10", "21", "22", "23", "24", "25", "26", "27", "28" };
		String city = citys[new Random().nextInt(citys.length - 1)];
		// 随机生成县、县级市、区代码 5-6
		String countys[] = { "01", "02", "03", "04", "05", "06", "07", "08",
				"09", "10", "21", "22", "23", "24", "25", "26", "27", "28",
				"29", "30", "31", "32", "33", "34", "35", "36", "37", "38" };
		String county = countys[new Random().nextInt(countys.length - 1)];
		// 随机生成出生年月 7-14
		SimpleDateFormat dft = new SimpleDateFormat("yyyyMMdd");
		Date beginDate = new Date();
		Calendar date = Calendar.getInstance();
		date.setTime(beginDate);
		date.set(Calendar.DATE,
				date.get(Calendar.DATE)- 365*25 - new Random().nextInt(365 * 5));
		String birth = dft.format(date.getTime());
		// 随机生成顺序号 15-17
		String no = new Random().nextInt(999) + "";
		// 随机生成校验码 18
		String checks[] = { "0", "1", "2", "3", "4", "5", "6", "7", "8", "9",
				"X" };
		String check = checks[new Random().nextInt(checks.length - 1)];
		// 拼接身份证号码
		id = province + city + county + birth + no + check;
		return id;
	}
}
