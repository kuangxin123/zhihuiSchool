package com.kx.myzhxy.pojo;

import lombok.Data;
import org.springframework.boot.web.servlet.ServletRegistrationBean;

/**
 * @ClassName LoginForm
 * @Description TODO
 * @Author kuang
 * @Date 2022/4/16 19:04
 */
@Data
public class LoginForm {
    private String username;
    private String password;
    private String verifiCode;
    private Integer userType;
}
