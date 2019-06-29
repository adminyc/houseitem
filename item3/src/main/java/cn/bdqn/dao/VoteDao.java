package cn.bdqn.dao;

import cn.bdqn.pojo.Options;
import cn.bdqn.pojo.Subject;

import java.util.List;
import java.util.Map;

public interface VoteDao {
    public List<Map<String, Object>> selectAll(Map<String, Object> map);

    public List<Map<String, Object>> findVoteBySid(int sid);

    public List<Map<String, Object>> itemByNumber(int sid);

    public int findItemTotalCount(int sid);

    public int addVote(Map<String, Object> map);

    public int verifyVote(Map<String, Object> mp);

    public int insertsubject(Subject subject);

    public int selectBySid(Subject subject);

    public int insertoptions(Options options);

    public int updateoptions(Options options);

    public int updatesubject(Subject subject);

    public int deleteoptions(int oid);

    public List<Map<String, Object>> selectmaintain(int sid);

    public int removesubject(int sid);

    public int removeoptions(int sid);

    public int removeitem(int sid);

    public int removeitembyoid(int sid);

}
