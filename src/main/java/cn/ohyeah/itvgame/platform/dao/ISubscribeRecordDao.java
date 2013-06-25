package cn.ohyeah.itvgame.platform.dao;

import java.util.List;

import cn.ohyeah.itvgame.platform.model.SubscribeRecord;
import cn.ohyeah.itvgame.platform.viewmodel.SubscribeDesc;

public interface ISubscribeRecordDao {
	public void save(SubscribeRecord sr);
	public List<SubscribeDesc> querySubscribeDescList(String userId, int productId, int offset, int length);
	public long querySubscribeList(String userId, int productId);
}
