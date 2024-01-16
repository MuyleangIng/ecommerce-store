package co.cstad.sen.api.users;


import org.apache.ibatis.jdbc.SQL;

public class UserProvider {
    private static final String  userTbl= "users";

    public static String buildSelectAllUsersSql(){
        return new SQL(){{
            SELECT("*");
            FROM(userTbl);
        }}.toString();
    }

    public static String buildSelectUserByUuidSql(){
        return new SQL(){{
            SELECT("*");
            FROM(userTbl);
            WHERE("uuid = #{uuid}", "is_disabled = false");
        }}.toString();
    }

    public static String buildCreateUserSql(){
        return new SQL(){{
            INSERT_INTO(userTbl);
            VALUES("uuid","u.uuid");
            VALUES("avatar","u.avatar");
            VALUES("firstName","u.first_name");
            VALUES("lastName","u.last_name");
            VALUES("username","u.username");
            VALUES("email","u.email");
            VALUES("password","u.password");
        }}.toString();
    }

    public static String buildUpdateUserByUuidSql(){
        return new SQL(){{
            UPDATE(userTbl);
            SET("avatar = #{u.avatar}");
            SET("firstName = #{u.first_name}");
            SET("lastName = #{u.last_name}");
            SET("username = #{u.username}");
            SET("email = #{u.email}");
            SET("password = #{u.password}");
            WHERE("uuid = #{u.uuid}","is_disabled=false");
        }}.toString();
    }

    public static String buildDeleteUserByUuidSql(){
        return new SQL(){{
            UPDATE(userTbl);
            SET("is_disabled = true");
            WHERE("uuid = #{uuid}" ,"is_disabled = false");
        }}.toString();
    }
}
