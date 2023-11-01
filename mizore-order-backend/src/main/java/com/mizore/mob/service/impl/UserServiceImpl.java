package com.mizore.mob.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.RandomUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mizore.mob.dto.LoginFromDTO;
import com.mizore.mob.dto.Result;
import com.mizore.mob.dto.SignFromDTO;
import com.mizore.mob.entity.User;
import com.mizore.mob.mapper.UserMapper;
import com.mizore.mob.service.IUserService;
import com.mizore.mob.util.JWT;
import com.mizore.mob.util.UserHolder;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static com.mizore.mob.util.Constant.*;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author mizore
 * @since 2023-10-22
 */
@Service
@Slf4j
@AllArgsConstructor
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {

    StringRedisTemplate stringRedisTemplate;

    @Override
    public Result sendCode(String phone) {
        // 已经使用spring valid 对phone进行了格式检查
        // TODO: 使用云服务向phone发送验证码，获取验证码并存入redis。这里先通过hutool工具类随机生成六位数字了
        String code = RandomUtil.randomNumbers(6);
        log.info("手机号 {} 的验证码是： {}", phone, code);
        stringRedisTemplate.opsForValue().set(LOGIN_CODE_PREFIX + phone, code, LOGIN_CODE_TTL, TimeUnit.MINUTES);
        return Result.ok();
    }

    @Override
    public Result sign(SignFromDTO signFromDTO) {
        // 检查手机号和验证码正确性
        String phone = signFromDTO.getPhone();
        if (checkPhoneAndCode(phone, signFromDTO.getCode())) {
            return Result.error("手机或验证码有误，或验证码已失效");
        }

        // 需要防止重复注册：同一手机号对同个用户角色只能注册一个用户
        if (query().eq("phone", phone).eq("role", signFromDTO.getRole()).one() != null) {
            return Result.error("手机号已被注册");
        }
        User newUser = new User();
        BeanUtil.copyProperties(signFromDTO, newUser, "code");
        boolean res = save(newUser);
        return res ? Result.ok() : Result.error(RUNTIME_EXCEPTION_MSG);
    }

    @Override
    public Result login(LoginFromDTO loginFromDTO) {
        // 检查手机号和验证码正确性
        String phone = loginFromDTO.getPhone();
        if (!checkPhoneAndCode(phone, loginFromDTO.getCode())) {
            return Result.error("手机或验证码有误，或验证码已失效");
        }
        // 验证手机号与角色的匹配
        User resUser = query().eq("phone", phone).eq("role", loginFromDTO.getRole()).one();
        if (resUser == null) {
            return Result.error("登陆失败，请检查手机号与所选角色是否正确！");
        }
        // 存userHolder 已经由RefreshTokenInterceptor做了
//        UserHolder.save(resUser);
        // 基于JWT发token
        Map<String, Object> claims = new HashMap<>();
        claims.put("id", resUser.getId());
        claims.put("phone", resUser.getPhone());
        claims.put("name", resUser.getName());
        claims.put("role", resUser.getRole());
        String jwt = JWT.generateJWT(claims);
        return Result.ok(jwt);
    }

    private boolean checkPhoneAndCode(String phone, String code) {
        String resCode = stringRedisTemplate.opsForValue().get(LOGIN_CODE_PREFIX + phone);
        if (resCode != null && resCode.equals(code)) {
            // 验证码有效且验证码手机号一致
            stringRedisTemplate.delete(LOGIN_CODE_PREFIX + phone);  // 主动删除验证码
            return true;
        }
        return false;
    }
}
