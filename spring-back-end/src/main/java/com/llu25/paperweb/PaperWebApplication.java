package com.llu25.paperweb;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.LinkedList;
import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@SpringBootApplication
public class PaperWebApplication {

    @GetMapping("/getAllPaper")
    public List<Paper> generateJsonArray() {
        List<Paper> list = new LinkedList<>();
        list.add(new Paper("paper1", "good"));
        list.add(new Paper("paper2", "ok"));
        list.add(new Paper("paper3", "bad"));
        return list;
    }

    public static void main(String[] args) {
        SpringApplication.run(PaperWebApplication.class, args);
    }


}
