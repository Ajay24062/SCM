package com.scm.config;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.scm.entities.Providers;
import com.scm.entities.User;
import com.scm.helpers.AppConstants;
import com.scm.repositories.UserRepo;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class AutheticationSuccessHandler implements AuthenticationSuccessHandler {

      Logger logger = LoggerFactory.getLogger(AutheticationSuccessHandler.class);

      @Autowired
      private UserRepo userRepo;

      @Override
      public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                  Authentication authentication) throws IOException, ServletException {
            logger.info("AutheticationSuccessHandler");
            // identify user provider

            var oauth2Authenticationtoken = (OAuth2AuthenticationToken) authentication;

            String authorizedClientRegistrationId = oauth2Authenticationtoken.getAuthorizedClientRegistrationId();

            logger.info(authorizedClientRegistrationId);

            var oauthUser = (DefaultOAuth2User) authentication.getPrincipal();

            oauthUser.getAttributes().forEach((key, value) -> {
                  logger.info(key + " : " + value);

            });

            User user = new User();
            user.setUserId(UUID.randomUUID().toString());
            user.setRoleList(List.of(AppConstants.ROLE_USER));
            user.setEmailVariefied(true);
            user.setEnabled(true);
            user.setPassword("dummy");

            if (authorizedClientRegistrationId.equalsIgnoreCase("google")) {

                  user.setEmail(oauthUser.getAttribute("email").toString());
                  user.setProfilePic(oauthUser.getAttribute("picture").toString());
                  user.setName(oauthUser.getAttribute("name").toString());
                  user.setProviderUserId(oauthUser.getName());
                  user.setProvider(Providers.GOOGLE);
                  user.setAbout("This is created using google");

            } else if (authorizedClientRegistrationId.equalsIgnoreCase("github")) {

                  String email = oauthUser.getAttribute("email") != null ? oauthUser.getAttribute("email").toString()
                              : oauthUser.getAttribute("login").toString() + "@gmail.com";
                  String picture = oauthUser.getAttribute("avatar_url").toString();
                  String name = oauthUser.getAttribute("login").toString();
                  String providerUserId = oauthUser.getName();

                  user.setEmail(email);
                  user.setProfilePic(picture);
                  user.setName(name);
                  user.setProviderUserId(providerUserId);
                  user.setProvider(Providers.GITHUB);
                  user.setAbout("This is created using github");

            } else if (authorizedClientRegistrationId.equalsIgnoreCase("linkedin")) {

            } else {
                  logger.info("onAuthenticationSuccessHandler : unknown provider");
            }

            // google

            // github

            // facebook

            // linkedin

            /*
             * DefaultOAuth2User user=(DefaultOAuth2User)authentication.getPrincipal();
             * // logger.info(user.getName());
             * // user.getAttributes().forEach((key,value)->{
             * // logger.info("{}=>{}",key,value);
             * // });
             * // logger.info(user.getAuthorities().toString());
             * 
             * String email=user.getAttribute("email").toString();
             * String name=user.getAttribute("name").toString();
             * String picture=user.getAttribute("picture").toString();
             * 
             * //create user and save to database
             * 
             * User user1=new User();
             * user1.setEmail(email);
             * user1.setName(name);
             * user1.setProfilePic(picture);
             * user1.setPassword("password");
             * user1.setUserId(UUID.randomUUID().toString());
             * user1.setProvider(Providers.GOOGLE);
             * user1.setEnabled(true);
             * 
             * user1.setEmailVariefied(true);
             * user1.setProviderUserId(user.getName());
             * user1.setRoleList(List.of(AppConstants.ROLE_USER));
             * user1.setAbout("This account is created using google");
             * 
             * 
             * User user2=userRepo.findByEmail(email).orElse(null);
             * 
             * if(user2==null)
             * {
             * userRepo.save(user1);
             * logger.info("user Saved"+email);
             * }
             * 
             */

            User user2 = userRepo.findByEmail(user.getEmail()).orElse(null);

            if (user2 == null) {
                  userRepo.save(user);
            }
            new DefaultRedirectStrategy().sendRedirect(request, response, "/user/profile");

      }

}