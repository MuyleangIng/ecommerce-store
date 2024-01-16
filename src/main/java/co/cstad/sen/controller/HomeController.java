package co.cstad.sen.controller;

import co.cstad.sen.api.users.User;
import co.cstad.sen.api.users.UserRepository;
import co.cstad.sen.utils.Notifications;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@AllArgsConstructor
public class HomeController {
    private final UserRepository userRepository;
    private final Notifications notifications;

    @GetMapping("/")
    String home(Model model) {
        return "index";
    }

    @GetMapping("/send")
    String send(){
        User user = userRepository.findByEmail("admin@gmail.com").orElseThrow(() -> new RuntimeException("User not found"));

        Notifications.Meta<?> meta = Notifications.Meta.builder()
                .to(user.getEmail())
                .from(user.getEmail())
                .data(user)
                .subject("Register new account")
                .templateUrl("mail/verify")
                .build();
        notifications.sendEmail(meta, user);
        return "sending";
    }
}
