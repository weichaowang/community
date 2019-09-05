package com.wwc.community.controller;

import com.wwc.community.dto.AccessTokenDTO;
import com.wwc.community.mapper.UserMapper;
import com.wwc.community.model.User;
import com.wwc.community.provider.GithubProvider;
import com.wwc.community.provider.GithubUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.UUID;

@Controller
public class AuthorizeController {
    @Autowired
    private GithubProvider githubProvider;

    @Autowired
    private UserMapper userMapper;

    @Value("${github.client.id}")
    private String clientId;

    @Value("${github.clent.secret}")
    private String clientSecret;

    @Value("${github.redirect.uri}")
    private String redirectUri;



    @GetMapping("/callback")
    public String callback(@RequestParam(name = "code") String code,
                           @RequestParam(name = "state") String state,
                           HttpServletRequest request,
                           HttpServletResponse response){
        AccessTokenDTO accessTokenDTO = new AccessTokenDTO();
        accessTokenDTO.setClient_id(clientId);
        accessTokenDTO.setClient_secret(clientSecret);
        accessTokenDTO.setCode(code);
        accessTokenDTO.setRedirect_uri(redirectUri);
        accessTokenDTO.setState(state);
        //ctrl+alt+v
        //shift + enter
        String accessToken = githubProvider.getAccessToken(accessTokenDTO);
        GithubUser user=githubProvider.getUser(accessToken);

        if (user!=null){
            User user1 = new User();
            user1.setToken(UUID.randomUUID().toString());
            user1.setAccount_id(user.getId().toString());
            user1.setName(user.getName());
            user1.setGmt_create(System.currentTimeMillis());
            user1.setGmt_modified(System.currentTimeMillis());
            userMapper.insert(user1);
//            request.getSession().setAttribute("user",user);
            Cookie cookie = new Cookie("token",user1.getToken());
            response.addCookie(cookie);
            return "redirect:/";
        }
        else
        {
            return "redirect:/";
        }


    }
}
