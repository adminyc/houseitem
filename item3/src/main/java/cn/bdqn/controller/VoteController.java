package cn.bdqn.controller;

import cn.bdqn.dao.VoteDao;
import cn.bdqn.pojo.Options;
import cn.bdqn.pojo.Subject;
import cn.bdqn.pojo.Users;
import cn.bdqn.service.VoteService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.WebApplicationContext;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.text.DecimalFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class VoteController {
    @Autowired
    private VoteService vs;
    @Autowired
    private ServletContext application;

    //用户登录
    @PostMapping("User-login")
    public String user_Login(Users us, Model model, HttpSession session) {//用不了application
        Users users = vs.loginUsers(us);
        if (users != null) {//将用户存入session作用域中，然后跳转到查询
            List<String> listUsers = (List<String>) application.getAttribute("listUsers");
            if (listUsers.contains(users.getUsername())) {
                model.addAttribute("err", "*该用户已经登陆");
                return "login";
            }
            session.setAttribute("user", users);
            listUsers.add(users.getUsername());
            System.out.println("listUsers:" + listUsers);
            return "forward:firstpage";
        }
        model.addAttribute("err", "*用户名或密码错误");
        return "login";
    }

    //Ajax验证姓名是否存在
    @RequestMapping("vername")
    @ResponseBody
    public boolean vername(HttpServletRequest request) {
        String username = request.getParameter("username");
        System.out.println("名字：" + username);
        return vs.verusername(username);
    }

    //用户注册
    @PostMapping("User-register")
    public String user_register(Users us, HttpSession session) {
        int i = vs.addUsers(us);
        if (i > 0) {//将用户存入session作用域中，然后跳转到查询
            session.setAttribute("user", us);
            return "reg_success";
        }
        return "regist";
    }

    //注册、登录成功后进入投票首页 查询所有
    @RequestMapping("firstpage")
    public String gofirstpage(HttpSession session, Model model, @RequestParam(name = "pageIndex", defaultValue = "1") int pageIndex, String dimtitle) {
        //判断是否还在登录状态
        boolean ck = isLogin(session);
        if (ck) {
            return "login";
        }
        Map<String, Object> map = new HashMap<String, Object>();
        //查询所有标记为0
        map.put("flag", 0);
        //在查询所有中模糊查询
        map.put("title", dimtitle);
        Page<Object> page = PageHelper.startPage(pageIndex, 3);
        vs.listBox(map);
        /*for(int i = 0;i< mapList.size();i++){
            System.out.println(mapList.get(i));
        }*/
        model.addAttribute("maplist", page);
        model.addAttribute("map", map);
        return "votelist";
    }

    //维护
    @RequestMapping("maintain")
    public String maintain(HttpSession session, Model model, @RequestParam(name = "pageIndex", defaultValue = "1") int pageIndex, String dimtitle) {
        Map<String, Object> map = new HashMap<String, Object>();
        //查询维护中所有标记不为0
        map.put("flag", 1);
        //在维护中模糊查询
        map.put("title", dimtitle);
        //判断是否还在登录状态
        boolean ck = isLogin(session);
        if (ck) {
            return "login";
        }
        //先判断是否为管理员
        ck = isAdmin(session);
        if (ck) {
            model.addAttribute("error", "*用户权限不足，请联系管理员！");
            return "error";
        }
        Page<Object> page = PageHelper.startPage(pageIndex, 3);
        vs.listBox(map);
        //页面所需数据
        model.addAttribute("maplist", page);
        //用于页面回显
        model.addAttribute("map", map);
        return "votelist";
    }

    //参与投票
    @GetMapping("goPoll/**/{iid}/{sid}/*")
    public String goPoll(Model model, @PathVariable int iid, @PathVariable int sid, HttpSession session) {
        //判断是否还在登录状态
        boolean ck = isLogin(session);
        if (ck) {
            return "login";
        }
        //判断是否有投票
        if (iid == 0) {
            model.addAttribute("error", "当前还没有用户投票！无法查看,需先投票");
        }
        List<Map<String, Object>> list = vs.serverFindVoteBySid(sid);
        model.addAttribute("list", list);
        return "vote_show";
    }

    //添加新投票
    @GetMapping("addVote")
    public String addVote(HttpSession session, Model model) {
        //判断是否还在登录状态
        boolean ck = isLogin(session);
        if (ck) {
            return "login";
        }
        //先判断是否为管理员
        ck = isAdmin(session);
        if (ck) {
            model.addAttribute("error", "*用户权限不足，请联系管理员！");
            return "error";
        }
        return "add";
    }

    //验证是否为管理员的方法
    private boolean isAdmin(HttpSession session) {
        boolean ck = false;
        Users users = (Users) session.getAttribute("user");
        if (users.getIsAdmin() != 1) {
            ck = true;
        }
        return ck;
    }

    //判断是否还在登录状态中
    private boolean isLogin(HttpSession session) {
        boolean ck = false;
        Users users = (Users) session.getAttribute("user");
        System.out.println("user:....." + users);
        if (users == null) {
            ck = true;
        }
        return ck;
    }

    //注销
    @GetMapping("logoff")
    private String logOut(HttpSession session) {
        Users users = (Users) session.getAttribute("user");
        List<String> listUsers = (List<String>) application.getAttribute("listUsers");
        if (users != null) {    //非空判断一下
            if (listUsers.contains(users.getUsername())) {
                //移除
                listUsers.remove(users.getUsername());
            }
        }
        //session.invalidate();
        return "redirect:login.html";
    }

    //查看投票
    @GetMapping("showvote/**/{iid}/{oid}/{title}/{sid}/*/*")
    public String showVote(HttpSession session, Model model, @PathVariable int iid, @PathVariable int oid, @PathVariable String title, @PathVariable int sid) {
        //判断是否还在登录状态
        boolean ck = isLogin(session);
        if (ck) {
            return "forward:/logoff";
        }
        //判断其中的投票数是否为0票
        if (iid == 0) {
            return "redirect:/goPoll/ssc/xnkc/" + iid + "/" + sid + "/6";
        }
        Map<String, Object> map = new HashMap<>();
        map.put("iid", iid);
        map.put("oid", oid);
        map.put("title", title);
        map.put("sid", sid);
        List<Map<String, Object>> list = vs.showItem(sid);
        int totalCount = vs.totalItemCount(sid);
        System.out.println("totalcount:" + totalCount);
        for (int i = 0; i < list.size(); i++) {
            System.out.println("list.get(i).get(iid):" + list.get(i).get("iid"));
            double iidl = Integer.valueOf(list.get(i).get("iid").toString());
            double il = Math.round(iidl / totalCount * 100);
            System.out.println("il百分比:" + il);
            list.get(i).put("percent", il);
            System.out.println("mmmm:" + list.get(i));
        }
        map.put("totalCount", totalCount);
        model.addAttribute("map", map);
        model.addAttribute("list", list);
        return "voteview";
    }

    //参与投票
    @RequestMapping("voteSave")
    public String voteSave(Model model, HttpServletRequest request) {
        System.out.println("参与投票中....");
        Users user = (Users) request.getSession().getAttribute("user");
        String username = user.getUsername();
        //投票列表ID
        String sid = request.getParameter("sid");
        int uid = user.getUid();
        int sida = Integer.valueOf(sid);
        Map<String, Object> mp = new HashMap<>();
        mp.put("uid", uid);
        mp.put("sid", sida);
        int i1 = vs.verifyVote(mp);
        if (i1 != 0) {
            System.out.println("jinle??");
            model.addAttribute("error", "*该用户已经投票，不能重复投票！");
            return "error";
        }
        //选项id
        String oid = request.getParameter("options");
        //标题
        String title = request.getParameter("title");
        //总票数
        Integer iid = Integer.valueOf(request.getParameter("iid"));
        //列表下项目大小
        String size = request.getParameter("size");
        //System.out.println("前username:"+username+",sid:"+sid+",oid"+oid+",title:"+title+",size:"+size+",iid"+iid);
        Map<String, Object> map = new HashMap<>();//投票集合
        map.put("uid", user.getUid());
        map.put("sid", sid);
        map.put("oid", oid);
        int i = vs.addVote(map);
        if (i > 0) {
            System.out.println("新增成功！");
        }
        Map<String, Object> list = new HashMap<>();
        //给票数增加一票
        iid = iid + 1;
        list.put("sid", sid);
        //票项大小
        list.put("oid", size);
        list.put("title", title);
        //总票数量
        list.put("iid", iid);
        //System.out.println("username:"+username+",sid:"+sid+",oid"+oid+",title:"+title+",size:"+oid);
        model.addAttribute("list", list);
        return "success";
    }

    //添加新投票
    @RequestMapping("saveadd")
    public String voteAdd(Subject subject, String[] options) {
        System.out.println(subject);
        int is = vs.addSubject(subject);
        int sid = 0;
        if (is > 0) {
            //获取到新增时主题的自增sid
            sid = vs.findBySid(subject);
        }
        //需要拿到sid  以便插入选项表  选项表中的osid对应每个主题表的sid
        for (int i = 0; i < options.length; i++) {
            Options os = new Options();
            os.setContent(options[i]);
            os.setOsid(sid);
            int io = vs.addOptions(os);
        }
        return "forward:/firstpage";
    }

    //维护页面的回显
    @GetMapping("maintain/**/{sid}/*")
    public String mainTain(Model model, @PathVariable int sid) {
        System.out.println("SID:" + sid);
        List<Map<String, Object>> maps = vs.findMaintain(sid);
        model.addAttribute("maps", maps);
        return "maintain";
    }

    //Ajax根据sid删除某投票项
    @RequestMapping("delbysid")
    @ResponseBody
    public int delVote(HttpServletRequest request) {
        System.out.println("进来了？？");
        Integer sid = Integer.valueOf(request.getParameter("sid"));
        System.out.println("sid:" + sid);
        int rp = vs.removeoptions(sid);
        int rs = vs.removesubject(sid);
        int ri = vs.removeitem(sid);
        int i = 0;
        if (rp > 0 && rs > 0 && ri > 0) {//删三表
            //删除成功！
            i = 1;
        }
        return 1;
    }

    //维护-需新增，修改，删除
    @RequestMapping("subjectsave")
    public String subjectSave(int sid, int type, String[] options, int[] oids) {
        //此处的size是记录原有的选项数量
        System.out.println("type:" + type);
        //记录的oid集合，遍历oids   无论是否删除选项都在 大小长度不变
        for (int i = 0; i < oids.length; i++) {
            System.out.println("遍历内oids[i]" + oids[i]);
            Options ops = new Options();
            if (!("null").equals(options[i])) {//如果遍历的投票选项不为空  则装载入对象
                ops.setOid(oids[i]);
                ops.setContent(options[i]);
                ops.setOsid(sid);//主题ID固定
                System.out.println(i + "我的OPTIONS:" + ops);
                int uoi = vs.updateoptions(ops);//此处对象通通进行修改
                //修改subject中的type
                Subject subject = new Subject();
                subject.setSid(sid);
                subject.setType(type);
                int usd = vs.updatesubject(subject);
                if (uoi > 0 && usd > 0) {
                    System.out.println("修改成功!");
                }
            } else {//表示原有主题被删
                int di = vs.deleteoptions(oids[i]);//拿到对应的原有选项ID将其删除
                int oi = vs.removeitembyoid(oids[i]);
                System.out.println("这是被删选项的ID：" + oids[i]);
                if (di > 0 && oi > 0) {
                    System.out.println("删除成功!");
                }
            }
        }
        if (options.length > oids.length) {//开始新增
            //获取到现有和原有的选项差数
            //int diff = options.length - oids.length;
            for (int i = oids.length; i < options.length; i++) {    //遍历差数
                Options ops = new Options();
                System.out.println("要新增的朋友：" + options[i]);
                ops.setContent(options[i]);
                ops.setOsid(sid);
                System.out.println("opt:" + ops);
                int ai = vs.addOptions(ops);
                if (ai > 0) {
                    System.out.println("新增成功!");
                }
            }
        }
        return "redirect:/maintain";
    }
}
