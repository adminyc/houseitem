package cn.bdqn.pojo;

import lombok.*;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Users {
    private int uid;
    private String username;
    private String password;
    private int isAdmin;
}
