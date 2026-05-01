package com.mall.LongTou.dto;

import lombok.Data;
import org.hibernate.validator.constraints.Length;
import javax.validation.constraints.NotBlank;

@Data
public class UserLoginDTO {
    @NotBlank(message = "用户名不能为空")
    @Length(min = 4, max = 20, message = "用户名长度4-20位")
    private String username;

    @NotBlank(message = "密码不能为空")
    @Length(min = 6, max = 32, message = "密码长度6-32位")
    private String password;
}

