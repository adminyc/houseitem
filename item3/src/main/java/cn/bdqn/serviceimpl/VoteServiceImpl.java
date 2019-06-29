package cn.bdqn.serviceimpl;

import cn.bdqn.dao.UserDao;
import cn.bdqn.dao.VoteDao;
import cn.bdqn.pojo.Options;
import cn.bdqn.pojo.Subject;
import cn.bdqn.pojo.Users;
import cn.bdqn.service.VoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Transactional
@Service
public class VoteServiceImpl implements VoteService {
    @Autowired
    private UserDao ud;
    @Autowired
    private VoteDao vd;

    @Override
    public Users loginUsers(Users us) {
        return ud.verifyUsers(us);
    }

    @Override
    public boolean verusername(String username) {
        boolean ck = false;
        int i = ud.vername(username);
        if (i > 0) {
            //已存在用户
            ck = true;
        }
        return ck;
    }

    @Override
    public int addUsers(Users us) {
        return ud.insertUsers(us);
    }

    @Override
    public List<Map<String, Object>> listBox(Map<String, Object> map) {
        return vd.selectAll(map);
    }

    @Override
    public List<Map<String, Object>> serverFindVoteBySid(int sid) {
        return vd.findVoteBySid(sid);
    }

    @Override
    public List<Map<String, Object>> showItem(int sid) {
        return vd.itemByNumber(sid);
    }

    @Override
    public int totalItemCount(int sid) {
        return vd.findItemTotalCount(sid);
    }

    @Override
    public int addVote(Map<String, Object> map) {
        return vd.addVote(map);
    }

    @Override
    public int verifyVote(Map<String, Object> mp) {
        return vd.verifyVote(mp);
    }

    @Override
    public int addSubject(Subject subject) {
        return vd.insertsubject(subject);
    }

    @Override
    public int findBySid(Subject subject) {
        return vd.selectBySid(subject);
    }

    @Override
    public int addOptions(Options options) {
        return vd.insertoptions(options);
    }

    @Override
    public int updateoptions(Options options) {
        return vd.updateoptions(options);
    }

    @Override
    public int updatesubject(Subject subject) {
        return vd.updatesubject(subject);
    }

    @Override
    public int deleteoptions(int oid) {
        return vd.deleteoptions(oid);
    }

    @Override
    public List<Map<String, Object>> findMaintain(int sid) {
        return vd.selectmaintain(sid);
    }

    @Override
    public int removeoptions(int sid) {
        return vd.removeoptions(sid);
    }

    @Override
    public int removesubject(int sid) {
        return vd.removesubject(sid);
    }

    @Override
    public int removeitem(int sid) {
        return vd.removeitem(sid);
    }

    @Override
    public int removeitembyoid(int oid) {
        return vd.removeitembyoid(oid);
    }
}
