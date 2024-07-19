package org.example.controller;

import jakarta.validation.Valid;
import org.example.Enums.Category;
import org.example.entity.TaskArchive;
import org.example.entity.Task;
import org.example.entity.User;
import org.example.service.TaskArchiveService;
import org.example.service.TaskService;
import org.example.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@Controller
@RequestMapping("/")
public class TaskController {
    private UserService userService;
    private TaskService taskService;
    private TaskArchiveService archiveService;

    @Autowired
    public TaskController(UserService userService, TaskService taskService, TaskArchiveService archiveService) {
        this.userService = userService;
        this.taskService = taskService;
        this.archiveService = archiveService;
    }

    @GetMapping()
    public String getAll(Model model) {
        model.addAttribute("base", new Task());
        return "main";
    }

    @GetMapping("main/delete/{id}")
    public String delete(@PathVariable int id,
                         @AuthenticationPrincipal UserDetails userDetails) {
        User user = userService.getUserByEmail(userDetails.getUsername()).orElseThrow();
        taskService.deleteBaseById(id, user);
        return "all";
    }

    @GetMapping("/main")
    public String main(Model model, @AuthenticationPrincipal UserDetails userDetails) {
        User user = userService.getUserByEmail(userDetails.getUsername()).orElseThrow();
//        model.addAttribute("base", new Task());
        model.addAttribute("all", taskService.getAllBase(user));
        List<TaskArchive> archiveTasks = archiveService.getAllArchive(user);
        model.addAttribute("archive", archiveTasks);

        return "main";
    }

    @PostMapping("/main")
    public String addProduct(@ModelAttribute("base") Task baseModel, Model model, @AuthenticationPrincipal UserDetails userDetails) {
        User user = userService.getUserByEmail(userDetails.getUsername()).orElseThrow();
        baseModel.setUser(user);
        taskService.createOrUpdateBase(baseModel, user);
        model.addAttribute("all", taskService.getAllBase(user));
        List<TaskArchive> archiveTasks = archiveService.getAllArchive(user);
        model.addAttribute("archive", archiveTasks);
        return "main";
    }

    @GetMapping("/main/{id}")
    public String getById(@PathVariable int id, Model model, @AuthenticationPrincipal UserDetails userDetails) {
        User user = userService.getUserByEmail(userDetails.getUsername()).orElseThrow();
        Task base = taskService.getBaseById(id, user);
        model.addAttribute("task", base);
        return "info";
    }

    @GetMapping("/main/all")
    public String getAllBase(Model model, @AuthenticationPrincipal UserDetails userDetails) {
        User user = userService.getUserByEmail(userDetails.getUsername()).orElseThrow();
        model.addAttribute("all", taskService.getAllBase(user));
        return "all";
    }

    @PostMapping("/main/edit/{id}")
    public String editBase(@PathVariable("id") int id,
                           @ModelAttribute("base") Task baseModel,
                           @AuthenticationPrincipal UserDetails userDetails,
                           Model model) {
        User user = userService.getUserByEmail(userDetails.getUsername()).orElseThrow();
        taskService.createOrUpdateBase(baseModel, user);
        return "edit";
    }

    @GetMapping("/main/all/notcompleted")
    public String getAllBaseNotComplete(Model model, @AuthenticationPrincipal UserDetails userDetails) {
        User user = userService.getUserByEmail(userDetails.getUsername()).orElseThrow();
        model.addAttribute("all", taskService.findByActive(false, user));
        return "all";
    }

    @GetMapping("/main/edit/{id}")
    public String editBase(@ModelAttribute("base") Task baseModel) {
        return "/edit";
    }


    @GetMapping("/main/all/completed")
    public String getAllBaseComplete(Model model, @AuthenticationPrincipal UserDetails userDetails) {
        User user = userService.getUserByEmail(userDetails.getUsername()).orElseThrow();
        model.addAttribute("all", taskService.findByActive(true, user));
        return "all";
    }


    @GetMapping("all/doneTask/{id}")
    public String donetask(@PathVariable Integer id, Model model, @AuthenticationPrincipal UserDetails userDetails) {
        User user = userService.getUserByEmail(userDetails.getUsername()).orElseThrow();
        Task task = taskService.getBaseById(id, user);
        task.setDone(true);
        taskService.createOrUpdateBase(task, user);
        model.addAttribute("all", taskService.getAllBase(user));
        return "all";
    }

    @GetMapping("main/all/all")
    public String allAll(Model model, @AuthenticationPrincipal UserDetails userDetails) {
        User user = userService.getUserByEmail(userDetails.getUsername()).orElseThrow();
        List<Task> bases = taskService.getAllBase(user);
        List<TaskArchive> archiveTasks = archiveService.getAllArchive(user);
        model.addAttribute("all", bases);
        model.addAttribute("archive", archiveTasks);
        return "all";
    }

    @GetMapping("/main/all/forTime")
    public String getAllBaseTime(Model model, @RequestParam(required = false) LocalDateTime date, @AuthenticationPrincipal UserDetails userDetails) {
        User user = userService.getUserByEmail(userDetails.getUsername()).orElseThrow();
        model.addAttribute("all", taskService.findByTime(date, user));
        return "all";
    }

    @GetMapping("/main/all/forCategory")
    public String getAllForCategory(Model model, @RequestParam(required = false) Category category, @AuthenticationPrincipal UserDetails userDetails) {
        User user = userService.getUserByEmail(userDetails.getUsername()).orElseThrow();
        model.addAttribute("all", taskService.findByCategory(category, user));
        return "all";
    }

    @GetMapping("/main/all/forName")
    public String getAllBaseName(Model model, @RequestParam(required = false) String search, @AuthenticationPrincipal UserDetails userDetails) {
        User user = userService.getUserByEmail(userDetails.getUsername()).orElseThrow();
        model.addAttribute("all", taskService.findByName(search, user));
        return "all";
    }

    @GetMapping("/main/archive/{id}")
    public String getByIdArchive(@PathVariable int id, Model model, @AuthenticationPrincipal UserDetails userDetails) {
        User user = userService.getUserByEmail(userDetails.getUsername()).orElseThrow();
        TaskArchive taskArchive = archiveService.getArchiveById(id, user);
        model.addAttribute("task", taskArchive);
        return "all";
    }

    @GetMapping("archive/{id}")
    public String archived(@PathVariable int id, @ModelAttribute("base") Task baseModel, @AuthenticationPrincipal UserDetails userDetails) {
        User user = userService.getUserByEmail(userDetails.getUsername()).orElseThrow();
        Task base = taskService.getBaseById(id, user);
        TaskArchive archiveTask = new TaskArchive(base.getId(), base.getTitle(), base.getDescription(), base.getDob(), base.getDone(), base.getPriority(), base.getCategory(), base.getRepeatable(), base.getUser());
        taskService.deleteBaseById(id, user);
        archiveService.createOrUpdateArchive(archiveTask, user);
        return "all";
    }


    @GetMapping("/main/all/allSort")
    public String AllSort(Model model, @AuthenticationPrincipal UserDetails userDetails) {
        User user = userService.getUserByEmail(userDetails.getUsername()).orElseThrow();
        List<Task> bases = taskService.sortByRating(user);
        model.addAttribute("all", bases);
        return "all";
    }


    @Scheduled(initialDelay = 2000, fixedRate = 3000)
    @Async
    public void nextTime(@AuthenticationPrincipal UserDetails userDetails) {
        User user = userService.getUserByEmail(userDetails.getUsername()).orElseThrow();
        List<Task> bases = taskService.findByTime(LocalDateTime.now(), user);
        taskService.nextTime(bases);
    }
}
