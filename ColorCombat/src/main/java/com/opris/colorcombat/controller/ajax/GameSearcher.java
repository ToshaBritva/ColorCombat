/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.opris.colorcombat.controller.ajax;
import com.google.gson.JsonObject;
import com.opris.colorcombat.controller.LobbySocket;
import com.opris.colorcombat.controller.SocketController;
import com.opris.colorcombat.repository.UserRepository;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Map;
import java.util.Objects;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 *
 * @author HP
 */
@Controller
@RequestMapping(value = {"/MainPage/findGame"})
public class GameSearcher
{    
    class UserForFind implements Map.Entry<String, Integer> 
    {
        private String key;
        private Integer value;

        public UserForFind(String key, Integer value) {
            this.key = key;
            this.value = value;
        }

        @Override
        public String getKey()
        {
            return key;
        }

        @Override
        public Integer getValue()
        {
            return value;
        }

        @Override
        public Integer setValue(Integer value)
        {
            Integer oldValue = this.value;
            this.value = value;
            return oldValue;
        }
    }
    
    static TreeSet<UserForFind> users = new TreeSet<>(
        new Comparator<UserForFind>()
        {
            @Override
            public int compare(UserForFind u1, UserForFind u2)
            {
                int keyComp = u1.getValue().compareTo(u2.getValue());
                if(keyComp == 0)
                {
                    return 1;
                }
                return keyComp;
            }
        }
    );
            
    private UserRepository userRepository;

    @Autowired
    public GameSearcher(UserRepository userRepository) 
    {
        this.userRepository = userRepository;
    }
    
    @RequestMapping(value = {"/start"}, method = RequestMethod.GET)
    @ResponseBody
    public void findGame(HttpServletRequest session)
    {
        int score = userRepository.findByName(session.getUserPrincipal().getName()).getRating();
        String username = session.getUserPrincipal().getName();
        
        users.add(new UserForFind(username, score));
        
        int gameSize = 4;
        if(users.size() >= gameSize)
        {
            ArrayList<String> playersNicknames = new ArrayList<>();
            
            for(int i=0; i<gameSize; i++)
            {
                playersNicknames.add(users.last().getKey());
                users.removeIf(x -> x.getKey().equals(users.last().getKey()));
            }
            SocketController.addGame(playersNicknames);
            
            JsonObject lobbyMessage = new JsonObject();
            lobbyMessage.addProperty("target", "startGame");
            LobbySocket.sendToUsers(playersNicknames, lobbyMessage.toString());
        }
        
        System.out.println("В поиск добавлен " + users.size() + " игрок " + session.getUserPrincipal().getName());
    }
    
    @RequestMapping(value = {"/cancel"}, method = RequestMethod.GET)
    @ResponseBody
    public void getLobby(HttpServletRequest session)
    {
        users.removeIf(x -> x.getKey().equals(session.getUserPrincipal().getName()));
        
        System.out.println("Из поиска удален " + users.size() + " игрок " + session.getUserPrincipal().getName());
    }
}
