package cn.bdqn.service;

import cn.bdqn.pojo.Options;
import cn.bdqn.pojo.Subject;
import cn.bdqn.pojo.Users;

import java.util.List;
import java.util.Map;

public interface VoteService {
    //用户登录
    public Users loginUsers(Users us);

    //注册用户
    public int addUsers(Users us);

    //判断用户名是否存在
    public boolean verusername(String username);

    //展示投票列表
    public List<Map<String, Object>> listBox(Map<String, Object> map);

    //根据主题ID查询单条主题
    public List<Map<String, Object>> serverFindVoteBySid(int sid);

    //根据主题ID查询到对应的投票项目
    public List<Map<String, Object>> showItem(int sid);

    //查询总投票项目中的总投票数
    public int totalItemCount(int sid);

    //参与投票
    public int addVote(Map<String, Object> map);

    //验证用户是否投过票
    public int verifyVote(Map<String, Object> mp);

    //添加新投票 主题
    public int addSubject(Subject subject);

    //查询获得添加新投票主题内容的Sid
    public int findBySid(Subject subject);

    //添加新投票 选项
    public int addOptions(Options options);

    //修改维护投票 选项
    public int updateoptions(Options options);

    //修改主题表中的类型
    public int updatesubject(Subject subject);

    //删除维护投票 选项
    public int deleteoptions(int oid);

    //根据sid进入到维护页面进行回显
    public List<Map<String, Object>> findMaintain(int sid);

    //删除投票中的选项
    public int removeoptions(int sid);

    //删除投票中的主题
    public int removesubject(int sid);

    //删除中间投票
    public int removeitem(int sid);

    //删除中间表中oid所对应的
    public int removeitembyoid(int oid);
}
