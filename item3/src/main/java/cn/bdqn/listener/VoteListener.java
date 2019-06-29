package cn.bdqn.listener;

import cn.bdqn.pojo.Users;
import javafx.application.Application;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;
import java.util.ArrayList;
import java.util.List;

@WebListener
public class VoteListener implements ServletContextListener, HttpSessionListener {
    private ServletContext application = null;

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        //application监听器创建
        System.out.println("contextInitialized:");
        application = sce.getServletContext();
        List<String> list = new ArrayList<String>();
        application.setAttribute("listUsers", list);
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        //application监听器销毁
        System.out.println("contextDestroyed:");
    }

    @Override
    public void sessionCreated(HttpSessionEvent se) {
        //session监听器创建
        HttpSession session = se.getSession();
        session.setMaxInactiveInterval(360);
        System.out.println("创建sessionId:" + session.getId());
    }

    @Override
    public void sessionDestroyed(HttpSessionEvent se) {
        //session监听器销毁
        HttpSession session = se.getSession();
        Users users = (Users) session.getAttribute("user");
        List<String> listUsers = (List<String>) application.getAttribute("listUsers");
        System.out.println("监听器里面：" + listUsers + "users:" + users);
        if (users != null) {    //非空判断一下
            if (listUsers.contains(users.getUsername())) {
                //移除
                listUsers.remove(users.getUsername());
            }
        }
        System.out.println("销毁sessionId:" + session.getId());
    }
}
