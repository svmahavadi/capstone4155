package uncc.code.inspectors.project.cci.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import uncc.code.inspectors.project.cci.entity.CodeInspector;
import uncc.code.inspectors.service.CodeInspectorService;

@RestController
@RequestMapping("/cci")
@CrossOrigin("http://localhost:4200")
public class CodeInspectorController {
    @Autowired
    private CodeInspectorService codeInspectorService;

    @GetMapping("/inspectors")
    public List<CodeInspector> getAll() {
        return codeInspectorService.getCodeInspectors();
    }

    @GetMapping("/inspector")
    public CodeInspector getInspector(@RequestParam Long id) {
        return codeInspectorService.getACodeInspector(id);
    }

    @PostMapping("/new")
    public void createCodeInspector(@RequestBody CodeInspector newCodeInspector) {
        codeInspectorService.createCodeInspector(newCodeInspector);
    }

    @DeleteMapping("/delete")
    public void deleteCodeInspector(@RequestParam Long id) {
        codeInspectorService.deleteCodeInspector(id);
    }

    @PutMapping("/update")
    public CodeInspector updateCodeInspector(@RequestBody CodeInspector updatCodeInspector) {
        return codeInspectorService.updateCodeInspector(updatCodeInspector);
    }

}