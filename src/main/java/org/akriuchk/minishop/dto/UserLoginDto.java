package org.akriuchk.minishop.dto;

import lombok.Data;

@Data
public class UserLoginDto {
    String username;
    String password;
    /**
     * auth:
     *  1. separate icon to login
     *  2. Edit & upload buttons only for admin
     *  3. Edit and upload APIs are guarded for admin user
     *  4. Token is available for 1 year
     *  5.
     *
     */
}
