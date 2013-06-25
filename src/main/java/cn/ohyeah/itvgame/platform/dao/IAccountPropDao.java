package cn.ohyeah.itvgame.platform.dao;

import java.util.List;

import cn.ohyeah.itvgame.platform.model.AccountProp;
import cn.ohyeah.itvgame.platform.viewmodel.OwnPropDesc;

public interface IAccountPropDao {
	public void save(AccountProp accountProp);
	public void update(AccountProp accountProp);
	public AccountProp read(int accountId, int productId, int propId);
	public List<OwnPropDesc> queryAccountPropList(int accountId, int productId);
	public void useProps(int accountId, int productId, int[] propIds, int[] nums);
	public void synProps(int accountId, int productId, int[] propIds, int[] counts);
}
