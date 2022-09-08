package com.yl.controller;

import com.yl.mapper.UserFolderMapper;
import com.yl.mapper.UserMapper;
import com.yl.pojo.User;
import com.yl.utils.CheckUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;

@Controller
public class UserController {
    @Autowired
    UserMapper userMapper;
    @Autowired
    UserFolderMapper userFolderMapper;

    /**
     * 登录页面，有session直接登录
     *
     * @param session
     * @return
     */

    @RequestMapping({"/", "/index.html", "/toLogin"})
    public String toLogin(HttpSession session) {
        if (session.getAttribute("user") != null) {
            return "redirect:/user/home";
        }
        return "index";
    }

    /**
     * @param username 用户名
     * @param password 密码
     * @param model    传递信息
     * @param session  登录成功后保存的信息
     * @return
     */

    @PostMapping("/login")
    public String login(String username, String password, Model model, HttpSession session) {
        //登录
        Subject subject = SecurityUtils.getSubject();
        UsernamePasswordToken token = new UsernamePasswordToken(username, password);
        try {
            subject.login(token);
            session.setAttribute("user", userMapper.queryByUsername(username));
            return "redirect:/user/home";
        } catch (UnknownAccountException e) {
            model.addAttribute("msg", "该用户不存在");
            return "index";
        } catch (IncorrectCredentialsException e) {
            model.addAttribute("msg", "密码错误");
            return "index";
        }
    }

    /**
     * 注册
     * @return
     */
    @RequestMapping("/toRegist")
    public String toRegist() {
        return "regist";
    }

    @RequestMapping("/regist")
    public String regist(String username, String password, String phone, Model model, String code, HttpSession session) {
//        String codeNum = (String) session.getAttribute("codeNum");
        if (password == null || "".equals(password)) {
            model.addAttribute("msg", "密码不能为空");
            return "regist";
        }
//        if (phone == null || "".equals(phone)) {
//            model.addAttribute("msg", "手机号不能为空");
//            return "regist";
//        }
//        if (code == null || "".equals(code)) {
//            model.addAttribute("msg", "验证码不能为空");
//            return "regist";
//        }
//        if (!CheckUtils.isChinaPhoneLegal(phone)) {
//            model.addAttribute("msg", "号码格式不规范");
//            return "regist";
//        }
        if (!CheckUtils.checkName(username)) {
            model.addAttribute("msg", "用户名为6-10位，包含字母、数字且不能以数字开头");
            return "regist";
        }
        if (!CheckUtils.checkPwd(password)) {
            model.addAttribute("msg", "密码为6-12位，包含字母、数字或下划线");
            return "regist";
        }
//        if (!codeNum.equals(code)) {
//            model.addAttribute("msg", "验证码错误");
//            return "regist";
//        }
        if (!"".equals(username)) {
            if (userMapper.queryByUsername(username) == null) {
                userMapper.addUser(username, password, phone);
                Subject subject = SecurityUtils.getSubject();

                UsernamePasswordToken token = new UsernamePasswordToken(username, password, phone);
                subject.login(token);

                User user = userMapper.queryByUsername(username);
                user.setNickname(username);
                userMapper.updateUser(user);

                session.setAttribute("user", user);
                return "redirect:/user/home";
            } else {
                model.addAttribute("msg", "用户名已存在");
                return "regist";
            }
        } else {
            model.addAttribute("msg", "用户名不能为空");
            return "regist";
        }
    }

    /**
     * 忘记密码
     *
     * @param username
     * @param phone
     * @return
     */

    @RequestMapping("/submitForget")
    public String forget(String username, String phone, String code, Model model, HttpSession session) {
        User user = userMapper.queryByUsername(username);
        String codeNum = (String) session.getAttribute("codeNum");
        if (user == null) {
            model.addAttribute("msg", "该用户不存在");
            return "forget";
        }
        if (phone.equals(user.getPhone())) {
            if (codeNum.equals(code)) {
                Subject subject = SecurityUtils.getSubject();
                UsernamePasswordToken token = new UsernamePasswordToken(username, user.getPassword());
                subject.login(token);
                session.setAttribute("user", userMapper.queryByUsername(username));
                return "user/changePassword";
            } else {
                model.addAttribute("msg", "验证码错误");
                return "forget";
            }
        } else {
            model.addAttribute("msg", "用户名与手机号不匹配");
            return "forget";
        }
    }

    /**
     * 修改密码
     *
     * @return
     */

    @RequestMapping("/user/changePassword")
    public String changePassword() {
        return "user/changePassword";
    }

    @PostMapping("/user/submitPassword")
    public String submitPassword(String password1, String password2, Model model, HttpSession session) {
        if (password1.equals(password2)) {
            User user = new User();
            user.setPassword(password1);
            User user1 = (User) session.getAttribute("user");
            user.setUserId(user1.getUserId());
            userMapper.updateUser(user);
            session.setAttribute("user", userMapper.queryById(user.getUserId()));
            return "redirect:/user/home";
        } else {
            model.addAttribute("error", "两次输入的密码不一致");
            return "user/changePassword";
        }
    }

    /**
     * 填写用户信息
     *
     * @return
     */

    @RequestMapping("/user/information")
    public String userInfo() {
        return "user/information";
    }

    /**
     * 填写用户安全信息
     *
     * @return
     */

    @RequestMapping("/user/safeInformation")
    public String safeInformation() {
        return "user/safeInformation";
    }

    /**
     * 提交用户信息并返回首页
     * @return
     */

    @PostMapping("/user/toSubmit")
    public String toSubmit(User user, HttpSession session){
        User user1 = (User) session.getAttribute("user");
        user.setUserId(user1.getUserId());
        user.setLevel(user1.getLevel());
        userMapper.updateUser(user);
        session.setAttribute("user", userMapper.queryById(user.getUserId()));

        return "redirect:/user/home";
    }

    @PostMapping("/user/toSubmitPrivate")
    public String toSubmitPrivate(String privatePass1, String privatePass2, String privateStatus, Model model, HttpSession session){
        System.out.println(privatePass1+"   "+privatePass2+"    "+privateStatus);
        User user = (User) session.getAttribute("user");
        if(!privatePass1.equals(privatePass2)){
            model.addAttribute("error","两次输入的密码不一致");
            return "user/safeInformation";
        }
        if("off".equals(privateStatus)){
            user.setPrivatePass(null);
            user.setPrivateStatus(0);
            userMapper.updateUser(user);
            return "redirect:/user/home";
        } else {
            user.setPrivateStatus(1);
            user.setPrivatePass(privatePass1);
            userMapper.updateUser(user);
            return "user/home";
        }
    }

    @GetMapping("/user/logout")
    public String logout(){
        Subject subject = SecurityUtils.getSubject();
        subject.logout();
        return "index.html";
    }

    @GetMapping("/user/backPage")
    public String backPage(HttpSession session){
        User user = (User) session.getAttribute("user");
        System.out.println(user.getUserId());

        Integer nowId = (Integer) session.getAttribute("uploadId");
        System.out.println(nowId);

        Integer parentId = userFolderMapper.queryParentId(user.getUserId(), nowId);
        System.out.println(parentId);

        if(parentId != 0){
            return "redirect:/user/home/" + parentId;
        }
        return "redirect:/user/home";
    }

}
