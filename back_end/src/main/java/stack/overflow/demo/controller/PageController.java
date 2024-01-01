package stack.overflow.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@Controller
@CrossOrigin(origins = "*",maxAge = 3600)
public class PageController {

    @RequestMapping(value = "/index", method = RequestMethod.GET)
    public ResponseEntity<byte[]> getIndex() throws IOException {
        // 读取 HTML 文件
        Resource resource = new ClassPathResource("index.html");
        byte[] htmlContent = Files.readAllBytes(Path.of(resource.getURI()));

        // 构建响应，设置 content type
        return ResponseEntity.ok()
                .contentType(MediaType.TEXT_HTML)
                .body(htmlContent);
    }
}
