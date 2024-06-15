package com.spacelab.coffeeapp.controller.admin;

import com.spacelab.coffeeapp.entity.Entity2;
import com.spacelab.coffeeapp.repository.EntityRepository;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;


@RequestMapping("/api")
@AllArgsConstructor
@Controller
public class EntityController {



    private final EntityRepository entityRepository;

    @GetMapping({"/", ""})
    public ModelAndView index (){
        return new ModelAndView("example");
    }

    @GetMapping("/entity2")
    @ResponseBody
    public  Page<Entity2> getEntities(@RequestParam(defaultValue = "0") int page,
                                      @RequestParam(defaultValue = "") String search,
                                      @RequestParam(defaultValue = "5") Integer size) {
        Pageable pageable = PageRequest.of(page, size); // 10 элементов на страницу
        if (search.isEmpty()) {
            Page<Entity2> entity = entityRepository.findAll(pageable);
            return entity;
        } else {
            return entityRepository.findByField1ContainingIgnoreCaseOrField2ContainingIgnoreCaseOrField3ContainingIgnoreCase(search, search, search, pageable);
        }
    }

    @GetMapping("entity2/{id}")
    @ResponseBody
    public Entity2 getEntity(@PathVariable Long id) {
        return entityRepository.findById(id).orElseThrow(() -> new RuntimeException("Entity not found"));
    }

    @PostMapping("entity2")
    @ResponseBody
    public Entity2 createEntity(@RequestBody Entity2 entity2) {
        return entityRepository.save(entity2);
    }

    @PutMapping("entity2/{id}")
    @ResponseBody
    public Entity2 updateEntity(@PathVariable Long id, @Valid @RequestBody Entity2 updatedEntity2) {
        return entityRepository.findById(id).map(entity2 -> {
            entity2.setField1(updatedEntity2.getField1());
            entity2.setField2(updatedEntity2.getField2());
            entity2.setField3(updatedEntity2.getField3());
            return entityRepository.save(entity2);
        }).orElseThrow(() -> new RuntimeException("Entity not found"));
    }

    @DeleteMapping("entity2/{id}")
    @ResponseBody
    public void deleteEntity(@PathVariable Long id) {
        entityRepository.deleteById(id);
    }
}
