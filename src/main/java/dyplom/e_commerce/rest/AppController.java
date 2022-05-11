//package dyplom.e_commerce.rest;
//
//import dyplom.e_commerce.entities.User;
//import dyplom.e_commerce.entityService.UserService;
//import org.springframework.security.access.prepost.PreAuthorize;
//import org.springframework.web.bind.annotation.*;
//
//@RestController
//@RequestMapping("/app")
//public class AppController {
//
//    private final UserService userService;
//
//    public AppController(UserService userService) {
//        this.userService = userService;
//    }
//
//    @GetMapping("/user/{id}")
//    @PreAuthorize("hasAuthority('user')")
//    public User getById(@PathVariable int id) {
//        return userService.getUserById(id);
//    }
//
//    @PostMapping
//    @PreAuthorize("hasAuthority('admin')")
//    public User user(@RequestBody User user){
//        userService.saveUser(user);
//        return user;
//    }
//
//}
