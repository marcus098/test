package com.example.uiservice.controller;

import com.example.commonlib.dto.ArticleDto;
import com.example.commonlib.dto.UserDto;
import com.example.commonlib.request.AddArticle;
import com.example.commonlib.response.AuthResponse;
import com.example.uiservice.service.ApiService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseCookie;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class PageController {
    @Autowired
    private ApiService apiService;

    //@GetMapping("/home")
    //public String home() {
    //    return "dashboard";  // src/main/resources/templates/index.html
    //}

    @GetMapping("/home")
    public String home(Model model, HttpSession session) {
        Object user = session.getAttribute("user");
        model.addAttribute("user", user);
        UserDto userDto = (UserDto) user;
        if (userDto == null) {
            return "redirect:/login";
        }
        if (userDto.getRole().equals("ADMIN")) {
            return "admin-home";
        } else if (userDto.getRole().equals("USER")) {
            return "user-home";
        }
        return "redirect:/login";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }


    @GetMapping("/dashboardpage")
    public String dashboard(Model model, HttpSession session) {
        UserDto user = (UserDto) session.getAttribute("user");
        String token = (String) session.getAttribute("jwt");
        if (user == null) return "redirect:/login";
        List<ArticleDto> articles = apiService.getArticlesByShop(token);
        model.addAttribute("user", user);
        model.addAttribute("articles", articles);
        model.addAttribute("article", new AddArticle());
        System.out.println(articles.size());
        return user.getRole().equals("ADMIN") || user.getRole().equals("SHOP")
                ? "new-admin-page"
                : "new-user-page";
    }

    @PostMapping("/dashboard/articles")
    public String addArticle(@RequestBody AddArticle addArticle,
                             HttpSession session) {
        String token = (String) session.getAttribute("jwt");
        apiService.createArticle(token, addArticle);
        return "redirect:/dashboardpage";
    }

    @PostMapping("/dashboard/articles/delete/{id}")
    public String deleteArticle(@PathVariable String id, HttpSession session) {
        String token = (String) session.getAttribute("jwt");
        apiService.deleteArticle(id, token);
        return "redirect:/dashboard";
    }

    @PostMapping("/dologin")
    public String login(@RequestParam String username,
                        @RequestParam String password,
                        HttpSession session,
                        HttpServletRequest request,
                        HttpServletResponse response,
                        Model model) {
        try {
            AuthResponse authResponse = apiService.login(username, password);
            String jwt = authResponse.getToken();

            session.setAttribute("user", authResponse.getUser());
            session.setAttribute("jwt", jwt);

            List<GrantedAuthority> authorities = List.of(
                    new SimpleGrantedAuthority("ROLE_" + authResponse.getUser().getRole())
            );

            UsernamePasswordAuthenticationToken authentication =
                    new UsernamePasswordAuthenticationToken(authResponse.getUser(), null, authorities);

            SecurityContext context = SecurityContextHolder.createEmptyContext();
            context.setAuthentication(authentication);
            SecurityContextHolder.setContext(context);

            // Salva contesto nella sessione
            HttpSessionSecurityContextRepository repo = new HttpSessionSecurityContextRepository();
            repo.saveContext(context, request, response);

            return "redirect:/home";

        } catch (Exception e) {
            model.addAttribute("error", "Username o password errati");
            return "login";
        }
    }

    @PostMapping("/products/shop")
    public String addProduct(@ModelAttribute AddArticle article,
                             HttpSession session) {
        String token = (String) session.getAttribute("jwt");
        apiService.createArticle(token, article);
        return "redirect:/dashboardpage";
    }

    @GetMapping("/products/search")
    public String searchProducts(@RequestParam("name") String name, Model model, HttpSession session) {
        String token = (String) session.getAttribute("jwt");
        List<ArticleDto> results = apiService.searchArticles(name, token); // Fai la chiamata all’API
        model.addAttribute("articles", results);
        return "searchpage"; // Il nome del file HTML Thymeleaf (es: searchpage.html)
    }


}

