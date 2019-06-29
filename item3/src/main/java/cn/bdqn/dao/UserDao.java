package cn.bdqn.dao;

import cn.bdqn.pojo.Users;

public interface UserDao {
    //用户登录
    public Users verifyUsers(Users us);

    //新增
    public int insertUsers(Users us);

    //注册时判断该用户是否存在
    public int vername(String username);
}
