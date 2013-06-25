package cn.halcyon.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class SqlFileUtil {
	/**
	 * 读取 SQL 文件，获取 SQL 语句
	 * @param sqlFile SQL 脚本文件
	 * @return List<sql> 返回所有 SQL 语句的 List
	 * @throws IOException 
	 */
	public static List<String> loadSql(String sqlFile) throws IOException {
			List<String> sqlList = new ArrayList<String>();
			InputStream sqlFileIs = SqlFileUtil.class.getResourceAsStream(sqlFile);
			
			try {
				byte[] data = new byte[sqlFileIs.available()];
				int readLen = 0;
				int curReadLen = 0;
				while (readLen < data.length) {
					curReadLen = sqlFileIs.read(data, readLen, data.length-readLen);
					if (curReadLen > 0) {
						readLen += curReadLen;
					}
				}
				// Windows 下换行是 \r\n, Linux 下是 \n
				String[] sqlArr = new String(data, "GBK").split("(;\\s*\\r\\n)|(;\\s*\\n)");
				for (int i = 0; i < sqlArr.length; i++) {
					String sql = sqlArr[i].replaceAll("--.*", "").trim();
					if (!sql.equals("")) {
						sqlList.add(sql);
					}
				}
				return sqlList;
			}
			finally {
				if (sqlFileIs != null) {
					sqlFileIs.close();
				}
			}
	} 
}
