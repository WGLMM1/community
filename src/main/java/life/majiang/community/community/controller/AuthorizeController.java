package life.majiang.community.community.controller;

import life.majiang.community.community.dto.AccessTokenDTO;
import life.majiang.community.community.dto.GithubUser;
import life.majiang.community.community.mapper.UserMapper;
import life.majiang.community.community.model.User;
import life.majiang.community.community.provider.GithubProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.UUID;

@Controller
public class AuthorizeController {
    @Autowired
    private GithubProvider githubProvider;

    @Value("${github.client.id}")//注意@Value("github.client.id")--->直接去成了字符串"github.client.id"，没有去取对应的值
    private String clientId;

    @Value("${github.client.secret}")
    private String clinetSecrect;

    @Value("${github.redirect.url}")
    private String clientRedirectUrl;

    @Resource
    private UserMapper userMapper;

    @GetMapping("/callback")
    public String callback (@RequestParam(name = "code") String code,
                            @RequestParam(name = "state") String state,
                            HttpServletRequest request) {
        AccessTokenDTO accessTokenDTO = new AccessTokenDTO();
        accessTokenDTO.setCode(code);
        accessTokenDTO.setRedirect_uri(clientRedirectUrl);
        accessTokenDTO.setClient_id(clientId);
        accessTokenDTO.setClient_secret(clinetSecrect);
        System.out.println(accessTokenDTO.getRedirect_uri());
        accessTokenDTO.setState(state);
        String accessToken = githubProvider.getAccessToken(accessTokenDTO);
        GithubUser githubUser = githubProvider.getUser(accessToken);
        if (githubUser != null) {
            //登录成功，写cookie和session
            User user = new User();
            user.setToken(UUID.randomUUID().toString());
            user.setName(githubUser.getName());
            user.setAccountId(String.valueOf(githubUser.getId()));
            user.setGmtCreate(System.currentTimeMillis());
            user.setGmtModified(user.getGmtCreate());
            userMapper.insert(user);
            request.getSession().setAttribute("user",githubUser);
            return "redirect:/";//重定向到index页面
        } else {
            //登录失败，重新登陆
            return "redirect:/";//重定向到index页面
        }
    }
}