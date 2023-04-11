# ORMSystem
### ORMSystem管理系统

[TOC]

后端：Springboot + Mybatis-plus + Redis

前端：vue + ElementUI + vue-element-admin + Vuex + Axios 

启动类

```java
package com.oumuanode.ormsystem;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
@MapperScan("com.oumuanode.ormsystem.*.mapper")
public class OrmSystemApplication {

    public static void main(String[] args) {
        SpringApplication.run(OrmSystemApplication.class, args);
    }
    
    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

}

```

配置类

```yaml
server:
  port: 8848

spring:
  datasource:
    username: root
    password: 123456
    url: jdbc:mysql:///xdb
  redis:
    port: 6379
    host: localhost
    password: 123456

logging:
  level:
    com.lantu: debug

mybatis-plus:
  global-config:
    db-config:
      logic-delete-field: deleted
      logic-not-delete-value: 0
      logic-delete-value: 1
  type-aliases-package: com.oumuanode.ormsystem.*.entity
```

响应类

```java
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResponseResult<T> implements Serializable {
    private Integer code;
    private String message;
    private T data;

    public ResponseResult() {
        this.code = AppHttpCodeEnum.SUCCESS.getCode();
        this.message = AppHttpCodeEnum.SUCCESS.getMsg();
    }

    public ResponseResult(Integer code, T data) {
        this.code = code;
        this.data = data;
    }

    public ResponseResult(Integer code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public ResponseResult(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    public static ResponseResult errorResult(int code, String message) {
        ResponseResult result = new ResponseResult();
        return result.error(code, message);
    }
    public static ResponseResult okResult(AppHttpCodeEnum success, String message) {
        ResponseResult result = new ResponseResult();
        return result;
    }

    public static <T> ResponseResult<T> success(String message){
        return new ResponseResult<>(200,message,null);
    }
    public static ResponseResult okResult(int code, String message) {
        ResponseResult result = new ResponseResult();
        return result.ok(code, null, message);
    }

    public static ResponseResult okResult(Object data) {
        ResponseResult result = setAppHttpCodeEnum(AppHttpCodeEnum.SUCCESS, AppHttpCodeEnum.SUCCESS.getMsg());
        if(data!=null) {
            result.setData(data);
        }
        return result;
    }

    public static <T> ResponseResult<T> success(T data){
        return new ResponseResult<>(200,"success",data);
    }

    public static ResponseResult errorResult(AppHttpCodeEnum enums){
        return setAppHttpCodeEnum(enums,enums.getMsg());
    }

    public static ResponseResult errorResult(AppHttpCodeEnum enums, String message){
        return setAppHttpCodeEnum(enums,message);
    }



    private static ResponseResult setAppHttpCodeEnum(AppHttpCodeEnum enums, String message){
        return okResult(enums.getCode(),message);
    }



    public ResponseResult<?> error(Integer code, String message) {
        this.code = code;
        this.message = message;
        return this;
    }

    public ResponseResult<?> ok(Integer code, T data) {
        this.code = code;
        this.data = data;
        return this;
    }

    public ResponseResult<?> ok(Integer code, T data, String message) {
        this.code = code;
        this.data = data;
        this.message = message;
        return this;
    }

    public ResponseResult<?> ok(T data) {
        this.data = data;
        return this;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getmessage() {
        return message;
    }

    public void setmessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

}
```

Vo

```java
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PageVo {
    private List rows;
    private Long total;
}
```

```java
@Data
@Accessors(chain = true)
public class UserVo {
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    private String username;
    private String password;
    private String email;
    private String phone;
    private Integer status;
    private String avatar;
    private Integer deleted;

}
```

```sql
数据库模型

CREATE DATABASE /*!32312 IF NOT EXISTS*/`xdb` /*!40100 DEFAULT CHARACTER SET utf8mb4 */;

USE `xdb`;

/*Table structure for table `x_menu` */

DROP TABLE IF EXISTS `x_menu`;

CREATE TABLE `x_menu` (
  `menu_id` int(11) NOT NULL AUTO_INCREMENT,
  `component` varchar(100) DEFAULT NULL,
  `path` varchar(100) DEFAULT NULL,
  `redirect` varchar(100) DEFAULT NULL,
  `name` varchar(100) DEFAULT NULL,
  `title` varchar(100) DEFAULT NULL,
  `icon` varchar(100) DEFAULT NULL,
  `parent_id` int(11) DEFAULT NULL,
  `is_leaf` varchar(1) DEFAULT NULL,
  `hidden` tinyint(1) DEFAULT NULL,
  PRIMARY KEY (`menu_id`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8mb4;

/*Data for the table `x_menu` */

insert  into `x_menu`(`menu_id`,`component`,`path`,`redirect`,`name`,`title`,`icon`,`parent_id`,`is_leaf`,`hidden`) values (1,'Layout','/sys','/sys/user','sysManage','系统管理','userManage',0,'N',0),(2,'sys/user','user',NULL,'userList','用户列表','user',1,'Y',0),(3,'sys/role','role',NULL,'roleList','角色列表','roleManage',1,'Y',0),(4,'Layout','/test','/test/test1','test','功能测试','form',0,'N',0),(5,'test/test1','test1','','test1','测试点一','form',4,'Y',0),(6,'test/test2','test2','','test2','测试点二','form',4,'Y',0),(7,'test/test3','test3','','test3','测试点三','form',4,'Y',0);

/*Table structure for table `x_role` */

DROP TABLE IF EXISTS `x_role`;

CREATE TABLE `x_role` (
  `role_id` int(11) NOT NULL AUTO_INCREMENT,
  `role_name` varchar(50) DEFAULT NULL,
  `role_desc` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`role_id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4;

/*Data for the table `x_role` */

insert  into `x_role`(`role_id`,`role_name`,`role_desc`) values (1,'admin','超级管理员'),(2,'hr','人事管理员'),(3,'normal','普通员工');

/*Table structure for table `x_role_menu` */

DROP TABLE IF EXISTS `x_role_menu`;

CREATE TABLE `x_role_menu` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `role_id` int(11) DEFAULT NULL,
  `menu_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=22 DEFAULT CHARSET=utf8mb4;

/*Data for the table `x_role_menu` */

insert  into `x_role_menu`(`id`,`role_id`,`menu_id`) values (11,1,1),(12,1,2),(13,1,3),(14,1,4),(15,1,5),(16,1,6),(17,1,7),(18,3,4),(19,3,5),(20,3,6),(21,3,7);

/*Table structure for table `x_user` */

DROP TABLE IF EXISTS `x_user`;

CREATE TABLE `x_user` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `username` varchar(50) NOT NULL,
  `password` varchar(100) DEFAULT NULL,
  `email` varchar(50) DEFAULT NULL,
  `phone` varchar(20) DEFAULT NULL,
  `status` int(1) DEFAULT NULL,
  `avatar` varchar(200) DEFAULT NULL,
  `deleted` int(1) DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=15 DEFAULT CHARSET=utf8;

/*Data for the table `x_user` */

insert  into `x_user`(`id`,`username`,`password`,`email`,`phone`,`status`,`avatar`,`deleted`) values (1,'admin','$2a$10$3ZDHAfyo/P7tSOabB8X.DuVM1DN92YWgSd46ecVY.5WUAQNef7iUC','super@aliyun.com','18677778888',1,'https://wpimg.wallstcn.com/f778738c-e4f8-4870-b634-56703b4acafe.gif',0),(2,'zhangsan','$2a$10$3ZDHAfyo/P7tSOabB8X.DuVM1DN92YWgSd46ecVY.5WUAQNef7iUC','zhangsan@gmail.com','13966667777',1,'https://wpimg.wallstcn.com/f778738c-e4f8-4870-b634-56703b4acafe.gif',0),(3,'lisi','$2a$10$3ZDHAfyo/P7tSOabB8X.DuVM1DN92YWgSd46ecVY.5WUAQNef7iUC','lisi@gmail.com','13966667778',1,'https://wpimg.wallstcn.com/f778738c-e4f8-4870-b634-56703b4acafe.gif',0),(4,'wangwu','$2a$10$3ZDHAfyo/P7tSOabB8X.DuVM1DN92YWgSd46ecVY.5WUAQNef7iUC','wangwu@gmail.com','13966667772',1,'https://wpimg.wallstcn.com/f778738c-e4f8-4870-b634-56703b4acafe.gif',0),(5,'zhaoer','$2a$10$3ZDHAfyo/P7tSOabB8X.DuVM1DN92YWgSd46ecVY.5WUAQNef7iUC','zhaoer@gmail.com','13966667776',1,'https://wpimg.wallstcn.com/f778738c-e4f8-4870-b634-56703b4acafe.gif',0),(6,'songliu','$2a$10$3ZDHAfyo/P7tSOabB8X.DuVM1DN92YWgSd46ecVY.5WUAQNef7iUC','songliu@gmail.com','13966667771',1,'https://wpimg.wallstcn.com/f778738c-e4f8-4870-b634-56703b4acafe.gif',0),(7,'aaa','$2a$10$3ZDHAfyo/P7tSOabB8X.DuVM1DN92YWgSd46ecVY.5WUAQNef7iUC','aaa@aliyun.com','18899998888',1,NULL,1),(8,'bbb','666666','bbb@qq.com','188888889999',1,NULL,1),(9,'ccc','$2a$10$3ZDHAfyo/P7tSOabB8X.DuVM1DN92YWgSd46ecVY.5WUAQNef7iUC','1111@ali.com','11111111',1,NULL,0),(10,'xxx222','$2a$10$dQ1n957Q3qP2D0lHBSqdEeRHym9kAauXgxnIqAv9KDhnsJOC0Kooi','1122@qq.com','111111122',0,NULL,1),(11,'yyy','$2a$10$hX03AhH9Yt//V.crSdqGP.nhj3EQi.cMwIEglT9Eqd.5KDkIGaCQC','1111111@ali.com','1111111',1,NULL,0),(12,'xxx6','$2a$10$LTtlLYIPtDelUtP01FhcSe9JZkNuJmeXf66YMJyZee.04Q1yzNoj.','xxx6@aliyun.com','18677778886',1,NULL,1),(13,'test','$2a$10$jWewP9UNr.6AhrP2ofza1OaTDBq7nBL23C9zHnwBXh8Q/9Vd2zNSC','test@ali.com','11111111111',1,NULL,0),(14,'xxx','$2a$10$QpmItVrXHa5YWTfH25yCEuTu69VFjJbcxBxMYQTWb8qVNBSp0laxq','xxx@ali.com','1111111111111',1,NULL,1);

/*Table structure for table `x_user_role` */

DROP TABLE IF EXISTS `x_user_role`;

CREATE TABLE `x_user_role` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` int(11) DEFAULT NULL,
  `role_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=utf8mb4;

/*Data for the table `x_user_role` */

insert  into `x_user_role`(`id`,`user_id`,`role_id`) values (1,1,1),(2,1,3),(6,3,2),(7,4,2),(8,4,3),(11,2,3);

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

```

#### AdminLoginController

前端

```js
export function login(data) {
  return request({
    url: '/user/login',
    method: 'post',
    data
  })
}

export function getInfo(token) {
  return request({
    url: '/user/info',
    method: 'get',
    params: { token }
  })
}

export function logout() {
  return request({
    url: '/user/logout',
    method: 'post'
  })
}
```

后端

```java
@Api(tags = {"登录接口列表"})
@RestController
@RequestMapping("/user")
public class AdminLoginController {
    @Autowired
    private IUserService userService;


    @PostMapping("/login")
    public ResponseResult login(@RequestBody User user){
        //字符串 不是 null ，并且不为空，而且不能是空白字符
        if (!StringUtils.hasText(user.getUsername())){
            throw new SysException(AppHttpCodeEnum.REQUIRE_USERNAME);
        }
        return userService.login(user);
    }


    @GetMapping("/info")
    public ResponseResult<Map<String,Object>> getUserInfo(@RequestParam("token")String token){
        Map<String,Object> data = userService.getUserInfo(token);
        if (data == null){
            return ResponseResult.errorResult(AppHttpCodeEnum.USERNAME_INVALID);
        }
        return ResponseResult.okResult(data);
    }


    @PostMapping("/logout")
    public ResponseResult<?> logout(@RequestHeader("X-Token") String token){
        //清除token即可
        userService.logout(token);
        return ResponseResult.success("退出成功");
    }

}
```

logout前端写死了

```java
const actions = {
  // user login
  login({ commit }, userInfo) {
    const { username, password } = userInfo
    return new Promise((resolve, reject) => {
      login({ username: username.trim(), password: password }).then(response => {
        const { data } = response
        commit('SET_TOKEN', data.token)
        setToken(data.token)
        resolve()
      }).catch(error => {
        reject(error)
      })
    })
  },

  // get user info
  getInfo({ commit, state }) {
    return new Promise((resolve, reject) => {
      getInfo(state.token).then(response => {
        const { data } = response

        if (!data) {
          reject('Verification failed, please Login again.')
        }

        const { name, avatar, menuList } = data

        commit('SET_NAME', name)
        commit('SET_AVATAR', avatar)
        commit('SET_MENU_LIST', menuList)
        resolve(data)
      }).catch(error => {
        reject(error)
      })
    })
  },

  // user logout
  logout({ commit, state }) {
    return new Promise((resolve, reject) => {
      logout(state.token).then(() => {
        removeToken() // must remove  token  first
        resetRouter()
        commit('RESET_STATE')
        resolve()
      }).catch(error => {
        reject(error)
      })
    })
  },

  // remove token
  resetToken({ commit }) {
    return new Promise(resolve => {
      removeToken() // must remove  token  first
      commit('RESET_STATE')
      resolve()
    })
  }
}
```

```java
public interface IUserService extends IService<User> {


    ResponseResult login(User user);

    ResponseResult logout();

    Map<String, Object> getUserInfo(String token);

    void logout(String token);


    void addUser(User user);

    void updateUser(User user);

    User getUserById(Integer id);

    void deleteUserById(Integer id);
}
```

Service

```java
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Resource
    private UserRoleMapper userRoleMapper;
    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private IMenuService menuService;
    @Autowired
    private RedisCache redisCache;



    @Override
    public ResponseResult login(User user) {
        LambdaQueryWrapper<User> query = new LambdaQueryWrapper<>();
        query.eq(User::getUsername,user.getUsername());
        User loginUser = this.baseMapper.selectOne(query);
        if (loginUser != null && passwordEncoder.matches(user.getPassword(),loginUser.getPassword())) {
            // 存入redis
            redisCache.setCacheObject("login"+loginUser.getPassword(),loginUser.getUsername());
//            loginUser.setPassword(null);
            // 创建jwt
            String token = jwtUtil.createToken(loginUser);

            // 返回数据
            Map<String, Object> data = new HashMap<>();
            data.put("token",token);

            return ResponseResult.success(data);
        }
        return null;

    }

    @Override
    public ResponseResult logout() {
        return null;
    }

    @Override
    public Map<String, Object> getUserInfo(String token) {
        // 根据token获取用户信息，redis
        //Object obj = redisTemplate.opsForValue().get(token);
        User loginUser = null;
        try {
            loginUser = jwtUtil.parseToken(token, User.class);

        } catch (Exception e) {
            e.printStackTrace();
        }
        if(loginUser != null){
            //User loginUser = JSON.parseObject(JSON.toJSONString(obj),User.class);
            Map<String, Object> data = new HashMap<>();
            data.put("name",loginUser.getUsername());
            data.put("avatar", loginUser.getAvatar());

            // 角色
            List<String> roleList = this.baseMapper.getRoleNameByUserId((loginUser.getId()));
            data.put("roles",roleList);

            // 权限列表
            List<Menu> menuList = menuService.getMenuListByUserId(loginUser.getId());
            data.put("menuList",menuList);

            return data;
        }
        return null;
    }

    @Override
    public void logout(String token) {

    }


    @Override
    public void addUser(User user) {
        this.baseMapper.insert(user);
        List<Integer> roleIdList = user.getRoleIdList();
        if (roleIdList != null){
            for (Integer roleId : roleIdList){
                userRoleMapper.insert(new UserRole(null,user.getId(),roleId));
            }
        }
    }

    @Override
    public void updateUser(User user) {
        this.baseMapper.updateById(user);
        // 清除原有角色
        LambdaQueryWrapper<UserRole> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(UserRole::getUserId,user.getId());
        userRoleMapper.delete(wrapper);
        // 设置新的角色
        List<Integer> roleIdList = user.getRoleIdList();
        if(roleIdList != null){
            for (Integer roleId : roleIdList) {
                userRoleMapper.insert(new UserRole(null,user.getId(),roleId));
            }
        }
    }

    @Override
    public User getUserById(Integer id) {
        User user = this.baseMapper.selectById(id);

        LambdaQueryWrapper<UserRole> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(UserRole::getUserId,id);
        List<UserRole> userRoles = userRoleMapper.selectList(queryWrapper);
        List<Integer> collect = userRoles.stream().map(UserRole::getRoleId).collect(Collectors.toList());

        user.setRoleIdList(collect);
        return user;

    }

    @Override
    public void deleteUserById(Integer id) {
        this.baseMapper.deleteById(id);
        // 清除原有角色
        LambdaQueryWrapper<UserRole> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(UserRole::getUserId,id);
        userRoleMapper.delete(wrapper);
    }
}
```

```xml
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE mapper PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN" "http://mybatis.org/dtd/mybatis-3-mapper.dtd">
<mapper namespace="com.oumuanode.ormsystem.sys.mapper.RoleMenuMapper">
    <select id="getMenuIdListByRoleId" parameterType="Integer" resultType="Integer">
        SELECT
            a.`menu_id`
        FROM x_role_menu a, x_menu b
        WHERE a.`menu_id` = b.`menu_id`
          AND b.`is_leaf` = 'Y'
          AND a.`role_id` = #{roleId}
    </select>

</mapper>
```

```java
@Mapper
public interface RoleMenuMapper extends BaseMapper<RoleMenu> {
    public List<Integer> getMenuIdListByRoleId(Integer roleId);

}
```

```java
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE mapper PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN" "http://mybatis.org/dtd/mybatis-3-mapper.dtd">
<mapper namespace="com.oumuanode.ormsystem.sys.mapper.UserMapper">
    <select id="getRoleNameByUserId" parameterType="Integer" resultType="String">
        SELECT
            b.`role_name`
        FROM x_user_role a, x_role b
        WHERE
            a.role_id = b.`role_id`
          AND a.user_id = #{userId}
    </select>

</mapper>
```

```java
@Mapper
public interface UserMapper extends BaseMapper<User> {
    public List<String> getRoleNameByUserId(Integer userId);
}
```



#### MenuController

前端

```js
export default{
  getAllMenu(){
    return request({
      url: '/menu',
      method: 'get'
    });
  }
}
```



后端

```java
@Api(tags = {"菜单接口列表"})
@RestController
@RequestMapping("/menu")
public class MenuController {
    @Autowired
    private IMenuService menuService;

    @ApiOperation("查询所有菜单数据")
    @GetMapping
    public ResponseResult<List<Menu>> getAllMenu(){
        List<Menu> menuList = menuService.getAllMenu();
        return ResponseResult.success(menuList);
    }

}
```



```java
public interface IMenuService extends IService<Menu> {

    List<Menu> getMenuListByUserId(Integer id);

    List<Menu> getAllMenu();
}
```

```java
@Service
public class MenuServiceImpl extends ServiceImpl<MenuMapper, Menu> implements IMenuService {

    @Override
    public List<Menu> getMenuListByUserId(Integer userId) {
        // 一级菜单
        List<Menu> menuList = this.baseMapper.getMunuListByUserId(userId, 0);
        // 子菜单
        setMenuChildrenByUserId(userId, menuList);
        return menuList;
    }

    @Override
    public List<Menu> getAllMenu() {
        // 一级菜单
        LambdaQueryWrapper<Menu> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Menu::getParentId,0);
        List<Menu> menuList = this.list(wrapper);
        // 填充子菜单
        setMenuChildren(menuList);
        return menuList;
    }
    private void setMenuChildren(List<Menu> menuList) {
        if(menuList != null){
            for (Menu menu : menuList) {
                LambdaQueryWrapper<Menu> subWrapper = new LambdaQueryWrapper<>();
                subWrapper.eq(Menu::getParentId,menu.getMenuId());
                List<Menu> subMenuList = this.list(subWrapper);
                menu.setChildren(subMenuList);
                // 递归
                setMenuChildren(subMenuList);
            }
        }
    }

    private void setMenuChildrenByUserId(Integer userId, List<Menu> menuList) {
        if(menuList != null){
            for (Menu menu : menuList) {
                List<Menu> subMenuList = this.baseMapper.getMunuListByUserId(userId, menu.getMenuId());
                menu.setChildren(subMenuList);
                // 递归
                setMenuChildrenByUserId(userId,subMenuList);
            }
        }
    }
}
```

```java
@Mapper
public interface MenuMapper extends BaseMapper<Menu> {
    public List<Menu> getMunuListByUserId(@Param("userId") Integer userId, @Param("pid") Integer pid);
}
```

```xml
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE mapper PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN" "http://mybatis.org/dtd/mybatis-3-mapper.dtd">
<mapper namespace="com.oumuanode.ormsystem.sys.mapper.MenuMapper">
    <select id="getMunuListByUserId"  resultType="Menu">
        SELECT
            distinct  a.*
        FROM x_menu a,
             x_role_menu b,
             x_user_role c
        WHERE a.`menu_id` = b.`menu_id`
          AND   b.`role_id` = c.`role_id`
          AND   c.`user_id` = #{userId}
          AND   a.`parent_id` = #{pid}
    </select>
</mapper>
```

#### RoleController

前端

```js
export default{
  getRoleList(searchModel){
    return request({
      url: '/role/list',
      method: 'get',
      params:{
        pageNo: searchModel.pageNo,
        pageSize: searchModel.pageSize,
        roleName: searchModel.roleName
      }
    });
  },
  addRole(role){
    return request({
      url: '/role',
      method: 'post',
      data: role
    });
  },
  updateRole(role){
    return request({
      url: '/role',
      method: 'put',
      data: role
    });
  },
  saveRole(role){
    if(role.roleId == null && role.roleId == undefined){
      return this.addRole(role);
    }
    return this.updateRole(role);
  },
  getRoleById(id){
    return request({
      url: `/role/${id}`,
      method: 'get'
    });
  },
  deleteRoleById(id){
    return request({
      url: `/role/${id}`,
      method: 'delete'
    });
  },
  getAllRoleList(){
    return request({
      url: '/role/all',
      method: 'get'
    });
  },
}
```

后端

```java
@Api(tags = {"角色控制列表"})
@RestController
@RequestMapping("/role")
public class RoleController {
    @Autowired
    private IRoleService roleService;

    @GetMapping("/list")
    public ResponseResult<Map<String,Object>> getRoleList(@RequestParam(value = "roleName",required = false) String roleName,
                                                          @RequestParam(value = "pageNo") Long pageNo,
                                                          @RequestParam(value = "pageSize") Long pageSize){

        LambdaQueryWrapper<Role> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(StringUtils.hasLength(roleName),Role::getRoleName,roleName);
        wrapper.orderByDesc(Role::getRoleId);

        Page<Role> page = new Page<>(pageNo,pageSize);
        roleService.page(page,wrapper);

        Map<String,Object> data = new HashMap<>();
        data.put("total",page.getTotal());
        data.put("rows",page.getRecords());

        return ResponseResult.success(data);

    }

    @PostMapping
    public ResponseResult<?> addRole(@RequestBody Role role){
        roleService.addRole(role);
        return ResponseResult.success("新增角色成功");
    }

    @PutMapping
    public ResponseResult<?> updateRole(@RequestBody Role role){
        roleService.updateRole(role);
        return ResponseResult.success("修改角色成功");
    }

    @GetMapping("/{id}")
    public ResponseResult<Role> getRoleById(@PathVariable("id") Integer id){
        Role role = roleService.getRoleById(id);
        return ResponseResult.success(role);
    }

    @DeleteMapping("/{id}")
    public ResponseResult<Role> deleteRoleById(@PathVariable("id") Integer id){
        roleService.deleteRoleById(id);
        return ResponseResult.success("删除角色成功");
    }

    @GetMapping("/all")
    public ResponseResult<List<Role>> getAllRole(){
        List<Role> roleList = roleService.list();
        return ResponseResult.success(roleList);
    }

}
```

```java
public interface IRoleService extends IService<Role> {

    void addRole(Role role);

    Role getRoleById(Integer id);

    void updateRole(Role role);

    void deleteRoleById(Integer id);
}
```

Service

```java
@Service
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements IRoleService {

    @Resource
    private RoleMenuMapper roleMenuMapper;

    @Override
    @Transactional
    public void addRole(Role role) {
        // 写入角色表
        this.baseMapper.insert(role);
        // 写入角色菜单关系表
        if( null != role.getMenuIdList()){
            for (Integer menuId : role.getMenuIdList()) {
                roleMenuMapper.insert(new RoleMenu(null,role.getRoleId(),menuId));
            }
        }
    }

    @Override
    public Role getRoleById(Integer id) {
        Role role = this.baseMapper.selectById(id);
        List<Integer> menuIdList = roleMenuMapper.getMenuIdListByRoleId(id);
        role.setMenuIdList(menuIdList);
        return role;
    }

    @Override
    @Transactional
    public void updateRole(Role role) {
        // 修改角色表
        this.baseMapper.updateById(role);
        // 删除原有权限
        LambdaQueryWrapper<RoleMenu> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(RoleMenu::getRoleId,role.getRoleId());
        roleMenuMapper.delete(wrapper);
        // 新增权限
        if( null != role.getMenuIdList()){
            for (Integer menuId : role.getMenuIdList()) {
                roleMenuMapper.insert(new RoleMenu(null,role.getRoleId(),menuId));
            }
        }
    }

    @Override
    @Transactional
    public void deleteRoleById(Integer id) {
        this.baseMapper.deleteById(id);
        // 删除权限
        LambdaQueryWrapper<RoleMenu> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(RoleMenu::getRoleId,id);
        roleMenuMapper.delete(wrapper);
    }
}
```

#### UserController

前端

```js
export default{
  getUserList(searchModel){
    return request({
      url: '/user/list',
      method: 'get',
      params:{
        pageNo: searchModel.pageNo,
        pageSize: searchModel.pageSize,
        username: searchModel.username,
        phone: searchModel.phone
      }
    });
  },
  addUser(user){
    return request({
      url: '/user',
      method: 'post',
      data: user
    });
  },
  updateUser(user){
    return request({
      url: '/user',
      method: 'put',
      data: user
    });
  },
  saveUser(user){
    if(user.id == null && user.id == undefined){
      return this.addUser(user);
    }
    return this.updateUser(user);
  },
  getUserById(id){
    return request({
      //url: '/user/' + id,
      url: `/user/${id}`,
      method: 'get'
    });
  },
  deleteUserById(id){
    return request({
      url: `/user/${id}`,
      method: 'delete'
    });
  },
}
```

request.js

```js
import axios from 'axios'
import { MessageBox, Message } from 'element-ui'
import store from '@/store'
import { getToken } from '@/utils/auth'

// create an axios instance
const service = axios.create({
  baseURL: process.env.VUE_APP_BASE_API, // url = base url + request url
  // withCredentials: true, // send cookies when cross-domain requests
  timeout: 5000 // request timeout
})

// request interceptor
service.interceptors.request.use(
  config => {
    // do something before request is sent

    if (store.getters.token) {
      // let each request carry token
      // ['X-Token'] is a custom headers key
      // please modify it according to the actual situation
      config.headers['X-Token'] = getToken()
    }
    return config
  },
  error => {
    // do something with request error
    console.log(error) // for debug
    return Promise.reject(error)
  }
)

// response interceptor
service.interceptors.response.use(
  /**
   * If you want to get http information such as headers or status
   * Please return  response => response
  */

  /**
   * Determine the request status by custom code
   * Here is just an example
   * You can also judge the status by HTTP Status Code
   */
  response => {
    const res = response.data

    // if the custom code is not 200, it is judged as an error.
    if (res.code !== 200) {
      Message({
        message: res.message || 'Error',
        type: 'error',
        duration: 5 * 1000
      })

      // 50008: Illegal token; 50012: Other clients logged in; 50014: Token expired;
      if (res.code === 50008 || res.code === 50012 || res.code === 50014) {
        // to re-login
        MessageBox.confirm('You have been logged out, you can cancel to stay on this page, or log in again', 'Confirm logout', {
          confirmButtonText: 'Re-Login',
          cancelButtonText: 'Cancel',
          type: 'warning'
        }).then(() => {
          store.dispatch('user/resetToken').then(() => {
            location.reload()
          })
        })
      }
      return Promise.reject(new Error(res.message || 'Error'))
    } else {
      return res
    }
  },
  error => {
    console.log('err' + error) // for debug
    Message({
      message: error.message,
      type: 'error',
      duration: 5 * 1000
    })
    return Promise.reject(error)
  }
)

export default service
```

后端

```java
@Api(tags = {"用户接口列表"})
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private IUserService userService;


    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping("/all")
    public ResponseResult<List<User>> getAllUsers() {
        List<User> users = userService.list();
        return ResponseResult.success("查询成功");
    }

    @GetMapping("/list")
    public ResponseResult<Map<String,Object>> getUserList(@RequestParam(value = "username",required = false) String username,
                                                  @RequestParam(value = "phone",required = false) String phone,
                                                  @RequestParam(value = "pageNo") Long pageNo,
                                                  @RequestParam(value = "pageSize") Long pageSize){

        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(StringUtils.hasLength(username),User::getUsername,username);
        wrapper.eq(StringUtils.hasLength(phone),User::getPhone,phone);
        wrapper.orderByDesc(User::getId);

        Page<User> page = new Page<>(pageNo,pageSize);
        userService.page(page,wrapper);

        Map<String,Object> data = new HashMap<>();
        data.put("total",page.getTotal());
        data.put("rows",page.getRecords());

        return ResponseResult.success(data);

    }


    @PostMapping
    public ResponseResult<?> addUser(@RequestBody User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userService.addUser(user);
        return ResponseResult.success("新增用户成功");
    }

    @PutMapping
    public ResponseResult<?> updateUser(@RequestBody User user) {
        user.setPassword(null);
        userService.updateUser(user);
        return ResponseResult.success("修改用户成功");
    }

    @GetMapping("/{id}")
    public ResponseResult<User> getUserById(@PathVariable("id") Integer id) {
        User user = userService.getUserById(id);
        return ResponseResult.okResult(user);
    }

    @DeleteMapping("/{id}")
    public ResponseResult<User> deleteUserById(@PathVariable("id") Integer id) {
        userService.deleteUserById(id);
        return ResponseResult.success("删除用户成功");
    }

}
```

```java
public interface IUserService extends IService<User> {


    ResponseResult login(User user);

    ResponseResult logout();

    Map<String, Object> getUserInfo(String token);

    void logout(String token);


    void addUser(User user);

    void updateUser(User user);

    User getUserById(Integer id);

    void deleteUserById(Integer id);
}
```



```java
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Resource
    private UserRoleMapper userRoleMapper;
    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private IMenuService menuService;
    @Autowired
    private RedisCache redisCache;


    @Override
    public void addUser(User user) {
        this.baseMapper.insert(user);
        List<Integer> roleIdList = user.getRoleIdList();
        if (roleIdList != null){
            for (Integer roleId : roleIdList){
                userRoleMapper.insert(new UserRole(null,user.getId(),roleId));
            }
        }
    }

    @Override
    public void updateUser(User user) {
        this.baseMapper.updateById(user);
        // 清除原有角色
        LambdaQueryWrapper<UserRole> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(UserRole::getUserId,user.getId());
        userRoleMapper.delete(wrapper);
        // 设置新的角色
        List<Integer> roleIdList = user.getRoleIdList();
        if(roleIdList != null){
            for (Integer roleId : roleIdList) {
                userRoleMapper.insert(new UserRole(null,user.getId(),roleId));
            }
        }
    }

    @Override
    public User getUserById(Integer id) {
        User user = this.baseMapper.selectById(id);

        LambdaQueryWrapper<UserRole> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(UserRole::getUserId,id);
        List<UserRole> userRoles = userRoleMapper.selectList(queryWrapper);
        List<Integer> collect = userRoles.stream().map(UserRole::getRoleId).collect(Collectors.toList());

        user.setRoleIdList(collect);
        return user;

    }

    @Override
    public void deleteUserById(Integer id) {
        this.baseMapper.deleteById(id);
        // 清除原有角色
        LambdaQueryWrapper<UserRole> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(UserRole::getUserId,id);
        userRoleMapper.delete(wrapper);
    }
}
```



#### Utils

```java
public class BeanCopyUtils {

    private BeanCopyUtils() {
    }

    public static <V> V copyBean(Object source,Class<V> clazz) {
        //创建目标对象
        V result = null;
        try {
            result = clazz.newInstance();
            //实现属性copy
            BeanUtils.copyProperties(source, result);
        } catch (Exception e) {
            e.printStackTrace();
        }
        //返回结果
        return result;
    }
    public static <O,V> List<V> copyBeanList(List<O> list, Class<V> clazz){
        return list.stream()
                .map(o -> copyBean(o, clazz))
                .collect(Collectors.toList());
    }
}
```



```java
/**
 * JWT工具类
 */
@Component
public class JwtUtil {
    // 有效期
    private static final long JWT_EXPIRE = 30*60*1000L;  //半小时
    // 令牌秘钥
    private static final String JWT_KEY = "123456";


    public static String createToken(Object data){
        // 当前时间
        long currentTime = System.currentTimeMillis();
        // 过期时间
        long expTime = currentTime+JWT_EXPIRE;
        // 构建jwt
        JwtBuilder builder = Jwts.builder()
                .setId(UUID.randomUUID()+"")
                .setSubject(JSON.toJSONString(data))
                .setIssuer("system")
                .setIssuedAt(new Date(currentTime))
                .signWith(SignatureAlgorithm.HS256, encodeSecret(JWT_KEY))
                .setExpiration(new Date(expTime));
        return builder.compact();
    }

    private static SecretKey encodeSecret(String key){
        byte[] encode = Base64.getEncoder().encode(key.getBytes());
        SecretKeySpec aes = new SecretKeySpec(encode, 0, encode.length, "AES");
        return  aes;
    }

    public static Claims parseToken(String token){
        Claims body = Jwts.parser()
                .setSigningKey(encodeSecret(JWT_KEY))
                .parseClaimsJws(token)
                .getBody();
        return body;
    }

    public <T> T parseToken(String token,Class<T> clazz){
        Claims body = Jwts.parser()
                .setSigningKey(encodeSecret(JWT_KEY))
                .parseClaimsJws(token)
                .getBody();
        return JSON.parseObject(body.getSubject(),clazz);
    }

}
```



```java
//告诉编译器忽略指定的警告，不用在编译完成后出现警告信息
// rawtypes 在类params上使用泛型时，要抑制相对于非特异性类型的警告
// unchecked 相对于不受约束的操作，抑制警告
@SuppressWarnings(value = { "unchecked", "rawtypes" })
@Component
public class RedisCache
{
    @Autowired
    public RedisTemplate redisTemplate;

    /**
     * 缓存基本的对象，Integer、String、实体类等
     *
     * @param key 缓存的键值
     * @param value 缓存的值
     */
    public <T> void setCacheObject(final String key, final T value)
    {
        redisTemplate.opsForValue().set(key, value);
    }

    /**
     * 缓存基本的对象，Integer、String、实体类等
     *
     * @param key 缓存的键值
     * @param value 缓存的值
     * @param timeout 时间
     * @param timeUnit 时间颗粒度
     */
    public <T> void setCacheObject(final String key, final T value, final Integer timeout, final TimeUnit timeUnit)
    {
        redisTemplate.opsForValue().set(key, value, timeout, timeUnit);
    }

    /**
     * 设置有效时间
     *
     * @param key Redis键
     * @param timeout 超时时间
     * @return true=设置成功；false=设置失败
     */
    public boolean expire(final String key, final long timeout)
    {
        return expire(key, timeout, TimeUnit.SECONDS);
    }

    /**
     * 设置有效时间
     *
     * @param key Redis键
     * @param timeout 超时时间
     * @param unit 时间单位
     * @return true=设置成功；false=设置失败
     */
    public boolean expire(final String key, final long timeout, final TimeUnit unit)
    {
        return redisTemplate.expire(key, timeout, unit);
    }

    /**
     * 获得缓存的基本对象。
     *
     * @param key 缓存键值
     * @return 缓存键值对应的数据
     */
    public <T> T getCacheObject(final String key)
    {
        ValueOperations<String, T> operation = redisTemplate.opsForValue();
        return operation.get(key);
    }

    /**
     * 删除单个对象
     *
     * @param key
     */
    public boolean deleteObject(final String key)
    {
        return redisTemplate.delete(key);
    }

    /**
     * 删除集合对象
     *
     * @param collection 多个对象
     * @return
     */
    public long deleteObject(final Collection collection)
    {
        return redisTemplate.delete(collection);
    }

    /**
     * 缓存List数据
     *
     * @param key 缓存的键值
     * @param dataList 待缓存的List数据
     * @return 缓存的对象
     */
    public <T> long setCacheList(final String key, final List<T> dataList)
    {
        Long count = redisTemplate.opsForList().rightPushAll(key, dataList);
        return count == null ? 0 : count;
    }

    /**
     * 获得缓存的list对象
     *
     * @param key 缓存的键值
     * @return 缓存键值对应的数据
     */
    public <T> List<T> getCacheList(final String key)
    {
        return redisTemplate.opsForList().range(key, 0, -1);
    }

    /**
     * 缓存Set
     *
     * @param key 缓存键值
     * @param dataSet 缓存的数据
     * @return 缓存数据的对象
     */
    public <T> BoundSetOperations<String, T> setCacheSet(final String key, final Set<T> dataSet)
    {
        BoundSetOperations<String, T> setOperation = redisTemplate.boundSetOps(key);
        Iterator<T> it = dataSet.iterator();
        while (it.hasNext())
        {
            setOperation.add(it.next());
        }
        return setOperation;
    }

    /**
     * 获得缓存的set
     *
     * @param key
     * @return
     */
    public <T> Set<T> getCacheSet(final String key)
    {
        return redisTemplate.opsForSet().members(key);
    }

    /**
     * 缓存Map
     *
     * @param key
     * @param dataMap
     */
    public <T> void setCacheMap(final String key, final Map<String, T> dataMap)
    {
        if (dataMap != null) {
            redisTemplate.opsForHash().putAll(key, dataMap);
        }
    }

    /**
     * 获得缓存的Map
     *
     * @param key
     * @return
     */
    public <T> Map<String, T> getCacheMap(final String key)
    {
        return redisTemplate.opsForHash().entries(key);
    }

    /**
     * 往Hash中存入数据
     *
     * @param key Redis键
     * @param hKey Hash键
     * @param value 值
     */
    public <T> void setCacheMapValue(final String key, final String hKey, final T value)
    {
        redisTemplate.opsForHash().put(key, hKey, value);
    }

    /**
     * 获取Hash中的数据
     *
     * @param key Redis键
     * @param hKey Hash键
     * @return Hash中的对象
     */
    public <T> T getCacheMapValue(final String key, final String hKey)
    {
        HashOperations<String, String, T> opsForHash = redisTemplate.opsForHash();
        return opsForHash.get(key, hKey);
    }


    public void incrementCacheMapValue(String key,String hKey,int v){
        redisTemplate.opsForHash().increment(key,hKey,v);
    }

    /**
     * 删除Hash中的数据
     * 
     * @param key
     * @param hkey
     */
    public void delCacheMapValue(final String key, final String hkey)
    {
        HashOperations hashOperations = redisTemplate.opsForHash();
        hashOperations.delete(key, hkey);
    }

    /**
     * 获取多个Hash中的数据
     *
     * @param key Redis键
     * @param hKeys Hash键集合
     * @return Hash对象集合
     */
    public <T> List<T> getMultiCacheMapValue(final String key, final Collection<Object> hKeys)
    {
        return redisTemplate.opsForHash().multiGet(key, hKeys);
    }

    /**
     * 获得缓存的基本对象列表
     *
     * @param pattern 字符串前缀
     * @return 对象列表
     */
    public Collection<String> keys(final String pattern)
    {
        return redisTemplate.keys(pattern);
    }
}
```

filter

```java
@Component
@Slf4j
public class JwtValidateInterceptor implements HandlerInterceptor {
    @Autowired
    private JwtUtil jwtUtil;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String token  = request.getHeader("X-Token");
        log.debug(request.getRequestURI() + "需要验证： " + token);
        if(token != null){
            try {
                jwtUtil.parseToken(token);
                log.debug(request.getRequestURI() + "验证通过");
                return true;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        log.debug(request.getRequestURI() + "验证失败，禁止访问");
        response.setContentType("application/json;charset=utf-8");
        ResponseResult<Object> fail = ResponseResult.errorResult(AppHttpCodeEnum.USERNAME_INVALID);
        response.getWriter().write(JSON.toJSONString(fail));
        return false; // 拦截
    }
}
```

exception

```java
public class SysException extends RuntimeException{

    private int code;

    private String msg;

    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }

    public SysException(AppHttpCodeEnum httpCodeEnum) {
        super(httpCodeEnum.getMsg());
        this.code = httpCodeEnum.getCode();
        this.msg = httpCodeEnum.getMsg();
    }
}
```

```java
public enum AppHttpCodeEnum {
    // 成功
    SUCCESS(200,"操作成功"),
    // 登录
    NEED_LOGIN(401,"需要登录后操作"),
    NO_OPERATOR_AUTH(403,"无权限操作"),
    SYSTEM_ERROR(500,"出现错误"),
    USERNAME_EXIST(501,"用户名已存在"),
     PHONENUMBER_EXIST(502,"手机号已存在"), EMAIL_EXIST(503, "邮箱已存在"),
    REQUIRE_USERNAME(504, "必需填写用户名"),
    CONTENT_NOT_NULL(506, "评论内容不能为空"),

    CONTENT_NOT_BYSELF ( 513,"自己不能回复自己哦"),
    FILE_TYPE_ERROR(507, "文件类型错误，请上传png文件"),
    USERNAME_NOT_NULL(508, "用户名不能为空"),

    USERNAME_INVALID(514,"登录信息无效，请重新登录"),
    NICKNAME_NOT_NULL(509, "昵称不能为空"),
    PASSWORD_NOT_NULL(510, "密码不能为空"),
    EMAIL_NOT_NULL(511, "邮箱不能为空"),
    NICKNAME_EXIST(512, "昵称已存在"),
    LOGIN_ERROR(505,"用户名或密码错误");
    int code;
    String message;

    AppHttpCodeEnum(int code, String errorMessage){
        this.code = code;
        this.message = errorMessage;
    }

    public int getCode() {
        return code;
    }

    public String getMsg() {
        return message;
    }
}
```

#### Config

```java
@Configuration
@EnableOpenApi
@EnableWebMvc
public class MySwaggerConfig {
    @Bean
    public Docket api() {
        return new Docket(DocumentationType.OAS_30)
                .apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.oumuanode.ormsystem"))
                .paths(PathSelectors.any())
                .build()
                .securitySchemes(Collections.singletonList(securityScheme()))
                .securityContexts(Collections.singletonList(securityContext()));
    }

    private SecurityScheme securityScheme() {
        //return new ApiKey("Authorization", "Authorization", "header");
        return new ApiKey("X-Token", "X-Token", "header");
    }

    private SecurityContext securityContext() {
        return SecurityContext.builder()
                .securityReferences(defaultAuth())
                .forPaths(PathSelectors.regex("^(?!auth).*$"))
                .build();
    }

    private List<SecurityReference> defaultAuth() {
        AuthorizationScope authorizationScope = new AuthorizationScope("global", "accessEverything");
        AuthorizationScope[] authorizationScopes = new AuthorizationScope[1];
        authorizationScopes[0] = authorizationScope;
        return Collections.singletonList(
                new SecurityReference("X-Token", authorizationScopes));
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("ORM管理系统接口文档")
                .version("1.0")
                .contact(new Contact("oumuanode", "http://www.oumuanode.cloud", "3068945697@qq.com"))
                .build();
    }
}
```

```java
@Configuration
public class MyRedisConfig {

    @Resource
    private RedisConnectionFactory factory;

    @Bean
    public RedisTemplate redisTemplate(){
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(factory);

        redisTemplate.setKeySerializer(new StringRedisSerializer());

        Jackson2JsonRedisSerializer serializer = new Jackson2JsonRedisSerializer<Object>(Object.class);
        redisTemplate.setValueSerializer(serializer);

        ObjectMapper om = new ObjectMapper();
        om.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        om.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
        om.setTimeZone(TimeZone.getDefault());
        om.configure(MapperFeature.USE_ANNOTATIONS, false);
        om.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        om.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        om.activateDefaultTyping(LaissezFaireSubTypeValidator.instance ,ObjectMapper.DefaultTyping.NON_FINAL, JsonTypeInfo.As.PROPERTY);
        om.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        serializer.setObjectMapper(om);

        return  redisTemplate;
    }
}
```



```java
@Configuration
public class MyInterceptorConfig implements WebMvcConfigurer {
    @Autowired
    private JwtValidateInterceptor jwtValidateInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        InterceptorRegistration registration = registry.addInterceptor(jwtValidateInterceptor);
        registration.addPathPatterns("/**")
                .excludePathPatterns(
                        "/user/login",
                        "/user/info",
                        "/user/logout",
                        "/error",
                        "/swagger-ui/**",
                        "/swagger-resources/**",
                        "/v3/**"

                );
    }


}
```



```java
@Configuration
public class MyCorsConfig {

    @Bean
    public CorsFilter corsFilter(){
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.addAllowedOrigin("http://localhost:8888");
        configuration.setAllowCredentials(true);
        configuration.addAllowedMethod("*");
        configuration.addAllowedHeader("*");

        UrlBasedCorsConfigurationSource urlBasedCorsConfigurationSource = new UrlBasedCorsConfigurationSource();
        urlBasedCorsConfigurationSource.registerCorsConfiguration("/**",configuration);

        return  new CorsFilter(urlBasedCorsConfigurationSource);
    }
}
```

```java
@Configuration
public class MpConfig {
    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor() {
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
        interceptor.addInnerInterceptor(new PaginationInnerInterceptor(DbType.MYSQL));
        return interceptor;
    }
}
```

Pom

```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 https://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>
    <parent>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-parent</artifactId>
        <version>2.7.10</version>
        <relativePath/> <!-- lookup parent from repository -->
    </parent>
    <groupId>com.oumuanode</groupId>
    <artifactId>ORMSystem</artifactId>
    <version>0.0.1-SNAPSHOT</version>
    <name>ORMSystem</name>
    <description>ORMSystem</description>
    <properties>
        <java.version>1.8</java.version>
    </properties>
    <dependencies>
        <!-- web -->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>
        <!-- mysql -->
        <dependency>
            <groupId>com.mysql</groupId>
            <artifactId>mysql-connector-j</artifactId>
        </dependency>
        <!-- mybatis-plus -->
        <dependency>
            <groupId>com.baomidou</groupId>
            <artifactId>mybatis-plus-boot-starter</artifactId>
            <version>3.5.2</version>
        </dependency>
        <dependency>
            <groupId>com.baomidou</groupId>
            <artifactId>mybatis-plus-generator</artifactId>
            <version>3.5.2</version>
        </dependency>
        <!-- freemarker -->
        <dependency>
            <groupId>org.freemarker</groupId>
            <artifactId>freemarker</artifactId>
        </dependency>
        <!-- lombok -->
        <dependency>
            <groupId>org.projectlombok</groupId>
            <artifactId>lombok</artifactId>
        </dependency>

        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-data-redis</artifactId>
        </dependency>

        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter</artifactId>
        </dependency>

        <!-- hutool  -->
        <dependency>
            <groupId>cn.hutool</groupId>
            <artifactId>hutool-all</artifactId>
            <version>5.7.20</version>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-test</artifactId>
            <scope>test</scope>
        </dependency>

        <!-- fastjson -->
        <dependency>
            <groupId>com.alibaba.fastjson2</groupId>
            <artifactId>fastjson2</artifactId>
            <version>2.0.7</version>
        </dependency>

<!--        &lt;!&ndash;SpringSecurity启动器&ndash;&gt;-->
<!--        <dependency>-->
<!--            <groupId>org.springframework.boot</groupId>-->
<!--            <artifactId>spring-boot-starter-security</artifactId>-->
<!--        </dependency>-->
        <dependency>
            <groupId>org.springframework.security</groupId>
            <artifactId>spring-security-core</artifactId>
        </dependency>

        <dependency>
            <groupId>io.jsonwebtoken</groupId>
            <artifactId>jjwt</artifactId>
            <version>0.9.1</version>
        </dependency>
        <!--Swagger文档工具-->
        <dependency>
            <groupId>io.springfox</groupId>
            <artifactId>springfox-boot-starter</artifactId>
            <version>3.0.0</version>
        </dependency>
    </dependencies>

    <build>
        <plugins>
            <plugin>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-maven-plugin</artifactId>
            </plugin>
        </plugins>
    </build>
</project>
```



完



