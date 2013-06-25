package cn.halcyon.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class SqlFileUtil {
	/**
	 * ��ȡ SQL �ļ�����ȡ SQL ���
	 * @param sqlFile SQL �ű��ļ�
	 * @return List<sql> �������� SQL ���� List
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
				// Windows �»����� \r\n, Linux ���� \n
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
